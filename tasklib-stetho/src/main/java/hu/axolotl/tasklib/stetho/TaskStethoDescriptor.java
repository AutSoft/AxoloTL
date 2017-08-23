/*
 * Copyright (C) 2017 AutSoft Kft.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hu.axolotl.tasklib.stetho;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.axolotl.tasklib.base.BaseCompletableTask;
import hu.axolotl.tasklib.base.BaseFlowableTask;
import hu.axolotl.tasklib.base.BaseObservableTask;
import hu.axolotl.tasklib.base.BaseRunTask;
import hu.axolotl.tasklib.base.BaseSingleTask;
import hu.axolotl.tasklib.base.BaseTask;
import hu.axolotl.tasklib.exception.GlobalTaskException;
import hu.axolotl.tasklib.exception.TaskException;
import hu.axolotl.tasklib.util.TaskLogger;

public class TaskStethoDescriptor {

    private static final String TAG = "TaskStethoDescriptor";

    private String sourceStr;
    private BaseTask task;
    private String requestContent;
    private String responseContent;
    private List<TaskStethoHeader> requestParams = new ArrayList<>();
    private List<TaskStethoHeader> responseParams = new ArrayList<>();

    public TaskStethoDescriptor(BaseTask task, Object sourceAndTarget) {
        sourceStr = sourceAndTarget != null ? sourceAndTarget.getClass().getSimpleName() : "NULL";
        this.task = task;
        createRequestParams();
        createResponseParams();

        requestContent = TaskLibStetho.getGsonInstance().toJson(getParamsObject());
        responseContent = TaskLibStetho.getGsonInstance().toJson(task.getResult());
    }

    private void createRequestParams() {
        for (Method method : task.getClass().getMethods()) {
            if (method.getName().startsWith("getParam")) {
                requestParams.add(TaskStethoHeader.createTaskRequestHeader(method, task));
            }
        }
    }

    private void createResponseParams() {
        responseParams.add(TaskStethoHeader.createResponseHeader("ResponseType", getClassNameOrNull(task.getResult())));
        responseParams.add(TaskStethoHeader.createResponseHeader("HasError", task.hasError()));
        responseParams.add(TaskStethoHeader.createResponseHeader("HasException", task.hasException()));
        responseParams.add(TaskStethoHeader.createResponseHeader("ExceptionClass", getClassNameOrNull(task.getException())));
        responseParams.add(TaskStethoHeader.createResponseHeader("ExceptionMessage",
                task.getException() != null ? task.getException().getMessage() : "NULL"));
        responseParams.add(TaskStethoHeader.createResponseHeader("ErrorCode", task.getErrorCode()));
        responseParams.add(TaskStethoHeader.createResponseHeader("ErrorObjectClass", getClassNameOrNull(task.getErrorObject())));
    }

    private String getClassNameOrNull(Object value) {
        return value != null ? value.getClass().getName() : "NULL";
    }

    private Map<String, Object> getParamsObject() {
        Map<String, Object> map = new HashMap<>();
        for (TaskStethoHeader param : requestParams) {
            map.put(param.getName(), param.getValue());
        }
        return map;
    }

    public byte[] getRequestContentBytes() throws UnsupportedEncodingException {
        return requestContent.getBytes(Util.UTF8);
    }

    public int getRequestContentLength() {
        return requestContent.length();
    }

    public byte[] getResponseContentBytes() throws UnsupportedEncodingException {
        return responseContent.getBytes(Util.UTF8);
    }

    public ByteArrayInputStream getResponseInputStream() {
        try {
            return new ByteArrayInputStream(getResponseContentBytes());
        } catch (UnsupportedEncodingException ex) {
            TaskLogger.exception(TAG, ex);
            return null;
        }
    }

    public int getResponseContentLength() {
        return responseContent.length();
    }

    public String getTaskClassName() {
        return task.getClass().getSimpleName();
    }

    public String getMethodName() {
        if (task instanceof BaseRunTask) {
            return "TL_RUN";
        } else if (task instanceof BaseSingleTask) {
            return "TL_SINGLE";
        } else if (task instanceof BaseFlowableTask) {
            return "TL_FLOWABLE";
        } else if (task instanceof BaseCompletableTask) {
            return "TL_COMPLETABLE";
        } else if (task instanceof BaseObservableTask) {
            return "TL_OBSERVABLE";
        } else {
            return task.getClass().getSuperclass().getSimpleName();
        }
    }

    public int getIdInt() {
        return (int) task.getId();
    }

    public String getIdStr() {
        return String.valueOf(task.getId());
    }

    public int getTaskParamCount() {
        return requestParams.size();
    }

    public TaskStethoHeader getTaskParam(int index) {
        return requestParams.get(index);
    }

    public String getSourceStr() {
        return sourceStr;
    }

    public int getResponseParamsCount() {
        return responseParams.size();
    }

    public TaskStethoHeader getResponseParam(int index) {
        return responseParams.get(index);
    }

    public int getStatusCode() {
        return task.hasError() ? 500 : 200;
    }

    public String getReasonPhrase() {
        if (!task.hasError()) {
            return "OK";
        }
        Throwable exception = task.getException();
        if (exception != null) {
            if (exception instanceof TaskException || exception instanceof GlobalTaskException) {
                return exception.getClass().getSimpleName() + "(" + task.getErrorCode() + ")";
            } else {
                return exception.getClass().getSimpleName() + " - " + exception.getMessage();
            }
        }
        return "???";
    }

    public String getResponseFirstHeaderValue() {
        if (!task.hasError()) {
            if (task.getResult() != null) {
                return task.getResult().getClass().getSimpleName();
            } else {
                return "NULL";
            }
        } else {
            Throwable exception = task.getException();
            if (exception != null) {
                return exception.getClass().getSimpleName();
            } else {
                return "ERROR";
            }
        }
    }
}
