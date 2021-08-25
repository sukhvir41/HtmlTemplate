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
import java.util.Map;

public class IfElseConditionTestTest {

    @Test
    public void testTrue() throws URISyntaxException {

        var file = TestUtils.getFile("IfElseConditionTest.html");

        var output = TestUtils.strip(
                HtmlTemplateLoader.load(file)
                        .render(Map.of("show", true))
        );

        Assert.assertEquals("If condition test true", TestUtils.strip(getExpectedOutputTrue()), output);

    }


    String getExpectedOutputTrue() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>\n" +
                "    this is before if else\n" +
                "</h1>\n" +
                "\n" +
                "<h1>\n" +
                "    inside if statement\n" +
                "</h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

    }

    @Test
    public void testFalse() throws URISyntaxException {

        var file = TestUtils.getFile("IfElseConditionTest.html");

        var output = TestUtils.strip(
                HtmlTemplateLoader.load(file)
                        .render(Map.of("show", false))
        );

        Assert.assertEquals("If condition test false", output, TestUtils.strip(getExpectedOutputFalse()));

    }

    String getExpectedOutputFalse() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>\n" +
                "    this is before if else\n" +
                "</h1>\n" +
                "\n" +
                "<h1>\n" +
                "    inside else statement\n" +
                "</h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

    }
}
