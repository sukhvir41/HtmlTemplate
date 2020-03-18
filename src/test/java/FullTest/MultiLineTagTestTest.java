package FullTest;

public class MultiLineTagTestTest extends Test {


    @Override
    String getFilePath() {
        return "MultiLineTest.html";
    }

    @Override
    @org.junit.Test
    public void testMethod() {
        test();
    }

    @Override
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
                "</body>\n" +
                "</html>";
    }

    @Override
    String getTestName() {
        return "MultiLineTagTest";
    }

    @Override
    String getClassName() {
        return "MultiLineTest";
    }
}
