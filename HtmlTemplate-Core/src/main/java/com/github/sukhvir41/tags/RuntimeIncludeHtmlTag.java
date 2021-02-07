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

import com.github.sukhvir41.core.RuntimeSubTemplate;
import com.github.sukhvir41.core.Template;
import com.github.sukhvir41.core.TemplateClassGenerator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RuntimeIncludeHtmlTag extends IncludeHtmlTag {

    public RuntimeIncludeHtmlTag(String htmlString, Template callingTemplate) {
        super(htmlString, callingTemplate);
    }


    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        String filePath = getFilePath();
        Path file = getFile(filePath);
        Template subTemplate = new RuntimeSubTemplate(file, getTemplate());
        classGenerator.addMappedVariables(subTemplate, getVariables(subTemplate, classGenerator));
        addMappedVariablesSetters(subTemplate, classGenerator);
        subTemplate.readAndProcessTemplateFile();
    }

    private Path getFile(String filePath) {
        if (Paths.get(filePath).isAbsolute()) {
            return Paths.get(filePath);
        } else {
            return getTemplate().getFile()
                    .getParent()
                    .resolve(filePath)
                    .toAbsolutePath();
        }
    }

    private void addMappedVariablesSetters(Template template, TemplateClassGenerator classGenerator) {
        getVariables(template, classGenerator)
                .forEach(variableInfo -> classGenerator.addCode(variableInfo.getMappedName() + "(" + variableInfo.getValue() + ");"));

    }


}
