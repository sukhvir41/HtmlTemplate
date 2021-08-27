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

package com.github.sukhvir41.tags;

import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.classgenerator.TemplateClassGeneratorOLD;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.utils.HtmlUtils;

public class RegularHtmlTag implements HtmlTag {

    protected String htmlString;
    protected final Template template;

    public RegularHtmlTag(String htmlString, Template instantiatingTemplate) {
        this.htmlString = htmlString;
        this.template = instantiatingTemplate;

    }

    @Override
    public String getName() {
        return HtmlUtils.getStartingHtmlTagName(this.htmlString);
    }

    @Override
    public boolean isClosingTag() {
        return this.htmlString.charAt(1) == '/';
    }


    @Override
    public boolean isSelfClosing() {
        return this.htmlString.charAt(this.htmlString.length() - 2) == '/';
    }

    @Override
    public boolean isDocTypeTag() {
        return HtmlUtils.isStartingTagDocType(this.htmlString);
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        classGenerator.appendPlainHtml(template, this.htmlString);
    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {
        // does nothing
    }


}
