package android.util;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import dalvik.system.CloseGuard;
import java.io.Closeable;
import java.io.IOException;
import java.util.UUID;
import libcore.io.IoUtils;

public final class MemoryIntArray implements Parcelable, Closeable {
    public static final Creator<MemoryIntArray> CREATOR = new Creator<MemoryIntArray>() {
        public MemoryIntArray createFromParcel(Parcel parcel) {
            try {
                return new MemoryIntArray(parcel, null);
            } catch (IOException e) {
                throw new IllegalArgumentException("Error unparceling MemoryIntArray");
            }
        }

        public MemoryIntArray[] newArray(int size) {
            return new MemoryIntArray[size];
        }
    };
    private static final int MAX_SIZE = 1024;
    private static final String TAG = "MemoryIntArray";
    private final CloseGuard mCloseGuard;
    private int mFd;
    private final boolean mIsOwner;
    private final long mMemoryAddr;

    private native void nativeClose(int i, long j, boolean z);

    private native int nativeCreate(String str, int i);

    private native int nativeGet(int i, long j, int i2);

    private native long nativeOpen(int i, boolean z);

    private native void nativeSet(int i, long j, int i2, int i3);

    private native int nativeSize(int i);

    /* synthetic */ MemoryIntArray(Parcel x0, AnonymousClass1 x1) throws IOException {
        this(x0);
    }

    public MemoryIntArray(int size) throws IOException {
        this.mCloseGuard = CloseGuard.get();
        this.mFd = -1;
        if (size <= 1024) {
            this.mIsOwner = true;
            this.mFd = nativeCreate(UUID.randomUUID().toString(), size);
            this.mMemoryAddr = nativeOpen(this.mFd, this.mIsOwner);
            this.mCloseGuard.open("close");
            return;
        }
        throw new IllegalArgumentException("Max size is 1024");
    }

    private MemoryIntArray(Parcel parcel) throws IOException {
        this.mCloseGuard = CloseGuard.get();
        this.mFd = -1;
        this.mIsOwner = false;
        ParcelFileDescriptor pfd = (ParcelFileDescriptor) parcel.readParcelable(null);
        if (pfd != null) {
            this.mFd = pfd.detachFd();
            this.mMemoryAddr = nativeOpen(this.mFd, this.mIsOwner);
            this.mCloseGuard.open("close");
            return;
        }
        throw new IOException("No backing file descriptor");
    }

    public boolean isWritable() {
        enforceNotClosed();
        return this.mIsOwner;
    }

    public int get(int index) throws IOException {
        enforceNotClosed();
        enforceValidIndex(index);
        return nativeGet(this.mFd, this.mMemoryAddr, index);
    }

    public void set(int index, int value) throws IOException {
        enforceNotClosed();
        enforceWritable();
        enforceValidIndex(index);
        nativeSet(this.mFd, this.mMemoryAddr, index, value);
    }

    public int size() throws IOException {
        enforceNotClosed();
        return nativeSize(this.mFd);
    }

    public void close() throws IOException {
        if (!isClosed()) {
            nativeClose(this.mFd, this.mMemoryAddr, this.mIsOwner);
            this.mFd = -1;
            this.mCloseGuard.close();
        }
    }

    public boolean isClosed() {
        return this.mFd == -1;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            IoUtils.closeQuietly(this);
        } finally {
            super.finalize();
        }
    }

    public int describeContents() {
        return 1;
    }

    /* JADX WARNING: Missing block: B:11:0x0013, code skipped:
            if (r0 != null) goto L_0x0015;
     */
    /* JADX WARNING: Missing block: B:13:?, code skipped:
            r0.close();
     */
    public void writeToParcel(android.os.Parcel r5, int r6) {
        /*
        r4 = this;
        r0 = r4.mFd;	 Catch:{ IOException -> 0x001e }
        r0 = android.os.ParcelFileDescriptor.fromFd(r0);	 Catch:{ IOException -> 0x001e }
        r5.writeParcelable(r0, r6);	 Catch:{ all -> 0x0010 }
        if (r0 == 0) goto L_0x000e;
    L_0x000b:
        r0.close();	 Catch:{ IOException -> 0x001e }
        return;
    L_0x0010:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0012 }
    L_0x0012:
        r2 = move-exception;
        if (r0 == 0) goto L_0x001d;
    L_0x0015:
        r0.close();	 Catch:{ all -> 0x0019 }
        goto L_0x001d;
    L_0x0019:
        r3 = move-exception;
        r1.addSuppressed(r3);	 Catch:{ IOException -> 0x001e }
    L_0x001d:
        throw r2;	 Catch:{ IOException -> 0x001e }
    L_0x001e:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r1.<init>(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.MemoryIntArray.writeToParcel(android.os.Parcel, int):void");
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this.mFd == ((MemoryIntArray) obj).mFd) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.mFd;
    }

    private void enforceNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("cannot interact with a closed instance");
        }
    }

    private void enforceValidIndex(int index) throws IOException {
        int size = size();
        if (index < 0 || index > size - 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(index);
            stringBuilder.append(" not between 0 and ");
            stringBuilder.append(size - 1);
            throw new IndexOutOfBoundsException(stringBuilder.toString());
        }
    }

    private void enforceWritable() {
        if (!isWritable()) {
            throw new UnsupportedOperationException("array is not writable");
        }
    }

    public static int getMaxSize() {
        return 1024;
    }
}
