package IntegrationTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

public class TagClosingInAttributeTestTest extends TestUtils {


    String getFilePath() {
        return "TagClosingInAttributeTest.html";
    }


    @org.junit.Test
    public void testMethod() throws URISyntaxException {

        var file = TestUtils.getFile(getFilePath());
        var htmlTemplate = new HtmlTemplate();

        var theClass = Reflect.compile(getClassName(), htmlTemplate.setTemplate(file)
                .renderReflection());


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("render", writer);

        var output = strip(writer.toString());

        Assert.assertEquals(getTestName(), strip(getExpectedOutput()), output);
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
                "<a href=\" test.test.com \"\n" +
                "   test=\" test > test\">test </a>\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>";
    }


    String getTestName() {
        return "TagClosingInAttributeTest";
    }


    String getClassName() {
        return "TagClosingInAttributeTest";
    }
}
