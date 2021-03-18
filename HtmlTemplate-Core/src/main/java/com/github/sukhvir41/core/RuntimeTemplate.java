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

package com.github.sukhvir41.core;

import com.github.sukhvir41.tags.*;
import com.github.sukhvir41.utils.HtmlUtils;
import com.github.sukhvir41.utils.StringUtils;

import java.nio.file.Path;

public final class RuntimeTemplate extends Template {

    private static final String PACKAGE_NAME = "com.github.sukhvir41.runtimeTemplates";

    public RuntimeTemplate(Path file) {
        super(file,
                PACKAGE_NAME,
                StringUtils.getClassNameFromFile(file),
                new RuntimeTemplateClassGenerator(PACKAGE_NAME, StringUtils.getClassNameFromFile(file)));
    }

    public RuntimeTemplate(Path file, String packageName) {
        super(file,
                packageName,
                StringUtils.getClassNameFromFile(file),
                new RuntimeTemplateClassGenerator(packageName, StringUtils.getClassNameFromFile(file)));
    }


    @Override
    public HtmlTag parseSection(String section) {
        if (HtmlUtils.isHtmlTagAtStart(section)) {
            if (containsDynamicAttribute(section)) {
                return parseDynamicHtml(section);
            } else {
                return new RegularHtmlTag(section);
            }
        } else {
            return new Content(section, this);
        }
    }

    private boolean containsDynamicAttribute(String htmlString) {
        return HtmlUtils.DYNAMIC_ATTRIBUTE.matcher(htmlString)
                .find() || ElseHtmlTag.matches(htmlString); // else html tag here as it does not match the dynamic attribute pattern which is the only exception.
    }

    private HtmlTag parseDynamicHtml(String tagString) {
        if (MetaImportHtmlTag.matches(tagString)) {
            return new MetaImportHtmlTag(tagString);

        } else if (RuntimeIncludeHtmlTag.matches(tagString)) {
            return new RuntimeIncludeHtmlTag(tagString, this);

        } else if (MetaVariableHtmlTag.matches(tagString)) {
            return new MetaVariableHtmlTag(tagString);

        } else if (IfHtmlTag.matches(tagString)) {
            return new IfHtmlTag(tagString);

        } else if (ElseIfHtmlTag.matches(tagString)) {
            return new ElseIfHtmlTag(tagString);

        } else if (ElseHtmlTag.matches(tagString)) { // else tag check should be after else if check
            return new ElseHtmlTag(tagString);

        } else if (ForHtmlTag.matches(tagString)) {
            return new ForHtmlTag(tagString);

        } else {
            return new DynamicAttributeHtmlTag(tagString);
        }

    }
}
