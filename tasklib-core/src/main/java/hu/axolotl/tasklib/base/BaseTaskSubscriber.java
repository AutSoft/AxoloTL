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

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import hu.axolotl.tasklib.RxTaskMessage;
import hu.axolotl.tasklib.exception.BaseTaskException;
import hu.axolotl.tasklib.exception.GlobalTaskException;
import hu.axolotl.tasklib.util.TaskLogger;

public abstract class BaseTaskSubscriber<T, U> implements Subscriber<RxTaskMessage<T, U>> {

    public static final String LOG_TAG = BaseTaskSubscriber.class.getSimpleName();

    @Override
    public void onSubscribe(Subscription s) {
        TaskLogger.d(LOG_TAG, "onSubscribe");
        onStatusStarted();
    }

    @Override
    public final void onNext(RxTaskMessage<T, U> message) {
        if (message.isProgress()) {
            TaskLogger.d(LOG_TAG, "onNext isProgress");
            onTaskProgress(message.getProgressObject());
        } else {
            TaskLogger.d(LOG_TAG, "onNext isResult");
            onStatusFinished();
            onTaskResult(message.getResultObject());
        }
    }

    @Override
    public final void onError(Throwable t) {
        onStatusFinished();
        if (t instanceof BaseTaskException) {
            BaseTaskException ex = (BaseTaskException) t;
            boolean global = ex instanceof GlobalTaskException;
            TaskLogger.d(LOG_TAG, "onError (global: " + global + ", code:" + ex.getErrorCode()
                    + ", object: " + (ex.getErrorObject() == null ? "null" : ex.getErrorObject().getClass().getName()) + ")");
            onTaskError(global, ex.getErrorCode(), ex.getErrorObject(), t);
        } else {
            TaskLogger.d(LOG_TAG, "onError other: " + t);
            onTaskError(false, Integer.MAX_VALUE, null, t);
        }
    }

    @Override
    public void onComplete() {
        TaskLogger.d(LOG_TAG, "onComplete");
    }

    protected abstract void onTaskProgress(U progress);

    protected abstract void onTaskResult(T resultObject);

    protected abstract void onTaskError(boolean global, int errorCode, Object errorObject, Throwable exception);

    protected void onStatusStarted() {
        // Do nothing (so you don't have to override it all the time)
    }

    protected void onStatusFinished() {
        // Do nothing (so you don't have to override it all the time)
    }

}
