package android.database;

import android.annotation.UnsupportedAppUsage;
import android.content.res.Resources;
import android.database.sqlite.SQLiteClosable;
import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.util.LongSparseArray;
import com.android.internal.R;
import dalvik.system.CloseGuard;

public class CursorWindow extends SQLiteClosable implements Parcelable {
    public static final Creator<CursorWindow> CREATOR = new Creator<CursorWindow>() {
        public CursorWindow createFromParcel(Parcel source) {
            return new CursorWindow(source, null);
        }

        public CursorWindow[] newArray(int size) {
            return new CursorWindow[size];
        }
    };
    private static final String STATS_TAG = "CursorWindowStats";
    @UnsupportedAppUsage
    private static int sCursorWindowSize = -1;
    @UnsupportedAppUsage
    private static final LongSparseArray<Integer> sWindowToPidMap = new LongSparseArray();
    private final CloseGuard mCloseGuard;
    private final String mName;
    private int mStartPos;
    @UnsupportedAppUsage
    public long mWindowPtr;

    private static native boolean nativeAllocRow(long j);

    private static native void nativeClear(long j);

    private static native void nativeCopyStringToBuffer(long j, int i, int i2, CharArrayBuffer charArrayBuffer);

    private static native long nativeCreate(String str, int i);

    private static native long nativeCreateFromParcel(Parcel parcel);

    private static native void nativeDispose(long j);

    private static native void nativeFreeLastRow(long j);

    private static native byte[] nativeGetBlob(long j, int i, int i2);

    private static native double nativeGetDouble(long j, int i, int i2);

    private static native long nativeGetLong(long j, int i, int i2);

    private static native String nativeGetName(long j);

    private static native int nativeGetNumRows(long j);

    private static native String nativeGetString(long j, int i, int i2);

    private static native int nativeGetType(long j, int i, int i2);

    private static native boolean nativePutBlob(long j, byte[] bArr, int i, int i2);

    private static native boolean nativePutDouble(long j, double d, int i, int i2);

    private static native boolean nativePutLong(long j, long j2, int i, int i2);

    private static native boolean nativePutNull(long j, int i, int i2);

    private static native boolean nativePutString(long j, String str, int i, int i2);

    private static native boolean nativeSetNumColumns(long j, int i);

    private static native void nativeWriteToParcel(long j, Parcel parcel);

    public CursorWindow(String name) {
        this(name, (long) getCursorWindowSize());
    }

    public CursorWindow(String name, long windowSizeBytes) {
        this.mCloseGuard = CloseGuard.get();
        this.mStartPos = 0;
        String str = (name == null || name.length() == 0) ? "<unnamed>" : name;
        this.mName = str;
        this.mWindowPtr = nativeCreate(this.mName, (int) windowSizeBytes);
        if (this.mWindowPtr != 0) {
            this.mCloseGuard.open("close");
            recordNewWindow(Binder.getCallingPid(), this.mWindowPtr);
            return;
        }
        throw new AssertionError();
    }

    @Deprecated
    public CursorWindow(boolean localWindow) {
        this((String) null);
    }

    private CursorWindow(Parcel source) {
        this.mCloseGuard = CloseGuard.get();
        this.mStartPos = source.readInt();
        this.mWindowPtr = nativeCreateFromParcel(source);
        long j = this.mWindowPtr;
        if (j != 0) {
            this.mName = nativeGetName(j);
            this.mCloseGuard.open("close");
            return;
        }
        throw new AssertionError();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            dispose();
        } finally {
            super.finalize();
        }
    }

    private void dispose() {
        CloseGuard closeGuard = this.mCloseGuard;
        if (closeGuard != null) {
            closeGuard.close();
        }
        long j = this.mWindowPtr;
        if (j != 0) {
            recordClosingOfWindow(j);
            nativeDispose(this.mWindowPtr);
            this.mWindowPtr = 0;
        }
    }

    public String getName() {
        return this.mName;
    }

    public void clear() {
        acquireReference();
        try {
            this.mStartPos = 0;
            nativeClear(this.mWindowPtr);
        } finally {
            releaseReference();
        }
    }

    public int getStartPosition() {
        return this.mStartPos;
    }

    public void setStartPosition(int pos) {
        this.mStartPos = pos;
    }

    public int getNumRows() {
        acquireReference();
        try {
            int nativeGetNumRows = nativeGetNumRows(this.mWindowPtr);
            return nativeGetNumRows;
        } finally {
            releaseReference();
        }
    }

    public boolean setNumColumns(int columnNum) {
        acquireReference();
        try {
            boolean nativeSetNumColumns = nativeSetNumColumns(this.mWindowPtr, columnNum);
            return nativeSetNumColumns;
        } finally {
            releaseReference();
        }
    }

    public boolean allocRow() {
        acquireReference();
        try {
            boolean nativeAllocRow = nativeAllocRow(this.mWindowPtr);
            return nativeAllocRow;
        } finally {
            releaseReference();
        }
    }

    public void freeLastRow() {
        acquireReference();
        try {
            nativeFreeLastRow(this.mWindowPtr);
        } finally {
            releaseReference();
        }
    }

    @Deprecated
    public boolean isNull(int row, int column) {
        return getType(row, column) == 0;
    }

    @Deprecated
    public boolean isBlob(int row, int column) {
        int type = getType(row, column);
        return type == 4 || type == 0;
    }

    @Deprecated
    public boolean isLong(int row, int column) {
        return getType(row, column) == 1;
    }

    @Deprecated
    public boolean isFloat(int row, int column) {
        return getType(row, column) == 2;
    }

    @Deprecated
    public boolean isString(int row, int column) {
        int type = getType(row, column);
        return type == 3 || type == 0;
    }

    public int getType(int row, int column) {
        acquireReference();
        try {
            int nativeGetType = nativeGetType(this.mWindowPtr, row - this.mStartPos, column);
            return nativeGetType;
        } finally {
            releaseReference();
        }
    }

    public byte[] getBlob(int row, int column) {
        acquireReference();
        try {
            byte[] nativeGetBlob = nativeGetBlob(this.mWindowPtr, row - this.mStartPos, column);
            return nativeGetBlob;
        } finally {
            releaseReference();
        }
    }

    public String getString(int row, int column) {
        acquireReference();
        try {
            String nativeGetString = nativeGetString(this.mWindowPtr, row - this.mStartPos, column);
            return nativeGetString;
        } finally {
            releaseReference();
        }
    }

    public void copyStringToBuffer(int row, int column, CharArrayBuffer buffer) {
        if (buffer != null) {
            acquireReference();
            try {
                nativeCopyStringToBuffer(this.mWindowPtr, row - this.mStartPos, column, buffer);
            } finally {
                releaseReference();
            }
        } else {
            throw new IllegalArgumentException("CharArrayBuffer should not be null");
        }
    }

    public long getLong(int row, int column) {
        acquireReference();
        try {
            long nativeGetLong = nativeGetLong(this.mWindowPtr, row - this.mStartPos, column);
            return nativeGetLong;
        } finally {
            releaseReference();
        }
    }

    public double getDouble(int row, int column) {
        acquireReference();
        try {
            double nativeGetDouble = nativeGetDouble(this.mWindowPtr, row - this.mStartPos, column);
            return nativeGetDouble;
        } finally {
            releaseReference();
        }
    }

    public short getShort(int row, int column) {
        return (short) ((int) getLong(row, column));
    }

    public int getInt(int row, int column) {
        return (int) getLong(row, column);
    }

    public float getFloat(int row, int column) {
        return (float) getDouble(row, column);
    }

    public boolean putBlob(byte[] value, int row, int column) {
        acquireReference();
        try {
            boolean nativePutBlob = nativePutBlob(this.mWindowPtr, value, row - this.mStartPos, column);
            return nativePutBlob;
        } finally {
            releaseReference();
        }
    }

    public boolean putString(String value, int row, int column) {
        acquireReference();
        try {
            boolean nativePutString = nativePutString(this.mWindowPtr, value, row - this.mStartPos, column);
            return nativePutString;
        } finally {
            releaseReference();
        }
    }

    public boolean putLong(long value, int row, int column) {
        acquireReference();
        try {
            boolean nativePutLong = nativePutLong(this.mWindowPtr, value, row - this.mStartPos, column);
            return nativePutLong;
        } finally {
            releaseReference();
        }
    }

    public boolean putDouble(double value, int row, int column) {
        acquireReference();
        try {
            boolean nativePutDouble = nativePutDouble(this.mWindowPtr, value, row - this.mStartPos, column);
            return nativePutDouble;
        } finally {
            releaseReference();
        }
    }

    public boolean putNull(int row, int column) {
        acquireReference();
        try {
            boolean nativePutNull = nativePutNull(this.mWindowPtr, row - this.mStartPos, column);
            return nativePutNull;
        } finally {
            releaseReference();
        }
    }

    public static CursorWindow newFromParcel(Parcel p) {
        return (CursorWindow) CREATOR.createFromParcel(p);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        acquireReference();
        try {
            dest.writeInt(this.mStartPos);
            nativeWriteToParcel(this.mWindowPtr, dest);
            if ((flags & 1) != 0) {
                releaseReference();
            }
        } finally {
            releaseReference();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAllReferencesReleased() {
        dispose();
    }

    private void recordNewWindow(int pid, long window) {
        synchronized (sWindowToPidMap) {
            sWindowToPidMap.put(window, Integer.valueOf(pid));
            if (Log.isLoggable(STATS_TAG, 2)) {
                String str = STATS_TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Created a new Cursor. ");
                stringBuilder.append(printStats());
                Log.i(str, stringBuilder.toString());
            }
        }
    }

    private void recordClosingOfWindow(long window) {
        synchronized (sWindowToPidMap) {
            if (sWindowToPidMap.size() == 0) {
                return;
            }
            sWindowToPidMap.delete(window);
        }
    }

    /* JADX WARNING: Missing block: B:12:0x003a, code skipped:
            r4 = r3.size();
            r5 = 0;
     */
    /* JADX WARNING: Missing block: B:13:0x003f, code skipped:
            if (r5 >= r4) goto L_0x0089;
     */
    /* JADX WARNING: Missing block: B:14:0x0041, code skipped:
            r0.append(" (# cursors opened by ");
            r6 = r3.keyAt(r5);
     */
    /* JADX WARNING: Missing block: B:15:0x004a, code skipped:
            if (r6 != r1) goto L_0x0053;
     */
    /* JADX WARNING: Missing block: B:16:0x004c, code skipped:
            r0.append("this proc=");
     */
    /* JADX WARNING: Missing block: B:17:0x0053, code skipped:
            r7 = new java.lang.StringBuilder();
            r7.append("pid ");
            r7.append(r6);
            r7.append("=");
            r0.append(r7.toString());
     */
    /* JADX WARNING: Missing block: B:18:0x006d, code skipped:
            r7 = r3.get(r6);
            r8 = new java.lang.StringBuilder();
            r8.append(r7);
            r8.append(")");
            r0.append(r8.toString());
            r2 = r2 + r7;
            r5 = r5 + 1;
     */
    /* JADX WARNING: Missing block: B:20:0x008f, code skipped:
            if (r0.length() <= 980) goto L_0x0097;
     */
    /* JADX WARNING: Missing block: B:21:0x0091, code skipped:
            r5 = r0.substring(0, 980);
     */
    /* JADX WARNING: Missing block: B:22:0x0097, code skipped:
            r5 = r0.toString();
     */
    /* JADX WARNING: Missing block: B:23:0x009b, code skipped:
            r6 = new java.lang.StringBuilder();
            r6.append("# Open Cursors=");
            r6.append(r2);
            r6.append(r5);
     */
    /* JADX WARNING: Missing block: B:24:0x00af, code skipped:
            return r6.toString();
     */
    @android.annotation.UnsupportedAppUsage
    private java.lang.String printStats() {
        /*
        r10 = this;
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = android.os.Process.myPid();
        r2 = 0;
        r3 = new android.util.SparseIntArray;
        r3.<init>();
        r4 = sWindowToPidMap;
        monitor-enter(r4);
        r5 = sWindowToPidMap;	 Catch:{ all -> 0x00b0 }
        r5 = r5.size();	 Catch:{ all -> 0x00b0 }
        if (r5 != 0) goto L_0x001e;
    L_0x001a:
        r6 = "";
        monitor-exit(r4);	 Catch:{ all -> 0x00b0 }
        return r6;
    L_0x001e:
        r6 = 0;
    L_0x001f:
        if (r6 >= r5) goto L_0x0039;
    L_0x0021:
        r7 = sWindowToPidMap;	 Catch:{ all -> 0x00b0 }
        r7 = r7.valueAt(r6);	 Catch:{ all -> 0x00b0 }
        r7 = (java.lang.Integer) r7;	 Catch:{ all -> 0x00b0 }
        r7 = r7.intValue();	 Catch:{ all -> 0x00b0 }
        r8 = r3.get(r7);	 Catch:{ all -> 0x00b0 }
        r8 = r8 + 1;
        r3.put(r7, r8);	 Catch:{ all -> 0x00b0 }
        r6 = r6 + 1;
        goto L_0x001f;
    L_0x0039:
        monitor-exit(r4);	 Catch:{ all -> 0x00b0 }
        r4 = r3.size();
        r5 = 0;
    L_0x003f:
        if (r5 >= r4) goto L_0x0089;
    L_0x0041:
        r6 = " (# cursors opened by ";
        r0.append(r6);
        r6 = r3.keyAt(r5);
        if (r6 != r1) goto L_0x0053;
    L_0x004c:
        r7 = "this proc=";
        r0.append(r7);
        goto L_0x006d;
    L_0x0053:
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = "pid ";
        r7.append(r8);
        r7.append(r6);
        r8 = "=";
        r7.append(r8);
        r7 = r7.toString();
        r0.append(r7);
    L_0x006d:
        r7 = r3.get(r6);
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r8.append(r7);
        r9 = ")";
        r8.append(r9);
        r8 = r8.toString();
        r0.append(r8);
        r2 = r2 + r7;
        r5 = r5 + 1;
        goto L_0x003f;
    L_0x0089:
        r5 = r0.length();
        r6 = 980; // 0x3d4 float:1.373E-42 double:4.84E-321;
        if (r5 <= r6) goto L_0x0097;
    L_0x0091:
        r5 = 0;
        r5 = r0.substring(r5, r6);
        goto L_0x009b;
    L_0x0097:
        r5 = r0.toString();
    L_0x009b:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "# Open Cursors=";
        r6.append(r7);
        r6.append(r2);
        r6.append(r5);
        r6 = r6.toString();
        return r6;
    L_0x00b0:
        r5 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x00b0 }
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.CursorWindow.printStats():java.lang.String");
    }

    private static int getCursorWindowSize() {
        if (sCursorWindowSize < 0) {
            sCursorWindowSize = Resources.getSystem().getInteger(R.integer.config_cursorWindowSize) * 1024;
        }
        return sCursorWindowSize;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getName());
        stringBuilder.append(" {");
        stringBuilder.append(Long.toHexString(this.mWindowPtr));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
