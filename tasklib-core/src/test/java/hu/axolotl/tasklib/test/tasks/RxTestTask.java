package hu.axolotl.tasklib.test.tasks;

import hu.axolotl.tasklib.RxTaskMessage;
import hu.axolotl.tasklib.TaskAgent;
import io.reactivex.Flowable;

public class RxTestTask extends BaseTestTask {

    public static Flowable<RxTaskMessage<String, String>> create() {
        return new RxTestTask().createFlowable();
    }

    @Override
    public String run(TaskAgent agent) {
        for (int i = 0; i < 3; i++) {
            if (i > 0) {
                agent.publishProgress("STEP" + i);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "DONE";
    }
}