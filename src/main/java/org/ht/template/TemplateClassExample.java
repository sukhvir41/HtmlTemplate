package org.ht.template;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;

public class TemplateClassExample extends Template {

    private static final String PLAINT_HTML_0 = "<html>";
    private static String PLAINT_HTML_1 = "</html>";

    @Override
    public void render(Writer writer) throws IOException {
        writer.append(PLAINT_HTML_0);

        forEach(Collections.emptySet(), (index, item) -> {
            writer.append(PLAINT_HTML_1);
            writer.append(PLAINT_HTML_1);
        });

    }
}
