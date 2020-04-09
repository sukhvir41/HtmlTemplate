package org.ht.tags;

import org.ht.processors.Code;
import org.ht.template.TemplateClass;

import java.util.regex.Matcher;

import static org.ht.tags.HtmlUtils.ELSEIF_ATTRIBUTE_PATTERN;

public class ElseIfHtmlTag extends RegularHtmlTag {

    public static boolean matches(String string) {
        return ELSEIF_ATTRIBUTE_PATTERN.matcher(string)
                .find();
    }


    ElseIfHtmlTag(String htmlString) {
        super(htmlString);
    }


    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        var matcher = ELSEIF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            String ifCondition = Code.parse(getIfCondition(matcher));
            templateClass.addCode("else if(condition( () -> " + ifCondition + " )){");
            templateClass.incrementFunctionIndentation();
        }
    }

    private String getIfCondition(Matcher matcher) {
        var ifAttribute = this.htmlString
                .substring(matcher.start(), matcher.end());

        var ifCondition = ifAttribute.substring(ifAttribute.indexOf('"') + 1, ifAttribute.length() - 1);

        return ifCondition.replace("'", "\"")
                .trim();
    }


    @Override
    public void processClosingTag(TemplateClass templateClass) {
        templateClass.decrementFunctionIndentation();
        templateClass.addCode("}");
    }

    @Override
    public String getHtml() {
        var matcher = ELSEIF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var leftPart = htmlString.substring(0, matcher.start());
            var rightPart = htmlString.substring(matcher.end());
            return leftPart + rightPart;
        } else {
            return super.htmlString;
        }
    }
}
