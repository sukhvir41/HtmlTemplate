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

package com.github.sukhvir41.core.classgenerator;

import com.github.sukhvir41.core.VariableInfo;
import com.github.sukhvir41.core.template.Template;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

import static com.github.sukhvir41.utils.StringUtils.getIndentations;

public class RuntimeTemplateClassGeneratorOLD extends TemplateClassGeneratorOLD {

    // key -> template being included using ht-include tag
    private final Map<Template, List<VariableInfo>> mappedVariables = new HashMap<>();


    public RuntimeTemplateClassGeneratorOLD(String packageName, String className) {
        super(packageName, className);
    }

    @Override
    public void addMappedVariables(Template template, List<VariableInfo> variables) {
        this.mappedVariables.put(template, variables);
    }

    @Override
    public Map<Template, List<VariableInfo>> getMappedVariables() {
        return this.mappedVariables;
    }

    @Override
    public void addSubTemplateVariables(Template template, String type, String name) {
        if (StringUtils.isAnyBlank(type, name) || template == null) {
            throw new IllegalArgumentException();
        }

        getMappedVariables()
                .getOrDefault(template, Collections.emptyList())
                .stream()
                .filter(variableInfo -> variableInfo.getName().equals(name))
                .findFirst()
                .ifPresentOrElse(variableInfo -> variableInfo.setType(type),
                        () -> super.addVariable(type, name));
    }

    @Override
    public String render() {
        StringBuilder classString = new StringBuilder();

        appendPackageName(classString);
        appendImports(classString);

        appendOpeningClass(classString);

        //static
        appendPlainHtmlVariables(classString);
        appendGetInstanceMethod(classString);

        //instance
        appendVariableDeclaration(classString);
        appendMappedVariableDeclaration(classString);

        appendGetter(classString);
        appendSetter(classString);

        appendMappedVariableGetter(classString);
        appendMappedVariableSetter(classString);

        appendRenderImpl(classString);

        appendClosingClass(classString);

        return classString.toString();
    }

    private void appendPackageName(StringBuilder classString) {
        if (StringUtils.isNotBlank(getPackageName())) {
            classString.append("package ")
                    .append(getPackageName())
                    .append(";")
                    .append(BREAK_LINE)
                    .append(BREAK_LINE);
        }
    }

    private void appendImports(StringBuilder classString) {
        getImports()
                .stream()
                .map(importPackage -> "import " + importPackage + ";" + BREAK_LINE)
                .forEach(classString::append);

        classString.append(BREAK_LINE)
                .append(BREAK_LINE);

    }

    private void appendOpeningClass(StringBuilder classString) {
        classString.append("public class ")
                .append(getClassName())
                .append(" extends ")
                .append(SUPER_CLASS)
                .append(" {")
                .append(BREAK_LINE)
                .append(BREAK_LINE);
    }

    private void appendPlainHtmlVariables(StringBuilder classString) {
        getPlainHtmlVariables()
                .forEach(
                        (name, value) -> {
                            classString.append(getIndentations(1))
                                    .append("private static final String ")
                                    .append(PLAIN_HTML_VARIABLE_PREFIX)
                                    .append(name)
                                    .append(" = \"")
                                    .append(value)
                                    .append("\";")
                                    .append(BREAK_LINE);
                        }
                );
        classString.append(BREAK_LINE);
    }

    private void appendGetInstanceMethod(StringBuilder classString) {
        classString.append(getIndentations(1))
                .append("public static final ")
                .append(getClassName())
                .append(" getInstance() {")
                .append(BREAK_LINE)
                .append(getIndentations(2))
                .append("return new ")
                .append(getClassName())
                .append("();")
                .append(BREAK_LINE)
                .append(getIndentations(1))
                .append("}")
                .append(BREAK_LINE)
                .append(BREAK_LINE);
    }

    private void appendVariableDeclaration(StringBuilder classString) {
        getVariables()
                .forEach((name, type) ->
                        classString.append(getIndentations(1))
                                .append("private ")
                                .append(covertFromPrimitiveToObjectType(type))
                                .append(" ")
                                .append(name)
                                .append(";")
                                .append(BREAK_LINE)
                );
        classString.append(BREAK_LINE);
    }

    private void appendMappedVariableDeclaration(StringBuilder classString) {
        getMappedVariables()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .forEach(variableInfo ->
                        classString.append(getIndentations(1))
                                .append("private ")
                                .append(covertFromPrimitiveToObjectType(variableInfo.getType()))
                                .append(" ")
                                .append(variableInfo.getMappedName())
                                .append(";")
                                .append(BREAK_LINE)
                );
        classString.append(BREAK_LINE);
    }

    private void appendGetter(StringBuilder classString) {
        getVariables()
                .forEach((name, type) ->
                        classString.append(getIndentations(1))
                                .append("private ")
                                .append(covertFromPrimitiveToObjectType(type))
                                .append(" ")
                                .append(name)
                                .append("() {")
                                .append(BREAK_LINE)
                                .append(getIndentations(2))
                                .append("return ")
                                .append(name)
                                .append(";")
                                .append(BREAK_LINE)
                                .append(getIndentations(1))
                                .append("}")
                                .append(BREAK_LINE)
                                .append(BREAK_LINE)
                );

    }

    private void appendSetter(StringBuilder classString) {
        getVariables()
                .forEach((name, type) ->
                        classString.append(getIndentations(1))
                                .append("public ")
                                .append(getClassName())
                                .append(" ")
                                .append(name)
                                .append("(")
                                .append(covertFromPrimitiveToObjectType(type))
                                .append(" ")
                                .append(name)
                                .append(") {")
                                .append(BREAK_LINE)
                                .append(getIndentations(2))
                                .append("this.")
                                .append(name)
                                .append(" = ")
                                .append(name)
                                .append(";")
                                .append(BREAK_LINE)
                                .append(getIndentations(2))
                                .append("return this;")
                                .append(BREAK_LINE)
                                .append(getIndentations(1))
                                .append("}")
                                .append(BREAK_LINE)
                                .append(BREAK_LINE)
                );

    }

    private void appendMappedVariableGetter(StringBuilder classString) {
        getMappedVariables()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .forEach(variableInfo ->
                        classString.append(getIndentations(1))
                                .append("private ")
                                .append(covertFromPrimitiveToObjectType(variableInfo.getType()))
                                .append(" ")
                                .append(variableInfo.getMappedName())
                                .append("() {")
                                .append(BREAK_LINE)
                                .append(getIndentations(2))
                                .append("return ")
                                .append(variableInfo.getMappedName())
                                .append(";")
                                .append(BREAK_LINE)
                                .append(getIndentations(1))
                                .append("}")
                                .append(BREAK_LINE)
                                .append(BREAK_LINE)
                );
    }

    private void appendMappedVariableSetter(StringBuilder classString) {
        getMappedVariables()
                .values()
                .stream()
                .flatMap(Collection::stream)
                .forEach(variableInfo ->
                        classString.append(getIndentations(1))
                                .append("private void")
                                .append(" ")
                                .append(variableInfo.getMappedName())
                                .append("(")
                                .append(variableInfo.getType())
                                .append(" ")
                                .append(variableInfo.getMappedName())
                                .append(") {")
                                .append(BREAK_LINE)
                                .append(getIndentations(2))
                                .append("this.")
                                .append(variableInfo.getMappedName())
                                .append(" = ")
                                .append(variableInfo.getMappedName())
                                .append(";")
                                .append(BREAK_LINE)
                                .append(getIndentations(1))
                                .append("}")
                                .append(BREAK_LINE)
                                .append(BREAK_LINE)
                );
    }

    private void appendRenderImpl(StringBuilder classString) {
        classString.append(getIndentations(1))
                .append("@Override")
                .append(BREAK_LINE)
                .append(getIndentations(1))
                .append("public void renderImpl(Writer ")
                .append(getWriterVariableName())
                .append(") throws IOException {")
                .append(BREAK_LINE)
                .append(getRenderFunctionBody())
                .append(BREAK_LINE)
                .append(getIndentations(1))
                .append("}")
                .append(BREAK_LINE)
                .append(BREAK_LINE);


    }

    private void appendClosingClass(StringBuilder classString) {
        classString.append("}");
    }

    private String covertFromPrimitiveToObjectType(String type) {
        switch (type) {
            case "byte":
                return "Byte";
            case "short":
                return "Short";
            case "int":
                return "Integer";
            case "long":
                return "Long";
            case "float":
                return "Float";
            case "double":
                return "Double";
            case "char":
                return "Character";
            case "boolean":
                return "Boolean";
            default:
                return type;
        }
    }
}
