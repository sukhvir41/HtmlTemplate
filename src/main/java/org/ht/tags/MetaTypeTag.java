package org.ht.tags;

import org.ht.template.IllegalSyntaxException;
import org.ht.template.TemplateClass;

import java.util.regex.Pattern;

public final class MetaTypeTag extends RegularHtmlTag {

    public static final Pattern META_TYPE_TAG_PATTERN =
            Pattern.compile("<\\s*meta\\s+ht-type\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);
    public static final Pattern TYPE_ATTRIBUTE_PATTERN =
            Pattern.compile("ht-type\\s*=\\s*\"[^\"]*\"", Pattern.CASE_INSENSITIVE);

    public static boolean matches(String html) {
        return META_TYPE_TAG_PATTERN.matcher(html)
                .find();
    }

    protected MetaTypeTag(String htmlString) {
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
                throw new IllegalSyntaxException("Missing name or type.\nLine -> " + this.htmlString);
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
