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

import static IntegrationTest.TestUtils.getFile;
import static IntegrationTest.TestUtils.strip;

public class SimpleTestTest {

    private final String expectedOutput = "" +
            "<html>\n" +
            "<head>\n" +
            "    <title>SimpleTest1</title>\n" +
            "\n" +
            "    <style>\n" +
            "    h1 {\n" +
            "        color: blue;\n" +
            "    }\n" +
            "    </style>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "\n" +
            "<h1>\n" +
            "    this is a simple test 1\n" +
            "</h1>\n" +
            "\n" +
            "<!--\n" +
            "    this is a comment <h1 org.ht-if = \"true\"> this h1 will not be processed </h1>\n" +
            "    <h1\n" +
            "    > this should also not be processed </h1>\n" +
            "-->\n" +
            "<!-- this is a comment <h1 org.ht-if = \"true\"> this h1 will\n" +
            "    not be processed </h1>\n" +
            "    <h1\n" +
            "    > this should also not be processed </h1>\n" +
            "-->\n" +
            "<a href=\"www.google.com\"> google link </a>\n" +
            "<!-- single line comment <h1> within single line comment </h1> -->\n" +
            "<h1>lef comment</h1> <!-- the comment <h1> the h1 in comment </h1> --> <h1>right of comment</h1>\n" +
            "\n" +
            "<script>\n" +
            "    // some script here  <h1> this should be processed as regular html </h2> made it h2 intentionally\n" +
            "       $('h1').click(function(){\n" +
            "        console.log('h1 clicked');\n" +
            "       });\n" +
            "\n" +
            "       $(\"h1\").append(\"<h1 org.ht-if = \"something\"> something </h1>\");\n" +
            "\n" +
            "</script>\n" +
            "\n" +
            "<!-- single line script tag -->\n" +
            "<script src=\"some/source\"></script>\n" +
            "\n" +
            "<!-- multi line script tag   -->\n" +
            "<script\n" +
            "        src=\"a very long path/test.js\">\n" +
            "</script>\n" +
            "\n" +
            "<link rel=\"stylesheet\"\n" +
            "      href=\"/a very long path/font-awesome.css\">\n" +
            "\n" +
            "<!-- single line style  -->\n" +
            "<style>h1 {color : blue; }</style>\n" +
            "\n" +
            "<!-- single line script -->\n" +
            "<script>  $(\"h1\").append(\"<h1 org.ht-if = \"somethingSecond\"> somethingSecond </h1>\"); </script>\n" +
            "\n" +
            "<!-- multi line src in script-->\n" +
            "<script src=\"a very long path\n" +
            "                /source\">\n" +
            "</script>\n" +
            "\n" +
            "<!-- multi line href link -->\n" +
            "<link rel=\"stylesheet\"\n" +
            "      href=\"/a very long\n" +
            "      path/font-awesome.css\">\n" +
            "\n" +
            "\n" +
            "\n" +
            "</body>\n" +
            "\n" +
            "</html>";


    @Test
    public void test() throws URISyntaxException {

        var file = getFile("SimpleTest1.html");


        var htmlTemplate = new HtmlTemplate();

        var classString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile("SimpleTest1", classString);

        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("render", writer);

        var output = strip(writer.toString());

        Assert.assertEquals("Simple test 1 ", strip(expectedOutput), output);
    }

}
