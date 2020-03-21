package org.ht.tags;

import org.ht.template.HtmlTemplate;

public class MetaTypeTag extends RegularHtmlTag {

    private HtmlTemplate htmlTemplate;
    private String variable = "";
    private String theClass = "";//the class

    public static boolean matches(String html) {
        return HtmlUtils.META_TYPE_TAG_PATTERN.matcher(html)
                .find();
    }

    protected MetaTypeTag(String htmlString, HtmlTemplate htmlTemplate) {
        super(htmlString);
        this.htmlTemplate = htmlTemplate;

        extractType();
        addType();
    }

    private void extractType() {
        var matcher = HtmlUtils.TYPE_ATTRIBUTE_PATTERN
                .matcher(super.htmlString);

        if (matcher.find()) {
            var htType = super.htmlString
                    .substring(matcher.start(), matcher.end());

            var rightSide = htType.substring(htType.indexOf("=") + 1)
                    .replace('"', ' ')
                    .trim();

            this.variable = rightSide.substring(0, rightSide.indexOf(','))
                    .trim();
            this.theClass = rightSide.substring(rightSide.indexOf(',') + 1)
                    .trim();
        }
    }

    private void addType() {
        this.htmlTemplate.addType(this.variable, this.theClass);
    }


    @Override
    public String getHtml() {
        return "";
    }


}
