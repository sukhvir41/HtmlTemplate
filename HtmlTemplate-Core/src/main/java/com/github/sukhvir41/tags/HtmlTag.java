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

import com.github.sukhvir41.template.TemplateClass;
import com.github.sukhvir41.utils.HtmlUtils;

public interface HtmlTag {

    static boolean matches(String html) {
        throw new UnsupportedOperationException("Implementation missing");
    }


    /**
     * this gets called before the html tag gets appended to the org.org.ht.template
     *
     * @param templateClass the org.org.ht.template
     */
    void processOpeningTag(TemplateClass templateClass);

    /**
     * process the html tag. Only gets called once after processOpeningTag or on its own.
     */
    void processTag(TemplateClass templateClass);

    /**
     * When a respective closing tag is found, This function gets called.
     *
     * @param templateClass org.org.ht.template class
     */
    void processClosingTag(TemplateClass templateClass);

    /**
     * returns the name of the html tag.
     * eg.
     * &lt; a href="#" &gt; : here "a" is the name of the tag
     *
     * @return name of the tag
     */
    String getName();

    /**
     * returns true if the html tag is a closing tag.<br>
     * eg: <br>
     * &lt;/ a &gt; = true <br>
     * &lt; br /&gt; = false
     *
     * @return is closing html tag
     */
    boolean isClosingTag();

    /**
     * returns true if the parsed htmlTag is the closing tag for this html tag
     *
     * @param htmlTag potential closing tag
     * @return is the parsed html tag closing tag for this HTML tag
     */
    default boolean isClosingTag(HtmlTag htmlTag) {
        return htmlTag.isClosingTag() && getName().equalsIgnoreCase(htmlTag.getName());
    }

    /**
     * returns true of the HTML tag is self-closing.
     * self-closing : if it ends with a "//>".
     *
     * @return true if it is self closing.
     */
    boolean isSelfClosing();

    /**
     * returns true is the tag is a void tag.
     * A void tag does not require a closing tag or self-closing tag.
     *
     * @return true if it is a void tag.
     */
    default boolean isVoidTag() {
        return HtmlUtils.isVoidTag(getName());
    }

    boolean isDocTypeTag();

}