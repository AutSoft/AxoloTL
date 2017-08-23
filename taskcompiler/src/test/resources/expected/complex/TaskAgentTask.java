package hu.axolotl.test.task.complex;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.test.ComplexWorker;
import hu.axolotl.test.task.ComplexWorkerTaskHelper;

public class TaskAgentTask extends BaseRunTask<Void, Object> {
    protected ComplexWorker worker;

    public TaskAgentTask() {
        ComplexWorkerTaskHelper helper = new ComplexWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
    }

    @Override
    public Void run(TaskAgent<Object> agent) {
        worker.taskAgent(agent);
        return null;
    }
}
