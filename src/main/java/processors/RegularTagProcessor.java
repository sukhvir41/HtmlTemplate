package processors;

import template.*;

class RegularTagProcessor implements Processor {

    @Override
    public void process(String html, TemplateClass templateClass, HtmlTemplate template) {

        template.setProcessor(HtmlProcessors.REGULAR);

        if (HtmlUtils.containsHtmlComment(html)) {
            HtmlProcessors.COMMENT.process(html, templateClass, template);
        } else if (StyleTagProcessor.containsStyleTag(html)) {
            HtmlProcessors.STYLE.process(html, templateClass, template);
        } else if (ScriptTagProcessor.containsScriptTag(html)) {
            HtmlProcessors.SCRIPT.process(html, templateClass, template);
        } else {
            processRegularTag(html, templateClass, template);
        }
    }

    private void processRegularTag(String html, TemplateClass templateClass, HtmlTemplate template) {
        String[] lineParts = html.split(">");
        for (var tagString : lineParts) {
            processHtmlTag(tagString, templateClass, template);
        }
    }

    private void processHtmlTag(String tagString, TemplateClass templateClass, HtmlTemplate template) {
        if (!tagString.isBlank()) {

            var isDocTypeTag = HtmlTag.parse(tagString)
                    .map(HtmlUtils::isDocTypeTag)
                    .orElse(false);

            if (isDocTypeTag) {
                templateClass.appendString(tagString + ">");
                return;
            }

            Content.parseContent(tagString)
                    .ifPresent(templateClass::appendContent);

            HtmlTag.parse(tagString)
                    .ifPresent(template::addOrRemoveHtmlTagFromStack);
        }
    }
}
