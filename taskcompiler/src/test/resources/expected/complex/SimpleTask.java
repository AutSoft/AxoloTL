package hu.axolotl.test.task.complex;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.test.ComplexWorker;
import hu.axolotl.test.task.ComplexWorkerTaskHelper;

public class SimpleTask extends BaseRunTask<Void, Void> {
    protected ComplexWorker worker;

    public SimpleTask() {
        ComplexWorkerTaskHelper helper = new ComplexWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
    }

    @Override
    public Void run(TaskAgent<Void> agent) {
        worker.simple();
        return null;
    }
}
