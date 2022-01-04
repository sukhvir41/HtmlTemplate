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
import com.github.sukhvir41.core.statements.PlainStringRenderBodyStatement;
import com.github.sukhvir41.core.template.Template;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public final class ElseHtmlTag extends RegularHtmlTag {

    private static String HT_ELSE = "ht-else";
    private static final String OPENING_CODE = "else {";
    private static final String CLOSING_CODE = "}";

    private final Function<String, String> codeParser;

    public static boolean matches(String string) {
        return StringUtils.containsIgnoreCase(string, HT_ELSE);
    }

    public ElseHtmlTag(String htmlString, Template instantiatingTemplate, Function<String, String> codeParser) {
        super(htmlString, instantiatingTemplate);
        this.codeParser = codeParser;
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        classGenerator.addStatement(super.template, new PlainStringRenderBodyStatement(OPENING_CODE));
        classGenerator.incrementRenderBodyIndentation(super.template);
        processTag(classGenerator);
    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {
        classGenerator.decrementRenderBodyIndentation(super.template);
        classGenerator.addStatement(super.template, new PlainStringRenderBodyStatement(CLOSING_CODE));
    }


    private void processTag(TemplateClassGenerator classGenerator) {

        int startIndex = this.htmlString.indexOf(HT_ELSE);

        if (startIndex == -1) {
            new DynamicAttributeHtmlTag(this.htmlString, super.template, codeParser)
                    .processOpeningTag(classGenerator);
        } else {
            int endIndex = startIndex + HT_ELSE.length();
            var leftPart = this.htmlString.substring(0, startIndex)
                    .trim();
            var rightPart = this.htmlString.substring(endIndex);
            new DynamicAttributeHtmlTag(leftPart + rightPart, super.template, codeParser)
                    .processOpeningTag(classGenerator);
        }

    }
}
