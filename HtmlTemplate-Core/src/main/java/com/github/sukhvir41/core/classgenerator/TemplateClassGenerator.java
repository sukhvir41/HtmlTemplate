package com.github.sukhvir41.core.classgenerator;

import com.github.sukhvir41.core.statements.PlainStringRenderBodyStatement;
import com.github.sukhvir41.core.statements.RenderBodyStatement;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.tags.HtmlTag;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.nio.file.Path;
import java.util.*;

import static com.github.sukhvir41.utils.StringUtils.getIndentations;

public abstract class TemplateClassGenerator {

    protected static final String PLAIN_HTML_VARIABLE_PREFIX = "PLAIN_HTML_";
    protected static final String SUPER_CLASS = "com.github.sukhvir41.template.HtTemplate";
    protected final static String BREAK_LINE = "\n";

    private final String packageName;
    private final String className;
    private final String writerVariableName;

    // classes to import
    private final Set<String> imports = new HashSet<>();

    //contains html tag that need to be closed
    private final Deque<HtmlTag> tagStack = new ArrayDeque<>();

    // plainHTML variables name, value
    private final Map<Integer, StringBuilder> plainHtmlVariables = new TreeMap<>();
    private int plainHtmlVariableCount = 0;

    private final Map<Template, TemplateData> templateDataMap = new HashMap<>();

    public TemplateClassGenerator(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
        this.writerVariableName = "writer" + ((int) (Math.random() * 10000));

        this.addImport("java.io.Writer");
        this.addImport("java.io.IOException");
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    /**
     * @return returns the writer variable name
     */
    public String getWriterVariableName() {
        return writerVariableName;
    }

    /**
     * returns an unmodifiable set of imports.
     * the statements don't include the key word import only the path
     *
     * @return set of imports
     */
    protected Set<String> getImports() {
        return Collections.unmodifiableSet(imports);
    }

    /**
     * add an import. Only the path is required.
     * expects the path is trimmed
     * eg. addImport("java.util.*")
     *
     * @param importString imports string
     */
    public void addImport(String importString) {
        this.imports.add(importString);
    }

    /**
     * @param htmlTag html tag to add to the stack
     */
    public void addHtmlTag(HtmlTag htmlTag) {
        this.tagStack.push(htmlTag);
    }

    /**
     * @return popped html tag.
     */
    public Optional<HtmlTag> popHtmlTag() {
        return Optional.ofNullable(tagStack.poll());
    }

    /**
     * @return unmodifiable map of plain html variables
     */
    protected Map<Integer, StringBuilder> getPlainHtmlVariables() {
        return Collections.unmodifiableMap(this.plainHtmlVariables);
    }

    /**
     * @param templateFile template file path
     * @return process template
     */
    public Optional<Template> getTemplate(Path templateFile) {
        return this.templateDataMap
                .keySet()
                .stream()
                .filter(template -> template.getFile().equals(templateFile))
                .findFirst();
    }

    /**
     * adds the statement to the provided Template.
     * <p>
     * internally increments the plain html variable count as well.
     *
     * @param template  Template
     * @param statement statement to add
     */
    public void addStatement(Template template, RenderBodyStatement statement) {
        this.templateDataMap.putIfAbsent(template, new TemplateData());
        this.templateDataMap.get(template)
                .appendToBody(statement);

        if (plainHtmlVariables.containsKey(plainHtmlVariableCount)) {
            ++this.plainHtmlVariableCount;
        }
    }

    /**
     * increments the plain html variable count.
     */
    public void incrementPlainHtmlVariableCount() {
        ++this.plainHtmlVariableCount;
    }

    /**
     * increments the indentation of the renderBody of the provided template.
     *
     * @param template Template
     */
    public void incrementRenderBodyIndentation(Template template) {
        this.templateDataMap.putIfAbsent(template, new TemplateData());
        this.templateDataMap.get(template)
                .incrementRenderBodyIndentation();
    }

    /**
     * decrements the indentation of the renderBody of the provided template.
     *
     * @param template Template
     */
    public void decrementRenderBodyIndentation(Template template) {
        this.templateDataMap.putIfAbsent(template, new TemplateData());
        this.templateDataMap.get(template)
                .decrementRenderBodyIndentation();
    }

    /**
     * key -> variable name
     * value -> variable type
     *
     * @param template Template
     * @return unmodifiable Map of variables of the template
     */
    public Map<String, String> getVariables(Template template) {
        return this.templateDataMap.get(template)
                .getVariables();
    }

    /**
     * @return returns a unmodifiable map of template data
     */
    protected Map<Template, TemplateData> getTemplateDataMap() {
        return Collections.unmodifiableMap(this.templateDataMap);
    }

    /**
     * Appends plain html for the specified template. Appends indentation at start and new line at the end.
     * Also adds a render body statement for the provided template
     *
     * @param template template
     * @param html     plain html
     */
    public void appendPlainHtml(Template template, String html) {

        appendPlainHtml(template, html, true, true);
    }

    /**
     * Appends plain html for the specified template. checks for blank html
     * Also adds a render body statement for the provided template
     *
     * @param template          template
     * @param html              plain html
     * @param appendIndentation should append indentation as the start
     * @param appendNewLine     should append new line at the end
     */
    public void appendPlainHtml(Template template, String html, boolean appendIndentation, boolean appendNewLine) {
        if (StringUtils.isNotBlank(html)) {
            StringBuilder builder = getPlainHtmlValueBuilder(template);

            if (appendIndentation) {
                builder.append(StringEscapeUtils.escapeJava(getIndentations(this.tagStack.size())));
            }

            builder.append(StringEscapeUtils.escapeJava(html));

            if (appendNewLine) {
                builder.append(StringEscapeUtils.escapeJava(BREAK_LINE));
            }

        }
    }

    /**
     * appends indentation to the plain html based on size of the tag stack.
     *
     * @param template template
     */
    public void appendPlainHtmlIndentation(Template template) {
        getPlainHtmlValueBuilder(template)
                .append(StringEscapeUtils.escapeJava(getIndentations(this.tagStack.size())));
    }

    /**
     * appends a new line to the plain html.
     *
     * @param template template
     */
    public void appendPlainHtmlNewLine(Template template) {
        getPlainHtmlValueBuilder(template)
                .append(StringEscapeUtils.escapeJava(BREAK_LINE));
    }


    /**
     * get html string builder of the provided template.
     *
     * @param template template
     * @return string builder of the template
     */
    private StringBuilder getPlainHtmlValueBuilder(Template template) {
        return this.plainHtmlVariables.computeIfAbsent(this.plainHtmlVariableCount, (variable) -> computeAbsentPlainHtmlVariable(template, variable));
    }

    /**
     * Adds the plane html writer to the body of the template
     *
     * @param template     template
     * @param variableName plain html variable counter
     * @return string builder
     */
    private StringBuilder computeAbsentPlainHtmlVariable(Template template, int variableName) {

        this.templateDataMap.computeIfAbsent(template, (template1) -> new TemplateData())
                .appendToBody(new PlainStringRenderBodyStatement(
                        getWriterVariableName() +
                                ".write(" +
                                PLAIN_HTML_VARIABLE_PREFIX +
                                variableName +
                                ");"
                ));

        return new StringBuilder();
    }

    /**
     * adds the variable to the respective template.
     * expects the name and type will not contain leading or trailing spaces.
     *
     * @param template template
     * @param type     type of the variable
     * @param name     name of the variable
     */
    public void addVariable(Template template, String type, String name) {
        this.templateDataMap.putIfAbsent(template, new TemplateData());
        this.templateDataMap.get(template)
                .addVariable(type, name);
    }

    /**
     * returns the root template
     *
     * @return the root template
     */
    public Template getRootTemplate() {
        return this.templateDataMap
                .keySet()
                .stream()
                .filter(template -> template.getRootTemplate().equals(template))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Couldn't find the root template"));
    }

    public abstract String render();

}
