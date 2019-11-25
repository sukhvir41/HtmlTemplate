package template;

public class TemplateClass {

    private StringBuilder classString = new StringBuilder();
    private HtmlTemplate template;

    public TemplateClass(String name, HtmlTemplate template) {
        this.template = template;

        classString.append("public class ")
                .append(name)
                .append(" {")
                .append("\n")
                .append("public static String render() {")
                .append("\n")
                .append("StringBuilder builder = new StringBuilder();");
    }

    public void appendHtmlTag(HtmlTag htmlTag) {
        classString.append("\n builder.append(\"")
                .append(getIndentation())
                .append(htmlTag.toString())
                .append("\\n\");");
    }

    public void appendContent(Content content) {
        classString.append("\n builder.append(\"")
                .append(getIndentation())
                .append(content.getContent())
                .append("\\n\");");
    }

    public void appendString(String string) {
        classString.append("\n builder.append(\"")
                .append(HtmlUtils.escapeQuotes(string))
                .append("\\n\");");
    }

    public void appendComment(String comment) {
        classString.append("\n builder.append(\"")
                .append(getIndentation())
                .append(HtmlUtils.escapeQuotes(comment))
                .append("\\n\");");
    }

    public void appendStyle(String style){
        classString.append("\n builder.append(\"")
                .append(getIndentation())
                .append(HtmlUtils.escapeQuotes(style))
                .append("\\n\");");
    }

    public String generateClass() {
        classString.append("\n return builder.toString(); \n }\n}");
        return classString.toString();
    }

    private String getIndentation() {
        return HtmlUtils.getIndentations(template.getTagsStackSize());
    }


}
