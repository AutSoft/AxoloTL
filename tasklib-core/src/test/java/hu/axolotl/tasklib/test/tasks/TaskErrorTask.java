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
package hu.axolotl.tasklib.test.tasks;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.exception.TaskException;


public class TaskErrorTask extends BaseTestTask {

    public static final int TASK_ERROR_CODE = 49;

    private Object errorObject;

    public TaskErrorTask() {
    }

    public TaskErrorTask(Object errorObject) {
        this.errorObject = errorObject;
    }

    @Override
    public String run(TaskAgent agent) {
        if (errorObject != null) {
            throw new TaskException(TASK_ERROR_CODE, errorObject);
        } else {
            throw new TaskException(TASK_ERROR_CODE);
        }
    }
}