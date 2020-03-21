package org.ht.processors;

public enum ProcessingModes implements ProcessingMode {
    REGULAR(new RegularProcessingMode()),
    SCRIPT(new ScriptProcessingMode()),
    STYLE(new StyleProcessingMode()),
    COMMENT(new CommentProcessingMode());


    ProcessingMode mode;

    ProcessingModes(ProcessingMode mode) {
        this.mode = mode;
    }


    @Override
    public boolean isClosingTagAtStart(String line) {
        return mode.isClosingTagAtStart(line);
    }

    @Override
    public ProcessingOutput getNextSectionOutput(String section) {
        return mode.getNextSectionOutput(section);
    }

}
