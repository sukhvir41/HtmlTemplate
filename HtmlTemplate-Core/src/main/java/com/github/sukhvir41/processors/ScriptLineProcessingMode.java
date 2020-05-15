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

package com.github.sukhvir41.processors;

import org.apache.commons.lang3.StringUtils;
import com.github.sukhvir41.utils.HtmlUtils;

public final class ScriptLineProcessingMode implements LineProcessingMode, HtmlProcessor {

    @Override
    public boolean isClosingTagAtStart(String line) {
        var matcher = HtmlUtils.SCRIPT_CLOSING_TAG_PATTERN.matcher(line);

        if (matcher.find()) {
            var endString = line.substring(matcher.start(), matcher.end());

            return StringUtils.startsWith(line.trim(), endString);

        } else {
            return false;
        }
    }

    @Override
    public ProcessingOutput getNextSectionOutput(String section) {
        var matcher = HtmlUtils.SCRIPT_CLOSING_TAG_PATTERN.matcher(section);

        if (matcher.find()) {
            var theSection = section.substring(0, matcher.start());

            var remainingPart = StringUtils.removeStart(section, theSection);

            return new ProcessingOutput.Builder()
                    .setNextMode(LineProcessingModes.REGULAR)
                    .setSection(theSection)
                    .setRemainingLine(remainingPart)
                    .build();

        } else {

            return new ProcessingOutput.Builder()
                    .setNextMode(LineProcessingModes.SCRIPT)
                    .setSection(section)
                    .setRemainingLine("")
                    .build();
        }

    }


    // --------------- HtmlProcessor -----------------

    @Override
    public void process(HtmlProcessorData data) {

        if (containsClosingTag(data.getHtml())) {
            var script = getLeftOfScriptTag(data.getHtml());

            data.getTemplateClass()
                    .appendPlainHtml(script);

            var remainingHtml = StringUtils.removeStart(data.getHtml(), script);

            data.getHtmlTemplate()
                    .setProcessor(HtmlProcessors.REGULAR);

            var newData = HtmlProcessorData.builder()
                    .setHtml(remainingHtml)
                    .setHtmlTemplate(data.getHtmlTemplate())
                    .setTemplateClass(data.getTemplateClass())
                    .build();

            HtmlProcessors.REGULAR.process(newData);


        } else {
            data.getTemplateClass()
                    .appendPlainHtml(data.getHtml());

            data.getHtmlTemplate()
                    .setProcessor(HtmlProcessors.SCRIPT);
        }

    }

    private boolean containsClosingTag(String html) {

        var matcher = HtmlUtils.SCRIPT_CLOSING_TAG_PATTERN.matcher(html);
        return matcher.find();

    }


    private String getLeftOfScriptTag(String html) {
        var matcher = HtmlUtils.SCRIPT_CLOSING_TAG_PATTERN.matcher(html);
        if (matcher.find()) {
            return html.substring(0, matcher.start());
        } else {
            return "";
        }
    }
}
