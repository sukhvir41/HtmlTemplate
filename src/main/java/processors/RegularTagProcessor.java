package processors;

import template.Content;
import template.HtmlTag;
import template.HtmlTemplate;
import template.TemplateClass;

class RegularTagProcessor implements Processor {

    @Override
    public void process(String html, TemplateClass templateClass, HtmlTemplate template) {

        template.setProcessor(HtmlProcessors.REGULAR);

        if (CommentTagProcessor.containsHtmlComment(html)) {
            HtmlProcessors.COMMENT.process(html, templateClass, template);
        } else if (StyleTagProcessor.containsStyleTag(html)) {
            HtmlProcessors.STYLE.process(html, templateClass, template);
        } else if (ScriptTagProcessor.containsScriptTag(html)) {
            HtmlProcessors.SCRIPT.process(html, templateClass, template);
        } else {
            System.out.println("REGULAR HTML -> " + html);
            processRegularTag(html, templateClass, template);
        }
    }

    private void processRegularTag(String html, TemplateClass templateClass, HtmlTemplate template) {
        String[] lineParts = html.split(">");
        for (var tagString : lineParts) {
            if (!tagString.isBlank()) {
                processHtml(tagString, templateClass, template);
            }
        }
    }

    private void processHtml(String tagString, TemplateClass templateClass, HtmlTemplate template) {
        var isDocTypeTag = HtmlTag.parse(tagString)
                .map(this::isDocTypeTag)
                .orElse(false);

        if (isDocTypeTag) {
            templateClass.appendString(tagString + ">");
            return;
        }

        Content.parseContent(tagString)
                .ifPresent(templateClass::appendContent);


        HtmlTag.parse(tagString)
                .ifPresent(htmlTag -> processHtmlTag(htmlTag, templateClass, template));

    }

    private boolean isDocTypeTag(HtmlTag htmlTag) {
        return htmlTag.getName()
                .equalsIgnoreCase("!DOCTYPE");
    }


    public void processHtmlTag(HtmlTag htmlTag, TemplateClass templateClass, HtmlTemplate template) {

        //System.out.println("PROCESSED HTML TAG -> " + htmlTag.toString());

        if (htmlTag.isSelfClosing()) {
            // System.out.println("IS SELF CLOSING ");
            templateClass.appendHtmlTag(htmlTag);
        } else if (htmlTag.isClosingTag()) {

            template.peekTagStack()
                    .filter(tag -> tag.isClosingTag(htmlTag))
                    .ifPresentOrElse(tag -> template.removeFromTagStack(),
                            () -> {
                                throw new RuntimeException("Miss matched closing tag " + htmlTag.getName() + " compared with " + template.peekTagStack().get());
                            });

            templateClass.appendHtmlTag(htmlTag);

        } else {
            templateClass.appendHtmlTag(htmlTag);
            template.addToTagStack(htmlTag);
        }
    }
}
