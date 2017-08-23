package hu.axolotl.tasklib.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import hu.axolotl.tasklib.exception.BaseTaskException;
import hu.axolotl.tasklib.exception.GlobalTaskException;
import hu.axolotl.tasklib.exception.TaskException;

public class TaskExceptionMatcher extends BaseMatcher<Object> {

    public static TaskExceptionMatcher codeWithoutObjectCheck(int errorCode) {
        return new TaskExceptionMatcher(false, errorCode, false, null);
    }

    public static TaskExceptionMatcher codeWithNullObject(int errorCode) {
        return new TaskExceptionMatcher(false, errorCode, true, null);
    }

    public static TaskExceptionMatcher codeWithObject(int errorCode, Object errorObject) {
        return new TaskExceptionMatcher(false, errorCode, true, errorObject);
    }

    public static TaskExceptionMatcher globalCodeWithoutObjectCheck(int errorCode) {
        return new TaskExceptionMatcher(true, errorCode, false, null);
    }

    public static TaskExceptionMatcher globalCodeWithNullObject(int errorCode) {
        return new TaskExceptionMatcher(true, errorCode, true, null);
    }

    public static TaskExceptionMatcher globalCodeWithObject(int errorCode, Object errorObject) {
        return new TaskExceptionMatcher(true, errorCode, true, errorObject);
    }

    enum MatchResult {
        RESULT_OK,
        RESULT_EXCEPTION_CLASS_ERROR,
        RESULT_ERROR_CODE_MISMATCH,
        RESULT_ERROR_OBJECT_MISMATCH
    }

    private final boolean global;
    private final int errorCode;
    private final boolean checkObject;
    private final Object errorObject;
    private MatchResult matchResult = MatchResult.RESULT_OK;

    private Object actualException;
    private int actualErrorCode;
    private Object actualErrorObject;

    private TaskExceptionMatcher(boolean global, int errorCode, boolean checkObject, Object errorObject) {
        this.global = global;
        this.errorCode = errorCode;
        this.checkObject = checkObject;
        this.errorObject = errorObject;
    }

    @Override
    public boolean matches(Object item) {
        actualException = item;
        if (global) {
            if (!(item instanceof GlobalTaskException)) {
                matchResult = MatchResult.RESULT_EXCEPTION_CLASS_ERROR;
                return false;
            }
        } else {
            if (!(item instanceof TaskException)) {
                matchResult = MatchResult.RESULT_EXCEPTION_CLASS_ERROR;
                return false;
            }
        }
        return checkTaskException((BaseTaskException) item);
    }

    private boolean checkTaskException(BaseTaskException taskException) {
        actualErrorCode = taskException.getErrorCode();
        actualErrorObject = taskException.getErrorObject();
        if (taskException.getErrorCode() != errorCode) {
            matchResult = MatchResult.RESULT_ERROR_CODE_MISMATCH;
            return false;
        }
        if (checkObject) {
            Object actualObject = taskException.getErrorObject();
            if (actualObject != null && errorObject != null) {
                if (!actualObject.equals(errorObject)) {
                    matchResult = MatchResult.RESULT_ERROR_OBJECT_MISMATCH;
                    return false;
                }
            } else if (actualObject != null || errorObject != null) {
                matchResult = MatchResult.RESULT_ERROR_OBJECT_MISMATCH;
                return false;
            }
        }
        matchResult = MatchResult.RESULT_OK;
        return true;
    }

    @Override
    public void describeTo(Description description) {
        switch (matchResult) {
            case RESULT_EXCEPTION_CLASS_ERROR:
                DescriptionBuilder.exceptionClassError(description,
                        global ? GlobalTaskException.class : TaskException.class,
                        actualException.getClass());
                break;
            case RESULT_ERROR_CODE_MISMATCH:
                DescriptionBuilder.errorCodeMismatch(description, errorCode, actualErrorCode);
                break;
            case RESULT_ERROR_OBJECT_MISMATCH:
                DescriptionBuilder.errorObjectMismatch(description, errorObject, actualErrorObject);
                break;
            default:
                description.appendText("");
                break;
        }
    }
}