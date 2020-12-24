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

package com.github.sukhvir41.parsers.lineParser;

import com.github.sukhvir41.tags.Content;
import com.github.sukhvir41.utils.StringUtils;
import com.github.sukhvir41.utils.HtmlUtils;

import java.util.regex.Pattern;

final class TagLineProcessor implements LineProcessor {

    public static final Pattern ATTRIBUTE_MATCHER_PATTERN = Pattern.compile("" +
                    "[^\\u007f-\\u009f,\\u0020,\\u0022,\\u0027,\\u003e,\\u002f,\\u003d,\\uFDD0-\\uFDEF,\\uFFFE, " +
                    "\\uFFFF, \\u1FFFE, \\u1FFFF, \\u2FFFE, \\u2FFFF, \\u3FFFE, \\u3FFFF, \\u4FFFE, \\u4FFFF, " +
                    "\\u5FFFE, \\u5FFFF, \\u6FFFE, \\u6FFFF, \\u7FFFE, \\u7FFFF, \\u8FFFE, \\u8FFFF, \\u9FFFE, " +
                    "\\u9FFFF, \\uAFFFE, \\uAFFFF, \\uBFFFE, \\uBFFFF, \\uCFFFE, \\uCFFFF, \\uDFFFE, \\uDFFFF, " +
                    "\\uEFFFE, \\uEFFFF, \\uFFFFE, \\uFFFFF, \\u10FFFE, \\u10FFFF]*\\s*=\\s*\"[^/\"]*\""
            , Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern INCOMPLETE_ATTRIBUTE_MATCHER_PATTERN = Pattern.compile("" +
                    "[^\\u007f-\\u009f,\\u0020,\\u0022,\\u0027,\\u003e,\\u002f,\\u003d,\\uFDD0-\\uFDEF,\\uFFFE, " +
                    "\\uFFFF, \\u1FFFE, \\u1FFFF, \\u2FFFE, \\u2FFFF, \\u3FFFE, \\u3FFFF, \\u4FFFE, \\u4FFFF, " +
                    "\\u5FFFE, \\u5FFFF, \\u6FFFE, \\u6FFFF, \\u7FFFE, \\u7FFFF, \\u8FFFE, \\u8FFFF, \\u9FFFE, " +
                    "\\u9FFFF, \\uAFFFE, \\uAFFFF, \\uBFFFE, \\uBFFFF, \\uCFFFE, \\uCFFFF, \\uDFFFE, \\uDFFFF, " +
                    "\\uEFFFE, \\uEFFFF, \\uFFFFE, \\uFFFFF, \\u10FFFE, \\u10FFFF]*\\s*=\\s*\""
            , Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);// is unicode needed?


    @Override
    public boolean isClosingTagAtStart(String line) {
        //REGULAR will never close
        return false;
    }

    @Override
    public LineOutput getNextSectionOutput(String line) {

        if (org.apache.commons.lang3.StringUtils.isBlank(line)) {
            return LineOutput.builder()
                    .setNextMode(LineProcessingModes.TAG)
                    .setRemainingLine("")
                    .setSection("")
                    .build();
        }

        if (HtmlUtils.isHtmlTagAtStart(line)) {
            return getHtmlTagOutput(line);
        } else {
            return getContentOutput(line);
        }
    }

    private LineOutput getHtmlTagOutput(String line) {

        if (HtmlUtils.getStartingHtmlTagName(line).equals("!--")) {
            return LineOutput.builder()
                    .setNextMode(LineProcessingModes.COMMENT)
                    .setRemainingLine(line)
                    .setSection("")
                    .build();

        }

        var tag = getFirstCompleteHtmlTag(line);

        if (org.apache.commons.lang3.StringUtils.isBlank(tag)) {
            return LineOutput.builder()
                    .setRemainingLine(line)
                    .setNextMode(LineProcessingModes.TAG)
                    .setSection("")
                    .build();
        } else {
            var mode = getProcessingModeBasedOnTag(tag);
            var remainingLine = org.apache.commons.lang3.StringUtils.removeStart(line, tag);

            return LineOutput.builder()
                    .setNextMode(mode)
                    .setRemainingLine(remainingLine)
                    .setSection(tag)
                    .build();

        }
    }

    private LineProcessingModes getProcessingModeBasedOnTag(String tag) {
        var name = HtmlUtils.getStartingHtmlTagName(tag);
        var isClosing = HtmlUtils.isStartingTagClosingTag(tag);

        if (name.equalsIgnoreCase("script")) {
            if (isClosing) {
                return LineProcessingModes.TAG;
            } else {
                return LineProcessingModes.SCRIPT;
            }
        }

        if (name.equalsIgnoreCase("style")) {
            if (isClosing) {
                return LineProcessingModes.TAG;
            } else {
                return LineProcessingModes.STYLE;
            }
        }
        return LineProcessingModes.TAG;
    }


    private String getFirstCompleteHtmlTag(String section) {
        if (hasHtmlTagEnd(section)) {
            int endIndex = getHtmlTagEndIndex(section) + 1; // +1 as '>' needs to be included in substring
            return section.substring(0, endIndex);
        } else {
            return "";
        }
    }

    private boolean hasHtmlTagEnd(String section) {
        return getHtmlTagEndIndex(section) > -1;
    }

    private int getHtmlTagEndIndex(String section) {
        int start = 0;
        int index = section.indexOf('>');

        if (index == -1) {
            return -1;
        }

        while (isIndexWithinAttribute(index, start, section)) {
            start = getEndOfAttribute(start, section);
            index = org.apache.commons.lang3.StringUtils.indexOf(section, '>', start);
        }

        return index;
    }


    private boolean isIndexWithinAttribute(int index, int start, String section) {
        var matcher = ATTRIBUTE_MATCHER_PATTERN.matcher(section);
        var incompleteMatcher = INCOMPLETE_ATTRIBUTE_MATCHER_PATTERN.matcher(section);
        int searchStart = start;

        while (true) {
            if (matcher.find(searchStart)) {
                if (index > matcher.end()) {
                    searchStart = matcher.end();
                } else {
                    return index > matcher.start() && index < matcher.end();
                }
            } else if (incompleteMatcher.find(searchStart)) {
                return index < incompleteMatcher.end();
            } else {
                break;
            }
        }
        return false;
    }

    private int getEndOfAttribute(int start, String section) {
        var matcher = ATTRIBUTE_MATCHER_PATTERN.matcher(section);
        if (matcher.find(start)) {
            return matcher.end();
        } else {
            return section.length() - 1;
        }

    }


    private LineOutput getContentOutput(String line) {

        if (org.apache.commons.lang3.StringUtils.isBlank(line)) {
            return LineOutput.builder()
                    .setNextMode(LineProcessingModes.TAG)
                    .setSection("")
                    .setRemainingLine("")
                    .build();
        } else {
            var content = getContent(line);
            var remainingLine = org.apache.commons.lang3.StringUtils.removeStart(line, content);

            return LineOutput.builder()
                    .setSection(content)
                    .setRemainingLine(remainingLine)
                    .setNextMode(LineProcessingModes.TAG)
                    .build();

        }

    }

    private String getContent(String line) {

        if (hasHtmlTagStart(line)) {
            int index = getHtmlTagStartIndex(line);
            return line.substring(0, index);
        } else {
            return line;
        }

    }

    private boolean hasHtmlTagStart(String line) {
        return getHtmlTagStartIndex(line) > -1;
    }

    private int getHtmlTagStartIndex(String line) {
        int start = 0;
        int index = line.indexOf('<');

        if (index == -1) {
            return -1;
        }

        while (isIndexWithinContent(index, start, line)) {
            int contentStartIndex = line.indexOf(Content.ESCAPED_CONTENT_START, start);
            start = StringUtils.findIndex(contentStartIndex, Content.ESCAPED_CONTENT_END, line);
            index = org.apache.commons.lang3.StringUtils.indexOf(line, '<', start);
        }

        return index;
    }

    private boolean isIndexWithinContent(int index, int start, String section) {

        int contentStartIndex = section.indexOf(Content.ESCAPED_CONTENT_START, start);
        int contentEndIndex = StringUtils.findIndex(contentStartIndex, Content.ESCAPED_CONTENT_END, section);

        if (contentStartIndex == -1 || contentEndIndex == -1) {
            return false;
        }

        while (contentStartIndex != -1) {
            if (contentStartIndex > index) {
                return false;
            } else if (contentStartIndex < index && index < contentEndIndex) {
                return true;
            } else {
                contentStartIndex = section.indexOf(Content.ESCAPED_CONTENT_START, contentEndIndex);
                contentEndIndex = StringUtils.findIndex(contentEndIndex, Content.ESCAPED_CONTENT_END, section);
            }
        }

        return false;
    }

}
