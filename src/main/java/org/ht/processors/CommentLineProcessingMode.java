package org.ht.processors;

import org.apache.commons.lang3.StringUtils;

public class CommentLineProcessingMode implements LineProcessingMode, HtmlProcessor {

    @Override
    public boolean isClosingTagAtStart(String line) {
        return StringUtils.startsWith(line.trim(), "-->");
    }

    @Override
    public ProcessingOutput getNextSectionOutput(String line) {

        if (isClosingTagAtStart(line)) {

            var section = line.substring(0, line.indexOf("-->") + 3);
            var remainingLine = StringUtils.removeStart(line, section);
            return ProcessingOutput.builder()
                    .setSection(section)
                    .setRemainingLine(remainingLine)
                    .setNextMode(LineProcessingModes.REGULAR)
                    .build();

        } else if (line.contains("-->")) {

            var section = line.substring(0, line.indexOf("-->"));
            var remainingLine = StringUtils.removeStart(line, section);
            return ProcessingOutput.builder()
                    .setSection(section)
                    .setRemainingLine(remainingLine)
                    .setNextMode(LineProcessingModes.COMMENT)
                    .build();

        } else {

            return ProcessingOutput.builder()
                    .setSection(line)
                    .setRemainingLine("")
                    .setNextMode(LineProcessingModes.COMMENT)
                    .build();

        }
    }

    @Override
    public void process(HtmlProcessorData data) {

        if (containsClosingPart(data.getHtml())) {

            var comment = getComment(data.getHtml());

            data.getTemplateClass()
                    .appendComment(comment);

            var remainingHtml = StringUtils.removeStart(data.getHtml(), comment);

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
                    .appendComment(data.getHtml());

            data.getHtmlTemplate()
                    .setProcessor(HtmlProcessors.COMMENT);
        }


    }

    private String getComment(String html) {
        if (containsClosingPart(html)) {
            return html.substring(0, html.indexOf("-->") + 3);
        } else {
            return "";
        }
    }

    private boolean containsClosingPart(String html) {
        return html.contains("-->");
    }
}
