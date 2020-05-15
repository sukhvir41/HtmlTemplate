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

package com.github.sukhvir41.tags;

import com.github.sukhvir41.template.IllegalSyntaxException;
import com.github.sukhvir41.template.TemplateClass;

import java.util.regex.Pattern;

final class MetaVariableHtmlTag extends RegularHtmlTag {

    public static final Pattern META_TYPE_TAG_PATTERN =
            Pattern.compile("<\\s*meta\\s+ht-variables\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);
    public static final Pattern TYPE_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-variables\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static boolean matches(String html) {
        return META_TYPE_TAG_PATTERN.matcher(html)
                .find();
    }

    protected MetaVariableHtmlTag(String htmlString) {
        super(htmlString);
    }

    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        extractAndAddType(templateClass);
    }

    private void extractAndAddType(TemplateClass templateClass) {

        var matcher = TYPE_ATTRIBUTE_PATTERN
                .matcher(super.htmlString);

        if (matcher.find()) {
            var types = super.htmlString
                    .substring(matcher.start(), matcher.end());
            types = types.substring(types.indexOf("\"") + 1, types.length() - 1);

            String[] typesParts = types.split(",");

            if (typesParts.length % 2 != 0) {
                throw new IllegalSyntaxException("A missing name or type.\nLine -> " + this.htmlString);
            } else {
                for (int i = 0; i < typesParts.length; i += 2) {
                    templateClass.addVariable(typesParts[i], typesParts[i + 1]);
                }
            }
        }

    }

    @Override
    public void processTag(TemplateClass templateClass) {
        // do nothing
    }


}
