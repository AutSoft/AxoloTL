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
package hu.axolotl.tasklib.test;

import org.junit.Test;

import hu.axolotl.tasklib.test.base.BaseTarget;
import hu.axolotl.tasklib.test.target.ExceptionInHandlersTarget;
import hu.axolotl.tasklib.test.target.GlobalErrorExceptionTarget;
import hu.axolotl.tasklib.test.tasks.GlobalErrorTask;
import hu.axolotl.tasklib.test.tasks.ProgressTask;

public class ExceptionsInHandlersTest extends TaskTestBase {

    @Test
    public void exceptionInResult() {
        BaseTarget target = executeTask(new ExceptionInHandlersTarget(false, true), new ProgressTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertProgressOrdered(ProgressTask.getProgresses())
                .assertExceptionInCallbacks(1, 0, 0)
                .assertOther();
    }

    @Test
    public void exceptionInProgress() {
        BaseTarget target = executeTask(new ExceptionInHandlersTarget(true, false), new ProgressTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertExceptionInCallbacks(0, ProgressTask.getProgresses().length, 0)
                .assertOther();
    }

    @Test
    public void exceptionInProgressAndResult() {
        BaseTarget target = executeTask(new ExceptionInHandlersTarget(true, true), new ProgressTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertExceptionInCallbacks(1, ProgressTask.getProgresses().length, 0)
                .assertOther();
    }

    @Test
    public void exceptionInGlobalErrorHandler() {
        BaseTarget target = executeTask(new GlobalErrorExceptionTarget(), new GlobalErrorTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertExceptionInCallbacks(0, 0, 1)
                .assertTaskErrorFromGlobal(GlobalErrorTask.TASK_GLOBAL_ERROR_CODE)
                .assertOther();
    }


}
