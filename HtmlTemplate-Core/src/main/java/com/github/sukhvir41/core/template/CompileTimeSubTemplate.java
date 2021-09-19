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

import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.tags.HtmlTag;
import com.github.sukhvir41.utils.StringUtils;
import org.apache.commons.lang3.NotImplementedException;

import java.io.File;
import java.nio.file.Path;

import static org.apache.commons.lang3.StringUtils.removeStart;
import static org.apache.commons.lang3.StringUtils.replace;

public class CompileTimeSubTemplate extends Template {

    private final Template parentTemplate;
    private final String fullyQualified;

    public CompileTimeSubTemplate(Path file, Template parentTemplate) {
        super(file, parentTemplate.getClassGenerator(), parentTemplate.getDepth() + 1, parentTemplate.getSettings());
        this.parentTemplate = parentTemplate;
        this.fullyQualified = computeFullyQualifiedName();
    }

    private String computeFullyQualifiedName() {
        String packageName = getSettings().get(SettingOptions.PACKAGE_NAME)
                .orElseThrow(() -> new IllegalStateException("Please provide PACKAGE_NAME setting"));

        String className = StringUtils.getClassNameFromFile(getFile());

        String rootFolder = getSettings().get(SettingOptions.ROOT_FOLDER)
                .orElseThrow(() -> new IllegalStateException("Please provide ROOT_FOLDER setting"))
                .normalize()
                .toAbsolutePath()
                .toString();

        String templateFilePath = getFile().normalize()
                .toAbsolutePath()
                .toString();

        String intermediatePath = removeStart(templateFilePath, rootFolder);
        intermediatePath = replace(intermediatePath, File.separator, ".");

        return packageName + "." + intermediatePath + "." + className;

    }


    @Override
    public String getFullyQualifiedName() {
        return this.fullyQualified;
    }

    @Override
    public HtmlTag parseSection(String section) {
        return null;
    }

    @Override
    public Template getRootTemplate() {
        return parentTemplate.getRootTemplate();
    }
}
