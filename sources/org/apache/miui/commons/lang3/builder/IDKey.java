package org.apache.miui.commons.lang3.builder;

final class IDKey {
    private final int id;
    private final Object value;

    public IDKey(Object _value) {
        this.id = System.identityHashCode(_value);
        this.value = _value;
    }

    public int hashCode() {
        return this.id;
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!(other instanceof IDKey)) {
            return false;
        }
        IDKey idKey = (IDKey) other;
        if (this.id != idKey.id) {
            return false;
        }
        if (this.value == idKey.value) {
            z = true;
        }
        return z;
    }
}
