package FullTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

import static FullTest.TestUtils.getFile;
import static FullTest.TestUtils.strip;

public class SimpleTest1 {

    private final String expectedOutput = "" +
            "<html>\n" +
            "<head>\n" +
            "    <title>SimpleTest1</title>\n" +
            "</head>\n" +
            "</html>";


    @Test
    public void test() throws URISyntaxException {

        var file = getFile("SimpleTest1.html");

        var htmlTemplate = new HtmlTemplate();

        var theClass = Reflect.compile("SimpleTest1", htmlTemplate.setTemplate(file)
                .render());

        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("render", writer);

        var output = strip(writer.toString());

        Assert.assertEquals("Simple test 1 ", output, strip(expectedOutput));
    }

}
