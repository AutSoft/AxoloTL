package hu.axolotl.test.task.complex;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.test.task.ComplexWorkerBaseTask;

public class RxIntegerObservableTask extends ComplexWorkerBaseTask<Object, Void> {

    public RxIntegerObservableTask() {
        setSchedulerId(0);
    }

    @Override
    public Object run(TaskAgent<Void> agent) {
        return worker.simpleWithObjectReturn();
    }
}
