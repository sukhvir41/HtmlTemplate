package org.ht.tags;

import org.ht.processors.Code;
import org.ht.template.TemplateClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IfHtmlTag extends RegularHtmlTag {

    public static final Pattern IF_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-if\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

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
            templateClass.addCode("if(condition( () -> " + ifCondition + " )){");
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
    public void processTag(TemplateClass templateClass) {
        var matcher = IF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var leftPart = this.htmlString.substring(0, matcher.start());
            var rightPart = this.htmlString.substring(matcher.end());
            templateClass.appendPlainHtml(leftPart + rightPart);
        } else {
            templateClass.appendPlainHtml(this.htmlString);
        }
    }

    @Override
    public void processClosingTag(TemplateClass templateClass) {
        templateClass.decrementFunctionIndentation();
        templateClass.addCode("}");
    }

}
