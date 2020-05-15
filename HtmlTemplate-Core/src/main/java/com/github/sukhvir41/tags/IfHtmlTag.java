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

import com.github.sukhvir41.processors.Code;
import com.github.sukhvir41.template.TemplateClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class IfHtmlTag extends RegularHtmlTag {

    public static final Pattern IF_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-if\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static boolean matches(String string) {
        return IF_ATTRIBUTE_PATTERN.matcher(string)
                .find();
    }


    IfHtmlTag(String htmlString) {
        super(htmlString);
    }

    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        var matcher = IF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            String ifCondition = Code.parse(getIfCondition(matcher));
            templateClass.addCode("if(condition( () -> " + ifCondition + " )){");
            templateClass.incrementFunctionIndentation();
        }
    }

    private String getIfCondition(Matcher matcher) {
        var ifAttribute = this.htmlString
                .substring(matcher.start(), matcher.end());

        var ifCondition = ifAttribute.substring(ifAttribute.indexOf('"') + 1, ifAttribute.length() - 1);

        return ifCondition.replace("'", "\"")
                .trim();
    }

    @Override
    public void processTag(TemplateClass templateClass) {
        var matcher = IF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var leftPart = this.htmlString.substring(0, matcher.start())
                    .trim();
            var rightPart = this.htmlString.substring(matcher.end());
            //templateClass.appendPlainHtml(leftPart + rightPart);
            new DynamicAttributeHtmlTag(leftPart + rightPart)
                    .processTag(templateClass);
        } else {
            //templateClass.appendPlainHtml(this.htmlString);
            new DynamicAttributeHtmlTag(this.htmlString)
                    .processTag(templateClass);
        }
    }

    @Override
    public void processClosingTag(TemplateClass templateClass) {
        templateClass.decrementFunctionIndentation();
        templateClass.addCode("}");
    }

}
