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

import com.github.sukhvir41.core.template.TemplateFactory;
import com.github.sukhvir41.core.template.TemplateType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.lang3.StringUtils.*;

public final class App {
    private final Settings settings;
    //private final com.github.sukhvir41.core.settings.Settings templateSettings;

    public App(Settings settings) {
        this.settings = settings;

    }

    public void createHtmlTemplateClass() {
        if (Files.isRegularFile(settings.getPath())) {
            throw new IllegalArgumentException("Please provide path to the folder where template files lie");
        } else {
            settings.getTemplateFiles()
                    .parallelStream()
                    .forEach(path -> createTemplate(getPackageName(path), path));
        }
    }

    private void createTemplate(String packageName, Path templateFile) {


        //TemplateFactory.getTemplate(templateFile, TemplateType.COMPILE_TIME, packageName, )


//        var htmlTemplate = new TemplateGenerator();
//        var classString = htmlTemplate.setTemplate(templateFile, packageName)
//                .render();
//
//        var outputPath = getOutputFilePath(packageName, StringUtils.getClassNameFromFile(templateFile.getFileName().toString()));
//        createDirectory(outputPath);
//        createFile(outputPath);
//        writeToFile(outputPath, classString);
    }

    private void writeToFile(Path path, String content) {
        try (var writer = Files.newBufferedWriter(path)) {
            writer.write(content);
            LogManager.getLogger()
                    .info("Content written to file " + path.toAbsolutePath().toString());
        } catch (Exception e) {
            throw new RuntimeException("Could not write to file " + path.toAbsolutePath().toString(), e);
        }
    }

    private void createFile(Path outputFilePath) {
        try {
            if (Files.exists(outputFilePath)) {
                LogManager.getLogger()
                        .warning("File: " + outputFilePath.toAbsolutePath().toString() + " already exits. Will be overwriting file contents");
            } else {
                Files.createFile(outputFilePath);
                LogManager.getLogger()
                        .info("Created File " + outputFilePath.toAbsolutePath().toString());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not create File " + outputFilePath.toString(), e);
        }
    }

    private void createDirectory(Path outputFilePath) {
        try {
            Files.createDirectories(outputFilePath.getParent());
            LogManager.getLogger()
                    .info("Created Directory " + outputFilePath.toAbsolutePath().toString());
        } catch (Exception e) {
            throw new RuntimeException("Could not create directory " + outputFilePath.getParent().toString(), e);
        }
    }

    private Path getOutputFilePath(String packageName, String className) {

        String incompleteOutputPathString = packageName.replace(".", File.separator) + File.separator + className + ".java";

        String outputDirectory = this.settings.getOutputPath()
                .toAbsolutePath()
                .normalize()
                .toString();

        return Paths.get(outputDirectory + File.separator + incompleteOutputPathString);

    }

    private String getPackageName(Path templateFile) {
        var absolutePathString = templateFile.toAbsolutePath()
                .getParent()
                .normalize()
                .toString();

        var sourceAbsolutePath = settings.getPath()
                .toAbsolutePath()
                .normalize()
                .toString();

        var outputFilePathString = removeStart(absolutePathString, sourceAbsolutePath);
        if (isBlank(settings.getPackageName())) {
            outputFilePathString = settings.getPath().getFileName().toString() + outputFilePathString;
        } else {
            outputFilePathString = settings.getPackageName() + File.separator + outputFilePathString;
        }

        return replace(outputFilePathString, File.separator, ".");
    }


}
