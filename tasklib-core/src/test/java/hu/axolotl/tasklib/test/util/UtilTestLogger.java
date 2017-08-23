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
package hu.axolotl.tasklib.test.util;

import hu.axolotl.tasklib.util.JavaTaskLogger;

public class UtilTestLogger extends JavaTaskLogger {

    int v = 0;
    int d = 0;
    int e = 0;

    @Override
    protected void logV(String tag, String message) {
        v++;
        super.logV(tag, message);
    }

    @Override
    protected void logD(String tag, String message) {
        d++;
        super.logD(tag, message);
    }

    @Override
    protected void logE(String tag, String message) {
        e++;
        super.logE(tag, message);
    }

    public int getV() {
        return v;
    }

    public int getD() {
        return d;
    }

    public int getE() {
        return e;
    }
}