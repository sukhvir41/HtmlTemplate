package org.ht.template;

import org.ht.tags.Content;
import org.ht.tags.HtmlTag;
import org.owasp.encoder.Encode;

import java.util.HashSet;
import java.util.Set;

public class TemplateClass {

    private StringBuilder classString = new StringBuilder();
    private HtmlTemplate template;
    private String className;
    private Set<String> imports;

    public TemplateClass(String name, HtmlTemplate template) {
        this.className = name;
        this.template = template;
        this.imports = new HashSet<>();
        this.addImportStatement("java.io.Writer");
        this.addImportStatement("org.ht.template.Parameters");
        this.addImportStatement("org.owasp.encoder.Encode;");
    }

    public void appendHtmlTag(HtmlTag htmlTag) {

        String html = htmlTag.getHtml();

        if (html.replace("\\n", "").isBlank()) {
            html = "";
        }

        classString.append("\n writer.append(\"")
                .append(getIndentation())
                .append(encodeForJava(html))
                .append("\\n\"); // REGULAR");
    }

    public void appendContent(Content content) {
        classString.append("\n writer.append(\"")
                .append(getIndentation())
                .append(content.getContent())
                //.append(encodeForJava(content.getContent()))
                .append("\\n\"); // CONTENT");

    }

    public void appendComment(String comment) {
        classString.append("\n writer.append(\"")
                .append(getIndentation())
                .append(encodeForJava(comment))
                .append("\\n\"); // COMMENT");
    }

    public void appendStyle(String style) {
        classString.append("\n writer.append(\"")
                .append(getIndentation())
                .append(encodeForJava(style))
                .append("\\n\"); // STYLE");
    }

    public void appendScript(String script) {
        classString.append("\n writer.append(\"")
                .append(getIndentation())
                .append(encodeForJava(script))
                .append("\\n\"); // SCRIPT");
    }

    public void appendCode(String code) {
        classString.append("\n")
                .append(getIndentation())
                .append(code);

    }

    public String generateClass() {

        return getClassNameImpl(false);
    }

    public String generateReflectionClass() {

        return getClassNameImpl(true);
    }

    private String getClassNameImpl(boolean runtime) {
        var head = new StringBuilder();

        imports.forEach(theImport -> head.append(theImport).append("\n"));

        if (runtime) {
            head.append("\n")
                    .append("class ");
        } else {
            head.append("\n")
                    .append("public class ");
        }

        head.append(this.className)
                .append(" {")
                .append("\n")
                .append("public static void render(Writer writer,Parameters params) {")
                .append("\n")
                .append(" try{");

        classString.insert(0, head.toString());

        classString.append("\n }catch(Exception e){")
                .append("\n  throw new RuntimeException(e);")
                .append("\n }")
                .append("\n }\n}");
        return classString.toString();
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
