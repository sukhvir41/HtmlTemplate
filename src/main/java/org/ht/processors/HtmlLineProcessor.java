package org.ht.processors;

import org.apache.commons.lang3.StringUtils;

public class HtmlLineProcessor {


    private String line;
    private String previousLine = ""; // this contains the previous lines part that got unprocessed.
    private String nextSection;
    private boolean isNextSectionCalculated = false;
    protected ProcessingModes mode = ProcessingModes.REGULAR;

    public void setLine(String line) {
        this.line = previousLine + line;
        this.previousLine = "";
    }

    public void carryForwardUnprocessedString() {
        if (!line.isBlank()) {
            previousLine = line;
        } else {
            previousLine = "";
        }
    }


    public boolean hasNextSection() {

        var output = mode.getNextSectionOutput(line);

        if (wasSomethingProcessed(output)) {
            this.line = output.getRemainingLine();
            this.nextSection = output.getSection();
            this.mode = output.getNextMode();
            isNextSectionCalculated = true;
            return true;
        }

        return false;
    }

    /**
     * checks to see if something processed
     *
     * @param output
     * @return
     */
    private boolean wasSomethingProcessed(ProcessingOutput output) {

        if (StringUtils.isNotBlank(output.getSection())) {
            return true;
        } else {
            return output.getNextMode() != this.mode;
        }
    }

    public String getNextSection() {
        if (isNextSectionCalculated) {
            isNextSectionCalculated = false;
            return nextSection;
        } else {
            return "";
        }
    }

}
