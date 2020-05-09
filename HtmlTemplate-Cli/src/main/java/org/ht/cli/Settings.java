package org.ht.cli;

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
            description = "HtmlTemplate File or folder where HtmlTemplate files reside",
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
            description = "Package name. Last folder name of the folder path(-f). if file then no package name", defaultValue = "")
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
                    .filter(this::fileNamePatterMatch)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            if (this.isVerboseOutputRequested()) {
                e.printStackTrace();
            }
            this.templateFiles = Collections.emptyList();
        }
    }

    private boolean fileNamePatterMatch(Path path) {
        try {
            var matcher = this.filePattern.matcher(path.getFileName().toString());
            return matcher.matches();
        } catch (Exception e) {
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
