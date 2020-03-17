package tags;

import java.util.HashSet;
import java.util.Set;

public class HtmlUtils {

    private static final Set<String> voidTags = new HashSet<>();

    static {
        voidTags.add("meta");
        voidTags.add("link");
        voidTags.add("br");
        voidTags.add("hr");
        voidTags.add("area");
    }


    public static boolean isVoidTag(String tagName) {
        return voidTags.contains(tagName);
    }


    public static boolean isHtmlTagAtStart(String line) {
        char c = ' ';
        for (int i = 0; i < line.length(); i++) {
            c = line.charAt(i);
            if (c != ' ') {
                break;
            }
        }
        return c == '<';
    }

    public static String getStartingHtmlTagName(String line) {
        if (isHtmlTagAtStart(line)) {
            return getHtmlTagName(line);
        } else {
            throw new IllegalArgumentException("the line passed does not start with an html tag");
        }
    }

    private static String getHtmlTagName(String line) {
        var trimmedLine = line.trim();
        var name = new StringBuilder();

        int startPosition;

        boolean characterHit = false;

        if (isStartingTagClosingTag(trimmedLine)) {
            //starting form 2 and the first chars are '</' to ignore it.
            startPosition = 2;
        } else {
            //starting form 1 ad the first char is '<' to ignore it.
            startPosition = 1;
        }

        for (int i = startPosition; i < trimmedLine.length(); i++) {
            if (trimmedLine.charAt(i) == ' ' || trimmedLine.charAt(i) == '>') {
                if (characterHit) {
                    break;
                }
            } else {
                characterHit = true;
                name.append(trimmedLine.charAt(i));
            }
        }

        return name.toString();
    }

    public static boolean isStartingTagClosingTag(String line) {
        if (isHtmlTagAtStart(line)) {
            return line.trim()
                    .charAt(1) == '/';
        } else {
            throw new IllegalArgumentException("the line passed does not start with an html tag");
        }
    }
}
