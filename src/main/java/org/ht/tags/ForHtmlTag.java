package org.ht.tags;

import org.ht.processors.Code;
import org.ht.template.IllegalSyntaxException;
import org.ht.template.TemplateClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ForHtmlTag extends RegularHtmlTag {

    public static final Pattern FOR_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-for\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    private static final String IN = " in ";


    static boolean matches(String html) {
        return FOR_ATTRIBUTE_PATTERN.matcher(html)
                .find();
    }


    ForHtmlTag(String htmlString) {
        super(htmlString);
    }

    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        var matcher = FOR_ATTRIBUTE_PATTERN.matcher(this.htmlString);
        if (matcher.find()) {
            var forStatement = extractForStatement(matcher);

            var forStatementParts = forStatement.split(IN);

            if (forStatementParts.length != 2) {
                throw new IllegalSyntaxException("Error in ht-for.\nLine -> " + this.htmlString);
            }

            var collection = Code.parse(forStatementParts[1].trim());
            var variables = forStatementParts[0].trim();

            templateClass.addCode("forEach(" + collection + ", (" + variables + ") -> {");
            templateClass.incrementFunctionIndentation();

        }
    }

    private String extractForStatement(Matcher matcher) {
        var forAttribute = this.htmlString
                .substring(matcher.start(), matcher.end());

        return forAttribute.substring(forAttribute.indexOf('"') + 1, forAttribute.length() - 1)
                .replace("'", "\"");
    }

    @Override
    public void processTag(TemplateClass templateClass) {
        var matcher = FOR_ATTRIBUTE_PATTERN.matcher(this.htmlString);
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
        templateClass.addCode("});");
    }
}
