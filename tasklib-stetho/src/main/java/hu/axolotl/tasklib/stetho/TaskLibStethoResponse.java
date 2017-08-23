package hu.axolotl.tasklib.stetho;

import android.support.annotation.Nullable;

import com.facebook.stetho.inspector.network.NetworkEventReporter;

class TaskLibStethoResponse implements NetworkEventReporter.InspectorResponse {

    private TaskStethoDescriptor descriptor;

    public TaskLibStethoResponse(TaskStethoDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public String requestId() {
        return descriptor.getIdStr();
    }

    @Override
    public String url() {
        return "TL - " + descriptor.getTaskClassName() + "(" + descriptor.getIdStr() + ")";
    }

    @Override
    public int statusCode() {
        return descriptor.getStatusCode();
    }

    @Override
    public String reasonPhrase() {
        return descriptor.getReasonPhrase();
    }

    @Override
    public boolean connectionReused() {
        // Not sure...
        return false;
    }

    @Override
    public int connectionId() {
        return descriptor.getIdInt();
    }

    @Override
    public boolean fromDiskCache() {
        return false;
    }

    @Override
    public int headerCount() {
        return descriptor.getResponseParamsCount();
    }

    @Override
    public String headerName(int index) {
        return descriptor.getResponseParam(index).getName();
    }

    @Override
    public String headerValue(int index) {
        return descriptor.getResponseParam(index).getValueStr();
    }

    @Nullable
    @Override
    public String firstHeaderValue(String name) {
        return descriptor.getResponseFirstHeaderValue();
    }
}