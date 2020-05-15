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

public final class CommentLineProcessingMode implements LineProcessingMode, HtmlProcessor {

    @Override
    public boolean isClosingTagAtStart(String line) {
        return StringUtils.startsWith(line.trim(), "-->");
    }

    @Override
    public ProcessingOutput getNextSectionOutput(String line) {

        if (line.contains("-->")) {

            var section = line.substring(0, line.indexOf("-->") + 3);
            var remainingLine = StringUtils.removeStart(line, section);
            return ProcessingOutput.builder()
                    .setSection(section)
                    .setRemainingLine(remainingLine)
                    .setNextMode(LineProcessingModes.REGULAR)
                    .build();

        } else {
            return ProcessingOutput.builder()
                    .setSection(line)
                    .setRemainingLine("")
                    .setNextMode(LineProcessingModes.COMMENT)
                    .build();

        }
    }

    // -------------- HtmlProcessor ----------------------

    @Override
    public void process(HtmlProcessorData data) {

        if (containsClosingPart(data.getHtml())) {

            var comment = getComment(data.getHtml());

            data.getTemplateClass()
                    .appendPlainHtml(comment);

            var remainingHtml = StringUtils.removeStart(data.getHtml(), comment);

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
                    .setProcessor(HtmlProcessors.COMMENT);
        }


    }

    private String getComment(String html) {
        if (containsClosingPart(html)) {
            return html.substring(0, html.indexOf("-->") + 3);
        } else {
            return "";
        }
    }

    private boolean containsClosingPart(String html) {
        return html.contains("-->");
    }
}
