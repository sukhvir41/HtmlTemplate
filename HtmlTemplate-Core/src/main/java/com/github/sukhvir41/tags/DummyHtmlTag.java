package com.github.sukhvir41.tags;

import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.template.Template;

public class DummyHtmlTag extends RegularHtmlTag {
    public DummyHtmlTag(String htmlString, Template instantiatingTemplate) {
        super(htmlString, instantiatingTemplate);
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {

    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {

    }
}
