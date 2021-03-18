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

import com.github.sukhvir41.utils.CheckedFunction;

import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

class SettingOptions<RETURN> {

    public static final SettingOptions<Boolean> SUPPRESS_NULL_EXCEPTIONS = new SettingOptions<>((Object param) -> Boolean.valueOf(String.valueOf(param)));
    public static final SettingOptions<String> TEMPLATE_FOLDER_PATH = new SettingOptions<>(String::valueOf);
    public static final SettingOptions<Level> LOGGING_LEVEL = new SettingOptions<>(CheckedFunction.wrapFunction((Object param) -> Level.parse(param.toString())));

    private final Function<Object, RETURN> caster;

    private SettingOptions(Function<Object, RETURN> caster) {
        this.caster = caster;
    }

    public Function<Object, RETURN> getCaster() {
        return caster;
    }
}

