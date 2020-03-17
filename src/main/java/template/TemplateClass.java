package template;

import tags.HtmlTag;

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
    }

    public void appendHtmlTag(HtmlTag htmlTag) {

        String html = htmlTag.getHtml();

        if (html.replace("\\n", "").isBlank()) {
            html = "";
        }

        classString.append("\n writer.append(\"")
                .append(getIndentation())
                .append(escapeQuotes(html))
                .append("\\n\");");
    }

    public void appendContent(Content content) {
        classString.append("\n writer.append(\"")
                .append(getIndentation())
                .append(escapeQuotes(content.getContent()))
                .append("\\n\");");
    }

    public void appendString(String string) {
        classString.append("\n writer.append(\"")
                .append(escapeQuotes(string))
                .append("\\n\");");
    }

    public void appendComment(String comment) {
        classString.append("\n writer.append(\"")
                .append(getIndentation())
                .append(escapeQuotes(comment))
                .append("\\n\");");
    }

    public void appendStyle(String style) {
        classString.append("\n writer.append(\"")
                .append(getIndentation())
                .append(escapeQuotes(style))
                .append("\\n\");");
    }

    public void appendScript(String script) {
        classString.append("\n writer.append(\"")
                .append(getIndentation())
                .append(escapeQuotes(script))
                .append("\\n\");");
    }

    public void appendCode(String code) {
        classString.append("\n")
                .append(getIndentation())
                .append(code);

    }

    public String generateClass() {

        var head = new StringBuilder();

        imports.forEach(head::append);

        head.append("\n")
                .append("public class ")
                .append(this.className)
                .append(" {")
                .append("\n")
                .append("public static void render(Writer writer) {")
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


    public static String escapeQuotes(String string) {
        return string.replace("\"", "\\\"");
    }

}
