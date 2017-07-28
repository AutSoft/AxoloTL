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
package hu.axolotl.tasklib.util;

public abstract class TaskLogger {

	private static TaskLogger instance;
	private static TaskLogger customInstance;

	public static void setCustomInstance(TaskLogger testInstance) {
		TaskLogger.customInstance = testInstance;
	}

	private static TaskLogger getInstance() {
		if (customInstance != null) {
			return customInstance;
		}
		if (instance == null) {
			instance = new JavaTaskLogger();
		}
		return instance;
	}

	public static void v(String tag, String message) {
		getInstance().logV(tag, message);
	}

	public static void d(String tag, String message) {
		getInstance().logD(tag, message);
	}

	public static void e(String tag, String message, Exception exception) {
		getInstance().logE(tag, message, exception);
	}

	public static void e(String tag, String message) {
		getInstance().logE(tag, message);
	}

	public static void exception(String tag, Exception ex) {
		getInstance().logException(tag, ex);
	}

	protected abstract void logV(String tag, String message);

	protected abstract void logD(String tag, String message);

	protected abstract void logE(String tag, String message);

	private void logE(String tag, String message, Exception exception) {
		logE(tag, message);
		logException(tag, exception);
	}

	private void logException(String tag, Exception ex) {
		logE(tag, "Exception");
		for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
			logE(tag, "\t" + stackTraceElement);
		}
	}
}
