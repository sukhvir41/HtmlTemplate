import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.Copy;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ProjectReleasePlugin implements Plugin<Project> {

    private static final String PARENT_PROJECT = "HtmlTemplate";

    @Override
    public void apply(Project project) {
        moveJars(project);
        
            project.getTasks().register("releaseProject", task -> {
                task.setGroup("Releases");
                Project parentProject = getHtmlTemplateProject(task.getProject());
                Map<String, Project> childProjects = parentProject.getChildProjects();
                task.dependsOn(getChildMoveJarDependency(childProjects));
            });

    }

    private Set<Task> getChildMoveJarDependency(Map<String, Project> childProjects) {
        return childProjects.values()
                .stream()
                .map(project -> project.getTasks().getByName("moveJar"))
                .collect(Collectors.toSet());
    }

    private static void moveJars(Project project) {
        Project parentProject = getHtmlTemplateProject(project);
        Map<String, Project> childProjects = parentProject.getChildProjects();
        registerMoveJarTask(childProjects.values());

    }

    private static void registerMoveJarTask(Collection<Project> projects) {
        projects.forEach(project -> {
            try {
                project.getTasks().register("moveJar", Copy.class, copy -> {
                    copy.setGroup("Releases");
                    copy.dependsOn(shadowJarDependency(project));
                    copy.from(project.getBuildDir().toPath().resolve("libs"));
                    copy.into(downloadFolderPath(project));
                });
            } catch (Exception e) {
                System.out.println("Move jar Task already assigned to project " + project.getName());
            }
        });
    }

    static Path downloadFolderPath(Project project) {
        Project parentProject = getHtmlTemplateProject(project);
        return parentProject.getProjectDir().toPath().resolve("Download");
    }

    private static Set<Task> shadowJarDependency(Project project) {
        return Set.of(Objects.requireNonNull(project.getTasks().findByName("shadowJar")));
    }


    private static Project getHtmlTemplateProject(Project project) {
        if (project.getName().equals(PARENT_PROJECT)) {
            return project;
        } else {
            Project parentProject = project.getParent();
            while (true) {
                if (parentProject == null) {
                    throw new RuntimeException("Project : " + project.getName() + " does not have a parent project ");
                } else if (parentProject.getName().equals(PARENT_PROJECT)) {
                    return parentProject;
                } else {
                    parentProject = parentProject.getParent();
                }
            }
        }
    }
}
