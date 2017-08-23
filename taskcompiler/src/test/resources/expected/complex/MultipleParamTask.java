package hu.axolotl.test.task.complex;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.test.ComplexWorker;
import hu.axolotl.test.task.ComplexWorkerTaskHelper;

public class MultipleParamTask extends BaseRunTask<Void, Void> {
    Object param1;

    Double param2;

    int param3;

    protected ComplexWorker worker;

    public MultipleParamTask(Object param1, Double param2, int param3) {
        ComplexWorkerTaskHelper helper = new ComplexWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
    }

    public Object getParamParam1() {
        return this.param1;
    }

    public Double getParamParam2() {
        return this.param2;
    }

    public int getParamParam3() {
        return this.param3;
    }

    @Override
    public Void run(TaskAgent<Void> agent) {
        worker.multipleParam(param1, param2, param3);
        return null;
    }
}
