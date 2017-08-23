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

import java.util.concurrent.BlockingQueue;

import hu.axolotl.tasklib.GlobalError;
import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.descriptor.ClassDescriptor;
import hu.axolotl.tasklib.descriptor.GlobalErrorMethodDescriptor;
import hu.axolotl.tasklib.exception.EngineRuntimeException;
import hu.axolotl.tasklib.util.TaskLogger;

public class TRGlobalExceptionRunnable extends BaseTaskCallbackRunnable {

    public static final String TAG = TRGlobalExceptionRunnable.class.getSimpleName();

    BlockingQueue<Boolean> queue;
    GlobalError globalError;

    public TRGlobalExceptionRunnable(ClassDescriptor classDescriptor, Object target, BaseTask task, GlobalError globalError, BlockingQueue<Boolean> queue) {
        super(classDescriptor, target, task);
        this.queue = queue;
        this.globalError = globalError;
    }

    @Override
    public void run() {
        TaskLogger.d(TAG, "run");
        boolean handled = false;
        try {
            TaskLogger.d(TAG, getClass().getSimpleName() + " task: " + task.getClass().getSimpleName());
            GlobalErrorMethodDescriptor md = classDescriptor.getGlobalErrorMethod();
            if (md != null) {
                handled = (Boolean) md.invoke(target, globalError);
                if (handled) {
                    TaskLogger.d(TAG, getClass().getSimpleName() + " handled!");
                } else {
                    TaskLogger.d(TAG, getClass().getSimpleName() + " not handled...");
                }
            }
        } catch (Exception ex) {
            TaskLogger.exception(TAG, ex);
            innerException = ex;
        }
        try {
            queue.put(handled);
        } catch (InterruptedException ex) {
            TaskLogger.exception(TAG, ex);
            throw new EngineRuntimeException("InterruptedException???", ex);
        }
    }
}
