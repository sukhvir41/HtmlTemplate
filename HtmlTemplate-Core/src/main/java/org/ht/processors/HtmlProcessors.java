package org.ht.processors;

public enum HtmlProcessors implements HtmlProcessor {
    REGULAR(new RegularHtmlProcessor()),
    SCRIPT(new ScriptLineProcessingMode()),
    COMMENT(new CommentLineProcessingMode()),
    STYLE(new StyleLineProcessingMode());

    private HtmlProcessor processor;

    HtmlProcessors(HtmlProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void process(HtmlProcessorData data) {
        processor.process(data);
    }
}
