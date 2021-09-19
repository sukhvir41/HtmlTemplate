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
@PrepareForTest(ElseHtmlTag.class)
public class ElseHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    public ArgumentCaptor<RenderBodyStatement> addCodeCapture;

    @Mock
    public DynamicAttributeHtmlTag dynamicAttributeHtmlTag;

    @Mock
    public TemplateClassGenerator classGenerator;

    @Mock
    public Template template;

    @Captor
    private ArgumentCaptor<Template> instantiatingTemplateCapture;

    @Before
    public void beforeTest() throws Exception {
        Mockito.doNothing().when(dynamicAttributeHtmlTag)
                .processOpeningTag(classGenerator);

        PowerMockito.whenNew(DynamicAttributeHtmlTag.class)
                .withArguments(ArgumentMatchers.anyString(), ArgumentMatchers.any(Template.class), ArgumentMatchers.any(Function.class))
                .thenReturn(dynamicAttributeHtmlTag);
    }

    @Test
    public void testOpeningProcess() throws Exception {
        Function<String, String> codeParser = Code::parseForFunction;

        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());
        ElseHtmlTag elseTag = new ElseHtmlTag("<h1 ht-else >", template, codeParser);

        elseTag.processOpeningTag(classGenerator);

        Mockito.verify(classGenerator)
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());
        assertEquals("else {", addCodeCapture.getValue().getStatement());

        Mockito.verify(classGenerator)
                .incrementRenderBodyIndentation(instantiatingTemplateCapture.capture());

        PowerMockito.verifyNew(DynamicAttributeHtmlTag.class)
                .withArguments("<h1 >", template, codeParser);

        Mockito.verify(dynamicAttributeHtmlTag)
                .processOpeningTag(classGenerator);
    }

    @Test
    public void testClosingProcess() {
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());
        ElseHtmlTag elseTag = new ElseHtmlTag("<h1 ht-else >", template, Code::parseForFunction);

        elseTag.processClosingTag(classGenerator);

        Mockito.verify(classGenerator)
                .addStatement(instantiatingTemplateCapture.capture(), addCodeCapture.capture());
        assertEquals("}", addCodeCapture.getValue().getStatement());

        Mockito.verify(classGenerator)
                .decrementRenderBodyIndentation(instantiatingTemplateCapture.capture());

    }

}
