package template;

import java.util.HashSet;
import java.util.Set;

public class HtmlUtils {

    private static final Set<String> selfClosingTags = new HashSet<>();

    static {
        selfClosingTags.add("meta");
        selfClosingTags.add("link");
        selfClosingTags.add("br");
        selfClosingTags.add("hr");
        selfClosingTags.add("area");
    }


    public static boolean isSelfClosingTag(String tagName) {
        return selfClosingTags.contains(tagName);
    }


}
