package org.ht.tags;

import org.apache.commons.lang3.StringUtils;
import org.ht.processors.Code;
import org.ht.template.TemplateClass;

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
        //todo : have to make this tailed recursion. stop it from modifying the class member variable.
        var escapedMatcher = HtmlUtils.ESCAPED_CONTENT_PATTERN.matcher(content);
        var unescapedMatcher = HtmlUtils.UNESCAPED_CONTENT_PATTERN.matcher(content);

        if (unescapedMatcher.find()) {
            processUnescapedDynamicContent(unescapedMatcher);
        } else if (escapedMatcher.find()) {
            processEscapedDynamicContent(escapedMatcher);
        } else {
            if (StringUtils.isBlank(content)) {
                templateClass.appendPlainHtmlNewLine();
            } else {
                templateClass.appendPlainHtml(content, false, true);
            }
        }

    }

    private void processEscapedDynamicContent(Matcher dynamicContentMatcher) {
        processLeftPart(dynamicContentMatcher.start());

        var theCode = content
                .substring(dynamicContentMatcher.start() + 2, dynamicContentMatcher.end() - 2)
                .trim();

        addCode(Code.parse(theCode));

        this.content = content.substring(dynamicContentMatcher.end());

        processDynamicContent();
    }

    private void processUnescapedDynamicContent(Matcher unescapedDynamicContentMatcher) {
        processLeftPart(unescapedDynamicContentMatcher.start());

        var theCode = content
                .substring(unescapedDynamicContentMatcher.start() + 3, unescapedDynamicContentMatcher.end() - 3)
                .trim();

        addUnescapedCode(Code.parse(theCode));

        this.content = content.substring(unescapedDynamicContentMatcher.end());

        processDynamicContent();
    }


    private void processLeftPart(int dynamicStartIndex) {
        var plaintHtmlLeft = StringUtils.left(content, dynamicStartIndex);
        if (StringUtils.isBlank(plaintHtmlLeft)) { //if staring of with {{ dynamic content}}
            templateClass.appendPlainHtmlIndentation();
        } else {
            templateClass.appendPlainHtml(plaintHtmlLeft, isFirstLeftPart, false);
        }
        isFirstLeftPart = false;
    }


    private void addCode(String code) {
        templateClass.addCode("writer.append(content(() -> String.valueOf(" + code + ")));");
    }

    private void addUnescapedCode(String code) {
        templateClass.addCode("writer.append(unescapedContent(() -> String.valueOf(" + code + ")));");
    }

}
