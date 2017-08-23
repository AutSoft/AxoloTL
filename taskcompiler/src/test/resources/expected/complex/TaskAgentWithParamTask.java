package hu.axolotl.test.task.complex;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.test.ComplexWorker;
import hu.axolotl.test.task.ComplexWorkerTaskHelper;

public class TaskAgentWithParamTask extends BaseRunTask<Void, Double> {
    int param;

    protected ComplexWorker worker;

    public TaskAgentWithParamTask(int param) {
        ComplexWorkerTaskHelper helper = new ComplexWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
        this.param = param;
    }

    public int getParamParam() {
        return this.param;
    }

    @Override
    public Void run(TaskAgent<Double> agent) {
        worker.taskAgentWithParam(agent, param);
        return null;
    }
}