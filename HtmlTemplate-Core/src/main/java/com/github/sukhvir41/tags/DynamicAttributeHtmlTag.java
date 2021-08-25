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

import com.github.sukhvir41.core.classgenerator.TemplateClassGeneratorOLD;
import com.github.sukhvir41.core.statements.PlainStringRenderBodyStatement;
import org.apache.commons.lang3.StringUtils;
import com.github.sukhvir41.parsers.Code;
import com.github.sukhvir41.utils.HtmlUtils;

import java.util.regex.Matcher;

public final class DynamicAttributeHtmlTag extends RegularHtmlTag {

    private static final String DYNAMIC_ATTRIBUTE_START = "ht-";
    private static final String ATTRIBUTE_LEFT_PART_CODE = ".append(content(() -> String.valueOf(";
    private static final String ATTRIBUTE_RIGHT_PART_CODE = ")));";

    boolean isFirstLeftPart = true;

    public DynamicAttributeHtmlTag(String htmlString) {
        super(htmlString);
    }


    @Override
    public void processOpeningTag(TemplateClassGeneratorOLD classGenerator) {
        processDynamicAttributes(classGenerator, this.htmlString);
    }

    private void processDynamicAttributes(TemplateClassGeneratorOLD classGenerator, String htmlString) {

        var matcher = HtmlUtils.DYNAMIC_ATTRIBUTE
                .matcher(htmlString);

        if (matcher.find()) {
            var leftPart = htmlString.substring(0, matcher.start());
            processLeftPart(classGenerator, leftPart);
            processDynamicAttribute(matcher, classGenerator, htmlString);

        } else {
            classGenerator.appendPlainHtml(htmlString, isFirstLeftPart, true);
        }

    }

    private void processLeftPart(TemplateClassGeneratorOLD classGenerator, String leftPart) {
        if (StringUtils.isNotBlank(leftPart)) {
            classGenerator.appendPlainHtml(leftPart, isFirstLeftPart, false);
        }
        isFirstLeftPart = false;
    }

    private void processDynamicAttribute(Matcher matcher, TemplateClassGeneratorOLD classGenerator, String htmlString) {
        var dynamicAttribute = htmlString.substring(matcher.start(), matcher.end());

        var attributeName = dynamicAttribute.substring(0, dynamicAttribute.indexOf("="));
        var actualAttributeName = getActualAttributeName(attributeName);

        var attributeValue = dynamicAttribute.substring(dynamicAttribute.indexOf("=") + 1)
                .replace("\"", "")
                .trim();

        var code = Code.parseForFunction(attributeValue);

        classGenerator.appendPlainHtml(actualAttributeName + " = \"", false, false);
        classGenerator.addCode(
                new PlainStringRenderBodyStatement(
                        classGenerator.getWriterVariableName() + ATTRIBUTE_LEFT_PART_CODE + code + ATTRIBUTE_RIGHT_PART_CODE
                )
        );
        classGenerator.appendPlainHtml("\" ", false, false);

        var remainingPart = htmlString.substring(matcher.end());

        processDynamicAttributes(classGenerator, remainingPart);

    }

    private String getActualAttributeName(String attributeName) {
        return StringUtils.removeStartIgnoreCase(attributeName, DYNAMIC_ATTRIBUTE_START)
                .trim();
    }
}
