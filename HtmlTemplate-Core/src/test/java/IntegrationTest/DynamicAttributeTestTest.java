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

import static com.github.sukhvir41.TestUtils.strip;

public class DynamicAttributeTestTest {


    private String fileName = "DynamicAttributeTest.html";
    private String className = "DynamicAttributeTest";
    private String testName = "DynamicAttributeTest";

    //@Test
    public void testMethod() throws URISyntaxException {

        var file = TestUtils.getFile(fileName);
        var htmlTemplate = new TemplateGenerator();

        var classString = htmlTemplate.setTemplate(file)
                .renderReflection();
        var theClass = Reflect.compile(className, classString);

        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("isTrue", true);
        instance.call("render", writer);

        var output = strip(writer.toString());

        Assert.assertEquals(testName, strip(expectedOutput), output);

    }

    private String expectedOutput = "" +
            "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h1 isTrue=\"true\" isFalse=\"false\">\n" +
            "    dynamic attribute test\n" +
            "</h1>\n" +
            "\n" +
            "<h1 isTrue=\"true\" isFalse=\"false\">\n" +
            "    dynamic attribute test\n" +
            "</h1>\n" +
            "\n" +
            "<h1 isTrue=\"true\" isFalse=\"false\">\n" +
            "    dynamic attribute test\n" +
            "</h1>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

}
