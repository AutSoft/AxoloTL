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

import java.lang.reflect.Method;

import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.stetho.exception.GetParamInvokeException;

public class TaskStethoHeader {

    private static final String PRE_GET_PARAM_METHOD = "getParam";

    static TaskStethoHeader createResponseHeader(String name, Object value) {
        return new TaskStethoHeader(name, value);
    }

    static TaskStethoHeader createTaskRequestHeader(Method method, BaseTask task) {
        try {
            return new TaskStethoHeader(
                    method.getName().replaceFirst(PRE_GET_PARAM_METHOD, ""),
                    method.invoke(task)
            );
        } catch (Exception ex) {
            throw new GetParamInvokeException(ex);
        }
    }

    private String name;
    private Object value;

    private TaskStethoHeader(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getValueStr() {
        return String.valueOf(value);
    }
}
