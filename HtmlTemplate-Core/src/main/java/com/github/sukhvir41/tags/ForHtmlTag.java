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
import com.github.sukhvir41.core.statements.PlainStringRenderBodyStatement;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.core.IllegalSyntaxException;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ForHtmlTag extends RegularHtmlTag {

    public static final Pattern FOR_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-for\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    private static final String IN = " in ";

    private final Function<String, String> codeParser;

    public static boolean matches(String html) {
        return FOR_ATTRIBUTE_PATTERN.matcher(html)
                .find();
    }


    public ForHtmlTag(String htmlString, Template instantiatingTemplate, Function<String, String> codeParser) {
        super(htmlString, instantiatingTemplate);
        this.codeParser = codeParser;
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        var matcher = FOR_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var forStatement = extractForStatement(matcher);

            var forStatementParts = forStatement.split(IN);

            if (forStatementParts.length != 2) {
                throw new IllegalSyntaxException("Error in org.ht-for.\nLine -> " + this.htmlString);
            }

            var collection = codeParser.apply(forStatementParts[1].trim());
            var variables = Stream.of(forStatementParts[0].trim().split(","))
                    .map(String::trim)
                    .collect(Collectors.joining(", "));

            classGenerator.addStatement(super.template, new PlainStringRenderBodyStatement("forEach(" + collection + ", (" + variables + ") -> {"));
            classGenerator.incrementRenderBodyIndentation(super.template);

        }

        processTag(classGenerator);
    }

    private String extractForStatement(Matcher matcher) {
        var forAttribute = this.htmlString
                .substring(matcher.start(), matcher.end());

        return forAttribute.substring(forAttribute.indexOf('"') + 1, forAttribute.length() - 1)
                .replace("'", "\"");
    }

    private void processTag(TemplateClassGenerator classGenerator) {
        var matcher = FOR_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var leftPart = this.htmlString.substring(0, matcher.start())
                    .trim();
            var rightPart = this.htmlString.substring(matcher.end());

            new DynamicAttributeHtmlTag(leftPart + rightPart, super.template, codeParser)
                    .processOpeningTag(classGenerator);
        } else {

            new DynamicAttributeHtmlTag(this.htmlString, super.template, codeParser)
                    .processOpeningTag(classGenerator);
        }
    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {
        classGenerator.decrementRenderBodyIndentation(super.template);
        classGenerator.addStatement(super.template, new PlainStringRenderBodyStatement("});"));
    }
}
