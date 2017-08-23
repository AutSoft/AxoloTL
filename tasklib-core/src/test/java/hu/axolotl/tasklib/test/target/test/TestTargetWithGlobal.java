package hu.axolotl.tasklib.test.target.test;

import hu.axolotl.tasklib.GlobalError;
import hu.axolotl.tasklib.test.tasks.SimpleTask;

public class TestTargetWithGlobal {
    void onTaskResult(SimpleTask task) {
    }

    void onTaskProgress(SimpleTask task, Object progress) {
    }

    boolean onTaskGlobalError(GlobalError globalError) {
        return true;
    }
}