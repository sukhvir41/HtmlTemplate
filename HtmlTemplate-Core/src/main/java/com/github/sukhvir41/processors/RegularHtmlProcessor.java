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

import com.github.sukhvir41.tags.Content;
import com.github.sukhvir41.tags.HtmlTag;
import com.github.sukhvir41.tags.HtmlTags;
import com.github.sukhvir41.template.HtmlTemplate;
import org.apache.commons.lang3.StringUtils;

public final class RegularHtmlProcessor implements HtmlProcessor {

    @Override
    public void process(HtmlProcessorData data) {

        if (StringUtils.isBlank(data.getHtml())) {
            return;
        }
        if (StringUtils.startsWith(data.getHtml(), "<!--")) {
            HtmlProcessors.COMMENT.process(data);
        } else {
            processRegularHtmlTag(data);
        }
    }

    private void processRegularHtmlTag(HtmlProcessorData data) {
        HtmlTags.parse(data.getHtml())
                .ifPresentOrElse(htmlTag -> processHtmlTag(htmlTag, data), () -> processHtmlContent(data));
    }

    private void processHtmlTag(HtmlTag htmlTag, HtmlProcessorData data) {
        if (htmlTag.isSelfClosing() || htmlTag.isVoidTag() || htmlTag.isDocTypeTag()) {
            processSelfClosingTag(htmlTag, data);
        } else if (htmlTag.isClosingTag()) {
            processClosingTag(htmlTag, data);
        } else {
            processOpeningTag(htmlTag, data);
        }
    }

    private void processOpeningTag(HtmlTag htmlTag, HtmlProcessorData data) {

        htmlTag.processOpeningTag(data.getTemplateClass());

        htmlTag.processTag(data.getTemplateClass());
        data.getHtmlTemplate()
                .addToTagStack(htmlTag);

        changeProcessor(htmlTag.getName(), data.getHtmlTemplate());

    }

    private void changeProcessor(String tagName, HtmlTemplate htmlTemplate) {

        if (tagName.equalsIgnoreCase("script")) {
            htmlTemplate.setProcessor(HtmlProcessors.SCRIPT);
        } else if (tagName.equalsIgnoreCase("style")) {
            htmlTemplate.setProcessor(HtmlProcessors.STYLE);
        }
    }

    private void processClosingTag(HtmlTag htmlTag, HtmlProcessorData data) {
        var template = data.getHtmlTemplate();

        var openingHtmlTag = template.peekTagStack()
                .orElseThrow();

        if (openingHtmlTag.isClosingTag(htmlTag)) {

            template.removeFromTagStack();

            htmlTag.processTag(data.getTemplateClass());
            openingHtmlTag.processClosingTag(data.getTemplateClass());


        } else {
            throw new IllegalStateException("Miss matched closing tag. Tags may not be closed in the right order " + htmlTag.getName());
        }

    }

    private void processSelfClosingTag(HtmlTag htmlTag, HtmlProcessorData data) {
        var templateClass = data.getTemplateClass();

        htmlTag.processOpeningTag(templateClass);
        htmlTag.processTag(templateClass);
        htmlTag.processClosingTag(templateClass);
    }

    private void processHtmlContent(HtmlProcessorData data) {
        new Content(data.getHtml(), data.getTemplateClass())
                .process();
    }
}
