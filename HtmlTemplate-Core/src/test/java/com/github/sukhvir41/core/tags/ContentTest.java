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

package com.github.sukhvir41.core.tags;

import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.settings.SettingsManager;
import com.github.sukhvir41.core.statements.RenderBodyStatement;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.parsers.Code;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Map;

import static org.junit.Assert.*;

public class ContentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> addPlainHtmlCapture;

    @Captor
    private ArgumentCaptor<RenderBodyStatement> addCodeCapture;

    @Captor
    private ArgumentCaptor<Boolean> appendIndentationCapture;

    @Captor
    private ArgumentCaptor<Boolean> appendNewLineCapture;

    @Captor
    private ArgumentCaptor<Template> instantiatingTemplateCapture;

    @Test
    public void plainTest() {
        TemplateClassGenerator classGenerator = Mockito.mock(TemplateClassGenerator.class);
        Template template = Mockito.mock(Template.class);
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());

        var content = new Content(" some content ", template, Code::parseForFunction);
        content.processOpeningTag(classGenerator);

        Mockito.verify(classGenerator)
                .appendPlainHtml(instantiatingTemplateCapture.capture(), addPlainHtmlCapture.capture());

        assertEquals(" some content ", addPlainHtmlCapture.getValue());
        assertEquals(template, instantiatingTemplateCapture.getValue());
    }

    @Test
    public void escapedCodeTest() {
        TemplateClassGenerator classGenerator = Mockito.mock(TemplateClassGenerator.class);
        Template template = Mockito.mock(Template.class);
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());

        Mockito.when(classGenerator.getWriterVariableName())
                .thenReturn("testWriter");


        var content = new Content(" content1 {{ \"some content2\" }} content3 {{ \"<h1> content4 </h1>\" }}", template, Code::parseForFunction);
        content.processOpeningTag(classGenerator);

        //testing plain content part
        Mockito.verify(classGenerator, Mockito.times(2))
                .appendPlainHtml(instantiatingTemplateCapture.capture(), addPlainHtmlCapture.capture(), appendIndentationCapture.capture(), appendNewLineCapture.capture());
        var capturedPlainHtml = addPlainHtmlCapture.getAllValues();
        var capturedIndentation = appendIndentationCapture.getAllValues();
        var capturedNewLine = appendNewLineCapture.getAllValues();
        assertEquals(" content1 ", capturedPlainHtml.get(0));
        assertTrue(capturedIndentation.get(0));
        assertFalse(capturedNewLine.get(0));
        assertEquals(" content3 ", capturedPlainHtml.get(1));
        assertFalse(capturedIndentation.get(1));
        assertFalse(capturedNewLine.get(1));
        assertEquals(template, instantiatingTemplateCapture.getValue());


        //testing escaped dynamic content part
        Mockito.verify(classGenerator, Mockito.times(2))
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());
        var addedCodes = addCodeCapture.getAllValues();
        assertEquals("testWriter.write(content(() -> String.valueOf(\"some content2\")));", addedCodes.get(0).getStatement());
        assertEquals("testWriter.write(content(() -> String.valueOf(\"<h1> content4 </h1>\")));", addedCodes.get(1).getStatement());
        //testing new line added at end
        Mockito.verify(classGenerator)
                .appendPlainHtmlNewLine(instantiatingTemplateCapture.capture());
    }

    @Test
    public void escapedCodeSuppressExceptionsTest() {
        TemplateClassGenerator classGenerator = Mockito.mock(TemplateClassGenerator.class);
        Template template = Mockito.mock(Template.class);
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load(Map.of(SettingOptions.SUPPRESS_EXCEPTIONS, false)));

        Mockito.when(classGenerator.getWriterVariableName())
                .thenReturn("testWriter");


        var content = new Content(" content1 {{ \"some content2\" }} content3 {{ \"<h1> content4 </h1>\" }}", template, Code::parseForFunction);
        content.processOpeningTag(classGenerator);

        //testing plain content part
        Mockito.verify(classGenerator, Mockito.times(2))
                .appendPlainHtml(instantiatingTemplateCapture.capture(), addPlainHtmlCapture.capture(), appendIndentationCapture.capture(), appendNewLineCapture.capture());
        var capturedPlainHtml = addPlainHtmlCapture.getAllValues();
        var capturedIndentation = appendIndentationCapture.getAllValues();
        var capturedNewLine = appendNewLineCapture.getAllValues();
        assertEquals(" content1 ", capturedPlainHtml.get(0));
        assertTrue(capturedIndentation.get(0));
        assertFalse(capturedNewLine.get(0));
        assertEquals(" content3 ", capturedPlainHtml.get(1));
        assertFalse(capturedIndentation.get(1));
        assertFalse(capturedNewLine.get(1));
        assertEquals(template, instantiatingTemplateCapture.getValue());


        //testing escaped dynamic content part
        Mockito.verify(classGenerator, Mockito.times(2))
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());
        var addedCodes = addCodeCapture.getAllValues();
        assertEquals("testWriter.write(content(\"some content2\"));", addedCodes.get(0).getStatement());
        assertEquals("testWriter.write(content(\"<h1> content4 </h1>\"));", addedCodes.get(1).getStatement());
        //testing new line added at end
        Mockito.verify(classGenerator)
                .appendPlainHtmlNewLine(instantiatingTemplateCapture.capture());
    }

    @Test
    public void unescapedCodeTest() {
        TemplateClassGenerator classGenerator = Mockito.mock(TemplateClassGenerator.class);

        Mockito.when(classGenerator.getWriterVariableName())
                .thenReturn("testWriter");

        Template template = Mockito.mock(Template.class);
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());

        var content = new Content(" content1 {{ \"some content2\" }} content3 {{{ \"<h1> content4 </h1>\" }}}", template, Code::parseForFunction);
        content.processOpeningTag(classGenerator);

        //testing plain content part
        Mockito.verify(classGenerator, Mockito.times(2))
                .appendPlainHtml(instantiatingTemplateCapture.capture(), addPlainHtmlCapture.capture(), appendIndentationCapture.capture(), appendNewLineCapture.capture());
        var capturedPlainHtml = addPlainHtmlCapture.getAllValues();
        var capturedIndentation = appendIndentationCapture.getAllValues();
        var capturedNewLine = appendNewLineCapture.getAllValues();
        assertEquals(" content1 ", capturedPlainHtml.get(0));
        assertTrue(capturedIndentation.get(0));
        assertFalse(capturedNewLine.get(0));
        assertEquals(" content3 ", capturedPlainHtml.get(1));
        assertFalse(capturedIndentation.get(1));
        assertFalse(capturedNewLine.get(1));


        //testing escaped dynamic content part
        Mockito.verify(classGenerator, Mockito.times(2))
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());
        var addedCodes = addCodeCapture.getAllValues();
        assertEquals("testWriter.write(content(() -> String.valueOf(\"some content2\")));", addedCodes.get(0).getStatement());
        assertEquals("testWriter.write(unescapedContent(() -> String.valueOf(\"<h1> content4 </h1>\")));", addedCodes.get(1).getStatement());

        //testing new line added at end
        Mockito.verify(classGenerator)
                .appendPlainHtmlNewLine(template);
    }

    @Test
    public void unescapedCodeSuppressExceptionsTest() {
        TemplateClassGenerator classGenerator = Mockito.mock(TemplateClassGenerator.class);

        Mockito.when(classGenerator.getWriterVariableName())
                .thenReturn("testWriter");

        Template template = Mockito.mock(Template.class);
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load(Map.of(SettingOptions.SUPPRESS_EXCEPTIONS, false)));

        var content = new Content(" content1 {{ \"some content2\" }} content3 {{{ \"<h1> content4 </h1>\" }}}", template, Code::parseForFunction);
        content.processOpeningTag(classGenerator);

        //testing plain content part
        Mockito.verify(classGenerator, Mockito.times(2))
                .appendPlainHtml(instantiatingTemplateCapture.capture(), addPlainHtmlCapture.capture(), appendIndentationCapture.capture(), appendNewLineCapture.capture());
        var capturedPlainHtml = addPlainHtmlCapture.getAllValues();
        var capturedIndentation = appendIndentationCapture.getAllValues();
        var capturedNewLine = appendNewLineCapture.getAllValues();
        assertEquals(" content1 ", capturedPlainHtml.get(0));
        assertTrue(capturedIndentation.get(0));
        assertFalse(capturedNewLine.get(0));
        assertEquals(" content3 ", capturedPlainHtml.get(1));
        assertFalse(capturedIndentation.get(1));
        assertFalse(capturedNewLine.get(1));


        //testing escaped dynamic content part
        Mockito.verify(classGenerator, Mockito.times(2))
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());
        var addedCodes = addCodeCapture.getAllValues();
        assertEquals("testWriter.write(content(\"some content2\"));", addedCodes.get(0).getStatement());
        assertEquals("testWriter.write(unescapedContent(\"<h1> content4 </h1>\"));", addedCodes.get(1).getStatement());

        //testing new line added at end
        Mockito.verify(classGenerator)
                .appendPlainHtmlNewLine(template);
    }

}
