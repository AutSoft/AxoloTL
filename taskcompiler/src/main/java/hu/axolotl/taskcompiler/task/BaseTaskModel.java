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

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.VariableElement;

import hu.autsoft.compiler.CompilerUtil;
import hu.autsoft.compiler.ProcessorException;
import hu.autsoft.compiler.TypeHelper;
import hu.axolotl.taskcompiler.Util;
import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.annotation.CreateTask;

public abstract class BaseTaskModel {

    private final Map<String, TypeHelper> parameters = new LinkedHashMap<>();

    private final TypeHelper workerClass;
    private final TypeHelper workerTaskHelperClass;
    private final TypeHelper taskClass;
    private final String workerFunction;
    private TypeHelper fullReturnType;
    private TypeHelper returnType;
    private TypeHelper genericTaskAgentType;
    private boolean needAgent = false;
    private final int threadId;

    BaseTaskModel(TypeHelper workerClass, TypeHelper workerTaskHelperClass, ExecutableElement methodElement) {
        this.workerClass = workerClass;
        this.workerTaskHelperClass = workerTaskHelperClass;
        taskClass = Util.getTaskClass(methodElement);

        workerFunction = methodElement.getSimpleName().toString();
        fullReturnType = TypeHelper.getTypeHelper(methodElement.getReturnType().toString());
        returnType = getReturnType(fullReturnType);

        TypeHelper progressType = TypeHelper.getVoid();

        int index = 0;
        for (VariableElement paramElement : methodElement.getParameters()) {
            TypeHelper methodParam = TypeHelper.getTypeHelper(paramElement.asType().toString());
            if (isTaskAgent(methodParam)) {
                if (index != 0) {
                    throw new ProcessorException("Invalid TaskAgent position (must be the first param)");
                }
                needAgent = true;
                progressType = methodParam.isParameterized() ? methodParam.getParamTypeArgument() : TypeHelper.getObject();
            } else {
                parameters.put(paramElement.getSimpleName().toString(), methodParam);
            }
            index++;
        }

        genericTaskAgentType = TypeHelper.getTypeHelper(ParameterizedTypeName.get(ClassName.get(TaskAgent.class), progressType.getTypeNameBoxed()).toString());

        CreateTask annotation = methodElement.getAnnotation(CreateTask.class);
        threadId = annotation.value();
    }

    protected abstract TypeHelper getReturnType(TypeHelper fullReturnType);

    private boolean isTaskAgent(TypeHelper typeHelper) {
        return typeHelper.hasClassName() && typeHelper.getClassName().toString().equals(TaskAgent.class.getName());
    }

    public final String getFilename() {
        return taskClass.getStrFullClassName();
    }

    public final String getJava() {
        TypeSpec.Builder builder = generateClass();
        generateConstructor(builder);
        generateGetters(builder);
        generateRunFunction(builder);

        return CompilerUtil.getJavaString(taskClass.getStrPackageName(), builder.build());
    }

    private TypeSpec.Builder generateClass() {
        TypeSpec.Builder builder = TypeSpec.classBuilder(taskClass.getStrClassName())
                .superclass(getSuperClass())
                .addModifiers(Modifier.PUBLIC);
        for (Map.Entry<String, TypeHelper> entry : parameters.entrySet()) {
            builder.addField(entry.getValue().getTypeName(), entry.getKey());
        }
        builder.addField(FieldSpec.builder(workerClass.getTypeName(), "worker", Modifier.PROTECTED).build());
        return builder;
    }

    protected abstract TypeName getSuperClass();

    protected final TypeName convertReturnGeneric(Class superClass) {
        return ParameterizedTypeName.get(TypeHelper.getTypeHelper(superClass).getClassName(), returnType.getTypeNameBoxed());
    }

    protected final TypeName convertReturnAndProgressGeneric(Class superClass) {
        return ParameterizedTypeName.get(TypeHelper.getTypeHelper(superClass).getClassName(), returnType.getTypeNameBoxed(), genericTaskAgentType.getParamTypeArgument().getTypeName());
    }

    private void generateConstructor(TypeSpec.Builder builder) {
        MethodSpec.Builder mb = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        mb.addStatement("$T helper = new $T()", workerTaskHelperClass.getTypeName(), workerTaskHelperClass.getTypeName());
        mb.addStatement("worker = helper.getWorker()");
        mb.addStatement("setSchedulerId($L)", threadId);
        for (Map.Entry<String, TypeHelper> entry : parameters.entrySet()) {
            mb.addParameter(entry.getValue().getTypeName(), entry.getKey());
            mb.addStatement("this.$N = $N", entry.getKey(), entry.getKey());
        }
        builder.addMethod(mb.build());
    }

    private String getGetterNameFromFieldName(String fieldName) {
        return "getParam" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private void generateGetters(TypeSpec.Builder builder) {
        for (Map.Entry<String, TypeHelper> entry : parameters.entrySet()) {
            MethodSpec.Builder mb = MethodSpec.methodBuilder(getGetterNameFromFieldName(entry.getKey()))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(entry.getValue().getTypeName())
                    .addStatement("return this.$N", entry.getKey());
            builder.addMethod(mb.build());
        }
    }

    protected abstract String getRunFunctionName();

    protected abstract boolean needAgentParamForRunFunction();

    private void generateRunFunction(TypeSpec.Builder builder) {
        MethodSpec.Builder mb = MethodSpec.methodBuilder(getRunFunctionName());
        mb.addModifiers(Modifier.PUBLIC);
        mb.addAnnotation(Override.class);
        if (needAgentParamForRunFunction()) {
            mb.addParameter(genericTaskAgentType.getTypeName(), "agent");
        }
        mb.returns(fullReturnType.getTypeNameBoxed());
        mb.addStatement((fullReturnType.isVoid() ? "" : "return ") + "worker." + workerFunction + "(" + getWorkerFunctionParameters(false) + ")");
        if (fullReturnType.isVoid()) {
            mb.addStatement("return null");
        }
        builder.addMethod(mb.build());
    }

    private String getWorkerFunctionParameters(boolean withoutAgent) {
        StringBuilder sb = new StringBuilder();
        if (!withoutAgent && needAgent) {
            sb.append("agent");
            if (parameters.size() > 0) {
                sb.append(", ");
            }
        }
        boolean first = true;
        for (Map.Entry<String, TypeHelper> entry : parameters.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            first = false;
            sb.append(entry.getKey());
        }
        return sb.toString();
    }

}
