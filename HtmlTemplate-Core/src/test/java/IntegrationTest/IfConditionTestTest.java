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

public class IfConditionTestTest {
    //@Test
    public void testTrue() throws URISyntaxException {

        var file = TestUtils.getFile("IfConditionTest.html");

        var htmlTemplate = new TemplateGenerator();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile("IfConditionTest", theClassString);


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("isThisTrue", true);
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("If condition test true", output, TestUtils.strip(getExpectedOutputTrue()));

    }


    String getExpectedOutputTrue() {
        return "<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<h1> before if condition </h1>\n" +
                "<div>\n" +
                "    <h1> inside if condition </h1>\n" +
                "</div>\n" +
                "\n" +
                "<h1> after if condition </h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

    }

   // @Test
    public void testFalse() throws URISyntaxException {

        var file = TestUtils.getFile("IfConditionTest.html");

        var htmlTemplate = new TemplateGenerator();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile("IfConditionTest", theClassString);


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("isThisTrue", false);
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("If condition test false", TestUtils.strip(getExpectedOutputFalse()), output);

    }

    String getExpectedOutputFalse() {
        return "<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<h1> before if condition </h1>\n" +
                "<h1> after if condition </h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

    }

}
