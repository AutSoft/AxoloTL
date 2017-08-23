package hu.axolotl.test.task;

import javax.inject.Inject;

import hu.axolotl.test.RxWorker;

public class RxWorkerTaskHelper {
    @Inject
    protected RxWorker worker;

    public RxWorkerTaskHelper() {
        hu.axolotl.test.TestApplication.injector.inject(this);
    }

    public RxWorker getWorker() {
        return worker;
    }

    public void setWorker(RxWorker worker) {
        this.worker = worker;
    }
}