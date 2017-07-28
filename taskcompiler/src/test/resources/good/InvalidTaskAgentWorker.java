package hu.axolotl.test;

import hu.axolotl.tasklib.annotation.CreateTask;
import hu.axolotl.tasklib.TaskAgent;

public class InvalidTaskAgentWorker {
	@CreateTask
	public void simple(int param, TaskAgent taskAgent) {
	}
}