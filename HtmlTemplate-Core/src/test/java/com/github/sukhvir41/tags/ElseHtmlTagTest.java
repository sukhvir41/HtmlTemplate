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

public class ElseHtmlTagTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> addCodeCapture;

    @Test
    public void test() {
        var templateClass = Mockito.mock(TemplateClass.class);
        var elseTag = new ElseHtmlTag(" <h1 ht-else >");

        elseTag.processOpeningTag(templateClass);
        Mockito.verify(templateClass)
                .addCode(addCodeCapture.capture());
        Mockito.verify(templateClass)
                .incrementFunctionIndentation();
        assertEquals("else {", addCodeCapture.getValue());

        elseTag.processClosingTag(templateClass);
        Mockito.verify(templateClass, Mockito.times(2))
                .addCode(addCodeCapture.capture());
        Mockito.verify(templateClass)
                .decrementFunctionIndentation();
        assertEquals("}", addCodeCapture.getValue());

    }


}
