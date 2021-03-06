package org.apache.miui.commons.lang3.mutable;

import java.io.Serializable;

public class MutableObject<T> implements Mutable<T>, Serializable {
    private static final long serialVersionUID = 86241875189L;
    private T value;

    public MutableObject(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        return this.value.equals(((MutableObject) obj).value);
    }

    public int hashCode() {
        Object obj = this.value;
        return obj == null ? 0 : obj.hashCode();
    }

    public String toString() {
        Object obj = this.value;
        return obj == null ? "null" : obj.toString();
    }
}
