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

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import hu.axolotl.taskcompiler.test.util.TestBase;
import hu.axolotl.taskcompiler.test.util.TestJavaUtils;
import hu.axolotl.taskcompiler.test.util.TestUtils;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class TaskTest extends TestBase {

    @Test
    public void simpleWorker() {
        assertAbout(javaSources())
                .that(TestJavaUtils.withGeneral("good/SimpleWorker.java"))
                .processedWith(TestUtils.getProcessors())
                .compilesWithoutError()
                .and()
                .generatesSources(
                        JavaFileObjects.forResource("expected/SimpleWorkerTaskHelper.java"),
                        JavaFileObjects.forResource("expected/SimpleTask.java")
                );
    }

    @Test
    public void simpleTask() {
        assertWithComplexWorker("SimpleTask");
    }

    @Test
    public void simpleWithIntReturnTask() {
        assertWithComplexWorker("SimpleWithIntReturnTask");
    }

    @Test
    public void simpleWithObjectReturnTask() {
        assertWithComplexWorker("SimpleWithObjectReturnTask");
    }

    @Test
    public void unnamedPackage() {
        assertAbout(javaSources())
                .that(TestJavaUtils.withGeneral("good/UnnamedPackageWorker.java"))
                .processedWith(TestUtils.getProcessors())
                .failsToCompile()
                .withErrorContaining("Unnamed package not allowed for worker");
    }

}
