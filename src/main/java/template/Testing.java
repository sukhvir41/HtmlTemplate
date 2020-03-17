package template;

import processors.HtmlLineProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Testing {


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {


        /*var string = new HtmlTemplate()
                .setTemplate(new File("test.html"))
                .render();

        var file = new File("src/main/java/Test.java");
        if (!file.exists()) {
            file.createNewFile();
        }
        Files.writeString(file.toPath(), string, StandardOpenOption.TRUNCATE_EXISTING);*/

        try (BufferedReader reader = Files.newBufferedReader(Paths.get("test.html"))) {
            String line;

            var proc = new HtmlLineProcessor();

            while ((line = reader.readLine()) != null) {
                System.out.println("processing line ->" + line);

                proc.setLine(line);

                while (proc.hasNextSection()) {
                    System.out.println("|" + proc.getNextSection() + "|");
                }

                proc.carryForwardUnprocessedString();
            }

        }


    }


}
