package hu.axolotl.test.task.rx;

import hu.axolotl.tasklib.base.BaseObservableTask;
import hu.axolotl.test.RxWorker;
import hu.axolotl.test.task.RxWorkerTaskHelper;
import io.reactivex.Observable;

public class IntegerObservableTask extends BaseObservableTask<Integer> {
    protected RxWorker worker;

    public IntegerObservableTask() {
        RxWorkerTaskHelper helper = new RxWorkerTaskHelper();
        worker = helper.getWorker();
        setSchedulerId(0);
    }

    @Override
    public Observable<Integer> getRunObservable() {
        return worker.integerObservable();
    }
}
