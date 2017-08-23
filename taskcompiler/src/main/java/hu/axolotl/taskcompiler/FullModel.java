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

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import hu.autsoft.compiler.CompilerUtil;
import hu.autsoft.compiler.ProcessorException;
import hu.axolotl.taskcompiler.task.BaseTaskModel;
import hu.axolotl.taskcompiler.task.TaskModelFactory;

class FullModel {

    private String staticInjectorVariable;
    private Map<String, WorkerTaskModel> workerModels = new HashMap<>();

    void processMethod(ExecutableElement methodElement) {
        Element workerClass = methodElement.getEnclosingElement();
        if (CompilerUtil.isPackageUnnamed(workerClass)) {
            throw new ProcessorException("Unnamed package not allowed for worker: " + workerClass.getSimpleName());
        }
        String workerKey = Util.getWorkerClass(workerClass).getStrFullClassName();
        if (!workerModels.containsKey(workerKey)) {
            WorkerTaskModel workerTaskModel = new WorkerTaskModel(workerClass);
            workerModels.put(workerKey, workerTaskModel);
        }
        WorkerTaskModel workerTaskModel = workerModels.get(workerKey);
        BaseTaskModel taskModel = TaskModelFactory.getTaskModel(workerTaskModel.getWorkerClass(), workerTaskModel.getWorkerTaskHelperClass(), methodElement);
        workerTaskModel.addTaskModel(taskModel);
    }

    private void writeFile(ProcessingEnvironment processingEnv, String filename, String content) throws IOException {
        JavaFileObject jfo = processingEnv.getFiler().createSourceFile(filename);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "creating source file: " + jfo.toUri());
        Writer writer = jfo.openWriter();
        writer.write(content);
        writer.close();
    }

    public void writeFiles(ProcessingEnvironment processingEnv) throws IOException {
        for (WorkerTaskModel workerTaskModel : workerModels.values()) {
            writeFile(processingEnv, workerTaskModel.getHelperFilename(), workerTaskModel.getHelperJava(staticInjectorVariable));
            for (BaseTaskModel taskModel : workerTaskModel.getTaskModels()) {
                writeFile(processingEnv, taskModel.getFilename(), taskModel.getJava());
            }
        }
    }

    public void setStaticInjector(Element staticInjector) {
        staticInjectorVariable = staticInjector.getEnclosingElement() + "." + staticInjector;
    }
}
