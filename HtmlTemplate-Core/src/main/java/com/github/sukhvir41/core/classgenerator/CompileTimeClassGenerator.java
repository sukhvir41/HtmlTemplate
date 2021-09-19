package com.github.sukhvir41.core.classgenerator;

import java.util.HashMap;
import java.util.Map;

import static com.github.sukhvir41.utils.StringUtils.getIndentations;

public class CompileTimeClassGenerator extends TemplateClassGenerator {
    public CompileTimeClassGenerator(String packageName, String className) {
        super(packageName, className);
    }

    @Override
    public String render() {
        StringBuilder classString = new StringBuilder();

        appendPackageName(classString);
        appendImports(classString);

        appendOpeningClass(classString);

        // appending static variables and methods
        appendPlainHtmlVariables(classString);
        appendGetInstanceMethod(classString);

        // appending writer initial size;
        appendWriterInitialSize(classString);

        //instance variables and methods
        appendVariableDeclaration(classString);

        appendGetter(classString);
        appendSetter(classString);

        appendRenderImpl(classString);

        appendClosingClass(classString);

        return classString.toString();
    }

    private void appendPackageName(StringBuilder classString) {
        classString.append("package ")
                .append(getPackageName())
                .append(";")
                .append(BREAK_LINE)
                .append(BREAK_LINE);
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

        Map<StringBuilder, Integer> uniquePlainHtmlVariables = new HashMap<>();

        getPlainHtmlVariables()
                .forEach((name, value) -> uniquePlainHtmlVariables.putIfAbsent(value, name));

        getPlainHtmlVariables()
                .forEach(
                        (name, value) -> {
                            classString.append(getIndentations(1))
                                    .append("private static final String ")
                                    .append(PLAIN_HTML_VARIABLE_PREFIX)
                                    .append(name)
                                    .append(" = ")
                                    .append(getPlainHtmlValue(name, value, uniquePlainHtmlVariables))
                                    .append(";")
                                    .append(BREAK_LINE);
                        }
                );
        classString.append(BREAK_LINE);
    }

    private StringBuilder getPlainHtmlValue(int name, StringBuilder value, Map<StringBuilder, Integer> uniquePlainHtmlVariables) {
        if (uniquePlainHtmlVariables.containsKey(value) && uniquePlainHtmlVariables.containsValue(name)) {
            return new StringBuilder("\"")
                    .append(value)
                    .append("\"");
        } else {
            return new StringBuilder(PLAIN_HTML_VARIABLE_PREFIX)
                    .append(uniquePlainHtmlVariables.get(value));
        }
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

    public void appendWriterInitialSize(StringBuilder classString) {
        long size = getPlainHtmlVariables()
                .values()
                .stream()
                .mapToLong(builder -> builder.length())
                .sum() * 2;

        if (size > Integer.MAX_VALUE) {
            size = Integer.MAX_VALUE;
        }

        classString.append(getIndentations(1))
                .append("@Override")
                .append(BREAK_LINE)
                .append(getIndentations(1))
                .append("public int writerInitialSize() {")
                .append(BREAK_LINE)
                .append(getIndentations(2))
                .append("return ")
                .append(size)
                .append(";")
                .append(BREAK_LINE)
                .append(getIndentations(1))
                .append("}")
                .append(BREAK_LINE)
                .append(BREAK_LINE);
    }

    private void appendVariableDeclaration(StringBuilder classString) {
        getVariables(getRootTemplate())
                .forEach((name, type) ->
                        classString.append(getIndentations(1))
                                .append("private ")
                                .append(type)
                                .append(" ")
                                .append(name)
                                .append(";")
                                .append(BREAK_LINE)
                );
        classString.append(BREAK_LINE);
    }

    private void appendGetter(StringBuilder classString) {
        getVariables(getRootTemplate())
                .forEach((name, type) ->
                        classString.append(getIndentations(1))
                                .append("private ")
                                .append(type)
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
        getVariables(getRootTemplate())
                .forEach((name, type) ->
                        classString.append(getIndentations(1))
                                .append("public ")
                                .append(getClassName())
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
                .append(renderCallRootTemplateRenderFunction())
                .append(BREAK_LINE)
                .append(getIndentations(1))
                .append("}")
                .append(BREAK_LINE)
                .append(BREAK_LINE);
    }

    private StringBuilder renderCallRootTemplateRenderFunction() {
        return this.getTemplateDataMap()
                .get(getRootTemplate())
                .getRenderFunctionBody();
    }

    private void appendClosingClass(StringBuilder classString) {
        classString.append("}");
    }
}
