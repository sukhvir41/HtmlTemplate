package org.ht.tags;

import org.ht.template.TemplateClass;

public interface HtmlTag {

    public static boolean matches(String html) {
        throw new UnsupportedOperationException("Sub class implementation missing");
    }


    /**
     * this gets called before the html tag gets appended to the org.ht.template
     *
     * @param templateClass the org.ht.template
     */
    void processOpeningTag(TemplateClass templateClass);

    /**
     * gets the html String data. this will only contain a single html data either opening or closing. Html content is not included
     *
     * @return the html String data
     */
    String getHtml();

    /**
     * When a respective closing tag is found, This function gets called.
     *
     * @param templateClass org.ht.template class
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
