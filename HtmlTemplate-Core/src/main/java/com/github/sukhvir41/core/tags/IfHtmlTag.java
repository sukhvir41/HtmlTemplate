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

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class IfHtmlTag extends RegularHtmlTag {

    public static final Pattern IF_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-if\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    private final Function<String, String> codeParser;

    public static boolean matches(String string) {
        return IF_ATTRIBUTE_PATTERN.matcher(string)
                .find();
    }


    public IfHtmlTag(String htmlString, Template instantiatingTemplate, Function<String, String> codeParser) {
        super(htmlString, instantiatingTemplate);
        this.codeParser = codeParser;
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        var matcher = IF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            String ifCondition = codeParser.apply(getIfCondition(matcher));
            if (suppressExceptions()) {
                classGenerator.addStatement(super.template, new PlainStringRenderBodyStatement("if (condition(() -> " + ifCondition + ")) {"));
            } else {
                classGenerator.addStatement(super.template, new PlainStringRenderBodyStatement("if (" + ifCondition + " ) {"));
            }

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

    public void processTag(TemplateClassGenerator classGenerator) {
        var matcher = IF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var leftPart = this.htmlString.substring(0, matcher.start())
                    .trim();
            var rightPart = this.htmlString.substring(matcher.end());
            //templateClass.appendPlainHtml(leftPart + rightPart);
            new DynamicAttributeHtmlTag(leftPart + rightPart, super.template, codeParser)
                    .processOpeningTag(classGenerator);
        } else {
            //templateClass.appendPlainHtml(this.htmlString);
            new DynamicAttributeHtmlTag(this.htmlString, super.template, codeParser)
                    .processOpeningTag(classGenerator);
        }
    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {
        classGenerator.decrementRenderBodyIndentation(super.template);
        classGenerator.addStatement(super.template, new PlainStringRenderBodyStatement("}"));
    }

    private boolean suppressExceptions() {
        return this.template.getSettings().get(SettingOptions.SUPPRESS_EXCEPTIONS)
                .orElseThrow(() -> new IllegalStateException("SUPPRESS_EXCEPTIONS setting was not set"));
    }

}
