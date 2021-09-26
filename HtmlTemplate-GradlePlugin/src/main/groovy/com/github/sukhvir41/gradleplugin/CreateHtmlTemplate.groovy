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

package com.github.sukhvir41.gradleplugin

import com.github.sukhvir41.core.settings.SettingOptions
import com.github.sukhvir41.core.settings.Settings
import com.github.sukhvir41.core.settings.SettingsManager
import com.github.sukhvir41.core.template.Template
import com.github.sukhvir41.core.template.TemplateFactory
import com.github.sukhvir41.core.template.TemplateType
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskAction

import java.nio.file.Files
import java.nio.file.Path

import static com.github.sukhvir41.utils.StringUtils.getClassNameFromFile

class CreateHtmlTemplate extends DefaultTask {

    final Path outputDirectory = getProject().projectDir.toPath().resolve("src/generated/HtmlTemplates/")

    @Input
    private Set<Path> templateFiles

    @Input
    private Path rootFolder

    @Input
    private String packageName

    @Override
    String getGroup() {
        return "Html Template"
    }

    void templateFiles(Set<Path> templateFiles) {
        this.templateFiles = templateFiles
    }

    void rootFolder(Path rootFolder) {
        this.rootFolder = rootFolder
    }

    void packageName(String packageName) {
        this.packageName = packageName
    }

    @TaskAction
    private void createTemplate() {
        FileUtils.deleteDirectory(getProject().buildDir.toPath().resolve(outputDirectory).toFile())
        Settings settings = SettingsManager.load(
                Map.of(
                        SettingOptions.ROOT_FOLDER, this.rootFolder,
                        SettingOptions.PACKAGE_NAME, this.packageName
                )
        )

        templateFiles.parallelStream()
                .forEach { Path file ->
                    Template template = TemplateFactory.getTemplate(file, TemplateType.COMPILE_TIME, getFilePackageName(file), settings)
                    template.readAndProcessTemplateFile()
                    def classString = template.render()
                    def templateFile = this.outputDirectory.resolve(
                            StringUtils.replace(this.getFilePackageName(file) + "." + getClassNameFromFile(file), ".", File.separator) + ".java"
                    )
                    Files.createDirectories(templateFile.getParent())
                    Files.createFile(templateFile)

                    Files.write(templateFile, classString.getBytes())
                }

        SourceSetContainer sourceSets = (SourceSetContainer) getProject().getProperties().get("sourceSets")
        def sourceSet = sourceSets.getByName("main")
        sourceSet.java
                .srcDirs(sourceSet.java.srcDirs, outputDirectory.toFile())
        sourceSets.add(sourceSet)

        setupIdeaPlugin()
    }

    String getFilePackageName(Path file) {
        def fileLocation = StringUtils.removeStart(
                file.getParent().normalize().toAbsolutePath().toString(),
                rootFolder.toAbsolutePath().normalize().toString()
        )

        return packageName + StringUtils.replace(fileLocation, File.separator, ".")
    }

    void setupIdeaPlugin() {
        def ideaPlugin = getProject().plugins.getPlugin("idea")
        if (ideaPlugin != null) {
            def ideaExt = getProject().extensions.getByName("idea")
            ideaExt.module.generatedSourceDirs += this.outputDirectory
        }
    }

}
