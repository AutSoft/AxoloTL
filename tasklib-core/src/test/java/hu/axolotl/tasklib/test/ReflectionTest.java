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

import hu.axolotl.tasklib.test.base.ReflectionTestTaskEngineHolder;
import hu.axolotl.tasklib.test.target.test.TestTargetWithGlobal;
import hu.axolotl.tasklib.test.target.test.TestTargetWithGlobalInParent;
import hu.axolotl.tasklib.test.target.test.TestTargetWithoutGlobal;
import hu.axolotl.tasklib.test.target.test.globalerror.TestTargetWithNotBooleanReturnTypeGlobal;
import hu.axolotl.tasklib.test.target.test.globalerror.TestTargetWithParamlessGlobal;
import hu.axolotl.tasklib.test.target.test.globalerror.TestTargetWithTwoParamGlobal;
import hu.axolotl.tasklib.test.target.test.globalerror.TestTargetWithVoidReturnTypeGlobal;
import hu.axolotl.tasklib.test.target.test.globalerror.TestTargetWithWrongParamGlobal;
import hu.axolotl.tasklib.test.tasks.SimpleTask;
import hu.axolotl.tasklib.test.tasks.TestSecondTask;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReflectionTest extends TaskTestBase {

    @Test
    public void globalErrorFunction() {
        {
            ReflectionTestTaskEngineHolder holder = new ReflectionTestTaskEngineHolder(new TestTargetWithoutGlobal());
            assertFalse(holder.hasGlobalErrorMethod());
        }
        {
            ReflectionTestTaskEngineHolder holder = new ReflectionTestTaskEngineHolder(new TestTargetWithGlobal());
            assertTrue(holder.hasGlobalErrorMethod());
        }
        {
            ReflectionTestTaskEngineHolder holder = new ReflectionTestTaskEngineHolder(new TestTargetWithGlobalInParent());
            assertTrue(holder.hasGlobalErrorMethod());
        }
    }

    @Test
    public void resultFunction() {
        {
            ReflectionTestTaskEngineHolder holder = new ReflectionTestTaskEngineHolder(new TestTargetWithoutGlobal());
            assertFalse(holder.hasResultMethod(SimpleTask.class));
            assertFalse(holder.hasResultMethod(TestSecondTask.class));
        }
        {
            ReflectionTestTaskEngineHolder holder = new ReflectionTestTaskEngineHolder(new TestTargetWithGlobal());
            assertTrue(holder.hasResultMethod(SimpleTask.class));
            assertFalse(holder.hasResultMethod(TestSecondTask.class));
        }
        {
            ReflectionTestTaskEngineHolder holder = new ReflectionTestTaskEngineHolder(new TestTargetWithGlobalInParent());
            assertTrue(holder.hasResultMethod(SimpleTask.class));
            assertTrue(holder.hasResultMethod(TestSecondTask.class));
        }
    }

    @Test
    public void progressFunction() {
        {
            ReflectionTestTaskEngineHolder holder = new ReflectionTestTaskEngineHolder(new TestTargetWithoutGlobal());
            assertFalse(holder.hasProgressMethod(SimpleTask.class));
            assertFalse(holder.hasProgressMethod(TestSecondTask.class));
        }
        {
            ReflectionTestTaskEngineHolder holder = new ReflectionTestTaskEngineHolder(new TestTargetWithGlobal());
            assertTrue(holder.hasProgressMethod(SimpleTask.class));
            assertFalse(holder.hasProgressMethod(TestSecondTask.class));
        }
        {
            ReflectionTestTaskEngineHolder holder = new ReflectionTestTaskEngineHolder(new TestTargetWithGlobalInParent());
            assertTrue(holder.hasProgressMethod(SimpleTask.class));
            assertTrue(holder.hasProgressMethod(TestSecondTask.class));
        }
    }

    @Test
    public void invalidGlobalErrorFunctions() {
        assertInvalidTarget(new TestTargetWithParamlessGlobal(), "Invalid args for GlobalError method");
        assertInvalidTarget(new TestTargetWithTwoParamGlobal(), "Invalid args for GlobalError method");
        assertInvalidTarget(new TestTargetWithWrongParamGlobal(), "Invalid arg class", "(instead of: class hu.axolotl.tasklib.GlobalError)");
        assertInvalidTarget(new TestTargetWithVoidReturnTypeGlobal(), "Invalid return type", "(must be boolean)");
        assertInvalidTarget(new TestTargetWithNotBooleanReturnTypeGlobal(), "Invalid return type", "(must be boolean)");
    }

    private void assertInvalidTarget(Object target, String... expectedErrorContains) {
        boolean hasException = false;
        try {
            new ReflectionTestTaskEngineHolder(target);
        } catch (Exception ex) {
            hasException = true;
            assertNotNull(ex.getMessage());
            for (String contains : expectedErrorContains) {
                assertTrue("Exception message not contains: " + contains + "\nMessage: " + ex.getMessage(),
                        ex.getMessage().contains(contains));
            }
        }
        assertTrue(hasException);
    }

}
