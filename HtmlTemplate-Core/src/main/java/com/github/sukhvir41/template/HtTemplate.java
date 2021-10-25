/*
 * Copyright 2020 Sukhvir Thapar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.sukhvir41.template;


import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public abstract class HtTemplate {

    private final String EMPTY_STRING = "";

    public final String content(Supplier<String> theContent) {
        try {
            String value = theContent.get();
            if (value == null) {
                return EMPTY_STRING;
            } else {
                return encodeContent(value);
            }
        } catch (Exception e) {
            return EMPTY_STRING;
        }
    }

    public final String content(int i) {
        return EMPTY_STRING + i;
    }

    public final String content(long i) {
        return EMPTY_STRING + i;
    }

    public final String content(float i) {
        return EMPTY_STRING + i;
    }

    public final String content(double i) {
        return EMPTY_STRING + i;
    }

    public final String content(String s) {
        if (s == null) {
            return EMPTY_STRING;
        } else {
            return encodeContent(s);
        }
    }

    public final String content(Object s) {
        if (s == null) {
            return EMPTY_STRING;
        } else {
            return encodeContent(s.toString());
        }
    }

    public final String content(Object[] s) {
        if (s == null)
            return EMPTY_STRING;

        int iMax = s.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(s[i]);
            if (i == iMax)
                return encodeContent(b.append(']').toString());
            b.append(", ");
        }
    }

    // reference https://cheatsheetseries.owasp.org/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.html#rule-1-html-encode-before-inserting-untrusted-data-into-html-element-content
    // used rocker template html encoder as reference
    private String encodeContent(String htmlContent) {
        int length = htmlContent.length();
        if (length == 0) {
            return htmlContent;
        } else {
            char c;
            String replace = null;
            StringBuilder builder = null;
            int lastPositionUpdated = 0;
            for (int i = 0; i < length; i++) {
                c = htmlContent.charAt(i);
                switch (c) {
                    case '&':
                        replace = "&amp;";
                        break;
                    case '<':
                        replace = "&lt;";
                        break;
                    case '>':
                        replace = "&gt;";
                        break;
                    case '"':
                        replace = "&quot;";
                        break;
                    case '\'':
                        replace = "&#x27;";
                        break;
                    default:
                        replace = null;
                }

                if (replace != null) {
                    if (builder == null) {
                        builder = new StringBuilder(length);
                    }
                    builder.append(htmlContent, lastPositionUpdated, i);
                    builder.append(replace);
                    lastPositionUpdated = i + 1;
                }

            }

            if (builder == null) {
                return htmlContent;
            } else if (lastPositionUpdated < length) {
                builder.append(htmlContent, lastPositionUpdated, length);
                return builder.toString();
            } else {
                return builder.toString();
            }

        }

    }

    public final String unescapedContent(Supplier<String> theContent) {
        try {
            String value = theContent.get();
            if (value == null) {
                return EMPTY_STRING;
            } else {
                return value;
            }
        } catch (Exception e) {
            return EMPTY_STRING;
        }
    }

    public final String unescapedContent(int i) {
        return EMPTY_STRING + i;
    }

    public final String unescapedContent(long i) {
        return EMPTY_STRING + i;
    }

    public final String unescapedContent(float i) {
        return EMPTY_STRING + i;
    }

    public final String unescapedContent(double i) {
        return EMPTY_STRING + i;
    }

    public final String unescapedContent(String s) {
        if (s == null) {
            return EMPTY_STRING;
        } else {
            return s;
        }
    }

    public final String unescapedContent(Object s) {
        if (s == null) {
            return EMPTY_STRING;
        } else {
            return s.toString();
        }
    }

    public final String unescapedContent(Object[] s) {
        if (s == null)
            return EMPTY_STRING;

        int iMax = s.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append(s[i]);
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    public final boolean condition(Supplier<Boolean> thePredicate) {
        try {
            return thePredicate.get();
        } catch (Exception e) {
            return false;
        }
    }

    public final <TYPE> void forEach(Iterator<TYPE> iterator, HtConsumer<TYPE> consume) {
        try {
            while (iterator.hasNext()) {
                consume.accept(iterator.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final <TYPE> void forEach(Iterator<TYPE> iterator, HtBiConsumer<Integer, TYPE> consume) {
        try {
            int index = 0;
            while (iterator.hasNext()) {
                consume.accept(index, iterator.next());
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final <TYPE> void forEach(Collection<TYPE> collection, HtConsumer<TYPE> consume) {
        try {
            for (TYPE type : collection) {
                consume.accept(type);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final <TYPE> void forEach(Collection<TYPE> collection, HtBiConsumer<Integer, TYPE> consume) {
        try {
            int index = 0;
            for (TYPE type : collection) {
                consume.accept(index, type);
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final <KEY, VALUE> void forEach(Map<KEY, VALUE> map, HtBiConsumer<KEY, VALUE> consume) {
        try {
            for (Map.Entry<KEY, VALUE> item : map.entrySet()) {
                consume.accept(item.getKey(), item.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final <KEY, VALUE> void forEach(Map<KEY, VALUE> map, HtTriConsumer<Integer, KEY, VALUE> consume) {
        try {
            int index = 0;
            for (Map.Entry<KEY, VALUE> item : map.entrySet()) {
                consume.accept(index, item.getKey(), item.getValue());
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final <TYPE> void forEach(TYPE[] array, HtConsumer<TYPE> consume) {
        try {
            for (TYPE element : array) {
                consume.accept(element);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final <TYPE> void forEach(TYPE[] array, HtBiConsumer<Integer, TYPE> consume) {
        try {
            int index = 0;
            for (TYPE element : array) {
                consume.accept(index, element);
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void forEach(int[] array, HtConsumer<Integer> consume) {
        try {
            for (int element : array) {
                consume.accept(element);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void forEach(int[] array, HtBiConsumer<Integer, Integer> consume) {
        try {
            int index = 0;
            for (int element : array) {
                consume.accept(index, element);
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void forEach(long[] array, HtConsumer<Long> consume) {
        try {
            for (long element : array) {
                consume.accept(element);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void forEach(long[] array, HtBiConsumer<Integer, Long> consume) {
        try {
            int index = 0;
            for (long element : array) {
                consume.accept(index, element);
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void forEach(float[] array, HtConsumer<Float> consume) {
        try {
            for (float element : array) {
                consume.accept(element);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void forEach(float[] array, HtBiConsumer<Integer, Float> consume) {
        try {
            int index = 0;
            for (float element : array) {
                consume.accept(index, element);
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void forEach(double[] array, HtConsumer<Double> consume) {
        try {
            for (double element : array) {
                consume.accept(element);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final void forEach(double[] array, HtBiConsumer<Integer, Double> consume) {
        try {
            int index = 0;
            for (double element : array) {
                consume.accept(index, element);
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public final String render() {
        try {
            Writer writer = new StringBuilderWriter(writerInitialSize());
            renderImpl(writer);
            return writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public final void render(Writer writer) {
        try {
            renderImpl(writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    protected abstract void renderImpl(Writer writer) throws IOException;

    public abstract int writerInitialSize();

}
