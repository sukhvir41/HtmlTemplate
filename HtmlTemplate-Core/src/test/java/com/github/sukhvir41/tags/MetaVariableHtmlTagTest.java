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
import com.github.sukhvir41.core.template.Template;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

public class MetaVariableHtmlTagTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> typeCaptor;

    @Captor
    private ArgumentCaptor<String> nameCaptor;

    @Captor
    private ArgumentCaptor<Template> instantiatingTemplateCapture;

    @Mock
    public Template template;

    @Mock
    private TemplateClassGenerator classGenerator;

    @Test
    public void testSingleVariable() {
        var metaVariableTag = new MetaVariableHtmlTag("<meta ht-variables=\"String,name\">", template);

        metaVariableTag.processOpeningTag(classGenerator);
        Mockito.verify(classGenerator)
                .addVariable(instantiatingTemplateCapture.capture(), typeCaptor.capture(), nameCaptor.capture());

        assertEquals("String", typeCaptor.getValue());
        assertEquals("name", nameCaptor.getValue());

    }

    @Test
    public void testMultiVariable() {
        var metaVariableTag = new MetaVariableHtmlTag("<meta ht-variables=\"String,name, int, age\" > ", template);

        metaVariableTag.processOpeningTag(classGenerator);
        Mockito.verify(classGenerator, Mockito.times(2))
                .addVariable(instantiatingTemplateCapture.capture(), typeCaptor.capture(), nameCaptor.capture());

        var typeList = typeCaptor.getAllValues();
        var nameList = nameCaptor.getAllValues();

        assertEquals("String", typeList.get(0).trim());
        assertEquals("name", nameList.get(0).trim());
        assertEquals("int", typeList.get(1).trim());
        assertEquals("age", nameList.get(1).trim());
    }

    @Test
    public void testMultiVariableWithBrackets() {
        var metaVariableTag = new MetaVariableHtmlTag("<meta ht-variables=\"String,name, int, age, Map<String,String>, keyNames\" > ", template);

        metaVariableTag.processOpeningTag(classGenerator);
        Mockito.verify(classGenerator, Mockito.times(3))
                .addVariable(instantiatingTemplateCapture.capture(), typeCaptor.capture(), nameCaptor.capture());

        var typeList = typeCaptor.getAllValues();
        var nameList = nameCaptor.getAllValues();

        assertEquals("String", typeList.get(0).trim());
        assertEquals("name", nameList.get(0).trim());
        assertEquals("int", typeList.get(1).trim());
        assertEquals("age", nameList.get(1).trim());
        assertEquals("Map<String,String>", typeList.get(2).trim());
        assertEquals("keyNames", nameList.get(2).trim());
    }


}
