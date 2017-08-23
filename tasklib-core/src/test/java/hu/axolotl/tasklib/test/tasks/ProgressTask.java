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
package hu.axolotl.tasklib.test.tasks;

import hu.axolotl.tasklib.TaskAgent;

public class ProgressTask extends BaseTestTask {

    public static Object[] getProgresses() {
        Object[] ret = new Object[4];
        ret[0] = 1;
        ret[1] = 2;
        ret[2] = 3;
        ret[3] = 4;
        return ret;
    }

    long progressIntervalInMs;

    public ProgressTask() {
        this(20);
    }

    public ProgressTask(long progressIntervalInMs) {
        this.progressIntervalInMs = progressIntervalInMs;
    }

    @Override
    protected String run(TaskAgent agent) {
        waitForProgressInterval();
        for (Object o : getProgresses()) {
            agent.publishProgress(o);
            waitForProgressInterval();
        }
        return getClass().getSimpleName();
    }

    private void waitForProgressInterval() {
        if (progressIntervalInMs > 0) {
            try {
                Thread.sleep(progressIntervalInMs);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
