/*
 * Copyright 2020 Sukhvir Thapar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.sukhvir41.template;

import org.joor.Reflect;

import java.io.Writer;
import java.util.Collections;
import java.util.Map;

public class HtmlTemplate {

    private Reflect templateClass;

    HtmlTemplate(Reflect templateClass) {
        this.templateClass = templateClass;
    }

    public String render(Map<String, Object> parameters) {
        Reflect instance = Reflect.on(getTemplateInstance());
        renderImpl(instance, parameters);
        return instance
                .call("render")
                .get();
    }

    public void render(Map<String, Object> parameters, Writer writer) {
        Reflect instance = Reflect.on(getTemplateInstance());
        renderImpl(instance, parameters);
        instance
                .call("render", writer);
    }

    public String render() {
        return this.render(Collections.emptyMap());
    }

    public void render(Writer writer) {
        this.render(Collections.emptyMap(), writer);
    }

    private void renderImpl(Reflect instance, Map<String, Object> parameters) {
        Map<String, Reflect> instanceFields = instance.fields();
        parameters.forEach((name, value) -> setParameterValue(instance, instanceFields, name, value));
    }

    private HtTemplate getTemplateInstance() {
        return templateClass.call("getInstance")
                .get();
    }

    private void setParameterValue(Reflect templateInstance, Map<String, Reflect> instanceFields, String name, Object value) {
        Reflect field = instanceFields.get(name);
        if (field != null) {
            verifyType(field, name, value);
            templateInstance.call(name, value);
        }
    }

    private void verifyType(Reflect field, String name, Object value) {
        Class<?> valueClass = Reflect.on(value)
                .type();

        if (!valueClass.equals(field.type())) {
            throw new IllegalArgumentException("Type does not match for parameter " + name + ". Expected: " + field.type().toString() + " Received: " + valueClass.getName());
        }
    }
}
