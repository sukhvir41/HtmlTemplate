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

import static com.github.sukhvir41.utils.HtmlUtils.STYLE_CLOSING_TAG_PATTERN;

final class StyleLineProcessor implements LineProcessor {

    @Override
    public boolean isClosingTagAtStart(String line) {
        var matcher = STYLE_CLOSING_TAG_PATTERN.matcher(line);

        if (matcher.find()) {
            var endString = line.substring(matcher.start(), matcher.end());

            return StringUtils.startsWith(line.trim(), endString);

        } else {
            return false;
        }
    }

    @Override
    public LineOutput getNextSectionOutput(String line) {
        var matcher = STYLE_CLOSING_TAG_PATTERN.matcher(line);

        if (matcher.find()) {
            var theSection = line.substring(0, matcher.start());

            var remainingPart = StringUtils.removeStart(line, theSection);

            return new LineOutput.Builder()
                    .setNextMode(LineProcessingModes.TAG)
                    .setSection(theSection)
                    .setRemainingLine(remainingPart)
                    .build();

        } else {

            return new LineOutput.Builder()
                    .setNextMode(LineProcessingModes.STYLE)
                    .setSection(line)
                    .setRemainingLine("")
                    .build();
        }
    }

}
