package org.ht.template;

@FunctionalInterface
public interface HtTriConsumer<T, U, V> {

    void accept(T t, U u, V v) throws Exception;

}
