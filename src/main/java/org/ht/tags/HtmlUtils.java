package org.ht.tags;

import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class HtmlUtils {

    private static final Set<String> voidTags = new HashSet<>();

    public static final Pattern ATTRIBUTE_MATCHER_PATTERN = Pattern.compile("" +
                    "[^\\u007f-\\u009f,^\\u0020,^\\u0022,^\\u0027,^\\u003e,^\\u002f,^\\u003d,^\\uFDD0-\\uFDEF,^\\uFFFE, " +
                    "^\\uFFFF, ^\\u1FFFE, ^\\u1FFFF, ^\\u2FFFE, ^\\u2FFFF, ^\\u3FFFE, ^\\u3FFFF, ^\\u4FFFE, ^\\u4FFFF, " +
                    "^\\u5FFFE, ^\\u5FFFF, ^\\u6FFFE, ^\\u6FFFF, ^\\u7FFFE, ^\\u7FFFF, ^\\u8FFFE, ^\\u8FFFF, ^\\u9FFFE, " +
                    "^\\u9FFFF, ^\\uAFFFE, ^\\uAFFFF, ^\\uBFFFE, ^\\uBFFFF, ^\\uCFFFE, ^\\uCFFFF, ^\\uDFFFE, ^\\uDFFFF, " +
                    "^\\uEFFFE, ^\\uEFFFF, ^\\uFFFFE, ^\\uFFFFF, ^\\u10FFFE, ^\\u10FFFF]*\\s*=\\s*\"[^/\"]*\""
            , Pattern.CASE_INSENSITIVE);

    public static final Pattern SCRIPT_CLOSING_TAG_PATTERN =
            Pattern.compile("</\\s*script", Pattern.CASE_INSENSITIVE);

    public static final Pattern STYLE_CLOSING_TAG_PATTERN =
            Pattern.compile("</\\s*style", Pattern.CASE_INSENSITIVE);

    public static final Pattern DYNAMIC_ATTRIBUTE =
            Pattern.compile("ht-[a-z]+", Pattern.CASE_INSENSITIVE);

    public static final Pattern IF_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-if\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static final Pattern IMPORT_META_TAG_PATTERN =
            Pattern.compile("<\\s*meta[\\s,\\S]* ht-import\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static final Pattern IMPORT_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-import\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static final Pattern ESCAPED_CONTENT_PATTERN =
            Pattern.compile("\\{\\{\\s*[^}}]*\\}\\}"); //this will match unescasped content pattern as well

    public static final Pattern UNESCAPED_CONTENT_PATTERN =
            Pattern.compile("\\{\\{\\{\\s*[^}}]*\\}\\}\\}");

    public static final Pattern META_TYPE_TAG_PATTERN =
            Pattern.compile("<\\s*meta\\s+ht-type\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static final Pattern TYPE_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-type\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static final Pattern PARAMS_PATTERN =
            Pattern.compile("[^a-z,0-9,\\.]+params.get\\(\"[^\"]*\"\\)", Pattern.CASE_INSENSITIVE);

    //params match [^a-z,0-9,\.]+params.get\("[^"]*"\)

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
            //starting form 2 and the first chars are '</' to ignore it.
            startPosition = 2;
        } else if (isStartingTagDocType(line)) {
            startPosition = getEndOfDocTypeTest(line);
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

    private static int getEndOfDocTypeTest(String line) {
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
}
