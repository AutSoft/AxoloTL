package hu.axolotl.tasklib.util;

public class JavaTaskLogger extends TaskLogger {

    @Override
    protected void logV(String tag, String message) {
        log("VERBOSE", tag, message);
    }

    @Override
    protected void logD(String tag, String message) {
        log("DEBUG", tag, message);
    }

    @Override
    protected void logE(String tag, String message) {
        log("ERROR", tag, message);
    }

    private void log(String type, String tag, String message) {
        System.out.println("[" + type + "] " + tag + " - " + message);
    }
}