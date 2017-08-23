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
package hu.axolotl.taskcompiler.test;

import org.junit.Test;

import hu.axolotl.taskcompiler.test.util.TestJavaUtils;
import hu.axolotl.taskcompiler.test.util.TestUtils;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class InjectorTest {

    @Test
    public void missingInjector() {
        assertAbout(javaSources())
                .that(TestJavaUtils.fromResources("good/ComplexWorker.java"))
                .processedWith(TestUtils.getProcessors())
                .failsToCompile()
                .withErrorContaining("Missing static injector!");
    }

    @Test
    public void multipleInjector() {
        assertAbout(javaSources())
                .that(TestJavaUtils.fromResources(
                        "general/StaticInjector.java",
                        "general/TestApplication.java",
                        "general/OtherTestApplication.java",
                        "good/ComplexWorker.java"

                ))
                .processedWith(TestUtils.getProcessors())
                .failsToCompile()
                .withErrorContaining("Static injector already set!");
    }

    @Test
    public void privateInjector() {
        assertAbout(javaSources())
                .that(TestJavaUtils.fromResources(
                        "general/StaticInjector.java",
                        "general/WrongTestApplication.java",
                        "good/ComplexWorker.java"

                ))
                .processedWith(TestUtils.getProcessors())
                .failsToCompile()
                .withErrorContaining("Static injector is not public static!");
    }

}
