package org.ht.tags;

import org.ht.template.IllegalSyntaxException;
import org.ht.template.TemplateClass;

import java.util.regex.Pattern;

public final class MetaImportHtmlTag implements HtmlTag {

    public static final Pattern IMPORT_META_TAG_PATTERN =
            Pattern.compile("<\\s*meta[\\s,\\S]* ht-import\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);
    public static final Pattern IMPORT_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-import\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    private String htmlString;

    public static boolean matches(String string) {
        return IMPORT_META_TAG_PATTERN.matcher(string)
                .find();
    }

    MetaImportHtmlTag(String htmlString) {
        this.htmlString = htmlString;
    }

    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        var matcher = IMPORT_ATTRIBUTE_PATTERN.matcher(htmlString);
        try {
            if (matcher.find()) {
                var importString = htmlString.substring(matcher.start(), matcher.end())
                        .split("=")[1]
                        .replace("\"", "")
                        .trim();

                templateClass.addImportStatement(importString);
            }
        } catch (Exception e) {
            throw new IllegalSyntaxException("Unable to process import statement \n" +
                    "Html tag -> " + this.htmlString, e);
        }
    }

    @Override
    public void processTag(TemplateClass templateClass) {
        // do nothing
    }

    @Override
    public void processClosingTag(TemplateClass templateClass) {
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
