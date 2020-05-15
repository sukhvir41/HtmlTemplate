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

import com.github.sukhvir41.template.HtmlTemplate;
import com.github.sukhvir41.template.TemplateClass;

public final class HtmlProcessorData {

    private final String html;
    private final TemplateClass templateClass;
    private final HtmlTemplate htmlTemplate;


    private HtmlProcessorData(String html, TemplateClass templateClass, HtmlTemplate htmlTemplate) {
        this.html = html;
        this.templateClass = templateClass;
        this.htmlTemplate = htmlTemplate;
    }


    public String getHtml() {
        return html;
    }

    public TemplateClass getTemplateClass() {
        return templateClass;
    }

    public HtmlTemplate getHtmlTemplate() {
        return htmlTemplate;
    }


    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String html;
        private TemplateClass templateClass;
        private HtmlTemplate htmlTemplate;


        public Builder setHtml(String html) {
            this.html = html;
            return this;
        }

        public Builder setTemplateClass(TemplateClass templateClass) {
            this.templateClass = templateClass;
            return this;
        }

        public Builder setHtmlTemplate(HtmlTemplate htmlTemplate) {
            this.htmlTemplate = htmlTemplate;
            return this;
        }

        public HtmlProcessorData build() {
            return new HtmlProcessorData(html, templateClass, htmlTemplate);
        }
    }

}
