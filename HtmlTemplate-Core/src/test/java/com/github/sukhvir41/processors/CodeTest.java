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

package com.github.sukhvir41.processors;

import org.junit.Assert;
import org.junit.Test;

public class CodeTest {

    @Test
    public void simpleTest() {
        var parsedCode = Code.parse("@name.contains(\"@someDomain.com\")");
        Assert.assertEquals("name().contains(\"@someDomain.com\")", parsedCode);
    }


    @Test
    public void escapedTest() {
        var parsedCode = Code.parse("@name.contains(\"\\\"@someDomain.com\\\"\")");
        Assert.assertEquals("name().contains(\"\\\"@someDomain.com\\\"\")", parsedCode);
    }
}
