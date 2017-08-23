package hu.axolotl.test;

import hu.axolotl.tasklib.annotation.CreateTask;

public class SimpleWorker {
    @CreateTask
    public void simple() {
    }

    @CreateTask
    public void simple(int i) {
    }
}