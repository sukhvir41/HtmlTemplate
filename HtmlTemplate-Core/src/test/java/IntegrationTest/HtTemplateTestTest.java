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

import com.github.sukhvir41.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

import static IntegrationTest.TestUtils.strip;

public class HtTemplateTestTest {

    private final String expectedOutPut = "" +
            "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h1>after this tag we will import a template</h1>\n" +
            "\n" +
            "<h1> we are inside the imported template file</h1>\n" +
            "\n" +
            "<div>\n" +
            "    test template\n" +
            "</div>" +
            "\n" +
            "<h1>this is after the import template</h1>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    @Test
    public void testTemplate() throws URISyntaxException {
        var file = TestUtils.getFile("HtTemplateTest.html");

        var theClass = Reflect.compile("HtTemplateTest",
                new HtmlTemplate().setTemplate(file)
                        .renderReflection()
        );

        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("render", writer);

        var output = strip(writer.toString());

        Assert.assertEquals("Ht-template test", TestUtils.strip(expectedOutPut), output);

    }


}
