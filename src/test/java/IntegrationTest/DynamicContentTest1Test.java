package IntegrationTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

public class DynamicContentTest1Test {

    @Test
    public void nameTest() throws URISyntaxException {

        var file = TestUtils.getFile("DynamicContentTest1.html");

        var htmlTemplate = new HtmlTemplate();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();


        var theClass = Reflect.compile("DynamicContentTest1", theClassString);


        Writer writer = new StringWriter();
        var instance = theClass.call("getInstance");
        instance.call("name", "SAM");
        instance.call("render", writer);

        var output = TestUtils.strip(writer.toString());

        Assert.assertEquals("Dynamic Content test", output, TestUtils.strip(getExpectedOutput()));

    }


    String getExpectedOutput() {
        return "" +
                "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Dynamic Content test</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>This is a dynamic content test</h1>\n" +
                "<div>\n" +
                "    <h1> hello SAM </h1>\n" +
                "    <h1> blank value test \"\"</h1>" +
                "    <h1> multi content test 1 SAM and test 2 this is a test 2 and name in lowerCase\n" +
                "        sam \n" +
                "        and now having nested variable M " +
                "        and just a plain line in middle <br> " +
                "        and some html to escaped here &lt;h1&gt; this h1 tag escaped &lt;/h1&gt;" +
                "        and now @ in string of the dynamic content sdas@dfsdf.com" +
                "        and indeof test -1" +
                "        equals test true" +
                "        unescaped content <h1> hello </h1> and escaped h1 &lt;h1&gt; this h1 tag escaped &lt;/h1&gt;" +
                "   </h1>" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

}
