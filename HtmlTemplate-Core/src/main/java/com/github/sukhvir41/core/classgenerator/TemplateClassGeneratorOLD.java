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
import com.github.sukhvir41.core.statements.PlainStringRenderBodyStatement;
import com.github.sukhvir41.core.statements.RenderBodyStatement;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.tags.HtmlTag;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;

import static com.github.sukhvir41.utils.StringUtils.getIndentations;

public abstract class TemplateClassGeneratorOLD {

    protected static final String PLAIN_HTML_VARIABLE_PREFIX = "PLAIN_HTML_";
    protected static final String SUPER_CLASS = "com.github.sukhvir41.template.HtTemplate";
    protected final static String BREAK_LINE = "\n";


    private final String packageName;
    private final String className;

    private final String writerVariableName;

    private final Deque<HtmlTag> tagStack = new ArrayDeque<>();

    // classes to be imported
    private final Set<String> imports = new HashSet<>();

    // plainHTML variables name, value
    private final Map<Integer, StringBuilder> plainHtmlVariables = new TreeMap<>();

    // variables name, type
    private final Map<String, String> variables = new HashMap<>();

    //private final StringBuilder renderFunctionBody = new StringBuilder();
    private final Deque<RenderBodyStatement> renderFunctionBody = new ArrayDeque<>();


    private int renderFunctionIndentation = 2;

    private int variableCount = 0;

    public TemplateClassGeneratorOLD(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;

        this.writerVariableName = "writer" + ((int) (Math.random() * 10000));
        this.addImportStatement("java.io.Writer");
        this.addImportStatement("java.io.IOException");
    }

    //returns the current variable value builder
    private StringBuilder getPlainHtmlValueBuilder() {
        return plainHtmlVariables.computeIfAbsent(this.variableCount, this::computeAbsentPlainHtmlVariable);
    }

    private StringBuilder computeAbsentPlainHtmlVariable(int variableName) {

        renderFunctionBody.add(new PlainStringRenderBodyStatement(
                new StringBuilder()
                        .append(getIndentations(renderFunctionIndentation))
                        .append(getWriterVariableName())
                        .append(".append(")
                        .append(PLAIN_HTML_VARIABLE_PREFIX)
                        .append(variableName)
                        .append(");")
                        .append(BREAK_LINE)
                        .toString()
        ));

        return new StringBuilder();
    }

    protected String getPackageName() {
        return packageName;
    }

    protected String getClassName() {
        return className;
    }

    protected Set<String> getImports() {
        return this.imports;
    }

    protected Map<Integer, StringBuilder> getPlainHtmlVariables() {
        return  plainHtmlVariables;
    }

    protected Map<String, String> getVariables() {
        return variables;
    }

    protected StringBuilder getRenderFunctionBody() {
        return renderFunctionBody.stream()
                .map(RenderBodyStatement::getStatement)
                .reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append);
    }

    public void addImportStatement(String importPath) {
        imports.add(importPath);
    }

    public void addTagToStack(HtmlTag htmlTag) {
        this.tagStack.push(htmlTag);
    }

    public Optional<HtmlTag> removeFromTagStack() {
        return Optional.ofNullable(tagStack.poll());
    }

    public void appendPlainHtml(String html) {
        appendPlainHtml(html, true, true);
    }

    public void appendPlainHtml(String html, boolean appendIndentation, boolean appendNewLine) {

        if (StringUtils.isNotBlank(html)) {
            if (appendIndentation) {
                getPlainHtmlValueBuilder()
                        .append(getIndentations(this.tagStack.size()));
            }

            getPlainHtmlValueBuilder()
                    .append(StringEscapeUtils.escapeJava(html));

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
                .append(getIndentations(this.tagStack.size()));
    }

    public void addCode(RenderBodyStatement renderBodyStatement) {

        this.renderFunctionBody.add(new JavaCodeRenderBodyStatement(this, renderBodyStatement));

        if (plainHtmlVariables.containsKey(variableCount)) {
            ++this.variableCount;
        }

    }

    public void addVariable(String type, String name) {
        if (StringUtils.isNoneBlank(name, type)) {
            this.variables.put(name.trim(), type.trim());
        }
    }

    public String getWriterVariableName() {
        return this.writerVariableName;
    }

    public void incrementRenderFunctionIndentation() {
        ++this.renderFunctionIndentation;
    }

    public void decrementRenderFunctionIndentation() {
        --this.renderFunctionIndentation;
    }


    public Map<String, String> getVariableMappings(Template template) {
        return getMappedVariables()
                .getOrDefault(template, Collections.emptyList())
                .stream()
                .reduce(new HashMap<>(),
                        (map, variableInfo) -> {
                            map.put(variableInfo.getName(), variableInfo.getMappedName());
                            return map;
                        },
                        (map1, map2) -> {
                            map1.putAll(map2);
                            return map1;
                        });
    }

    //abstract methods

    public abstract String render();

    public abstract void addMappedVariables(Template template, List<VariableInfo> variables);

    public abstract Map<Template, List<VariableInfo>> getMappedVariables();

    public abstract void addSubTemplateVariables(Template template, String type, String name);

    protected static class JavaCodeRenderBodyStatement implements RenderBodyStatement {

        private final TemplateClassGeneratorOLD classGenerator;
        private final RenderBodyStatement renderBodyStatement;
        private final String indentation;


        public JavaCodeRenderBodyStatement(TemplateClassGeneratorOLD classGenerator, RenderBodyStatement renderBodyStatement) {
            this.classGenerator = classGenerator;
            this.renderBodyStatement = renderBodyStatement;
            this.indentation = getIndentations(classGenerator.renderFunctionIndentation);
        }


        @Override
        public String getStatement() {
            return indentation +
                    renderBodyStatement.getStatement() +
                    BREAK_LINE;
        }
    }
}
