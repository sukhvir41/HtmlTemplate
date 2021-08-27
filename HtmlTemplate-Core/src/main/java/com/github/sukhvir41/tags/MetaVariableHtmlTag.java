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
import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.classgenerator.TemplateClassGeneratorOLD;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.utils.HtmlUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MetaVariableHtmlTag implements HtmlTag {

    private static final String TAG_NAME = "meta";
    public static final Pattern TYPE_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-variables\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    private final String htmlString;
    private final Template template;


    public static boolean matches(String html) {

        return HtmlUtils.getStartingHtmlTagName(html)
                .equalsIgnoreCase(TAG_NAME)
                &&
                TYPE_ATTRIBUTE_PATTERN.matcher(html)
                        .find();
    }

    public MetaVariableHtmlTag(String htmlString, Template instantiatingTemplate) {
        this.htmlString = htmlString;
        this.template = instantiatingTemplate;
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        String variablesString = extractVariablesString();
        Map<String, String> variables = getVariables(variablesString);
        addVariables(classGenerator, variables);

    }

    protected String extractVariablesString() {
        var matcher = TYPE_ATTRIBUTE_PATTERN
                .matcher(this.htmlString);
        if (matcher.find()) {
            String htVariables = this.htmlString
                    .substring(matcher.start(), matcher.end());
            return htVariables.substring(htVariables.indexOf("\"") + 1, htVariables.length() - 1);
        } else {
            return "";
        }
    }

    // key variable name, value -  type
    protected Map<String, String> getVariables(String variableString) {
        if (StringUtils.isBlank(variableString)) {
            return Collections.emptyMap();
        } else {
            String[] variableParts = variableString.split(",");
            if (variableParts.length % 2 != 0) {
                throw new IllegalSyntaxException("A missing name or type.\nLine -> " + this.htmlString);
            } else {
                Map<String, String> variables = new HashMap<>();
                for (int i = 0; i < variableParts.length; i += 2) {
                    variables.put(variableParts[i + 1].trim(), variableParts[i].trim());
                }
                return variables;
            }
        }
    }

    protected void addVariables(TemplateClassGenerator classGenerator, Map<String, String> variables) {
        variables.forEach((name, type) -> classGenerator.addVariable(this.template, type, name));
    }


    @Override

    public void processClosingTag(TemplateClassGenerator classGenerator) {
    }

    @Override
    public String getName() {
        return "meta";
    }

    @Override
    public boolean isClosingTag() {
        return false;
    }

    @Override
    public boolean isSelfClosing() {
        return true;
    }

    @Override
    public boolean isDocTypeTag() {
        return false;
    }

}
