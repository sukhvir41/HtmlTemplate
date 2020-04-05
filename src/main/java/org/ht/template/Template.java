package org.ht.template;

import org.owasp.encoder.Encode;

import java.io.IOException;
import java.io.Writer;
import java.util.function.Supplier;

public abstract class Template {

    public String encodeContent(String htmlContent) {
        return Encode.forHtmlContent(htmlContent);
    }

    public abstract void render(Writer writer) throws IOException;

    public String content(Supplier<String> theContent) {
        try {
            var value = theContent.get();
            if (value == null) {
                return "";
            } else {
                return encodeContent(value);
            }
        } catch (Exception e) {
            return "";
        }
    }

    public String unescapedContent(Supplier<String> theContent) {
        try {
            var value = theContent.get();
            if (value == null) {
                return "";
            } else {
                return value;
            }
        } catch (Exception e) {
            return "";
        }
    }


}
