import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Copy

import java.nio.file.Path

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

class ProjectReleasePlugin implements Plugin<Project> {

    private static final String PARENT_PROJECT = "HtmlTemplate"

    @Override
    void apply(Project project) {
        moveJars(project)
        project.task("releaseProject") {
            group = "Releases"
            dependsOn(project.tasks.moveJar,
                    getHtmlTemplateProject(project).tasks.build,
                    getHtmlTemplateProject(project).tasks.test)
        }
    }

    private static void moveJars(Project project) {
        project.tasks.register("moveJar", Copy) {
            group = "Releases"
            dependsOn shadowJarDependency(project)
            description = "Moves shadow jar to download directory in the parent folder"
            from(project.buildDir.toPath().resolve("libs"))
            into(downloadFolderPath(project))
        }
    }

    private static Path downloadFolderPath(Project project) {
        def parentProject = project.parent
        while (true) {
            if (parentProject == null) {
                throw new RuntimeException("Project : ${project.name} does not have a parent project ")
            } else if (parentProject.name.equals(PARENT_PROJECT)) {
                return parentProject.projectDir.toPath().resolve("Download")
            } else {
                parentProject = parentProject.parent
            }
        }

    }


    private static Set<Task> shadowJarDependency(Project project) {
        return Set.of(project.tasks.findByName("shadowJar"))
    }

    private static Project getHtmlTemplateProject(Project project) {
        if (project.name.equals(PARENT_PROJECT)) {
            return project
        } else {
            def parentProject = project.parent
            while (true) {
                if (parentProject == null) {
                    throw new RuntimeException("Project : ${project.name} does not have a parent project ")
                } else if (parentProject.name.equals(PARENT_PROJECT)) {
                    return parentProject
                } else {
                    parentProject = parentProject.parent
                }
            }
        }
    }
}