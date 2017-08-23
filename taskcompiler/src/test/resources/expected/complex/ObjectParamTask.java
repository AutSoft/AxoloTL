package hu.axolotl.test.task.complex;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.test.ComplexWorker;
import hu.axolotl.test.task.ComplexWorkerTaskHelper;

public class ObjectParamTask extends BaseRunTask<Void, Void> {
    Object param;

    protected ComplexWorker worker;

    public ObjectParamTask(Object param) {
        ComplexWorkerTaskHelper helper = new ComplexWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
        this.param = param;
    }

    public Object getParamParam() {
        return this.param;
    }

    @Override
    public Void run(TaskAgent<Void> agent) {
        worker.objectParam(param);
        return null;
    }
}
