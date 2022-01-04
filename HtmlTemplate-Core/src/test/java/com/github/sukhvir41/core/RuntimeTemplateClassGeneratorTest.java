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

import com.github.sukhvir41.core.classgenerator.RuntimeClassGenerator;
import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.statements.PlainStringRenderBodyStatement;
import com.github.sukhvir41.core.template.RuntimeSubTemplate;
import com.github.sukhvir41.core.template.RuntimeTemplate;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.core.tags.HtmlTag;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Path;

public class RuntimeTemplateClassGeneratorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void onlyWithNormalVariables() {
        RuntimeClassGenerator classGenerator =
                new RuntimeClassGenerator("com.github.sukhvir41.core.test", "TestClass");

        Template template = Mockito.mock(Template.class);
        Mockito.when(template.getRootTemplate()).thenReturn(template);

        classGenerator.addVariable(template, "String", "greeting");

        classGenerator.appendPlainHtml(template, "<html>");
        classGenerator.addHtmlTag(new DummyHtmlTag());
        classGenerator.appendPlainHtml(template, "<h1>");
        classGenerator.addHtmlTag(new DummyHtmlTag());
        classGenerator.appendPlainHtmlIndentation(template);
        classGenerator
                .addStatement(template, new PlainStringRenderBodyStatement(classGenerator.getWriterVariableName() + ".append(content(() -> String.valueOf(greeting())));"));
        classGenerator.popHtmlTag();
        classGenerator.appendPlainHtmlNewLine(template);

        classGenerator.appendPlainHtml(template, "</h1>");

        classGenerator.popHtmlTag();
        classGenerator.appendPlainHtml(template, "</html>");

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
    public void with1levelDeep1SubTemplate() throws IOException {

        Template parentTemplate = Mockito.mock(RuntimeTemplate.class);

        Mockito.when(parentTemplate.getRootTemplate())
                .thenReturn(parentTemplate);

        Path file = folder.newFile("TestClass").toPath();

        TemplateClassGenerator classGenerator =
                new RuntimeClassGenerator("com.github.sukhvir41.core.test", "TestClass");

        classGenerator.addVariable(parentTemplate, "String", "greetingParent");
        classGenerator.addVariable(parentTemplate, "String", "greetingChild");

        classGenerator.appendPlainHtml(parentTemplate, "<html>");
        classGenerator.addHtmlTag(new DummyHtmlTag());
        classGenerator.appendPlainHtml(parentTemplate, "<h1>");
        classGenerator.addHtmlTag(new DummyHtmlTag());
        classGenerator.appendPlainHtmlIndentation(parentTemplate);
        classGenerator
                .addStatement(parentTemplate, new PlainStringRenderBodyStatement(classGenerator.getWriterVariableName() + ".append(content(() -> String.valueOf(greetingParent())));"));
        classGenerator.appendPlainHtmlNewLine(parentTemplate);
        classGenerator.appendPlainHtmlIndentation(parentTemplate);

        Template subTemplate = new RuntimeSubTemplate(file, parentTemplate);
        classGenerator.addStatement(parentTemplate, new PlainStringRenderBodyStatement(
                subTemplate.getFullyQualifiedName() + "(" + classGenerator.getWriterVariableName() + ",greetingChild());"
        ));
        classGenerator
                .addStatement(subTemplate, new PlainStringRenderBodyStatement(classGenerator.getWriterVariableName() + ".append(content(() -> String.valueOf(greetingChild)));"));


        classGenerator.addVariable(subTemplate, "String", "greetingChild");

        classGenerator.popHtmlTag();
        classGenerator.appendPlainHtmlNewLine(parentTemplate);

        classGenerator.appendPlainHtml(parentTemplate, "</h1>");

        classGenerator.popHtmlTag();
        classGenerator.appendPlainHtml(parentTemplate, "</html>");


        String generatedClass = classGenerator.render();
        System.out.println(generatedClass);
        Reflect compiledClass = Reflect.compile("com.github.sukhvir41.core.test.TestClass", generatedClass);

        Reflect instance = compiledClass.call("getInstance");

        instance.call("greetingParent", "Hello Universe!");
        instance.call("greetingChild", "Hello World!");

        Reflect renderedString = instance.call("render");
        Assert.assertEquals("<html>\n" +
                "\t<h1>\n" +
                "\t\tHello Universe!\n" +
                "\t\tHello World!\n" +
                "\t</h1>\n" +
                "</html>\n", renderedString.toString());

    }

    private static class DummyHtmlTag implements HtmlTag {

        @Override
        public void processOpeningTag(TemplateClassGenerator classGenerator) {

        }

        @Override
        public void processClosingTag(TemplateClassGenerator classGenerator) {

        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public boolean isClosingTag() {
            return false;
        }

        @Override
        public boolean isSelfClosing() {
            return false;
        }

        @Override
        public boolean isDocTypeTag() {
            return false;
        }
    }
}
