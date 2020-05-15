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

public class ForHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> addCodeCapture;

    @Test
    public void valueTest() {
        var templateClass = Mockito.mock(TemplateClass.class);

        var forTag = new ForHtmlTag("<h1 ht-for=\"name in @names\"");
        forTag.processOpeningTag(templateClass);
        forTag.processClosingTag(templateClass);

        Mockito.verify(templateClass)
                .incrementFunctionIndentation();
        Mockito.verify(templateClass)
                .decrementFunctionIndentation();
        Mockito.verify(templateClass, Mockito.times(2))
                .addCode(addCodeCapture.capture());


        var codes = addCodeCapture.getAllValues();
        assertEquals("forEach(names(), (name) -> {", codes.get(0));
        assertEquals("});", codes.get(1));
    }

    @Test
    public void indexValueTest() {
        var templateClass = Mockito.mock(TemplateClass.class);

        var forTag = new ForHtmlTag("<h1 ht-for=\"index,name in @names\"");
        forTag.processOpeningTag(templateClass);
        forTag.processClosingTag(templateClass);

        Mockito.verify(templateClass)
                .incrementFunctionIndentation();
        Mockito.verify(templateClass)
                .decrementFunctionIndentation();
        Mockito.verify(templateClass, Mockito.times(2))
                .addCode(addCodeCapture.capture());


        var codes = addCodeCapture.getAllValues();
        assertEquals("forEach(names(), (index, name) -> {", codes.get(0));
        assertEquals("});", codes.get(1));
    }

    @Test
    public void MapTest() {
        var templateClass = Mockito.mock(TemplateClass.class);

        var forTag = new ForHtmlTag("<h1 ht-for=\"index,key,value in @names\"");
        forTag.processOpeningTag(templateClass);
        forTag.processClosingTag(templateClass);

        Mockito.verify(templateClass)
                .incrementFunctionIndentation();
        Mockito.verify(templateClass)
                .decrementFunctionIndentation();
        Mockito.verify(templateClass, Mockito.times(2))
                .addCode(addCodeCapture.capture());


        var codes = addCodeCapture.getAllValues();
        assertEquals("forEach(names(), (index, key, value) -> {", codes.get(0));
        assertEquals("});", codes.get(1));
    }

}
