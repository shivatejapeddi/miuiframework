package org.apache.miui.commons.lang3.mutable;

public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number> {
    private static final long serialVersionUID = 512176391864L;
    private int value;

    public MutableInt(int value) {
        this.value = value;
    }

    public MutableInt(Number value) {
        this.value = value.intValue();
    }

    public MutableInt(String value) throws NumberFormatException {
        this.value = Integer.parseInt(value);
    }

    public Integer getValue() {
        return Integer.valueOf(this.value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setValue(Number value) {
        this.value = value.intValue();
    }

    public void increment() {
        this.value++;
    }

    public void decrement() {
        this.value--;
    }

    public void add(int operand) {
        this.value += operand;
    }

    public void add(Number operand) {
        this.value += operand.intValue();
    }

    public void subtract(int operand) {
        this.value -= operand;
    }

    public void subtract(Number operand) {
        this.value -= operand.intValue();
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

    public Integer toInteger() {
        return Integer.valueOf(intValue());
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof MutableInt)) {
            return false;
        }
        if (this.value == ((MutableInt) obj).intValue()) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.value;
    }

    public int compareTo(MutableInt other) {
        int anotherVal = other.value;
        int i = this.value;
        if (i < anotherVal) {
            return -1;
        }
        return i == anotherVal ? 0 : 1;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
