package template;

import processors.HtmlProcessors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
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

    private File file;


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
        this.file = template;
        templateClass = new TemplateClass("Test", this);
        return this;
    }


    private TemplateClass getTemplateClass() {
        return this.templateClass;
    }


    public String render() {
        try {
            reader = Files.newBufferedReader(file.toPath());
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
     *
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


    public HtmlTag removeFromTagStack() {
        return this.tagsStack.removeLast();
    }

    public void addToTagStack(HtmlTag htmlTag) {

        this.tagsStack.add(htmlTag);
    }

    public void printStack() {
        this.tagsStack
                .forEach(System.out::println);
    }

    public Optional<HtmlTag> peekTagStack() {

        try {
            return Optional.ofNullable(this.tagsStack.peekLast());
        } catch (Exception e) {
            return Optional.empty();
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
