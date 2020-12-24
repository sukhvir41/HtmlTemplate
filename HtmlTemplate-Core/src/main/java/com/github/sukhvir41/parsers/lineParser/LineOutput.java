/*
 * Copyright 2020 Sukhvir Thapar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.sukhvir41.parsers.lineParser;

public final class LineOutput {

    private final String section;
    private final String remainingLine;
    private final LineProcessingModes nextMode;


    private LineOutput(String section, String remainingLine, LineProcessingModes nextMode) {
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
        private LineProcessingModes nextMode = LineProcessingModes.TAG;

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
         * @return output
         */
        public LineOutput build() {
            if (remainingLine == null) {
                throw new IllegalArgumentException("Remaining line can't be null");
            }
            return new LineOutput(section, remainingLine, nextMode);
        }


    }

}
