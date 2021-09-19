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

package com.github.sukhvir41.core.template;

import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.core.settings.Settings;
import com.github.sukhvir41.parsers.htmlParsers.HtmlParserData;
import com.github.sukhvir41.parsers.htmlParsers.HtmlParsers;
import com.github.sukhvir41.tags.HtmlTag;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.util.Objects;

public abstract class Template {

    private final Path file;
    private int depth;

    private final TemplateClassGenerator classGenerator;
    private final Settings settings;
    private HtmlParsers htmlParser = HtmlParsers.TAG;

    protected Template(Path file, TemplateClassGenerator classGenerator, Settings settings) {
        this.file = file;
        this.classGenerator = classGenerator;
        this.depth = 0;
        this.settings = settings;
    }

    protected Template(Path file, TemplateClassGenerator classGenerator, int depth, Settings settings) {
        this.file = file;
        this.classGenerator = classGenerator;
        this.depth = depth;
        this.settings = settings;
    }

    public void readAndProcessTemplateFile() {
        settings.getLogger()
                .info("reading template file");
        TemplateReader.read(file, (section) -> {
            if (StringUtils.isNotBlank(section)) {
                settings.getLogger()
                        .info("process section: " + section);
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

    public final void setHtmlParser(HtmlParsers htmlParser) {
        this.htmlParser = htmlParser;
    }

    public void setDepth(int depth) {
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

    public Settings getSettings() {
        return settings;
    }

    public abstract String getFullyQualifiedName();

    public abstract HtmlTag parseSection(String section);

    public abstract Template getRootTemplate();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;

        return Objects.equals(file, template.file);
    }

    @Override
    public int hashCode() {
        return file != null ? file.hashCode() : 0;
    }
}
