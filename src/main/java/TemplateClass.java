public class TemplateClass {

    private StringBuilder classString = new StringBuilder();

    public TemplateClass(String name) {

        classString.append("public class ")
                .append(name)
                .append(" {")
                .append("\n")
                .append("public static String render() {")
                .append("\n")
                .append("StringBuilder builder = new StringBuilder();");


    }

    public void appendString(String string) {
        classString.append("\n builder.append(\"")
                .append(string)
                .append("\\n\");");
    }

    public void appendContent(String content) {
        classString.append("\n builder.append(\"")
                .append(content)
                .append("\\n\");");
    }

    public String generateClass() {
        classString.append("\n return builder.toString(); \n }\n}");
        return classString.toString();
    }


}
