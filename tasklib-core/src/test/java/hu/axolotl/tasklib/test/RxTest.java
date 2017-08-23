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

import hu.axolotl.tasklib.base.BaseTaskSubscriber;
import hu.axolotl.tasklib.test.base.BaseTarget;
import hu.axolotl.tasklib.test.rx.RxCompletableTask;
import hu.axolotl.tasklib.test.rx.RxFlowableStringTask;
import hu.axolotl.tasklib.test.rx.RxObservableIntegerMultipleResultTask;
import hu.axolotl.tasklib.test.rx.RxObservableIntegerTask;
import hu.axolotl.tasklib.test.rx.RxSingleDoubleTask;
import hu.axolotl.tasklib.test.target.RxTarget;
import hu.axolotl.tasklib.test.tasks.RxTestTask;
import hu.axolotl.tasklib.test.util.UtilTestLogger;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.TestSubscriber;

public class RxTest extends TaskTestBase {

    public static final String TAG = RxTest.class.getSimpleName();

    @Test
    public void flowable() {
        RxTestTask.create()
                .subscribeOn(Schedulers.io())
                .subscribe(new TestSubscriber<>(new BaseTaskSubscriber<String, String>() {
                    @Override
                    protected void onTaskProgress(String progress) {
                        UtilTestLogger.d(TAG, "onTaskProgress");
                    }

                    @Override
                    protected void onTaskResult(String resultObject) {
                        UtilTestLogger.d(TAG, "onTaskResult");
                    }

                    @Override
                    protected void onTaskError(boolean global, int errorCode, Object errorObject, Throwable throwable) {
                        UtilTestLogger.d(TAG, "onTaskError");
                    }
                }));
        UtilTestLogger.d(TAG, "afterSubscribe");
        try {
            Thread.sleep(500);
        } catch (Exception ex) {
        }
    }

    @Test
    public void observableIntegerTask() {
        BaseTarget target = executeTask(new RxTarget(), new RxObservableIntegerTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1, RxObservableIntegerTask.RESULT)
                .assertOther();
    }

    @Test
    public void singleDoubleTask() {
        BaseTarget target = executeTask(new RxTarget(), new RxSingleDoubleTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1, RxSingleDoubleTask.RESULT)
                .assertOther();
    }

    @Test
    public void flowableStringTask() {
        BaseTarget target = executeTask(new RxTarget(), new RxFlowableStringTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1, RxFlowableStringTask.RESULT)
                .assertOther();
    }

    @Test
    public void completableTask() {
        BaseTarget target = executeTask(new RxTarget(), new RxCompletableTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1, null)
                .assertOther();
    }

    @Test
    public void observableIntegerMultipleResultTask() {
        BaseTarget target = executeTask(new RxTarget(), new RxObservableIntegerMultipleResultTask());
        target.createTargetTestResult()
                .assertSubmittedCount(1)
                .assertResultCount(1, new Integer(1))
                .assertOther();

        // TODO: check error messages in log!
    }

}
