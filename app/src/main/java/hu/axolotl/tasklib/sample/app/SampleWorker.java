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

import android.util.Log;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.TaskSchedulers;
import hu.axolotl.tasklib.annotation.CreateTask;
import hu.axolotl.tasklib.exception.GlobalTaskException;
import hu.axolotl.tasklib.exception.TaskException;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class SampleWorker {

    private static final String LOG_TAG = "SampleWorker";

    @CreateTask
    public String simple() {
        return "Simple Task Result";
    }

    @CreateTask
    public String inGlobalErrorHandler() {
        return "Task result which started from global error handler";
    }

    @CreateTask
    public TestResultObject complex(int id, TestPayloadObject requestObject) {
        return new TestResultObject();
    }

    @CreateTask
    public String longRunning(TaskAgent<String> agent) {
        int max = 10;
        for (int i = 0; i < max; i++) {
            int percent = (int) ((100.0 * i) / max);
            agent.publishProgress("PROGRESS: " + percent + "%");
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Log.e(LOG_TAG, "Interrupted", e);
            }
        }
        agent.publishProgress("PROGRESS: 100%");
        return "DONE";
    }

    @CreateTask
    public void exception() {
        throw new TaskException(91);
    }

    @CreateTask
    public void exceptionWithObject() {
        throw new TaskException(91, new TestPayloadObject());
    }

    @CreateTask
    public void runtimeException() {
        throw new RuntimeException("Akármi???");
    }

    @CreateTask
    public void globalException() {
        throw new GlobalTaskException(2, "GE error object");
    }

    @CreateTask
    public String early() {
        return "Early Task Result";
    }

    @CreateTask
    public Flowable<String> flowable() {
        return Flowable.just("Flowable done!");
    }

    @CreateTask
    public Observable<String> observable() {
        return Observable.just("Observable done!");
    }

    @CreateTask
    public Single<String> single() {
        return Single.just("Single done!");
    }

    @CreateTask
    public Completable completable() {
        return Completable.complete();
    }

    @CreateTask(TaskSchedulers.IO)
    public void customSchedulerIo() {
    }

	/*@CreateTask(MyCustomSchedulers.MY_SCHEDULER)
    public void fullCustomScheduler() {
		//TaskSchedulers.registerScheduler(MyCustomSchedulers.MY_SCHEDULER, Schedulers.newThread());
	}*/

}
