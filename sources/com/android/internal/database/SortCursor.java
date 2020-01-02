package com.android.internal.database;

import android.annotation.UnsupportedAppUsage;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;
import java.lang.reflect.Array;

public class SortCursor extends AbstractCursor {
    private static final String TAG = "SortCursor";
    private final int ROWCACHESIZE = 64;
    private int[][] mCurRowNumCache;
    @UnsupportedAppUsage
    private Cursor mCursor;
    private int[] mCursorCache = new int[64];
    @UnsupportedAppUsage
    private Cursor[] mCursors;
    private int mLastCacheHit = -1;
    private DataSetObserver mObserver = new DataSetObserver() {
        public void onChanged() {
            SortCursor.this.mPos = -1;
        }

        public void onInvalidated() {
            SortCursor.this.mPos = -1;
        }
    };
    private int[] mRowNumCache = new int[64];
    private int[] mSortColumns;

    @UnsupportedAppUsage
    public SortCursor(Cursor[] cursors, String sortcolumn) {
        this.mCursors = cursors;
        int length = this.mCursors.length;
        this.mSortColumns = new int[length];
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                cursorArr[i].registerDataSetObserver(this.mObserver);
                this.mCursors[i].moveToFirst();
                this.mSortColumns[i] = this.mCursors[i].getColumnIndexOrThrow(sortcolumn);
            }
        }
        this.mCursor = null;
        String smallest = "";
        int j = 0;
        while (j < length) {
            Cursor[] cursorArr2 = this.mCursors;
            if (!(cursorArr2[j] == null || cursorArr2[j].isAfterLast())) {
                String current = this.mCursors[j].getString(this.mSortColumns[j]);
                if (this.mCursor == null || current.compareToIgnoreCase(smallest) < 0) {
                    smallest = current;
                    this.mCursor = this.mCursors[j];
                }
            }
            j++;
        }
        for (j = this.mRowNumCache.length - 1; j >= 0; j--) {
            this.mRowNumCache[j] = -2;
        }
        this.mCurRowNumCache = (int[][]) Array.newInstance(int.class, new int[]{64, length});
    }

    public int getCount() {
        int count = 0;
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                count += cursorArr[i].getCount();
            }
        }
        return count;
    }

    public boolean onMove(int oldPosition, int newPosition) {
        if (oldPosition == newPosition) {
            return true;
        }
        int cache_entry = newPosition % 64;
        int which;
        if (this.mRowNumCache[cache_entry] == newPosition) {
            which = this.mCursorCache[cache_entry];
            this.mCursor = this.mCursors[which];
            Cursor cursor = this.mCursor;
            if (cursor == null) {
                Log.w(TAG, "onMove: cache results in a null cursor.");
                return false;
            }
            cursor.moveToPosition(this.mCurRowNumCache[cache_entry][which]);
            this.mLastCacheHit = cache_entry;
            return true;
        }
        int i;
        int i2;
        this.mCursor = null;
        which = this.mCursors.length;
        if (this.mLastCacheHit >= 0) {
            for (int i3 = 0; i3 < which; i3++) {
                Cursor[] cursorArr = this.mCursors;
                if (cursorArr[i3] != null) {
                    cursorArr[i3].moveToPosition(this.mCurRowNumCache[this.mLastCacheHit][i3]);
                }
            }
        }
        if (newPosition < oldPosition || oldPosition == -1) {
            for (i = 0; i < which; i++) {
                Cursor[] cursorArr2 = this.mCursors;
                if (cursorArr2[i] != null) {
                    cursorArr2[i].moveToFirst();
                }
            }
            oldPosition = 0;
        }
        if (oldPosition < 0) {
            oldPosition = 0;
        }
        i = -1;
        for (i2 = oldPosition; i2 <= newPosition; i2++) {
            String smallest = "";
            i = -1;
            int j = 0;
            while (j < which) {
                Cursor[] cursorArr3 = this.mCursors;
                if (!(cursorArr3[j] == null || cursorArr3[j].isAfterLast())) {
                    String current = this.mCursors[j].getString(this.mSortColumns[j]);
                    if (i < 0 || current.compareToIgnoreCase(smallest) < 0) {
                        smallest = current;
                        i = j;
                    }
                }
                j++;
            }
            if (i2 == newPosition) {
                break;
            }
            Cursor[] cursorArr4 = this.mCursors;
            if (cursorArr4[i] != null) {
                cursorArr4[i].moveToNext();
            }
        }
        this.mCursor = this.mCursors[i];
        this.mRowNumCache[cache_entry] = newPosition;
        this.mCursorCache[cache_entry] = i;
        for (i2 = 0; i2 < which; i2++) {
            Cursor[] cursorArr5 = this.mCursors;
            if (cursorArr5[i2] != null) {
                this.mCurRowNumCache[cache_entry][i2] = cursorArr5[i2].getPosition();
            }
        }
        this.mLastCacheHit = -1;
        return true;
    }

    public String getString(int column) {
        return this.mCursor.getString(column);
    }

    public short getShort(int column) {
        return this.mCursor.getShort(column);
    }

    public int getInt(int column) {
        return this.mCursor.getInt(column);
    }

    public long getLong(int column) {
        return this.mCursor.getLong(column);
    }

    public float getFloat(int column) {
        return this.mCursor.getFloat(column);
    }

    public double getDouble(int column) {
        return this.mCursor.getDouble(column);
    }

    public int getType(int column) {
        return this.mCursor.getType(column);
    }

    public boolean isNull(int column) {
        return this.mCursor.isNull(column);
    }

    public byte[] getBlob(int column) {
        return this.mCursor.getBlob(column);
    }

    public String[] getColumnNames() {
        Cursor cursor = this.mCursor;
        if (cursor != null) {
            return cursor.getColumnNames();
        }
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                return cursorArr[i].getColumnNames();
            }
        }
        throw new IllegalStateException("No cursor that can return names");
    }

    public void deactivate() {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                cursorArr[i].deactivate();
            }
        }
    }

    public void close() {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                cursorArr[i].close();
            }
        }
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                cursorArr[i].registerDataSetObserver(observer);
            }
        }
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                cursorArr[i].unregisterDataSetObserver(observer);
            }
        }
    }

    public boolean requery() {
        int length = this.mCursors.length;
        int i = 0;
        while (i < length) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null && !cursorArr[i].requery()) {
                return false;
            }
            i++;
        }
        return true;
    }
}
