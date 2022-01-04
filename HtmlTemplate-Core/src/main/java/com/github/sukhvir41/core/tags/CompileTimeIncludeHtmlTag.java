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

package com.github.sukhvir41.core.tags;

import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.statements.RenderBodyStatement;
import com.github.sukhvir41.core.template.CompileTimeSubTemplate;
import com.github.sukhvir41.core.template.Template;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CompileTimeIncludeHtmlTag extends IncludeHtmlTag {
    public CompileTimeIncludeHtmlTag(String htmlString, Function<String, String> codeParser, Template template) {
        super(htmlString, codeParser, template);
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        String filePath = getFilePath();
        Path file = getFile(filePath);

        Path rootFolder = getTemplate().getSettings().get(SettingOptions.ROOT_FOLDER)
                .orElseThrow(() -> new IllegalArgumentException("ROOT_FOLDER setting not present"))
                .normalize();

        if (!file.startsWith(rootFolder)) {
            throw new IllegalArgumentException("Template file is outside the root folder. \nTemplate file: " + file.normalize().toString() +
                    "\nRoot Folder: " + rootFolder.toString());
        }

        Template subTemplate = new CompileTimeSubTemplate(file, getTemplate());
        Optional<Template> processedTemplate = classGenerator.getTemplate(file);

        if (processedTemplate.isPresent()) {
            classGenerator.addStatement(getTemplate(), new CompileTimeIncludeHtmlTag.SubTemplateRenderInclude(classGenerator, processedTemplate.get(), getPassedVariables(), getHtmlString()));
        } else {
            classGenerator.addStatement(getTemplate(), new CompileTimeIncludeHtmlTag.SubTemplateRenderInclude(classGenerator, subTemplate, getPassedVariables(), getHtmlString()));
            subTemplate.readAndProcessTemplateFile();
        }

    }

    private Path getFile(String filePath) {
        if (Paths.get(filePath).isAbsolute()) {
            return Paths.get(filePath);
        } else {
            return getTemplate().getFile()
                    .toAbsolutePath()
                    .getParent()
                    .resolve(filePath)
                    .toAbsolutePath();
        }
    }


    private static final class SubTemplateRenderInclude implements RenderBodyStatement {

        private final TemplateClassGenerator classGenerator;
        private final Template subTemplate;
        private final Map<String, String> passedVariables;
        private final String html;

        public SubTemplateRenderInclude(TemplateClassGenerator classGenerator, Template subTemplate, Map<String, String> passedVariables, String html) {
            this.classGenerator = classGenerator;
            this.subTemplate = subTemplate;
            this.passedVariables = passedVariables;
            this.html = html;
        }

        @Override
        public String getStatement() {
            testPassedVariables();
            String variableCalls = classGenerator.getVariables(subTemplate)
                    .keySet()
                    .stream()
                    .map(name -> name + "(" + passedVariables.get(name) + ")")
                    .collect(Collectors.joining("."));


            return subTemplate.getFullyQualifiedName() +
                    ".getInstance()" +
                    (variableCalls.length() > 0 ? "." + variableCalls : "") +
                    ".render(" + classGenerator.getWriterVariableName() + ");";
        }


        /**
         * tests to see if the number of variables passed to subTemplate match the number of variables of that template
         */
        private void testPassedVariables() {
            if (passedVariables.keySet().size() != classGenerator.getVariables(subTemplate).size()) {
                throw new IllegalStateException("The number of arguments passed does not match number variables the template takes. \n" +
                        "Template " + subTemplate.getFile() + "\n" +
                        "expected arguments " + classGenerator.getVariables(subTemplate).keySet() + "\n" +
                        "passed arguments " + passedVariables.keySet() + "\n" +
                        "html -> " + html);
            }

            Set<String> variables = classGenerator.getVariables(subTemplate)
                    .keySet();

            for (String variable : variables) {
                if (!passedVariables.containsKey(variable)) {
                    throw new IllegalArgumentException("The template requires the argument " + variable + ".\n" +
                            "Template " + subTemplate.getFile() + "\n" +
                            "html ->" + html);
                }
            }
        }
    }
}
