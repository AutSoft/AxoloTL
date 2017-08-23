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

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import hu.autsoft.compiler.CompilerUtil;
import hu.autsoft.compiler.TypeHelper;
import hu.axolotl.taskcompiler.task.BaseTaskModel;

class WorkerTaskModel {

    private final TypeHelper workerClass;
    private final TypeHelper workerTaskHelperClass;
    private List<BaseTaskModel> taskModels = new ArrayList<>();

    WorkerTaskModel(Element workerClass) {
        this.workerClass = Util.getWorkerClass(workerClass);
        workerTaskHelperClass = Util.getWorkerTaskHelperClass(workerClass);
    }

    String getHelperFilename() {
        return workerTaskHelperClass.getStrFullClassName();
    }

    void addTaskModel(BaseTaskModel taskModel) {
        taskModels.add(taskModel);
    }

    List<BaseTaskModel> getTaskModels() {
        return taskModels;
    }

    String getHelperJava(String injectorVariable) {
        TypeSpec baseWorkerTaskClassSpec = TypeSpec.classBuilder(workerTaskHelperClass.getStrClassName())
                .addModifiers(Modifier.PUBLIC)
                .addField(FieldSpec.builder(workerClass.getTypeName(), "worker")
                        .addModifiers(Modifier.PROTECTED)
                        .addAnnotation(Inject.class)
                        .build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addStatement(injectorVariable + ".inject(this)")
                        .build())
                .addMethod(MethodSpec.methodBuilder("getWorker")
                        .addModifiers(Modifier.PUBLIC)
                        .returns(workerClass.getTypeName())
                        .addStatement("return worker")
                        .build())
                .addMethod(MethodSpec.methodBuilder("setWorker")
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(workerClass.getTypeName(), "worker")
                        .addStatement("this.worker = worker")
                        .build())
                .build();
        return CompilerUtil.getJavaString(workerTaskHelperClass.getStrPackageName(), baseWorkerTaskClassSpec);
    }

    public TypeHelper getWorkerClass() {
        return workerClass;
    }

    public TypeHelper getWorkerTaskHelperClass() {
        return workerTaskHelperClass;
    }
}
