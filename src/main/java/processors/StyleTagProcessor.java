package processors;

import template.HtmlTemplate;
import template.TemplateClass;

class StyleTagProcessor implements Processor {
    @Override
    public void process(String html, TemplateClass templateClass, HtmlTemplate template) {

        if (template.getProcessor() == HtmlProcessors.STYLE) {
            processMultiLineStyleTag();
        } else if (containsStyleTag(html)) {

        } else {

        }
    }

    private void processMultiLineStyleTag(String html) {

        if ()

    }

    private boolean containsClosingStyleTag(String html) {
        return html.contains("</script>");
    }


    private boolean containsStyleTag(String html) {
        return html.contains("<script");
    }


}
