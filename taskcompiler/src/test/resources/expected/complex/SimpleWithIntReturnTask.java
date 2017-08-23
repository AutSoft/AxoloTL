package hu.axolotl.test.task.complex;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.test.ComplexWorker;
import hu.axolotl.test.task.ComplexWorkerTaskHelper;

public class SimpleWithIntReturnTask extends BaseRunTask<Integer, Void> {
    protected ComplexWorker worker;

    public SimpleWithIntReturnTask() {
        ComplexWorkerTaskHelper helper = new ComplexWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
    }

    @Override
    public Integer run(TaskAgent<Void> agent) {
        return worker.simpleWithIntReturn();
    }
}