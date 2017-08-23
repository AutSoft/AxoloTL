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

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import hu.axolotl.tasklib.GlobalError;
import hu.axolotl.tasklib.exception.EngineRuntimeException;
import hu.axolotl.tasklib.util.TaskLogger;

public class ClassDescriptor {

    public static final String TAG = ClassDescriptor.class.getSimpleName();

    private static final String TASK_RESULT_FUNCTION_NAME = "onTaskResult";
    private static final String TASK_PROGRESS_FUNCTION_NAME = "onTaskProgress";
    private static final String TASK_GLOBAL_ERROR_FUNCTION_NAME = "onTaskGlobalError";

    private static LoadingCache<Class, ClassDescriptor> classDescriptorCache;

    static {
        classDescriptorCache = CacheBuilder.newBuilder()
                .maximumSize(200)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(new CacheLoader<Class, ClassDescriptor>() {
                    @Override
                    public ClassDescriptor load(Class targetClass) {
                        return new ClassDescriptor(targetClass);
                    }
                });
    }

    public static ClassDescriptor getClassDescriptor(Class targetClass) {
        return classDescriptorCache.getUnchecked(targetClass);
    }

    private Class targetClass;
    private Map<Class, ResultMethodDescriptor> resultMethods = new HashMap<>();
    private Map<Class, ProgressMethodDescriptor> progressMethods = new HashMap<>();
    private GlobalErrorMethodDescriptor globalError;

    private ClassDescriptor(Class targetClass) {
        this.targetClass = targetClass;
        parseMethods();
        if (targetClass.getSuperclass() != null) {
            ClassDescriptor superClassDescriptor = getClassDescriptor(targetClass.getSuperclass());
            appendSuper(superClassDescriptor);
        }
    }

    private void appendSuper(ClassDescriptor superClassDescriptor) {
        for (Map.Entry<Class, ResultMethodDescriptor> entry : superClassDescriptor.resultMethods.entrySet()) {
            addTaskResultMethodOrDie(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<Class, ProgressMethodDescriptor> entry : superClassDescriptor.progressMethods.entrySet()) {
            addTaskProgressMethodOrDie(entry.getKey(), entry.getValue());
        }
        if (globalError == null && superClassDescriptor.globalError != null) {
            globalError = superClassDescriptor.globalError;
        }
    }

    private void addTaskResultMethodOrDie(Class requestClass, ResultMethodDescriptor rmd) {
        resultMethods.put(requestClass, rmd);
    }

    private void addTaskProgressMethodOrDie(Class taskClass, ProgressMethodDescriptor pmd) {
        progressMethods.put(taskClass, pmd);
    }

    private void parseMethods() {
        for (Method method : targetClass.getDeclaredMethods()) {
            if (TASK_RESULT_FUNCTION_NAME.equals(method.getName())) {
                parseTaskResultMethod(method);
            }
            if (TASK_PROGRESS_FUNCTION_NAME.equals(method.getName())) {
                parseTaskProgressMethod(method);
            }
            if (TASK_GLOBAL_ERROR_FUNCTION_NAME.equals(method.getName())) {
                parseGlobalErrorMethod(method);
            }
        }
        logCallbackMethods();
    }

    private void parseTaskResultMethod(Method method) {
        Class[] paramClasses = method.getParameterTypes();
        if (paramClasses.length != 1) {
            throw new EngineRuntimeException("Invalid args for TaskResult method: " + targetClass.getSimpleName() + "." + method.getName());
        }
        addTaskResultMethodOrDie(paramClasses[0], new ResultMethodDescriptor(paramClasses[0], method));
    }

    private void parseTaskProgressMethod(Method method) {
        Class[] paramClasses = method.getParameterTypes();
        if (paramClasses.length != 2) {
            throw new EngineRuntimeException("Invalid args for TaskProgress method: " + targetClass.getSimpleName() + "." + method.getName());
        }
        Class requestClass = paramClasses[0];
        addTaskProgressMethodOrDie(requestClass, new ProgressMethodDescriptor(requestClass, method));
    }

    private void parseGlobalErrorMethod(Method method) {
        Class[] paramClasses = method.getParameterTypes();
        if (paramClasses.length != 1) {
            throw new EngineRuntimeException("Invalid args for GlobalError method: " + targetClass.getSimpleName() + "." + method.getName());
        }
        Class paramClass = paramClasses[0];
        if (!paramClass.equals(GlobalError.class)) {
            throw new EngineRuntimeException("Invalid arg class: " + paramClass.getName() + " (instead of: " + GlobalError.class + ")");
        }
        if (!method.getReturnType().equals(boolean.class)) {
            throw new EngineRuntimeException("Invalid return type: " + method.getReturnType().getName() + " (must be boolean)");
        }
        globalError = new GlobalErrorMethodDescriptor(method);
    }

    public ResultMethodDescriptor getResultMethod(Class taskClass) {
        if (!resultMethods.containsKey(taskClass)) {
            TaskLogger.e(TAG, "No callback in " + targetClass.getSimpleName() + " for task: " + taskClass.getSimpleName());
        }
        return resultMethods.get(taskClass);
    }

    public ProgressMethodDescriptor getProgressMethod(Class taskClass) {
        if (!progressMethods.containsKey(taskClass)) {
            TaskLogger.e(TAG, "No progress callback in " + targetClass.getSimpleName() + " for task: " + taskClass.getSimpleName());
        }
        return progressMethods.get(taskClass);
    }

    public GlobalErrorMethodDescriptor getGlobalErrorMethod() {
        return globalError;
    }

    private void logCallbackMethods() {
        TaskLogger.d(TAG, "Callback methods for " + targetClass.getSimpleName());
        for (MethodDescriptor md : resultMethods.values()) {
            TaskLogger.d(TAG, ".    " + md.getRequestClass().getSimpleName() + " - " + md.getMethodName());
        }
    }
}
