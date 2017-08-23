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
package hu.axolotl.taskcompiler.test.util;

import java.io.Reader;
import java.util.Arrays;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;

import hu.axolotl.taskcompiler.CreateTaskProcessor;

public class TestUtils {

    public static Iterable<? extends Processor> getProcessors() {
        return Arrays.asList(
                new CreateTaskProcessor()
        );
    }

    public static String getJavaFileObjectSource(JavaFileObject javaFileObject) {
        Reader reader = null;
        try {
            final int bufferSize = 1024;
            final char[] buffer = new char[bufferSize];
            final StringBuilder out = new StringBuilder();
            reader = javaFileObject.openReader(false);
            for (; ; ) {
                int rsz = reader.read(buffer, 0, buffer.length);
                if (rsz < 0)
                    break;
                out.append(buffer, 0, rsz);
            }
            return out.toString();
        } catch (Exception ex) {
            throw new RuntimeException("JavaFileObject reader exception");
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception ex) {
                throw new RuntimeException("JavaFileObject reader close exception");
            }
        }
    }

}
