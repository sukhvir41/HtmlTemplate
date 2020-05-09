package org.ht.tags;

import org.ht.template.TemplateClass;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class IfHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> addCodeCapture;

    @Test
    public void test1() {
        TemplateClass templateClass = Mockito.mock(TemplateClass.class);

        IfHtmlTag ifHtmlTag = new IfHtmlTag("< h1 ht-if=\"@isTrue\">  ");

        ifHtmlTag.processOpeningTag(templateClass);
        Mockito.verify(templateClass)
                .addCode(addCodeCapture.capture());
        Mockito.verify(templateClass).incrementFunctionIndentation();
        assertEquals("if(condition( () -> isTrue() )){", addCodeCapture.getValue());

        ifHtmlTag.processClosingTag(templateClass);
        Mockito.verify(templateClass, Mockito.times(2))
                .addCode(addCodeCapture.capture());
        Mockito.verify(templateClass)
                .decrementFunctionIndentation();
        assertEquals("}", addCodeCapture.getValue());

    }

    @Test
    public void test2() {
        TemplateClass templateClass = Mockito.mock(TemplateClass.class);

        IfHtmlTag ifHtmlTag = new IfHtmlTag("< h1 ht-if=\"@string.indexOf('@test.com') > 10\">  ");

        ifHtmlTag.processOpeningTag(templateClass);
        Mockito.verify(templateClass)
                .addCode(addCodeCapture.capture());
        Mockito.verify(templateClass)
                .incrementFunctionIndentation();
        assertEquals("if(condition( () -> string().indexOf(\"@test.com\") > 10 )){", addCodeCapture.getValue());

        ifHtmlTag.processClosingTag(templateClass);
        Mockito.verify(templateClass, Mockito.times(2))
                .addCode(addCodeCapture.capture());
        Mockito.verify(templateClass)
                .decrementFunctionIndentation();
        assertEquals("}", addCodeCapture.getValue());

    }

}
