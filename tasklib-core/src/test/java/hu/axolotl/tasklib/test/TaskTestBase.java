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

import org.junit.Before;

import hu.axolotl.tasklib.TaskEngine;
import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.test.base.BaseTarget;
import hu.axolotl.tasklib.test.base.TestTaskEngineHolder;
import hu.axolotl.tasklib.test.util.TestHolderUtil;
import hu.axolotl.tasklib.util.JavaTaskLogger;
import hu.axolotl.tasklib.util.TaskLogger;

public abstract class TaskTestBase {

    protected <T extends BaseTarget> T createHolder(T target) {
        new TestTaskEngineHolder(target);
        return target;
    }

    protected <T extends BaseTarget> T createHolderAndStart(T target) {
        createHolder(target);
        target.startHolder();
        return target;
    }

    protected <T extends BaseTarget> T executeTask(T target, BaseTask... tasks) {
        createHolderAndStart(target);
        for (BaseTask task : tasks) {
            target.executeTask(task);
        }
        waitForHoldersAndStop();
        return target;
    }

    protected void waitForHolders() {
        TestHolderUtil.getInstance().waitUntilFinished();
    }

    protected void waitForHoldersAndStop() {
        waitForHolders();
        TestHolderUtil.getInstance().stopHolders();
    }

    @Before
    public void before() {
        TaskLogger.setCustomInstance(new JavaTaskLogger());
        TaskEngine.getInstance().setTaskEngineListener(new TaskEngine.TaskEngineListener() {

            @Override
            public void onTaskPendingFinished(BaseTask task) {
                TestHolderUtil.getInstance().onTaskPendingFinished();
            }

            @Override
            public void onTaskException(BaseTask task, Throwable exception) {

            }
        });
    }

}
