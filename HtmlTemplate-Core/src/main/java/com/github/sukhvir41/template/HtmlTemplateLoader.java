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

import com.github.sukhvir41.core.Template;
import com.github.sukhvir41.core.TemplateFactory;
import com.github.sukhvir41.core.TemplateType;
import org.joor.Reflect;

import java.nio.file.Path;

public class HtmlTemplateLoader {

    public static HtmlTemplate load(Path file) {
        Template template = TemplateFactory.getTemplate(file, TemplateType.RUN_TIME);
        template.readAndProcessTemplateFile();
        String templateCode = template.render();
        System.out.println(templateCode);
        Reflect reflect = Reflect.compile(template.getFullClassName(), templateCode);
        return new HtmlTemplate(reflect);

    }

}
