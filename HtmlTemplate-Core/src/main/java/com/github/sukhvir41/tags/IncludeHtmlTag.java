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
import com.github.sukhvir41.core.VariableInfo;
import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.parsers.Code;
import com.github.sukhvir41.utils.HtmlUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

abstract class IncludeHtmlTag implements HtmlTag {

    public static final Pattern INCLUDE_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-include\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static final Pattern VARIABLES_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-variables\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    private final String htmlString;
    private final Template template;
    private final Function<String, String> codeParser;


    public static boolean matches(String section) {
        if (HtmlUtils.getStartingHtmlTagName(section).equalsIgnoreCase("meta")) {
            return INCLUDE_ATTRIBUTE_PATTERN.matcher(section)
                    .find();
        } else {
            return false;
        }
    }

    IncludeHtmlTag(String htmlString, Function<String, String> codeParser, Template template) {
        this.htmlString = htmlString;
        this.template = template;
        this.codeParser = codeParser;
    }

    @Override
    public final boolean isSelfClosing() {
        return true;
    }

    @Override
    public final boolean isDocTypeTag() {
        return false;
    }

    @Override
    public final boolean isClosingTag() {
        return this.htmlString.charAt(this.htmlString.length() - 2) == '/';
    }

    @Override
    public final String getName() {
        return "meta";
    }

    @Override
    public final void processClosingTag(TemplateClassGenerator classGenerator) {
    }

    /**
     * @return the file path in the ht-include attribute
     */
    protected String getFilePath() {
        var matcher = INCLUDE_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var htTemplateAttribute = this.htmlString.substring(matcher.start(), matcher.end());
            return htTemplateAttribute.substring(htTemplateAttribute.indexOf("\"") + 1, htTemplateAttribute.length() - 1);
        } else {
            throw new IllegalStateException("Couldn't find the file path. html string " + this.htmlString);
        }
    }

    /**
     * @return return the calling template
     */
    protected final Template getTemplate() {
        return this.template;
    }

    protected final String getHtmlString() {
        return this.htmlString;
    }

    /**
     * @return the variables in ht-variables
     */
    protected Map<String, String> getPassedVariables() {
        try {
            var matcher = VARIABLES_ATTRIBUTE_PATTERN.matcher(this.htmlString);
            if (matcher.find()) {
                return getPassedVariablesList(extractVariablesString(matcher));
            } else {
                return Collections.emptyMap();
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error in parsing variables in meta include tag -> " + this.htmlString, e);
        }
    }


    private String extractVariablesString(Matcher matcher) {
        var variables = this.htmlString
                .substring(matcher.start(), matcher.end());
        return variables.substring(variables.indexOf("\"") + 1, variables.length() - 1);
    }

    private Map<String, String> getPassedVariablesList(String variables) {
        Map<String, String> passedVariables = new HashMap<>();
        List<String> codeParts = Code.getCodeParts(variables, ",");

        if (codeParts.size() % 2 != 0) {
            throw new IllegalSyntaxException("The variables in include html tag should be key value pair format. html > " + htmlString);
        } else {
            for (int i = 0; i < codeParts.size(); i += 2) {
                passedVariables.put(
                        codeParts.get(i),
                        codeParser.apply(codeParts.get(i + 1))
                );
            }
        }

        return passedVariables;
    }

}
