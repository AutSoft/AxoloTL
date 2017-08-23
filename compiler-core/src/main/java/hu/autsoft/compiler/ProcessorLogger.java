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
package hu.autsoft.compiler;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

public class ProcessorLogger {

    private static ProcessingEnvironment processingEnvironment;
    private static String logTag;

    public static void init(ProcessingEnvironment processingEnvironment, String logTag) {
        ProcessorLogger.processingEnvironment = processingEnvironment;
        ProcessorLogger.logTag = logTag;
    }

    private static String formatMessage(String msg) {
        return "[" + logTag + "] " + msg;
    }

    public static void note(String msg) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.NOTE, formatMessage(msg));
    }

    public static void error(String msg) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, formatMessage(msg));
    }

    public static void error(String msg, Element e) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, formatMessage(msg), e);
    }

    public static void exception(Exception ex) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, formatMessage("Exception: " + ex));
        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "\t" + stackTraceElement);
        }
    }

    public static void processorException(ProcessorException ex) {
        processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, formatMessage("Exception: " + ex + " - " + ex.getMessage()), ex.getElement());
        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            processingEnvironment.getMessager().printMessage(Diagnostic.Kind.ERROR, "\t" + stackTraceElement);
        }
    }

}
