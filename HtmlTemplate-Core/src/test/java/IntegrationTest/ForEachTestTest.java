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
import com.github.sukhvir41.core.settings.SettingsManager;
import com.github.sukhvir41.template.HtmlTemplateLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.sukhvir41.TestUtils.strip;

public class ForEachTestTest {

    private String fileName = "ForEachTest.html";
    private String testName = "ForEachTest";

    @Test
    public void testMethod() throws URISyntaxException {
        String[] names = {"SAM", "DEAN"};
        List<Integer> ages = new ArrayList<>();
        ages.add(1);
        ages.add(2);
        int[] counters = new int[]{1, 2};

        var file = TestUtils.getFile(fileName);

        var output = strip(
                HtmlTemplateLoader.load(file)
                        .render(Map.of("names", names, "ages", (List<Integer>) ages, "counters", counters))
        );

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
