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
package hu.axolotl.tasklib.test.target;

import hu.axolotl.tasklib.GlobalError;
import hu.axolotl.tasklib.test.base.BaseTarget;
import hu.axolotl.tasklib.test.tasks.GlobalErrorTask;

public class GlobalErrorExceptionTarget extends BaseTarget {

    void onTaskResult(GlobalErrorTask task) {
        otr(task);
    }

    void onTaskProgress(GlobalErrorTask task, Object progress) {
        otp(task, progress);
    }

    @Override
    protected boolean handleGlobalError(GlobalError error) {
        throw new ExceptionInGlobalError();
    }

    public static class ExceptionInGlobalError extends RuntimeException {
    }
}
