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

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public final class HtmlUtils {

    //Handy link for html syntax https://html.spec.whatwg.org/#syntax

    private static final Set<String> voidTags = new HashSet<>();

    public static final Pattern SCRIPT_CLOSING_TAG_PATTERN =
            Pattern.compile("</\\s*script", Pattern.CASE_INSENSITIVE);

    public static final Pattern STYLE_CLOSING_TAG_PATTERN =
            Pattern.compile("</\\s*style", Pattern.CASE_INSENSITIVE);

    public static final Pattern DYNAMIC_ATTRIBUTE =
            Pattern.compile("ht-[^=]*\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    /**
     * string literal regex from stack overflow.
     * https://stackoverflow.com/questions/171480/regex-grabbing-values-between-quotation-marks
     * author: deadbug.
     * regex taken from comment by: Aran-Fey.
     */
    public static final Pattern CONTENT_VARIABLE_STRING_PATTERN =
            Pattern.compile("([\"])(?:\\\\.|[^\\\\])*?\\1", Pattern.CASE_INSENSITIVE);

    private static final String DOCTYPE = "!DOCTYPE";


    static {
        // void elements : https://html.spec.whatwg.org/#void-elements
        voidTags.add("area");
        voidTags.add("base");
        voidTags.add("br");
        voidTags.add("col");
        voidTags.add("embed");
        voidTags.add("hr");
        voidTags.add("img");
        voidTags.add("input");
        voidTags.add("link");
        voidTags.add("meta");
        voidTags.add("param");
        voidTags.add("source");
        voidTags.add("track");
        voidTags.add("wbr");
    }

    private HtmlUtils() {
        throw new UnsupportedOperationException("Cannot create an object");
    }


    public static boolean isVoidTag(String tagName) {
        return voidTags.contains(tagName);
    }


    /**
     * return true if the line parsed starts with a html tag ("<") else false
     *
     * @param line html line
     * @return if the html line start with a html tag i.e "<"
     */
    public static boolean isHtmlTagAtStart(String line) {
        if (StringUtils.isNotBlank(line)) {
            var newLine = line.trim();
            return newLine.charAt(0) == '<';
        } else {
            return false;
        }

    }

    /**
     * returns the name of the starting html tag name if the line start s with a html tag else throws an IllegalArgumentException
     * <pre>
     * "{{<some code>}} &lt;h1&gt;some heading &lt;/h1&gt; " = "throws IllegalArgumentException"
     * "some content &lt;h1&gt;some heading &lt;/h1&gt; " = "throws IllegalArgumentException"
     * " &lt;h1&gt;some heading &lt;/h1&gt;" = "h1"
     * " &lt;!DOCTYPE html&gt;some text &lt;/html&gt;" = "html"
     * </pre>
     *
     * @param line html line
     * @return html tag name of the first html tag in the line.
     * @throws IllegalArgumentException if the line does ot start with a html tag.
     */
    public static String getStartingHtmlTagName(String line) {
        if (isHtmlTagAtStart(line)) {
            return getHtmlTagName(line);
        } else {
            throw new IllegalArgumentException("The line passed does not start with a html tag. line -> " + line);
        }
    }

    /**
     * implementation of method {@link HtmlUtils#getStartingHtmlTagName}
     *
     * @param line html line
     * @return starting html tag name
     */
    private static String getHtmlTagName(String line) {
        var trimmedLine = line.trim();
        var name = new StringBuilder();

        int startPosition;

        boolean characterHit = false;

        if (isStartingTagClosingTag(trimmedLine)) {
            //starting form 2 as the first chars are '</' and need to be ignored.
            startPosition = 2;
        } else if (isStartingTagDocType(trimmedLine)) {
            startPosition = getEndOfDocTypeText(trimmedLine);
        } else {
            //starting form 1 ad the first char is '<' to ignore it.
            startPosition = 1;
        }

        for (int i = startPosition; i < trimmedLine.length(); i++) {
            if (trimmedLine.charAt(i) == ' ' || trimmedLine.charAt(i) == '>') {
                if (characterHit) {
                    break;
                }
            } else {
                characterHit = true;
                name.append(trimmedLine.charAt(i));
            }
        }

        return name.toString();
    }

    /**
     * assumes that the there is a html tag at start and that html tag start with !DOCTYPE.
     *
     * @param line line containing html
     * @return end index of !DOCTYPE
     */
    private static int getEndOfDocTypeText(String line) {
        return line.indexOf(DOCTYPE) + DOCTYPE.length();
    }


    public static boolean isStartingTagDocType(String line) {
        var trimmedLine = line.trim();

        if (!isHtmlTagAtStart(trimmedLine)) {
            return false;
        }

        if (isStartingTagClosingTag(line)) {
            return false;
        }

        var name = new StringBuilder();
        boolean characterHit = false;

        for (int i = 1; i < trimmedLine.length(); i++) {
            if (trimmedLine.charAt(i) == ' ' || trimmedLine.charAt(i) == '>') {
                if (characterHit) {
                    break;
                }
            } else {
                characterHit = true;
                name.append(trimmedLine.charAt(i));
            }
        }

        return name.toString()
                .equalsIgnoreCase(DOCTYPE);
    }

    public static boolean isStartingTagClosingTag(String line) {
        if (isHtmlTagAtStart(line)) {
            return line.trim()
                    .charAt(1) == '/';
        } else {
            throw new IllegalArgumentException("the line passed does not start with an html tag");
        }
    }

}
