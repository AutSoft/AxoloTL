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

import hu.axolotl.tasklib.RxTaskMessage;
import hu.axolotl.tasklib.TaskEventListener;
import hu.axolotl.tasklib.TaskSchedulers;
import hu.axolotl.tasklib.TaskSubscriber;
import hu.axolotl.tasklib.util.TaskGlobalUtil;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;

public abstract class BaseTask<T, U> {

    public static final String TAG = BaseTask.class.getSimpleName();

    private long id;
    private long sourceId = 0;

    private static final int STATUS_CREATED = 0;
    private static final int STATUS_RUNNING = 1;
    private static final int STATUS_FINISHED = 2;

    private int status = STATUS_CREATED;

    private T result;
    private boolean errorGlobal = false;
    private int errorCode = 0;
    private Object errorObject = null;
    private Throwable errorThrowable = null;

    private boolean stickyResult = false;

    private Scheduler scheduler;

    BaseTask() {
        id = TaskGlobalUtil.getNextTaskId();
    }

    protected void setSchedulerId(int id) {
        scheduler = TaskSchedulers.getScheduler(id);
    }

    public final long getId() {
        return id;
    }

    public final void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public final long getSourceId() {
        return sourceId;
    }

    public final boolean hasError() {
        return errorCode != 0;
    }

    public boolean hasGlobalError() {
        return errorCode != 0 && errorGlobal;
    }

    public final T getResult() {
        return result;
    }

    public final int getErrorCode() {
        return errorCode;
    }

    public final Object getErrorObject() {
        return errorObject;
    }

    public final Throwable getException() {
        return errorThrowable;
    }

    public abstract Flowable<RxTaskMessage<T, U>> createFlowable();

    public final BaseTaskSubscriber<T, U> createSubscriber(TaskSubscriber.TaskSubscriberListener taskListener) {
        return new TaskSubscriber<T, U>(this, taskListener) {
            @Override
            protected void onStatusStarted() {
                TaskEventListener.notifySubscribed(BaseTask.this);
                status = STATUS_RUNNING;
            }

            @Override
            protected void onStatusFinished() {
                status = STATUS_FINISHED;
            }
        };
    }

    public final BaseTask<T, U> setStickyResult() {
        stickyResult = true;
        return this;
    }

    public final boolean hasStickyResult() {
        return stickyResult;
    }

    public final void setResult(T result) {
        this.result = result;
    }

    public final boolean isRunning() {
        return status == STATUS_RUNNING;
    }

    public final void setError(boolean global, int errorCode, Object errorObject, Throwable throwable) {
        this.errorGlobal = global;
        this.errorCode = errorCode;
        this.errorObject = errorObject;
        this.errorThrowable = throwable;
    }

    private void reset() {
        result = null;
        errorCode = 0;
        errorObject = null;
        errorThrowable = null;
    }

    public final Scheduler getScheduler() {
        if (scheduler == null) {
            setSchedulerId(TaskSchedulers.IO);
        }
        return scheduler;
    }

    public final boolean hasException() {
        return errorThrowable != null;
    }

    public final void testWithSuccess(T result) {
        reset();
        this.result = result;
    }

    public final void testWithError(int errorCode) {
        reset();
        this.errorCode = errorCode;
    }

    public final void testWithError(int errorCode, Object errorObject) {
        testWithError(errorCode);
        this.errorObject = errorObject;
    }
}
