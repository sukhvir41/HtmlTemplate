package org.ht.processors;

import org.apache.commons.lang3.StringUtils;

import static org.ht.tags.HtmlUtils.STYLE_CLOSING_TAG_PATTERN;

public class StyleProcessingMode implements ProcessingMode, HtmlProcessor {


    @Override
    public boolean isClosingTagAtStart(String line) {
        var matcher = STYLE_CLOSING_TAG_PATTERN.matcher(line);

        if (matcher.find()) {
            var endString = line.substring(matcher.start(), matcher.end());

            return StringUtils.startsWith(line.trim(), endString);

        } else {
            return false;
        }
    }

    @Override
    public ProcessingOutput getNextSectionOutput(String line) {
        var matcher = STYLE_CLOSING_TAG_PATTERN.matcher(line);

        if (matcher.find()) {
            var theSection = line.substring(0, matcher.start());

            var remainingPart = StringUtils.removeStart(line, theSection);

            return new ProcessingOutput.Builder()
                    .setNextMode(ProcessingModes.REGULAR)
                    .setSection(theSection)
                    .setRemainingLine(remainingPart)
                    .build();

        } else {

            return new ProcessingOutput.Builder()
                    .setNextMode(ProcessingModes.STYLE)
                    .setSection(line)
                    .setRemainingLine("")
                    .build();
        }
    }

    @Override
    public void process(HtmlProcessorData data) {

        if (containsClosingTag(data.getHtml())) {
            var style = getLeftOfClosingTag(data.getHtml());

            data.getTemplateClass()
                    .appendStyle(style);

            var remainingHtml = StringUtils.removeStart(data.getHtml(), style);

            data.getHtmlTemplate()
                    .setProcessor(HtmlProcessors.REGULAR);

            var newData = HtmlProcessorData.builder()
                    .setHtml(remainingHtml)
                    .setHtmlTemplate(data.getHtmlTemplate())
                    .setTemplateClass(data.getTemplateClass())
                    .build();

            HtmlProcessors.REGULAR.process(newData);

        } else {

            data.getTemplateClass()
                    .appendStyle(data.getHtml());

            data.getHtmlTemplate()
                    .setProcessor(HtmlProcessors.STYLE);

        }


    }

    private String getLeftOfClosingTag(String html) {
        var matcher = STYLE_CLOSING_TAG_PATTERN.matcher(html);

        if (matcher.find()) {
            return html.substring(0, matcher.start());
        } else {
            return "";
        }


    }

    private boolean containsClosingTag(String html) {
        var matcher = STYLE_CLOSING_TAG_PATTERN.matcher(html);
        return matcher.find();

    }
}
