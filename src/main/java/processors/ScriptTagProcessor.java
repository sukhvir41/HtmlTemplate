package processors;

import template.HtmlTemplate;
import template.TemplateClass;

import java.util.Optional;
import java.util.regex.Pattern;

class ScriptTagProcessor implements Processor {

    private static Pattern closingTagPattern = Pattern.compile("(</script\\s*>)|(</SCRIPT\\s*>)");
    private static Pattern startingTagPattern = Pattern.compile("(<script\\s*>)|(<SCRIPT\\s*>)");

    public static boolean containsScriptTag(String html) {
        var matcher = startingTagPattern.matcher(html);
        return matcher.find();
    }

    @Override
    public void process(String html, TemplateClass templateClass, HtmlTemplate template) {
        if (template.getProcessor() == HtmlProcessors.SCRIPT) {
            processMultiLineScriptTag(html, templateClass, template);
        } else if (containsScriptTag(html)) {
            processHtml(html, templateClass, template);
        } else {
            processRegularHtml(html, templateClass, template);
        }

    }

    private void processHtml(String html, TemplateClass templateClass, HtmlTemplate template) {
        getLeftOfScriptTag(html)
                .ifPresent(regularHtml -> processRegularHtml(regularHtml, templateClass, template));

        getScriptHtml(html)
                .ifPresent(templateClass::appendComment);

        if (containsClosingScriptTag(html)) {
            getRightOfScriptTag(html)
                    .ifPresent(regularHtml -> processRegularHtml(regularHtml, templateClass, template));
        } else {
            template.setProcessor(HtmlProcessors.SCRIPT);
        }
    }

    private void processMultiLineScriptTag(String html, TemplateClass templateClass, HtmlTemplate template) {
        if (containsClosingScriptTag(html)){

        }else{

        }
    }

    private Optional<String> getLeftOfScriptTag(String html) {
        try {

            if (containsScriptTag(html)) {
                var regularHtml = html.substring(0, getStartingIndexOfScriptTag(html));
                return Optional.of(regularHtml);
            } else {
                return Optional.of(html);
            }

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<String> getScriptHtml(String html) {
        try {
            if (containsScriptTag(html)) {
                if (containsClosingScriptTag(html)) {
                    var scriptHtml = html.substring(getStartingIndexOfScriptTag(html), getEndingIndexOfClosingTag(html));
                    return Optional.of(scriptHtml);
                } else {
                    var scriptHtml = html.substring(getStartingIndexOfScriptTag(html));
                    return Optional.of(scriptHtml);
                }
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<String> getRightOfScriptTag(String html) {
        try {
            if (containsClosingScriptTag(html)) {
                var regularHtml = html.substring(getEndingIndexOfClosingTag(html));
                return Optional.of(regularHtml);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<String> getEndOfMultiLine


    private int getStartingIndexOfScriptTag(String html) {
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

    private boolean containsClosingScriptTag(String html) {
        var matcher = closingTagPattern.matcher(html);
        return matcher.find();
    }

    private void processRegularHtml(String html, TemplateClass templateClass, HtmlTemplate template) {
        HtmlProcessors.REGULAR.process(html, templateClass, template);
    }

}
