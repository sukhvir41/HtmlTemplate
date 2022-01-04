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

package com.github.sukhvir41.core.tags;

import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.statements.PlainStringRenderBodyStatement;
import com.github.sukhvir41.core.template.Template;
import org.apache.commons.lang3.StringUtils;
import com.github.sukhvir41.utils.HtmlUtils;

import java.util.function.Function;
import java.util.regex.Matcher;

public final class DynamicAttributeHtmlTag extends RegularHtmlTag {

    private static final String DYNAMIC_ATTRIBUTE_START = "ht-";
    private static final String ATTRIBUTE_LEFT_PART_CODE = ".write(content(() -> String.valueOf(";
    private static final String ATTRIBUTE_LEFT_PART_NO_CHECK_CODE = ".write(content(";

    private static final String ATTRIBUTE_RIGHT_PART_CODE = ")));";
    private static final String ATTRIBUTE_RIGHT_PART_NO_CHECK_CODE = "));";


    private boolean isFirstLeftPart = true;
    private final Function<String, String> codeParser;

    public DynamicAttributeHtmlTag(String htmlString, Template instantiatingTemplate, Function<String, String> codeParser) {
        super(htmlString, instantiatingTemplate);
        this.codeParser = codeParser;
    }


    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        processDynamicAttributes(classGenerator, this.htmlString);
    }

    private void processDynamicAttributes(TemplateClassGenerator classGenerator, String htmlString) {

        var matcher = HtmlUtils.DYNAMIC_ATTRIBUTE
                .matcher(htmlString);

        if (matcher.find()) {
            var leftPart = htmlString.substring(0, matcher.start());
            processLeftPart(classGenerator, leftPart);
            processDynamicAttribute(matcher, classGenerator, htmlString);

        } else {
            classGenerator.appendPlainHtml(super.template, htmlString, isFirstLeftPart, true);
        }

    }

    private void processLeftPart(TemplateClassGenerator classGenerator, String leftPart) {
        if (StringUtils.isNotBlank(leftPart)) {
            classGenerator.appendPlainHtml(super.template, leftPart, isFirstLeftPart, false);
        }
        isFirstLeftPart = false;
    }

    private void processDynamicAttribute(Matcher matcher, TemplateClassGenerator classGenerator, String htmlString) {
        var dynamicAttribute = htmlString.substring(matcher.start(), matcher.end());

        var attributeName = dynamicAttribute.substring(0, dynamicAttribute.indexOf("="));
        var actualAttributeName = getActualAttributeName(attributeName);

        var attributeValue = dynamicAttribute.substring(dynamicAttribute.indexOf("=") + 1)
                .replace("\"", "")
                .replace("'", "\"")
                .trim();

        var code = codeParser.apply(attributeValue);

        classGenerator.appendPlainHtml(super.template, actualAttributeName + " = \"", false, false);

        if (suppressExceptions()) {
            classGenerator.addStatement(super.template,
                    new PlainStringRenderBodyStatement(
                            classGenerator.getWriterVariableName() +
                                    ATTRIBUTE_LEFT_PART_CODE +
                                    code +
                                    ATTRIBUTE_RIGHT_PART_CODE
                    )
            );
        } else {
            classGenerator.addStatement(super.template,
                    new PlainStringRenderBodyStatement(
                            classGenerator.getWriterVariableName() +
                                    ATTRIBUTE_LEFT_PART_NO_CHECK_CODE +
                                    code +
                                    ATTRIBUTE_RIGHT_PART_NO_CHECK_CODE
                    )
            );
        }


        classGenerator.appendPlainHtml(super.template, "\" ", false, false);

        var remainingPart = htmlString.substring(matcher.end());

        processDynamicAttributes(classGenerator, remainingPart);

    }

    private String getActualAttributeName(String attributeName) {
        return StringUtils.removeStartIgnoreCase(attributeName, DYNAMIC_ATTRIBUTE_START)
                .trim();
    }

    private boolean suppressExceptions() {
        return this.template.getSettings()
                .get(SettingOptions.SUPPRESS_EXCEPTIONS)
                .orElseThrow(() -> new IllegalStateException("SUPPRESS_EXCEPTIONS setting was not set"));
    }
}
