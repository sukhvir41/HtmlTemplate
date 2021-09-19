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

import com.github.sukhvir41.parsers.lineParser.LineParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

public final class TemplateReader {

    public static void read(Path file, Consumer<String> lineSectionConsumer) {
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            readImpl(reader, lineSectionConsumer);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not locate File : " + file.toAbsolutePath().toString());
        }
    }

    private static void readImpl(BufferedReader reader, Consumer<String> lineSectionConsumer) throws IOException {
        LineParser lineParser = new LineParser();
        String line;

        while ((line = reader.readLine()) != null) {
            lineParser.setLine(line);
            while (lineParser.hasNextSection()) {
                String section = lineParser.getNextSection()
                        .trim();
                lineSectionConsumer.accept(section);
            }
        }
    }
}
