package FullTest;

public class TagClosingInAttributeTestTest extends Test {
    @Override
    String getFilePath() {
        return "TagClosingInAttributeTest.html";
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

    @Override
    String getTestName() {
        return "TagClosingInAttributeTest";
    }

    @Override
    String getClassName() {
        return "TagClosingInAttributeTest";
    }
}
