package com.github.sukhvir41.tags;

import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;

public class DummyHtmlTag implements HtmlTag {
    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {

    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isClosingTag() {
        return false;
    }

    @Override
    public boolean isSelfClosing() {
        return false;
    }

    @Override
    public boolean isDocTypeTag() {
        return false;
    }
}
