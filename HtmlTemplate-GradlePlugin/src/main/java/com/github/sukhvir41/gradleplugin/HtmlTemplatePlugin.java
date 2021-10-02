package com.github.sukhvir41.gradleplugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.plugins.JavaPluginExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;

public class HtmlTemplatePlugin implements Plugin<Project> {


    @Override
    public void apply(Project project) {
        project.getPluginManager()
                .apply(JavaPlugin.class);

        HtTemplateExtension extension = project.getExtensions().create("HtTemplate", HtTemplateExtension.class);

        TaskProvider<GenerateHtmlTemplates> htmlTask = project.getTasks().register("generateTemplates", GenerateHtmlTemplates.class, task -> {
            task.getJavaPackage().set(extension.getJavaPackage());
            task.getIgnoreFiles().set(extension.getIgnoreFiles());
            task.getTemplateDirectory().set(extension.getTemplateDirectory());
            task.getOutputDirectory().set(extension.getOutputDirectory());
        });

        getSourceSets(project)
                .configureEach(sourceSet -> {
                    if (sourceSet.getName().equals("main")) {
                        sourceSet.getJava().srcDir(htmlTask.get().getOutputDirectory());
                    }
                });

        project.getTasks().getByName("compileJava")
                .dependsOn(htmlTask);
    }


    public SourceSetContainer getSourceSets(Project project) {
        return project.getConvention()
                .getPlugin(JavaPluginConvention.class)
                .getSourceSets();
    }
}
