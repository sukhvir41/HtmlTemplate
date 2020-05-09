package IntegrationTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

public class IfConditionTestTest {
    @Test
    public void testTrue() throws URISyntaxException {

        var file = TestUtils.getFile("IfConditionTest.html");

        var htmlTemplate = new HtmlTemplate();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile("IfConditionTest", theClassString);


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("isThisTrue", true);
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("If condition test true", output, TestUtils.strip(getExpectedOutputTrue()));

    }


    String getExpectedOutputTrue() {
        return "<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<h1> before if condition </h1>\n" +
                "<div>\n" +
                "    <h1> inside if condition </h1>\n" +
                "</div>\n" +
                "\n" +
                "<h1> after if condition </h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

    }

    @Test
    public void testFalse() throws URISyntaxException {

        var file = TestUtils.getFile("IfConditionTest.html");

        var htmlTemplate = new HtmlTemplate();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile("IfConditionTest", theClassString);


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("isThisTrue", false);
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("If condition test false", TestUtils.strip(getExpectedOutputFalse()), output);

    }

    String getExpectedOutputFalse() {
        return "<!DOCTYPE html>\n" +
                "\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<h1> before if condition </h1>\n" +
                "<h1> after if condition </h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

    }

}
