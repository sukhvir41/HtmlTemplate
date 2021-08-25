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
import org.apache.commons.lang3.NotImplementedException;

import java.nio.file.Path;

public class CompileTimeSubTemplate extends Template {

    private final Template parentTemplate;

    public CompileTimeSubTemplate(Path file, Template parentTemplate) {
        super(file, parentTemplate.getClassGenerator(), parentTemplate.getDepth() + 1, parentTemplate.getSettings());
        this.parentTemplate = parentTemplate;
        if (getDepth() > 99) {
            throw new IllegalStateException("Reached Template depth");
        }

        validateFileLocation();

    }

    public void validateFileLocation() {
        Path rootFolder = getSettings().get(SettingOptions.ROOT_FOLDER)
                .orElseThrow()
                .normalize();

        if (!getFile().startsWith(rootFolder)) {
            throw new IllegalArgumentException("Template file is outside the root folder. \nTemplate file: " + getFile().normalize().toString() +
                    "\nRoot Folder: " + rootFolder.toString());
        }

    }


    @Override
    public String getFullyQualifiedName() {
        throw new NotImplementedException("don't what the name should be for a compile time sub template");
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
