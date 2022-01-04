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

package com.github.sukhvir41.core.template;

import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.settings.Settings;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public final class TemplateFactory {

    private static final Logger LOGGER = Logger.getLogger(TemplateFactory.class.getName());

    public static Template getTemplate(Path file, TemplateType type, Settings settings) {
        return getTemplate(file, type, "", settings);
    }

    public static Template getTemplate(Path file, TemplateType type, String packageName, Settings settings) {

        validateFile(file);

        if (type == null) {
            throw new IllegalArgumentException("Please provide template type");
        }

        if (settings == null) {
            throw new IllegalArgumentException("Please provide settings");
        }

        switch (type) {
            case RUN_TIME:
                LOGGER.info("Runtime template mode selected for file " + file.getFileName());
                if (StringUtils.isBlank(packageName)) {
                    LOGGER.fine("Blank package name");
                    return new RuntimeTemplate(file, settings);
                } else {
                    LOGGER.fine("Package name " + packageName);
                    return new RuntimeTemplate(file, packageName, settings);
                }

            case COMPILE_TIME:
                LOGGER.info("compile time template mode selected");
                validateFileLocation(settings, file);
                return new CompileTimeTemplate(file, packageName, settings);
            default:
                throw new IllegalArgumentException("Please provide template type");
        }
    }


    private static void validateFile(Path file) {
        if (file == null) {
            throw new IllegalArgumentException("Please provide template file");
        }
        if (!Files.exists(file)) {
            throw new IllegalArgumentException("Template file not found : " + file.getFileName().toString());
        }
        if (Files.isDirectory(file)) {
            throw new IllegalArgumentException("Provided path is a directory and not a file. Path: " + file.toAbsolutePath().toString());
        }
        if (!Files.isReadable(file)) {
            throw new IllegalArgumentException("Can not read file : " + file.getFileName().toString());
        }
    }

    private static void validateFileLocation(Settings settings, Path file) {
        Path rootFolder = settings.get(SettingOptions.ROOT_FOLDER)
                .orElseThrow(() -> new IllegalArgumentException("ROOT_FOLDER setting not present"))
                .normalize();

        if (!file.startsWith(rootFolder)) {
            throw new IllegalArgumentException("Template file is outside the root folder. \nTemplate file: " + file.normalize().toString() +
                    "\nRoot Folder: " + rootFolder.toString());
        }

    }
}
