package org.ht.tags;

import org.apache.commons.lang3.StringUtils;
import org.ht.template.HtmlTemplate;
import org.owasp.encoder.Encode;

public class Content {

    private String content;
    private HtmlTemplate htmlTemplate;

    public Content(String content, HtmlTemplate htmlTemplate) {
        this.content = content;
        this.htmlTemplate = htmlTemplate;
    }


    public String getContent() {
        if (containsDynamicContent()) {
            return dynamicContent();
        } else {
            return encodeContent(content);
        }
    }

    private boolean containsDynamicContent() {
        return HtmlUtils.ESCAPED_CONTENT_PATTERN
                .matcher(content)
                .find();
    }

    private String dynamicContent() {
        var content = this.content;
        var processedContent = new StringBuilder();

        processDynamicContent(content, processedContent);

        return processedContent.toString();
    }

    private void processDynamicContent(String content, StringBuilder processedContent) {
        var matcher = HtmlUtils.ESCAPED_CONTENT_PATTERN.matcher(content);

        if (matcher.find()) {

            //appending plain content
            processedContent.append(content, 0, matcher.start());

            // this would contain {{ <code> }}
            var dynamicContent = content.substring(matcher.start(), matcher.end());

            var paramsMatcher = HtmlUtils.PARAMS_PATTERN.matcher(dynamicContent);

            if (paramsMatcher.find()) {

                var paramsContent = getParamsContent(dynamicContent);

                appendDynamicContent(processedContent, paramsContent);

                processDynamicContent(content.substring(matcher.end()), processedContent);
            } else {
                var contentToAppend = dynamicContent.substring(2, dynamicContent.length() - 2);

                appendDynamicContent(processedContent, contentToAppend);

                processDynamicContent(content.substring(matcher.end()), processedContent);
            }
        } else {
            if (StringUtils.isNotBlank(content)) {
                processedContent.append(encodeContent(content));
            }
        }
    }

    private String getParamsContent(String dynamicContent) {
        return "";
    }

    private int getStartIndexOfParams(String dynamicContent, int start) {
        for (int i = start; i < dynamicContent.length(); i++) {
            if (dynamicContent.charAt(i) == 'p') {
                return i;
            }
        }
        return -1;
    }

    private void appendDynamicContent(StringBuilder processedContent, String dynamicContent) {
        processedContent.append("\").append(")
                .append("Encode.forHtmlContent(String.valueOf(")
                .append(dynamicContent)
                .append("))).append(\"");
    }


    protected String encodeContent(String content) {
        return Encode.forJava(content);
    }


}
