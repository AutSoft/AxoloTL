package hu.axolotl.tasklib.test.target.test.globalerror;

import hu.axolotl.tasklib.GlobalError;

public class TestTargetWithTwoParamGlobal {
    boolean onTaskGlobalError(GlobalError globalError, Object object) {
        return true;
    }
}