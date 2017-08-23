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
package hu.axolotl.tasklib.test.base;


import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.test.tasks.BaseTestTask;
import hu.axolotl.tasklib.util.TaskLogger;

public abstract class BaseTargetTestResultHolder {

    public static final String TAG = BaseTargetTestResultHolder.class.getSimpleName();

    protected TargetTestResult targetTestResult = new TargetTestResult();

    protected final void otp(BaseTestTask task, Object progress) {
        targetTestResult.progressObjects.add(progress);
        TaskLogger.d(TAG, "onTaskProgress (" + targetTestResult.progressObjects.size() + ")");
    }

    protected final void otr(BaseTask task) {
        targetTestResult.resultCount++;
        if (!task.hasError()) {
            targetTestResult.lastResult = task.getResult();
        } else {
            targetTestResult.handleTaskError(task);
        }
    }

    public final TargetTestResult createTargetTestResult() {
        return new TargetTestResult(targetTestResult);
    }

}
