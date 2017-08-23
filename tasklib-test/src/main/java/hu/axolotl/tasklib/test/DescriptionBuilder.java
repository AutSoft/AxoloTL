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

import org.hamcrest.Description;
import org.hamcrest.StringDescription;

public abstract class DescriptionBuilder {

    public static String exceptionClassErrorStr(Class expected, Class actual) {
        StringDescription sd = new StringDescription();
        exceptionClassError(sd, expected, actual);
        return sd.toString();
    }

    public static void exceptionClassError(Description description, Class expected, Class actual) {
        logValueMismatch(description, "Exception class error", expected, actual);
    }

    public static String errorCodeMismatchStr(int expectedErrorCode, int actualErrorCode) {
        StringDescription sd = new StringDescription();
        errorCodeMismatch(sd, expectedErrorCode, actualErrorCode);
        return sd.toString();
    }

    public static void errorCodeMismatch(Description description, int expectedErrorCode, int actualErrorCode) {
        logValueMismatch(description, "Error code mismatch", expectedErrorCode, actualErrorCode);
    }

    public static String errorObjectMismatchStr(Object expectedErrorObject, Object actualErrorObject) {
        StringDescription sd = new StringDescription();
        errorObjectMismatch(sd, expectedErrorObject, actualErrorObject);
        return sd.toString();
    }

    public static void errorObjectMismatch(Description description, Object expectedErrorObject, Object actualErrorObject) {
        logValueMismatch(description, "Error object mismatch", expectedErrorObject, actualErrorObject);
    }

    private static void logValueMismatch(Description description, String message, Object expected, Object actual) {
        description.appendText(message);
        description.appendText("\n");
        description.appendText("Expected: ");
        description.appendValue(expected);
        description.appendText("\n");
        description.appendText("Actual: ");
        description.appendValue(actual);
        description.appendText("\n");
    }
}
