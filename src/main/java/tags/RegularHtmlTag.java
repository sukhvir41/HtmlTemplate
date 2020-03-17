package tags;

import template.TemplateClass;

class RegularHtmlTag implements HtmlTag {

    private String htmlString;
    private char[] tagCharacters;

    public static boolean matches(String html) {
        return html.contains("<");
    }

    protected RegularHtmlTag(String htmlString) {
        this.htmlString = htmlString;
        this.tagCharacters = htmlString.toCharArray();
    }


    @Override
    public String getHtml() {
        if (tagCharacters[tagCharacters.length - 1] == '>') {
            return htmlString;

        } else {
            return htmlString + ">";
        }
    }

    @Override
    public String getName() {
        var name = new StringBuilder();

        int startPosition;

        boolean characterHit = false;

        if (this.isOnlyClosingTag()) {
            //starting form 2 and the first chars are '</' to ignore it.
            startPosition = 2;
        } else {
            //starting form 1 ad the first char is '<' to ignore it.
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

    private boolean isOnlyClosingTag() {
        return tagCharacters[1] == '/';
    }

    @Override
    public boolean isClosingTag() {
        return tagCharacters[1] == '/' || isSelfClosing();
    }

    @Override
    public boolean isClosingTag(HtmlTag htmlTag) {
        if (!this.getName().equalsIgnoreCase(htmlTag.getName())) {
            return false;
        } else {
            return htmlTag.isClosingTag();
        }
    }

    @Override
    public boolean isSelfClosing() {
        if (tagCharacters[tagCharacters.length - 1] == '>') {
            return tagCharacters[tagCharacters.length - 2] == '/' || HtmlUtils.isVoidTag(getName().toLowerCase());
        } else {
            return tagCharacters[tagCharacters.length - 1] == '/' || HtmlUtils.isVoidTag(getName().toLowerCase());
        }

    }

    @Override
    public void processOpeningTag(TemplateClass templateClass) {
        // does nothing
    }

    @Override
    public void processClosingTag(TemplateClass templateClass) {
        // does nothing
    }


}
