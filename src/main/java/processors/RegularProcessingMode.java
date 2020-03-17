package processors;

import org.apache.commons.lang3.StringUtils;
import tags.HtmlUtils;

import java.util.regex.Pattern;

import static processors.ProcessingModes.*;

public class RegularProcessingMode implements ProcessingMode {


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
            return getContent(line);
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

    private ProcessingModes getProcessingModeBasedOnTag(String tag) {
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
            int endIndex = getHtmlTagEndIndex(section) + 1;
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

        int searchStart = 0;

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


    private ProcessingOutput getContent(String line) {
        var output = ProcessingOutput.builder();
        // todo: have to check if < is actual start of a tag or inside "{{ }}"
        var section = line.substring(0, line.indexOf('<'));
        var remainingLine = StringUtils.removeStart(line, section);

        return output.setRemainingLine(remainingLine)
                .setSection(section)
                .setNextMode(REGULAR)
                .build();
    }

}
