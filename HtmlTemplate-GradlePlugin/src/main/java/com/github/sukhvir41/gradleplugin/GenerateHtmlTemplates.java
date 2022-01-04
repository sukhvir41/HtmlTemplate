package com.github.sukhvir41.gradleplugin;

import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.settings.Settings;
import com.github.sukhvir41.core.settings.SettingsManager;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.core.template.TemplateFactory;
import com.github.sukhvir41.core.template.TemplateType;
import org.apache.commons.lang3.StringUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.github.sukhvir41.utils.StringUtils.getClassNameFromFile;

public abstract class GenerateHtmlTemplates extends DefaultTask {

    @Input
    @Optional
    public abstract Property<String> getJavaPackage();

    @Input
    @Optional
    public abstract Property<String> getLogLevel();

    @Input
    @Optional
    public abstract ListProperty<String> getIgnoreFiles();

    @Input
    @Optional
    public abstract Property<Boolean> getSuppressExceptions();

    @InputDirectory
    public abstract DirectoryProperty getTemplateDirectory();

    @OutputDirectory
    public abstract DirectoryProperty getOutputDirectory();

    @TaskAction
    public void generateTemplates() {
        List<String> filesToIgnore = getIgnoreFiles().getOrElse(Collections.emptyList());

        Settings settings = SettingsManager.load(
                Map.of(
                        SettingOptions.PACKAGE_NAME, getJavaPackage().getOrElse(""),
                        SettingOptions.LOGGING_LEVEL, getLogLevel().getOrElse("OFF"),
                        SettingOptions.ROOT_FOLDER, getTemplateDirectory().get().getAsFile().toPath(),
                        SettingOptions.SUPPRESS_EXCEPTIONS, getSuppressExceptions().getOrElse(true)
                )
        );
        getTemplateDirectory().getAsFileTree()
                .getFiles()
                .parallelStream()
                .filter(file -> !filesToIgnore.contains(file.toPath().getFileName().toString()) &&
                        StringUtils.endsWith(file.toPath().getFileName().toString(), ".html"))
                .map(File::toPath)
                .forEach(file -> writeFile(file, settings));
    }


    private void writeFile(Path file, Settings settings) {
        try {
            String packageName = getFilePackageName(file);

            Template template = TemplateFactory.getTemplate(file, TemplateType.COMPILE_TIME, packageName, settings);
            template.readAndProcessTemplateFile();
            String classString = template.render();

            Path templateFile = getOutputDirectory().get()
                    .getAsFile()
                    .toPath()
                    .resolve(
                            StringUtils.replace(packageName + "." + getClassNameFromFile(file), ".", File.separator) + ".java"
                    );

            Files.createDirectories(templateFile.getParent());

            Files.write(templateFile,
                    classString.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getFilePackageName(Path file) {
        String fileLocation = StringUtils.removeStart(
                file.getParent().normalize().toAbsolutePath().toString(),
                getTemplateDirectory().get().getAsFile().toPath().toAbsolutePath().normalize().toString()
        );

        if (getJavaPackage().getOrElse("").equals("")) {
            return StringUtils.replace(fileLocation.substring(1), File.separator, ".");
        } else {
            return getJavaPackage().get() + StringUtils.replace(fileLocation, File.separator, ".");
        }
    }
}

