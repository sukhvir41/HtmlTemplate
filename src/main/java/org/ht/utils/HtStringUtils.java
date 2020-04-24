package org.ht.utils;

public interface HtStringUtils {


    /**
     * <p>
     * Finds the index of toSearch in the content provided from the starting position specified by start. <br>
     * ignores the toSearch string if it is within a literal string and carries on. <br>
     * eg: <br>
     * start = 0, toSearch = "c" , content = ""abc"c" <br>
     * return : 5 <br>
     * </p>
     *
     * @param start    search begin position in content
     * @param toSearch string to search
     * @param content  string to check
     * @return starting position of toSearch in content. if not found returns -1.
     */
    static int findIndex(int start, String toSearch, String content) {
        if (content == null || toSearch == null) {
            return -1;
        }

        int searchStartIndex = Math.max(start, 0);
        int index = content.indexOf(toSearch, searchStartIndex);

        if (index == -1) {
            return -1;
        }

        var stringVariableMatcher = HtmlUtils.CONTENT_VARIABLE_STRING_PATTERN
                .matcher(content);

        while (stringVariableMatcher.find(searchStartIndex)) {
            if (stringVariableMatcher.start() > index) {
                break;
            } else if (stringVariableMatcher.start() < index && index < stringVariableMatcher.end()) {
                index = content.indexOf(toSearch, stringVariableMatcher.end());
                searchStartIndex = stringVariableMatcher.end();
            } else {
                searchStartIndex = stringVariableMatcher.end();
            }
        }

        return index;
    }

}
