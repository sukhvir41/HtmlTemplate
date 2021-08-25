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

import com.github.sukhvir41.core.classgenerator.TemplateClassGeneratorOLD;
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

    RegularHtmlTag regularHtmlTag = new RegularHtmlTag("<h1 attribute = \"value\" >");

    @Mock
    private TemplateClassGeneratorOLD classGenerator;

    @Test
    public void testClosing() {
        regularHtmlTag.processClosingTag(classGenerator);
        Mockito.verifyNoInteractions(classGenerator);
    }


    @Test
    public void testProcessTag() {
        regularHtmlTag.processOpeningTag(classGenerator);
        Mockito.verify(classGenerator)
                .appendPlainHtml(addPlainHtmlCapture.capture());
        assertEquals("<h1 attribute = \"value\" >", addPlainHtmlCapture.getValue());

    }

}
