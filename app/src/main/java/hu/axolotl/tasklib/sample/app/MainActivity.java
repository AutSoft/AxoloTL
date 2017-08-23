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
package hu.axolotl.tasklib.sample.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import hu.axolotl.app.R;
import hu.axolotl.tasklib.GlobalError;
import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.sample.app.task.sample.ComplexTask;
import hu.axolotl.tasklib.sample.app.task.sample.EarlyTask;
import hu.axolotl.tasklib.sample.app.task.sample.ExceptionTask;
import hu.axolotl.tasklib.sample.app.task.sample.ExceptionWithObjectTask;
import hu.axolotl.tasklib.sample.app.task.sample.FlowableTask;
import hu.axolotl.tasklib.sample.app.task.sample.GlobalExceptionTask;
import hu.axolotl.tasklib.sample.app.task.sample.InGlobalErrorHandlerTask;
import hu.axolotl.tasklib.sample.app.task.sample.LongRunningTask;
import hu.axolotl.tasklib.sample.app.task.sample.RuntimeExceptionTask;
import hu.axolotl.tasklib.sample.app.task.sample.SimpleTask;

public class MainActivity extends BaseActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String LONG_RUNNING_TASK_ID = "longRunningTaskId";

    ButtonMenuLayout menu;
    TextView taskStatusTV;

    Long longRunningTaskId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null && savedInstanceState.containsKey(LONG_RUNNING_TASK_ID)) {
            longRunningTaskId = savedInstanceState.getLong(LONG_RUNNING_TASK_ID);
        }

        taskStatusTV = (TextView) findViewById(R.id.task_status);
        menu = (ButtonMenuLayout) findViewById(R.id.buttonMenuLayout);

        menu.addMenuItem("RunAll", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnStart();
            }
        });
        menu.addMenuItem("Simple", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeTask(new SimpleTask());
            }
        });
        menu.addMenuItem("Complex", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeTask(new ComplexTask(67, new TestPayloadObject()));
            }
        });
        menu.addMenuItem("Error", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeTask(new ExceptionTask());
            }
        });
        menu.addMenuItem("Error W obj", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeTask(new ExceptionWithObjectTask());
            }
        });
        menu.addMenuItem("RuntimeE.", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeTask(new RuntimeExceptionTask());
            }
        });
        menu.addMenuItem("GlobalException", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeTask(new GlobalExceptionTask());
            }
        });
        menu.addMenuItem("LongRunning", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeTask(new LongRunningTask());
            }
        });
        menu.addMenuItem("Flowable", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeTask(new FlowableTask());
            }
        });

        executeTask(new EarlyTask());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (longRunningTaskId == null) {
            runOnStart();
        } else {
            followTask(LongRunningTask.class);
        }
    }

    private void runOnStart() {
        executeTask(new SimpleTask());
        executeTask(new GlobalExceptionTask());
        executeTask(new LongRunningTask());
        executeTask(new SimpleTask());
        executeTask(new FlowableTask());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (longRunningTaskId != null) {
            outState.putLong(LONG_RUNNING_TASK_ID, longRunningTaskId);
        }
    }

    @Override
    protected void setTaskStatus(BaseTask task, String status) {
        String oldStatus = taskStatusTV.getText().toString();
        taskStatusTV.setText((task != null ? task.getClass().getSimpleName() : "NULL") + " - " + status + "\n" + oldStatus);
        Log.d(TAG, "setTaskStatus: " + status);
    }

    @Override
    protected <T, U> long executeTask(BaseTask<T, U> task) {
        long ret = super.executeTask(task);
        setTaskStatus(task, "executeTask");
        return ret;
    }

    private void logTaskResult(BaseTask task) {
        if (!task.hasError()) {
            setTaskStatus(task, "onTaskResult success");
        } else {
            setTaskStatus(task, "onTaskResult error");
        }
    }

    protected void onTaskResult(EarlyTask task) {
        logTaskResult(task);
    }

    protected void onTaskResult(SimpleTask task) {
        logTaskResult(task);
        throw new RuntimeException("Test???");
    }

    protected void onTaskResult(LongRunningTask task) {
        logTaskResult(task);
    }

    protected void onTaskProgress(LongRunningTask task, Object progress) {
        setTaskStatus(task, "onTaskProgress: " + progress);
    }

    protected void onTaskResult(GlobalExceptionTask task) {
        logTaskResult(task);
    }

    protected void onTaskResult(FlowableTask task) {
        logTaskResult(task);
    }

    @Override
    protected boolean onTaskGlobalError(GlobalError error) {
        setTaskStatus(null, "onTaskGlobalError (" + error.getErrorCode() + "): " + error.getErrorObject());
        executeTask(new InGlobalErrorHandlerTask());
        return false;
    }
}
