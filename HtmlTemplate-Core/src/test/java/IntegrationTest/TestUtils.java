package IntegrationTest;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestUtils {


    public static String strip(String s) {
        return s.replace(" ", "")
                .replace("\n", "")
                .replace("\t", "");
    }

    public static Path getFile(String fileName) throws URISyntaxException {
        return Paths.get(TestUtils.class.getClassLoader().getResource(fileName).toURI());
    }


}
