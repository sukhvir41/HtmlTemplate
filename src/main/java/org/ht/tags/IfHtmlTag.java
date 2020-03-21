package org.ht.tags;

import org.ht.template.TemplateClass;

import java.util.regex.Matcher;

import static org.ht.tags.HtmlUtils.IF_ATTRIBUTE_PATTERN;

public class IfHtmlTag extends RegularHtmlTag {

    private String ifCondition;

    public static boolean matches(String string) {
        return IF_ATTRIBUTE_PATTERN.matcher(string)
                .find();
    }


    IfHtmlTag(String htmlString) {
        super(htmlString);
        this.htmlString = htmlString;
    }

    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        var matcher = IF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            String ifCondition = getIfCondition(matcher);
            templateClass.appendCode("if(" + ifCondition + "){");
        }
    }

    private String getIfCondition(Matcher matcher) {
        var ifCondition = this.htmlString
                .substring(matcher.start(), this.htmlString.indexOf('"', matcher.start() + 7) + 1)
                .substring(7);

        return ifCondition.substring(0, ifCondition.length() - 1)
                .replace("'", "\"")
                .trim();
    }

    @Override
    public String getHtml() {
        var matcher = IF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var html = htmlString.substring(0, matcher.start())
                    + htmlString.substring(matcher.end());

            if (html.charAt(html.length() - 1) == '>') {
                return html;

            } else {
                return html + ">";
            }

        } else {
            return super.htmlString;
        }
    }

    @Override
    public void processClosingTag(TemplateClass templateClass) {
        templateClass.appendCode("}");
    }

}
