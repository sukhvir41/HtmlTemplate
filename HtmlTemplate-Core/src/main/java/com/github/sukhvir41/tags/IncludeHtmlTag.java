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

import com.github.sukhvir41.core.IllegalSyntaxException;
import com.github.sukhvir41.core.Template;
import com.github.sukhvir41.core.TemplateClassGenerator;
import com.github.sukhvir41.core.VariableInfo;
import com.github.sukhvir41.parsers.Code;
import com.github.sukhvir41.utils.HtmlUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class IncludeHtmlTag implements HtmlTag {

    public static final Pattern INCLUDE_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-include\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static final Pattern VARIABLES_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-variables\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    private String htmlString;
    private Template template;


    public static boolean matches(String section) {
        if (HtmlUtils.getStartingHtmlTagName(section).equalsIgnoreCase("meta")) {
            return INCLUDE_ATTRIBUTE_PATTERN.matcher(section)
                    .find();
        } else {
            return false;
        }
    }

    IncludeHtmlTag(String htmlString, Template template) {
        this.htmlString = htmlString;
        this.template = template;
    }

    @Override
    public boolean isSelfClosing() {
        return true;
    }

    @Override
    public boolean isDocTypeTag() {
        return false;
    }

    @Override
    public boolean isClosingTag() {
        return this.htmlString.charAt(this.htmlString.length() - 2) == '/';
    }

    @Override
    public String getName() {
        return "meta";
    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {
    }

    protected String getFilePath() {
        var matcher = INCLUDE_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var htTemplateAttribute = this.htmlString.substring(matcher.start(), matcher.end());
            return htTemplateAttribute.substring(htTemplateAttribute.indexOf("\"") + 1, htTemplateAttribute.length() - 1);
        } else {
            throw new IllegalStateException("");
        }
    }

    protected final Template getTemplate() {
        return this.template;
    }

    protected final String getHtmlString() {
        return this.htmlString;
    }

    protected List<VariableInfo> getVariables(Template template, TemplateClassGenerator classGenerator) {
        return classGenerator.getMappedVariables()
                .getOrDefault(template, getVariableImpl(classGenerator));
    }

    private List<VariableInfo> getVariableImpl(TemplateClassGenerator classGenerator) {
        try {
            var matcher = VARIABLES_ATTRIBUTE_PATTERN.matcher(this.htmlString);
            if (matcher.find()) {
                String variables = extractVariablesString(matcher);

                return getVariablesMapping(variables, classGenerator);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error in parsing variables in meta include tag -> " + this.htmlString);
        }
    }

    private String extractVariablesString(Matcher matcher) {
        var variables = this.htmlString
                .substring(matcher.start(), matcher.end());
        return variables.substring(variables.indexOf("\"") + 1, variables.length() - 1);
    }

    private List<VariableInfo> getVariablesMapping(String variables, TemplateClassGenerator classGenerator) {
        var variableList = new ArrayList<VariableInfo>();
        String[] variablesParts = variables.split(",");

        if (variablesParts.length % 2 != 0) {
            throw new IllegalSyntaxException(" -> " + this.htmlString);
        } else {
            for (int i = 0; i < variablesParts.length; i += 2) {
                variableList.add(
                        new VariableInfo(
                                variablesParts[i],
                                Code.parse(variablesParts[i + 1], classGenerator.getVariableMappings(template))
                        )
                );
            }
        }

        return variableList;
    }

}
