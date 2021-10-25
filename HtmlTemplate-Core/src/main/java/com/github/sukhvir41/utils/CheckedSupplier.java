package com.github.sukhvir41.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

@FunctionalInterface
public interface CheckedSupplier<R> {

    static <R> Supplier<R> wrapFunction(CheckedSupplier<R> checkedSupplier) {
        return () -> {
            try {
                return checkedSupplier.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    R get() throws Exception;

}
