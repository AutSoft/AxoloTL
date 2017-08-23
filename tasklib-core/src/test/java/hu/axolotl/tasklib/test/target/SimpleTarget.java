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
import hu.axolotl.tasklib.test.tasks.SimpleTask;
import hu.axolotl.tasklib.test.util.UtilTestLogger;

public class SimpleTarget extends BaseTarget {

    public static final String TAG = SimpleTarget.class.getSimpleName();

    void onTaskResult(SimpleTask task) {
        UtilTestLogger.d(TAG, "onTaskResult: SimpleTask");
        otr(task);
    }

}
