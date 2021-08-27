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

package com.github.sukhvir41.utils;

import java.nio.file.Path;
import java.util.regex.Pattern;

public interface StringUtils {

    /**
     * string literal regex from stack overflow.
     * https://stackoverflow.com/questions/171480/regex-grabbing-values-between-quotation-marks
     * author: deadbug.
     * regex taken from comment by: Aran-Fey.
     */
    Pattern CONTENT_VARIABLE_STRING_PATTERN =
            Pattern.compile("([\"])(?:\\\\.|[^\\\\])*?\\1", Pattern.CASE_INSENSITIVE);

    /**
     * <p>
     * Finds the index of toSearch in the content provided from the starting position specified by start. <br>
     * ignores the toSearch string if it is within a literal string and carries on. <br>
     * eg: <br>
     * start = 0, toSearch = "c" , content = ""abc"c" <br>
     * return : 5 <br>
     * </p>
     *
     * @param start    search begin position in content
     * @param toSearch string to search
     * @param content  string to check
     * @return starting position of toSearch in content. if not found returns -1.
     */
    static int findIndex(int start, String toSearch, String content) {
        if (content == null || toSearch == null) {
            return -1;
        }

        int searchStartIndex = Math.max(start, 0); // just in case the value of start is in negatives

        //if can't find the content of the
        int index = content.indexOf(toSearch, searchStartIndex);
        if (index == -1) {
            return -1;
        }

        var stringVariableMatcher = CONTENT_VARIABLE_STRING_PATTERN
                .matcher(content);

        while (stringVariableMatcher.find(searchStartIndex)) {
            if (stringVariableMatcher.start() > index) {
                break;
            } else if (stringVariableMatcher.start() < index && index < stringVariableMatcher.end()) {
                index = content.indexOf(toSearch, stringVariableMatcher.end());
                searchStartIndex = stringVariableMatcher.end();
            } else {
                searchStartIndex = stringVariableMatcher.end();
            }
        }

        return index;
    }


    static String getClassNameFromFile(String fileName) {
        var nameParts = fileName.split("\\.");
        return nameParts[0].replace(" ", "")
                .replace("-", "_");
    }

    static String getClassNameFromFile(Path file) {
        String fileName = file.getFileName().toString();
        return getClassNameFromFile(fileName);
    }

    static String getIndentations(int count) {
        var builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            builder.append("\t");
        }

        return builder.toString();
    }


}
