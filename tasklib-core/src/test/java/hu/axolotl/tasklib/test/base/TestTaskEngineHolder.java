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
package hu.axolotl.tasklib.test.base;

import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.base.BaseTaskEngineHolder;
import hu.axolotl.tasklib.callback.BaseTaskCallbackRunnable;
import hu.axolotl.tasklib.callback.TRGlobalExceptionRunnable;
import hu.axolotl.tasklib.callback.TRProgressRunnable;
import hu.axolotl.tasklib.callback.TRResultRunnable;
import hu.axolotl.tasklib.util.TaskLogger;

public class TestTaskEngineHolder extends BaseTaskEngineHolder {

    public static final String TAG = TestTaskEngineHolder.class.getSimpleName();

    BaseTarget target;

    public TestTaskEngineHolder(BaseTarget target) {
        super(target);
        this.target = target;
        target.setHolder(this);
    }

    @Override
    protected void beforeTaskExecuted(BaseTask task) {
        TaskLogger.d(TAG, "beforeTaskExecuted");
        target.taskSubmitted();
    }

    @Override
    protected void postToProperThread(BaseTaskCallbackRunnable runnable) {
        runnable.run();
        if (runnable.hasInnerException()) {
            if (runnable instanceof TRResultRunnable) {
                target.exceptionInResultCallback();
            } else if (runnable instanceof TRProgressRunnable) {
                target.exceptionInProgressCallback();
            } else if (runnable instanceof TRGlobalExceptionRunnable) {
                target.exceptionInGlobalErrorCallback();
            } else {
                throw new RuntimeException("Invalid runnable?");
            }
        }
    }

}
