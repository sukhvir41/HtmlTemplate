public class Test {
    public static String render() {
        StringBuilder builder = new StringBuilder();
        builder.append("<html>");
        builder.append("   <body>");
        builder.append("       <h1>");
        builder.append(" this is a test </h1>");
        builder.append("   </body>");
        builder.append("</html>");
        return builder.toString();
    }
}