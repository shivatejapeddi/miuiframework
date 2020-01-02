package org.apache.miui.commons.lang3.mutable;

public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number> {
    private static final long serialVersionUID = -2135791679;
    private short value;

    public MutableShort(short value) {
        this.value = value;
    }

    public MutableShort(Number value) {
        this.value = value.shortValue();
    }

    public MutableShort(String value) throws NumberFormatException {
        this.value = Short.parseShort(value);
    }

    public Short getValue() {
        return Short.valueOf(this.value);
    }

    public void setValue(short value) {
        this.value = value;
    }

    public void setValue(Number value) {
        this.value = value.shortValue();
    }

    public void increment() {
        this.value = (short) (this.value + 1);
    }

    public void decrement() {
        this.value = (short) (this.value - 1);
    }

    public void add(short operand) {
        this.value = (short) (this.value + operand);
    }

    public void add(Number operand) {
        this.value = (short) (this.value + operand.shortValue());
    }

    public void subtract(short operand) {
        this.value = (short) (this.value - operand);
    }

    public void subtract(Number operand) {
        this.value = (short) (this.value - operand.shortValue());
    }

    public short shortValue() {
        return this.value;
    }

    public int intValue() {
        return this.value;
    }

    public long longValue() {
        return (long) this.value;
    }

    public float floatValue() {
        return (float) this.value;
    }

    public double doubleValue() {
        return (double) this.value;
    }

    public Short toShort() {
        return Short.valueOf(shortValue());
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof MutableShort)) {
            return false;
        }
        if (this.value == ((MutableShort) obj).shortValue()) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.value;
    }

    public int compareTo(MutableShort other) {
        short anotherVal = other.value;
        short s = this.value;
        if (s < anotherVal) {
            return -1;
        }
        return s == anotherVal ? 0 : 1;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
