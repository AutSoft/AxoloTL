package hu.axolotl.test.task.rx;

import hu.axolotl.tasklib.base.BaseFlowableTask;
import hu.axolotl.test.RxWorker;
import hu.axolotl.test.task.RxWorkerTaskHelper;
import io.reactivex.Flowable;

public class IntegerFlowableTask extends BaseFlowableTask<Integer> {
    protected RxWorker worker;

    public IntegerFlowableTask() {
        RxWorkerTaskHelper helper = new RxWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
    }

    @Override
    public Flowable<Integer> getRunFlowable() {
        return worker.integerFlowable();
    }
}
