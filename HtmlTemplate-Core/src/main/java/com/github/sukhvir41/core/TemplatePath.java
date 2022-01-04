package com.github.sukhvir41.core;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.util.Collections;

public class TemplatePath implements Path {

    private final Path file;

    public static Path getPath(Path file) {
        if (Files.isRegularFile(file)) {
            return new TemplatePath(file);
        } else {
            throw new IllegalArgumentException("the path specified is not file. Path: " + file);
        }
    }

    private TemplatePath(Path file) {
        this.file = file;
    }

    @Override
    public FileSystem getFileSystem() {
        try {
            if (Files.isReadable(file)) {
                return file.getFileSystem();
            } else {
                String jarPath = getClass().getProtectionDomain()
                        .getCodeSource()
                        .getLocation()
                        .getPath();
                URI uri = URI.create("jar:file:" + jarPath);
                return FileSystems.newFileSystem(uri, Collections.emptyMap());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isAbsolute() {
        return file.isAbsolute();
    }

    @Override
    public Path getRoot() {
        return file.getRoot();
    }

    @Override
    public Path getFileName() {
        return file.getFileName();
    }

    @Override
    public Path getParent() {
        return file.getParent();
    }

    @Override
    public int getNameCount() {
        return file.getNameCount();
    }

    @Override
    public Path getName(int index) {
        return file.getName(index);
    }

    @Override
    public Path subpath(int beginIndex, int endIndex) {
        return file.subpath(beginIndex, endIndex);
    }

    @Override
    public boolean startsWith(Path other) {
        return file.startsWith(other);
    }

    @Override
    public boolean endsWith(Path other) {
        return file.endsWith(other);
    }

    @Override
    public Path normalize() {
        return file.normalize();
    }

    @Override
    public Path resolve(Path other) {
        return file.resolve(other);
    }

    @Override
    public Path relativize(Path other) {
        return file.relativize(other);
    }

    @Override
    public URI toUri() {
        return file.toUri();
    }

    @Override
    public Path toAbsolutePath() {
        return file.toAbsolutePath();
    }

    @Override
    public Path toRealPath(LinkOption... options) throws IOException {
        return file.toRealPath(options);
    }

    @Override
    public WatchKey register(WatchService watcher, WatchEvent.Kind<?>[] events, WatchEvent.Modifier... modifiers) throws IOException {
        return file.register(watcher, events, modifiers);
    }

    @Override
    public int compareTo(Path other) {
        return file.compareTo(other);
    }
}
