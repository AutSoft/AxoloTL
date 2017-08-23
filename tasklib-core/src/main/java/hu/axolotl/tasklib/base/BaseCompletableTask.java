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
import io.reactivex.Completable;
import io.reactivex.Flowable;

public abstract class BaseCompletableTask extends BaseTask<Void, Void> {

    @Override
    public final Flowable<RxTaskMessage<Void, Void>> createFlowable() {
        return getRunCompletable()
                .andThen(Flowable.just(RxTaskMessage.<Void, Void>createResult(null)));
    }

    protected abstract Completable getRunCompletable();

}
