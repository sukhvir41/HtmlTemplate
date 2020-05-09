package org.ht.utils;

import org.ht.utils.HtmlUtils;
import org.junit.Assert;
import org.junit.Test;

public class HtmlUtilsTest {

    @Test
    public void isHtmlStartTest() {
        var test1 = "hello  <h1>";
        Assert.assertFalse("contents start test ", HtmlUtils.isHtmlTagAtStart(test1));

        var test2 = "    < html>   sometext </html>";
        Assert.assertTrue("html tag at start test", HtmlUtils.isHtmlTagAtStart(test2));

        var test3 = " {{  < html >";
        Assert.assertFalse("dynamic content start 1 test", HtmlUtils.isHtmlTagAtStart(test3));

        var test4 = " {{{  < html >";
        Assert.assertFalse("dynamic content start 2 test", HtmlUtils.isHtmlTagAtStart(test4));

    }

    @Test
    public void getStartingHtmlTagNameTest() {
        var test1 = " some content <h1 > some heading </ h1>";
        try {
            var tagName = HtmlUtils.getStartingHtmlTagName(test1);
            Assert.fail("should throw an error as line does not start with a html tag. output " + tagName);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Line starting with content so, html tag name does not exist", true);
        }

        var test2 = " {{ \"<h1> test </h1>\" }}<h1 > some heading </ h1>";
        try {
            var tagName = HtmlUtils.getStartingHtmlTagName(test2);
            Assert.fail("should throw an error as line does not start with a html tag. output: " + tagName);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Line starting with dynamic content so, html tag name does not exist", true);
        }

        var test3 = " {{{ \"<h1> test </h1>\" }}}<h1 > some heading </ h1>";
        try {
            var tagName = HtmlUtils.getStartingHtmlTagName(test3);
            Assert.fail("should throw an error as line does not start with a html tag. output: " + tagName);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Line starting with dynamic content so, html tag name does not exist", true);
        }

        var test4 = "   <     h1 > some heading </ h1> <h2>";
        var tagName = HtmlUtils.getStartingHtmlTagName(test4);
        Assert.assertEquals("Starting html tag name", "h1", tagName);

        var test5 = "   <!DOCTYPE     html>  some heading </ html>";
        var tagName1 = HtmlUtils.getStartingHtmlTagName(test5);
        Assert.assertEquals("Starting html tag name", "html", tagName1);

        var test6 = "   <    !DOCTYPE     html>  some heading </ html>";
        var tagName2 = HtmlUtils.getStartingHtmlTagName(test6);
        Assert.assertEquals("Starting html tag name", "html", tagName2);

    }

    @Test
    public void isStartingTagDocTypeTest() {

        var test1 = " {{ <some code> }} <html>";
        Assert.assertFalse("starting with synamic Content", HtmlUtils.isStartingTagDocType(test1));

        var test2 = "  < !DOCTYPE html > some text </html>";
        Assert.assertTrue("starting with !DOCTYPE html tag ", HtmlUtils.isStartingTagDocType(test2));

        var test3 = "  < html > some text </html>";
        Assert.assertFalse("starting with !DOCTYPE html tag ", HtmlUtils.isStartingTagDocType(test3));
    }

    @Test
    public void isStartingTagClosingTagTest() {
        var test1 = " <h1> some heading </h1> ";
        Assert.assertFalse("opening h1 tag", HtmlUtils.isStartingTagClosingTag(test1));

        var test2 = " </   h1> some content ";
        Assert.assertTrue("opening h1 tag", HtmlUtils.isStartingTagClosingTag(test2));

        try {
            var test3 = " some content <h1> some heading </h1>";
            HtmlUtils.isStartingTagClosingTag(test3);
            Assert.fail("should throw an IllegalArgumentException as the line starts with html content");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void isMetaIncludeTagTest() {
        var test1 = " {{ <some code> }}";
        Assert.assertFalse("contains only dynamic content", HtmlUtils.isMetaIncludeTag(test1));

        var test2 = " < meta    ht-include =  \"some/link\"> {{ <some code> }}";
        Assert.assertTrue("contains only dynamic content", HtmlUtils.isMetaIncludeTag(test2));

        var test3 = " <meta    ht-include =  \"some/link\">";
        Assert.assertTrue("contains only dynamic content", HtmlUtils.isMetaIncludeTag(test3));
    }

}
