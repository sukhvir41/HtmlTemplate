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

package com.github.sukhvir41.parsers.htmlParsers;

import com.github.sukhvir41.core.Template;
import com.github.sukhvir41.tags.HtmlTag;
import org.apache.commons.lang3.StringUtils;

final class TagParser implements HtmlParser {

    @Override
    public void parse(HtmlParserData data) {

        if (StringUtils.isBlank(data.getSection())) {
            return;
        }
        if (StringUtils.startsWith(data.getSection(), "<!--")) {
            HtmlParsers.COMMENT.parse(data);
        } else {
            processRegularHtmlTag(data);
        }
    }

    private void processRegularHtmlTag(HtmlParserData data) {
        HtmlTag htmlTag = data.getTemplate()
                .parseSection(data.getSection());
        processHtmlTag(htmlTag, data);
    }

    private void processHtmlTag(HtmlTag htmlTag, HtmlParserData data) {
        if (htmlTag.isSelfClosing() || htmlTag.isVoidTag() || htmlTag.isDocTypeTag()) {
            processSelfClosingTag(htmlTag, data);
        } else if (htmlTag.isClosingTag()) {
            processClosingTag(htmlTag, data);
        } else {
            processOpeningTag(htmlTag, data);
        }
    }

    private void processSelfClosingTag(HtmlTag htmlTag, HtmlParserData data) {
        htmlTag.processOpeningTag(data.getClassGenerator());
        htmlTag.processClosingTag(data.getClassGenerator());
    }

    private void processClosingTag(HtmlTag htmlTag, HtmlParserData data) {
        var classGenerator = data.getClassGenerator();

        var openingHtmlTag = classGenerator.removeFromTagStack()
                .orElseThrow(() -> new IllegalStateException("There is An extra closing tag. tag: " + data.getSection()));

        if (openingHtmlTag.isClosingTag(htmlTag)) {
            htmlTag.processOpeningTag(classGenerator);
            openingHtmlTag.processClosingTag(classGenerator);
        } else {
            throw new IllegalStateException("Miss matched closing tag. Tags may not be closed in the right order " + data.getSection());
        }

    }

    private void processOpeningTag(HtmlTag htmlTag, HtmlParserData data) {

        htmlTag.processOpeningTag(data.getClassGenerator());

        data.getClassGenerator()
                .addTagToStack(htmlTag);

        changeProcessor(htmlTag.getName(), data.getTemplate());

    }

    private void changeProcessor(String tagName, Template template) {

        if (tagName.equalsIgnoreCase("script")) {
            template.setHtmlParser(HtmlParsers.SCRIPT);
        } else if (tagName.equalsIgnoreCase("style")) {
            template.setHtmlParser(HtmlParsers.STYLE);
        }
    }
}
