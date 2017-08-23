package hu.axolotl.test.task.simple;

public class SecondTask extends hu.autsoft.axolotl.test.task.SimpleWorkerBaseTask<Void> {

    public SecondTask() {
        setSchedulerId(0);
    }

    @Override
    public Void run(RxTaskListener listener) {
        worker.second();
        return null;
    }
}