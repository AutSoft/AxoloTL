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
package hu.axolotl.tasklib.stetho;

import com.facebook.stetho.inspector.console.CLog;
import com.facebook.stetho.inspector.protocol.module.Console;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hu.axolotl.tasklib.TaskEventListener;

public class TaskLibStetho {

    private static Gson gsonInstance;

    public static Gson getGsonInstance() {
        return gsonInstance;
    }

    public static void init() {
        init(new GsonBuilder().create());
    }

    public static void init(Gson gsonInstance) {
        CLog.writeToConsole(Console.MessageLevel.LOG, Console.MessageSource.OTHER, "TaskLibStetho.init");
        TaskLibStetho.gsonInstance = gsonInstance;
        TaskEventListener.setInstance(new StethoEventListener());
    }

}
