package org.ht.cli;

import org.apache.commons.lang3.StringUtils;
import org.ht.template.HtmlTemplate;
import org.ht.utils.HtStringUtils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class App {
    private Settings settings;

    public App(Settings settings) {
        this.settings = settings;
    }

    public void createHtmlTemplateClass() {
        if (Files.isRegularFile(settings.getPath())) {
            createTemplate(settings.getPackageName(), settings.getPath());
        } else {
            settings.getTemplateFiles()
                    .parallelStream()
                    .forEach(path -> createTemplate(getPackageName(path), path));
        }
    }

    private void createTemplate(String packageName, Path templateFile) {
        var htmlTemplate = new HtmlTemplate();
        var classString = htmlTemplate.setTemplate(templateFile, packageName)
                .render();

        var outputPath = getOutputFilePath(packageName, HtStringUtils.getClassNameFromFile(templateFile.getFileName().toString()));
        createDirectory(outputPath);
        createFile(outputPath);
        writeToFile(outputPath, classString);
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

    public String getPackageName(Path templateFile) {
        var absolutePathString = templateFile.toAbsolutePath()
                .getParent()
                .normalize()
                .toString();

        var sourceAbsolutePath = settings.getPath()
                .toAbsolutePath()
                .normalize()
                .toString();

        var outputFilePathString = StringUtils.removeStart(absolutePathString, sourceAbsolutePath);
        if (StringUtils.isBlank(settings.getPackageName())) {
            outputFilePathString = settings.getPath().getFileName().toString() + outputFilePathString;
        } else {
            outputFilePathString = settings.getPackageName() + File.separator + outputFilePathString;
        }


        return StringUtils.replace(outputFilePathString, File.separator, ".");
    }


}
