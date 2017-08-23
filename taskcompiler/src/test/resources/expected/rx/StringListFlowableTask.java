package hu.axolotl.test.task.rx;

import java.util.List;

import hu.axolotl.tasklib.base.BaseFlowableTask;
import hu.axolotl.test.RxWorker;
import hu.axolotl.test.task.RxWorkerTaskHelper;
import io.reactivex.Flowable;

public class StringListFlowableTask extends BaseFlowableTask<List<String>> {
    protected RxWorker worker;

    public StringListFlowableTask() {
        RxWorkerTaskHelper helper = new RxWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
    }

    @Override
    public Flowable<List<String>> getRunFlowable() {
        return worker.stringListFlowable();
    }
}
