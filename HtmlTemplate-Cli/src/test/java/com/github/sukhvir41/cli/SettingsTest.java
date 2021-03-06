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

package com.github.sukhvir41.cli;

import org.joor.Reflect;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class SettingsTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Rule
    public TemporaryFolder tempOutputFolder = new TemporaryFolder();


    @Test
    public void testFileTraversal() throws IOException, InterruptedException {

        var testFolder = Paths.get(tempFolder.newFolder("test").toURI());
        //creating files in test Folder
        Files.createFile(testFolder.resolve("test1.html"));
        Files.createFile(testFolder.resolve("test2.html"));
        //creating testFolder2 inside test folder
        var testFolder2 = Files.createDirectories(testFolder.resolve("testFolder2"));
        Files.createFile(testFolder2.resolve("test3.html"));
        Files.createFile(testFolder2.resolve("test4.html"));

        Set<String> ignoreFiles = new HashSet<>();
        ignoreFiles.add("testFolder2/test4.html");

        var settings = new Settings();
        Reflect.on(settings)
                .set("path", testFolder)
                .set("packageName", "")
                .set("verboseOutputRequested", true)
                .set("quiteOutputRequested", false)
                .set("filePattern", Pattern.compile("[\\s,\\S]*\\.html"))
                .set("filesToIgnore", ignoreFiles)
                .call("setLoggingLevel");

        assertEquals("test", settings.getPackageName());
        assertThat(settings.getTemplateFiles(), hasItems(testFolder.resolve("test1.html")));
        assertThat(settings.getTemplateFiles(), hasItems(testFolder.resolve("test2.html")));
        assertThat(settings.getTemplateFiles(), hasItems(testFolder2.resolve("test3.html")));
        assertThat(settings.getTemplateFiles(), not(testFolder2.resolve("test4.html")));
    }


}
