package com.github.sukhvir41.tags;

import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.parsers.Code;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

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
        IncludeHtmlTag includeHtmlTag = new RuntimeIncludeHtmlTag(htmlString, Code::parseForFunction, template);

        Assert.assertEquals("path/to/file", includeHtmlTag.getFilePath());
    }

    @Test
    public void getPassedVariables() {
        String htmlString = "<meta ht-include=\"path/to/file\" ht-variables= \"test, @test, name, @name.subString(@start,@end)\" >";
        Template template = Mockito.mock(Template.class);
        IncludeHtmlTag includeHtmlTag = new RuntimeIncludeHtmlTag(htmlString, Code::parseForFunction, template);
        Map<String, String> passedVariables = includeHtmlTag.getPassedVariables();
        Map<String, String> expectedPassedVariables = Map.of(
                "test", "test()",
                "name", "name().subString(start(),end())"
        );

        Assert.assertEquals(expectedPassedVariables.size(), passedVariables.size());

        Assert.assertEquals(expectedPassedVariables, passedVariables);

    }

}
