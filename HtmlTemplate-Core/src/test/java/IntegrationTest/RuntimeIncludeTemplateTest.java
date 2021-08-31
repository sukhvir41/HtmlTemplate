package IntegrationTest;

import com.github.sukhvir41.TestUtils;
import com.github.sukhvir41.core.settings.SettingOptions;
import com.github.sukhvir41.core.settings.SettingsManager;
import com.github.sukhvir41.template.HtmlTemplate;
import com.github.sukhvir41.template.HtmlTemplateLoader;
import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;
import java.util.Map;

import static com.github.sukhvir41.TestUtils.strip;

public class RuntimeIncludeTemplateTest {

    @Test
    public void includeTest() throws URISyntaxException {
        HtmlTemplate htmlTemplate = HtmlTemplateLoader.load(TestUtils.getFile("HtIncludeTest.html"), SettingsManager.load(Map.of(SettingOptions.LOGGING_LEVEL, "INFO")));

        String renderedTemplate = htmlTemplate.render(Map.of("number", 10));

        Assert.assertEquals(strip("<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "\n" +
                "<h1> we are inside the imported template file</h1>\n" +
                "<h1>20: the number</h1>\n" +
                "<div>\n" +
                "    test template\n" +
                "</div>" +
                "\n" +
                "<h1> we are inside the imported template file</h1>\n" +
                "<h1>30: the number</h1>\n" +
                "<div>\n" +
                "    test template\n" +
                "</div>" +
                "\n" +
                "</body>\n" +
                "</html>"), strip(renderedTemplate));


    }
}
