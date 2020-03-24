package org.ht.template;

import org.apache.commons.lang3.StringUtils;
import org.ht.processors.HtmlLineProcessor;
import org.ht.processors.HtmlProcessorData;
import org.ht.processors.HtmlProcessors;
import org.ht.tags.HtmlTag;
import org.ht.tags.HtmlUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.*;

public final class HtmlTemplate {


    private final Deque<HtmlTag> tagsStack = new ArrayDeque<>();

    // org.ht.template class being generated
    private TemplateClass templateClass;

    private HtmlProcessors processor = HtmlProcessors.REGULAR;
    private File file;
    protected Map<String, String> types = new HashMap<>();

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
        //templateClass = new TemplateClass(className, this);
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
            readFile(file);
            return getTemplateClass().generateClass();
        } catch (IOException e) {
            return "";
        }
    }

    public String renderReflection() {
        try {
            readFile(file);
            return getTemplateClass().generateReflectionClass();
        } catch (IOException e) {
            return "";
        }
    }

    private void readFile(File file) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            read(reader);
        }
    }


    /**
     * starts reading the file and uses the specified processor to process the line.
     *
     * @throws IOException
     */
    private void read(BufferedReader reader) throws IOException {

        HtmlLineProcessor lineProcessor = new HtmlLineProcessor();

        String line;
        while ((line = reader.readLine()) != null) {

            lineProcessor.setLine(line);

            while (lineProcessor.hasNextSection()) {

                String section = lineProcessor.getNextSection()
                        .trim();

                if (processor == HtmlProcessors.REGULAR) {
                    if (HtmlUtils.isTemplateMetaTag(section)) {
                        readTemplate(section);
                    } else {
                        processSection(section);
                    }
                } else {
                    processSection(section);
                }
            }

            lineProcessor.carryForwardUnprocessedString();

        }

        if (tagsStack.size() > 0) {
            var htmlTag = tagsStack.pop();
            System.out.println("the tag " + htmlTag.getName() + " is left open");
        }
    }

    private void readTemplate(String section) {
        String filePath = getFilePathFromTemplateMetaTag(section);
        try {
            readFile(new File(filePath));
        } catch (IOException e) {
            System.err.println("Problem in reading file " + filePath);
        }
    }

    //todo : implement this
    private String getFilePathFromTemplateMetaTag(String section) {
        return "";
    }

    private void processSection(String section) {
        processor.process(
                HtmlProcessorData.builder()
                        .setHtml(section)
                        .setTemplateClass(this.templateClass)
                        .setHtmlTemplate(this)
                        .build()
        );
    }


    public HtmlTag removeFromTagStack() {
        var last = this.tagsStack.removeLast();

        return last;
    }

    public void addToTagStack(HtmlTag htmlTag) {
        this.tagsStack.add(htmlTag);
    }

    public Optional<HtmlTag> peekTagStack() {
        try {
            return Optional.ofNullable(this.tagsStack.peekLast());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void addType(String name, String theClass) {
        if (StringUtils.isNoneBlank(name, theClass)) {
            this.types.put(name, theClass);
        }
    }

    public String getType(String name) {
        return this.types.getOrDefault(name, "");
    }


}
