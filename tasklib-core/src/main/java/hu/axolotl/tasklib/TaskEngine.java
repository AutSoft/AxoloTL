/*
 * Copyright (C) 2017 AutSoft Kft.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hu.axolotl.tasklib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.base.BaseTaskEngineHolder;
import hu.axolotl.tasklib.util.TaskLogger;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

public final class TaskEngine implements TaskSubscriber.TaskSubscriberListener {

    public static final String TAG = "TaskEngine";

    public interface TaskEngineListener {
        void onTaskPendingFinished(BaseTask task);

        void onTaskException(BaseTask task, Throwable throwable);
    }

    private static TaskEngine instance = new TaskEngine();

    public static TaskEngine getInstance() {
        return instance;
    }

    private Scheduler observeOnScheduler;
    private TaskEngineListener taskEngineListener;
    private final List<BaseTaskEngineHolder> holders = new ArrayList<>();

    private final List<BaseTask> runningTasks = new ArrayList<>();
    private final List<BaseTask> pendingTasks = new ArrayList<>();
    private final Map<String, BaseTask> stickyResultTasks = new HashMap<>();

    private TaskEngine() {
        observeOnScheduler = Schedulers.from(Executors.newSingleThreadExecutor());
    }

    public void setTaskEngineListener(TaskEngineListener taskEngineListener) {
        this.taskEngineListener = taskEngineListener;
    }


    public void register(BaseTaskEngineHolder holder) {
        synchronized (runningTasks) {
            holders.add(holder);
            TaskEventListener.notifyHolderRegistered(holder);
        }
    }

    public void unregister(BaseTaskEngineHolder holder) {
        synchronized (runningTasks) {
            holders.remove(holder);
            TaskEventListener.notifyHolderDeregistered(holder);
        }
    }

    public <T, U> void executeTask(BaseTask<T, U> task, Object sourceAndTarget) {
        synchronized (runningTasks) {
            onTaskAdded(task);
            TaskEventListener.notifyExecuteCalled(task, sourceAndTarget);
            task.createFlowable()
                    .subscribeOn(task.getScheduler())
                    .observeOn(observeOnScheduler)
                    .subscribe(new TestSubscriber<>(task.createSubscriber(this)));
        }
    }

    public <T, U> boolean followTask(BaseTaskEngineHolder holder, Class<? extends BaseTask<T, U>> taskClass, BaseTaskEngineHolder.SyncFollowCallback followCallback) {
        synchronized (runningTasks) {
            TaskEventListener.notifyFollowCalled(taskClass);
            boolean isRunning = isTaskRunning(taskClass);
            followCallback.followTaskSynchronised(taskClass);
            if (!isRunning) {
                BaseTask stickyTask = getStickyTask(taskClass);
                if (stickyTask != null) {
                    holder.onTRResult(stickyTask);
                }
            }
            return isRunning;
        }
    }

    private boolean isTaskRunning(Class<? extends BaseTask> taskClass) {
        boolean isRunning = false;
        for (BaseTask runningTask : runningTasks) {
            if (runningTask.getClass().getName().equalsIgnoreCase(taskClass.getName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    private <T, U> BaseTask getStickyTask(Class<? extends BaseTask<T, U>> taskClass) {
        BaseTask stickyTask = null;
        for (Map.Entry<String, BaseTask> srEntry : stickyResultTasks.entrySet()) {
            if (srEntry.getKey().equalsIgnoreCase(taskClass.getName())) {
                stickyTask = srEntry.getValue();
            }
        }
        return stickyTask;
    }

    @Override
    public void onTRResult(BaseTask task) {
        TaskLogger.v(TAG, "onTRResult: " + task.getClass().getSimpleName());
        List<BaseTaskEngineHolder> orderedHolders;
        synchronized (runningTasks) {
            onTaskFinished(task);
            orderedHolders = getOrderedHolder(task.getSourceId());
        }
        TaskEventListener.notifyResult(task);
        boolean needResultCall = true;
        if (task.hasGlobalError()) {
            needResultCall = !notifyGlobalError(orderedHolders, task);
        }
        if (needResultCall) {
            if (task.hasException() && taskEngineListener != null) {
                taskEngineListener.onTaskException(task, task.getException());
            }
            if (task.hasStickyResult()) {
                stickyResultTasks.put(task.getClass().getName(), task);
            }
            for (BaseTaskEngineHolder holder : orderedHolders) {
                holder.onTRResult(task);
            }
        }
        onTaskPendingFinished(task);
    }

    private boolean notifyGlobalError(List<BaseTaskEngineHolder> orderedHolders, BaseTask task) {
        GlobalError globalError = new GlobalError(task);
        TaskLogger.v(TAG, "notifyGlobalError, holder count: " + orderedHolders.size());
        for (BaseTaskEngineHolder holder : orderedHolders) {
            if (holder.onTRGlobalException(task, globalError)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTRProgress(BaseTask task, Object progress) {
        synchronized (runningTasks) {
            TaskEventListener.notifyProgress(task, progress);
            for (BaseTaskEngineHolder holder : holders) {
                holder.onTRProgress(task, progress);
            }
        }
    }

    private List<BaseTaskEngineHolder> getOrderedHolder(long sourceId) {
        List<BaseTaskEngineHolder> orderedList = new ArrayList<>();
        for (BaseTaskEngineHolder holder : holders) {
            if (holder.getSourceId() == sourceId) {
                orderedList.add(0, holder);
            } else {
                orderedList.add(holder);
            }
        }
        return orderedList;
    }

    private void onTaskAdded(BaseTask task) {
        TaskLogger.v(TAG, "onTaskAdded (r: " + runningTasks.size() + ", p: " + pendingTasks.size() + ")");
        runningTasks.add(task);
        pendingTasks.add(task);
    }

    private void onTaskFinished(BaseTask task) {
        TaskLogger.v(TAG, "onTaskFinished (r: " + runningTasks.size() + ")");
        runningTasks.remove(task);
    }

    protected void onTaskPendingFinished(BaseTask task) {
        TaskLogger.v(TAG, "onTaskPendingFinished (p: " + pendingTasks.size() + ")");
        pendingTasks.remove(task);
        if (taskEngineListener != null) {
            taskEngineListener.onTaskPendingFinished(task);
        }
    }

    public int getPendingTaskCount() {
        return pendingTasks.size();
    }

}
