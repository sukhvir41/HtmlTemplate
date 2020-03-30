package org.ht.processors;

import org.apache.commons.lang3.StringUtils;
import org.ht.tags.HtmlUtils;

public final class ScriptLineProcessingMode implements LineProcessingMode, HtmlProcessor {

    @Override
    public boolean isClosingTagAtStart(String line) {
        var matcher = HtmlUtils.SCRIPT_CLOSING_TAG_PATTERN.matcher(line);

        if (matcher.find()) {
            var endString = line.substring(matcher.start(), matcher.end());

            return StringUtils.startsWith(line.trim(), endString);

        } else {
            return false;
        }
    }

    @Override
    public ProcessingOutput getNextSectionOutput(String section) {
        var matcher = HtmlUtils.SCRIPT_CLOSING_TAG_PATTERN.matcher(section);

        if (matcher.find()) {
            var theSection = section.substring(0, matcher.start());

            var remainingPart = StringUtils.removeStart(section, theSection);

            return new ProcessingOutput.Builder()
                    .setNextMode(LineProcessingModes.REGULAR)
                    .setSection(theSection)
                    .setRemainingLine(remainingPart)
                    .build();

        } else {

            return new ProcessingOutput.Builder()
                    .setNextMode(LineProcessingModes.SCRIPT)
                    .setSection(section)
                    .setRemainingLine("")
                    .build();
        }

    }


    // --------------- HtmlProcessor -----------------

    @Override
    public void process(HtmlProcessorData data) {


        if (containsClosingTag(data.getHtml())) {
            var script = getLeftOfScriptTag(data.getHtml());

            data.getTemplateClass()
                    .appendPlainHtml(script);

            var remainingHtml = StringUtils.removeStart(data.getHtml(), script);

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
                    .appendPlainHtml(data.getHtml());

            data.getHtmlTemplate()
                    .setProcessor(HtmlProcessors.SCRIPT);
        }

    }

    private boolean containsClosingTag(String html) {

        var matcher = HtmlUtils.SCRIPT_CLOSING_TAG_PATTERN.matcher(html);
        return matcher.find();

    }


    private String getLeftOfScriptTag(String html) {
        var matcher = HtmlUtils.SCRIPT_CLOSING_TAG_PATTERN.matcher(html);
        if (matcher.find()) {
            return html.substring(0, matcher.start());
        } else {
            return "";
        }
    }
}
