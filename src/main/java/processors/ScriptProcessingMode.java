package processors;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public final class ScriptProcessingMode implements ProcessingMode, HtmlProcessor {

    private final Pattern SCRIPT_CLOSING_TAG_PATTERN = Pattern.compile("</\\s*script", Pattern.CASE_INSENSITIVE);


    @Override
    public boolean isClosingTagAtStart(String line) {
        var matcher = SCRIPT_CLOSING_TAG_PATTERN.matcher(line);

        if (matcher.find()) {
            var endString = line.substring(matcher.start(), matcher.end());

            return StringUtils.startsWith(line.trim(), endString);

        } else {
            return false;
        }
    }

    @Override
    public ProcessingOutput getNextSectionOutput(String section) {
        var matcher = SCRIPT_CLOSING_TAG_PATTERN.matcher(section);

        if (matcher.find()) {
            var theSection = section.substring(0, matcher.start());

            var remainingPart = StringUtils.removeStart(section, theSection);

            return new ProcessingOutput.Builder()
                    .setNextMode(ProcessingModes.REGULAR)
                    .setSection(theSection)
                    .setRemainingLine(remainingPart)
                    .build();

        } else {

            return new ProcessingOutput.Builder()
                    .setNextMode(ProcessingModes.SCRIPT)
                    .setSection(section)
                    .setRemainingLine("")
                    .build();
        }

    }


    @Override
    public void process(HtmlProcessorData data) {

        if (containsClosingTag(data.getHtml())) {
            var script = getLeftOfScriptTag(data.getHtml());

            data.getTemplateClass()
                    .appendScript(script);

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
                    .appendScript(data.getHtml());

            data.getHtmlTemplate()
                    .setProcessor(HtmlProcessors.SCRIPT);
        }

    }

    private boolean containsClosingTag(String html) {

        var matcher = SCRIPT_CLOSING_TAG_PATTERN.matcher(html);
        return matcher.find();

    }


    private String getLeftOfScriptTag(String html) {
        var matcher = SCRIPT_CLOSING_TAG_PATTERN.matcher(html);
        if (matcher.find()) {
            return html.substring(0, matcher.end());
        } else {
            return "";
        }
    }
}
