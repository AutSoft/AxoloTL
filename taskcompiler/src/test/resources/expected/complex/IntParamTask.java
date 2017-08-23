package hu.axolotl.test.task.complex;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.test.ComplexWorker;
import hu.axolotl.test.task.ComplexWorkerTaskHelper;

public class IntParamTask extends BaseRunTask<Void, Void> {
    int param;

    protected ComplexWorker worker;

    public IntParamTask(int param) {
        ComplexWorkerTaskHelper helper = new ComplexWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
        this.param = param;
    }

    public int getParamParam() {
        return this.param;
    }

    @Override
    public Void run(TaskAgent<Void> agent) {
        worker.intParam(param);
        return null;
    }
}
