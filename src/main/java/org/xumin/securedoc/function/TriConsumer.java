package org.xumin.securedoc.function;

import java.util.Objects;

@FunctionalInterface
public interface TriConsumer<T,U,V> {
    void accept(T t, U u, V v);
}
