package processors;

import template.HtmlTemplate;
import template.HtmlUtils;
import template.TemplateClass;

import java.util.Optional;

class CommentTagProcessor implements Processor {

    @Override
    public void process(String html, TemplateClass templateClass, HtmlTemplate template) {

        if (HtmlUtils.containsHtmlComment(html)) {
            processHtml(html, templateClass, template);
        } else if (template.getProcessor().equals(HtmlProcessors.COMMENT)) {// multi-line comment
            processMultiLineComment(html, templateClass, template);
        } else {
            processNonCommentHtml(html, templateClass, template);
        }
    }

    private void processHtml(String html, TemplateClass templateClass, HtmlTemplate template) {
        getLeftOfComment(html)
                .ifPresent(regularHtml -> processNonCommentHtml(regularHtml, templateClass, template));

        getComment(html)
                .ifPresent(templateClass::appendComment);

        if (HtmlUtils.containsClosingHtmlComment(html)) {
            getRightOfComment(html)
                    .ifPresent(regularHtml -> processNonCommentHtml(regularHtml, templateClass, template));
        } else {
            template.setProcessor(HtmlProcessors.COMMENT);
        }
    }

    private void processMultiLineComment(String html, TemplateClass templateClass, HtmlTemplate template) {

        if (HtmlUtils.containsClosingHtmlComment(html)) {
            getEndOfMultiLineComment(html)
                    .ifPresent(templateClass::appendComment);

            template.setProcessor(HtmlProcessors.REGULAR);

            getRightOfComment(html)
                    .ifPresent(regularHtml -> processNonCommentHtml(html, templateClass, template));

        } else {
            templateClass.appendComment(html);
        }
    }


    private Optional<String> getLeftOfComment(String html) {
        try {
            var leftOfComment = html.substring(0, html.indexOf("<!--"));
            if (leftOfComment.isBlank()) {
                return Optional.empty();
            } else {
                return Optional.of(leftOfComment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private Optional<String> getComment(String html) {
        try {
            String commentHtml;
            if (HtmlUtils.containsClosingHtmlComment(html)) {
                commentHtml = html.substring(html.indexOf("<!--"), html.indexOf("-->") + 3);
            } else {
                commentHtml = html.substring(html.indexOf("<!--"));
            }
            return Optional.of(commentHtml);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<String> getEndOfMultiLineComment(String html) {
        try {
            String commentHtml = html.substring(0, html.indexOf("-->") + 3);

            return Optional.of(commentHtml);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Optional<String> getRightOfComment(String html) {
        try {
            String rightOfComment = html.substring(html.indexOf("-->") + 3);
            if (rightOfComment.isBlank()) {
                return Optional.empty();
            } else {
                return Optional.of(rightOfComment);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }


    private void processNonCommentHtml(String html, TemplateClass templateClass, HtmlTemplate template) {
        HtmlProcessors.REGULAR.process(html, templateClass, template);
    }
}
