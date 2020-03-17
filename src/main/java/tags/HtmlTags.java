package tags;

import java.util.Optional;
import java.util.regex.Pattern;

public final class HtmlTags {

    private static final Pattern DYNAMIC_HTML_TAG = Pattern.compile("<[\\s,\\S]* ht-[a-z]+\\s*=\"[\\s,\\S]", Pattern.CASE_INSENSITIVE);

    /**
     * tries to parse the html string and returns the appropriate HtmlTag
     *
     * @param htmlString string containing html
     * @return HtmlTag
     */
    public static Optional<HtmlTag> parse(String htmlString) {
        if (containsHtmlTag(htmlString)) {
            var tagString = htmlString.substring(htmlString.lastIndexOf('<'));

            var matcher = DYNAMIC_HTML_TAG.matcher(tagString);

            if (matcher.find()) {
                return parseDynamicHtml(tagString);
            } else {
                return Optional.of(new RegularHtmlTag(tagString));
            }
        } else {
            return Optional.empty();
        }
    }

    private static Optional<HtmlTag> parseDynamicHtml(String tagString) {
        if (MetaImportHtmlTag.matches(tagString)) {
            return Optional.of(new MetaImportHtmlTag(tagString));
        } else if (IfHtmlTag.matches(tagString)) {
            return Optional.of(new IfHtmlTag(tagString));
        } else {
            return Optional.empty();
        }

    }

    public static boolean containsHtmlTag(String html) {
        return html.contains("<");
    }

}
