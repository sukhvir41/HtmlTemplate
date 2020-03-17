package tags;

import template.TemplateClass;

import java.util.regex.Pattern;

public final class MetaImportHtmlTag implements HtmlTag {

    private static final Pattern IMPORT_META_TAG_FINDER_PATTERN = Pattern.compile("<meta[\\s,\\S]* ht-import=\"[\\s,A-Z,a-z,0-9,\\.,_]*\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern IMPORT_META_TAG_PATTERN = Pattern.compile("ht-import=\"[\\s,A-Z,a-z,0-9,\\.,_]*\"", Pattern.CASE_INSENSITIVE);

    private String htmlString;

    public static boolean matches(String string) {
        return IMPORT_META_TAG_FINDER_PATTERN.matcher(string)
                .find();
    }

    MetaImportHtmlTag(String htmlString) {
        this.htmlString = htmlString;
    }

    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        var matcher = IMPORT_META_TAG_PATTERN.matcher(htmlString);
        try {
            if (matcher.find()) {
                var importString = htmlString.substring(matcher.start(), matcher.end())
                        .split("=")[1]
                        .replace("\"", "")
                        .trim();

                templateClass.addImportStatement(importString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getHtml() {
        return "";
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
        return true;
    }

    @Override
    public boolean isClosingTag(HtmlTag htmlTag) {
        return htmlTag.isClosingTag() && getName() == htmlTag.getName();
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