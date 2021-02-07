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
import org.apache.commons.lang3.StringUtils;

public final class ElseHtmlTag extends RegularHtmlTag {

    private static String HT_ELSE = "ht-else";
    private static final String OPENING_CODE = "else {";
    private static final String CLOSING_CODE = "}";

    public static boolean matches(String string) {
        return StringUtils.containsIgnoreCase(string, HT_ELSE);
    }

    public ElseHtmlTag(String htmlString) {
        super(htmlString);
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        classGenerator.addCode(OPENING_CODE);
        classGenerator.incrementRenderFunctionIndentation();
        processTag(classGenerator);
    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {
        classGenerator.decrementRenderFunctionIndentation();
        classGenerator.addCode(CLOSING_CODE);
    }


    private void processTag(TemplateClassGenerator classGenerator) {

        int startIndex = this.htmlString.indexOf(HT_ELSE);

        if (startIndex == -1) {
            new DynamicAttributeHtmlTag(this.htmlString)
                    .processOpeningTag(classGenerator);
        } else {
            int endIndex = startIndex + HT_ELSE.length();
            var leftPart = this.htmlString.substring(0, startIndex)
                    .trim();
            var rightPart = this.htmlString.substring(endIndex);
            new DynamicAttributeHtmlTag(leftPart + rightPart)
                    .processOpeningTag(classGenerator);
        }

    }
}
