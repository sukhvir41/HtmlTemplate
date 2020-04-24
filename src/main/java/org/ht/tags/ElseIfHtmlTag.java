package org.ht.tags;

import org.ht.processors.Code;
import org.ht.template.TemplateClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElseIfHtmlTag extends RegularHtmlTag {

    public static final Pattern ELSEIF_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-elseIf\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

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
    public void processTag(TemplateClass templateClass) {
        var matcher = ELSEIF_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var leftPart = this.htmlString.substring(0, matcher.start());
            var rightPart = this.htmlString.substring(matcher.end());
            templateClass.appendPlainHtml(leftPart + rightPart);
        } else {
            templateClass.appendPlainHtml(this.htmlString);
        }
    }
}
