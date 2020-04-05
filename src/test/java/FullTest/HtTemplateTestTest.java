package FullTest;

import org.ht.template.HtmlTemplate;
import org.ht.template.Testing;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

import static FullTest.TestUtils.strip;

public class HtTemplateTestTest {

    private final String expectedOutPut = "" +
            "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h1>after this tag we will import a template</h1>\n" +
            "\n" +
            "<h1> we are inside the imported template file</h1>\n" +
            "\n" +
            "<div>\n" +
            "    test template\n" +
            "</div>" +
            "\n" +
            "<h1>this is after the import template</h1>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

    @Test
    public void testTemplate() throws URISyntaxException {
        var file = TestUtils.getFile("HtTemplateTest.html");

        var theClass = Reflect.compile("HtTemplateTest",
                new HtmlTemplate().setTemplate(file)
                        .renderReflection()
        );

        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("render", writer);

        var output = strip(writer.toString());

        Assert.assertEquals("Ht-template test", output, TestUtils.strip(expectedOutPut));

    }


}
