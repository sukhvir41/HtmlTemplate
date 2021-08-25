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

package com.github.sukhvir41.core.settings;

import java.util.Map;
import java.util.Optional;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Settings {
    private final Map<SettingOptions<?>, Object> settings;
    private final Logger logger = Logger.getAnonymousLogger();

    public Settings(Map<SettingOptions<?>, Object> settings) {
        this.settings = settings;
        setLoggerLevel(get(SettingOptions.LOGGING_LEVEL).orElse(Level.OFF));
    }

    public void setLoggerLevel(Level level) {
        //https://stackoverflow.com/a/31852952/4803757
        Handler console = new ConsoleHandler();
        console.setLevel(level);
        logger.addHandler(console);
        logger.setUseParentHandlers(false);
        logger.setLevel(level);
    }


    public <RETURN> Optional<RETURN> get(SettingOptions<RETURN> option) {
        Object setting = settings.get(option);
        if (setting == null) {
            return Optional.empty();
        } else {
            return Optional.of(
                    option.getCaster()
                            .apply(setting)
            );
        }

    }

    public Logger getLogger() {
        return logger;
    }
}
