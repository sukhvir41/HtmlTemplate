package org.ht.processors;

import org.apache.commons.lang3.StringUtils;
import org.ht.tags.HtmlUtils;

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

        if (StringUtils.isBlank(tag)) {
            return output.setRemainingLine(line)
                    .setNextMode(REGULAR)
                    .setSection("")
                    .build();
        } else {
            var mode = getProcessingModeBasedOnTag(tag);
            var remainingLine = StringUtils.removeStart(line, tag);
            if (mode == REGULAR) {
                return output.setNextMode(mode)
                        .setRemainingLine(remainingLine)
                        .setSection(tag)
                        .build();
            } else {
                if (mode.isClosingTagAtStart(remainingLine)) {
                    return output.setNextMode(REGULAR)
                            .setRemainingLine(remainingLine)
                            .setSection(tag)
                            .build();
                } else {
                    return output.setNextMode(mode)
                            .setRemainingLine(remainingLine)
                            .setSection(tag)
                            .build();
                }
            }

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

        if (StringUtils.startsWith(name, "!--")) {
            if (StringUtils.endsWith(tag, "-->")) {
                return REGULAR;
            } else {
                return COMMENT;
            }
        }

        return REGULAR;
    }


    private String getHtmlTag(String section) {
        if (hasHtmlTagEnd(section)) {
            int endIndex = getHtmlTagEndIndex(section) + 1; // +1 as we '>' needs to be included in substring
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

        if (index > -1) {

            while (true) {
                if (isIndexWithinAttribute(index, start, section)) {
                    start = getEndOfAttribute(start, section);
                    index = StringUtils.indexOf(section, '>', start);
                } else {
                    break;
                }
            }

            return index;

        } else {
            return -1;
        }


    }


    private boolean isIndexWithinAttribute(int index, int start, String section) {
        var matcher = HtmlUtils.ATTRIBUTE_MATCHER_PATTERN.matcher(section);

        int searchStart = start;

        while (true) {
            if (matcher.find(searchStart)) {
                if (index > matcher.end()) {
                    searchStart = matcher.end();
                } else {
                    return index > matcher.start() && index < matcher.end();
                }
            } else {
                break;
            }
        }
        return false;
    }

    private int getEndOfAttribute(int start, String section) {
        var matcher = HtmlUtils.ATTRIBUTE_MATCHER_PATTERN.matcher(section);

        matcher.find(start);

        return matcher.end();

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

        if (index > -1) {
            while (true) {
                if (isIndexWithinContent(index, start, line)) {
                    start = getContentEndIndex(start, line);
                    index = StringUtils.indexOf(line, '<', start);
                } else {
                    break;
                }
            }
            return index;
        } else {
            return -1;
        }
    }

    private boolean isIndexWithinContent(int index, int start, String section) {
        var matcher = HtmlUtils.ESCAPED_CONTENT_PATTERN.matcher(section);

        int searchStart = start;

        while (true) {
            if (matcher.find(searchStart)) {
                if (index > matcher.end()) {
                    searchStart = matcher.end();
                } else {
                    return index > matcher.start() && index < matcher.end();
                }
            } else {
                break;
            }
        }
        return false;
    }

    private int getContentEndIndex(int start, String line) {
        var matcher = HtmlUtils.ESCAPED_CONTENT_PATTERN.matcher(line);

        matcher.find(start);

        return matcher.end();
    }


}
