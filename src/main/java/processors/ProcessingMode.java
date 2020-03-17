package processors;

public interface ProcessingMode {

    boolean isClosingTagAtStart(String line);

    ProcessingOutput getNextSectionOutput(String line);

}
