package org.ht.processors;

import org.apache.commons.lang3.StringUtils;
import org.ht.tags.HtmlUtils;

public class Code {

    public static String parse(String theCode) {
        var variableMatcher = HtmlUtils.CONTENT_VARIABLE_PATTERN.matcher(theCode);
        int findIndex = 0;
        String newCode = theCode;
        while (variableMatcher.find(findIndex)) {

            if (isVariableInString(theCode, variableMatcher.start())) {
                findIndex = theCode.indexOf("\"", variableMatcher.end());
                continue;
            }

            var variable = theCode.substring(variableMatcher.start(), variableMatcher.end());
            newCode = StringUtils.replaceOnce(
                    newCode, variable, StringUtils.replaceOnce(variable, "@", "") + "()"
            );
            findIndex = variableMatcher.end();
        }

        return newCode.trim();
    }

    private static boolean isVariableInString(String theCode, int variableStartIndex) {
        var stringMatcher = HtmlUtils.CONTENT_VARIABLE_STRING_PATTERN.matcher(theCode);
        var startIndex = 0;

        while (stringMatcher.find(startIndex)) {
            if (stringMatcher.start() < variableStartIndex && stringMatcher.end() > variableStartIndex) {
                return true;
            } else {
                startIndex = stringMatcher.end();
            }
        }

        return false;
    }


}
