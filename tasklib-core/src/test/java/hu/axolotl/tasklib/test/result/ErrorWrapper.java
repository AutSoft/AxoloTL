package hu.axolotl.tasklib.test.result;

import hu.axolotl.tasklib.util.TaskLogger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ErrorWrapper {

    public static final String TAG = ErrorWrapper.class.getSimpleName();

    Class expectedExceptionClass;

    boolean handled = false;
    int errorCount = 0;
    int lastErrorCode = 0;
    Object lastErrorObject = null;
    Throwable lastException = null;

    public ErrorWrapper(Class expectedExceptionClass) {
        this.expectedExceptionClass = expectedExceptionClass;
    }

    public ErrorWrapper(ErrorWrapper other) {
        this(other.expectedExceptionClass);
        errorCount = other.errorCount;
        lastErrorCode = other.lastErrorCode;
        lastErrorObject = other.lastErrorObject;
        lastException = other.lastException;
    }

    public void handleError(int lastErrorCode, Object lastErrorObject, Throwable lastException) {
        errorCount++;
        this.lastErrorCode = lastErrorCode;
        this.lastErrorObject = lastErrorObject;
        this.lastException = lastException;
    }

    private void assertErrorCodeWithObject(int expectedErrorCount, int expectedErrorCode,
                                           Class expectedExceptionClass, Object expectedErrorObject) {
        handled = true;
        assertEquals(expectedErrorCount, errorCount);
        if (expectedErrorCount > 0) {
            assertNotNull(lastException);
            TaskLogger.d(TAG, lastException.getClass().getSimpleName());
            TaskLogger.d(TAG, expectedExceptionClass.getSimpleName());
            assertTrue("Exception class: " + lastException.getClass() + " instead of: " + expectedExceptionClass,
                    expectedExceptionClass.isInstance(lastException));
            if (expectedErrorCode > 0) {
                assertEquals(expectedErrorCode, lastErrorCode);
                if (expectedErrorObject != null) {
                    assertEquals(expectedErrorObject, lastErrorObject);
                } else {
                    assertNull(lastErrorObject);
                }
            }
        } else {
            assertNull(lastException);
            assertEquals(0, lastErrorCode);
            assertNull(lastErrorObject);
        }
    }

    public void assertNoError() {
        assertErrorCodeWithObject(0, 0, null, null);
    }

    public void assertErrorCode(int expectedErrorCode) {
        assertErrorCodeWithObject(1, expectedErrorCode, expectedExceptionClass, null);
    }

    public void assertErrorCodeWithObject(int expectedErrorCode, Object expectedErrorObject) {
        assertErrorCodeWithObject(1, expectedErrorCode, expectedExceptionClass, expectedErrorObject);
    }

    public void assertErrorWithCustomException(Class expectedExceptionClass) {
        assertErrorCodeWithObject(1, 0, expectedExceptionClass, null);
    }

    public void assertErrorWithCustomException(int expectedErrorCode, Class expectedExceptionClass) {
        assertErrorCodeWithObject(1, expectedErrorCode, expectedExceptionClass, null);
    }

    public void handleIfNotHandled() {
        if (!handled) {
            assertNoError();
        }
    }
}