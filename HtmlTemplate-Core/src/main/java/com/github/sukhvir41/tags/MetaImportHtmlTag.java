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

import com.github.sukhvir41.core.IllegalSyntaxException;
import com.github.sukhvir41.core.classgenerator.TemplateClassGenerator;
import com.github.sukhvir41.utils.HtmlUtils;

import java.util.regex.Pattern;

public final class MetaImportHtmlTag implements HtmlTag {

    private static final String TAG_NAME = "meta";
    public static final Pattern IMPORT_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-import\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    private final String htmlString;

    public static boolean matches(String string) {

        return HtmlUtils.getStartingHtmlTagName(string)
                .equalsIgnoreCase(TAG_NAME)
                &&
                IMPORT_ATTRIBUTE_PATTERN.matcher(string)
                        .find();
    }

    public MetaImportHtmlTag(String htmlString) {
        this.htmlString = htmlString;
    }

    @Override
    public void processOpeningTag(TemplateClassGenerator classGenerator) {
        var matcher = IMPORT_ATTRIBUTE_PATTERN.matcher(htmlString);
        try {
            if (matcher.find()) {
                var importString = htmlString.substring(matcher.start(), matcher.end())
                        .split("=")[1]
                        .replace("\"", "")
                        .trim();

                var imports = importString.split(",");
                for (String anImport : imports) {
                    classGenerator.addImport(anImport.trim());
                }
            }
        } catch (Exception e) {
            throw new IllegalSyntaxException("Unable to process import statement \n" +
                    "Html tag -> " + this.htmlString, e);
        }
    }

    @Override
    public void processClosingTag(TemplateClassGenerator classGenerator) {
        // do nothing
    }

    @Override
    public String getName() {
        return "meta";
    }

    @Override
    public boolean isClosingTag() {
        return false;
    }

    @Override
    public boolean isSelfClosing() {
        return true;
    }

    @Override
    public boolean isDocTypeTag() {
        return false;
    }


}
