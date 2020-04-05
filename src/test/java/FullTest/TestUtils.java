package FullTest;

import java.io.File;
import java.net.URISyntaxException;

public class TestUtils {


    public static String strip(String s) {
        return s.replace(" ", "")
                .replace("\n", "")
                .replace("\t", "");
    }

    public static File getFile(String fileName) throws URISyntaxException {
        return new File(TestUtils.class.getClassLoader().getResource(fileName).toURI());
    }


}
