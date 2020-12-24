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

import com.github.sukhvir41.core.ClassGenerator;
import com.github.sukhvir41.newCore.Template;
import com.github.sukhvir41.newCore.TemplateClassGenerator;
import com.github.sukhvir41.newCore.VariableInfo;
import com.github.sukhvir41.parsers.Code;
import com.github.sukhvir41.core.IllegalSyntaxException;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static com.github.sukhvir41.utils.StringUtils.findIndex;

public final class Content implements HtmlTag {

    public static final String ESCAPED_CONTENT_START = "{{";
    public static final String ESCAPED_CONTENT_END = "}}";

    public static final String UNESCAPED_CONTENT_START = "{{{";
    public static final String UNESCAPED_CONTENT_END = "}}}";

    private static final String ESCAPED_VARIABLE_LEFT_PART_CODE = ".append(content(() -> String.valueOf(";
    private static final String ESCAPED_VARIABLE_RIGHT_PART_CODE = ")));";

    private static final String UNESCAPED_VARIABLE_LEFT_PART_CODE = ".append(unescapedContent(() -> String.valueOf(";
    private static final String UNESCAPED_VARIABLE_RIGHT_PART_CODE = ")));";

    private final String content;
    private final Template template;
    private boolean isFirstLeftPart = true;

    public Content(String content, Template instantiatingTemplate) {
        this.content = content;
        this.template = instantiatingTemplate;
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        if (containsDynamicContent()) {
            processDynamicContent(content, classGenerator);//process left to right
        } else {
            classGenerator.appendPlainHtml(content);
        }
    }


    private boolean containsDynamicContent() {
        //this will match both escaped and unescaped content
        return content.contains(ESCAPED_CONTENT_START);
    }

    private void processDynamicContent(String content, TemplateClassGenerator classGenerator) {

        int escapedStartIndex = content.indexOf(ESCAPED_CONTENT_START);
        int unescapedStartIndex = content.indexOf(UNESCAPED_CONTENT_START);

        if (unescapedStartIndex > -1 && unescapedStartIndex <= escapedStartIndex) { // is unescaped brackets before escaped brackets
            processUnescapedDynamicContent(unescapedStartIndex, content, classGenerator);
        } else if (escapedStartIndex > -1) {
            processEscapedDynamicContent(escapedStartIndex, content, classGenerator);
        } else {
            if (StringUtils.isBlank(content)) {
                classGenerator.appendPlainHtmlNewLine();
            } else {
                classGenerator.appendPlainHtml(content, false, true);
            }
        }
    }

    private void processEscapedDynamicContent(int escapedContentStart, String content, TemplateClassGenerator classGenerator) {
        processLeftPart(escapedContentStart, content, classGenerator);

        int endIndex = findIndex(escapedContentStart, ESCAPED_CONTENT_END, content);

        if (endIndex == -1) { // end not found
            throw new IllegalSyntaxException("Could not find the \"" + ESCAPED_CONTENT_END + "\" in line -> \n " + this.content);
        }

        var theCode = content
                .substring(escapedContentStart + ESCAPED_CONTENT_START.length(), endIndex)
                .trim();

        addCode(Code.parse(theCode, classGenerator.getVariableMappings(template)), classGenerator);

        var remainingContent = content.substring(endIndex + ESCAPED_CONTENT_END.length());

        processDynamicContent(remainingContent, classGenerator);
    }


    private void processUnescapedDynamicContent(int unescapedContentStart, String content, TemplateClassGenerator classGenerator) {
        processLeftPart(unescapedContentStart, content, classGenerator);

        int endIndex = findIndex(unescapedContentStart, UNESCAPED_CONTENT_END, content);

        if (endIndex == -1) { // end not found
            throw new IllegalSyntaxException("Could not find the \"" + UNESCAPED_CONTENT_END + "\" in line -> \n " + this.content);
        }

        var theCode = content
                .substring(unescapedContentStart + UNESCAPED_CONTENT_START.length(), endIndex)
                .trim();

        addUnescapedCode(Code.parse(theCode, classGenerator.getVariableMappings(template)), classGenerator);

        var remainingContent = content.substring(endIndex + UNESCAPED_CONTENT_END.length());

        processDynamicContent(remainingContent, classGenerator);
    }


    private void processLeftPart(int dynamicStartIndex, String content, TemplateClassGenerator classGenerator) {
        var plaintHtmlLeft = StringUtils.left(content, dynamicStartIndex);
        if (StringUtils.isBlank(plaintHtmlLeft)) { //if staring of with {{ dynamic content}} or {{{ dynamic content }}}
            classGenerator.appendPlainHtmlIndentation();
        } else {
            classGenerator.appendPlainHtml(plaintHtmlLeft, isFirstLeftPart, false);
        }
        isFirstLeftPart = false;
    }


    private void addCode(String code, TemplateClassGenerator classGenerator) {
        classGenerator.addCode(classGenerator.getWriterVariableName() + ESCAPED_VARIABLE_LEFT_PART_CODE + code + ESCAPED_VARIABLE_RIGHT_PART_CODE);
    }

    private void addUnescapedCode(String code, TemplateClassGenerator classGenerator) {
        classGenerator.addCode(classGenerator.getWriterVariableName() + UNESCAPED_VARIABLE_LEFT_PART_CODE + code + UNESCAPED_VARIABLE_RIGHT_PART_CODE);
    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {
        //do nothing
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean isClosingTag() {
        //content is always closing
        return true;
    }

    @Override
    public boolean isSelfClosing() {
        //content is always self closing
        return true;
    }

    @Override
    public boolean isDocTypeTag() {
        return false;
    }
}
