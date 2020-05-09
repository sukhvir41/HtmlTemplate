package org.ht.template;

@FunctionalInterface
public interface HtBiConsumer<T, U> {
    void accept(T t, U u) throws Exception;
}
