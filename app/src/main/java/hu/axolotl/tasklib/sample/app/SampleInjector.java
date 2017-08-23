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
package hu.axolotl.tasklib.sample.app;

import hu.axolotl.tasklib.annotation.Injector;
import hu.axolotl.tasklib.sample.app.task.SampleWorkerTaskHelper;

public class SampleInjector {

    // This is just a sample injector. In real cases, use dagger instead!

    @Injector
    public static final SampleInjector injector = new SampleInjector();

    private SampleWorker sampleWorker;

    private SampleWorker getSampleWorker() {
        if (sampleWorker == null) {
            sampleWorker = new SampleWorker();
        }
        return sampleWorker;
    }

    public void inject(SampleWorkerTaskHelper sampleWorkerTaskHelper) {
        sampleWorkerTaskHelper.setWorker(getSampleWorker());
    }
}
