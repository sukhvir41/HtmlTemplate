package org.ht.processors;

public interface LineProcessingMode {

    boolean isClosingTagAtStart(String line);

    ProcessingOutput getNextSectionOutput(String line);

}
