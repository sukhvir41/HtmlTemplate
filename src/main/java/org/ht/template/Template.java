package org.ht.template;

import org.owasp.encoder.Encode;

import java.io.IOException;
import java.io.Writer;

public abstract class Template {

    public String encodeContent(String htmlContent) {
        return Encode.forHtmlContent(htmlContent);
    }

    public abstract void render(Writer writer) throws IOException;


}
