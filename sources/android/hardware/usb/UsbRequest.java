package android.hardware.usb;

import android.annotation.UnsupportedAppUsage;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.nio.ByteBuffer;

public class UsbRequest {
    static final int MAX_USBFS_BUFFER_SIZE = 16384;
    private static final String TAG = "UsbRequest";
    @UnsupportedAppUsage
    private ByteBuffer mBuffer;
    private Object mClientData;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private UsbDeviceConnection mConnection;
    private UsbEndpoint mEndpoint;
    private boolean mIsUsingNewQueue;
    @UnsupportedAppUsage
    private int mLength;
    private final Object mLock = new Object();
    @UnsupportedAppUsage
    private long mNativeContext;
    private ByteBuffer mTempBuffer;

    private native boolean native_cancel();

    private native void native_close();

    private native int native_dequeue_array(byte[] bArr, int i, boolean z);

    private native int native_dequeue_direct();

    private native boolean native_init(UsbDeviceConnection usbDeviceConnection, int i, int i2, int i3, int i4);

    private native boolean native_queue(ByteBuffer byteBuffer, int i, int i2);

    private native boolean native_queue_array(byte[] bArr, int i, boolean z);

    private native boolean native_queue_direct(ByteBuffer byteBuffer, int i, boolean z);

    public boolean initialize(UsbDeviceConnection connection, UsbEndpoint endpoint) {
        this.mEndpoint = endpoint;
        this.mConnection = (UsbDeviceConnection) Preconditions.checkNotNull(connection, "connection");
        boolean wasInitialized = native_init(connection, endpoint.getAddress(), endpoint.getAttributes(), endpoint.getMaxPacketSize(), endpoint.getInterval());
        if (wasInitialized) {
            this.mCloseGuard.open("close");
        }
        return wasInitialized;
    }

    public void close() {
        if (this.mNativeContext != 0) {
            this.mEndpoint = null;
            this.mConnection = null;
            native_close();
            this.mCloseGuard.close();
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    public UsbEndpoint getEndpoint() {
        return this.mEndpoint;
    }

    public Object getClientData() {
        return this.mClientData;
    }

    public void setClientData(Object data) {
        this.mClientData = data;
    }

    @Deprecated
    public boolean queue(ByteBuffer buffer, int length) {
        boolean result;
        boolean out = this.mEndpoint.getDirection() == 0;
        if (this.mConnection.getContext().getApplicationInfo().targetSdkVersion < 28 && length > 16384) {
            length = 16384;
        }
        synchronized (this.mLock) {
            this.mBuffer = buffer;
            this.mLength = length;
            if (buffer.isDirect()) {
                result = native_queue_direct(buffer, length, out);
            } else if (buffer.hasArray()) {
                result = native_queue_array(buffer.array(), length, out);
            } else {
                throw new IllegalArgumentException("buffer is not direct and has no array");
            }
            if (!result) {
                this.mBuffer = null;
                this.mLength = 0;
            }
        }
        return result;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0068  */
    public boolean queue(java.nio.ByteBuffer r9) {
        /*
        r8 = this;
        r0 = r8.mNativeContext;
        r2 = 0;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        r1 = 1;
        r2 = 0;
        if (r0 == 0) goto L_0x000c;
    L_0x000a:
        r0 = r1;
        goto L_0x000d;
    L_0x000c:
        r0 = r2;
    L_0x000d:
        r3 = "request is not initialized";
        com.android.internal.util.Preconditions.checkState(r0, r3);
        r0 = r8.mIsUsingNewQueue;
        r0 = r0 ^ r1;
        r3 = "this request is currently queued";
        com.android.internal.util.Preconditions.checkState(r0, r3);
        r0 = r8.mEndpoint;
        r0 = r0.getDirection();
        if (r0 != 0) goto L_0x0026;
    L_0x0024:
        r0 = r1;
        goto L_0x0027;
    L_0x0026:
        r0 = r2;
    L_0x0027:
        r3 = r8.mLock;
        monitor-enter(r3);
        r8.mBuffer = r9;	 Catch:{ all -> 0x00a7 }
        r4 = 0;
        if (r9 != 0) goto L_0x0036;
    L_0x002f:
        r8.mIsUsingNewQueue = r1;	 Catch:{ all -> 0x00a7 }
        r1 = r8.native_queue(r4, r2, r2);	 Catch:{ all -> 0x00a7 }
        goto L_0x009d;
    L_0x0036:
        r5 = r8.mConnection;	 Catch:{ all -> 0x00a7 }
        r5 = r5.getContext();	 Catch:{ all -> 0x00a7 }
        r5 = r5.getApplicationInfo();	 Catch:{ all -> 0x00a7 }
        r5 = r5.targetSdkVersion;	 Catch:{ all -> 0x00a7 }
        r6 = 28;
        if (r5 >= r6) goto L_0x0051;
    L_0x0046:
        r5 = r9.remaining();	 Catch:{ all -> 0x00a7 }
        r6 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r7 = "number of remaining bytes";
        com.android.internal.util.Preconditions.checkArgumentInRange(r5, r2, r6, r7);	 Catch:{ all -> 0x00a7 }
    L_0x0051:
        r5 = r9.isReadOnly();	 Catch:{ all -> 0x00a7 }
        if (r5 == 0) goto L_0x005c;
    L_0x0057:
        if (r0 == 0) goto L_0x005a;
    L_0x0059:
        goto L_0x005c;
    L_0x005a:
        r5 = r2;
        goto L_0x005d;
    L_0x005c:
        r5 = r1;
    L_0x005d:
        r6 = "buffer can not be read-only when receiving data";
        com.android.internal.util.Preconditions.checkArgument(r5, r6);	 Catch:{ all -> 0x00a7 }
        r5 = r9.isDirect();	 Catch:{ all -> 0x00a7 }
        if (r5 != 0) goto L_0x008f;
    L_0x0068:
        r5 = r8.mBuffer;	 Catch:{ all -> 0x00a7 }
        r5 = r5.remaining();	 Catch:{ all -> 0x00a7 }
        r5 = java.nio.ByteBuffer.allocateDirect(r5);	 Catch:{ all -> 0x00a7 }
        r8.mTempBuffer = r5;	 Catch:{ all -> 0x00a7 }
        if (r0 == 0) goto L_0x008c;
    L_0x0076:
        r5 = r8.mBuffer;	 Catch:{ all -> 0x00a7 }
        r5.mark();	 Catch:{ all -> 0x00a7 }
        r5 = r8.mTempBuffer;	 Catch:{ all -> 0x00a7 }
        r6 = r8.mBuffer;	 Catch:{ all -> 0x00a7 }
        r5.put(r6);	 Catch:{ all -> 0x00a7 }
        r5 = r8.mTempBuffer;	 Catch:{ all -> 0x00a7 }
        r5.flip();	 Catch:{ all -> 0x00a7 }
        r5 = r8.mBuffer;	 Catch:{ all -> 0x00a7 }
        r5.reset();	 Catch:{ all -> 0x00a7 }
    L_0x008c:
        r5 = r8.mTempBuffer;	 Catch:{ all -> 0x00a7 }
        r9 = r5;
    L_0x008f:
        r8.mIsUsingNewQueue = r1;	 Catch:{ all -> 0x00a7 }
        r1 = r9.position();	 Catch:{ all -> 0x00a7 }
        r5 = r9.remaining();	 Catch:{ all -> 0x00a7 }
        r1 = r8.native_queue(r9, r1, r5);	 Catch:{ all -> 0x00a7 }
    L_0x009d:
        monitor-exit(r3);	 Catch:{ all -> 0x00a7 }
        if (r1 != 0) goto L_0x00a6;
    L_0x00a0:
        r8.mIsUsingNewQueue = r2;
        r8.mTempBuffer = r4;
        r8.mBuffer = r4;
    L_0x00a6:
        return r1;
    L_0x00a7:
        r1 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x00a7 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.usb.UsbRequest.queue(java.nio.ByteBuffer):boolean");
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:17:0x0039, B:31:0x0076] */
    /* JADX WARNING: Missing block: B:23:0x0052, code skipped:
            r8.mTempBuffer = null;
     */
    /* JADX WARNING: Missing block: B:33:0x007c, code skipped:
            r1 = move-exception;
     */
    /* JADX WARNING: Missing block: B:34:0x007d, code skipped:
            if (r9 != false) goto L_0x007f;
     */
    /* JADX WARNING: Missing block: B:36:?, code skipped:
            r4 = TAG;
            r6 = new java.lang.StringBuilder();
            r6.append("Buffer ");
            r6.append(r8.mBuffer);
            r6.append(" does not have enough space to read ");
            r6.append(r5);
            r6.append(" bytes");
            android.util.Log.e(r4, r6.toString(), r1);
     */
    /* JADX WARNING: Missing block: B:37:0x00a9, code skipped:
            throw new java.nio.BufferOverflowException();
     */
    /* JADX WARNING: Missing block: B:39:0x00ab, code skipped:
            throw r1;
     */
    public void dequeue(boolean r9) {
        /*
        r8 = this;
        r0 = r8.mEndpoint;
        r0 = r0.getDirection();
        r1 = 0;
        if (r0 != 0) goto L_0x000b;
    L_0x0009:
        r0 = 1;
        goto L_0x000c;
    L_0x000b:
        r0 = r1;
    L_0x000c:
        r2 = r8.mLock;
        monitor-enter(r2);
        r3 = r8.mIsUsingNewQueue;	 Catch:{ all -> 0x00b2 }
        r4 = 0;
        if (r3 == 0) goto L_0x0055;
    L_0x0014:
        r3 = r8.native_dequeue_direct();	 Catch:{ all -> 0x00b2 }
        r8.mIsUsingNewQueue = r1;	 Catch:{ all -> 0x00b2 }
        r5 = r8.mBuffer;	 Catch:{ all -> 0x00b2 }
        if (r5 != 0) goto L_0x0020;
    L_0x001e:
        goto L_0x00ac;
    L_0x0020:
        r5 = r8.mTempBuffer;	 Catch:{ all -> 0x00b2 }
        if (r5 != 0) goto L_0x0032;
    L_0x0024:
        r5 = r8.mBuffer;	 Catch:{ all -> 0x00b2 }
        r6 = r8.mBuffer;	 Catch:{ all -> 0x00b2 }
        r6 = r6.position();	 Catch:{ all -> 0x00b2 }
        r6 = r6 + r3;
        r5.position(r6);	 Catch:{ all -> 0x00b2 }
        goto L_0x00ac;
    L_0x0032:
        r5 = r8.mTempBuffer;	 Catch:{ all -> 0x00b2 }
        r5.limit(r3);	 Catch:{ all -> 0x00b2 }
        if (r0 == 0) goto L_0x0046;
    L_0x0039:
        r5 = r8.mBuffer;	 Catch:{ all -> 0x0051 }
        r6 = r8.mBuffer;	 Catch:{ all -> 0x0051 }
        r6 = r6.position();	 Catch:{ all -> 0x0051 }
        r6 = r6 + r3;
        r5.position(r6);	 Catch:{ all -> 0x0051 }
        goto L_0x004d;
    L_0x0046:
        r5 = r8.mBuffer;	 Catch:{ all -> 0x0051 }
        r6 = r8.mTempBuffer;	 Catch:{ all -> 0x0051 }
        r5.put(r6);	 Catch:{ all -> 0x0051 }
    L_0x004d:
        r8.mTempBuffer = r4;	 Catch:{ all -> 0x00b2 }
        goto L_0x00ac;
    L_0x0051:
        r1 = move-exception;
        r8.mTempBuffer = r4;	 Catch:{ all -> 0x00b2 }
        throw r1;	 Catch:{ all -> 0x00b2 }
    L_0x0055:
        r3 = r8.mBuffer;	 Catch:{ all -> 0x00b2 }
        r3 = r3.isDirect();	 Catch:{ all -> 0x00b2 }
        if (r3 == 0) goto L_0x0062;
    L_0x005d:
        r3 = r8.native_dequeue_direct();	 Catch:{ all -> 0x00b2 }
        goto L_0x006e;
    L_0x0062:
        r3 = r8.mBuffer;	 Catch:{ all -> 0x00b2 }
        r3 = r3.array();	 Catch:{ all -> 0x00b2 }
        r5 = r8.mLength;	 Catch:{ all -> 0x00b2 }
        r3 = r8.native_dequeue_array(r3, r5, r0);	 Catch:{ all -> 0x00b2 }
    L_0x006e:
        if (r3 < 0) goto L_0x00ac;
    L_0x0070:
        r5 = r8.mLength;	 Catch:{ all -> 0x00b2 }
        r5 = java.lang.Math.min(r3, r5);	 Catch:{ all -> 0x00b2 }
        r6 = r8.mBuffer;	 Catch:{ IllegalArgumentException -> 0x007c }
        r6.position(r5);	 Catch:{ IllegalArgumentException -> 0x007c }
        goto L_0x00ac;
    L_0x007c:
        r1 = move-exception;
        if (r9 == 0) goto L_0x00aa;
    L_0x007f:
        r4 = "UsbRequest";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00b2 }
        r6.<init>();	 Catch:{ all -> 0x00b2 }
        r7 = "Buffer ";
        r6.append(r7);	 Catch:{ all -> 0x00b2 }
        r7 = r8.mBuffer;	 Catch:{ all -> 0x00b2 }
        r6.append(r7);	 Catch:{ all -> 0x00b2 }
        r7 = " does not have enough space to read ";
        r6.append(r7);	 Catch:{ all -> 0x00b2 }
        r6.append(r5);	 Catch:{ all -> 0x00b2 }
        r7 = " bytes";
        r6.append(r7);	 Catch:{ all -> 0x00b2 }
        r6 = r6.toString();	 Catch:{ all -> 0x00b2 }
        android.util.Log.e(r4, r6, r1);	 Catch:{ all -> 0x00b2 }
        r4 = new java.nio.BufferOverflowException;	 Catch:{ all -> 0x00b2 }
        r4.<init>();	 Catch:{ all -> 0x00b2 }
        throw r4;	 Catch:{ all -> 0x00b2 }
        throw r1;	 Catch:{ all -> 0x00b2 }
    L_0x00ac:
        r8.mBuffer = r4;	 Catch:{ all -> 0x00b2 }
        r8.mLength = r1;	 Catch:{ all -> 0x00b2 }
        monitor-exit(r2);	 Catch:{ all -> 0x00b2 }
        return;
    L_0x00b2:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x00b2 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.usb.UsbRequest.dequeue(boolean):void");
    }

    public boolean cancel() {
        return native_cancel();
    }
}
