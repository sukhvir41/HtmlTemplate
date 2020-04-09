package org.ht.tags;

import org.ht.template.TemplateClass;

import static org.ht.tags.HtmlUtils.ELSE_ATTRIBUTE_PATTERN;

public class ElseHtmlTag extends RegularHtmlTag {

    public static boolean matches(String string) {
        return ELSE_ATTRIBUTE_PATTERN.matcher(string)
                .find();
    }

    public ElseHtmlTag(String htmlString) {
        super(htmlString);
    }

    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        templateClass.addCode("else {");
        templateClass.incrementFunctionIndentation();
    }

    @Override
    public void processClosingTag(TemplateClass templateClass) {
        templateClass.decrementFunctionIndentation();
        templateClass.addCode("}");
    }

    @Override
    public String getHtml() {
        var matcher = ELSE_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var leftPart = this.htmlString.substring(0, matcher.start());
            var rightPart = this.htmlString.substring(matcher.end());
            return leftPart + rightPart;
        } else {
            return this.htmlString;
        }
    }
}
