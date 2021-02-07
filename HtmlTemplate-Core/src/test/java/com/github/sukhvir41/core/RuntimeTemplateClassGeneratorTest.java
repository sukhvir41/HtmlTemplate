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

package com.github.sukhvir41.core;

import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class RuntimeTemplateClassGeneratorTest {

    @Test
    public void onlyWithNormalVariables() {
        RuntimeTemplateClassGenerator classGenerator =
                new RuntimeTemplateClassGenerator("com.github.sukhvir41.core.test", "TestClass");

        classGenerator.addVariable("String", "greeting");

        classGenerator.appendPlainHtml("<html>");
        classGenerator.addTagToStack(null);
        classGenerator.appendPlainHtml("<h1>");
        classGenerator.addTagToStack(null);
        classGenerator.appendPlainHtmlIndentation();
        classGenerator
                .addCode(classGenerator.getWriterVariableName() + ".append(content(() -> String.valueOf(greeting())));");
        classGenerator.removeFromTagStack();
        classGenerator.appendPlainHtmlNewLine();

        classGenerator.appendPlainHtml("</h1>");

        classGenerator.removeFromTagStack();
        classGenerator.appendPlainHtml("</html>");


        String generatedClass = classGenerator.render();

        Reflect compiledClass = Reflect.compile("com.github.sukhvir41.core.test.TestClass", generatedClass);

        Reflect instance = compiledClass.call("getInstance");

        instance.call("greeting", "Hello World !");

        Reflect renderedString = instance.call("render");

        Assert.assertEquals("<html>\n" +
                "\t<h1>\n" +
                "\t\tHello World !\n" +
                "\t</h1>\n" +
                "</html>\n", renderedString.toString());


    }

    @Test
    public void with1levelDeep1SubTemplate() {

        Template parentTemplate = Mockito.mock(RuntimeTemplate.class);

        TemplateClassGenerator classGenerator =
                new RuntimeTemplateClassGenerator("com.github.sukhvir41.core.test", "TestClass");

        classGenerator.addVariable("String", "greetingParent");

        classGenerator.appendPlainHtml("<html>");
        classGenerator.addTagToStack(null);
        classGenerator.appendPlainHtml("<h1>");
        classGenerator.addTagToStack(null);
        classGenerator.appendPlainHtmlIndentation();
        classGenerator
                .addCode(classGenerator.getWriterVariableName() + ".append(content(() -> String.valueOf(greetingParent())));");
        classGenerator.appendPlainHtmlNewLine();
        classGenerator.appendPlainHtmlIndentation();
        classGenerator
                .addCode(classGenerator.getWriterVariableName() + ".append(content(() -> String.valueOf(greetingChild())));");

        Template subTemplate = new RuntimeSubTemplate(null, parentTemplate);
        classGenerator.addSubTemplateVariables(subTemplate, "String", "greetingChild");

        classGenerator.removeFromTagStack();
        classGenerator.appendPlainHtmlNewLine();

        classGenerator.appendPlainHtml("</h1>");

        classGenerator.removeFromTagStack();
        classGenerator.appendPlainHtml("</html>");


        String generatedClass = classGenerator.render();
        System.out.println(generatedClass);
        Reflect compiledClass = Reflect.compile("com.github.sukhvir41.core.test.TestClass", generatedClass);

        Reflect instance = compiledClass.call("getInstance");

        instance.call("greetingParent", "Hello Universe!");
        instance.call("greetingChild", "Hello World!");

        Reflect renderedString = instance.call("render");
        System.out.println(renderedString.toString());
        Assert.assertEquals("<html>\n" +
                "\t<h1>\n" +
                "\t\tHello Universe!\n" +
                "\t\tHello World!\n" +
                "\t</h1>\n" +
                "</html>\n", renderedString.toString());

    }
}
