package OldImpl;

import OldImpl.CommentTagProcessor;
import OldImpl.Processor;
import processors.HtmlProcessors;
import processors.ScriptTagProcessor;
import processors.StyleTagProcessor;
import tags.HtmlTag;
import tags.HtmlTags;
import template.Content;
import template.HtmlTemplate;
import template.TemplateClass;

class RegularTagProcessor implements Processor {


    /**
     * starting point to process the raw HTML. Here the decision is made to which processor the raw html needs to be passed
     *
     * @param html          raw HTML
     * @param templateClass Template class that will make the .java file out of the HTML
     * @param template      HTML templater of the File being passed
     */
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
            processRegularTag(html, templateClass, template);
        }
    }

    /**
     * splits the raw single line HTML to respective html tags and parses each of them
     *
     * @param html          raw single Line HTML
     * @param templateClass Template class that will make the .java file out of the HTML
     * @param template      HTML templater of the File being passed
     */
    private void processRegularTag(String html, TemplateClass templateClass, HtmlTemplate template) {

        String[] lineParts = html.split(">");
        for (var tagString : lineParts) {
            if (!tagString.isBlank()) {
                processIndividualHtmlTag(tagString, templateClass, template);
            }
        }


    }

    /**
     * checks if the tags are closed or not.<br>
     * eg:<br>
     * "&lt;sdfsdf &gt;" -> will return true <br>
     * "&lt;dsfdf"    -> will return false <br>
     * "&lt;a&gt;&lt;/a"    -> wil return false <br>
     * TLDR; checks to see of the there are equal number of "&lt;" and  "&gt;"<br>
     *
     * @param htmlString html string to check
     * @return true if there  equal number of "&lt;" and  "&gt;"
     */
    private boolean allTagsClosed(String htmlString) {
        int lessThenCount = 0;
        int greaterThanCount = 0;

        for (int i = 0; i < htmlString.length(); i++) {
            if (htmlString.charAt(i) == '<') {
                ++lessThenCount;
            } else if (htmlString.charAt(i) == '>') {
                ++greaterThanCount;
            }
        }
        return lessThenCount == greaterThanCount;
    }

    /**
     * parses a single html tag ie tries to convert the sting html to and HTML object
     *
     * @param tagString     string of a single html tag
     * @param templateClass Template class that will make the .java file out of the HTML
     * @param template      HTML templater of the File being passed
     */
    private void processIndividualHtmlTag(String tagString, TemplateClass templateClass, HtmlTemplate template) {
        var isDocTypeTag = HtmlTags.parse(tagString)
                .map(this::isDocTypeTag)
                .orElse(false);

        if (isDocTypeTag) {
            templateClass.appendString(tagString + ">");
            return;
        }

        Content.parseContent(tagString)
                .ifPresent(templateClass::appendContent);


        HtmlTags.parse(tagString)
                .ifPresent(htmlTag -> processHtmlTag(htmlTag, templateClass, template));

    }

    private boolean isDocTypeTag(HtmlTag htmlTag) {
        return htmlTag.getName()
                .equalsIgnoreCase("!DOCTYPE");
    }


    public void processHtmlTag(HtmlTag htmlTag, TemplateClass templateClass, HtmlTemplate template) {

        if (htmlTag.isClosingTag()) {
            if (htmlTag.isSelfClosing()) {

                htmlTag.processOpeningTag(templateClass);
                templateClass.appendHtmlTag(htmlTag);
                htmlTag.processClosingTag(templateClass);

            } else {

                template.peekTagStack()
                        .filter(tag -> tag.isClosingTag(htmlTag))
                        .ifPresentOrElse(tag -> {
                                    template.removeFromTagStack();
                                    tag.processClosingTag(templateClass);
                                },
                                () -> throwMissMatchClosingTagException(htmlTag, template));

                templateClass.appendHtmlTag(htmlTag);

            }

        } else {
            htmlTag.processOpeningTag(templateClass);
            templateClass.appendHtmlTag(htmlTag);
            template.addToTagStack(htmlTag);
        }

    }

    private void throwMissMatchClosingTagException(HtmlTag htmlTag, HtmlTemplate template) {
        throw new RuntimeException("Miss matched closing tag " + htmlTag.getName() + " compared with " +
                                   template.peekTagStack()
                                           .map(HtmlTag::getName)
                                           .orElse("Empty Tag stack"));
    }

}
