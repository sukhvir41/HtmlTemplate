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

package com.github.sukhvir41.tags;

import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.settings.SettingsManager;
import com.github.sukhvir41.core.statements.RenderBodyStatement;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.parsers.Code;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.*;

public class DynamicAttributeHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> addPlainHtmlCapture;

    @Captor
    private ArgumentCaptor<RenderBodyStatement> addCodeCapture;

    @Captor
    private ArgumentCaptor<Boolean> appendIndentationCapture;

    @Captor
    private ArgumentCaptor<Template> instantiatingTemplateCapture;

    @Captor
    private ArgumentCaptor<Boolean> appendNewLineCapture;


    @Test
    public void testClosing() {
        TemplateClassGenerator templateClass = Mockito.mock(TemplateClassGenerator.class);
        Template template = Mockito.mock(Template.class);
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());
        DynamicAttributeHtmlTag dynamicTag = new DynamicAttributeHtmlTag(
                "<h1 ht-test = \"@testValue\" ht-test1 = \"@test1Value\" >",
                template,
                Code::parseForFunction);

        dynamicTag.processClosingTag(templateClass);

        Mockito.verifyNoInteractions(templateClass);

    }

    @Test
    public void testOpeningTag() {
        Template template = Mockito.mock(Template.class);
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());

        TemplateClassGenerator classGenerator = Mockito.mock(TemplateClassGenerator.class);

        Mockito.when(classGenerator.getWriterVariableName())
                .thenReturn("testWriter");

        DynamicAttributeHtmlTag dynamicTag = new DynamicAttributeHtmlTag(
                "<h1 ht-test = \"@testValue\" ht-test1 = \"@test1Value\" >",
                template,
                Code::parseForFunction);

        dynamicTag.processOpeningTag(classGenerator);

        Mockito.verify(classGenerator, Mockito.times(6))
                .appendPlainHtml(instantiatingTemplateCapture.capture(), addPlainHtmlCapture.capture(), appendIndentationCapture.capture(), appendNewLineCapture.capture());

        var capturedPlainHtml = addPlainHtmlCapture.getAllValues();
        var capturedIndentation = appendIndentationCapture.getAllValues();
        var capturedNewLine = appendNewLineCapture.getAllValues();

        assertEquals("<h1 ", capturedPlainHtml.get(0));
        assertTrue(capturedIndentation.get(0));
        assertFalse(capturedNewLine.get(0));
        assertEquals("test = \"", capturedPlainHtml.get(1));
        assertFalse(capturedIndentation.get(1));
        assertFalse(capturedNewLine.get(1));
        assertEquals("\" ", capturedPlainHtml.get(2));
        assertFalse(capturedIndentation.get(2));
        assertFalse(capturedNewLine.get(2));
        assertEquals("test1 = \"", capturedPlainHtml.get(3));
        assertFalse(capturedIndentation.get(3));
        assertFalse(capturedNewLine.get(3));
        assertEquals("\" ", capturedPlainHtml.get(4));
        assertFalse(capturedIndentation.get(4));
        assertFalse(capturedNewLine.get(4));
        assertEquals(" >", capturedPlainHtml.get(5));
        assertFalse(capturedIndentation.get(5));
        assertTrue(capturedNewLine.get(5));


        Mockito.verify(classGenerator, Mockito.times(2))
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());

        var codes = addCodeCapture.getAllValues();
        assertEquals("testWriter.write(content(() -> String.valueOf(testValue())));", codes.get(0).getStatement());
        assertEquals("testWriter.write(content(() -> String.valueOf(test1Value())));", codes.get(1).getStatement());


    }


}
