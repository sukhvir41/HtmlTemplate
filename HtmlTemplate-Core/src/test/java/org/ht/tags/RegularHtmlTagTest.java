package org.ht.tags;

import org.ht.template.TemplateClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;

public class RegularHtmlTagTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Captor
    private ArgumentCaptor<String> addPlainHtmlCapture;

    RegularHtmlTag regularHtmlTag = new RegularHtmlTag(" <h1 attribute = \"value\" >");

    @Test
    public void testOpeningAndClosing() {
        TemplateClass templateClass = Mockito.mock(TemplateClass.class);

        regularHtmlTag.processOpeningTag(templateClass);
        Mockito.verifyNoInteractions(templateClass);

        regularHtmlTag.processClosingTag(templateClass);
        Mockito.verifyNoInteractions(templateClass);
    }


    @Test
    public void testProcessTag() {
        TemplateClass templateClass = Mockito.mock(TemplateClass.class);

        regularHtmlTag.processTag(templateClass);
        Mockito.verify(templateClass)
                .appendPlainHtml(addPlainHtmlCapture.capture());
        assertEquals("<h1 attribute = \"value\" >", addPlainHtmlCapture.getValue());

    }

}
