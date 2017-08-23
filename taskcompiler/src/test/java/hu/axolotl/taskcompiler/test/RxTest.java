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

public class RxTest extends TestBase {

    private void assertRxWorker(String generatedTaskName) {
        assertAbout(javaSources())
                .that(TestJavaUtils.withGeneral("good/RxWorker.java"))
                .processedWith(TestUtils.getProcessors())
                .compilesWithoutError()
                .and()
                .generatesSources(
                        JavaFileObjects.forResource("expected/rx/RxWorkerTaskHelper.java"),
                        JavaFileObjects.forResource("expected/rx/" + generatedTaskName + ".java")
                );
    }

    @Test
    public void integerObservable() {
        assertRxWorker("IntegerObservableTask");
    }

    @Test
    public void integerFlowable() {
        assertRxWorker("IntegerFlowableTask");
    }

    @Test
    public void integerListFlowable() {
        assertRxWorker("StringListFlowableTask");
    }

    @Test
    public void doubleSingle() {
        assertRxWorker("DoubleSingleTask");
    }

    @Test
    public void completable() {
        assertRxWorker("CompletableTask");
    }

}
