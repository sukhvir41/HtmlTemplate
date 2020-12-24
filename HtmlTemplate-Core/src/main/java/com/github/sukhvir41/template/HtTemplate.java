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
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public abstract class HtTemplate {

    public final String encodeContent(String htmlContent) {
        return StringEscapeUtils.escapeHtml4(htmlContent);
    }

    public final String content(Supplier<String> theContent) {
        try {
            String value = theContent.get();
            if (value == null) {
                return "";
            } else {
                return encodeContent(value);
            }
        } catch (Exception e) {
            return "";
        }
    }

    public final String unescapedContent(Supplier<String> theContent) {
        try {
            String value = theContent.get();
            if (value == null) {
                return "";
            } else {
                return value;
            }
        } catch (Exception e) {
            return "";
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
            Writer writer = new StringBuilderWriter();
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

}
