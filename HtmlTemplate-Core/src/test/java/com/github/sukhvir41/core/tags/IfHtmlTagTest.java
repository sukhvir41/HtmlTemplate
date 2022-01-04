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

import static org.junit.Assert.*;

import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.settings.SettingsManager;
import com.github.sukhvir41.core.statements.RenderBodyStatement;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.parsers.Code;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.function.Function;

@RunWith(PowerMockRunner.class)
@PrepareForTest(IfHtmlTag.class)
public class IfHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<RenderBodyStatement> addCodeCapture;

    @Captor
    private ArgumentCaptor<Template> instantiatingTemplateCapture;

    @Mock
    public Template template;

    @Mock
    public TemplateClassGenerator classGenerator;

    @Mock
    public DynamicAttributeHtmlTag dynamicAttributeHtmlTag;

    @Before
    public void beforeTest() throws Exception {
        Mockito.doNothing()
                .when(dynamicAttributeHtmlTag)
                .processOpeningTag(classGenerator);

        PowerMockito.whenNew(DynamicAttributeHtmlTag.class)
                .withArguments(ArgumentMatchers.anyString(), ArgumentMatchers.any(Template.class), ArgumentMatchers.any(Function.class))
                .thenReturn(dynamicAttributeHtmlTag);
    }

    @Test
    public void test1() throws Exception {
        Function<String, String> codeParser = Code::parseForFunction;
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());

        IfHtmlTag ifHtmlTag = new IfHtmlTag("<h1 ht-if=\"@isTrue\">", template, codeParser);

        ifHtmlTag.processOpeningTag(classGenerator);

        Mockito.verify(classGenerator)
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());
        Mockito.verify(classGenerator)
                .incrementRenderBodyIndentation(instantiatingTemplateCapture.capture());
        assertEquals("if (condition(() -> isTrue())) {", addCodeCapture.getValue().getStatement());

        ifHtmlTag.processClosingTag(classGenerator);

        Mockito.verify(classGenerator, Mockito.times(2))
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());
        Mockito.verify(classGenerator)
                .decrementRenderBodyIndentation(instantiatingTemplateCapture.capture());
        assertEquals("}", addCodeCapture.getValue().getStatement());

        PowerMockito.verifyNew(DynamicAttributeHtmlTag.class)
                .withArguments("<h1>", template, codeParser);

    }

    @Test
    public void test2() throws Exception {
        Function<String, String> codeParser = Code::parseForFunction;
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());

        IfHtmlTag ifHtmlTag = new IfHtmlTag("<h1 ht-if=\"@string.indexOf('@test.com') > 10\">", template, codeParser);

        ifHtmlTag.processOpeningTag(classGenerator);

        Mockito.verify(classGenerator)
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());
        Mockito.verify(classGenerator)
                .incrementRenderBodyIndentation(instantiatingTemplateCapture.capture());
        assertEquals("if (condition(() -> string().indexOf(\"@test.com\") > 10)) {", addCodeCapture.getValue().getStatement());

        ifHtmlTag.processClosingTag(classGenerator);

        Mockito.verify(classGenerator, Mockito.times(2))
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());
        Mockito.verify(classGenerator)
                .decrementRenderBodyIndentation(instantiatingTemplateCapture.capture());
        assertEquals("}", addCodeCapture.getValue().getStatement());

        PowerMockito.verifyNew(DynamicAttributeHtmlTag.class)
                .withArguments("<h1>", template, codeParser);

    }

}
