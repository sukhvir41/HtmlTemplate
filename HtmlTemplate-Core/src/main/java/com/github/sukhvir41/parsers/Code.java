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

package com.github.sukhvir41.parsers;

import org.apache.commons.lang3.StringUtils;
import com.github.sukhvir41.utils.HtmlUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Code {

    public static final Pattern CONTENT_VARIABLE_PATTERN =
            Pattern.compile("@[a-z,_,$][a-z,0-9,_,$]*", Pattern.CASE_INSENSITIVE);

    public static String parse(String theCode, Map<String, String> mappedVariables) {
        var variableMatcher = CONTENT_VARIABLE_PATTERN.matcher(theCode);
        int findIndex = 0;
        String newCode = theCode;
        while (variableMatcher.find(findIndex)) {

            if (isVariableInString(theCode, variableMatcher.start())) {
                findIndex = theCode.indexOf("\"", variableMatcher.end());
                continue;
            }

            newCode = replaceWithMethod(theCode, variableMatcher, newCode, mappedVariables);
            findIndex = variableMatcher.end();
        }

        return newCode.trim();
    }

    public static String parse(String theCode) {
        return parse(theCode, Collections.emptyMap());
    }

    // replace the variable with a method call of the variable name. eg @name -> name()
    private static String replaceWithMethod(String theCode, Matcher variableMatcher, String newCode, Map<String, String> mappedVariables) {
        String htmlVariable = theCode.substring(variableMatcher.start(), variableMatcher.end());
        String variable = StringUtils.replaceOnce(htmlVariable, "@", "");
        String mappedToVariable = mappedVariables.getOrDefault(variable, variable);
        newCode = StringUtils.replaceOnce(
                newCode, htmlVariable, mappedToVariable + "()"
        );
        return newCode;
    }

    private static boolean isVariableInString(String theCode, int variableStartIndex) {
        var stringMatcher = HtmlUtils.CONTENT_VARIABLE_STRING_PATTERN.matcher(theCode);
        var startIndex = 0;

        while (stringMatcher.find(startIndex)) {
            if (stringMatcher.start() < variableStartIndex && stringMatcher.end() > variableStartIndex) {
                return true;
            } else {
                startIndex = stringMatcher.end() + 1;
            }
        }

        return false;
    }


}
