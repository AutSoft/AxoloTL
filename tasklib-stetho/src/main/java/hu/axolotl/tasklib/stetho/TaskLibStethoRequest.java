package hu.axolotl.tasklib.stetho;

import android.support.annotation.Nullable;

import com.facebook.stetho.inspector.network.NetworkEventReporter;

import java.io.IOException;

class TaskLibStethoRequest implements NetworkEventReporter.InspectorRequest {

    private TaskStethoDescriptor descriptor;

    public TaskLibStethoRequest(TaskStethoDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public TaskStethoDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public String id() {
        return descriptor.getIdStr();
    }

    @Override
    public String friendlyName() {
        return descriptor.getSourceStr();
    }

    @Nullable
    @Override
    public Integer friendlyNameExtra() {
        return null;
    }

    @Override
    public String url() {
        return "TL - " + descriptor.getTaskClassName() + "(" + descriptor.getIdStr() + ")";
    }

    @Override
    public String method() {
        return descriptor.getMethodName();
    }

    @Nullable
    @Override
    public byte[] body() throws IOException {
        return descriptor.getRequestContentBytes();
    }

    @Override
    public int headerCount() {
        return descriptor.getTaskParamCount();
    }

    @Override
    public String headerName(int index) {
        return descriptor.getTaskParam(index).getName();
    }

    @Override
    public String headerValue(int index) {
        return descriptor.getTaskParam(index).getValueStr();
    }

    @Nullable
    @Override
    public String firstHeaderValue(String name) {
        return "TLS_Req(" + descriptor.getIdStr() + ")_" + "firstHeaderValue( " + name + " )";
    }
}