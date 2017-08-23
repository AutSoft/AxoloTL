package hu.axolotl.tasklib;

import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.base.BaseTaskSubscriber;
import hu.axolotl.tasklib.util.TaskLogger;

public class TaskSubscriber<T, U> extends BaseTaskSubscriber<T, U> {

    public static final String TAG = TaskSubscriber.class.getSimpleName();

    public interface TaskSubscriberListener<U> {
        void onTRResult(BaseTask task);

        void onTRProgress(BaseTask task, U progress);
    }

    TaskSubscriberListener listener;
    BaseTask<T, U> task;
    boolean hasResult = false;

    public TaskSubscriber(BaseTask<T, U> task, TaskSubscriberListener listener) {
        this.task = task;
        this.listener = listener;
    }

    @Override
    protected void onTaskProgress(U progress) {
        if (hasResult) {
            TaskLogger.e(TAG, "Progress after result is not allowed!");
            return;
        }
        listener.onTRProgress(task, progress);
    }

    @Override
    protected void onTaskResult(T resultObject) {
        if (hasResult) {
            TaskLogger.e(TAG, "Multiple result in task is not allowed!");
            return;
        }
        hasResult = true;
        task.setResult(resultObject);
        listener.onTRResult(task);
    }

    @Override
    protected void onTaskError(boolean global, int errorCode, Object errorObject, Throwable throwable) {
        if (hasResult) {
            TaskLogger.e(TAG, "Task Error after result is not allowed!");
            return;
        }
        hasResult = true;
        task.setError(global, errorCode, errorObject, throwable);
        listener.onTRResult(task);
    }
}
