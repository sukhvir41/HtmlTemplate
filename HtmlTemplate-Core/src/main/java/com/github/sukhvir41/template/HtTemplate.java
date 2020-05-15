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

import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;

public abstract class HtTemplate {

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

    public <Type> void forEach(Iterator<Type> iterator, HtConsumer<Type> consume) {
        try {
            while (iterator.hasNext()) {
                consume.accept(iterator.next());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <Type> void forEach(Iterator<Type> iterator, HtBiConsumer<Integer, Type> consume) {
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

    public <Type> void forEach(Collection<Type> collection, HtConsumer<Type> consume) {
        try {
            for (Type type : collection) {
                consume.accept(type);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <Type> void forEach(Collection<Type> collection, HtBiConsumer<Integer, Type> consume) {
        try {
            int index = 0;
            for (Type type : collection) {
                consume.accept(index, type);
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <Key, Value> void forEach(Map<Key, Value> map, HtBiConsumer<Key, Value> consume) {
        try {
            for (Map.Entry<Key, Value> item : map.entrySet()) {
                consume.accept(item.getKey(), item.getValue());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <Key, Value> void forEach(Map<Key, Value> map, HtTriConsumer<Integer, Key, Value> consume) {
        try {
            int index = 0;
            for (Map.Entry<Key, Value> item : map.entrySet()) {
                consume.accept(index, item.getKey(), item.getValue());
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <Type> void forEach(Type[] array, HtConsumer<Type> consume) {
        try {
            for (Type element : array) {
                consume.accept(element);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <Type> void forEach(Type[] array, HtBiConsumer<Integer, Type> consume) {
        try {
            int index = 0;
            for (Type element : array) {
                consume.accept(index, element);
                ++index;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void forEach(int[] array, HtConsumer<Integer> consume) {
        try {
            for (int element : array) {
                consume.accept(element);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void forEach(int[] array, HtBiConsumer<Integer, Integer> consume) {
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

    public void forEach(long[] array, HtConsumer<Long> consume) {
        try {
            for (long element : array) {
                consume.accept(element);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void forEach(long[] array, HtBiConsumer<Integer, Long> consume) {
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

    public void forEach(float[] array, HtConsumer<Float> consume) {
        try {
            for (float element : array) {
                consume.accept(element);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void forEach(float[] array, HtBiConsumer<Integer, Float> consume) {
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

    public void forEach(double[] array, HtConsumer<Double> consume) {
        try {
            for (double element : array) {
                consume.accept(element);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void forEach(double[] array, HtBiConsumer<Integer, Double> consume) {
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


}
