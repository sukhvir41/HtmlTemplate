package processors;

import template.TemplateClass;

public enum HtmlProcessors {
    REGULAR(new RegularTagProcessor()),
    SCRIPT(new ScriptTagProcessor()),
    COMMENT(new CommentTagProcessor()),
    STYLE(new StyleTagProcessor());

    private Processor processor;

    HtmlProcessors(Processor processor) {
        this.processor = processor;
    }

    public void process(String html, TemplateClass templateClass) {
        processor.process(html, templateClass);
    }

}