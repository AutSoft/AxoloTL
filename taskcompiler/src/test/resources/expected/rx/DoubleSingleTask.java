package hu.axolotl.test.task.rx;

import hu.axolotl.tasklib.base.BaseSingleTask;
import hu.axolotl.test.RxWorker;
import hu.axolotl.test.task.RxWorkerTaskHelper;
import io.reactivex.Single;

public class DoubleSingleTask extends BaseSingleTask<Double> {
    protected RxWorker worker;

    public DoubleSingleTask() {
        RxWorkerTaskHelper helper = new RxWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
    }

    @Override
    public Single<Double> getRunSingle() {
        return worker.doubleSingle();
    }
}
