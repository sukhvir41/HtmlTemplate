package IntegrationTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

import static IntegrationTest.TestUtils.strip;

public class MetaImportTestTest {


    private String fileName = "MetaImportTest.html";
    private String className = "MetaImportTest";
    private String testName = "Meta Import statement test";

    @Test
    public void testMethod() throws URISyntaxException {

        var file = TestUtils.getFile(fileName);
        var htmlTemplate = new HtmlTemplate();

        var classString = htmlTemplate.setTemplate(file)
                .renderReflection();
        var theClass = Reflect.compile(className, classString);

        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("render", writer);

        var output = strip(writer.toString());
        // can't test for imports as testing through reflection is not possible. So, if this compiles then assuming
        // everything went fine
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
            "<h1>meta import test</h1>\n" +
            "</body>\n" +
            "</html>";

}
