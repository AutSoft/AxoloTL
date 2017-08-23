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

import hu.axolotl.tasklib.test.base.BaseInlineTaskListener;
import hu.axolotl.tasklib.test.base.BaseTarget;
import hu.axolotl.tasklib.test.target.SimpleTarget;
import hu.axolotl.tasklib.test.tasks.SimpleTask;

public class BasicTest extends TaskTestBase {

    @Test
    public void simpleTask() {
        BaseTarget target = executeTask(new SimpleTarget(), new SimpleTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1, SimpleTask.class.getSimpleName())
                .assertOther();
    }

    @Test
    public void inlineTaskListener() {
        BaseTarget target = createHolderAndStart(new SimpleTarget());
        BaseInlineTaskListener inlineTaskListener = new BaseInlineTaskListener();
        target.executeTask(new SimpleTask(), inlineTaskListener);
        waitForHoldersAndStop();

        inlineTaskListener.createTargetTestResult()
                .assertResultCount(1)
                .assertOther();
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertOther();
    }

    @Test
    public void executedFromQueueAfterStart() {
        BaseTarget target = createHolder(new SimpleTarget());
        target.executeTask(new SimpleTask());

        target.createTargetTestResult()
                .assertOther();

        waitForHolders();

        target.startHolder();

        waitForHoldersAndStop();

        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertOther();
    }

    @Test
    public void notHandledAfterStop() {
        BaseTarget target = createHolder(new SimpleTarget());

        target.createTargetTestResult()
                .assertOther();

        waitForHoldersAndStop();

        target.executeTask(new SimpleTask());

        target.createTargetTestResult()
                .assertSubmittedCount(0)
                .assertResultCount(0)
                .assertOther();
    }

}
