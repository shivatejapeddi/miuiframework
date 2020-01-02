package android.text;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.io.PrintStream;
import libcore.util.EmptyArray;

class PackedObjectVector<E> {
    private int mColumns;
    private int mRowGapLength = this.mRows;
    private int mRowGapStart = 0;
    private int mRows = 0;
    private Object[] mValues = EmptyArray.OBJECT;

    public PackedObjectVector(int columns) {
        this.mColumns = columns;
    }

    public E getValue(int row, int column) {
        if (row >= this.mRowGapStart) {
            row += this.mRowGapLength;
        }
        return this.mValues[(this.mColumns * row) + column];
    }

    public void setValue(int row, int column, E value) {
        if (row >= this.mRowGapStart) {
            row += this.mRowGapLength;
        }
        this.mValues[(this.mColumns * row) + column] = value;
    }

    public void insertAt(int row, E[] values) {
        moveRowGapTo(row);
        if (this.mRowGapLength == 0) {
            growBuffer();
        }
        this.mRowGapStart++;
        this.mRowGapLength--;
        int i;
        if (values == null) {
            for (i = 0; i < this.mColumns; i++) {
                setValue(row, i, null);
            }
            return;
        }
        for (i = 0; i < this.mColumns; i++) {
            setValue(row, i, values[i]);
        }
    }

    public void deleteAt(int row, int count) {
        moveRowGapTo(row + count);
        this.mRowGapStart -= count;
        this.mRowGapLength += count;
        int i = this.mRowGapLength;
        size();
    }

    public int size() {
        return this.mRows - this.mRowGapLength;
    }

    public int width() {
        return this.mColumns;
    }

    private void growBuffer() {
        Object[] newvalues = ArrayUtils.newUnpaddedObjectArray(GrowingArrayUtils.growSize(size()) * this.mColumns);
        int newsize = newvalues.length;
        int i = this.mColumns;
        newsize /= i;
        int after = this.mRows;
        int i2 = this.mRowGapStart;
        after -= this.mRowGapLength + i2;
        System.arraycopy(this.mValues, 0, newvalues, 0, i * i2);
        Object[] objArr = this.mValues;
        i2 = this.mRows - after;
        int i3 = this.mColumns;
        System.arraycopy(objArr, i2 * i3, newvalues, (newsize - after) * i3, i3 * after);
        this.mRowGapLength += newsize - this.mRows;
        this.mRows = newsize;
        this.mValues = newvalues;
    }

    private void moveRowGapTo(int where) {
        int moving = this.mRowGapStart;
        if (where != moving) {
            int i;
            int moving2;
            int i2;
            int i3;
            Object[] objArr;
            if (where > moving) {
                i = this.mRowGapLength;
                moving2 = (where + i) - (moving + i);
                moving += i;
                while (true) {
                    i = this.mRowGapStart;
                    i2 = this.mRowGapLength;
                    if (moving >= (i + i2) + moving2) {
                        break;
                    }
                    i2 = (moving - (i2 + i)) + i;
                    i = 0;
                    while (true) {
                        i3 = this.mColumns;
                        if (i >= i3) {
                            break;
                        }
                        objArr = this.mValues;
                        objArr[(i3 * i2) + i] = objArr[(moving * i3) + i];
                        i++;
                    }
                    moving++;
                }
            } else {
                moving -= where;
                for (i = (where + moving) - 1; i >= where; i--) {
                    moving2 = (((i - where) + this.mRowGapStart) + this.mRowGapLength) - moving;
                    i2 = 0;
                    while (true) {
                        i3 = this.mColumns;
                        if (i2 >= i3) {
                            break;
                        }
                        objArr = this.mValues;
                        objArr[(i3 * moving2) + i2] = objArr[(i * i3) + i2];
                        i2++;
                    }
                }
            }
            this.mRowGapStart = where;
        }
    }

    public void dump() {
        int i = 0;
        while (i < this.mRows) {
            int j = 0;
            while (true) {
                int i2 = this.mColumns;
                if (j >= i2) {
                    break;
                }
                Object val = this.mValues[(i2 * i) + j];
                int i3 = this.mRowGapStart;
                PrintStream printStream;
                StringBuilder stringBuilder;
                if (i < i3 || i >= i3 + this.mRowGapLength) {
                    printStream = System.out;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(val);
                    stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                    printStream.print(stringBuilder.toString());
                } else {
                    printStream = System.out;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("(");
                    stringBuilder.append(val);
                    stringBuilder.append(") ");
                    printStream.print(stringBuilder.toString());
                }
                j++;
            }
            System.out.print(" << \n");
            i++;
        }
        System.out.print("-----\n\n");
    }
}
