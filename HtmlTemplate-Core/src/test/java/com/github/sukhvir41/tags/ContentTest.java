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

import static org.junit.Assert.*;

public class ContentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> addPlainHtmlCapture;

    @Captor
    private ArgumentCaptor<String> addCodeCapture;

    @Captor
    private ArgumentCaptor<Boolean> appendIndentationCapture;

    @Captor
    private ArgumentCaptor<Boolean> appendNewLineCapture;


    @Test
    public void plainTest() {
        TemplateClass templateClass = Mockito.mock(TemplateClass.class);

        var content = new Content(" some content ", templateClass);
        content.process();

        Mockito.verify(templateClass)
                .appendPlainHtml(addPlainHtmlCapture.capture());

        assertEquals(" some content ", addPlainHtmlCapture.getValue());

    }

    @Test
    public void escapedCodeTest() {
        TemplateClass templateClass = Mockito.mock(TemplateClass.class);

        var content = new Content(" content1 {{ \"some content2\" }} content3 {{ \"<h1> content4 </h1>\" }}", templateClass);
        content.process();

        //testing plain content part
        Mockito.verify(templateClass, Mockito.times(2))
                .appendPlainHtml(addPlainHtmlCapture.capture(), appendIndentationCapture.capture(), appendNewLineCapture.capture());
        var capturedPlainHtml = addPlainHtmlCapture.getAllValues();
        var capturedIndentation = appendIndentationCapture.getAllValues();
        var capturedNewLine = appendNewLineCapture.getAllValues();
        assertEquals(" content1 ", capturedPlainHtml.get(0));
        assertTrue(capturedIndentation.get(0));
        assertFalse(capturedNewLine.get(0));
        assertEquals(" content3 ", capturedPlainHtml.get(1));
        assertFalse(capturedIndentation.get(1));
        assertFalse(capturedNewLine.get(1));


        //testing escaped dynamic content part
        Mockito.verify(templateClass, Mockito.times(2))
                .addCode(addCodeCapture.capture());
        var addedCodes = addCodeCapture.getAllValues();
        assertEquals("writer.append(content(() -> String.valueOf(\"some content2\")));", addedCodes.get(0));
        assertEquals("writer.append(content(() -> String.valueOf(\"<h1> content4 </h1>\")));", addedCodes.get(1));

        //testing new line added at end
        Mockito.verify(templateClass)
                .appendPlainHtmlNewLine();
    }

    @Test
    public void unescapedCodeTest() {
        TemplateClass templateClass = Mockito.mock(TemplateClass.class);

        var content = new Content(" content1 {{ \"some content2\" }} content3 {{{ \"<h1> content4 </h1>\" }}}", templateClass);
        content.process();

        //testing plain content part
        Mockito.verify(templateClass, Mockito.times(2))
                .appendPlainHtml(addPlainHtmlCapture.capture(), appendIndentationCapture.capture(), appendNewLineCapture.capture());
        var capturedPlainHtml = addPlainHtmlCapture.getAllValues();
        var capturedIndentation = appendIndentationCapture.getAllValues();
        var capturedNewLine = appendNewLineCapture.getAllValues();
        assertEquals(" content1 ", capturedPlainHtml.get(0));
        assertTrue(capturedIndentation.get(0));
        assertFalse(capturedNewLine.get(0));
        assertEquals(" content3 ", capturedPlainHtml.get(1));
        assertFalse(capturedIndentation.get(1));
        assertFalse(capturedNewLine.get(1));


        //testing escaped dynamic content part
        Mockito.verify(templateClass, Mockito.times(2))
                .addCode(addCodeCapture.capture());
        var addedCodes = addCodeCapture.getAllValues();
        assertEquals("writer.append(content(() -> String.valueOf(\"some content2\")));", addedCodes.get(0));
        assertEquals("writer.append(unescapedContent(() -> String.valueOf(\"<h1> content4 </h1>\")));", addedCodes.get(1));

        //testing new line added at end
        Mockito.verify(templateClass)
                .appendPlainHtmlNewLine();
    }


}
