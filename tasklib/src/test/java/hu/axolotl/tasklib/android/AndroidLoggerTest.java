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
package hu.axolotl.tasklib.android;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import hu.axolotl.tasklib.android.util.AndroidTaskLogger;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;


@RunWith(PowerMockRunner.class)
@PrepareForTest(android.util.Log.class)
public class AndroidLoggerTest {

    private static final String TEST_TAG = "TestTag";
    private static final String TEST_MESSAGE = "TestMessage";

    @Before
    public void before() {
        AndroidTaskInitializer.init();
    }

    @Test
    public void verbose() throws Exception {
        PowerMockito.mockStatic(Log.class);

        AndroidTaskLogger.v(TEST_TAG, TEST_MESSAGE);

        PowerMockito.verifyStatic(times(1));
        Log.v(anyString(), anyString());
    }

    @Test
    public void debug() throws Exception {
        PowerMockito.mockStatic(Log.class);

        AndroidTaskLogger.d(TEST_TAG, TEST_MESSAGE);

        PowerMockito.verifyStatic(times(1));
        Log.d(anyString(), anyString());
    }

    @Test
    public void error() throws Exception {
        PowerMockito.mockStatic(Log.class);

        AndroidTaskLogger.e(TEST_TAG, TEST_MESSAGE);

        PowerMockito.verifyStatic(times(1));
        Log.e(anyString(), anyString());
    }
}
