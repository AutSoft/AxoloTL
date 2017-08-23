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
package hu.axolotl.tasklib.stetho;

import com.facebook.stetho.inspector.console.CLog;
import com.facebook.stetho.inspector.protocol.module.Console;

import hu.axolotl.tasklib.TaskEventListener;
import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.base.BaseTaskEngineHolder;

class StethoEventListener extends TaskEventListener {

    private TaskLibStethoBridge bridge = new TaskLibStethoBridge();

    @Override
    protected void onHolderRegistered(BaseTaskEngineHolder holder) {
        // Do nothing (needed for future version)
    }

    @Override
    protected void onHolderDeregistered(BaseTaskEngineHolder holder) {
        // Do nothing (needed for future version)
    }

    @Override
    protected void onExecuteCalled(BaseTask task, Object sourceAndTarget) {
        CLog.writeToConsole(Console.MessageLevel.LOG, Console.MessageSource.OTHER, "onExecuteCalled");
        bridge.reportTaskExecuteCalled(task, sourceAndTarget);
    }

    @Override
    protected void onSubscribed(BaseTask task) {
        bridge.reportTaskStarted(task);
    }

    @Override
    protected void onFollowCalled(Class<? extends BaseTask> taskClass) {
        // Do nothing (needed for future version)
    }

    @Override
    protected void onProgress(BaseTask task, Object progress) {
        // Do nothing (needed for future version)
    }

    @Override
    protected void onResult(BaseTask task) {
        CLog.writeToConsole(Console.MessageLevel.LOG, Console.MessageSource.OTHER, "onResult");
        bridge.reportTaskFinished(task);
    }

    @Override
    protected void onExceptionInCallback(BaseTask task) {
        // Do nothing (needed for future version)
    }

}
