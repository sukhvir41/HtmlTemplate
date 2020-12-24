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

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;

public final class ClassGenerator {

    private static final String PLAIN_HTML_VARIABLE_PREFIX = "PLAIN_HTML_";
    private static final String SUPER_CLASS = "com.github.sukhvir41.template.HtTemplate";
    private final static String BREAK_LINE = "\n";

    private final TemplateGenerator template;
    private final String className;
    private String packageName;

    // classes to be imported
    private final Set<String> imports = new HashSet<>();
    // plainHTML variables name, value
    private final Map<Integer, StringBuilder> plainHtmlVariables = new TreeMap<>();
    // variables name, type
    private final Map<String, String> variablesTypes = new HashMap<>();

    private final StringBuilder renderFunctionBody = new StringBuilder();

    private int renderFunctionIndentation = 2;

    private int variableCount = 0;


    public ClassGenerator(String packageName, String className, TemplateGenerator template) {
        this.className = className;
        this.packageName = packageName;
        this.template = template;
        this.addImportStatement("java.io.Writer");
        this.addImportStatement("java.io.IOException");
    }

    public ClassGenerator(String className, TemplateGenerator template) {
        this("", className, template);
    }

    public void appendPlainHtml(String html) {
        appendPlainHtml(html, true, true);
    }

    public void appendPlainHtml(String html, boolean appendIndentation, boolean appendNewLine) {

        if (StringUtils.isNotBlank(html)) {
            if (appendIndentation) {
                getPlainHtmlValueBuilder()
                        .append(getIndentation());
            }

            getPlainHtmlValueBuilder()
                    .append(encodeForJava(html));

            if (appendNewLine) {
                getPlainHtmlValueBuilder()
                        .append("\\n");
            }
        }
    }

    public void appendPlainHtmlNewLine() {
        getPlainHtmlValueBuilder()
                .append("\\n");
    }

    public void appendPlainHtmlIndentation() {
        getPlainHtmlValueBuilder()
                .append(getIndentation());
    }

    //returns the current variable value builder
    private StringBuilder getPlainHtmlValueBuilder() {
        return plainHtmlVariables.computeIfAbsent(getCurrentVariable(), this::computeAbsentPlainHtmlVariable);
    }

    private StringBuilder computeAbsentPlainHtmlVariable(int variableName) {

        this.renderFunctionBody
                .append(getIndentations(renderFunctionIndentation))
                .append("writer.append(").append(PLAIN_HTML_VARIABLE_PREFIX).append(variableName).append(");")
                .append(BREAK_LINE);

        return new StringBuilder();
    }


    public void addCode(String code) {
        this.renderFunctionBody
                .append(getIndentations(renderFunctionIndentation))
                .append(code)
                .append(BREAK_LINE);

        if (plainHtmlVariables.containsKey(variableCount)) {
            ++this.variableCount;
        }

    }

    private int getCurrentVariable() {
        return variableCount;
    }


    public void incrementFunctionIndentation() {
        ++this.renderFunctionIndentation;
    }

    public void decrementFunctionIndentation() {
        --this.renderFunctionIndentation;
    }


    public void addVariable(String type, String name) {
        if (StringUtils.isNoneBlank(name, type)) {
            this.variablesTypes.put(name.trim(), type.trim());
        }
    }

    public String generateClass() {
        return generateClassImpl();
    }


    private void addPackage(StringBuilder classString) {
        if (StringUtils.isNotBlank(packageName)) {
            classString.append("package ")
                    .append(packageName)
                    .append(";")
                    .append(BREAK_LINE)
                    .append(BREAK_LINE);
        }
    }

    public String generateReflectionClass() {
        this.packageName = "";
        return generateClassImpl();
    }


    private String generateClassImpl() {
        var theClass = new StringBuilder();

        addPackage(theClass);
        addImports(theClass);

        theClass.append("public class ")
                .append(className)
                .append(" extends ")
                .append(SUPER_CLASS)
                .append(" {")
                .append(BREAK_LINE);

        addPlainHtmlVariables(theClass);
        addConstructor(theClass);
        addVariablesToClass(theClass);
        addRenderFunction(theClass);
        addInstanceMethod(theClass);

        theClass.append("}");

        return theClass.toString();
    }


    private void addImports(StringBuilder classString) {
        imports.forEach(theImport -> classString.append(theImport).append(BREAK_LINE));
        classString.append(BREAK_LINE);
    }


    private void addPlainHtmlVariables(StringBuilder classString) {
        this.plainHtmlVariables
                .forEach(
                        (name, value) -> {
                            classString.append(getIndentations(1))
                                    .append(createPublicFinalString(name, value))
                                    .append(BREAK_LINE);
                        }
                );
        classString.append(BREAK_LINE);
    }

    private String createPublicFinalString(int name, StringBuilder value) {
        return "private static final String " + PLAIN_HTML_VARIABLE_PREFIX + name + " = \"" + value.toString() + "\";";
    }

    private void addVariablesToClass(StringBuilder theClass) {

        this.variablesTypes
                .forEach((name, type) -> generateVariablesGetterAndSetter(name, type, theClass));

    }

    private void generateVariablesGetterAndSetter(String name, String type, StringBuilder theClass) {
        generateVariables(name, type, theClass);
        theClass.append(BREAK_LINE);

        generateGetter(name, type, theClass);
        generateSetter(name, type, theClass);

    }


    private void generateVariables(String name, String type, StringBuilder theClass) {
        theClass.append(getIndentations(1))
                .append("private ")
                .append(type)
                .append(" ")
                .append(name)
                .append(";")
                .append(BREAK_LINE);
    }

    private void generateGetter(String name, String type, StringBuilder theClass) {

        theClass.append(getIndentations(1))
                .append("public ")
                .append(type)
                .append(" ")
                .append(name)
                .append("() {")
                .append(BREAK_LINE)
                .append(getIndentations(2))
                .append("return this.")
                .append(name)
                .append(";")
                .append(BREAK_LINE)
                .append(getIndentations(1))
                .append("}")
                .append(BREAK_LINE)
                .append(BREAK_LINE);

    }

    private void generateSetter(String name, String type, StringBuilder theClass) {

        theClass.append(getIndentations(1))
                .append("public ")
                .append(className)
                .append(" ")
                .append(name)
                .append("(")
                .append(type)
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
                .append(BREAK_LINE);
    }

    private void addConstructor(StringBuilder classString) {
        classString.append(getIndentations(1))
                .append("private ")
                .append(className)
                .append(" () {}")
                .append(BREAK_LINE)
                .append(BREAK_LINE);
    }


    private void addRenderFunction(StringBuilder theClass) {
        theClass.append(getIndentations(1))
                .append("@Override")
                .append(BREAK_LINE)
                .append(getIndentations(1))
                .append("public void render(Writer writer) throws IOException {")
                .append(BREAK_LINE)
                .append(renderFunctionBody.toString())
                .append(BREAK_LINE)
                .append(getIndentations(1))
                .append("}")
                .append(BREAK_LINE)
                .append(BREAK_LINE);

    }

    private void addInstanceMethod(StringBuilder theClass) {
        theClass.append(getIndentations(1))
                .append("public static ")
                .append(className)
                .append(" getInstance() {")
                .append(BREAK_LINE)
                .append(getIndentations(2))
                .append("return new ")
                .append(className)
                .append("();")
                .append(BREAK_LINE)
                .append(getIndentations(1))
                .append("}")
                .append(BREAK_LINE);
    }

    public void addImportStatement(String importString) {
        imports.add("import " + importString + ";");
    }

    private String getIndentation() {
        return getIndentations(template.getTagsStackSize());
    }

    private String getIndentations(int count) {

        var builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            builder.append("\t");
        }

        return builder.toString();
    }


    public static String encodeForJava(String string) {
        return StringEscapeUtils.escapeJava(string);
    }

}
