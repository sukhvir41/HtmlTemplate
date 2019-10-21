package processors;

import template.HtmlTemplate;
import template.TemplateClass;

public interface Processor {

    void process(String html, TemplateClass templateClass, HtmlTemplate template);

}
