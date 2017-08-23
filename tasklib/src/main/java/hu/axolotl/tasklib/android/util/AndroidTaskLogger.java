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
package hu.axolotl.tasklib.android.util;

import android.util.Log;

import hu.axolotl.tasklib.util.TaskLogger;

public class AndroidTaskLogger extends TaskLogger {

    @Override
    protected void logV(String tag, String message) {
        Log.v(tag, message);
    }

    @Override
    protected void logD(String tag, String message) {
        Log.d(tag, message);
    }

    @Override
    protected void logE(String tag, String message) {
        Log.e(tag, message);
    }

    @Override
    protected void logE(String tag, String message, Exception exception) {
        Log.e(tag, message, exception);
    }
}
