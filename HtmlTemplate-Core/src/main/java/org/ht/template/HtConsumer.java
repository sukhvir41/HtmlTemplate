package org.ht.template;

@FunctionalInterface
public interface HtConsumer<T> {
    void accept(T t) throws Exception;
}
