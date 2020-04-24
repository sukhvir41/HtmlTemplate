package org.ht.tags;

import org.apache.commons.lang3.StringUtils;
import org.ht.processors.Code;
import org.ht.template.IllegalSyntaxException;
import org.ht.template.TemplateClass;
import org.ht.utils.HtStringUtils;

public class Content {

    public static final String ESCAPED_CONTENT_START = "{{";
    public static final String ESCAPED_CONTENT_END = "}}";
    public static final String UNESCAPED_CONTENT_START = "{{{";
    public static final String UNESCAPED_CONTENT_END = "}}}";

    private String content;
    private TemplateClass templateClass;
    private boolean isFirstLeftPart = true;


    public Content(String content, TemplateClass templateClass) {
        this.content = content;
        this.templateClass = templateClass;
    }


    public void process() {
        if (containsDynamicContent()) {
            processDynamicContent(content);//process left to right
        } else {
            templateClass.appendPlainHtml(content);
        }
    }

    private boolean containsDynamicContent() {
        //this will match both escaped and unescaped content
        return content.contains(ESCAPED_CONTENT_START);
    }

    private void processDynamicContent(String content) {

        int escapedStartIndex = content.indexOf(ESCAPED_CONTENT_START);
        int unescapedStartIndex = content.indexOf(UNESCAPED_CONTENT_START);

        if (unescapedStartIndex > -1 && unescapedStartIndex <= escapedStartIndex) {
            processUnescapedDynamicContent(unescapedStartIndex, content);
        } else if (escapedStartIndex > -1) {
            processEscapedDynamicContent(escapedStartIndex, content);
        } else {
            if (StringUtils.isBlank(content)) {
                templateClass.appendPlainHtmlNewLine();
            } else {
                templateClass.appendPlainHtml(content, false, true);
            }
        }
    }

    private void processEscapedDynamicContent(int escapedContentStart, String content) {
        processLeftPart(escapedContentStart, content);

        int endIndex = HtStringUtils.findIndex(escapedContentStart, ESCAPED_CONTENT_END, content);

        if (endIndex == -1) { // end not found
            throw new IllegalSyntaxException("Could not find the \"" + ESCAPED_CONTENT_END + "\" in line -> \n " + this.content);
        }

        var theCode = content
                .substring(escapedContentStart + ESCAPED_CONTENT_START.length(), endIndex)
                .trim();

        addCode(Code.parse(theCode));

        var remainingContent = content.substring(endIndex + ESCAPED_CONTENT_END.length());

        processDynamicContent(remainingContent);
    }


    private void processUnescapedDynamicContent(int unescapedContentStart, String content) {
        processLeftPart(unescapedContentStart, content);

        int endIndex = HtStringUtils.findIndex(unescapedContentStart, UNESCAPED_CONTENT_END, content);

        if (endIndex == -1) { // end nt found
            throw new IllegalSyntaxException("Could not find the \"" + UNESCAPED_CONTENT_END + "\" in line -> \n " + this.content);
        }

        var theCode = content
                .substring(unescapedContentStart + UNESCAPED_CONTENT_START.length(), endIndex)
                .trim();

        addUnescapedCode(Code.parse(theCode));

        var remainingContent = content.substring(endIndex + UNESCAPED_CONTENT_END.length());

        processDynamicContent(remainingContent);
    }


    private void processLeftPart(int dynamicStartIndex, String content) {
        var plaintHtmlLeft = StringUtils.left(content, dynamicStartIndex);
        if (StringUtils.isBlank(plaintHtmlLeft)) { //if staring of with {{ dynamic content}} or {{{ dynamic content }}}
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
