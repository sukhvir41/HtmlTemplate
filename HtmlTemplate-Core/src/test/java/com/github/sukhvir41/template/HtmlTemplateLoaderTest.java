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

package com.github.sukhvir41.template;

import com.github.sukhvir41.TestUtils;
import com.github.sukhvir41.core.settings.SettingsManager;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HtmlTemplateLoaderTest {

    @Test
    public void loadNonexistentFile() {
        try {
            HtmlTemplateLoader.load(Paths.get("test.html"));
        } catch (Exception e) {
            Assert.assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void shouldReturnTemplate() throws URISyntaxException {
        Path file = TestUtils.getFile("SimpleTest1.html");
        HtmlTemplate htmlTemplate = HtmlTemplateLoader.load(file);
        Assert.assertNotNull(htmlTemplate);
    }


}
