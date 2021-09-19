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

package com.github.sukhvir41.core.template;

import com.github.sukhvir41.core.classgenerator.RuntimeClassGenerator;
import com.github.sukhvir41.core.settings.Settings;
import com.github.sukhvir41.parsers.Code;
import com.github.sukhvir41.tags.*;
import com.github.sukhvir41.utils.HtmlUtils;
import com.github.sukhvir41.utils.StringUtils;

import java.nio.file.Path;
import java.util.UUID;

public final class RuntimeTemplate extends Template {

    private static final String DEFAULT_PACKAGE_NAME = "com.github.sukhvir41.runtimeTemplates";

    private final String packageName;
    private final String className;

    public RuntimeTemplate(Path file, Settings settings) {
        this(
                file,
                DEFAULT_PACKAGE_NAME,
                StringUtils.getClassNameFromFile(file) + "_" + UUID.randomUUID().toString().replace("-", "_"),
                settings
        );
    }

    public RuntimeTemplate(Path file, String packageName, Settings settings) {
        this(
                file,
                packageName,
                StringUtils.getClassNameFromFile(file) + "_" + UUID.randomUUID().toString().replace("-", "_"),
                settings
        );
    }

    private RuntimeTemplate(Path file, String packageName, String className, Settings settings) {
        super(file,
                new RuntimeClassGenerator(DEFAULT_PACKAGE_NAME, className),
                settings
        );
        this.packageName = packageName;
        this.className = className;
    }


    @Override
    public String getFullyQualifiedName() {
        return this.packageName + "." + this.className;
    }

    @Override
    public HtmlTag parseSection(String section) {
        if (HtmlUtils.isHtmlTagAtStart(section)) {
            if (containsDynamicAttribute(section)) {
                return parseDynamicHtml(section);
            } else {
                return new RegularHtmlTag(section, this);
            }
        } else {
            return new Content(section, this, Code::parseForFunction);
        }
    }

    @Override
    public Template getRootTemplate() {
        return this;
    }

    private boolean containsDynamicAttribute(String htmlString) {
        return HtmlUtils.DYNAMIC_ATTRIBUTE.matcher(htmlString)
                .find() || ElseHtmlTag.matches(htmlString); // else html tag here as it does not match the dynamic attribute pattern which is the only exception.
    }

    private HtmlTag parseDynamicHtml(String tagString) {
        if (MetaImportHtmlTag.matches(tagString)) {
            return new MetaImportHtmlTag(tagString);

        } else if (RuntimeIncludeHtmlTag.matches(tagString)) {
            return new RuntimeIncludeHtmlTag(tagString, Code::parseForFunction, this);

        } else if (MetaVariableHtmlTag.matches(tagString)) {
            return new MetaVariableHtmlTag(tagString, this);

        } else if (IfHtmlTag.matches(tagString)) {
            return new IfHtmlTag(tagString, this, Code::parseForFunction);

        } else if (ElseIfHtmlTag.matches(tagString)) {
            return new ElseIfHtmlTag(tagString, this, Code::parseForFunction);

        } else if (ElseHtmlTag.matches(tagString)) { // else tag check should be after else if check
            return new ElseHtmlTag(tagString, this, Code::parseForFunction);

        } else if (ForHtmlTag.matches(tagString)) {
            return new ForHtmlTag(tagString, this, Code::parseForFunction);

        } else {
            return new DynamicAttributeHtmlTag(tagString, this, Code::parseForFunction);
        }

    }
}
