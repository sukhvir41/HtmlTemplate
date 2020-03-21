package org.ht.processors;

import org.ht.template.HtmlTemplate;
import org.ht.template.TemplateClass;

public final class HtmlProcessorData {

    private final String html;
    private final TemplateClass templateClass;
    private final HtmlTemplate htmlTemplate;


    private HtmlProcessorData(String html, TemplateClass templateClass, HtmlTemplate htmlTemplate) {
        this.html = html;
        this.templateClass = templateClass;
        this.htmlTemplate = htmlTemplate;
    }


    public String getHtml() {
        return html;
    }

    public TemplateClass getTemplateClass() {
        return templateClass;
    }

    public HtmlTemplate getHtmlTemplate() {
        return htmlTemplate;
    }


    public static final Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String html;
        private TemplateClass templateClass;
        private HtmlTemplate htmlTemplate;


        public Builder setHtml(String html) {
            this.html = html;
            return this;
        }

        public Builder setTemplateClass(TemplateClass templateClass) {
            this.templateClass = templateClass;
            return this;
        }

        public Builder setHtmlTemplate(HtmlTemplate htmlTemplate) {
            this.htmlTemplate = htmlTemplate;
            return this;
        }

        public HtmlProcessorData build() {
            return new HtmlProcessorData(html, templateClass, htmlTemplate);
        }
    }

}
