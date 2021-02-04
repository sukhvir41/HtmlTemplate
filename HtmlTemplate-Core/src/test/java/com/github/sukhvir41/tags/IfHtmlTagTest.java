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

import static org.junit.Assert.*;

import com.github.sukhvir41.newCore.TemplateClassGenerator;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest(IfHtmlTag.class)
public class IfHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> addCodeCapture;

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
                .withArguments(ArgumentMatchers.anyString())
                .thenReturn(dynamicAttributeHtmlTag);
    }

    @Test
    public void test1() throws Exception {


        IfHtmlTag ifHtmlTag = new IfHtmlTag("<h1 ht-if=\"@isTrue\">");

        ifHtmlTag.processOpeningTag(classGenerator);

        Mockito.verify(classGenerator)
                .addCode(addCodeCapture.capture());
        Mockito.verify(classGenerator)
                .incrementRenderFunctionIndentation();
        assertEquals("if(condition( () -> isTrue() )){", addCodeCapture.getValue());

        ifHtmlTag.processClosingTag(classGenerator);

        Mockito.verify(classGenerator, Mockito.times(2))
                .addCode(addCodeCapture.capture());
        Mockito.verify(classGenerator)
                .decrementRenderFunctionIndentation();
        assertEquals("}", addCodeCapture.getValue());

        PowerMockito.verifyNew(DynamicAttributeHtmlTag.class)
                .withArguments("<h1>");

    }

    @Test
    public void test2() throws Exception {

        IfHtmlTag ifHtmlTag = new IfHtmlTag("<h1 ht-if=\"@string.indexOf('@test.com') > 10\">");

        ifHtmlTag.processOpeningTag(classGenerator);

        Mockito.verify(classGenerator)
                .addCode(addCodeCapture.capture());
        Mockito.verify(classGenerator)
                .incrementRenderFunctionIndentation();
        assertEquals("if(condition( () -> string().indexOf(\"@test.com\") > 10 )){", addCodeCapture.getValue());

        ifHtmlTag.processClosingTag(classGenerator);

        Mockito.verify(classGenerator, Mockito.times(2))
                .addCode(addCodeCapture.capture());
        Mockito.verify(classGenerator)
                .decrementRenderFunctionIndentation();
        assertEquals("}", addCodeCapture.getValue());

        PowerMockito.verifyNew(DynamicAttributeHtmlTag.class)
                .withArguments("<h1>");

    }

}
