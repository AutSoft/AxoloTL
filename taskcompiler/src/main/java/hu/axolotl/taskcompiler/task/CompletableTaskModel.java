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

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.ExecutableElement;

import hu.autsoft.compiler.TypeHelper;
import hu.axolotl.tasklib.base.BaseCompletableTask;

public class CompletableTaskModel extends BaseTaskModel {
	CompletableTaskModel(TypeHelper workerClass, TypeHelper workerTaskHelperClass, ExecutableElement methodElement) {
		super(workerClass, workerTaskHelperClass, methodElement);
	}

	@Override
	protected TypeHelper getReturnType(TypeHelper fullReturnType) {
		return TypeHelper.getVoid();
	}

	@Override
	protected TypeName getSuperClass() {
		return TypeHelper.getTypeHelper(BaseCompletableTask.class).getTypeName();
	}

	@Override
	protected String getRunFunctionName() {
		return "getRunCompletable";
	}

	@Override
	protected boolean needAgentParamForRunFunction() {
		return false;
	}
}
