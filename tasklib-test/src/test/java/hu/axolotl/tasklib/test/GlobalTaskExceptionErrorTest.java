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

import hu.axolotl.tasklib.exception.GlobalTaskException;
import hu.axolotl.tasklib.exception.TaskException;

import static hu.axolotl.tasklib.test.TestObject.TEST_ERROR_CODE;
import static hu.axolotl.tasklib.test.TestObject.TEST_ERROR_CODE_OTHER;

public class GlobalTaskExceptionErrorTest extends TestBase {

    @Test
    public void notTaskException() {
        assertMatcherError(TaskExceptionMatcher.globalCodeWithoutObjectCheck(TEST_ERROR_CODE),
                new RuntimeException("test..."),
                DescriptionBuilder.exceptionClassErrorStr(GlobalTaskException.class, RuntimeException.class));
    }

    @Test
    public void notTaskExceptionButGlobal() {
        assertMatcherError(TaskExceptionMatcher.globalCodeWithoutObjectCheck(TEST_ERROR_CODE),
                new TaskException(TEST_ERROR_CODE_OTHER),
                DescriptionBuilder.exceptionClassErrorStr(GlobalTaskException.class, TaskException.class));
    }

    @Test
    public void code() {
        assertMatcherError(TaskExceptionMatcher.globalCodeWithoutObjectCheck(TEST_ERROR_CODE),
                new GlobalTaskException(TEST_ERROR_CODE_OTHER),
                DescriptionBuilder.errorCodeMismatchStr(TEST_ERROR_CODE, TEST_ERROR_CODE_OTHER));
    }

    @Test
    public void objectNullActual() {
        TestObject expected = TestObject.create();
        TestObject actual = null;
        assertMatcherError(TaskExceptionMatcher.globalCodeWithObject(TEST_ERROR_CODE, expected),
                new GlobalTaskException(TEST_ERROR_CODE, actual),
                DescriptionBuilder.errorObjectMismatchStr(expected, actual));
    }

    @Test
    public void objectNullExpected() {
        TestObject expected = null;
        TestObject actual = TestObject.createModified();
        assertMatcherError(TaskExceptionMatcher.globalCodeWithObject(TEST_ERROR_CODE, expected),
                new GlobalTaskException(TEST_ERROR_CODE, actual),
                DescriptionBuilder.errorObjectMismatchStr(expected, actual));
    }

    @Test
    public void object() {
        TestObject expected = TestObject.create();
        TestObject actual = TestObject.createModified();
        assertMatcherError(TaskExceptionMatcher.globalCodeWithObject(TEST_ERROR_CODE, expected),
                new GlobalTaskException(TEST_ERROR_CODE, actual),
                DescriptionBuilder.errorObjectMismatchStr(expected, actual));
    }


}
