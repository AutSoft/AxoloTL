package hu.axolotl.test.task.simple;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.test.SimpleWorker;
import hu.axolotl.test.task.SimpleWorkerTaskHelper;

public class SimpleTask extends BaseRunTask<Void, Void> {

    protected SimpleWorker worker;

    public SimpleTask() {
        SimpleWorkerTaskHelper helper = new SimpleWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
    }

    @Override
    public Void run(TaskAgent<Void> agent) {
        worker.simple();
        return null;
    }
}