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


import java.util.HashMap;
import java.util.Map;

import static com.github.sukhvir41.core.settings.SettingOptions.*;

public class SettingsManager {


    public static Settings load(Map<SettingOptions<?>, Object> theSettings) {
        var settings = new HashMap<>(getDefaultSettings());
        settings.putAll(theSettings);
        return new Settings(settings);
    }

    public static Settings load() {
        var settings = new HashMap<>(getDefaultSettings());
        return new Settings(settings);
    }


    private static Map<SettingOptions<?>, Object> getDefaultSettings() {
        return Map.of(
                SUPPRESS_NULL_EXCEPTIONS, true,
                TEMPLATE_FOLDER_PATH, "",
                LOGGING_LEVEL, "OFF"
        );
    }
}

