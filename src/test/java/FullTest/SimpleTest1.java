package FullTest;

import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;
import template.HtmlTemplate;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

public class SimpleTest1 {

    private final String expectedOutput = "" +
                                          "<html>\n" +
                                          "<head>\n" +
                                          "    <title>SimpleTest1</title>\n" +
                                          "</head>\n" +
                                          "</html>";


    @Test
    public void test() throws URISyntaxException {

        var file = new File(getClass().getClassLoader().getResource("SimpleTest1.html").toURI());

        var htmlTemplate = new HtmlTemplate();

        var theClass = Reflect.compile("SimpleTest1", htmlTemplate.setTemplate(file)
                .render());

        Writer writer = new StringWriter();
        theClass.call("render", writer);

        var output = strip(writer.toString());

        Assert.assertEquals("Simple test 1 ", output, strip(expectedOutput));
    }

    private String strip(String s) {
        return s.replace(" ", "")
                .replace("\n", "")
                .replace("\t", "");
    }

}
