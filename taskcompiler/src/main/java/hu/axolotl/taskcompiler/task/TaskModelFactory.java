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
package hu.axolotl.taskcompiler.task;

import javax.lang.model.element.ExecutableElement;

import hu.autsoft.compiler.TypeHelper;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class TaskModelFactory {

    public static BaseTaskModel getTaskModel(TypeHelper workerClass, TypeHelper workerTaskHelperClass, ExecutableElement methodElement) {
        TypeHelper returnType = TypeHelper.getTypeHelper(methodElement.getReturnType().toString());
        if (returnType.isParameterized() && returnType.isClass(Observable.class)) {
            return new ObservableTaskModel(workerClass, workerTaskHelperClass, methodElement);
        } else if (returnType.isParameterized() && returnType.isClass(Flowable.class)) {
            return new FlowableTaskModel(workerClass, workerTaskHelperClass, methodElement);
        } else if (returnType.isParameterized() && returnType.isClass(Single.class)) {
            return new SingleTaskModel(workerClass, workerTaskHelperClass, methodElement);
        } else if (returnType.isClass(Completable.class)) {
            return new CompletableTaskModel(workerClass, workerTaskHelperClass, methodElement);
        } else {
            return new RunTaskModel(workerClass, workerTaskHelperClass, methodElement);
        }
    }

}
