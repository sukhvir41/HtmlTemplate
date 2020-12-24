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
import com.github.sukhvir41.parsers.lineParser.LineParser;
import com.github.sukhvir41.utils.StringUtils;
import com.github.sukhvir41.utils.HtmlUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public final class TemplateGenerator {

    private final Deque<HtmlTag> tagsStack = new ArrayDeque<>();

    private ClassGenerator classGenerator;

    private HtmlParsers fileParser = HtmlParsers.TAG;

    private Path file;

    public void setFileParser(HtmlParsers fileParser) {
        this.fileParser = fileParser;
    }

    int getTagsStackSize() {
        return tagsStack.size();
    }

    public TemplateGenerator setTemplate(Path template) {
        this.file = template;
        var className = StringUtils.getClassNameFromFile(file.getFileName().toString());
        classGenerator = new ClassGenerator(className, this);
        return this;
    }

    public TemplateGenerator setTemplate(Path template, String packageName) {
        this.file = template;
        var className = StringUtils.getClassNameFromFile(file.getFileName().toString());
        classGenerator = new ClassGenerator(packageName, className, this);
        return this;
    }


    private ClassGenerator getClassGenerator() {
        return this.classGenerator;
    }

    public String render() {
        try {
            readFile(file);
            printIncompleteTags();
            return getClassGenerator().generateClass();
        } catch (IOException e) {
            return "";
        }
    }

    public String renderReflection() {
        try {
            readFile(file);
            printIncompleteTags();
            return getClassGenerator().generateReflectionClass();
        } catch (IOException e) {
            return "";
        }
    }

    private void readFile(Path file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            read(reader);
        }
    }


    private void printIncompleteTags() {
        if (tagsStack.size() > 0) {
            var htmlTag = tagsStack.pop();
            System.err.println("the tag " + htmlTag.getName() + " is left open");
        }
    }


    /**
     * starts reading the file and uses the specified processor to process the line.
     *
     * @throws IOException
     */
    private void read(BufferedReader reader) throws IOException {

//        LineParser lineParser = new LineParser();
//        String line;
//
//        while ((line = reader.readLine()) != null) {
//            lineParser.setLine(line);
//            while (lineParser.hasNextSection()) {
//                String section = lineParser.getNextSection()
//                        .trim();
//
//                if (fileParser == HtmlParsers.TAG) {
//                    if (HtmlUtils.isMetaIncludeTag(section)) {
//                        readTemplate(section);
//                    } else {
//                        processSection(section);
//                    }
//                } else {
//                    processSection(section);
//                }
//            }
//        }
    }

    private void readTemplate(String section) {
        Path filePath = Paths.get(getFilePathFromTemplateMetaTag(section));
        try {
            if (filePath.isAbsolute()) {
                readFile(filePath);
            } else {
                readFile(file.toAbsolutePath()
                        .getParent()
                        .resolve(filePath));
            }
        } catch (IOException e) {
            System.err.println("Problem in reading file \"" + filePath + "\" tag \"" + section + "\"");
            e.printStackTrace();
        }
    }


    private String getFilePathFromTemplateMetaTag(String section) {
//        try {
//            var matcher = HtmlUtils.INCLUDE_ATTRIBUTE_PATTERN.matcher(section);
//            if (matcher.find()) {
//                var htTemplateAttribute = section.substring(matcher.start(), matcher.end());
//                return htTemplateAttribute.substring(htTemplateAttribute.indexOf("\"") + 1, htTemplateAttribute.length() - 1);
//            } else return "";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }

        return null;


    }

    private void processSection(String section) {
//        fileParser.parse(
//                HtmlParserData.builder()
//                        .setHtml(section)
//                        .setClassGenerator(this.classGenerator)
//                        .setTemplateGenerator(this)
//                        .build()
//        );
    }


    public HtmlTag removeFromTagStack() {
        return this.tagsStack.removeLast();
    }

    public void addToTagStack(HtmlTag htmlTag) {
        this.tagsStack.add(htmlTag);
    }

    public Optional<HtmlTag> peekTagStack() {
        try {
            return Optional.ofNullable(this.tagsStack.peekLast());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
