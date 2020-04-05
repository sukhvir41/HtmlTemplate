package org.ht.tags;

import org.apache.commons.lang3.StringUtils;
import org.ht.template.TemplateClass;
import org.owasp.encoder.Encode;

import java.util.regex.Matcher;

public class Content {

    private String content;
    private TemplateClass templateClass;
    private boolean isFirstLeftPart = true;

    public Content(String content, TemplateClass templateClass) {
        this.content = content;
        this.templateClass = templateClass;
    }


    public void process() {
        if (containsDynamicContent()) {
            processDynamicContent();
        } else {
            templateClass.appendPlainHtml(content);
        }
    }

    private boolean containsDynamicContent() {
        //this will match both escaped and unescaped content
        return HtmlUtils.ESCAPED_CONTENT_PATTERN
                .matcher(content)
                .find();
    }

    private void processDynamicContent() {
        var matcher = HtmlUtils.ESCAPED_CONTENT_PATTERN.matcher(content);

        // todo add unescaped dynamic content
        if (matcher.find()) {
            processEscapedDynamicContent(matcher);
        } else {
            if (StringUtils.isBlank(content)) {
                templateClass.appendPlainHtmlNewLine();
            } else {
                templateClass.appendPlainHtml(content, false, true);
            }
        }

    }

    private void processEscapedDynamicContent(Matcher dynamicContentMatcher) {

        var plaintHtmlLeft = StringUtils.left(content, dynamicContentMatcher.start());
        if (StringUtils.isBlank(plaintHtmlLeft)) { //if staring of with {{ dynamic content}}
            templateClass.appendPlainHtmlIndentation();
        } else {
            templateClass.appendPlainHtml(plaintHtmlLeft, isFirstLeftPart, false);
        }
        isFirstLeftPart = false;

        var theCode = content.substring(dynamicContentMatcher.start() + 2, dynamicContentMatcher.end() - 2)
                .trim();

        processTheCode(theCode);

        this.content = content.substring(dynamicContentMatcher.end());

        processDynamicContent();
    }

    private void processTheCode(String theCode) {
        var variableMatcher = HtmlUtils.CONTENT_VARIABLE_PATTERN.matcher(theCode);
        int findIndex = 0;
        String newCode = theCode;
        while (variableMatcher.find(findIndex)) {
            var variable = theCode.substring(variableMatcher.start(), variableMatcher.end());
            newCode = StringUtils.replaceOnce(
                    newCode, variable, StringUtils.replaceOnce(variable, "@", "") + "()"
            );
            findIndex = variableMatcher.end();
        }
        addCode(newCode);
    }

    private void addCode(String code) {
        templateClass.addCode("writer.append(content(() -> String.valueOf(" + code + ")));");
    }


    protected String encodeContent(String content) {
        return Encode.forJava(content);
    }


}
