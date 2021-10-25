package com.github.sukhvir41.tags;

import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;

public class DummyContent implements HtmlTag{
    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {

    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {

    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public boolean isClosingTag() {
        return true;
    }

    @Override
    public boolean isSelfClosing() {
        return true;
    }

    @Override
    public boolean isDocTypeTag() {
        return false;
    }
}
