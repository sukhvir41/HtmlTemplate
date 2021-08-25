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

import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.core.template.TemplateFactory;
import com.github.sukhvir41.core.template.TemplateType;
import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.settings.SettingsManager;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.Map;

import static com.github.sukhvir41.TestUtils.getFile;
import static com.github.sukhvir41.TestUtils.strip;

public class CompileTmeIncludeTemplateTest {

    private String subTemplateExpectedString = "" +
            "\n" +
            "<h1> we are inside the imported template file</h1>\n" +
            "<h1>10: the number</h1>\n" +
            "<div>\n" +
            "    test template\n" +
            "</div>";


    @Test
    public void testInclude() throws URISyntaxException {

        var settings = SettingsManager.load(Map.of(
                SettingOptions.ROOT_FOLDER,
                getFile("importTemplate.html").getParent().getParent().getParent()));

        Template template = TemplateFactory
                .getTemplate(getFile("importTemplate.html"), TemplateType.COMPILE_TIME, "test", settings);
        template.readAndProcessTemplateFile();

        String subTemplateClassString = template.render();

        Reflect subTemplateClass = Reflect.compile("test.importTemplate", subTemplateClassString);

        String subTemplateString = subTemplateClass.call("getInstance")
                .call("number", 10)
                .call("render")
                .get();

        Assert.assertEquals(strip(subTemplateExpectedString), strip(subTemplateString));

        Template mainTemplate = TemplateFactory
                .getTemplate(getFile("HtIncludeTest.html"), TemplateType.COMPILE_TIME, "test", settings);
        mainTemplate.readAndProcessTemplateFile();

        String mainTemplateClassString = mainTemplate.render();

        System.out.println(mainTemplateClassString);

        Assert.fail();
    }

    @Test
    public void shouldThrowErrorForFileOutsideRootFolder() throws URISyntaxException {
        try {
            var settings = SettingsManager.load(Map.of(
                    SettingOptions.ROOT_FOLDER,
                    getFile("")));

            Template template = TemplateFactory
                    .getTemplate(getFile("importTemplate.html"), TemplateType.COMPILE_TIME, "test", settings);

            Assert.fail("Should fail as the file path as out side the root folder");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }

    }


}
