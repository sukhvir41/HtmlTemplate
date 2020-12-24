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
import java.util.List;

import static com.github.sukhvir41.TestUtils.strip;

public class ForEachTestTest {

    private String fileName = "ForEachTest.html";
    private String className = "ForEachTest";
    private String testName = "ForEachTest";

    private String[] names = {"SAM", "DEAN"};
    private List<Integer> ages = List.of(1, 2);
    private int[] counters = new int[]{1, 2};

   // @Test
    public void testMethod() throws URISyntaxException {

        var file = TestUtils.getFile(fileName);
        var htmlTemplate = new TemplateGenerator();

        var classString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile(className, classString);

        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("names", new Object[]{names});
        instance.call("ages", ages);
        instance.call("counters", new Object[]{counters});
        instance.call("render", writer);

        var output = strip(writer.toString());
        // can't test for imports as testing through reflection is not possible. So, if this compiles then assuming
        // everything went fine
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
            "<h1>looping names</h1>\n" +
            "<h1 >\n" +
            "    SAM\n" +
            "</h1>\n" +
            "<h1 >\n" +
            "    DEAN\n" +
            "</h1>\n" +
            "\n" +
            "<h1>looping names with index </h1>\n" +
            "<h1>\n" +
            "    SAM,0\n" +
            "</h1>\n" +
            "<h1>\n" +
            "    DEAN,1\n" +
            "</h1>\n" +
            "\n" +
            "<h1> looping ages</h1>\n" +
            "<h1 >\n" +
            "    1\n" +
            "</h1>\n" +
            "<h1 >\n" +
            "    2\n" +
            "</h1>\n" +
            "\n" +
            "<h1> looping ages with index</h1>\n" +
            "<h1 >\n" +
            "    1,0\n" +
            "</h1>\n" +
            "<h1 >\n" +
            "    2,1\n" +
            "</h1>\n" +
            "\n" +
            "<h1>looping counter</h1>\n" +
            "\n" +
            "<h1 >\n" +
            "    1\n" +
            "</h1>\n" +
            "<h1 >\n" +
            "    2\n" +
            "</h1>\n" +
            "\n" +
            "<h1>looping counter with index</h1>\n" +
            "\n" +
            "<h1 >\n" +
            "    1,0\n" +
            "</h1>\n" +
            "<h1 >\n" +
            "    2,1\n" +
            "</h1>\n" +
            "\n" +
            "</body>\n" +
            "</html>";


}
