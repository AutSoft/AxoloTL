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

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import hu.autsoft.compiler.ProcessorException;
import hu.autsoft.compiler.ProcessorLogger;
import hu.axolotl.tasklib.annotation.CreateTask;
import hu.axolotl.tasklib.annotation.Injector;

//@SupportedAnnotationTypes({"hu.axolotl.tasklib.annotation.Injector", "hu.axolotl.tasklib.annotation.CreateTask"})
//@SupportedSourceVersion(SourceVersion.RELEASE_7)
//@AutoService(Processor.class)
public class CreateTaskProcessor extends AbstractProcessor {

    private static final String LOG_PRE_TAG = "TaskCompiler";


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Injector.class.getName());
        set.add(CreateTask.class.getName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    /**
     * @param annotations set of annotations found
     * @param roundEnv    the environment for this processor round
     * @return whether a new processor round would be needed
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ProcessorLogger.init(processingEnv, LOG_PRE_TAG);
        if (annotations.isEmpty()) {
            return true;
        }
        try {
            FullModel fullModel = new FullModel();
            evalStaticInjector(roundEnv, fullModel);
            evalTaskAnnotations(roundEnv, fullModel);
            fullModel.writeFiles(processingEnv);
        } catch (ProcessorException ex) {
            ProcessorLogger.processorException(ex);
            return false;
        } catch (Exception ex) {
            ProcessorLogger.exception(ex);
            return false;
        }

        return true;
    }

    private void evalStaticInjector(RoundEnvironment roundEnv, FullModel fullModel) {
        Element staticInjector = null;
        ProcessorLogger.note("Injector annotation: " + roundEnv.getElementsAnnotatedWith(Injector.class).size());
        for (Element e : roundEnv.getElementsAnnotatedWith(Injector.class)) {
            if (staticInjector != null) {
                throw new ProcessorException("Static injector already set!");
            }
            if (e.getKind() == ElementKind.FIELD) {
                VariableElement varElement = (VariableElement) e;
                Set<Modifier> modifiers = varElement.getModifiers();
                if (!modifiers.contains(Modifier.PUBLIC) || !modifiers.contains(Modifier.STATIC)) {
                    throw new ProcessorException("Static injector is not public static!", e);
                }
                staticInjector = varElement;
            }
        }
        if (staticInjector == null) {
            throw new ProcessorException("Missing static injector!");
        }
        fullModel.setStaticInjector(staticInjector);
    }

    private void evalTaskAnnotations(RoundEnvironment roundEnv, FullModel fullModel) {
        ProcessorLogger.note("CreateTask annotation: " + roundEnv.getElementsAnnotatedWith(CreateTask.class).size());
        for (Element e : roundEnv.getElementsAnnotatedWith(CreateTask.class)) {
            if (e.getKind() == ElementKind.METHOD) {
                ExecutableElement exeElement = (ExecutableElement) e;
                fullModel.processMethod(exeElement);
            }
        }
    }
}