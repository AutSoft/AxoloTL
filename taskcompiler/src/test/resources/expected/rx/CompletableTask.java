package hu.axolotl.test.task.rx;

import hu.axolotl.tasklib.base.BaseCompletableTask;
import hu.axolotl.test.RxWorker;
import hu.axolotl.test.task.RxWorkerTaskHelper;
import io.reactivex.Completable;

public class CompletableTask extends BaseCompletableTask {
    protected RxWorker worker;

    public DoubleSingleTask() {
        RxWorkerTaskHelper helper = new RxWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
    }

    @Override
    public Completable getRunCompletable() {
        return worker.completable();
    }
}
