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
package hu.axolotl.tasklib.test.target;

import hu.axolotl.tasklib.test.base.BaseTarget;
import hu.axolotl.tasklib.test.rx.RxCompletableTask;
import hu.axolotl.tasklib.test.rx.RxFlowableStringTask;
import hu.axolotl.tasklib.test.rx.RxObservableIntegerMultipleResultTask;
import hu.axolotl.tasklib.test.rx.RxObservableIntegerTask;
import hu.axolotl.tasklib.test.rx.RxSingleDoubleTask;
import hu.axolotl.tasklib.test.util.UtilTestLogger;

public class RxTarget extends BaseTarget {

    public static final String TAG = RxTarget.class.getSimpleName();

    void onTaskResult(RxCompletableTask task) {
        UtilTestLogger.d(TAG, "onTaskResult: RxCompletableTask");
        otr(task);
    }

    void onTaskResult(RxFlowableStringTask task) {
        UtilTestLogger.d(TAG, "onTaskResult: RxFlowableStringTask");
        otr(task);
    }

    void onTaskResult(RxObservableIntegerTask task) {
        UtilTestLogger.d(TAG, "onTaskResult: RxObservableIntegerTask");
        otr(task);
    }

    void onTaskResult(RxSingleDoubleTask task) {
        UtilTestLogger.d(TAG, "onTaskResult: RxSingleDoubleTask");
        otr(task);
    }

    void onTaskResult(RxObservableIntegerMultipleResultTask task) {
        UtilTestLogger.d(TAG, "onTaskResult: RxObservableIntegerMultipleResultTask");
        otr(task);
    }

}
