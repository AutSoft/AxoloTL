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

import com.google.testing.compile.JavaFileObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaFileObject;

import hu.axolotl.tasklib.util.TaskLogger;

public class TestJavaUtils {

    public static List<JavaFileObject> withGeneral(String... resources) {
        List<String> fileObjects = new ArrayList<>();
        fileObjects.add("general/TestApplication.java");
        fileObjects.add("general/StaticInjector.java");
        fileObjects.addAll(Arrays.asList(resources));
        return fromResources(fileObjects.toArray(new String[0]));
    }

	/*public static JavaFileObject[] expected(String... resources) {
        List<String> fileObjects = new ArrayList<String>();
		for (String resource : resources) {
			fileObjects.add("expected/" + resource);
		}
		return fromResources(fileObjects.toArray(new String[0])).toArray(new JavaFileObject[0]);
	}*/

    public static List<JavaFileObject> fromResources(String... resources) {
        TaskLogger.d("TestUtils", "getJavaFiles");
        List<JavaFileObject> fileObjects = new ArrayList<>();
        for (String resource : resources) {
            JavaFileObject javaFileObject = JavaFileObjects.forResource(resource);
            TestLogger.log("TestUtils", javaFileObject);
            fileObjects.add(javaFileObject);
        }
        return fileObjects;
    }

}
