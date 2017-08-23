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
package hu.axolotl.tasklib.descriptor;

import java.lang.reflect.Method;

import hu.axolotl.tasklib.util.TaskLogger;

public abstract class MethodDescriptor {

    private static final String TAG = MethodDescriptor.class.getSimpleName();

    private Class requestClass;
    protected Method method;

    public MethodDescriptor(Class requestClass, Method method) {
        this.requestClass = requestClass;
        this.method = method;
    }

    protected Object invokeMethod(Object object, Object... args) throws InvokeException {
        try {
            method.setAccessible(true);
            Object ret = method.invoke(object, args);
            TaskLogger.v(TAG, "After invokeMethod...");
            return ret;
        } catch (Exception e) {
            TaskLogger.e(TAG, "invokeMethod " + getMethodName() + "(" + requestClass.getSimpleName() + ")  exception - Exception", e);
            throw new InvokeException(requestClass, method, e);
        }
    }

    public String getMethodName() {
        return method.getName();
    }

    public Class getRequestClass() {
        return requestClass;
    }
}