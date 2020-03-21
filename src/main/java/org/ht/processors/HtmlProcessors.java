package org.ht.processors;

public enum HtmlProcessors implements HtmlProcessor {
    REGULAR(new RegularHtmlProcessor()),
    SCRIPT(new ScriptProcessingMode()),
    COMMENT(new CommentProcessingMode()),
    STYLE(new StyleProcessingMode());

    private HtmlProcessor processor;

    HtmlProcessors(HtmlProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void process(HtmlProcessorData data) {
        processor.process(data);
    }
}
