package org.ht.template;

import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public abstract class Template {

    public String encodeContent(String htmlContent) {
        return StringEscapeUtils.escapeHtml4(htmlContent);
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

    public boolean condition(Supplier<Boolean> thePredicate) {
        try {
            return thePredicate.get();
        } catch (Exception e) {
            return false;
        }
    }

    public <Type> void forEach(Iterator<Type> iterator, BiConsumer<Integer, Type> consume) {
        int index = 0;
        while (iterator.hasNext()) {
            consume.accept(index, iterator.next());
            ++index;
        }
    }

    public <Type> void forEach(Collection<Type> collection, BiConsumer<Integer, Type> consume) {
        int index = 0;
        for (Type type : collection) {
            consume.accept(index, type);
            ++index;
        }
    }

    public <Key, Value> void forEach(Map<Key, Value> map, TriConsumer<Integer, Key, Value> consume) {
        int index = 0;
        for (Map.Entry<Key, Value> item : map.entrySet()) {
            consume.accept(index, item.getKey(), item.getValue());
            ++index;
        }
    }

    public <Type> void forEach(Type[] array, BiConsumer<Integer, Type> consume) {
        int index = 0;
        for (Type element : array) {
            consume.accept(index, element);
            ++index;
        }
    }

}
