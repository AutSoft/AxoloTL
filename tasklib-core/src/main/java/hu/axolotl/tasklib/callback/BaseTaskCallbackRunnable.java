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
package hu.axolotl.tasklib.callback;

import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.descriptor.ClassDescriptor;

public abstract class BaseTaskCallbackRunnable implements Runnable {

    final ClassDescriptor classDescriptor;
    final Object target;
    final BaseTask task;
    Exception innerException = null;

    public BaseTaskCallbackRunnable(ClassDescriptor classDescriptor, Object target, BaseTask task) {
        this.classDescriptor = classDescriptor;
        this.target = target;
        this.task = task;
    }

    public boolean hasInnerException() {
        return innerException != null;
    }
}