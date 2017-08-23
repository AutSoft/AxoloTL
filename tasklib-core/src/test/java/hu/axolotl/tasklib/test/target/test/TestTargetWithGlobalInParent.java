package hu.axolotl.tasklib.test.target.test;

import hu.axolotl.tasklib.test.tasks.TestSecondTask;

public class TestTargetWithGlobalInParent extends TestTargetWithGlobal {
    void onTaskResult(TestSecondTask task) {
    }

    void onTaskProgress(TestSecondTask task, Object progress) {
    }
}