package com.github.sukhvir41.mavenplugin;

import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.settings.Settings;
import com.github.sukhvir41.core.settings.SettingsManager;
import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.core.template.TemplateFactory;
import com.github.sukhvir41.core.template.TemplateType;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.sukhvir41.utils.StringUtils.getClassNameFromFile;

@Mojo(name = "generateTemplates", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class HtmlTemplateMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(required = true)
    private File templateDirectory;

    @Parameter(required = true)
    private File outputDirectory;

    @Parameter(defaultValue = "false")
    private boolean suppressExceptions;

    @Parameter(defaultValue = "OFF")
    private String logLevel;

    @Parameter
    private List<String> ignoreFiles;

    @Parameter(defaultValue = "")
    private String javaPackage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            if (ignoreFiles == null) {
                ignoreFiles = new ArrayList<>();
            }

            Settings settings = SettingsManager.load(
                    Map.of(
                            SettingOptions.PACKAGE_NAME, javaPackage,
                            SettingOptions.LOGGING_LEVEL, logLevel,
                            SettingOptions.ROOT_FOLDER, templateDirectory,
                            SettingOptions.SUPPRESS_EXCEPTIONS, suppressExceptions
                    )
            );

            Files.walk(templateDirectory.toPath())
                    .filter(file -> !ignoreFiles.contains(file.getFileName().toString()) &&
                            StringUtils.endsWith(file.getFileName().toString(), ".html"))
                    .forEach(file -> writeFile(file, settings));

            project.addCompileSourceRoot(outputDirectory.toString());
        } catch (IOException e) {
            getLog().error(e);
        }
    }

    private void writeFile(Path file, Settings settings) {
        try {
            String packageName = getFilePackageName(file);

            Template template = TemplateFactory.getTemplate(file, TemplateType.COMPILE_TIME, packageName, settings);
            template.readAndProcessTemplateFile();
            String classString = template.render();

            Path templateFile = outputDirectory.toPath()
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
                templateDirectory.toPath().toAbsolutePath().normalize().toString()
        );

        if (javaPackage.equals("")) {
            return StringUtils.replace(fileLocation.substring(1), File.separator, ".");
        } else {
            return javaPackage + StringUtils.replace(fileLocation, File.separator, ".");
        }
    }
}
