package org.ht.template;

import org.apache.commons.lang3.StringUtils;
import org.owasp.encoder.Encode;

import java.util.*;

public class TemplateClass {

    private StringBuilder classString = new StringBuilder();
    private HtmlTemplate template;
    private String className;
    private String packageName;

    // classes to be imported
    private Set<String> imports = new HashSet<>();
    // plainHTML variables name, value
    private Map<String, StringBuilder> plainHtmlVariables = new TreeMap<>();
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
        this.addImportStatement("org.ht.template.Parameters");
        this.addImportStatement("java.io.IOException");
    }

    public TemplateClass(String className, HtmlTemplate template) {
        this("", className, template);
    }

    public void appendPlainHtml(String html) {
        if (StringUtils.isNotBlank(html)) {
            getPlainHtmlValueBuilder()
                    .append(getIndentation())
                    .append(encodeForJava(html))
                    .append("\\n");
        }
    }

    //returns the current variable value builder
    private StringBuilder getPlainHtmlValueBuilder() {
        return plainHtmlVariables.computeIfAbsent(getCurrentVariable(), this::computeAbsentPlainHtmlVariable);
    }

    private StringBuilder computeAbsentPlainHtmlVariable(String variableName) {

        this.renderFunctionBody
                .append(getIndentations(renderFunctionIndentation))
                .append("writer.append(").append(variableName).append(");")
                .append(BREAK_LINE);

        return new StringBuilder();
    }


    public void addCode(String code) {
        this.renderFunctionBody
                .append(getIndentations(renderFunctionIndentation))
                .append(code)
                .append(BREAK_LINE);

        ++this.variableCount;
    }

    private String getCurrentVariable() {
        return "PLAIN_HTML_" + variableCount;
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

    private String createPublicFinalString(String name, StringBuilder value) {
        return "private static final String " + name + " = \"" + value.toString() + "\";";
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
        return Encode.forJava(string);
    }

}
