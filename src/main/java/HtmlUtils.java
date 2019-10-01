public class HtmlUtils {


    public static boolean containsHtmlTag(String html) {
        return html.indexOf('<') > -1;
    }

    public static boolean containsContent(String html) {
        if (!containsHtmlTag(html)) {
            return true;
        } else {
            var contentLength = html.substring(0, html.indexOf('<'))
                    .trim()
                    .length();
            return contentLength > 0;
        }
    }

    public static String escapeQuotes(String string) {
        return string.replace("\"", "\\\"");
    }

    public static String getIndentations(int count) {

        var builder = new StringBuilder();

        for (int i = 0; i < count; i++) {
            builder.append("\t");
        }

        return builder.toString();
    }

    public static boolean isDocTypeTag(String html) {
        return html.trim()
                .startsWith("<!DOCTYPE") || html.trim()
                .startsWith("<!doctype");
    }

    public static boolean containsHtmlComment(String html) {
        return html.contains("<!--");
    }

    public static boolean containsClosingHtmlComment(String html) {
        return html.contains("-->");
    }

}
