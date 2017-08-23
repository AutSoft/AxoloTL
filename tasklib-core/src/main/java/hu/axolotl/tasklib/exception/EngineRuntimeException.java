package hu.axolotl.tasklib.exception;

public class EngineRuntimeException extends RuntimeException {

    public EngineRuntimeException(String message) {
        super(message);
    }

    public EngineRuntimeException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
