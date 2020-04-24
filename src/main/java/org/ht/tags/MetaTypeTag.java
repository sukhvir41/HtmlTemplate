package org.ht.tags;

import org.ht.template.TemplateClass;

import java.util.regex.Pattern;

public class MetaTypeTag extends RegularHtmlTag {

    public static final Pattern META_TYPE_TAG_PATTERN =
            Pattern.compile("<\\s*meta\\s+ht-type\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);
    public static final Pattern TYPE_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-type\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    private String variable = "";
    private String theClass = "";//the class

    public static boolean matches(String html) {
        return META_TYPE_TAG_PATTERN.matcher(html)
                .find();
    }

    protected MetaTypeTag(String htmlString) {
        super(htmlString);
    }

    private void extractType() {
        var matcher = TYPE_ATTRIBUTE_PATTERN
                .matcher(super.htmlString);

        if (matcher.find()) {
            var htType = super.htmlString
                    .substring(matcher.start(), matcher.end());

            var rightSide = htType.substring(htType.indexOf("=") + 1)
                    .replace('"', ' ')
                    .trim();

            this.variable = rightSide.substring(0, rightSide.indexOf(','))
                    .trim();
            this.theClass = rightSide.substring(rightSide.indexOf(',') + 1)
                    .trim();
        }
    }

    private void addType(TemplateClass templateClass) {
        templateClass.addVariable(this.variable, this.theClass);
    }

    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        extractType();
        addType(templateClass);
    }

    @Override
    public void processTag(TemplateClass templateClass) {
        // do nothing
    }
}
