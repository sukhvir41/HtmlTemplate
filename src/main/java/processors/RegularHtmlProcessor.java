package processors;

import org.apache.commons.lang3.StringUtils;
import tags.HtmlTag;
import tags.HtmlTags;
import template.HtmlTemplate;

public class RegularHtmlProcessor implements HtmlProcessor {


    @Override
    public void process(HtmlProcessorData data) {

        if (StringUtils.isBlank(data.getHtml())) {
            return;
        }

        if (StringUtils.startsWith(data.getHtml(), "<!--")) {
            HtmlProcessors.COMMENT.process(data);
        } else {
            processRegularHtmlTag(data);
        }
    }

    private void processRegularHtmlTag(HtmlProcessorData data) {
        HtmlTags.parse(data.getHtml())
                .ifPresentOrElse(htmlTag -> processHtmlTag(htmlTag, data), () -> processHtmlContent(data));
    }

    private void processHtmlTag(HtmlTag htmlTag, HtmlProcessorData data) {
        if (htmlTag.isSelfClosing() || htmlTag.isVoidTag()) {
            processSelfClosingTag(htmlTag, data);
        } else if (htmlTag.isClosingTag()) {
            processClosingTag(htmlTag, data);
        } else {
            processOpeningTag(htmlTag, data);
        }
    }

    private void processOpeningTag(HtmlTag htmlTag, HtmlProcessorData data) {

        htmlTag.processOpeningTag(data.getTemplateClass());

        data.getTemplateClass()
                .appendHtmlTag(htmlTag);

        data.getHtmlTemplate()
                .addToTagStack(htmlTag);

        changeProcessor(htmlTag.getName(), data.getHtmlTemplate());

    }

    private void changeProcessor(String tagName, HtmlTemplate htmlTemplate) {

        if (tagName.equalsIgnoreCase("script")) {
            htmlTemplate.setProcessor(HtmlProcessors.SCRIPT);
        } else if (tagName.equalsIgnoreCase("style")) {
            htmlTemplate.setProcessor(HtmlProcessors.STYLE);
        }
    }

    private void processClosingTag(HtmlTag htmlTag, HtmlProcessorData data) {
        var template = data.getHtmlTemplate();

        var openingHtmlTag = template.peekTagStack()
                .orElseThrow();

        if (openingHtmlTag.isClosingTag(htmlTag)) {

            data.getTemplateClass()
                    .appendHtmlTag(htmlTag);

            openingHtmlTag.processClosingTag(data.getTemplateClass());

            template.removeFromTagStack();


        } else {
            throw new IllegalStateException("Miss matched closing tag. Tags may not be closed in the right order " + htmlTag.getName());
        }


    }

    private void processSelfClosingTag(HtmlTag htmlTag, HtmlProcessorData data) {
        var templateClass = data.getTemplateClass();

        htmlTag.processOpeningTag(templateClass);
        templateClass.appendHtmlTag(htmlTag);
        htmlTag.processClosingTag(templateClass);
    }


    private void processHtmlContent(HtmlProcessorData data) {

    }
}
