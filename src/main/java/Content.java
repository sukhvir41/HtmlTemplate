import java.util.Optional;

public class Content {
    private String content;

    public static Optional<Content> parseContent(String html) {

        if (HtmlUtils.containsContent(html)) {

            Content content;

            if (HtmlUtils.containsHtmlTag(html)) {
                var contentString = html.substring(0, html.indexOf('<'))
                        .trim();
                content = new Content(contentString);
            } else {
                content = new Content(html);
            }
            return Optional.of(content);
        }

        return Optional.empty();
    }


    private Content(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

}
