package processors;

import template.HtmlTemplate;
import template.TemplateClass;

import java.util.Optional;
import java.util.regex.Pattern;

class StyleTagProcessor implements Processor {

    private static Pattern closingTagPattern = Pattern.compile("(</style\\s*>)|(</STYLE\\s*>)");
    private static Pattern startingTagPattern = Pattern.compile("(<style\\s*>)|(<STYLE\\s*>)");

    public static boolean containsStyleTag(String html) {
        var matcher = startingTagPattern.matcher(html);
        return matcher.find();
    }

    @Override
    public void process(String html, TemplateClass templateClass, HtmlTemplate template) {

        if (template.getProcessor() == HtmlProcessors.STYLE) {
            System.out.println("STYLE -> " + html);
            processMultiLineStyleTag(html, templateClass, template);
        } else if (containsStyleTag(html)) {
            System.out.println("STYLE -> " + html);
            processHtml(html, templateClass, template);
        } else {
            processNonStyleHtml(html, templateClass, template);
        }
    }

    private void processMultiLineStyleTag(String html, TemplateClass templateClass, HtmlTemplate template) {

        if (containsClosingStyleTag(html)) {
            getEndOfMultiLineStyleTag(html)
                    .filter(s -> !s.isBlank())
                    .ifPresent(templateClass::appendStyle);

            template.setProcessor(HtmlProcessors.REGULAR);

            getRightOfStyleTag(html)
                    .filter(s -> !s.isBlank())
                    .ifPresent(regularHtml -> processNonStyleHtml(regularHtml, templateClass, template));

        } else {
            templateClass.appendStyle(html);
        }
    }

    private void processHtml(String html, TemplateClass templateClass, HtmlTemplate template) {

        getLeftOfStyleTag(html)
                .filter(s -> !s.isBlank())
                .ifPresent(regularHtml -> processNonStyleHtml(regularHtml, templateClass, template));

        getStyleTag(html)
                .filter(s -> !s.isBlank())
                .ifPresent(templateClass::appendStyle);

        if (containsClosingStyleTag(html)) {
            getRightOfStyleTag(html)
                    .filter(s -> !s.isBlank())
                    .ifPresent(regularHtml -> processNonStyleHtml(regularHtml, templateClass, template));
        } else {
            template.setProcessor(HtmlProcessors.STYLE);
        }


    }

    private Optional<String> getLeftOfStyleTag(String html) {
        try {

            if (containsStyleTag(html)) {
                var regularHtml = html.substring(0, getStartingIndexOfStyleTag(html));
                return Optional.of(regularHtml);
            } else {
                return Optional.of(html);
            }

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<String> getStyleTag(String html) {
        try {

            if (containsStyleTag(html)) {
                if (containsClosingStyleTag(html)) {
                    var styleHtml = html.substring(getStartingIndexOfStyleTag(html), getEndingIndexOfClosingTag(html));
                    return Optional.of(styleHtml);
                } else {
                    var styleHtml = html.substring(getStartingIndexOfStyleTag(html));
                    return Optional.of(styleHtml);
                }
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private boolean containsClosingStyleTag(String html) {
        var matcher = closingTagPattern.matcher(html);
        return matcher.find();
    }


    private Optional<String> getEndOfMultiLineStyleTag(String html) {
        try {

            var style = html.substring(0, getEndingIndexOfClosingTag(html));
            return Optional.of(style);

        } catch (Exception e) {
            return Optional.empty();
        }

    }

    private int getStartingIndexOfStyleTag(String html) {
        var matcher = startingTagPattern.matcher(html);
        if (matcher.find()) {
            return matcher.start();
        } else {
            return -1;
        }
    }

    private int getEndingIndexOfClosingTag(String html) {
        var matcher = closingTagPattern.matcher(html);
        if (matcher.find()) {
            return matcher.end();
        } else {
            return -1;
        }
    }

    private Optional<String> getRightOfStyleTag(String html) {
        try {

            var regularHtml = html.substring(getEndingIndexOfClosingTag(html));
            return Optional.of(regularHtml);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private void processNonStyleHtml(String html, TemplateClass templateClass, HtmlTemplate template) {
        HtmlProcessors.REGULAR.process(html, templateClass, template);
    }

}
