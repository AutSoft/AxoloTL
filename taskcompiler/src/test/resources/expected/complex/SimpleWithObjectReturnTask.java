package hu.axolotl.test.task.complex;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.test.ComplexWorker;
import hu.axolotl.test.task.ComplexWorkerTaskHelper;

public class SimpleWithObjectReturnTask extends BaseRunTask<Object, Void> {
    protected ComplexWorker worker;

    public SimpleWithObjectReturnTask() {
        ComplexWorkerTaskHelper helper = new ComplexWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
    }

    @Override
    public Object run(TaskAgent<Void> agent) {
        return worker.simpleWithObjectReturn();
    }
}