package org.ht.tags;

import org.ht.processors.Code;
import org.ht.template.HtmlTemplate;
import org.ht.template.TemplateClass;

import java.util.regex.Matcher;

import static org.ht.tags.HtmlUtils.IF_ATTRIBUTE_PATTERN;

public class IfHtmlTag extends RegularHtmlTag {

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
            String ifCondition = Code.parse(getIfCondition(matcher));
            templateClass.addCode("if(condition( ()-> " + ifCondition + " )){");
            templateClass.incrementFunctionIndentation();
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
            return html;
        } else {
            return super.htmlString;
        }
    }

    @Override
    public void processClosingTag(TemplateClass templateClass) {
        templateClass.decrementFunctionIndentation();
        templateClass.addCode("}");
    }

}
