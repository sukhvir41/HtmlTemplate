package org.ht.tags;

import org.ht.template.HtmlTemplate;

import java.util.Optional;

public final class HtmlTags {


    /**
     * tries to parse the html string and returns the appropriate HtmlTag
     *
     * @param htmlString string containing html
     * @return HtmlTag
     */
    public static Optional<HtmlTag> parse(String htmlString, HtmlTemplate htmlTemplate) {

        if (HtmlUtils.isHtmlTagAtStart(htmlString)) {
            //return Optional.of(new RegularHtmlTag(htmlString));
            if (containsDynamicAttribute(htmlString)) {
                return parseDynamicHtml(htmlString, htmlTemplate);
            } else {
                return Optional.of(new RegularHtmlTag(htmlString));
            }
        } else {
            return Optional.empty();
        }

    }

    private static Optional<HtmlTag> parseDynamicHtml(String tagString, HtmlTemplate htmlTemplate) {
        if (MetaImportHtmlTag.matches(tagString)) {
            return Optional.of(new MetaImportHtmlTag(tagString));

        } else if (MetaTypeTag.matches(tagString)) {
            return Optional.of(new MetaTypeTag(tagString, htmlTemplate));

        } else if (IfHtmlTag.matches(tagString)) {
            return Optional.of(new IfHtmlTag(tagString));

        } else if (ElseIfHtmlTag.matches(tagString)) {
            return Optional.of(new ElseIfHtmlTag(tagString));

        } else if (ElseHtmlTag.matches(tagString)) { // else tag check should be after else if check
            return Optional.of(new ElseHtmlTag(tagString));

        } else {
            return Optional.empty();
        }

    }

    private static boolean containsDynamicAttribute(String htmlString) {
        return HtmlUtils.DYNAMIC_ATTRIBUTE.matcher(htmlString)
                .find();
    }

}
