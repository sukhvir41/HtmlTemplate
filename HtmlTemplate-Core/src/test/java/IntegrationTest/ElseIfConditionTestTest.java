package IntegrationTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

public class ElseIfConditionTestTest {
    @Test
    public void testTrue() throws URISyntaxException {

        var file = TestUtils.getFile("ElseIfConditionTest.html");

        var htmlTemplate = new HtmlTemplate();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();


        var theClass = Reflect.compile("ElseIfConditionTest", theClassString);


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("showIf", true);
        instance.call("showElseIf", false);
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("Else If condition test if = true", TestUtils.strip(getExpectedOutputTrue()), output);

    }


    String getExpectedOutputTrue() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>\n" +
                "    if statement\n" +
                "</h1>\n" +
                "</body>\n" +
                "</html>";

    }

    @Test
    public void testFalse() throws URISyntaxException {

        var file = TestUtils.getFile("ElseIfConditionTest.html");

        var htmlTemplate = new HtmlTemplate();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile("ElseIfConditionTest", theClassString);


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("showIf", false);
        instance.call("showElseIf", true);
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("ELse If condition test else if false", TestUtils.strip(getExpectedOutputFalse()), output);

    }

    String getExpectedOutputFalse() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>\n" +
                "    else if statement\n" +
                "</h1>\n" +
                "</body>\n" +
                "</html>";

    }

}
