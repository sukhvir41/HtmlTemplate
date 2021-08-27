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

final class CommentParser implements HtmlParser {

    @Override
    public void parse(HtmlParserData data) {

        if (containsClosingPart(data.getSection())) {

            if (data.getTemplate().shouldAppendComment()) {
                data.getClassGenerator()
                        .appendPlainHtml(data.getTemplate(), data.getSection());
            }

            data.getTemplate()
                    .setHtmlParser(HtmlParsers.TAG);

        } else {
            if (data.getTemplate().shouldAppendComment()) {
                data.getClassGenerator()
                        .appendPlainHtml(data.getTemplate(), data.getSection());
            }


            data.getTemplate()
                    .setHtmlParser(HtmlParsers.COMMENT);
        }


    }

    private boolean containsClosingPart(String html) {
        return html.contains("-->");
    }
}
