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

package com.github.sukhvir41.parsers;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class CodeTest {

    @Test
    public void simpleTest() {
        var parsedFunctionCode = Code.parseForFunction("@name.contains(\"@someDomain.com\")");
        Assert.assertEquals("name().contains(\"@someDomain.com\")", parsedFunctionCode);

        var parsedVariableCode = Code.parseForVariable("@name.contains(\"@someDomain.com\")");
        Assert.assertEquals("name.contains(\"@someDomain.com\")", parsedVariableCode);
    }


    @Test
    public void escapedTest() {
        var parsedFunctionCode = Code.parseForFunction("@name.contains(\"\\\"@someDomain.com\\\"\")");
        Assert.assertEquals("name().contains(\"\\\"@someDomain.com\\\"\")", parsedFunctionCode);

        var parsedVariableCode = Code.parseForVariable("@name.contains(\"\\\"@someDomain.com\\\"\")");
        Assert.assertEquals("name.contains(\"\\\"@someDomain.com\\\"\")", parsedVariableCode);
    }

    @Test
    public void sameNameVariableTest() {
        var parsedFunctionCode = Code.parseForFunction("@name.contains(\"@name.com\") + @name");
        Assert.assertEquals("name().contains(\"@name.com\") + name()", parsedFunctionCode);

        var parsedVariableCode = Code.parseForVariable("@name.contains(\"@name.com\") + @name");
        Assert.assertEquals("name.contains(\"@name.com\") + name", parsedVariableCode);
    }

    @Test
    public void sameNameVariableRepeatTest() {
        var parsedFunctionCode = Code.parseForFunction("@name.contains(\"@name.com\") + @name + @newName + @name + @newName + @name.contains(\"@name.com\")");
        Assert.assertEquals("name().contains(\"@name.com\") + name() + newName() + name() + newName() + name().contains(\"@name.com\")", parsedFunctionCode);

        var parsedVariableCode = Code.parseForVariable("@name.contains(\"@name.com\") + @name + @newName + @name + @newName + @name.contains(\"@name.com\")");
        Assert.assertEquals("name.contains(\"@name.com\") + name + newName + name + newName + name.contains(\"@name.com\")", parsedVariableCode);
    }

    @Test
    public void codePartsSimpleTest() {
        String theCode = "int , age , String , name";
        List<String> codeParts = List.of("int", "age", "String", "name");
        Assert.assertEquals(codeParts, Code.getCodeParts(theCode, ","));
    }

    @Test
    public void codePartsSeparatorInCodeTest() {
        String theCode = "int , age , boolean , name.contains(\",\")";
        List<String> codeParts = List.of("int", "age", "boolean", "name.contains(\",\")");
        Assert.assertEquals(codeParts, Code.getCodeParts(theCode, ","));
    }

    @Test
    public void codePartsSeparatorWithInStringInString() {
        String theCode = "boolean , name.contains(\"\\\",\\\"\") ,int , age ";
        List<String> codeParts = List.of("boolean", "name.contains(\"\\\",\\\"\")", "int", "age");
        Assert.assertEquals(codeParts, Code.getCodeParts(theCode, ","));
    }

    @Test
    public void codePartsSeparatorWithCode() {
        String theCode = "boolean , name.substring(@start,@end) ,int , age ";
        List<String> codeParts = List.of("boolean", "name.substring(@start,@end)", "int", "age");
        Assert.assertEquals(codeParts, Code.getCodeParts(theCode, ","));

        String theCode1 = "boolean , name.substring(@start,@end,@something) ,int , age ";
        List<String> codeParts1 = List.of("boolean", "name.substring(@start,@end,@something)", "int", "age");
        Assert.assertEquals(codeParts1, Code.getCodeParts(theCode1, ","));

        String theCode2 = "boolean , name.someFunction((@start.substring(@a,@b)),@end,@something) ,int , age ";
        List<String> codeParts2 = List.of("boolean", "name.someFunction((@start.substring(@a,@b)),@end,@something)", "int", "age");
        Assert.assertEquals(codeParts2, Code.getCodeParts(theCode2, ","));

        String theCode3 = "int , age , boolean , name.someFunction(\",\",@testVariable), boolean, name.contains(\"\\\",\\\"\") , int , number.precision(1,3) ";
        List<String> codeParts3 = List.of("int", "age", "boolean", "name.someFunction(\",\",@testVariable)", "boolean", "name.contains(\"\\\",\\\"\")", "int", "number.precision(1,3)");
        Assert.assertEquals(codeParts3, Code.getCodeParts(theCode3, ","));
    }


}
