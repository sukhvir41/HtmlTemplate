package org.ht.tags;

import org.ht.template.TemplateClass;

class RegularHtmlTag implements HtmlTag {

    protected String htmlString;

    public static boolean matches(String html) {
        throw new UnsupportedOperationException("not implemented by child");
    }

    protected RegularHtmlTag(String htmlString) {
        this.htmlString = htmlString.trim();

    }


    @Override
    public String getHtml() {
        return htmlString;
    }

    @Override
    public String getName() {
        return HtmlUtils.getStartingHtmlTagName(htmlString);
    }

    @Override
    public boolean isClosingTag() {
        return htmlString.charAt(1) == '/';
    }


    @Override
    public boolean isSelfClosing() {
        return htmlString.charAt(htmlString.length() - 2) == '/';
    }

    @Override
    public boolean isDocTypeTag() {
        return HtmlUtils.isStartingTagDocType(htmlString);
    }

    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        // does nothing
    }

    @Override
    public void processClosingTag(TemplateClass templateClass) {
        // does nothing
    }


}
