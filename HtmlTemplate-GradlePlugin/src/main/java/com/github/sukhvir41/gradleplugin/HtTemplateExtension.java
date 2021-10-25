package com.github.sukhvir41.gradleplugin;

import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.provider.ListProperty;
import org.gradle.api.provider.Property;

public abstract class HtTemplateExtension {

    public abstract Property<String> getJavaPackage();

    public abstract ListProperty<String> getIgnoreFiles();

    public abstract Property<Boolean> getSuppressExceptions();

    public abstract DirectoryProperty getTemplateDirectory();

    public abstract DirectoryProperty getOutputDirectory();
}
