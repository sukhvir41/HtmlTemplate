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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest(IfHtmlTag.class)
public class MetaImportHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> importCapture;

    @Mock
    private TemplateClassGenerator classGenerator;

    @Test
    public void singleImportTest() {
        MetaImportHtmlTag importTag = new MetaImportHtmlTag("<meta ht-import=\"java.nio.file.Path\">");
        importTag.processOpeningTag(classGenerator);
        importTag.processClosingTag(classGenerator);

        Mockito.verify(classGenerator)
                .addImportStatement(importCapture.capture());

        assertEquals("java.nio.file.Path", importCapture.getValue());
    }

    @Test
    public void multipleImportTest() {
        MetaImportHtmlTag importTag = new MetaImportHtmlTag("<meta ht-import=\"java.nio.file.Path, java.nio.file.Paths\">");
        importTag.processOpeningTag(classGenerator);
        importTag.processClosingTag(classGenerator);

        Mockito.verify(classGenerator, Mockito.times(2))
                .addImportStatement(importCapture.capture());

        var importList = importCapture.getAllValues();
        assertEquals("java.nio.file.Path", importList.get(0));
        assertEquals("java.nio.file.Paths", importList.get(1));
    }


}
