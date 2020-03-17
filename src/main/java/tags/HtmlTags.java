package tags;

import java.util.Optional;
import java.util.regex.Pattern;

public final class HtmlTags {


    /**
     * tries to parse the html string and returns the appropriate HtmlTag
     *
     * @param htmlString string containing html
     * @return HtmlTag
     */
    public static Optional<HtmlTag> parse(String htmlString) {

        if (HtmlUtils.isHtmlTagAtStart(htmlString)) {
            return Optional.of(new RegularHtmlTag(htmlString));
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
