package FullTest;

import org.ht.template.HtmlTemplate;
import org.ht.template.Parameters;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

public class MetaTypeTestTest extends FullTest.Test {


    @Override
    String getFilePath() {
        return "MetaTypeTest.html";
    }

    @Test
    @Override
    public void testMethod() throws URISyntaxException {

        var file = new File(getClass().getClassLoader().getResource(getFilePath()).toURI());
        var htmlTemplate = new HtmlTemplate();

        var theClass = Reflect.compile(getClassName(), htmlTemplate.setTemplate(file)
                .renderReflection());


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("render", writer);

        var output = strip(writer.toString());

        Assert.assertEquals(getTestName(), output, strip(getExpectedOutput()));

    }

    @Override
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

    @Override
    String getTestName() {
        return "MetaTypeTest";
    }

    @Override
    String getClassName() {
        return "MetaTypeTest";
    }


}
