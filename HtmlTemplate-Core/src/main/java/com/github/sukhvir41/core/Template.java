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

import com.github.sukhvir41.parsers.htmlParsers.HtmlParserData;
import com.github.sukhvir41.parsers.htmlParsers.HtmlParsers;
import com.github.sukhvir41.tags.HtmlTag;
import com.github.sukhvir41.utils.LogManager;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;

public abstract class Template {

    private final Path file;
    private int depth;
    private String packageName;
    private String className;

    private TemplateClassGenerator classGenerator;
    private HtmlParsers htmlParser = HtmlParsers.TAG;

    protected Template(Path file, String packageName, String className, TemplateClassGenerator classGenerator) {
        this.file = file;
        this.classGenerator = classGenerator;
        this.packageName = packageName;
        this.className = className;
        this.depth = 0;
    }

    protected Template(Path file, TemplateClassGenerator classGenerator, int depth) {
        this.file = file;
        this.classGenerator = classGenerator;
        this.depth = depth;
    }

    public void readAndProcessTemplateFile() {
        LogManager.getLogger()
                .info("reading template file");
        TemplateReader.read(file, (section) -> {
            if (StringUtils.isNotBlank(section)) {
                LogManager.getLogger()
                        .fine("process section: " + section);
                processSection(section);
            }
        });
    }

    private void processSection(String section) {
        htmlParser.parse(
                HtmlParserData.builder()
                        .setSection(section)
                        .setClassGenerator(this.classGenerator)
                        .setTemplate(this)
                        .build()
        );
    }

    public final String render() {
        return this.classGenerator.render();
    }

    public final String getFullClassName() {
        return packageName + "." + className;
    }

    public final void setHtmlParser(HtmlParsers htmlParser) {
        this.htmlParser = htmlParser;
    }

    void setDepth(int depth) {
        this.depth = depth;
    }

    public final TemplateClassGenerator getClassGenerator() {
        return this.classGenerator;
    }

    public final int getDepth() {
        return this.depth;
    }

    public final Path getFile() {
        return this.file;
    }

    public boolean shouldAppendScript() {
        return true;
    }

    public boolean shouldAppendStyle() {
        return true;
    }

    public boolean shouldAppendComment() {
        return true;
    }

    public abstract HtmlTag parseSection(String section);

}
