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
package hu.axolotl.tasklib.test.base;

import hu.axolotl.tasklib.base.BaseTaskEngineHolder;
import hu.axolotl.tasklib.callback.BaseTaskCallbackRunnable;

public class ReflectionTestTaskEngineHolder extends BaseTaskEngineHolder {

    public ReflectionTestTaskEngineHolder(Object target) {
        super(target);
    }

    @Override
    protected void postToProperThread(BaseTaskCallbackRunnable runnable) {
        throw new RuntimeException("Reflection only");
    }

    public boolean hasResultMethod(Class taskClass) {
        return classDescriptor.getResultMethod(taskClass) != null;
    }

    public boolean hasProgressMethod(Class taskClass) {
        return classDescriptor.getProgressMethod(taskClass) != null;
    }

    public boolean hasGlobalErrorMethod() {
        return classDescriptor.getGlobalErrorMethod() != null;
    }

}
