package org.ht.utils;

import org.ht.utils.HtStringUtils;
import org.junit.Assert;
import org.junit.Test;

public class HtStringUtilsTest {

    @Test
    public void findIndexTest() {
        Assert.assertEquals(5, HtStringUtils.findIndex(0, "c", "\"abc\"c"));
        Assert.assertEquals(7, HtStringUtils.findIndex(0, "c", "\"ab\\\"c\"c"));
        Assert.assertEquals(15, HtStringUtils.findIndex(0, "c", "\"abc\\\"abc\\\"abc\"c"));
    }

    @Test
    public void getClassNameFromFileTest() {
        Assert.assertEquals("test_test",HtStringUtils.getClassNameFromFile("test-test.html"));
        Assert.assertEquals("testtest1",HtStringUtils.getClassNameFromFile("test test1.html"));
        Assert.assertEquals("testtest1_test1",HtStringUtils.getClassNameFromFile("test test1-test1.html"));
    }


}
