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

package com.github.sukhvir41.utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LogManager {

    private final static Logger LOGGER = Logger.getAnonymousLogger();

    public static void setLoggerLevel(Level level) {
        //https://stackoverflow.com/a/31852952/4803757
        Handler console = new ConsoleHandler();
        console.setLevel(level);
        LOGGER.addHandler(console);
        LOGGER.setUseParentHandlers(false);
        LOGGER.setLevel(level);
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
