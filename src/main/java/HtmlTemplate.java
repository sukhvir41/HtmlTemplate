import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Stack;

public class HtmlTemplate implements AutoCloseable {

    private BufferedReader reader;
    private StringReader stringReader;
    private Stack<HtmlTag> tagsStack = new Stack<>();
    private TemplateClass templateClass;

    public HtmlTemplate setTemplate(File template) {
        try {
            reader = Files.newBufferedReader(template.toPath());
            templateClass = new TemplateClass("Test");

            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HtmlTemplate setTemplate(String template) {
        stringReader = new StringReader(template);
        reader = new BufferedReader(stringReader);
        templateClass = new TemplateClass("Test");

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
            String[] lineParts = line.split(">");
            for (var tagString : lineParts) {
                processTag(tagString);
            }
        }

        if (tagsStack.size() > 0) {
            var htmlTag = tagsStack.pop();
            System.out.println("the tag " + htmlTag.getName() + " is left open");
        }
    }

    private void processTag(String tagString) {
        if (!tagString.isBlank()) {
            var htmlTag = new HtmlTag(tagString);

            templateClass.appendString(htmlTag.toString());

            if (!htmlTag.isClosingTag()) {
                tagsStack.push(htmlTag);
            } else {
                var stackHtmlTag = tagsStack.peek();
                if (stackHtmlTag.isClosingTag(htmlTag)) {
                    tagsStack.pop();
                }
            }
        }
    }


    @Override
    public void close() {
        try {
            reader.close();
            if (Objects.nonNull(stringReader)) {
                stringReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
