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

import hu.axolotl.tasklib.test.tasks.SimpleTask;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestTaskTest extends TaskTestBase {

    private static final String RESULT = "ThisIsTheResult";
    private static final int ERROR_CODE = 67;
    private static final Object ERROR_OBJECT = new Object();

    @Test
    public void taskTestSuccess() {
        assertTaskSuccess(new SimpleTask());
    }

    @Test
    public void taskTestError() {
        assertTaskError(new SimpleTask());
    }

    @Test
    public void taskTestErrorObject() {
        assertTaskErrorWithObject(new SimpleTask());
    }

    @Test
    public void taskTestMultipleResults() {
        SimpleTask task = new SimpleTask();

        assertTaskSuccess(task);
        assertTaskError(task);
        assertTaskErrorWithObject(task);
        assertTaskSuccess(task);
        assertTaskError(task);
        assertTaskErrorWithObject(task);
    }

    private void assertTaskSuccess(SimpleTask task) {
        task.testWithSuccess(RESULT);

        assertFalse(task.hasError());
        assertEquals(RESULT, task.getResult());
    }

    private void assertTaskError(SimpleTask task) {
        task.testWithError(ERROR_CODE);

        assertTrue(task.hasError());
        assertEquals(ERROR_CODE, task.getErrorCode());
    }

    private void assertTaskErrorWithObject(SimpleTask task) {
        task.testWithError(ERROR_CODE, ERROR_OBJECT);

        assertTrue(task.hasError());
        assertEquals(ERROR_CODE, task.getErrorCode());
        assertEquals(ERROR_OBJECT, task.getErrorObject());
    }

}
