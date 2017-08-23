package hu.axolotl.test;

import hu.axolotl.tasklib.TaskAgent;
import hu.axolotl.tasklib.annotation.CreateTask;

public class InvalidTaskAgentWorker {
    @CreateTask
    public void simple(int param, TaskAgent taskAgent) {
    }
}