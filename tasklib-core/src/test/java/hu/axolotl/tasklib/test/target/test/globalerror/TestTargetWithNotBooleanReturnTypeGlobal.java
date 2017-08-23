package hu.axolotl.tasklib.test.target.test.globalerror;

import hu.axolotl.tasklib.GlobalError;

public class TestTargetWithNotBooleanReturnTypeGlobal {
    int onTaskGlobalError(GlobalError globalError) {
        return -1;
    }
}