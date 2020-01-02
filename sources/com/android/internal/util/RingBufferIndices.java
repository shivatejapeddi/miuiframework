package com.android.internal.util;

public class RingBufferIndices {
    private final int mCapacity;
    private int mSize;
    private int mStart;

    public RingBufferIndices(int capacity) {
        this.mCapacity = capacity;
    }

    public int add() {
        int i = this.mSize;
        int i2 = this.mCapacity;
        if (i < i2) {
            i2 = this.mSize;
            this.mSize = i + 1;
            return i2;
        }
        i = this.mStart;
        this.mStart++;
        if (this.mStart == i2) {
            this.mStart = 0;
        }
        return i;
    }

    public void clear() {
        this.mStart = 0;
        this.mSize = 0;
    }

    public int size() {
        return this.mSize;
    }

    public int indexOf(int pos) {
        int index = this.mStart + pos;
        int i = this.mCapacity;
        if (index >= i) {
            return index - i;
        }
        return index;
    }
}
