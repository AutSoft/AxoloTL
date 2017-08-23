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
package hu.axolotl.taskcompiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;

import hu.autsoft.compiler.CompilerUtil;
import hu.autsoft.compiler.TypeHelper;

public class Util {

    static TypeHelper getWorkerClass(Element workerClass) {
        return TypeHelper.getTypeHelper(CompilerUtil.getPackageNameForClass(workerClass) + "." + workerClass.getSimpleName());
    }

    static TypeHelper getWorkerTaskHelperClass(Element workerClass) {
        return TypeHelper.getTypeHelper(CompilerUtil.getPackageNameForClass(workerClass, "task") + "." + workerClass.getSimpleName() + "TaskHelper");
    }

    public static TypeHelper getTaskClass(ExecutableElement methodElement) {
        Element workerClass = methodElement.getEnclosingElement();
        String workerBasedPackageName = workerClass.getSimpleName().toString().toLowerCase();
        String postName = "worker";
        if (workerBasedPackageName.endsWith(postName)) {
            workerBasedPackageName = workerBasedPackageName.substring(0, workerBasedPackageName.length() - postName.length());
        }
        String methodName = methodElement.getSimpleName().toString();
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
        return TypeHelper.getTypeHelper(CompilerUtil.getPackageNameForClass(workerClass, "task." + workerBasedPackageName) + "." + methodName + "Task");
    }

}
