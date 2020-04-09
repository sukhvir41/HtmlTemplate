package org.ht.template;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.util.*;

public class TemplateClass {

    private static final String PLAIN_HTML_VARIABLE_PREFIX = "PLAIN_HTML_";

    private HtmlTemplate template;
    private String className;
    private String packageName;

    // classes to be imported
    private Set<String> imports = new HashSet<>();
    // plainHTML variables name, value
    private Map<Integer, StringBuilder> plainHtmlVariables = new TreeMap<>();
    // variables name, type
    private Map<String, String> variablesTypes = new HashMap<>();

    private StringBuilder renderFunctionBody = new StringBuilder();

    private int renderFunctionIndentation = 2;

    private int variableCount = 0;

    private final String BREAK_LINE = "\n";

    public TemplateClass(String packageName, String className, HtmlTemplate template) {
        this.className = className;
        this.packageName = packageName;
        this.template = template;
        this.addImportStatement("java.io.Writer");
        this.addImportStatement("java.io.IOException");
    }

    public TemplateClass(String className, HtmlTemplate template) {
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


    public void addVariable(String name, String type) {
        if (StringUtils.isNoneBlank(name, type)) {
            this.variablesTypes.put(name, type);
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
                .append(" extends org.ht.template.Template {")
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
