package org.ht.template;

import java.io.IOException;
import java.io.Writer;

public class TemplateClassExample extends Template {

    private static final String PLAINT_HTML_0 = "<html>";
    private static final String PLAINT_HTML_1 = "</html>";


    @Override
    public void render(Writer writer) throws IOException {
        writer.append(PLAINT_HTML_0);
        if (true) {
            writer.append(PLAINT_HTML_1);
        }
    }
}
