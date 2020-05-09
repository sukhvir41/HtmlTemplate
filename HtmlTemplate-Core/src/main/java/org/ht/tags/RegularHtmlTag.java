package org.ht.tags;

import org.ht.template.TemplateClass;
import org.ht.utils.HtmlUtils;

class RegularHtmlTag implements HtmlTag {

    protected String htmlString;

    protected RegularHtmlTag(String htmlString) {
        this.htmlString = htmlString.trim();

    }

    @Override
    public void processTag(TemplateClass templateClass) {
        templateClass.appendPlainHtml(this.htmlString);
    }

    @Override
    public String getName() {
        return HtmlUtils.getStartingHtmlTagName(this.htmlString);
    }

    @Override
    public boolean isClosingTag() {
        return this.htmlString.charAt(1) == '/';
    }


    @Override
    public boolean isSelfClosing() {
        return this.htmlString.charAt(this.htmlString.length() - 2) == '/';
    }

    @Override
    public boolean isDocTypeTag() {
        return HtmlUtils.isStartingTagDocType(this.htmlString);
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
