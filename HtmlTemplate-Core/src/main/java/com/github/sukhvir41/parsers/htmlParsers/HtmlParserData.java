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
import com.github.sukhvir41.core.TemplateClassGenerator;

public final class HtmlParserData {

    private final String section;
    private final TemplateClassGenerator classGenerator;
    private final Template template;


    private HtmlParserData(String section,
                           TemplateClassGenerator classGenerator,
                           Template template) {
        this.section = section;
        this.classGenerator = classGenerator;
        this.template = template;
    }


    public String getSection() {
        return section;
    }

    public TemplateClassGenerator getClassGenerator() {
        return classGenerator;
    }

    public Template getTemplate() {
        return this.template;
    }


    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String section;
        private TemplateClassGenerator classGenerator;
        private Template template;


        public Builder setSection(String section) {
            this.section = section;
            return this;
        }

        public Builder setClassGenerator(TemplateClassGenerator classGenerator) {
            this.classGenerator = classGenerator;
            return this;
        }

        public Builder setTemplate(Template template) {
            this.template = template;
            return this;
        }

        public HtmlParserData build() {
            return new HtmlParserData(section, classGenerator, template);
        }
    }

}
