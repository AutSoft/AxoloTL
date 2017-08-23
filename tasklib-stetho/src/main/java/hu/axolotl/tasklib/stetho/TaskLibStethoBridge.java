package hu.axolotl.tasklib.stetho;

import com.facebook.stetho.inspector.network.DefaultResponseHandler;
import com.facebook.stetho.inspector.network.NetworkEventReporter;
import com.facebook.stetho.inspector.network.NetworkEventReporterImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.util.TaskLogger;

class TaskLibStethoBridge {

    private static final String TAG = "TaskLibStethoBridge";

    private final NetworkEventReporter eventReporter = NetworkEventReporterImpl.get();

    void reportTaskExecuteCalled(BaseTask task, Object sourceAndTarget) {
        if (eventReporter.isEnabled()) {
            TaskStethoDescriptor descriptor = new TaskStethoDescriptor(task, sourceAndTarget);
            eventReporter.requestWillBeSent(new TaskLibStethoRequest(descriptor));
            int requestContentLength = descriptor.getRequestContentLength();
            eventReporter.dataSent(String.valueOf(task.getId()), requestContentLength, requestContentLength);
        }
    }

    void reportTaskStarted(BaseTask task) {
        // Do nothing (for future version...)
    }

    void reportTaskFinished(BaseTask task) {
        if (eventReporter.isEnabled()) {
            TaskStethoDescriptor descriptor = new TaskStethoDescriptor(task, null);
            eventReporter.responseHeadersReceived(new TaskLibStethoResponse(descriptor));
            int responseContentLength = descriptor.getResponseContentLength();
            eventReporter.dataReceived(descriptor.getIdStr(), responseContentLength, responseContentLength);
            InputStream responseStream = eventReporter.interpretResponseStream(
                    descriptor.getIdStr(),
                    "application/json; charset=utf-8",
                    null,
                    descriptor.getResponseInputStream(),
                    new DefaultResponseHandler(eventReporter, descriptor.getIdStr()));
            readResponseStreamForLog(descriptor, responseStream);
        }
    }

    private void readResponseStreamForLog(TaskStethoDescriptor descriptor, InputStream responseStream) {
        BufferedReader reader = null;
        try {
            if (responseStream != null) {
                reader = new BufferedReader(new InputStreamReader(responseStream, Util.UTF8));
                while (reader.readLine() != null) {
                }
                eventReporter.responseReadFinished(descriptor.getIdStr());
            } else {
                eventReporter.responseReadFailed(descriptor.getIdStr(), "NULL responseStream");
            }
        } catch (IOException ex) {
            TaskLogger.exception(TAG, ex);
            eventReporter.responseReadFailed(descriptor.getIdStr(), ex.toString() + " - " + ex.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    TaskLogger.exception(TAG, ex);
                }
            }
        }
    }


}