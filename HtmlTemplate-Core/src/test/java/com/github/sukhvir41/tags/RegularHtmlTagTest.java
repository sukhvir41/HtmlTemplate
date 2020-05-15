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

import com.github.sukhvir41.template.TemplateClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

public class RegularHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> addPlainHtmlCapture;

    RegularHtmlTag regularHtmlTag = new RegularHtmlTag(" <h1 attribute = \"value\" >");

    @Test
    public void testOpeningAndClosing() {
        TemplateClass templateClass = Mockito.mock(TemplateClass.class);

        regularHtmlTag.processOpeningTag(templateClass);
        Mockito.verifyNoInteractions(templateClass);

        regularHtmlTag.processClosingTag(templateClass);
        Mockito.verifyNoInteractions(templateClass);
    }


    @Test
    public void testProcessTag() {
        TemplateClass templateClass = Mockito.mock(TemplateClass.class);

        regularHtmlTag.processTag(templateClass);
        Mockito.verify(templateClass)
                .appendPlainHtml(addPlainHtmlCapture.capture());
        assertEquals("<h1 attribute = \"value\" >", addPlainHtmlCapture.getValue());

    }

}