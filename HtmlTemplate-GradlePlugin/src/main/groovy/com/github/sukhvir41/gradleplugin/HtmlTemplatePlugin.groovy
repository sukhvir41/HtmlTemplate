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

import groovy.io.FileType
import org.apache.commons.lang3.StringUtils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention

import java.nio.file.Path

class HtmlTemplatePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.getPluginManager()
                .apply(JavaPlugin);

        def extension = project.extensions.create("HtTemplate", HtTemplateExt)

        def htTemplateTask = project.tasks.register("createHtmlTemplates", CreateHtmlTemplate) {
            templateFiles(getTemplateFiles(extension, project))
            rootFolder(getRootFolder(extension, project))
            packageName(extension.javaPackage)

            if (extension.outputDirectory != null && !extension.outputDirectory.trim().equals("")) {
                outputDirectory(
                        project.getProjectDir()
                                .toPath()
                                .resolve(extension.outputDirectory)
                )
            } else {
                throw new IllegalArgumentException("outputDirectory property missing in extension")
            }
        }

        project.getTasksByName("compileJava", false).forEach { it.dependsOn(htTemplateTask) }

    }

    private static Set<Path> getTemplateFiles(HtTemplateExt ext, Project project) {
        def rootFolder = getRootFolder(ext, project);

        def set = new HashSet<Path>()
        rootFolder.eachFileRecurse(FileType.FILES) { file ->
            if (!ext.ignoreFiles.contains(file.getFileName().toString()) && StringUtils.endsWith(file.getFileName().toString(), ".html")) {
                set.add(file)
            }
        }
        return set
    }

    private static Path getRootFolder(HtTemplateExt ext, Project project) {
        def sourceDir = project.convention.getPlugin(JavaPluginConvention).sourceSets.main.java
        return sourceDir.sourceDirectories
                .singleFile
                .toPath()
                .resolve(ext.javaPackage.replace(".", File.separator))
    }

}
