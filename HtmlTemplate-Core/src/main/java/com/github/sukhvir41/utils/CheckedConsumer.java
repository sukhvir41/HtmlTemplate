package com.github.sukhvir41.utils;

import java.util.function.Consumer;
import java.util.function.Function;

@FunctionalInterface
public interface CheckedConsumer<T> {
    static <T> Consumer<T> wrapFunction(CheckedConsumer<T> checkedConsumer) {
        return (T t) -> {
            try {
                checkedConsumer.accept(t);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    void accept(T t) throws Exception;
}
