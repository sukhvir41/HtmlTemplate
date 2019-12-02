package template;

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
    }

    public void appendHtmlTag(HtmlTag htmlTag) {
        classString.append("\n builder.append(\"")
                .append(getIndentation())
                .append(escapeQuotes(htmlTag.toString()))
                .append("\\n\");");
    }

    public void appendContent(Content content) {
        classString.append("\n builder.append(\"")
                .append(getIndentation())
                .append(escapeQuotes(content.getContent()))
                .append("\\n\");");
    }

    public void appendString(String string) {
        classString.append("\n builder.append(\"")
                .append(escapeQuotes(string))
                .append("\\n\");");
    }

    public void appendComment(String comment) {
        classString.append("\n builder.append(\"")
                .append(getIndentation())
                .append(escapeQuotes(comment))
                .append("\\n\");");
    }

    public void appendStyle(String style) {
        classString.append("\n builder.append(\"")
                .append(getIndentation())
                .append(escapeQuotes(style))
                .append("\\n\");");
    }

    public void appendScript(String style) {
        classString.append("\n builder.append(\"")
                .append(getIndentation())
                .append(escapeQuotes(style))
                .append("\\n\");");
    }

    public String generateClass() {

        var head = new StringBuilder();

        imports.forEach(head::append);

        head.append("\n")
                .append("public class ")
                .append(this.className)
                .append(" {")
                .append("\n")
                .append("public static String render() {")
                .append("\n")
                .append("StringBuilder builder = new StringBuilder();");

        classString.insert(0, head.toString());

        classString.append("\n return builder.toString(); \n }\n}");
        return classString.toString();
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
