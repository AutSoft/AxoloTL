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

import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.base.BaseTaskEngineHolder;

public abstract class TaskEventListener {

    private static TaskEventListener instance;

    public static void setInstance(TaskEventListener listener) {
        TaskEventListener.instance = listener;
    }

    public static void notifyHolderRegistered(BaseTaskEngineHolder holder) {
        if (instance != null) {
            instance.onHolderRegistered(holder);
        }
    }

    protected abstract void onHolderRegistered(BaseTaskEngineHolder holder);

    public static void notifyHolderDeregistered(BaseTaskEngineHolder holder) {
        if (instance != null) {
            instance.onHolderDeregistered(holder);
        }
    }

    protected abstract void onHolderDeregistered(BaseTaskEngineHolder holder);

    public static void notifyExecuteCalled(BaseTask task, Object sourceAndTarget) {
        if (instance != null) {
            instance.onExecuteCalled(task, sourceAndTarget);
        }
    }

    protected abstract void onExecuteCalled(BaseTask task, Object sourceAndTarget);

    public static void notifySubscribed(BaseTask task) {
        if (instance != null) {
            instance.onSubscribed(task);
        }
    }

    protected abstract void onSubscribed(BaseTask task);

    public static void notifyFollowCalled(Class<? extends BaseTask> taskClass) {
        if (instance != null) {
            instance.onFollowCalled(taskClass);
        }
    }

    protected abstract void onFollowCalled(Class<? extends BaseTask> taskClass);

    public static void notifyProgress(BaseTask task, Object progress) {
        if (instance != null) {
            instance.onProgress(task, progress);
        }
    }

    protected abstract void onProgress(BaseTask task, Object progress);

    public static void notifyResult(BaseTask task) {
        if (instance != null) {
            instance.onResult(task);
        }
    }

    protected abstract void onResult(BaseTask task);

    public static void notifyExceptionInCallback(BaseTask task) {
        if (instance != null) {
            instance.onExceptionInCallback(task);
        }
    }

    protected abstract void onExceptionInCallback(BaseTask task);

}
