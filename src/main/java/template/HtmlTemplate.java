package template;

import processors.HtmlLineProcessor;
import processors.HtmlProcessorData;
import processors.HtmlProcessors;
import tags.HtmlTag;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public final class HtmlTemplate implements AutoCloseable {

    private BufferedReader reader;

    private final Deque<HtmlTag> tagsStack = new ArrayDeque<>();

    // template class being generated
    private TemplateClass templateClass;

    private final HtmlLineProcessor lineProcessor = new HtmlLineProcessor();

    private HtmlProcessors processor = HtmlProcessors.REGULAR;

    private File file;

    private boolean tagIncomplete;


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
        var className = getClassNameFromFile(file.getName());
        templateClass = new TemplateClass(className, this);
        return this;
    }

    private String getClassNameFromFile(String fileName) {
        var nameParts = fileName.split("\\.");
        return nameParts[0].replace(" ", "")
                .replace(".", "")
                .replace("-", "_");
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

            lineProcessor.setLine(line);

            while (lineProcessor.hasNextSection()) {

                var section = lineProcessor.getNextSection()
                        .trim();

                System.out.println(section + " | section");

                processor.process(
                        HtmlProcessorData.builder()
                                .setHtml(section)
                                .setTemplateClass(this.templateClass)
                                .setHtmlTemplate(this)
                                .build()
                );

            }

            lineProcessor.carryForwardUnprocessedString();

        }

        if (tagsStack.size() > 0) {
            var htmlTag = tagsStack.pop();
            System.out.println("the tag " + htmlTag.getName() + " is left open");
        }
    }


    public HtmlTag removeFromTagStack() {
        var last = this.tagsStack.removeLast();

        System.out.println("removed from stack " + last.getName());

        return last;
    }

    public void addToTagStack(HtmlTag htmlTag) {

        System.out.println("added to stack " + htmlTag.getName() + "  " + htmlTag.getHtml());

        this.tagsStack.add(htmlTag);
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
