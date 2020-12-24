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

import com.github.sukhvir41.TestUtils;
import com.github.sukhvir41.newCore.*;
import org.junit.Assert;
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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


@RunWith(PowerMockRunner.class)
@PrepareForTest(RuntimeIncludeHtmlTag.class)
public class RuntimeIncludeHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    public TemplateClassGenerator classGenerator;

    @Mock
    public RuntimeSubTemplate subTemplate;

    @Mock
    public Template runtimeTemplate;

    @Captor
    public ArgumentCaptor<Template> templateArgumentCaptor;

    @Captor
    public ArgumentCaptor<List<VariableInfo>> variableArgumentCaptor;


    @Before
    public void before() throws Exception {
        PowerMockito.whenNew(RuntimeSubTemplate.class)
                .withArguments(ArgumentMatchers.any(Path.class), ArgumentMatchers.any(Template.class))
                .thenReturn(subTemplate);

        Path path = TestUtils.getFile("SimpleTest1.html");
        Mockito.when(runtimeTemplate.getFile())
                .thenReturn(path);
    }

    @Test
    public void testOpeningTagPath() throws Exception {

        RuntimeIncludeHtmlTag includeHtmlTag = new RuntimeIncludeHtmlTag(
                "<meta ht-include=\"somepath/somefile.html\" ht-variables=\"subname,@name.getSubName(),age,10\">"
                , runtimeTemplate
        );

        includeHtmlTag.processOpeningTag(classGenerator);

        Path pathArgument = runtimeTemplate.getFile()
                .getParent()
                .resolve("somepath/somefile.html");

        PowerMockito.verifyNew(RuntimeSubTemplate.class)
                .withArguments(pathArgument, runtimeTemplate);

    }

    @Test
    public void testVariables() {
        RuntimeIncludeHtmlTag includeHtmlTag = new RuntimeIncludeHtmlTag(
                "<meta ht-include=\"somepath/somefile.html\" ht-variables=\"subname,@name.getSubName(),age,10\">"
                , runtimeTemplate
        );

        includeHtmlTag.processOpeningTag(classGenerator);

        Mockito.verify(classGenerator)
                .addMappedVariables(templateArgumentCaptor.capture(), variableArgumentCaptor.capture());


        Assert.assertEquals(new VariableInfo("subname", "name().getSubName()"), variableArgumentCaptor.getValue().get(0));

        Assert.assertEquals(new VariableInfo("age", "10"), variableArgumentCaptor.getValue()
                .get(1));
    }

}
