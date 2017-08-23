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
package hu.axolotl.tasklib.android.base;

import android.support.v7.app.AppCompatActivity;

import hu.axolotl.tasklib.InlineTaskListener;
import hu.axolotl.tasklib.android.TaskEngineHolder;
import hu.axolotl.tasklib.base.BaseTask;

public class TaskActivity extends AppCompatActivity {

    TaskEngineHolder holder;

    public TaskActivity() {
        holder = new TaskEngineHolder(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        holder.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        holder.stop();
    }

    protected <T, U> long executeTask(BaseTask<T, U> task) {
        return holder.executeTask(task);
    }

    protected <T, U> long executeTask(BaseTask<T, U> task, InlineTaskListener<T, U> listener) {
        return holder.executeTask(task, listener);
    }

    protected <T, U> boolean followTask(Class<? extends BaseTask<T, U>> taskClass) {
        return holder.followTask(taskClass);
    }
}
