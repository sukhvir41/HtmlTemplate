/*
 * Copyright 2020 Sukhvir Thapar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package IntegrationTest;

import com.github.sukhvir41.TestUtils;
import com.github.sukhvir41.template.HtmlTemplateLoader;
import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.Map;

public class DynamicContentTestTest {

    @Test
    public void nameTest() throws URISyntaxException {

        var file = TestUtils.getFile("DynamicContentTest.html");

        var output = TestUtils.strip(
                HtmlTemplateLoader.load(file)
                        .render(Map.of("name", "SAM"))
        );

        Assert.assertEquals("Dynamic Content test", TestUtils.strip(getExpectedOutput()), output);

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
