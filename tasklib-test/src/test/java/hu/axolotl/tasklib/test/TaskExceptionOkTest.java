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

import hu.axolotl.tasklib.exception.TaskException;

import static hu.axolotl.tasklib.test.TestObject.TEST_ERROR_CODE;

public class TaskExceptionOkTest extends TestBase {

    @Test
    public void code() {
        assertMatcherOk(TaskExceptionMatcher.codeWithoutObjectCheck(TEST_ERROR_CODE),
                new TaskException(TEST_ERROR_CODE));
    }

    @Test
    public void codeWithNullObject() {
        assertMatcherOk(TaskExceptionMatcher.codeWithNullObject(TEST_ERROR_CODE),
                new TaskException(TEST_ERROR_CODE));
    }

    @Test
    public void codeWithObject() {
        assertMatcherOk(TaskExceptionMatcher.codeWithObject(TEST_ERROR_CODE, TestObject.create()),
                new TaskException(TEST_ERROR_CODE, TestObject.create()));
    }

}
