package hu.axolotl.test.task;

import hu.axolotl.test.SimpleWorker;
import javax.inject.Inject;

public class SimpleWorkerTaskHelper {

	@Inject
	protected SimpleWorker worker;

	public SimpleWorkerTaskHelper() {
		hu.axolotl.test.TestApplication.injector.inject(this);
	}

	public SimpleWorker getWorker() {
		return worker;
	}

	public void setWorker(SimpleWorker worker) {
		this.worker = worker;
	}
}