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

import java.util.ArrayList;
import java.util.List;

import hu.axolotl.tasklib.GlobalError;
import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.exception.GlobalTaskException;
import hu.axolotl.tasklib.exception.TaskException;
import hu.axolotl.tasklib.test.result.ErrorWrapper;

import static org.junit.Assert.assertEquals;

public class TargetTestResult {

    private boolean submittedAssert = false;
    int submittedCount = 0;

    private boolean resultAssert = false;
    Object lastResult = null;
    int resultCount = 0;

    private boolean progressAssert = false;
    List<Object> progressObjects = new ArrayList<>();

    private ErrorWrapper taskError;
    private ErrorWrapper globalError;

    private boolean exceptionCounterAssert = false;
    int exceptionInResultCallback = 0;
    int exceptionInProgressCallback = 0;
    int exceptionInGlobalErrorCallback = 0;

    public TargetTestResult() {
        taskError = new ErrorWrapper(TaskException.class);
        globalError = new ErrorWrapper(GlobalTaskException.class);
    }

    public TargetTestResult(TargetTestResult other) {
        submittedCount = other.submittedCount;
        resultCount = other.resultCount;
        lastResult = other.lastResult;
        progressObjects = new ArrayList<>(other.progressObjects);
        taskError = new ErrorWrapper(other.taskError);
        globalError = new ErrorWrapper(other.globalError);
        exceptionInResultCallback = other.exceptionInResultCallback;
        exceptionInProgressCallback = other.exceptionInProgressCallback;
        exceptionInGlobalErrorCallback = other.exceptionInGlobalErrorCallback;
    }

    public void handleTaskError(BaseTask task) {
        taskError.handleError(task.getErrorCode(), task.getErrorObject(), task.getException());
    }

    public void handleGlobalError(GlobalError error) {
        globalError.handleError(error.getErrorCode(), error.getErrorObject(), error.getTask().getException());
    }

    public TargetTestResult assertSubmittedCount(int expectedSubmittedCount) {
        submittedAssert = true;
        assertEquals(expectedSubmittedCount, submittedCount);
        return this;
    }

    public TargetTestResult assertResultCount(int expectedResultCount) {
        return assertResultCount(expectedResultCount, null);
    }

    public TargetTestResult assertResultCount(int expectedResultCount, Object expectedLastResult) {
        resultAssert = true;
        assertEquals(expectedResultCount, resultCount);
        if (expectedLastResult != null) {
            assertEquals(expectedLastResult, lastResult);
        }
        return this;
    }

    public TargetTestResult assertProgressOrdered(Object... expectedProgressObjects) {
        progressAssert = true;
        assertEquals(expectedProgressObjects.length, progressObjects.size());
        for (int i = 0; i < expectedProgressObjects.length; i++) {
            assertEquals(expectedProgressObjects[i], progressObjects.get(i));
        }
        return this;
    }

    public TargetTestResult assertNoProgress() {
        assertProgressOrdered();
        return this;
    }

    public TargetTestResult assertTaskError(int expectedErrorCode) {
        taskError.assertErrorCode(expectedErrorCode);
        return this;
    }

    public TargetTestResult assertTaskError(int expectedErrorCode, Object expectedErrorObject) {
        taskError.assertErrorCodeWithObject(expectedErrorCode, expectedErrorObject);
        return this;
    }

    public TargetTestResult assertTaskErrorFromGlobal(int expectedErrorCode) {
        taskError.assertErrorWithCustomException(expectedErrorCode, GlobalTaskException.class);
        return this;
    }

    public TargetTestResult assertTaskError(Class expectedExceptionClass) {
        taskError.assertErrorWithCustomException(expectedExceptionClass);
        return this;
    }

    public TargetTestResult assertGlobalError(int expectedLastGlobalErrorCode) {
        globalError.assertErrorCode(expectedLastGlobalErrorCode);
        return this;
    }

    public TargetTestResult assertGlobalError(int expectedLastGlobalErrorCode, Object errorObject) {
        globalError.assertErrorCodeWithObject(expectedLastGlobalErrorCode, errorObject);
        return this;
    }

    public TargetTestResult assertExceptionInCallbacks(int expectedExceptionInResult, int expectedExceptionInProgress, int expectedExceptionInGlobalError) {
        exceptionCounterAssert = true;
        assertEquals(expectedExceptionInResult, exceptionInResultCallback);
        assertEquals(expectedExceptionInProgress, exceptionInProgressCallback);
        assertEquals(expectedExceptionInGlobalError, exceptionInGlobalErrorCallback);
        return this;
    }

    public void assertOther() {
        if (!submittedAssert) {
            assertSubmittedCount(0);
        }
        if (!resultAssert) {
            assertResultCount(0);
        }
        if (!progressAssert) {
            assertNoProgress();
        }
        taskError.handleIfNotHandled();
        globalError.handleIfNotHandled();
        if (!exceptionCounterAssert) {
            assertExceptionInCallbacks(0, 0, 0);
        }
    }


}
