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

package com.github.sukhvir41.parsers.htmlParsers;

import com.github.sukhvir41.utils.HtmlUtils;
import org.apache.commons.lang3.StringUtils;

final class ScriptParser implements HtmlParser {

    @Override
    public void parse(HtmlParserData data) {

        if (containsClosingTag(data.getSection())) {

            data.getTemplate()
                    .setHtmlParser(HtmlParsers.TAG);

            var newData = HtmlParserData.builder()
                    .setSection(data.getSection())
                    .setTemplate(data.getTemplate())
                    .setClassGenerator(data.getClassGenerator())
                    .build();

            HtmlParsers.TAG.parse(newData);

        } else {
            if (data.getTemplate().shouldAppendScript()) {
                data.getClassGenerator()
                        .appendPlainHtml(data.getSection());
            }

            data.getTemplate()
                    .setHtmlParser(HtmlParsers.SCRIPT);
        }

    }

    private boolean containsClosingTag(String html) {

        var matcher = HtmlUtils.SCRIPT_CLOSING_TAG_PATTERN.matcher(html);
        return matcher.find();

    }
}
