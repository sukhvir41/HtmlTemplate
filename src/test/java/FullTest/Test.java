package FullTest;

import org.joor.Reflect;
import org.junit.Assert;
import template.HtmlTemplate;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;

public abstract class Test {


    abstract String getFilePath();

    /**
     * this should only call test
     */
    abstract public void testMethod();

    abstract String getExpectedOutput();

    abstract String getTestName();

    abstract String getClassName();


    protected void test() {
        try {
            var file = new File(getClass().getClassLoader().getResource(getFilePath()).toURI());

            var htmlTemplate = new HtmlTemplate();

            var theClass = Reflect.compile(getClassName(), htmlTemplate.setTemplate(file)
                    .renderReflection());


            Writer writer = new StringWriter();
            theClass.call("render", writer);

            var output = strip(writer.toString());

            Assert.assertEquals(getTestName(), output, strip(getExpectedOutput()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String strip(String s) {
        return s.replace(" ", "")
                .replace("\n", "")
                .replace("\t", "");
    }

}
