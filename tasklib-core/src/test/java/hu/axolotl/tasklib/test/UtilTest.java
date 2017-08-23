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
package hu.axolotl.tasklib.test;

import org.junit.Test;

import hu.axolotl.tasklib.test.util.UtilTestLogger;
import hu.axolotl.tasklib.util.TaskLogger;

import static org.junit.Assert.assertEquals;

public class UtilTest {

    private static final String TAG = UtilTest.class.getSimpleName();

    @Test
    public void logger() {
        TaskLogger.setCustomInstance(null);
        TaskLogger.d(UtilTest.class.getSimpleName(), "default logger");
        UtilTestLogger logger = new UtilTestLogger();
        TaskLogger.setCustomInstance(logger);
        TaskLogger.v(TAG, "v");
        TaskLogger.v(TAG, "v");
        TaskLogger.v(TAG, "v");
        TaskLogger.d(TAG, "d");
        TaskLogger.d(TAG, "d");
        TaskLogger.e(TAG, "e");
        assertEquals(3, logger.getV());
        assertEquals(2, logger.getD());
        assertEquals(1, logger.getE());
    }

	/*public void testTestExecutor() {
        UtilTestExecutor testExecutor = new UtilTestExecutor();
		TaskGlobalUtil.setTestExecutor(testExecutor);

		assertEquals(0, testExecutor.getExecutedCount());
		TaskEngine.getInstance().executeTask(new SimpleTask());
		assertEquals(1, testExecutor.getExecutedCount());
		TaskEngine.getInstance().executeTask(new SimpleTask());
		assertEquals(2, testExecutor.getExecutedCount());
	}*/


}
