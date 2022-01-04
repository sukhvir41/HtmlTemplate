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

package com.github.sukhvir41.template;

import com.github.sukhvir41.core.template.Template;
import com.github.sukhvir41.core.template.TemplateFactory;
import com.github.sukhvir41.core.template.TemplateType;
import com.github.sukhvir41.core.settings.Settings;
import com.github.sukhvir41.core.settings.SettingsManager;
import org.joor.Reflect;

import java.nio.file.Path;
import java.util.Set;
import java.util.logging.Logger;

public class HtmlTemplateLoader {
    private static final Logger LOGGER = Logger.getLogger(HtmlTemplateLoader.class.getName());

    public static HtmlTemplate load(Path file) {
        Settings settings = SettingsManager.load();
        return load(file, settings);
    }

    public static HtmlTemplate load(Path file, Settings settings) {
        Template template = TemplateFactory.getTemplate(file, TemplateType.RUN_TIME, settings);
        template.readAndProcessTemplateFile();
        String templateCode = template.render();
        LOGGER.info("Class generated: \n" + templateCode);
        Reflect reflect = Reflect.compile(template.getFullyQualifiedName(), templateCode);
        return new HtmlTemplate(reflect);
    }

}
