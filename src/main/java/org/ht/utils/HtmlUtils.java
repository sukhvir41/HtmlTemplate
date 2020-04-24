package org.ht.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class HtmlUtils {

    private static final Set<String> voidTags = new HashSet<>();

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
            , Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

    public static final Pattern SCRIPT_CLOSING_TAG_PATTERN =
            Pattern.compile("</\\s*script", Pattern.CASE_INSENSITIVE);

    public static final Pattern STYLE_CLOSING_TAG_PATTERN =
            Pattern.compile("</\\s*style", Pattern.CASE_INSENSITIVE);

    public static final Pattern DYNAMIC_ATTRIBUTE =
            Pattern.compile("ht-[a-z]+", Pattern.CASE_INSENSITIVE);

    public static final Pattern META_TEMPLATE_TAG_PATTERN =
            Pattern.compile("<\\s*meta[\\s,\\S]* ht-template\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static final Pattern TEMPLATE_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-template\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);


    public static final Pattern CONTENT_VARIABLE_PATTERN =
            Pattern.compile("@[a-z,_,$][a-z,0-9,_,$]*", Pattern.CASE_INSENSITIVE);

    /**
     * string literal regex from stack overflow.
     * https://stackoverflow.com/questions/171480/regex-grabbing-values-between-quotation-marks
     * author: deadbug.
     * regex taken from comment by: Aran-Fey.
     */
    public static final Pattern CONTENT_VARIABLE_STRING_PATTERN =
            Pattern.compile("([\"])(?:\\\\.|[^\\\\])*?\\1", Pattern.CASE_INSENSITIVE);


    static {
        voidTags.add("meta");
        voidTags.add("link");
        voidTags.add("br");
        voidTags.add("hr");
        voidTags.add("area");
    }


    public static boolean isVoidTag(String tagName) {
        return voidTags.contains(tagName);
    }


    public static boolean isHtmlTagAtStart(String line) {

        if (StringUtils.isNotBlank(line)) {
            var newLine = line.trim();
            return newLine.charAt(0) == '<';
        } else {
            return false;
        }

    }

    public static String getStartingHtmlTagName(String line) {

        if (isHtmlTagAtStart(line)) {
            return getHtmlTagName(line);
        } else {
            throw new IllegalArgumentException("the line passed does not start with an html tag. line -> " + line);
        }
    }

    private static String getHtmlTagName(String line) {
        var trimmedLine = line.trim();
        var name = new StringBuilder();

        int startPosition;

        boolean characterHit = false;

        if (isStartingTagClosingTag(trimmedLine)) {
            //starting form 2 as the first chars are '</' and need to be ignored.
            startPosition = 2;
        } else if (isStartingTagDocType(line)) {
            startPosition = getEndOfDocTypeText(line);
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

    private static int getEndOfDocTypeText(String line) {
        var trimmedLine = line.trim();

        int index = 1;
        boolean characterHit = false;


        for (int i = 1; i < trimmedLine.length(); i++) {
            if (trimmedLine.charAt(i) == ' ' || trimmedLine.charAt(i) == '>') {
                if (characterHit) {
                    break;
                }
            } else {
                characterHit = true;
                ++index;
            }
        }

        return index;

    }

    public static boolean isStartingTagDocType(String line) {
        var trimmedLine = line.trim();

        if (isStartingTagClosingTag(line)) {
            return false;
        } else {

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
                    .equalsIgnoreCase("!DOCTYPE");
        }

    }


    public static boolean isStartingTagClosingTag(String line) {
        if (isHtmlTagAtStart(line)) {
            return line.trim()
                    .charAt(1) == '/';
        } else {
            throw new IllegalArgumentException("the line passed does not start with an html tag");
        }
    }

    public static boolean isTemplateMetaTag(String tags) {
        return META_TEMPLATE_TAG_PATTERN.matcher(tags)
                .find();
    }

}
