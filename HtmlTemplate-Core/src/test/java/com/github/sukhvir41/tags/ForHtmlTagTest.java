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
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ForHtmlTag.class)
public class ForHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<RenderBodyStatement> addCodeCapture;

    @Captor
    private ArgumentCaptor<Template> instantiatingTemplateCapture;

    @Mock
    public Template template;

    @Mock
    public TemplateClassGenerator templateClass;

    @Mock
    public DynamicAttributeHtmlTag dynamicAttributeHtmlTag;

    @Before
    public void beforeTest() throws Exception {
        Mockito.doNothing()
                .when(dynamicAttributeHtmlTag)
                .processOpeningTag(templateClass);

        PowerMockito.whenNew(DynamicAttributeHtmlTag.class)
                .withArguments(ArgumentMatchers.anyString(), ArgumentMatchers.any(Template.class), ArgumentMatchers.any(Function.class))
                .thenReturn(dynamicAttributeHtmlTag);

        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());
    }

    @Test
    public void valueTest() throws Exception {
        Function<String, String> codeParser = Code::parseForFunction;
        ForHtmlTag forTag = new ForHtmlTag("<h1 test=\"test\" ht-for=\"name in @names\" test=\"test\">", template, codeParser);
        forTag.processOpeningTag(templateClass);
        forTag.processClosingTag(templateClass);

        Mockito.verify(templateClass)
                .incrementRenderBodyIndentation(instantiatingTemplateCapture.capture());
        Mockito.verify(templateClass)
                .decrementRenderBodyIndentation(instantiatingTemplateCapture.capture());
        Mockito.verify(templateClass, Mockito.times(2))
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());


        var codes = addCodeCapture.getAllValues();
        assertEquals("forEach(names(), (name) -> {", codes.get(0).getStatement());
        assertEquals("});", codes.get(1).getStatement());

        PowerMockito.verifyNew(DynamicAttributeHtmlTag.class)
                .withArguments("<h1 test=\"test\" test=\"test\">", template, codeParser);

    }

    @Test
    public void indexValueTest() throws Exception {
        Function<String, String> codeParser = Code::parseForFunction;
        var forTag = new ForHtmlTag("<h1 ht-for=\"index,name in @names\" >", template, codeParser);
        forTag.processOpeningTag(templateClass);
        forTag.processClosingTag(templateClass);

        Mockito.verify(templateClass)
                .incrementRenderBodyIndentation(instantiatingTemplateCapture.capture());
        Mockito.verify(templateClass)
                .decrementRenderBodyIndentation(instantiatingTemplateCapture.capture());
        Mockito.verify(templateClass, Mockito.times(2))
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());


        var codes = addCodeCapture.getAllValues();
        assertEquals("forEach(names(), (index, name) -> {", codes.get(0).getStatement());

        assertEquals("});", codes.get(1).getStatement());

        PowerMockito.verifyNew(DynamicAttributeHtmlTag.class)
                .withArguments("<h1 >", template, codeParser);
    }

    @Test
    public void MapTest() throws Exception {
        Function<String, String> codeParser = Code::parseForFunction;
        var forTag = new ForHtmlTag("<h1 ht-for=\"index,key,value in @names\">", template, codeParser);
        forTag.processOpeningTag(templateClass);
        forTag.processClosingTag(templateClass);

        Mockito.verify(templateClass)
                .incrementRenderBodyIndentation(instantiatingTemplateCapture.capture());
        Mockito.verify(templateClass)
                .decrementRenderBodyIndentation(instantiatingTemplateCapture.capture());
        Mockito.verify(templateClass, Mockito.times(2))
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());


        var codes = addCodeCapture.getAllValues();
        assertEquals("forEach(names(), (index, key, value) -> {", codes.get(0).getStatement());
        assertEquals("});", codes.get(1).getStatement());

        PowerMockito.verifyNew(DynamicAttributeHtmlTag.class)
                .withArguments("<h1>", template, codeParser);
    }

}
