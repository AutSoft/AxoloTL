package hu.axolotl.test.task.complex;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.test.ComplexWorker;
import hu.axolotl.test.task.ComplexWorkerTaskHelper;

public class CustomExecutorTask extends BaseRunTask<Void, Void> {
    protected ComplexWorker worker;

    public CustomExecutorTask() {
        ComplexWorkerTaskHelper helper = new ComplexWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(5);
    }

    @Override
    public Void run(TaskAgent<Void> agent) {
        worker.customExecutor();
        return null;
    }
}
