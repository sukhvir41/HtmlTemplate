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

package com.github.sukhvir41.core;

import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.settings.Settings;
import com.github.sukhvir41.core.settings.SettingsManager;
import com.github.sukhvir41.core.template.CompileTimeTemplate;
import com.github.sukhvir41.core.template.RuntimeTemplate;
import com.github.sukhvir41.core.template.TemplateFactory;
import com.github.sukhvir41.core.template.TemplateType;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class TemplateFactoryTest {

    @Test
    public void allNullInputs() {
        try {
            TemplateFactory.getTemplate(null, null, null);
        } catch (Exception e) {
            Assert.assertEquals("Please provide template file", e.getMessage());
            Assert.assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void nonExistingFile() {
        try {
            TemplateFactory.getTemplate(Paths.get("someFile.html"), null, null);
        } catch (Exception e) {
            Assert.assertEquals("Template file not found : someFile.html", e.getMessage());
            Assert.assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void nullTemplateType() {
        try {
            var tempFile = Files.createTempFile("", "test.html");
            TemplateFactory.getTemplate(tempFile, null, null);
        } catch (Exception e) {
            Assert.assertEquals("Please provide template type", e.getMessage());
            Assert.assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void nullSettings() {
        try {
            var tempFile = Files.createTempFile("", "test.html");
            TemplateFactory.getTemplate(tempFile, TemplateType.RUN_TIME, null);
        } catch (Exception e) {
            Assert.assertEquals("Please provide settings", e.getMessage());
            Assert.assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }


    @Test
    public void runTimeTemplateType() throws IOException {
        try {
            var tempFile = Files.createTempFile("", "test.html");
            var template = TemplateFactory.getTemplate(tempFile, TemplateType.RUN_TIME, SettingsManager.load());

            Assert.assertEquals(RuntimeTemplate.class, template.getClass());

        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("error in runtime template instantiation");
        }
    }

    @Test
    public void compileTimeTemplateType() throws IOException {
        try {
            var tempFile = Files.createTempFile("", "test.html");

            Settings settings = SettingsManager.load(Map.of(SettingOptions.ROOT_FOLDER, tempFile.getParent()));

            var template = TemplateFactory.getTemplate(tempFile, TemplateType.COMPILE_TIME, settings);

            Assert.assertEquals(CompileTimeTemplate.class, template.getClass());
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("error in compile time template instantiation");
        }
    }

}
