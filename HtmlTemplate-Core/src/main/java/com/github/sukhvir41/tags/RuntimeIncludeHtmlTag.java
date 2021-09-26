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
import com.github.sukhvir41.core.statements.RenderBodyStatement;
import com.github.sukhvir41.core.template.RuntimeSubTemplate;
import com.github.sukhvir41.core.template.Template;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RuntimeIncludeHtmlTag extends IncludeHtmlTag {

    public RuntimeIncludeHtmlTag(String htmlString, Function<String, String> codeParser, Template callingTemplate) {
        super(htmlString, codeParser, callingTemplate);
    }


    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        String filePath = getFilePath();
        Path file = getFile(filePath);

        getTemplate()
                .getRootTemplate()
                .setDepth(getTemplate().getDepth() + 1);
        if (getTemplate().getRootTemplate().getDepth() > 99) {
            throw new IllegalStateException("Reached Template depth");
        }

        Template subTemplate = new RuntimeSubTemplate(file, getTemplate());
        Optional<Template> processedTemplate = classGenerator.getTemplate(file);

        if (processedTemplate.isPresent() && subTemplate.equals(processedTemplate.get())) {
            classGenerator.addStatement(getTemplate(), new IncludeRenderBodyStatement(classGenerator, processedTemplate.get(), getPassedVariables(), getHtmlString()));
        } else {
            classGenerator.addStatement(getTemplate(), new IncludeRenderBodyStatement(classGenerator, subTemplate, getPassedVariables(), getHtmlString()));
            subTemplate.readAndProcessTemplateFile();
            classGenerator.incrementPlainHtmlVariableCount();
        }

        getTemplate()
                .getRootTemplate()
                .setDepth(getTemplate().getRootTemplate().getDepth() - 1);
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

    private static class IncludeRenderBodyStatement implements RenderBodyStatement {

        private final TemplateClassGenerator classGenerator;
        private final Template subTemplate;
        private final Map<String, String> passedVariables;
        private final String html;


        public IncludeRenderBodyStatement(TemplateClassGenerator classGenerator, Template subTemplate, Map<String, String> passedVariables, String html) {
            this.classGenerator = classGenerator;
            this.subTemplate = subTemplate;
            this.passedVariables = passedVariables;
            this.html = html;
        }

        @Override
        public String getStatement() {
            testPassedVariables();

            String variablesParams = classGenerator.getVariables(subTemplate)
                    .keySet()
                    .stream()
                    .map(passedVariables::get)
                    .collect(Collectors.joining(","));

            return subTemplate.getFullyQualifiedName() +
                    "(" +
                    classGenerator.getWriterVariableName() +
                    (variablesParams.length() > 0 ? "," + variablesParams : "") +
                    ");";
        }

        /**
         * tests to see if the number of variables passed to subTemplate match the number of variables of that template
         */
        private void testPassedVariables() {
            if (passedVariables.keySet().size() != classGenerator.getVariables(subTemplate).size()) {
                throw new IllegalStateException("The number of arguments passed does not match number variables the template takes. \n" +
                        "Template " + subTemplate.getFile() + "\n" +
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
