package template;

import processors.HtmlProcessors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public final class HtmlTemplate implements AutoCloseable {

    private BufferedReader reader;

    private Deque<HtmlTag> tagsStack = new ArrayDeque<>();

    // template class being generated
    private TemplateClass templateClass;

    private HtmlProcessors processor;

    public HtmlTemplate() {
        this.processor = HtmlProcessors.REGULAR;
    }

    public void setProcessor(HtmlProcessors processor) {
        this.processor = processor;
    }

    public HtmlProcessors getProcessor() {
        return processor;
    }

    int getTagsStackSize() {
        return tagsStack.size();
    }

    public HtmlTemplate setTemplate(File template) {
        try {
            reader = Files.newBufferedReader(template.toPath());
            templateClass = new TemplateClass("template.Test", this);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private TemplateClass getTemplateClass() {
        return this.templateClass;
    }


    public HtmlTemplate setTemplate(String template, String name) {
        var stringReader = new StringReader(template);
        reader = new BufferedReader(stringReader);
        templateClass = new TemplateClass(name, this);
        return this;
    }


    public String render() {
        try {
            read();
            return getTemplateClass().generateClass();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            close();
        }
    }


    /**
     * starts reading the file and uses the specified processor to process the line.
     * @throws IOException
     */
    private void read() throws IOException {
        String line;

        while ((line = reader.readLine()) != null) {
            this.processor.process(line, templateClass, this);
        }

        if (tagsStack.size() > 0) {
            var htmlTag = tagsStack.pop();
            System.out.println("the tag " + htmlTag.getName() + " is left open");
        }
    }

    public void addOrRemoveHtmlTagFromStack(HtmlTag htmlTag) {

        if (htmlTag.isClosingTag()) {
            Optional.ofNullable(tagsStack.peek())
                    .filter(tag -> tag.isClosingTag(htmlTag))
                    .ifPresentOrElse(tag -> tagsStack.pop(), () -> {
                        throw new RuntimeException("Miss matched closing tag " + htmlTag.getName());
                    });
            getTemplateClass().appendHtmlTag(htmlTag);
        } else {
            getTemplateClass().appendHtmlTag(htmlTag);
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
