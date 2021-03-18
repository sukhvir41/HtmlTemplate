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

import com.github.sukhvir41.core.TemplateClassGenerator;
import com.github.sukhvir41.core.statements.RenderBodyStatement;
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

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ElseIfHtmlTag.class)
public class ElseIfHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<RenderBodyStatement> addCodeCapture;

    @Mock
    private DynamicAttributeHtmlTag dynamicAttributeHtmlTag;

    @Mock
    private TemplateClassGenerator templateClass;

    @Before
    public void beforeTest() throws Exception {
        Mockito.doNothing().when(dynamicAttributeHtmlTag)
                .processOpeningTag(templateClass);

        PowerMockito.whenNew(DynamicAttributeHtmlTag.class)
                .withArguments(ArgumentMatchers.anyString())
                .thenReturn(dynamicAttributeHtmlTag);
    }


    @Test
    public void testOpeningProcess() throws Exception {

        ElseIfHtmlTag elseIfTag = new ElseIfHtmlTag("<h1 ht-elseIf = \"@test\">");

        elseIfTag.processOpeningTag(templateClass);

        Mockito.verify(templateClass)
                .addCode(addCodeCapture.capture());
        assertEquals("else if (condition(() -> test())) {", addCodeCapture.getValue().getStatement());

        Mockito.verify(templateClass)
                .incrementRenderFunctionIndentation();

        PowerMockito.verifyNew(DynamicAttributeHtmlTag.class)
                .withArguments("<h1>");

        Mockito.verify(dynamicAttributeHtmlTag)
                .processOpeningTag(templateClass);
    }

    @Test
    public void testClosingProcess() {

        ElseIfHtmlTag elseIfTag = new ElseIfHtmlTag("<h1 ht-elseIf = \"@test\">");

        elseIfTag.processClosingTag(templateClass);
        Mockito.verify(templateClass)
                .decrementRenderFunctionIndentation();

        Mockito.verify(templateClass)
                .addCode(addCodeCapture.capture());
        assertEquals("}", addCodeCapture.getValue().getStatement());

    }
}
