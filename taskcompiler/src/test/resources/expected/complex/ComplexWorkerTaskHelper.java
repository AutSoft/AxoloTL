package hu.axolotl.test.task;

import hu.axolotl.test.ComplexWorker;
import javax.inject.Inject;

public class ComplexWorkerTaskHelper {
	@Inject
	protected ComplexWorker worker;

	public ComplexWorkerTaskHelper() {
		hu.axolotl.test.TestApplication.injector.inject(this);
	}

	public ComplexWorker getWorker() {
		return worker;
	}

	public void setWorker(ComplexWorker worker) {
		this.worker = worker;
	}
}