package org.ht.processors;

public final class ProcessingOutput {

    private String section;
    private String remainingLine;
    private LineProcessingModes nextMode;


    private ProcessingOutput(String section, String remainingLine, LineProcessingModes nextMode) {
        this.section = section;
        this.remainingLine = remainingLine;
        this.nextMode = nextMode;
    }

    public String getSection() {
        return section;
    }

    public LineProcessingModes getNextMode() {
        return nextMode;
    }

    public String getRemainingLine() {
        return remainingLine;
    }


    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String section = "";
        private String remainingLine;
        private LineProcessingModes nextMode = LineProcessingModes.REGULAR;

        public Builder setSection(String section) {
            this.section = section;
            return this;
        }

        public Builder setNextMode(LineProcessingModes nextMode) {
            this.nextMode = nextMode;
            return this;
        }

        public Builder setRemainingLine(String remainingLine) {
            this.remainingLine = remainingLine;
            return this;
        }

        /**
         * by default creates a ProcessingOutput with default values of
         * section = "" and nextMode = ProcessingModes.REGULAR
         * unless specified
         *
         * @return out put
         */
        public ProcessingOutput build() {
            if (remainingLine == null) {
                throw new IllegalArgumentException("Remaining line can't be null");
            }
            return new ProcessingOutput(section, remainingLine, nextMode);
        }


    }

}
