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
package hu.axolotl.tasklib.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import hu.axolotl.tasklib.GlobalError;
import hu.axolotl.tasklib.InlineTaskListener;
import hu.axolotl.tasklib.TaskEngine;
import hu.axolotl.tasklib.callback.BaseTaskCallbackRunnable;
import hu.axolotl.tasklib.callback.TRGlobalExceptionRunnable;
import hu.axolotl.tasklib.callback.TRProgressRunnable;
import hu.axolotl.tasklib.callback.TRResultRunnable;
import hu.axolotl.tasklib.descriptor.ClassDescriptor;
import hu.axolotl.tasklib.exception.EngineRuntimeException;
import hu.axolotl.tasklib.util.TaskGlobalUtil;
import hu.axolotl.tasklib.util.TaskLogger;

public abstract class BaseTaskEngineHolder {

    private static final String TAG = BaseTaskEngineHolder.class.getSimpleName();

    public interface SyncFollowCallback {
        void followTaskSynchronised(Class<? extends BaseTask> taskClass);
    }

    private final TaskEngine taskEngine;
    private final Object target;
    private final long sourceId;
    protected final ClassDescriptor classDescriptor;
    private final Queue<BaseTask<?, ?>> earlyTaskQueue = new LinkedList<>();
    private final List<Class<? extends BaseTask>> followedTasks = new ArrayList<>();
    private final Map<Long, InlineTaskListener> innerListeners = new HashMap<>();

    private boolean started = false;

    public BaseTaskEngineHolder(Object target) {
        this.target = target;
        taskEngine = TaskEngine.getInstance();
        classDescriptor = ClassDescriptor.getClassDescriptor(target.getClass());
        sourceId = TaskGlobalUtil.getNextSourceId();
    }

    public void start() {
        innerListeners.clear();
        taskEngine.register(this);
        started = true;
        while (!earlyTaskQueue.isEmpty()) {
            sendTaskToEngine(earlyTaskQueue.remove());
        }
    }

    public void stop() {
        started = false;
        taskEngine.unregister(this);
    }

    protected void beforeTaskExecuted(BaseTask task) {
    }

    public final <T, U> long executeTask(BaseTask<T, U> task) {
        return executeTask(task, null);
    }

    public final <T, U> long executeTask(BaseTask<T, U> task, InlineTaskListener<T, U> taskListener) {
        if (!started) {
            earlyTaskQueue.add(task);
            TaskLogger.d(TAG, "Task not started yet! Only added to queue");
        } else {
            if (taskListener != null) {
                innerListeners.put(task.getId(), taskListener);
            }
            sendTaskToEngine(task);
        }
        return task.getId();
    }

    private <T, U> void sendTaskToEngine(BaseTask<T, U> task) {
        beforeTaskExecuted(task);
        task.setSourceId(sourceId);
        taskEngine.executeTask(task, target);
    }

    public <T, U> boolean followTask(Class<? extends BaseTask<T, U>> taskClass) {
        return taskEngine.followTask(this, taskClass, new SyncFollowCallback() {
            @Override
            public void followTaskSynchronised(Class<? extends BaseTask> taskClass) {
                if (!isFollowedTask(taskClass)) {
                    followedTasks.add(taskClass);
                }
            }
        });
    }

    private boolean needCall(BaseTask<?, ?> task) {
        return task.getSourceId() == sourceId || isFollowedTask(task.getClass());
    }

    private boolean isFollowedTask(Class taskClass) {
        for (Class<? extends BaseTask> followedTask : followedTasks) {
            if (followedTask.getName().equalsIgnoreCase(taskClass.getName())) {
                return true;
            }
        }
        return false;
    }

    public void onTRResult(final BaseTask task) {
        if (!started) {
            return;
        }
        if (needCall(task)) {
            TaskLogger.d(TAG, getClass().getSimpleName() + ".onTRResult() - task: " + task.getClass().getSimpleName());
            InlineTaskListener innerListener = null;
            if (innerListeners.containsKey(task.getId())) {
                innerListener = innerListeners.remove(task.getId());
            }
            postToProperThread(new TRResultRunnable(classDescriptor, target, task, innerListener));
        } else {
            TaskLogger.v(TAG, getClass().getSimpleName() + ".onTRResult() - SKIPPED: " + task.getClass().getSimpleName());
        }
    }

    public void onTRProgress(final BaseTask task, final Object progress) {
        if (!started) {
            return;
        }
        if (task.isRunning()) {
            if (needCall(task)) {
                TaskLogger.d(TAG, getClass().getSimpleName() + ".onTRProgress() - task: " + task.getClass().getSimpleName());
                postToProperThread(new TRProgressRunnable(classDescriptor, target, task, progress));
            } else {
                TaskLogger.v(TAG, getClass().getSimpleName() + ".onTRProgress() - SKIPPED: " + task.getClass().getSimpleName());
            }
        } else {
            TaskLogger.e(TAG, "Task is not running");
        }
    }

    public boolean onTRGlobalException(BaseTask task, GlobalError globalError) {
        if (!started) {
            return false;
        }
        final BlockingQueue<Boolean> queue = new LinkedBlockingQueue<>(1);
        TaskLogger.v(TAG, "onTRGlobalException before postToProperThread");
        postToProperThread(new TRGlobalExceptionRunnable(classDescriptor, target, task, globalError, queue));
        TaskLogger.v(TAG, "onTRGlobalException after postToProperThread");
        boolean result;
        try {
            TaskLogger.v(TAG, "onTRGlobalException before queue.take");
            result = queue.take();
            TaskLogger.v(TAG, "onTRGlobalException after queue.take");
        } catch (InterruptedException ex) {
            TaskLogger.exception(TAG, ex);
            throw new EngineRuntimeException("InterruptedException???", ex);
        }
        return result;
    }

    protected abstract void postToProperThread(BaseTaskCallbackRunnable runnable);

    public long getSourceId() {
        return sourceId;
    }

}