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

import com.github.sukhvir41.core.settings.Settings;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;

public final class TemplateFactory {

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
                settings.getLogger()
                        .info("runtime template mode selected");
                if (StringUtils.isBlank(packageName)) {
                    return new RuntimeTemplate(file, settings);
                } else {
                    return new RuntimeTemplate(file, packageName, settings);
                }

            case COMPILE_TIME:
                settings.getLogger()
                        .info("compile time template mode selected");
                return null;//new CompileTimeTemplate(file, packageName, settings);
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
}
