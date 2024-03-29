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

import org.apache.commons.lang3.StringUtils;

import java.util.logging.Logger;

public final class LineParser {

    private String line;
    private String previousLine = ""; // this contains the previous lines part that got unprocessed.
    private String nextSection;
    private boolean isNextSectionCalculated = false;
    LineProcessingModes mode = LineProcessingModes.TAG;

    public void setLine(String line) {

        if (StringUtils.isNotBlank(previousLine)) {
            this.line = previousLine + " " + StringUtils.stripStart(line, " ");
        } else {
            this.line = line;
        }
    }

    private void carryForwardUnprocessedLine() {
        if (StringUtils.isNotBlank(line)) {
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

        carryForwardUnprocessedLine();
        return false;
    }

    private boolean wasSomethingProcessed(LineOutput output) {

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
