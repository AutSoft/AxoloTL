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

import hu.axolotl.tasklib.test.target.FollowTarget;
import hu.axolotl.tasklib.test.tasks.ProgressTask;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FollowTest extends TaskTestBase {

    @Test
    public void followResult() {
        FollowTarget target1 = createHolderAndStart(new FollowTarget());
        FollowTarget target2 = createHolderAndStart(new FollowTarget());
        FollowTarget target3 = createHolderAndStart(new FollowTarget());
        target2.subscribeTask(ProgressTask.class);
        target1.executeTask(new ProgressTask());
        waitForHoldersAndStop();

        target1.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertProgressOrdered(1, 2, 3, 4)
                .assertOther();
        target2.createTargetTestResult()
                .assertSubmittedCount(0)
                .assertResultCount(1)
                .assertProgressOrdered(1, 2, 3, 4)
                .assertOther();
        target3.createTargetTestResult()
                .assertSubmittedCount(0)
                .assertResultCount(0)
                .assertOther();
    }

    @Test
    public void followWhenRunning() {
        FollowTarget target1 = createHolderAndStart(new FollowTarget());
        FollowTarget target2 = createHolderAndStart(new FollowTarget());
        FollowTarget target3 = createHolderAndStart(new FollowTarget());
        boolean isRunning_2 = target2.subscribeTask(ProgressTask.class);
        target1.executeTask(new ProgressTask());
        boolean isRunning_3 = target3.subscribeTask(ProgressTask.class);
        waitForHoldersAndStop();

        assertFalse(isRunning_2);
        assertTrue(isRunning_3);
        target1.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertProgressOrdered(1, 2, 3, 4)
                .assertOther();
        target2.createTargetTestResult()
                .assertSubmittedCount(0)
                .assertResultCount(1)
                .assertProgressOrdered(1, 2, 3, 4)
                .assertOther();
        target3.createTargetTestResult()
                .assertSubmittedCount(0)
                .assertResultCount(1)
                .assertProgressOrdered(1, 2, 3, 4)
                .assertOther();
    }

    @Test
    public void followResultFromSameHolder_And_Execute() {
        FollowTarget target1 = createHolderAndStart(new FollowTarget());
        target1.executeTask(new ProgressTask());
        target1.subscribeTask(ProgressTask.class);
        waitForHoldersAndStop();

        target1.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertProgressOrdered(1, 2, 3, 4)
                .assertOther();
    }

    @Test
    public void execute_And_FollowResultFromSameHolder() {
        FollowTarget target1 = createHolderAndStart(new FollowTarget());
        target1.executeTask(new ProgressTask());
        target1.subscribeTask(ProgressTask.class);
        waitForHoldersAndStop();

        target1.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1)
                .assertProgressOrdered(1, 2, 3, 4)
                .assertOther();
    }

}
