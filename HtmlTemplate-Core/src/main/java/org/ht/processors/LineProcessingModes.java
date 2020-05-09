package org.ht.processors;

public enum LineProcessingModes implements LineProcessingMode {
    REGULAR(new RegularLineProcessingMode()),
    SCRIPT(new ScriptLineProcessingMode()),
    STYLE(new StyleLineProcessingMode()),
    COMMENT(new CommentLineProcessingMode());


    LineProcessingMode mode;

    LineProcessingModes(LineProcessingMode mode) {
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
