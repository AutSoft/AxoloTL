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
import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.util.TaskLogger;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public abstract class BaseRunTask<T, U> extends BaseTask<T, U> {

    @Override
    public final Flowable<RxTaskMessage<T, U>> createFlowable() {
        return Flowable.create(new FlowableOnSubscribe<RxTaskMessage<T, U>>() {
            @Override
            public void subscribe(final FlowableEmitter<RxTaskMessage<T, U>> e) throws Exception {
                TaskLogger.v(TAG, "subscribe start");
                T innerResult = run(new TaskAgent<U>() {
                    @Override
                    public void publishProgress(U progressObject) {
                        TaskLogger.v(TAG, "onProgress");
                        e.onNext(RxTaskMessage.<T, U>createProgress(progressObject));
                    }
                });
                e.onNext(RxTaskMessage.<T, U>createResult(innerResult));
                e.onComplete();
                TaskLogger.v(TAG, "subscribe end");
            }
        }, BackpressureStrategy.ERROR);
    }

    protected abstract T run(TaskAgent<U> agent);

}
