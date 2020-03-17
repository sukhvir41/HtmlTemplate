package processors;

import org.apache.commons.lang3.StringUtils;

public class CommentProcessingMode implements ProcessingMode, HtmlProcessor {

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
                    .setNextMode(ProcessingModes.REGULAR)
                    .build();

        } else if (line.contains("-->")) {

            var section = line.substring(0, line.indexOf("-->"));
            var remainingLine = StringUtils.removeStart(line, section);
            return ProcessingOutput.builder()
                    .setSection(section)
                    .setRemainingLine(remainingLine)
                    .setNextMode(ProcessingModes.COMMENT)
                    .build();

        } else {

            return ProcessingOutput.builder()
                    .setSection(line)
                    .setRemainingLine("")
                    .setNextMode(ProcessingModes.COMMENT)
                    .build();

        }
    }

    @Override
    public void process(HtmlProcessorData data) {

    }
}
