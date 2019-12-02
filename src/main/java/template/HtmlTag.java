package template;

import java.util.Optional;
import java.util.regex.Pattern;

public class HtmlTag {

    private static final Pattern DYNAMIC_HTML_TAG = Pattern.compile("<[\\s,\\S]* ht-[a-z]+\\s*=\"[\\s,\\S]");

    private static final Pattern IMPORT_META_TAG_FINDER_PATTERN = Pattern.compile("<meta[\\s,\\S]* ht-import=\"[\\s,A-Z,a-z,0-9,\\.,_]*\"", Pattern.CASE_INSENSITIVE);
    private static final Pattern IMPORT_META_TAG_PATTERN = Pattern.compile("ht-import=\"[\\s,A-Z,a-z,0-9,\\.,_]*\"", Pattern.CASE_INSENSITIVE);

    private String tag;
    private char[] tagCharacters;

    public static boolean containsHtmlTag(String html) {
        return html.contains("<");
    }

    public static Optional<HtmlTag> parse(String html) {
        if (containsHtmlTag(html)) {
            var tagString = html.substring(html.lastIndexOf('<'));
            return Optional.of(new HtmlTag(tagString));
        } else {
            return Optional.empty();
        }
    }


    private HtmlTag(String tagString) {
        this.tag = tagString;
        this.tagCharacters = this.tag.toCharArray();
    }

    private boolean isHtmlTag(String tagString) {
        return tagString.indexOf('<') > -1;
    }


    public String getName() {
        var name = new StringBuilder();

        int startPosition;

        boolean characterHit = false;

        if (this.isClosingTag()) {
            //starting form 2 ad teh first chars are '</' to ignore it.
            startPosition = 2;
        } else {
            //starting form 1 ad teh first char is '<' to ignore it.
            startPosition = 1;
        }

        for (int i = startPosition; i < tagCharacters.length; i++) {
            if (tagCharacters[i] == ' ') {
                if (characterHit) {
                    break;
                }
            } else {
                characterHit = true;
                name.append(tagCharacters[i]);
            }
        }

        return name.toString();
    }

    /**
     * checks the htmlTag passed as argument is the closing tag of this tag
     *
     * @param htmlTag
     * @return
     */
    public boolean isClosingTag(HtmlTag htmlTag) {

        if (!this.getName().equalsIgnoreCase(htmlTag.getName())) {
            return false;
        } else {
            return htmlTag.isClosingTag();
        }
    }

    public boolean isClosingTag() {
        return tagCharacters[1] == '/';
    }


    public boolean isSelfClosing() {
        if (tagCharacters[tagCharacters.length - 1] == '>') {
            return tagCharacters[tagCharacters.length - 2] == '/' || HtmlUtils.isSelfClosingTag(getName().toLowerCase());
        } else {
            return tagCharacters[tagCharacters.length - 1] == '/' || HtmlUtils.isSelfClosingTag(getName().toLowerCase());
        }
    }


    @Override
    public String toString() {

        if (tagCharacters[tagCharacters.length - 1] == '>') {
            return tag;

        } else {
            return tag + ">";
        }
    }

}
