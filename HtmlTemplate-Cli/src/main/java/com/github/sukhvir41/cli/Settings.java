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

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Settings {

    @CommandLine.Option(names = {"-f", "--file", "--folder"},
            description = "HtmlTemplate folder where HtmlTemplate files reside",
            required = true)
    @Getter
    private Path path;

    @CommandLine.Option(names = {"-o", "--output"},
            description = "Output folder for generated java classes",
            required = true)
    @Getter
    private Path outputPath;

    @CommandLine.Option(names = {"-h", "--help"},
            usageHelp = true,
            description = "Help message")
    @Getter
    private boolean usageHelpRequested;

    @CommandLine.Option(names = {"-i", "--ignore-files"},
            description = "Name of files to ignore")
    @Getter
    private Set<String> filesToIgnore = new HashSet<>();

    @CommandLine.Option(names = "--regex",
            description = "Java regex file name matcher. Default regex: [\\s,\\S]*\\.html",
            defaultValue = "[\\s,\\S]*\\.html")
    @Getter
    private Pattern filePattern;

    @CommandLine.Option(names = {"-v", "--verbose"},
            description = "Verbose output")
    @Getter
    private boolean verboseOutputRequested;

    @CommandLine.Option(names = {"-p", "--package-name"},
            description = "Package name. umbrella package name given to all templatess", defaultValue = "")
    private String packageName;

    @CommandLine.Option(names = {"-q", "--quite"}, description = "Quite mode")
    @Getter
    private boolean quiteOutputRequested;

    private List<Path> templateFiles = new ArrayList<>();


    Settings() {
    }


    public List<Path> getTemplateFiles() {
        if (templateFiles.isEmpty()) {
            if (Files.isRegularFile(this.getPath())) {
                templateFiles.add(this.getPath());
            } else {
                populateTemplateFiles();
            }
        }
        return templateFiles;
    }

    private void populateTemplateFiles() {
        try (Stream<Path> stream = Files.walk(this.getPath(), Integer.MAX_VALUE)) {
            this.templateFiles = stream
                    .filter(Files::isReadable)
                    .filter(file -> !this.ignoreFile(file))
                    .filter(Files::isRegularFile)
                    .filter(this::fileNamePatternMatch)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            LogManager.getLogger()
                    .warning(e.getMessage());
            this.templateFiles = Collections.emptyList();
        }
    }

    private boolean fileNamePatternMatch(Path path) {
        try {
            var matcher = this.filePattern.matcher(path.getFileName().toString());
            return matcher.matches();
        } catch (Exception e) {
            LogManager.getLogger()
                    .warning(e.getMessage());
            return false;
        }
    }

    private boolean ignoreFile(Path path) {
        var fullPath = path.toAbsolutePath();

        for (String ignoreFile : getFilesToIgnore()) {
            var file = Paths.get(ignoreFile);

            if (file.isAbsolute() && file.equals(path)) {
                return true;
            } else if (this.getPath().resolve(ignoreFile).equals(fullPath)) {
                return true;
            }
        }
        return false;
    }

    public String getPackageName() {
        if (this.packageName.equals("")) {
            if (!Files.isRegularFile(path)) {
                this.packageName = StringUtils.deleteWhitespace(path.getFileName().toString());
            }
        }
        return this.packageName;
    }

    public void setLoggingLevel() {
        LogManager.setLoggerLevel(Level.WARNING);

        if (this.isVerboseOutputRequested()) {
            LogManager.setLoggerLevel(Level.INFO);
        }

        if (this.isQuiteOutputRequested()) {
            LogManager.setLoggerLevel(Level.OFF);
        }
    }
}
