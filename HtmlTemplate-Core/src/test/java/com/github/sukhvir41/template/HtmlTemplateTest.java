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

import com.github.sukhvir41.TestUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class HtmlTemplateTest {


    @Test
    public void testRenderWithoutArguments() {
        Reflect htTemplateClass = TestUtils.getTestReflectClass();

        HtmlTemplate htmlTemplate = new HtmlTemplate(htTemplateClass);
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("name", "person1");
        String html = htmlTemplate.render(parameters);
        Assert.assertEquals("Greetings person1", html);

        parameters.put("name", "person2");
        html = htmlTemplate.render(parameters);
        Assert.assertEquals("Greetings person2", html);
    }

    @Test
    public void testRenderWithArguments() {
        Reflect htTemplateClass = TestUtils.getTestReflectClass();

        HtmlTemplate htmlTemplate = new HtmlTemplate(htTemplateClass);
        Map<String, Object> parameters = new HashMap<>();

        Writer writer = new StringBuilderWriter();
        parameters.put("name", "person1");
        htmlTemplate.render(parameters, writer);
        Assert.assertEquals("Greetings person1", writer.toString());

        writer = new StringBuilderWriter();
        parameters.put("name", "person2");
        htmlTemplate.render(parameters, writer);
        Assert.assertEquals("Greetings person2", writer.toString());
    }

    @Test
    public void testNonExistingParameters() {
        Reflect htTemplateClass = TestUtils.getTestReflectClass();

        HtmlTemplate htmlTemplate = new HtmlTemplate(htTemplateClass);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", "person1");
        parameters.put("age", 1);
        parameters.put("heightInCm", 180.5);

        String html = htmlTemplate.render(parameters);
        Assert.assertEquals("Greetings person1", html);
    }

    @Test
    public void testWrongValueParameter() {

        Reflect htTemplateClass = TestUtils.getTestReflectClass();

        HtmlTemplate htmlTemplate = new HtmlTemplate(htTemplateClass);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", 47);
        try {
            String html = htmlTemplate.render(parameters);
        } catch (Exception e) {
            Assert.assertEquals(IllegalArgumentException.class, e.getClass());
            Assert.assertEquals("Type does not match for parameter name. Expected: class java.lang.String Received: java.lang.Integer", e.getMessage());
        }


    }

}
