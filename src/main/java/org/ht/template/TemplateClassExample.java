package org.ht.template;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class TemplateClassExample extends Template {

    private static final String PLAINT_HTML_0 = "<html>";
    private static String PLAINT_HTML_1 = "</html>";

    public static String getPlaintHtml1() {
        return PLAINT_HTML_1;
    }

    public static void setPlaintHtml1(String plaintHtml1) {
        PLAINT_HTML_1 = plaintHtml1;
    }

    @Override
    public void render(Writer writer) throws IOException {
        writer.append(PLAINT_HTML_0);

    }
}
