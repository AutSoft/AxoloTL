package hu.axolotl.tasklib;

public class RxTaskMessage<T, U> {

    public static <T, U> RxTaskMessage<T, U> createProgress(U progressObject) {
        return new RxTaskMessage<>(true, progressObject, null);
    }

    public static <T, U> RxTaskMessage<T, U> createResult(T resultObject) {
        return new RxTaskMessage<>(false, null, resultObject);
    }

    boolean progress = false;
    private U progressObject;
    private T resultObject;

    private RxTaskMessage(boolean progress, U progressObject, T resultObject) {
        this.progress = progress;
        this.progressObject = progressObject;
        this.resultObject = resultObject;
    }

    public boolean isProgress() {
        return progress;
    }

    public U getProgressObject() {
        return progressObject;
    }

    public T getResultObject() {
        return resultObject;
    }
}
