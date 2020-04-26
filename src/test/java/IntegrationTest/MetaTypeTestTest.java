package IntegrationTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

import static IntegrationTest.TestUtils.getFile;
import static IntegrationTest.TestUtils.strip;

public class MetaTypeTestTest {


    String getFilePath() {
        return "MetaTypeTest.html";
    }

    @Test
    public void testMethod() throws URISyntaxException {

        var file = TestUtils.getFile(getFilePath());
        var htmlTemplate = new HtmlTemplate();

        var classString = htmlTemplate.setTemplate(file)
                .renderReflection();
        var theClass = Reflect.compile(getClassName(), classString);

        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("render", writer);

        var output = strip(writer.toString());

        Assert.assertEquals(getTestName(), output, strip(getExpectedOutput()));

    }

    @Test
    public void testType() throws URISyntaxException {

        var file = getFile(getFilePath());
        var htmlTemplate = new HtmlTemplate();

        var theClass = Reflect.compile(getClassName(), htmlTemplate.setTemplate(file)
                .renderReflection());


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("render", writer);

        instance.call("name", "world");

        Assert.assertEquals("type test", "world", instance.call("name").get());

    }


    String getExpectedOutput() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>\n" +
                "    checking content </h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }


    String getTestName() {
        return "MetaTypeTest";
    }

    String getClassName() {
        return "MetaTypeTest";
    }


}
