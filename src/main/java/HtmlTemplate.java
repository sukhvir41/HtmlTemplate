import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class HtmlTemplate implements AutoCloseable {

    private BufferedReader reader;
    private Deque<HtmlTag> tagsStack = new ArrayDeque<>();
    private TemplateClass templateClass;
    protected HtmlBreaks htmlBeingProcessed;

    public Deque<HtmlTag> getTagsStack() {
        return tagsStack;
    }

    public HtmlTemplate setTemplate(File template) {
        try {
            reader = Files.newBufferedReader(template.toPath());
            templateClass = new TemplateClass("Test", this);
            htmlBeingProcessed = HtmlBreaks.REGULAR;
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HtmlTemplate setTemplate(String template, String name) {
        var stringReader = new StringReader(template);
        reader = new BufferedReader(stringReader);
        templateClass = new TemplateClass(name, this);
        htmlBeingProcessed = HtmlBreaks.REGULAR;
        return this;
    }


    public String render() {
        try {
            read();
            return templateClass.generateClass();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            close();
        }
    }

    private void createTemplateClass() {

    }

    private void read() throws IOException {
        String line;

        while ((line = reader.readLine()) != null) {
            if (HtmlUtils.containsHtmlComment(line)) {
                htmlBeingProcessed = HtmlBreaks.COMMENT;
            } else {
                processRegularTag(line);
            }

        }

        if (tagsStack.size() > 0) {
            var htmlTag = tagsStack.pop();
            System.out.println("the tag " + htmlTag.getName() + " is left open");
        }
    }

    private void processHtmlComment(String line) {

    }


    private void processRegularTag(String line) {
        String[] lineParts = line.split(">");
        for (var tagString : lineParts) {
            processHtmlTag(tagString);
        }
    }

    private void processHtmlTag(String tagString) {
        if (!tagString.isBlank()) {

            if (HtmlUtils.isDocTypeTag(tagString)) {
                templateClass.appendString(tagString + ">");
                return;
            }

            Content.parseContent(tagString)
                    .ifPresent(content -> templateClass.appendContent(content));

            HtmlTag.parse(tagString)
                    .ifPresent(this::addOrRemoveHtmlTagFromStack);

        }
    }

    private void addOrRemoveHtmlTagFromStack(HtmlTag htmlTag) {


        if (htmlTag.isClosingTag()) {
            Optional.ofNullable(tagsStack.peek())
                    .filter(tag -> tag.isClosingTag(htmlTag))
                    .ifPresentOrElse(tag -> tagsStack.pop(), () -> {
                        throw new RuntimeException("Miss matched closing tag " + htmlTag.getName());
                    });
            templateClass.appendHtmlTag(htmlTag);
        } else {
            templateClass.appendHtmlTag(htmlTag);
            tagsStack.push(htmlTag);
        }


    }


    @Override
    public void close() {
        try {
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
