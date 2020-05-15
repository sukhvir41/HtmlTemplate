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

package com.github.sukhvir41.cli;

import picocli.CommandLine;

public final class Main {

    public static void main(String[] args) {
        Settings settings = CommandLine.populateCommand(new Settings(), args);
        if (settings.isUsageHelpRequested()) {
            CommandLine.usage(new Settings(), System.out);
            return;
        }
        settings.setLoggingLevel();
        App app = new App(settings);
        app.createHtmlTemplateClass();

    }
}
