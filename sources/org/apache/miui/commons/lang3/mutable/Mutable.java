package org.apache.miui.commons.lang3.mutable;

public interface Mutable<T> {
    T getValue();

    void setValue(T t);
}
