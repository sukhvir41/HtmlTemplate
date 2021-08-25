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
import com.github.sukhvir41.template.HtmlTemplate;
import com.github.sukhvir41.template.HtmlTemplateLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.util.Map;

import static com.github.sukhvir41.TestUtils.strip;

public class HtmlTemplateIncludeTest {

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
            "<h1>10: the number</h1>\n" +
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
    public void testRuntimeIncludeTemplate() throws URISyntaxException {
        var file = TestUtils.getFile("HtTemplateTest.html");

        HtmlTemplate template = HtmlTemplateLoader.load(file);

        Map<String, Object> params = Map.of("number", 10);

        var output = strip(template.render(params));

        Assert.assertEquals("Ht-template test", TestUtils.strip(expectedOutPut), output);

    }

    public String expectedOutPutVariables = "" +
            "<html>" +
            "<h1>" +
            "   Main template" +
            "</h1>" +
            "<h1>" +
            "   we are inside the imported template file" +
            "</h1>" +
            "<h1>" +
            "   11: the number" +
            "</h1>" +
            "<div>" +
            "   test template" +
            "</div>" +
            "</html>";

    @Test
    public void testRuntimeIncludeTemplateWithVariables() throws URISyntaxException {
        var file = TestUtils.getFile("HtIncludeTemplateVariables.html");

        HtmlTemplate template = HtmlTemplateLoader.load(file);

        var output = strip(template.render(Map.of("number", 11)));

        Assert.assertEquals("Ht-template test", TestUtils.strip(expectedOutPutVariables), output);

    }
}
