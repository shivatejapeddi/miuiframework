package com.android.framework.protobuf.nano;

public final class FieldArray implements Cloneable {
    private static final FieldData DELETED = new FieldData();
    private FieldData[] mData;
    private int[] mFieldNumbers;
    private boolean mGarbage;
    private int mSize;

    FieldArray() {
        this(10);
    }

    FieldArray(int initialCapacity) {
        this.mGarbage = false;
        initialCapacity = idealIntArraySize(initialCapacity);
        this.mFieldNumbers = new int[initialCapacity];
        this.mData = new FieldData[initialCapacity];
        this.mSize = 0;
    }

    /* Access modifiers changed, original: 0000 */
    public FieldData get(int fieldNumber) {
        int i = binarySearch(fieldNumber);
        if (i >= 0) {
            FieldData[] fieldDataArr = this.mData;
            if (fieldDataArr[i] != DELETED) {
                return fieldDataArr[i];
            }
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public void remove(int fieldNumber) {
        int i = binarySearch(fieldNumber);
        if (i >= 0) {
            FieldData[] fieldDataArr = this.mData;
            FieldData fieldData = fieldDataArr[i];
            FieldData fieldData2 = DELETED;
            if (fieldData != fieldData2) {
                fieldDataArr[i] = fieldData2;
                this.mGarbage = true;
            }
        }
    }

    private void gc() {
        int n = this.mSize;
        int o = 0;
        int[] keys = this.mFieldNumbers;
        FieldData[] values = this.mData;
        for (int i = 0; i < n; i++) {
            FieldData val = values[i];
            if (val != DELETED) {
                if (i != o) {
                    keys[o] = keys[i];
                    values[o] = val;
                    values[i] = null;
                }
                o++;
            }
        }
        this.mGarbage = false;
        this.mSize = o;
    }

    /* Access modifiers changed, original: 0000 */
    public void put(int fieldNumber, FieldData data) {
        int i = binarySearch(fieldNumber);
        if (i >= 0) {
            this.mData[i] = data;
        } else {
            FieldData[] fieldDataArr;
            int[] nkeys;
            i = ~i;
            if (i < this.mSize) {
                fieldDataArr = this.mData;
                if (fieldDataArr[i] == DELETED) {
                    this.mFieldNumbers[i] = fieldNumber;
                    fieldDataArr[i] = data;
                    return;
                }
            }
            if (this.mGarbage && this.mSize >= this.mFieldNumbers.length) {
                gc();
                i = ~binarySearch(fieldNumber);
            }
            int i2 = this.mSize;
            if (i2 >= this.mFieldNumbers.length) {
                i2 = idealIntArraySize(i2 + 1);
                nkeys = new int[i2];
                FieldData[] nvalues = new FieldData[i2];
                int[] iArr = this.mFieldNumbers;
                System.arraycopy(iArr, 0, nkeys, 0, iArr.length);
                FieldData[] fieldDataArr2 = this.mData;
                System.arraycopy(fieldDataArr2, 0, nvalues, 0, fieldDataArr2.length);
                this.mFieldNumbers = nkeys;
                this.mData = nvalues;
            }
            i2 = this.mSize;
            if (i2 - i != 0) {
                nkeys = this.mFieldNumbers;
                System.arraycopy(nkeys, i, nkeys, i + 1, i2 - i);
                fieldDataArr = this.mData;
                System.arraycopy(fieldDataArr, i, fieldDataArr, i + 1, this.mSize - i);
            }
            this.mFieldNumbers[i] = fieldNumber;
            this.mData[i] = data;
            this.mSize++;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int size() {
        if (this.mGarbage) {
            gc();
        }
        return this.mSize;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    /* Access modifiers changed, original: 0000 */
    public FieldData dataAt(int index) {
        if (this.mGarbage) {
            gc();
        }
        return this.mData[index];
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldArray)) {
            return false;
        }
        FieldArray other = (FieldArray) o;
        if (size() != other.size()) {
            return false;
        }
        if (!(arrayEquals(this.mFieldNumbers, other.mFieldNumbers, this.mSize) && arrayEquals(this.mData, other.mData, this.mSize))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        if (this.mGarbage) {
            gc();
        }
        int result = 17;
        for (int i = 0; i < this.mSize; i++) {
            result = (((result * 31) + this.mFieldNumbers[i]) * 31) + this.mData[i].hashCode();
        }
        return result;
    }

    private int idealIntArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    private int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++) {
            if (need <= (1 << i) - 12) {
                return (1 << i) - 12;
            }
        }
        return need;
    }

    private int binarySearch(int value) {
        int lo = 0;
        int hi = this.mSize - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int midVal = this.mFieldNumbers[mid];
            if (midVal < value) {
                lo = mid + 1;
            } else if (midVal <= value) {
                return mid;
            } else {
                hi = mid - 1;
            }
        }
        return ~lo;
    }

    private boolean arrayEquals(int[] a, int[] b, int size) {
        for (int i = 0; i < size; i++) {
            if (a[i] != b[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean arrayEquals(FieldData[] a, FieldData[] b, int size) {
        for (int i = 0; i < size; i++) {
            if (!a[i].equals(b[i])) {
                return false;
            }
        }
        return true;
    }

    public final FieldArray clone() {
        int size = size();
        FieldArray clone = new FieldArray(size);
        System.arraycopy(this.mFieldNumbers, 0, clone.mFieldNumbers, 0, size);
        for (int i = 0; i < size; i++) {
            FieldData[] fieldDataArr = this.mData;
            if (fieldDataArr[i] != null) {
                clone.mData[i] = fieldDataArr[i].clone();
            }
        }
        clone.mSize = size;
        return clone;
    }
}
