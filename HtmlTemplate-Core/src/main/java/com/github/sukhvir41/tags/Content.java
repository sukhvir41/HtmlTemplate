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
import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.core.statements.PlainStringRenderBodyStatement;
import com.github.sukhvir41.core.IllegalSyntaxException;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;
import java.util.logging.Logger;

import static com.github.sukhvir41.utils.StringUtils.findIndex;

public final class Content implements HtmlTag {

    public static final String ESCAPED_CONTENT_START = "{{";
    public static final String ESCAPED_CONTENT_END = "}}";

    public static final String UNESCAPED_CONTENT_START = "{{{";
    public static final String UNESCAPED_CONTENT_END = "}}}";

    private static final String ESCAPED_VARIABLE_LEFT_PART_CODE = ".write(content(() -> String.valueOf(";
    private static final String ESCAPED_VARIABLE_RIGHT_PART_CODE = ")));";

    private static final String ESCAPED_VARIABLE_NO_CHECK_LEFT_PART_CODE = ".write(content(";
    private static final String ESCAPED_VARIABLE_NO_CHECK_RIGHT_PART_CODE = "));";

    private static final String UNESCAPED_VARIABLE_LEFT_PART_CODE = ".write(unescapedContent(() -> String.valueOf(";
    private static final String UNESCAPED_VARIABLE_RIGHT_PART_CODE = ")));";

    private static final String UNESCAPED_VARIABLE_NO_CHECK_LEFT_PART_CODE = ".write(unescapedContent(";
    private static final String UNESCAPED_VARIABLE_NO_CHECK_RIGHT_PART_CODE = "));";

    private final String content;
    private final Template template;
    private final Function<String, String> codeParser;
    private boolean isFirstLeftPart = true;
    private final Logger logger;

    public Content(String content, Template instantiatingTemplate, Function<String, String> codeParser) {
        this.content = content;
        this.template = instantiatingTemplate;
        this.codeParser = codeParser;
        this.logger = this.template.getSettings().getLogger();
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        this.logger.info("Processing Content. content " + this.content);
        if (containsDynamicContent()) {
            this.logger.fine("processing dynamic content");
            processDynamicContent(content, classGenerator);//process left to right
        } else {
            classGenerator.appendPlainHtml(template, content);
        }
    }


    private boolean containsDynamicContent() {
        //this will match both escaped and unescaped content
        return content.contains(ESCAPED_CONTENT_START);
    }

    private void processDynamicContent(String content, TemplateClassGenerator classGenerator) {

        int escapedStartIndex = content.indexOf(ESCAPED_CONTENT_START);
        int unescapedStartIndex = content.indexOf(UNESCAPED_CONTENT_START);

        if (unescapedStartIndex > -1 && unescapedStartIndex <= escapedStartIndex) { // is unescaped brackets before escaped brackets or have the same start
            this.logger.finer("processing unescaped dynamic content");
            processUnescapedDynamicContent(unescapedStartIndex, content, classGenerator);
        } else if (escapedStartIndex > -1) {
            this.logger.finer("processing escaped dynamic content");
            processEscapedDynamicContent(escapedStartIndex, content, classGenerator);
        } else {
            this.logger.finer("processing plain content");
            if (StringUtils.isBlank(content)) {
                classGenerator.appendPlainHtmlNewLine(template);
            } else {
                classGenerator.appendPlainHtml(template, content, false, true);
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

        addCode(this.codeParser.apply(theCode), classGenerator);

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

        addUnescapedCode(this.codeParser.apply(theCode), classGenerator);

        var remainingContent = content.substring(endIndex + UNESCAPED_CONTENT_END.length());

        processDynamicContent(remainingContent, classGenerator);
    }


    private void processLeftPart(int dynamicStartIndex, String content, TemplateClassGenerator classGenerator) {
        var plaintHtmlLeft = StringUtils.left(content, dynamicStartIndex);
        if (StringUtils.isBlank(plaintHtmlLeft)) { //if staring of with {{ dynamic content}} or {{{ dynamic content }}}
            classGenerator.appendPlainHtmlIndentation(template);
        } else {
            classGenerator.appendPlainHtml(template, plaintHtmlLeft, isFirstLeftPart, false);
        }
        isFirstLeftPart = false;
    }


    private void addCode(String code, TemplateClassGenerator classGenerator) {
        classGenerator.addStatement(template,
                new PlainStringRenderBodyStatement(
                        classGenerator.getWriterVariableName() +
                                (suppressExceptions() ? ESCAPED_VARIABLE_LEFT_PART_CODE : ESCAPED_VARIABLE_NO_CHECK_LEFT_PART_CODE) +
                                code +
                                (suppressExceptions() ? ESCAPED_VARIABLE_RIGHT_PART_CODE : ESCAPED_VARIABLE_NO_CHECK_RIGHT_PART_CODE)
                )
        );

    }

    private void addUnescapedCode(String code, TemplateClassGenerator classGenerator) {
        classGenerator.addStatement(template, new PlainStringRenderBodyStatement(
                classGenerator.getWriterVariableName() +
                        (suppressExceptions() ? UNESCAPED_VARIABLE_LEFT_PART_CODE : UNESCAPED_VARIABLE_NO_CHECK_LEFT_PART_CODE) +
                        code +
                        (suppressExceptions() ? UNESCAPED_VARIABLE_RIGHT_PART_CODE : UNESCAPED_VARIABLE_NO_CHECK_RIGHT_PART_CODE)
        ));
    }

    private boolean suppressExceptions() {
        return this.template.getSettings()
                .get(SettingOptions.SUPPRESS_EXCEPTIONS)
                .orElseThrow(() -> new IllegalStateException("SUPPRESS_EXCEPTIONS setting was not set"));
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
