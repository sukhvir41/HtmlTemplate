package IntegrationTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

public class IfElseConditionTestTest {

    @Test
    public void testTrue() throws URISyntaxException {

        var file = TestUtils.getFile("IfElseConditionTest.html");

        var htmlTemplate = new HtmlTemplate();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile("IfElseConditionTest", theClassString);

        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("show", true);
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("If condition test true", TestUtils.strip(getExpectedOutputTrue()), output);

    }


    String getExpectedOutputTrue() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>\n" +
                "    this is before if else\n" +
                "</h1>\n" +
                "\n" +
                "<h1>\n" +
                "    inside if statement\n" +
                "</h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

    }

    @Test
    public void testFalse() throws URISyntaxException {

        var file = TestUtils.getFile("IfElseConditionTest.html");

        var htmlTemplate = new HtmlTemplate();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile("IfElseConditionTest", theClassString);


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("show", false);
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("If condition test false", output, TestUtils.strip(getExpectedOutputFalse()));

    }

    String getExpectedOutputFalse() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<h1>\n" +
                "    this is before if else\n" +
                "</h1>\n" +
                "\n" +
                "<h1>\n" +
                "    inside else statement\n" +
                "</h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

    }
}
