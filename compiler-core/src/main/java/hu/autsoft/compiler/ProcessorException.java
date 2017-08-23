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

import javax.lang.model.element.Element;

public class ProcessorException extends RuntimeException {

    private final Element element;

    public ProcessorException(String msg) {
        super(msg);
        element = null;
    }

    public ProcessorException(String msg, Element element) {
        super(msg);
        this.element = element;
    }

    public final Element getElement() {
        return element;
    }
}
