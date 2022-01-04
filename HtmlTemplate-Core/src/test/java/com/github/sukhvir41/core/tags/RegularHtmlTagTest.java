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
import com.github.sukhvir41.core.settings.SettingsManager;
import com.github.sukhvir41.core.template.Template;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

public class RegularHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> addPlainHtmlCapture;

    @Captor
    private ArgumentCaptor<Template> instantiatingTemplateCapture;

    @Mock
    public Template template;

    @Mock
    private TemplateClassGenerator classGenerator;

    @Before
    public void setup() {
        Mockito.when(template.getSettings())
                .thenReturn(SettingsManager.load());
    }

    @Test
    public void testClosing() {
        RegularHtmlTag regularHtmlTag = new RegularHtmlTag("<h1 attribute = \"value\" >", template);
        regularHtmlTag.processClosingTag(classGenerator);
        Mockito.verifyNoInteractions(classGenerator);
    }


    @Test
    public void testProcessTag() {
        RegularHtmlTag regularHtmlTag = new RegularHtmlTag("<h1 attribute = \"value\" >", template);
        regularHtmlTag.processOpeningTag(classGenerator);
        Mockito.verify(classGenerator)
                .appendPlainHtml(instantiatingTemplateCapture.capture(), addPlainHtmlCapture.capture());
        assertEquals("<h1 attribute = \"value\" >", addPlainHtmlCapture.getValue());

    }

}
