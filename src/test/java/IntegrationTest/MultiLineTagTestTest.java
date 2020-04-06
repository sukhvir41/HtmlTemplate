package IntegrationTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

public class MultiLineTagTestTest extends TestUtils {


    String getFilePath() {
        return "MultiLineTest.html";
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

        Assert.assertEquals(getTestName(), output, strip(getExpectedOutput()));
    }


    String getExpectedOutput() {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>InCompleteTest</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "<a href=\"www.google.com \"></a>\n" +
                "\n" +
                "<div>\n" +
                "    this is a test\n" +
                "</div>\n" +
                "\n" +
                "<div test =\"  dfsdf > fdfsd \">\n" +
                "</div>" +
                "</body>\n" +
                "</html>";
    }


    String getTestName() {
        return "MultiLineTagTest";
    }


    String getClassName() {
        return "MultiLineTest";
    }
}
