package android.database;

public class MergeCursor extends AbstractCursor {
    private Cursor mCursor;
    private Cursor[] mCursors;
    private DataSetObserver mObserver = new DataSetObserver() {
        public void onChanged() {
            MergeCursor.this.mPos = -1;
        }

        public void onInvalidated() {
            MergeCursor.this.mPos = -1;
        }
    };

    public MergeCursor(Cursor[] cursors) {
        this.mCursors = cursors;
        this.mCursor = cursors[0];
        int i = 0;
        while (true) {
            Cursor[] cursorArr = this.mCursors;
            if (i < cursorArr.length) {
                if (cursorArr[i] != null) {
                    cursorArr[i].registerDataSetObserver(this.mObserver);
                }
                i++;
            } else {
                return;
            }
        }
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
        this.mCursor = null;
        int cursorStartPos = 0;
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                if (newPosition < cursorArr[i].getCount() + cursorStartPos) {
                    this.mCursor = this.mCursors[i];
                    break;
                }
                cursorStartPos += this.mCursors[i].getCount();
            }
        }
        boolean ret = this.mCursor;
        if (ret) {
            return ret.moveToPosition(newPosition - cursorStartPos);
        }
        return false;
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
        return new String[0];
    }

    public void deactivate() {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                cursorArr[i].deactivate();
            }
        }
        super.deactivate();
    }

    public void close() {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                cursorArr[i].close();
            }
        }
        super.close();
    }

    public void registerContentObserver(ContentObserver observer) {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                cursorArr[i].registerContentObserver(observer);
            }
        }
    }

    public void unregisterContentObserver(ContentObserver observer) {
        int length = this.mCursors.length;
        for (int i = 0; i < length; i++) {
            Cursor[] cursorArr = this.mCursors;
            if (cursorArr[i] != null) {
                cursorArr[i].unregisterContentObserver(observer);
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
