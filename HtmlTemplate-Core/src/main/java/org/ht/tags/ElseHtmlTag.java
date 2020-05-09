package org.ht.tags;

import org.ht.template.TemplateClass;
import org.apache.commons.lang3.StringUtils;

public final class ElseHtmlTag extends RegularHtmlTag {

    private static String HT_ELSE = "ht-else";

    public static boolean matches(String string) {
        return StringUtils.containsIgnoreCase(string, HT_ELSE);
    }

    ElseHtmlTag(String htmlString) {
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
    public void processTag(TemplateClass templateClass) {

        int startIndex = this.htmlString.indexOf(HT_ELSE);

        if (startIndex == -1) {
            //templateClass.appendPlainHtml(this.htmlString);
            new DynamicAttributeHtmlTag(this.htmlString)
                    .processTag(templateClass);
        } else {
            int endIndex = startIndex + HT_ELSE.length();
            var leftPart = this.htmlString.substring(0, startIndex)
                    .trim();
            var rightPart = this.htmlString.substring(endIndex);
            //templateClass.appendPlainHtml(leftPart + rightPart);
            new DynamicAttributeHtmlTag(leftPart + rightPart)
                    .processTag(templateClass);
        }

    }
}
