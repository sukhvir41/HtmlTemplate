package com.github.sukhvir41.tags;

import com.github.sukhvir41.core.template.Template;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class IncludeHtmlTagTest {


    @Test
    public void matchesTest() {
        Assert.assertTrue(RuntimeIncludeHtmlTag.matches("<meta ht-include=\"path/to/file\">"));
        Assert.assertFalse(RuntimeIncludeHtmlTag.matches("<meta ht-if=\"@someCondition\"/>"));
    }

    @Test
    public void getFilePathTest() {
        String htmlString = "<meta ht-include=\"path/to/file\">";
        Template template = Mockito.mock(Template.class);
        IncludeHtmlTag includeHtmlTag = new RuntimeIncludeHtmlTag(htmlString, template);

        Assert.assertEquals("path/to/file", includeHtmlTag.getFilePath());
    }

}
