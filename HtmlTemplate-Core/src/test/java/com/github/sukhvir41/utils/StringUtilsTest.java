/*
 * Copyright 2020 Sukhvir Thapar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.sukhvir41.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void findIndexTest() {
        Assert.assertEquals(5, StringUtils.findIndex(0, "c", "\"abc\"c"));
        Assert.assertEquals(7, StringUtils.findIndex(0, "c", "\"ab\\\"c\"c"));
        Assert.assertEquals(15, StringUtils.findIndex(0, "c", "\"abc\\\"abc\\\"abc\"c"));
    }

    @Test
    public void getClassNameFromFileTest() {
        Assert.assertEquals("test_test", StringUtils.getClassNameFromFile("test-test.html"));
        Assert.assertEquals("testtest1", StringUtils.getClassNameFromFile("test test1.html"));
        Assert.assertEquals("testtest1_test1", StringUtils.getClassNameFromFile("test test1-test1.html"));
    }


}
