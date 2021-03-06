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
package hu.axolotl.tasklib.util;

import java.util.concurrent.atomic.AtomicLong;

public abstract class TaskGlobalUtil {

    private static AtomicLong nextId = new AtomicLong(1);

    public static long getNextSourceId() {
        return nextId.getAndIncrement();
    }

    public static long getNextTaskId() {
        return nextId.getAndIncrement();
    }
}