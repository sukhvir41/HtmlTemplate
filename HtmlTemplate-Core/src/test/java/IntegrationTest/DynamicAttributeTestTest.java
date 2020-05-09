package IntegrationTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

import static IntegrationTest.TestUtils.strip;

public class DynamicAttributeTestTest {


    private String fileName = "DynamicAttributeTest.html";
    private String className = "DynamicAttributeTest";
    private String testName = "DynamicAttributeTest";

    @Test
    public void testMethod() throws URISyntaxException {

        var file = TestUtils.getFile(fileName);
        var htmlTemplate = new HtmlTemplate();

        var classString = htmlTemplate.setTemplate(file)
                .renderReflection();
        var theClass = Reflect.compile(className, classString);

        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("isTrue", true);
        instance.call("render", writer);

        var output = strip(writer.toString());

        Assert.assertEquals(testName, strip(expectedOutput), output);

    }

    private String expectedOutput = "" +
            "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Title</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "\n" +
            "<h1 isTrue=\"true\" isFalse=\"false\">\n" +
            "    dynamic attribute test\n" +
            "</h1>\n" +
            "\n" +
            "<h1 isTrue=\"true\" isFalse=\"false\">\n" +
            "    dynamic attribute test\n" +
            "</h1>\n" +
            "\n" +
            "<h1 isTrue=\"true\" isFalse=\"false\">\n" +
            "    dynamic attribute test\n" +
            "</h1>\n" +
            "\n" +
            "</body>\n" +
            "</html>";

}
