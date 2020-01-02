package android.view.inputmethod;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.util.Slog;
import java.util.List;

public class InputMethodSubtypeArray {
    private static final String TAG = "InputMethodSubtypeArray";
    private volatile byte[] mCompressedData;
    private final int mCount;
    private volatile int mDecompressedSize;
    private volatile InputMethodSubtype[] mInstance;
    private final Object mLockObject = new Object();

    @UnsupportedAppUsage
    public InputMethodSubtypeArray(List<InputMethodSubtype> subtypes) {
        if (subtypes == null) {
            this.mCount = 0;
            return;
        }
        this.mCount = subtypes.size();
        this.mInstance = (InputMethodSubtype[]) subtypes.toArray(new InputMethodSubtype[this.mCount]);
    }

    public InputMethodSubtypeArray(Parcel source) {
        this.mCount = source.readInt();
        if (this.mCount > 0) {
            this.mDecompressedSize = source.readInt();
            this.mCompressedData = source.createByteArray();
        }
    }

    public void writeToParcel(Parcel dest) {
        int i = this.mCount;
        if (i == 0) {
            dest.writeInt(i);
            return;
        }
        byte[] compressedData = this.mCompressedData;
        int decompressedSize = this.mDecompressedSize;
        if (compressedData == null && decompressedSize == 0) {
            synchronized (this.mLockObject) {
                compressedData = this.mCompressedData;
                decompressedSize = this.mDecompressedSize;
                if (compressedData == null && decompressedSize == 0) {
                    byte[] decompressedData = marshall(this.mInstance);
                    compressedData = compress(decompressedData);
                    if (compressedData == null) {
                        decompressedSize = -1;
                        Slog.i(TAG, "Failed to compress data.");
                    } else {
                        decompressedSize = decompressedData.length;
                    }
                    this.mDecompressedSize = decompressedSize;
                    this.mCompressedData = compressedData;
                }
            }
        }
        if (compressedData == null || decompressedSize <= 0) {
            Slog.i(TAG, "Unexpected state. Behaving as an empty array.");
            dest.writeInt(0);
        } else {
            dest.writeInt(this.mCount);
            dest.writeInt(decompressedSize);
            dest.writeByteArray(compressedData);
        }
    }

    public InputMethodSubtype get(int index) {
        if (index < 0 || this.mCount <= index) {
            throw new ArrayIndexOutOfBoundsException();
        }
        InputMethodSubtype[] instance = this.mInstance;
        if (instance == null) {
            synchronized (this.mLockObject) {
                instance = this.mInstance;
                if (instance == null) {
                    byte[] decompressedData = decompress(this.mCompressedData, this.mDecompressedSize);
                    this.mCompressedData = null;
                    this.mDecompressedSize = 0;
                    if (decompressedData != null) {
                        instance = unmarshall(decompressedData);
                    } else {
                        Slog.e(TAG, "Failed to decompress data. Returns null as fallback.");
                        instance = new InputMethodSubtype[this.mCount];
                    }
                    this.mInstance = instance;
                }
            }
        }
        return instance[index];
    }

    public int getCount() {
        return this.mCount;
    }

    private static byte[] marshall(InputMethodSubtype[] array) {
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            parcel.writeTypedArray(array, 0);
            byte[] marshall = parcel.marshall();
            parcel.recycle();
            return marshall;
        } catch (Throwable th) {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }

    private static InputMethodSubtype[] unmarshall(byte[] data) {
        Parcel parcel = null;
        try {
            parcel = Parcel.obtain();
            parcel.unmarshall(data, 0, data.length);
            parcel.setDataPosition(0);
            InputMethodSubtype[] inputMethodSubtypeArr = (InputMethodSubtype[]) parcel.createTypedArray(InputMethodSubtype.CREATOR);
            parcel.recycle();
            return inputMethodSubtypeArr;
        } catch (Throwable th) {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }

    /* JADX WARNING: Missing block: B:17:?, code skipped:
            $closeResource(r3, r2);
     */
    /* JADX WARNING: Missing block: B:24:?, code skipped:
            $closeResource(r2, r1);
     */
    private static byte[] compress(byte[] r5) {
        /*
        r0 = 0;
        r1 = new java.io.ByteArrayOutputStream;	 Catch:{ Exception -> 0x002b }
        r1.<init>();	 Catch:{ Exception -> 0x002b }
        r2 = new java.util.zip.GZIPOutputStream;	 Catch:{ all -> 0x0024 }
        r2.<init>(r1);	 Catch:{ all -> 0x0024 }
        r2.write(r5);	 Catch:{ all -> 0x001d }
        r2.finish();	 Catch:{ all -> 0x001d }
        r3 = r1.toByteArray();	 Catch:{ all -> 0x001d }
        $closeResource(r0, r2);	 Catch:{ all -> 0x0024 }
        $closeResource(r0, r1);	 Catch:{ Exception -> 0x002b }
        return r3;
    L_0x001d:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x001f }
    L_0x001f:
        r4 = move-exception;
        $closeResource(r3, r2);	 Catch:{ all -> 0x0024 }
        throw r4;	 Catch:{ all -> 0x0024 }
    L_0x0024:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0026 }
    L_0x0026:
        r3 = move-exception;
        $closeResource(r2, r1);	 Catch:{ Exception -> 0x002b }
        throw r3;	 Catch:{ Exception -> 0x002b }
    L_0x002b:
        r1 = move-exception;
        r2 = "InputMethodSubtypeArray";
        r3 = "Failed to compress the data.";
        android.util.Slog.e(r2, r3, r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodSubtypeArray.compress(byte[]):byte[]");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    /* JADX WARNING: Missing block: B:28:?, code skipped:
            $closeResource(r3, r2);
     */
    /* JADX WARNING: Missing block: B:35:?, code skipped:
            $closeResource(r2, r1);
     */
    private static byte[] decompress(byte[] r7, int r8) {
        /*
        r0 = 0;
        r1 = new java.io.ByteArrayInputStream;	 Catch:{ Exception -> 0x003d }
        r1.<init>(r7);	 Catch:{ Exception -> 0x003d }
        r2 = new java.util.zip.GZIPInputStream;	 Catch:{ all -> 0x0036 }
        r2.<init>(r1);	 Catch:{ all -> 0x0036 }
        r3 = new byte[r8];	 Catch:{ all -> 0x002f }
        r4 = 0;
    L_0x000f:
        r5 = r3.length;	 Catch:{ all -> 0x002f }
        if (r4 >= r5) goto L_0x001d;
    L_0x0012:
        r5 = r3.length;	 Catch:{ all -> 0x002f }
        r5 = r5 - r4;
        r6 = r2.read(r3, r4, r5);	 Catch:{ all -> 0x002f }
        if (r6 >= 0) goto L_0x001b;
    L_0x001a:
        goto L_0x001d;
    L_0x001b:
        r4 = r4 + r6;
        goto L_0x000f;
    L_0x001d:
        if (r8 == r4) goto L_0x0027;
        $closeResource(r0, r2);	 Catch:{ all -> 0x0036 }
        $closeResource(r0, r1);	 Catch:{ Exception -> 0x003d }
        return r0;
        $closeResource(r0, r2);	 Catch:{ all -> 0x0036 }
        $closeResource(r0, r1);	 Catch:{ Exception -> 0x003d }
        return r3;
    L_0x002f:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0031 }
    L_0x0031:
        r4 = move-exception;
        $closeResource(r3, r2);	 Catch:{ all -> 0x0036 }
        throw r4;	 Catch:{ all -> 0x0036 }
    L_0x0036:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0038 }
    L_0x0038:
        r3 = move-exception;
        $closeResource(r2, r1);	 Catch:{ Exception -> 0x003d }
        throw r3;	 Catch:{ Exception -> 0x003d }
    L_0x003d:
        r1 = move-exception;
        r2 = "InputMethodSubtypeArray";
        r3 = "Failed to decompress the data.";
        android.util.Slog.e(r2, r3, r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.inputmethod.InputMethodSubtypeArray.decompress(byte[], int):byte[]");
    }
}
