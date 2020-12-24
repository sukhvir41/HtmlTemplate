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

package IntegrationTest;

import com.github.sukhvir41.TestUtils;
import com.github.sukhvir41.core.TemplateGenerator;
import org.joor.Reflect;
import org.junit.Assert;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

public class ElseIfConditionTestTest {
    //@Test
    public void testTrue() throws URISyntaxException {

        var file = TestUtils.getFile("ElseIfConditionTest.html");

        var htmlTemplate = new TemplateGenerator();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();


        var theClass = Reflect.compile("ElseIfConditionTest", theClassString);


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("showIf", true);
        instance.call("showElseIf", false);
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("Else If condition test if = true", TestUtils.strip(getExpectedOutputTrue()), output);

    }


    String getExpectedOutputTrue() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>\n" +
                "    if statement\n" +
                "</h1>\n" +
                "</body>\n" +
                "</html>";

    }

   // @Test
    public void testFalse() throws URISyntaxException {

        var file = TestUtils.getFile("ElseIfConditionTest.html");

        var htmlTemplate = new TemplateGenerator();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile("ElseIfConditionTest", theClassString);


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("showIf", false);
        instance.call("showElseIf", true);
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("ELse If condition test else if false", TestUtils.strip(getExpectedOutputFalse()), output);

    }

    String getExpectedOutputFalse() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>\n" +
                "    else if statement\n" +
                "</h1>\n" +
                "</body>\n" +
                "</html>";

    }

}
