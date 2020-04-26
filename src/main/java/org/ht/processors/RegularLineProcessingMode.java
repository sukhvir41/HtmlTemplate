package org.ht.processors;

import org.apache.commons.lang3.StringUtils;
import org.ht.tags.Content;
import org.ht.utils.HtStringUtils;
import org.ht.utils.HtmlUtils;

import static org.ht.processors.LineProcessingModes.*;

public class RegularLineProcessingMode implements LineProcessingMode {


    @Override
    public boolean isClosingTagAtStart(String line) {
        //REGULAR will never close
        return false;
    }

    @Override
    public ProcessingOutput getNextSectionOutput(String line) {

        if (line.isBlank()) {
            return ProcessingOutput.builder()
                    .setNextMode(REGULAR)
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

    private ProcessingOutput getHtmlTagOutput(String line) {
        var tag = getHtmlTag(line);
        var output = ProcessingOutput.builder();


        if (HtmlUtils.getStartingHtmlTagName(line).equals("!--")) {
            return output.setNextMode(COMMENT)
                    .setRemainingLine(line)
                    .setSection("")
                    .build();

        } else if (StringUtils.isBlank(tag)) {
            return output.setRemainingLine(line)
                    .setNextMode(REGULAR)
                    .setSection("")
                    .build();
        } else {
            var mode = getProcessingModeBasedOnTag(tag);
            var remainingLine = StringUtils.removeStart(line, tag);

            return output.setNextMode(mode)
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
                return REGULAR;
            } else {
                return SCRIPT;
            }
        }

        if (name.equalsIgnoreCase("style")) {
            if (isClosing) {
                return REGULAR;
            } else {
                return STYLE;
            }
        }
        return REGULAR;
    }


    private String getHtmlTag(String section) {
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
            index = StringUtils.indexOf(section, '>', start);
        }

        return index;
    }


    private boolean isIndexWithinAttribute(int index, int start, String section) {
        var matcher = HtmlUtils.ATTRIBUTE_MATCHER_PATTERN.matcher(section);
        var incompleteMatcher = HtmlUtils.INCOMPLETE_ATTRIBUTE_MATCHER_PATTERN.matcher(section);
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
        var matcher = HtmlUtils.ATTRIBUTE_MATCHER_PATTERN.matcher(section);
        if (matcher.find(start)) {
            return matcher.end();
        } else {
            return section.length() - 1;
        }

    }


    private ProcessingOutput getContentOutput(String line) {

        if (StringUtils.isBlank(line)) {
            return ProcessingOutput.builder()
                    .setNextMode(REGULAR)
                    .setSection("")
                    .setRemainingLine("")
                    .build();
        } else {
            var content = getContent(line);
            var remainingLine = StringUtils.removeStart(line, content);

            return ProcessingOutput.builder()
                    .setSection(content)
                    .setRemainingLine(remainingLine)
                    .setNextMode(REGULAR)
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
            start = HtStringUtils.findIndex(contentStartIndex, Content.ESCAPED_CONTENT_END, line);
            index = StringUtils.indexOf(line, '<', start);
        }

        return index;
    }

    private boolean isIndexWithinContent(int index, int start, String section) {

        int contentStartIndex = section.indexOf(Content.ESCAPED_CONTENT_START, start);
        int contentEndIndex = HtStringUtils.findIndex(contentStartIndex, Content.ESCAPED_CONTENT_END, section);

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
                contentEndIndex = HtStringUtils.findIndex(contentEndIndex, Content.ESCAPED_CONTENT_END, section);
            }
        }

        return false;
    }

}