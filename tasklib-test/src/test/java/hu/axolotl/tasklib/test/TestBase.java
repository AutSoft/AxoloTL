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

import org.hamcrest.StringDescription;

import static org.junit.Assert.assertEquals;

abstract class TestBase {

    void assertMatcherOk(TaskExceptionMatcher matcher, Exception exception) {
        assertMatcher(false, matcher, exception, "");
    }

    void assertMatcherError(TaskExceptionMatcher matcher, Exception exception, String expectedString) {
        assertMatcher(true, matcher, exception, expectedString);
    }

    private void assertMatcher(boolean error, TaskExceptionMatcher matcher, Exception exception, String expectedString) {
        assertEquals(!error, matcher.matches(exception));
        StringDescription sd = new StringDescription();
        matcher.describeTo(sd);
        assertEquals(expectedString, sd.toString());
    }
}
