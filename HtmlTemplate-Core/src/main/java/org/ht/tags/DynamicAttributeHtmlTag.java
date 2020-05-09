package org.ht.tags;

import org.apache.commons.lang3.StringUtils;
import org.ht.processors.Code;
import org.ht.template.TemplateClass;
import org.ht.utils.HtmlUtils;

import java.util.regex.Matcher;

public class DynamicAttributeHtmlTag extends RegularHtmlTag {

    private static final String DYNAMIC_ATTRIBUTE_START = "ht-";

    boolean isFirstLeftPart = true;

    DynamicAttributeHtmlTag(String htmlString) {
        super(htmlString);
    }

    @Override
    public void processTag(TemplateClass templateClass) {
        processDynamicAttributes(templateClass, this.htmlString);
    }

    private void processDynamicAttributes(TemplateClass templateClass, String htmlString) {

        var matcher = HtmlUtils.DYNAMIC_ATTRIBUTE
                .matcher(htmlString);

        if (matcher.find()) {
            var leftPart = htmlString.substring(0, matcher.start());
            processLeftPart(templateClass, leftPart);
            processDynamicAttribute(matcher, templateClass, htmlString);

        } else {
            templateClass.appendPlainHtml(htmlString, isFirstLeftPart, true);
        }

    }

    private void processLeftPart(TemplateClass templateClass, String leftPart) {
        if (StringUtils.isNotBlank(leftPart)) {
            templateClass.appendPlainHtml(leftPart, isFirstLeftPart, false);
        }
        isFirstLeftPart = false;
    }

    private void processDynamicAttribute(Matcher matcher, TemplateClass templateClass, String htmlString) {
        var dynamicAttribute = htmlString.substring(matcher.start(), matcher.end());

        var attributeName = dynamicAttribute.substring(0, dynamicAttribute.indexOf("="));
        var actualAttributeName = getActualAttributeName(attributeName);

        var attributeValue = dynamicAttribute.substring(dynamicAttribute.indexOf("=") + 1)
                .replace("\"", "")
                .trim();

        var code = Code.parse(attributeValue);

        templateClass.appendPlainHtml(actualAttributeName + " = \"", false, false);
        templateClass.addCode("writer.append(content(() -> String.valueOf(" + code + ")));");
        templateClass.appendPlainHtml("\" ", false, false);

        var remainingPart = htmlString.substring(matcher.end());

        processDynamicAttributes(templateClass, remainingPart);

    }

    private String getActualAttributeName(String attributeName) {
        return StringUtils.removeStartIgnoreCase(attributeName, DYNAMIC_ATTRIBUTE_START)
                .trim();
    }
}
