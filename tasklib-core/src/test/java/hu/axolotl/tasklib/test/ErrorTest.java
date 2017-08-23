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

import hu.axolotl.tasklib.exception.InvalidTaskErrorCodeException;
import hu.axolotl.tasklib.test.base.BaseTarget;
import hu.axolotl.tasklib.test.target.GlobalErrorHandledTarget;
import hu.axolotl.tasklib.test.target.GlobalErrorNotHandledTarget;
import hu.axolotl.tasklib.test.target.InnerExceptionTarget;
import hu.axolotl.tasklib.test.target.InvalidTaskErrorCodeTarget;
import hu.axolotl.tasklib.test.target.TaskErrorTarget;
import hu.axolotl.tasklib.test.tasks.GlobalErrorTask;
import hu.axolotl.tasklib.test.tasks.InnerExceptionTask;
import hu.axolotl.tasklib.test.tasks.InvalidTaskErrorCodeTask;
import hu.axolotl.tasklib.test.tasks.TaskErrorTask;

public class ErrorTest extends TaskTestBase {

    @Test
    public void invalidTaskErrorCode() {
        BaseTarget target = executeTask(new InvalidTaskErrorCodeTarget(), new InvalidTaskErrorCodeTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertTaskError(InvalidTaskErrorCodeException.class)
                .assertOther();
    }

    @Test
    public void innerException() {
        BaseTarget target = executeTask(new InnerExceptionTarget(), new InnerExceptionTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertTaskError(InnerExceptionTask.CustomInnerException.class)
                .assertOther();
    }

    @Test
    public void taskError() {
        BaseTarget target = executeTask(new TaskErrorTarget(), new TaskErrorTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertTaskError(TaskErrorTask.TASK_ERROR_CODE)
                .assertOther();
    }

    @Test
    public void taskErrorWithObject() {
        Object errorObject = 121;
        BaseTarget target = executeTask(new TaskErrorTarget(), new TaskErrorTask(errorObject));
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertTaskError(TaskErrorTask.TASK_ERROR_CODE, errorObject)
                .assertOther();
    }

    @Test
    public void globalErrorHandled() {
        BaseTarget target = executeTask(new GlobalErrorHandledTarget(), new GlobalErrorTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(0)
                .assertGlobalError(GlobalErrorTask.TASK_GLOBAL_ERROR_CODE)
                .assertOther();
    }

    @Test
    public void globalErrorWithObjectHandled() {
        Object errorObject = 109;
        BaseTarget target = executeTask(new GlobalErrorHandledTarget(), new GlobalErrorTask(errorObject));
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(0)
                .assertGlobalError(GlobalErrorTask.TASK_GLOBAL_ERROR_CODE, errorObject)
                .assertOther();
    }

    @Test
    public void globalErrorHandledInSecondTarget() {
        BaseTarget target1 = createHolderAndStart(new GlobalErrorNotHandledTarget());
        BaseTarget target2 = createHolderAndStart(new GlobalErrorHandledTarget());
        target1.executeTask(new GlobalErrorTask());
        waitForHoldersAndStop();
        target1.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(0)
                .assertOther();
        target2.createTargetTestResult()
                .assertSubmittedCount(0)
                .assertResultCount(0)
                .assertGlobalError(GlobalErrorTask.TASK_GLOBAL_ERROR_CODE)
                .assertOther();
    }

    @Test
    public void globalErrorHandledInSourceTarget() {
        BaseTarget target1 = createHolderAndStart(new GlobalErrorHandledTarget());
        BaseTarget target2 = createHolderAndStart(new GlobalErrorHandledTarget());
        target2.executeTask(new GlobalErrorTask());
        waitForHoldersAndStop();
        target1.createTargetTestResult()
                .assertSubmittedCount(0)
                .assertResultCount(0)
                .assertOther();
        target2.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(0)
                .assertGlobalError(GlobalErrorTask.TASK_GLOBAL_ERROR_CODE)
                .assertOther();
    }

    @Test
    public void globalErrorNotHandled() {
        BaseTarget target = executeTask(new GlobalErrorNotHandledTarget(), new GlobalErrorTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertTaskErrorFromGlobal(GlobalErrorTask.TASK_GLOBAL_ERROR_CODE)
                .assertOther();
    }


}
