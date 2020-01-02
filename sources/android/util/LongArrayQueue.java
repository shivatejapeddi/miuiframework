package android.util;

import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.util.NoSuchElementException;
import libcore.util.EmptyArray;

public class LongArrayQueue {
    private int mHead;
    private int mSize;
    private int mTail;
    private long[] mValues;

    public LongArrayQueue(int initialCapacity) {
        if (initialCapacity == 0) {
            this.mValues = EmptyArray.LONG;
        } else {
            this.mValues = ArrayUtils.newUnpaddedLongArray(initialCapacity);
        }
        this.mSize = 0;
        this.mTail = 0;
        this.mHead = 0;
    }

    public LongArrayQueue() {
        this(16);
    }

    private void grow() {
        int newSize = this.mSize;
        if (newSize >= this.mValues.length) {
            long[] newArray = ArrayUtils.newUnpaddedLongArray(GrowingArrayUtils.growSize(newSize));
            long[] jArr = this.mValues;
            int r = jArr.length;
            int i = this.mHead;
            r -= i;
            System.arraycopy(jArr, i, newArray, 0, r);
            System.arraycopy(this.mValues, 0, newArray, r, this.mHead);
            this.mValues = newArray;
            this.mHead = 0;
            this.mTail = this.mSize;
            return;
        }
        throw new IllegalStateException("Queue not full yet!");
    }

    public int size() {
        return this.mSize;
    }

    public void clear() {
        this.mSize = 0;
        this.mTail = 0;
        this.mHead = 0;
    }

    public void addLast(long value) {
        if (this.mSize == this.mValues.length) {
            grow();
        }
        long[] jArr = this.mValues;
        int i = this.mTail;
        jArr[i] = value;
        this.mTail = (i + 1) % jArr.length;
        this.mSize++;
    }

    public long removeFirst() {
        int i = this.mSize;
        if (i != 0) {
            long[] jArr = this.mValues;
            int i2 = this.mHead;
            long ret = jArr[i2];
            this.mHead = (i2 + 1) % jArr.length;
            this.mSize = i - 1;
            return ret;
        }
        throw new NoSuchElementException("Queue is empty!");
    }

    public long get(int position) {
        if (position < 0 || position >= this.mSize) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Index ");
            stringBuilder.append(position);
            stringBuilder.append(" not valid for a queue of size ");
            stringBuilder.append(this.mSize);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }
        int index = this.mHead + position;
        long[] jArr = this.mValues;
        return jArr[index % jArr.length];
    }

    public long peekFirst() {
        if (this.mSize != 0) {
            return this.mValues[this.mHead];
        }
        throw new NoSuchElementException("Queue is empty!");
    }

    public long peekLast() {
        if (this.mSize != 0) {
            int index = this.mTail;
            if (index == 0) {
                index = this.mValues.length;
            }
            return this.mValues[index - 1];
        }
        throw new NoSuchElementException("Queue is empty!");
    }
}
