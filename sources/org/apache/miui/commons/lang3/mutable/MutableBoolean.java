package org.apache.miui.commons.lang3.mutable;

import java.io.Serializable;

public class MutableBoolean implements Mutable<Boolean>, Serializable, Comparable<MutableBoolean> {
    private static final long serialVersionUID = -4830728138360036487L;
    private boolean value;

    public MutableBoolean(boolean value) {
        this.value = value;
    }

    public MutableBoolean(Boolean value) {
        this.value = value.booleanValue();
    }

    public Boolean getValue() {
        return Boolean.valueOf(this.value);
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    public void setValue(Boolean value) {
        this.value = value.booleanValue();
    }

    public boolean isTrue() {
        return this.value;
    }

    public boolean isFalse() {
        return this.value ^ 1;
    }

    public boolean booleanValue() {
        return this.value;
    }

    public Boolean toBoolean() {
        return Boolean.valueOf(booleanValue());
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof MutableBoolean)) {
            return false;
        }
        if (this.value == ((MutableBoolean) obj).booleanValue()) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return (this.value ? Boolean.TRUE : Boolean.FALSE).hashCode();
    }

    public int compareTo(MutableBoolean other) {
        boolean anotherVal = other.value;
        boolean z = this.value;
        if (z == anotherVal) {
            return 0;
        }
        return z ? 1 : -1;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
