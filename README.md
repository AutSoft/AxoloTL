# AxoloTL
Axolo Task Library

## Dependencies (from jitpack)

```
compile 'com.github.AutSoft.AxoloTL:compiler-core:1.0.1'
compile 'com.github.AutSoft.AxoloTL:tasklib:1.0.1'
compile 'com.github.AutSoft.AxoloTL:tasklib-core:1.0.1'
compile 'com.github.AutSoft.AxoloTL:tasklib-stetho:1.0.1'
testCompile 'com.github.AutSoft.AxoloTL:tasklib-test:1.0.1'
annotationProcessor 'com.github.AutSoft.AxoloTL:taskcompiler:1.0.1'
```

## Task creation
* Create a *Worker class
* Create a function in the Worker class with synchronous code
* Use `@CreateTask` annotation for the function
* TaskLib compiler will create a Task for every function annotated with `@CreateTask`
    * Every task class has the same parameters for the constructor as in the function which represented by the class
    * The `getResult()` function has the same return type as the function

```java
public class SimpleWorker {
    @CreateTask
    public String first(int parameter) {
        return "Result: " + parameter;
    }
}
```

## Task usage
* Create a `TaskEngineHolder` where you would like to use the task (inside in an activity, fragment, etc.)
* Execute the task with the executeTask function of the TaskEngineHolder

```java
public class SampleTaskFragment extends Fragment {
  
    TaskEngineHolder holder;
 
    public SampleTaskFragment() {
        holder = new TaskEngineHolder(this);
    }
 
    @Override
    public void onResume() {
        super.onResume();
        holder.start();

        holder.executeTask(new FirstTask(3));
    }
 
    @Override
    public void onPause() {
        super.onPause();
        holder.stop();
    }
 
    public void onTaskResult(FirstTask task) {
        if(!task.hasError()) {
            // Handle result
            String result = task.getResult();
        } else {
            // Handle error...
        }
    }
}
```


For MVP, sample presenter:

```java
public abstract class Presenter<S> {
 
    protected S screen;
 
    private TaskEngineHolder holder = new TaskEngineHolder(this);
 
    @CallSuper
    public void attachScreen(S screen) {
        this.screen = screen;
        holder.start();
    }
 
    @CallSuper
    public void detachScreen() {
        this.screen = null;
        holder.stop();
    }
     
    public void startFirstTask() {
        holder.executeTask(new FirstTask);
    }
 
    public void onTaskResult(FirstTask task) {
        if(!task.hasError()) {
            // Handle result
            String result = task.getResult();
        } else {
            // Handle error...
        }
    }
}
```


### Inline 'callbacks'

```java
holder.executeTask(new FirstTask(3), , new InlineTaskListener<String, Void>() {
    @Override
    public void onTaskResult(BaseTask<String, Void> task) {
        // handle task result...
    }
});
```

## Error

In the worker function you can throw any `RuntimeException`.
If any exception thrown, then the `task.hasError()` function will return true.
With the `task.getException()` function you can get the thrown exception in the task result function.

Function in worker:
```java
@CreateTask
public void taskWithError() {
    throw new TaskException(ERROR_CODE_INTEGER);
}
```

Callback:
```java
public void onTaskResult(TaskWithErrorTask task) {
    task.hasError(); // returns true
    task.getErrorCode(); // returns ERROR_CODE_INTEGER
}
```

### Error Object

You can pass any error object with the `TaskException`:
```java
@CreateTask
public void taskWithError() {
    throw new TaskException(ERROR_CODE_INTEGER, new CustomObject());
}
```

In the callback function use `getErrorObject()` function:
```java
public void onTaskResult(TaskWithErrorTask task) {
    task.hasError(); // returns true
    task.getErrorCode(); // returns ERROR_CODE_INTEGER
    task.getErrorObject(); // returns the created CustomObject
}
```

## Progress

In the worker function you can use a `TaskAgent` as a first argument (it is only allowed in the first place).
With a `TaskAgent` instance (which is created and passed by the Task Library) you can send progress updates to the caller.

```java
public class ProgressWorker {
    @CreateTask
    public String longRunning(TaskAgent<String> agent, int parameter) {
        int max = 10;
        for (int i = 0; i < max; i++) {
            int percent = (int) ((100.0 * i) / max);
            agent.publishProgress("PROGRESS: " + percent + "%");
            Thread.sleep(300); // some long-running synch work
        }
        agent.publishProgress("PROGRESS: 100%");
        return "DONE: " + parameter;
    }
}
```

In the target object you can define an `onTaskProgress` function with two argument.
One for the task and one for the progress object:

```java
protected void onTaskProgress(LongRunningTask task, String progress) {
    // setProgress("onTaskProgress: " + progress);
}
```

## Global Error

FIXME

## Follow Task

FIXME

## Defining a scheduler for a task

The `@CreateTask` annotation has a `value` parameter to specify which scheduler you want to use for running the task.
```
public class CustomSchedulersWorker {
 
    @CreateTask(TaskSchedulers.IO)
    public void customSchedulerIo() {
    	// do some work...
    }
 
    @CreateTask(MyCustomSchedulers.MY_SCHEDULER)
    public void fullCustomScheduler() {
        // do some work...
    }
 
}
```

In `TaskSchedulers` there are some basic scheduler which comes from RxJava:
* SINGLE
* COMPUTATION
* IO
* TRAMPOLINE
* NEW_THREAD

If you need a custom scheduler you can register it with a positive integer id and you can use this id in `@CreateTask` annotation.
```java
TaskSchedulers.registerScheduler(MyCustomSchedulers.MY_SCHEDULER, Schedulers.newThread());
```

## Dagger compatibility

FIXME

## Rx support

```java
public class RxTasksWorker {
 
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
 
}
```

## Stetho support

FIXME

## Testing

FIXME

### Matcher

FIXME

# Util

[Compiler-Core](compiler-core/README.md)

# Future plans
* Foreground service support for workers: So you can use executeTask to start a foreground service with given icon, name, etc.

# License

```
Copyright 2017 AutSoft Kft.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
