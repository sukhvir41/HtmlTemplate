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

package com.github.sukhvir41.core;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.sukhvir41.core.SettingOptions.*;

public class SettingsManager {

    private static Map<SettingOptions<?>, Object> settings;

    public static void load(Map<SettingOptions<?>, Object> theSettings) {
        synchronized (SettingsManager.class) {
            if (settings == null) {
                settings = new HashMap<>(getDefaultSettings());
                settings.putAll(theSettings);
            } else {
                throw new IllegalStateException("Can not change settings once set");
            }
        }
    }

    public static <RETURN> Optional<RETURN> getSetting(SettingOptions<RETURN> option) {
        if (settings == null) {
            throw new IllegalStateException("Settings are not loaded");
        }

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

    public static Map<SettingOptions<?>, Object> getDefaultSettings() {
        return Map.of(
                SUPPRESS_NULL_EXCEPTIONS, true,
                TEMPLATE_FOLDER_PATH, ""
        );
    }

}

