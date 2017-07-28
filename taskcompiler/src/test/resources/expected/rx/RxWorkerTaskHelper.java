package hu.axolotl.test.task;

import hu.axolotl.test.RxWorker;
import javax.inject.Inject;

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