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

public class TestObject {

    public static TestObject create() {
        return new TestObject();
    }

    public static TestObject createModified() {
        TestObject testObject = new TestObject();
        testObject.number += 1;
        testObject.text += "!";
        return testObject;
    }

    public static final int TEST_ERROR_CODE = 67;
    public static final int TEST_ERROR_CODE_OTHER = 68;

    int number = 3;
    String text = "TestObjectText";

    private TestObject() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestObject that = (TestObject) o;

        if (number != that.number) return false;
        return text != null ? text.equals(that.text) : that.text == null;

    }
}
