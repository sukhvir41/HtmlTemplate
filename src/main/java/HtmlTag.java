public class HtmlTag {

    private String tag;
    private char[] tagCharacters;
    private String content;

    public HtmlTag(String tagString) {
        if (isHtmlTag(tagString)) {
            this.content = tagString.substring(0, tagString.lastIndexOf('<'));
            this.tag = tagString.substring(tagString.lastIndexOf('<'));
        } else {
            this.content = tagString;
            this.tag = "";
        }

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
     * checks the htmltag passed as argument is the closing tag of this tag
     *
     * @param htmlTag
     * @return
     */
    public boolean isClosingTag(HtmlTag htmlTag) {
        if (!this.getName().equals(htmlTag.getName())) {
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
            return tagCharacters[tagCharacters.length - 2] == '/';
        } else {
            return tagCharacters[tagCharacters.length - 1] == '/';
        }
    }


    @Override
    public String toString() {

        if (tagCharacters.length == 0) {
            return content;
        } else if (tagCharacters[tagCharacters.length - 1] == '>') {
            return content + tag;
        } else {
            return content + tag + ">";
        }
    }


}
