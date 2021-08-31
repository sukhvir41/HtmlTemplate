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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.sukhvir41.utils.StringUtils.CONTENT_VARIABLE_STRING_PATTERN;
import static com.github.sukhvir41.utils.StringUtils.findIndex;

public final class Code {

    public static final Pattern CONTENT_VARIABLE_PATTERN =
            Pattern.compile("@[a-z_$][a-z0-9_$]*", Pattern.CASE_INSENSITIVE);

    public static String parseForFunction(String theCode) {
        return parserImpl(theCode, "()");
    }

    public static String parseForVariable(String theCode) {
        return parserImpl(theCode, "");
    }

    /**
     * theCode = "1,2, 3, 5 , 6"
     * separator = ","
     * returned value = [1,2,3,4,5,6]
     * <p>
     * it also checks if the separator is brackets or within string literals if so ignores that separator
     *
     * @param theCode
     * @param separator
     * @return returns list of strings that are separated by the separator
     */
    public static List<String> getCodeParts(String theCode, String separator) {
        List<String> parts = new ArrayList<>();
        String newCode = theCode;
        int separatorIndex;
        int startIndex = 0;
        while ((separatorIndex = findIndex(startIndex, separator, newCode)) > -1) {
            if (isWithinBrackets(newCode, separatorIndex)) {
                startIndex = separatorIndex + 1;
            } else {
                parts.add(newCode.substring(0, separatorIndex).trim());
                newCode = newCode.substring(separatorIndex + 1);
                startIndex = 0;
            }
        }
        if (StringUtils.isNotBlank(newCode)) {
            parts.add(newCode.trim());
        }
        return parts;
    }

    private static boolean isWithinBrackets(String theCode, int commaIndex) {
        Deque<Integer> openBrackets = new LinkedList<>();
        int openBracketIndex = 0;
        int openBracketsSearchIndex = 0;
        while (true) {
            openBracketIndex = findIndex(openBracketsSearchIndex, "(", theCode);
            if (openBracketIndex == -1 || openBracketIndex > commaIndex) {
                break;
            } else {
                openBrackets.add(openBracketIndex);
                openBracketsSearchIndex = openBracketIndex + 1;
            }
        }

        if (!openBrackets.isEmpty()) {
            int closingBracketIndex;
            int searchIndex = openBrackets.getLast();
            while (!openBrackets.isEmpty()) {
                closingBracketIndex = findIndex(searchIndex, ")", theCode);
                if (closingBracketIndex > commaIndex) {
                    return true;
                } else {
                    openBrackets.removeLast();
                    searchIndex = closingBracketIndex + 1;
                }
            }
        }
        return false;
    }


    private static String parserImpl(String theCode, String variableAppender) {
        String newCode = theCode;
        int findIndex = 0;
        Matcher variableMatcher = CONTENT_VARIABLE_PATTERN.matcher(newCode);
        while (variableMatcher.find(findIndex)) {
            if (isVariableInString(newCode, variableMatcher.start())) {
                findIndex = newCode.indexOf("\"", variableMatcher.end());
            } else {
                newCode = replaceVariableAndAppend(newCode, variableMatcher, variableAppender);
                variableMatcher = CONTENT_VARIABLE_PATTERN.matcher(newCode);
            }
        }
        return newCode;
    }

    // replace the variable the variable name. eg @name -> name + <appender>
    private static String replaceVariableAndAppend(String theCode, Matcher variableMatcher, String appender) {
        String htmlVariable = theCode.substring(variableMatcher.start(), variableMatcher.end());
        String variable = StringUtils.replaceOnce(htmlVariable, "@", "");
        String leftPart = theCode.substring(0, variableMatcher.start());
        String rightPart = theCode.substring(variableMatcher.start());
        rightPart = StringUtils.replaceOnce(
                rightPart, htmlVariable, variable + appender
        );
        return leftPart + rightPart;
    }

    private static boolean isVariableInString(String theCode, int variableStartIndex) {
        var stringMatcher = CONTENT_VARIABLE_STRING_PATTERN.matcher(theCode);
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
