package IntegrationTest;

import org.ht.template.HtmlTemplate;
import org.joor.Reflect;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;

public class DynamicContentTestTest {

    @Test
    public void nameTest() throws URISyntaxException {

        var file = TestUtils.getFile("DynamicContentTest.html");

        var htmlTemplate = new HtmlTemplate();
        var theClassString = htmlTemplate.setTemplate(file)
                .renderReflection();

        var theClass = Reflect.compile("DynamicContentTest", theClassString);

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
                "    <h1> blank value test \"\"</h1>\n" +
                "    <h1> multi content test 1 SAM and test 2 this is a test 2 and name in lowerCase\n" +
                "        sam\n" +
                "        and now having nested variable M\n" +
                "        and just a plain line in middle <br>\n" +
                "        and some html to escaped here &lt;h1&gt; this h1 tag escaped &lt;/h1&gt;\n" +
                "        and now @ in string of the dynamic content sdas@dfsdf.com\n" +
                "        and indeof test -1 \n" +
                "        equals test true \n" +
                "        unescaped content <h1> hello </h1> and escaped h1 &lt;h1&gt; this h1 tag escaped &lt;/h1&gt;\n" +
                "        unescaped content <h1> hello </h1> and escaped content &lt;h1&gt; this h1 tag escaped &lt;/h1&gt; and again unescaped content <h1> hello </h1>\n" +
                "        brackets inside test  }} }}}  and   }}} }}  and }}} }} and  }} }}} " +
                "        brackets inside test }} }}} }} }} }}  and  }}} }} }}} }}}  and }}} }} }}} }} and }} }}} }} }}}" +
                "        brackets inside test }} }}} }} }} &lt;h1&gt; hello &lt;/h1&gt; and  }}} }} }}} }}} <h1> hello </h1>" +
                "        brackets inside test }} }}} }} }} &lt;h1&gt; hello &lt;/h1&gt; &quot; }} and  }}} }} }}} }}} <h1> hello </h1> \" }}} " +
                "    </h1>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>";
    }

}
