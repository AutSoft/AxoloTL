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

import hu.axolotl.tasklib.InlineTaskListener;
import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.descriptor.ClassDescriptor;
import hu.axolotl.tasklib.descriptor.InvokeException;
import hu.axolotl.tasklib.descriptor.ResultMethodDescriptor;

public class TRResultRunnable extends BaseTaskCallbackRunnable {

    InlineTaskListener innerListener;

    public TRResultRunnable(ClassDescriptor classDescriptor, Object target, BaseTask task, InlineTaskListener innerListener) {
        super(classDescriptor, target, task);
        this.innerListener = innerListener;
    }

    @Override
    public void run() {
        try {
            if (innerListener != null) {
                innerListener.onTaskResult(task);
            } else {
                ResultMethodDescriptor md = classDescriptor.getResultMethod(task.getClass());
                if (md != null) {
                    md.invoke(target, task);
                }
            }
        } catch (InvokeException ex) {
            innerException = ex;
        }
    }
}