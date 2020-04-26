package org.ht.template;

import org.ht.processors.HtmlLineProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Testing {


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {

        //createClass();
        //
        printLine();
        //System.out.println(HtStringUtils.findIndex(0,"c","\"abc\"c"));

        //printNewClass();''


    }

    private static void printNewClass() {
        var theClass = new TemplateClass("test", "test", new HtmlTemplate());

        theClass.addVariable("theClassName", "String");

        theClass.appendPlainHtml("<html>");
        theClass.addCode("if (true) {");
        theClass.incrementFunctionIndentation();
        theClass.appendPlainHtml("<head>");
        theClass.appendPlainHtml("</head>");
        theClass.decrementFunctionIndentation();
        theClass.addCode("}");
        theClass.appendPlainHtml("</html>");

        System.out.println(theClass.generateReflectionClass());


    }


    static void test(Object o) {
        System.out.println(o.getClass().getName());
    }

    static void createClass() throws IOException {

        var string = new HtmlTemplate()
                .setTemplate(new File("src/test/resources/SimpleTest1.html"))
                .render();

        System.out.println(string);
    }


    static void printLine() throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("src/test/resources/DynamicContentTest.html"))) {
            String line;

            var proc = new HtmlLineProcessor();

            while ((line = reader.readLine()) != null) {
                //System.out.println("processing line ->" + line);
                proc.setLine(line);

                while (proc.hasNextSection()) {
                    System.out.println("|" + proc.getNextSection() + "|");
                }

                proc.carryForwardUnprocessedString();
            }

        }
    }


}
