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

import hu.axolotl.tasklib.GlobalError;
import hu.axolotl.tasklib.InlineTaskListener;
import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.test.util.TestHolderUtil;

public class BaseTarget extends BaseTargetTestResultHolder {

    public static final String TAG = BaseTarget.class.getSimpleName();

    TestTaskEngineHolder holder;

    public BaseTarget() {
        TestHolderUtil.getInstance().registerTarget(this);
    }

    public void setHolder(TestTaskEngineHolder holder) {
        this.holder = holder;
    }

    public void startHolder() {
        holder.start();
    }

    public void stopHolder() {
        holder.stop();
    }

    protected boolean onTaskGlobalError(GlobalError error) {
        boolean handled = handleGlobalError(error);
        if (handled) {
            targetTestResult.handleGlobalError(error);
        }
        return handled;
    }

    protected boolean handleGlobalError(GlobalError error) {
        return true;
    }

    public void executeTask(BaseTask task) {
        holder.executeTask(task);
    }

    public void executeTask(BaseTask task, InlineTaskListener<String, String> inlineTaskListener) {
        holder.executeTask(task, inlineTaskListener);
    }

    public <T, U> boolean subscribeTask(Class<? extends BaseTask<T, U>> taskClass) {
        return holder.followTask(taskClass);
    }

    public void taskSubmitted() {
        targetTestResult.submittedCount++;
    }

    public void exceptionInResultCallback() {
        targetTestResult.exceptionInResultCallback++;
    }

    public void exceptionInProgressCallback() {
        targetTestResult.exceptionInProgressCallback++;
    }

    public void exceptionInGlobalErrorCallback() {
        targetTestResult.exceptionInGlobalErrorCallback++;
    }
}