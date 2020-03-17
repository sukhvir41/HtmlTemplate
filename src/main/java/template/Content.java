package template;

import tags.HtmlTags;

import java.util.Optional;

public class Content {
    private String content;

    public static boolean containsContent(String html) {
        if (!HtmlTags.containsHtmlTag(html)) {
            return true;
        } else {
            var contentLength = html.substring(0, html.indexOf('<'))
                    .trim()
                    .length();

            return contentLength > 0;
        }
    }

    public static Optional<Content> parseContent(String html) {

        if (containsContent(html)) {

            Content content;

            if (HtmlTags.containsHtmlTag(html)) {
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
