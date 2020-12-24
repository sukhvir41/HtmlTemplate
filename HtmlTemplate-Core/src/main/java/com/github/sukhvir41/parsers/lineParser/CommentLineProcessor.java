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

final class CommentLineProcessor implements LineProcessor {

    @Override
    public boolean isClosingTagAtStart(String line) {
        return StringUtils.startsWith(line.trim(), "-->");
    }

    @Override
    public LineOutput getNextSectionOutput(String line) {

        if (line.contains("-->")) {

            var section = line.substring(0, line.indexOf("-->") + 3);
            var remainingLine = StringUtils.removeStart(line, section);
            return LineOutput.builder()
                    .setSection(section)
                    .setRemainingLine(remainingLine)
                    .setNextMode(LineProcessingModes.TAG)
                    .build();

        } else {
            return LineOutput.builder()
                    .setSection(line)
                    .setRemainingLine("")
                    .setNextMode(LineProcessingModes.COMMENT)
                    .build();

        }
    }
}
