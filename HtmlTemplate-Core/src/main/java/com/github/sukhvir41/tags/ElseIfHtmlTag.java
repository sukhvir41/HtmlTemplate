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
import com.github.sukhvir41.core.classgenerator.TemplateClassGeneratorOLD;
import com.github.sukhvir41.core.statements.PlainStringRenderBodyStatement;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.parsers.Code;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ElseIfHtmlTag extends RegularHtmlTag {

    public static final Pattern ELSEIF_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-elseIf\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    private static final String OPENING_LEFT_PART_CODE = "else if (condition(() -> ";
    private static final String OPENING_RIGHT_PART_CODE = ")) {";
    private static final String CLOSING_CODE = "}";

    public static boolean matches(String string) {
        return ELSEIF_ATTRIBUTE_PATTERN.matcher(string)
                .find();
    }

    public ElseIfHtmlTag(String htmlString, Template instantiatingTemplate) {
        super(htmlString, instantiatingTemplate);
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        var matcher = ELSEIF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            String ifCondition = Code.parseForFunction(getIfCondition(matcher));
            classGenerator.addStatement(super.template, new PlainStringRenderBodyStatement(OPENING_LEFT_PART_CODE + ifCondition + OPENING_RIGHT_PART_CODE));
            classGenerator.incrementRenderBodyIndentation(super.template);
        }

        processTag(classGenerator);
    }

    private String getIfCondition(Matcher matcher) {
        var ifAttribute = this.htmlString
                .substring(matcher.start(), matcher.end());

        var ifCondition = ifAttribute.substring(ifAttribute.indexOf('"') + 1, ifAttribute.length() - 1);

        return ifCondition.replace("'", "\"")
                .trim();
    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {
        classGenerator.decrementRenderBodyIndentation(super.template);
        classGenerator.addStatement(super.template, new PlainStringRenderBodyStatement(CLOSING_CODE));
    }


    private void processTag(TemplateClassGenerator classGenerator) {
        Matcher elseIfAttributeMatcher = ELSEIF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (elseIfAttributeMatcher.find()) {
            var leftPart = this.htmlString.substring(0, elseIfAttributeMatcher.start())
                    .trim();
            var rightPart = this.htmlString.substring(elseIfAttributeMatcher.end());
            new DynamicAttributeHtmlTag(leftPart + rightPart, super.template)
                    .processOpeningTag(classGenerator);
        } else {
            new DynamicAttributeHtmlTag(this.htmlString, super.template)
                    .processOpeningTag(classGenerator);
        }
    }
}
