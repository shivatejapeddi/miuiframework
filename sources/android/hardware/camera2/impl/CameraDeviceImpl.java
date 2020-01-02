package android.hardware.camera2.impl;

import android.app.ActivityThread;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraDevice.StateCallback;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.ICameraDeviceCallbacks.Stub;
import android.hardware.camera2.ICameraDeviceUser;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.InputConfiguration;
import android.hardware.camera2.params.OutputConfiguration;
import android.hardware.camera2.params.SessionConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.hardware.camera2.utils.SubmitInfo;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.os.SystemProperties;
import android.text.TextUtils.SimpleStringSplitter;
import android.text.TextUtils.StringSplitter;
import android.util.Log;
import android.util.Range;
import android.util.Size;
import android.util.SparseArray;
import android.view.Surface;
import com.android.internal.util.Preconditions;
import com.android.internal.util.function.pooled.PooledLambda;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public class CameraDeviceImpl extends CameraDevice implements DeathRecipient {
    private static final long NANO_PER_SECOND = 1000000000;
    private static final int REQUEST_ID_NONE = -1;
    private final boolean DEBUG = false;
    private final String TAG;
    private int customOpMode = 0;
    private final int mAppTargetSdkVersion;
    private final Runnable mCallOnActive = new Runnable() {
        /* JADX WARNING: Missing block: B:9:0x0018, code skipped:
            if (r0 == null) goto L_0x001f;
     */
        /* JADX WARNING: Missing block: B:10:0x001a, code skipped:
            r0.onActive(r3.this$0);
     */
        /* JADX WARNING: Missing block: B:11:0x001f, code skipped:
            return;
     */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1 = r1.mInterfaceLock;
            monitor-enter(r1);
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0020 }
            r2 = r2.mRemoteDevice;	 Catch:{ all -> 0x0020 }
            if (r2 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            return;
        L_0x0010:
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0020 }
            r2 = r2.mSessionStateCallback;	 Catch:{ all -> 0x0020 }
            r0 = r2;
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            if (r0 == 0) goto L_0x001f;
        L_0x001a:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onActive(r1);
        L_0x001f:
            return;
        L_0x0020:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl$AnonymousClass3.run():void");
        }
    };
    private final Runnable mCallOnBusy = new Runnable() {
        /* JADX WARNING: Missing block: B:9:0x0018, code skipped:
            if (r0 == null) goto L_0x001f;
     */
        /* JADX WARNING: Missing block: B:10:0x001a, code skipped:
            r0.onBusy(r3.this$0);
     */
        /* JADX WARNING: Missing block: B:11:0x001f, code skipped:
            return;
     */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1 = r1.mInterfaceLock;
            monitor-enter(r1);
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0020 }
            r2 = r2.mRemoteDevice;	 Catch:{ all -> 0x0020 }
            if (r2 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            return;
        L_0x0010:
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0020 }
            r2 = r2.mSessionStateCallback;	 Catch:{ all -> 0x0020 }
            r0 = r2;
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            if (r0 == 0) goto L_0x001f;
        L_0x001a:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onBusy(r1);
        L_0x001f:
            return;
        L_0x0020:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl$AnonymousClass4.run():void");
        }
    };
    private final Runnable mCallOnClosed = new Runnable() {
        private boolean mClosedOnce = false;

        public void run() {
            if (this.mClosedOnce) {
                throw new AssertionError("Don't post #onClosed more than once");
            }
            StateCallbackKK sessionCallback;
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onClosed(CameraDeviceImpl.this);
            }
            CameraDeviceImpl.this.mDeviceCallback.onClosed(CameraDeviceImpl.this);
            this.mClosedOnce = true;
        }
    };
    private final Runnable mCallOnDisconnected = new Runnable() {
        /* JADX WARNING: Missing block: B:9:0x0018, code skipped:
            if (r0 == null) goto L_0x001f;
     */
        /* JADX WARNING: Missing block: B:10:0x001a, code skipped:
            r0.onDisconnected(r3.this$0);
     */
        /* JADX WARNING: Missing block: B:11:0x001f, code skipped:
            android.hardware.camera2.impl.CameraDeviceImpl.access$200(r3.this$0).onDisconnected(r3.this$0);
     */
        /* JADX WARNING: Missing block: B:12:0x002a, code skipped:
            return;
     */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1 = r1.mInterfaceLock;
            monitor-enter(r1);
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x002b }
            r2 = r2.mRemoteDevice;	 Catch:{ all -> 0x002b }
            if (r2 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r1);	 Catch:{ all -> 0x002b }
            return;
        L_0x0010:
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x002b }
            r2 = r2.mSessionStateCallback;	 Catch:{ all -> 0x002b }
            r0 = r2;
            monitor-exit(r1);	 Catch:{ all -> 0x002b }
            if (r0 == 0) goto L_0x001f;
        L_0x001a:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onDisconnected(r1);
        L_0x001f:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1 = r1.mDeviceCallback;
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1.onDisconnected(r2);
            return;
        L_0x002b:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x002b }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl$AnonymousClass7.run():void");
        }
    };
    private final Runnable mCallOnIdle = new Runnable() {
        /* JADX WARNING: Missing block: B:9:0x0018, code skipped:
            if (r0 == null) goto L_0x001f;
     */
        /* JADX WARNING: Missing block: B:10:0x001a, code skipped:
            r0.onIdle(r3.this$0);
     */
        /* JADX WARNING: Missing block: B:11:0x001f, code skipped:
            return;
     */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1 = r1.mInterfaceLock;
            monitor-enter(r1);
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0020 }
            r2 = r2.mRemoteDevice;	 Catch:{ all -> 0x0020 }
            if (r2 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            return;
        L_0x0010:
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0020 }
            r2 = r2.mSessionStateCallback;	 Catch:{ all -> 0x0020 }
            r0 = r2;
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            if (r0 == 0) goto L_0x001f;
        L_0x001a:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onIdle(r1);
        L_0x001f:
            return;
        L_0x0020:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl$AnonymousClass6.run():void");
        }
    };
    private final Runnable mCallOnOpened = new Runnable() {
        /* JADX WARNING: Missing block: B:9:0x0018, code skipped:
            if (r0 == null) goto L_0x001f;
     */
        /* JADX WARNING: Missing block: B:10:0x001a, code skipped:
            r0.onOpened(r3.this$0);
     */
        /* JADX WARNING: Missing block: B:11:0x001f, code skipped:
            android.hardware.camera2.impl.CameraDeviceImpl.access$200(r3.this$0).onOpened(r3.this$0);
     */
        /* JADX WARNING: Missing block: B:12:0x002a, code skipped:
            return;
     */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1 = r1.mInterfaceLock;
            monitor-enter(r1);
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x002b }
            r2 = r2.mRemoteDevice;	 Catch:{ all -> 0x002b }
            if (r2 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r1);	 Catch:{ all -> 0x002b }
            return;
        L_0x0010:
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x002b }
            r2 = r2.mSessionStateCallback;	 Catch:{ all -> 0x002b }
            r0 = r2;
            monitor-exit(r1);	 Catch:{ all -> 0x002b }
            if (r0 == 0) goto L_0x001f;
        L_0x001a:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onOpened(r1);
        L_0x001f:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1 = r1.mDeviceCallback;
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1.onOpened(r2);
            return;
        L_0x002b:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x002b }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl$AnonymousClass1.run():void");
        }
    };
    private final Runnable mCallOnUnconfigured = new Runnable() {
        /* JADX WARNING: Missing block: B:9:0x0018, code skipped:
            if (r0 == null) goto L_0x001f;
     */
        /* JADX WARNING: Missing block: B:10:0x001a, code skipped:
            r0.onUnconfigured(r3.this$0);
     */
        /* JADX WARNING: Missing block: B:11:0x001f, code skipped:
            return;
     */
        public void run() {
            /*
            r3 = this;
            r0 = 0;
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r1 = r1.mInterfaceLock;
            monitor-enter(r1);
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0020 }
            r2 = r2.mRemoteDevice;	 Catch:{ all -> 0x0020 }
            if (r2 != 0) goto L_0x0010;
        L_0x000e:
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            return;
        L_0x0010:
            r2 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0020 }
            r2 = r2.mSessionStateCallback;	 Catch:{ all -> 0x0020 }
            r0 = r2;
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            if (r0 == 0) goto L_0x001f;
        L_0x001a:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0.onUnconfigured(r1);
        L_0x001f:
            return;
        L_0x0020:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl$AnonymousClass2.run():void");
        }
    };
    private final CameraDeviceCallbacks mCallbacks = new CameraDeviceCallbacks();
    private final String mCameraId;
    private final SparseArray<CaptureCallbackHolder> mCaptureCallbackMap = new SparseArray();
    private final CameraCharacteristics mCharacteristics;
    private final AtomicBoolean mClosing = new AtomicBoolean();
    private SimpleEntry<Integer, InputConfiguration> mConfiguredInput = new SimpleEntry(Integer.valueOf(-1), null);
    private final SparseArray<OutputConfiguration> mConfiguredOutputs = new SparseArray();
    private CameraCaptureSessionCore mCurrentSession;
    private final StateCallback mDeviceCallback;
    private final Executor mDeviceExecutor;
    private final FrameNumberTracker mFrameNumberTracker = new FrameNumberTracker();
    private boolean mIdle = true;
    private boolean mInError = false;
    final Object mInterfaceLock = new Object();
    private boolean mIsPrivilegedApp = false;
    private int mNextSessionId = 0;
    private ICameraDeviceUserWrapper mRemoteDevice;
    private int mRepeatingRequestId = -1;
    private int[] mRepeatingRequestTypes;
    private final List<RequestLastFrameNumbersHolder> mRequestLastFrameNumbersList = new ArrayList();
    private volatile StateCallbackKK mSessionStateCallback;
    private final int mTotalPartialCount;

    public interface CaptureCallback {
        public static final int NO_FRAMES_CAPTURED = -1;

        void onCaptureBufferLost(CameraDevice cameraDevice, CaptureRequest captureRequest, Surface surface, long j);

        void onCaptureCompleted(CameraDevice cameraDevice, CaptureRequest captureRequest, TotalCaptureResult totalCaptureResult);

        void onCaptureFailed(CameraDevice cameraDevice, CaptureRequest captureRequest, CaptureFailure captureFailure);

        void onCapturePartial(CameraDevice cameraDevice, CaptureRequest captureRequest, CaptureResult captureResult);

        void onCaptureProgressed(CameraDevice cameraDevice, CaptureRequest captureRequest, CaptureResult captureResult);

        void onCaptureSequenceAborted(CameraDevice cameraDevice, int i);

        void onCaptureSequenceCompleted(CameraDevice cameraDevice, int i, long j);

        void onCaptureStarted(CameraDevice cameraDevice, CaptureRequest captureRequest, long j, long j2);
    }

    public static abstract class StateCallbackKK extends StateCallback {
        public void onUnconfigured(CameraDevice camera) {
        }

        public void onActive(CameraDevice camera) {
        }

        public void onBusy(CameraDevice camera) {
        }

        public void onIdle(CameraDevice camera) {
        }

        public void onRequestQueueEmpty() {
        }

        public void onSurfacePrepared(Surface surface) {
        }
    }

    public class CameraDeviceCallbacks extends Stub {
        public IBinder asBinder() {
            return this;
        }

        /* JADX WARNING: Missing block: B:26:0x0063, code skipped:
            return;
     */
        public void onDeviceError(int r6, android.hardware.camera2.impl.CaptureResultExtras r7) {
            /*
            r5 = this;
            r0 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0 = r0.mInterfaceLock;
            monitor-enter(r0);
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0069 }
            r1 = r1.mRemoteDevice;	 Catch:{ all -> 0x0069 }
            if (r1 != 0) goto L_0x000f;
        L_0x000d:
            monitor-exit(r0);	 Catch:{ all -> 0x0069 }
            return;
        L_0x000f:
            if (r6 == 0) goto L_0x004a;
        L_0x0011:
            r1 = 1;
            r2 = 4;
            if (r6 == r1) goto L_0x0046;
        L_0x0015:
            r1 = 3;
            if (r6 == r1) goto L_0x0042;
        L_0x0018:
            if (r6 == r2) goto L_0x0042;
        L_0x001a:
            r2 = 5;
            if (r6 == r2) goto L_0x0042;
        L_0x001d:
            r3 = 6;
            if (r6 == r3) goto L_0x003e;
        L_0x0020:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0069 }
            r1 = r1.TAG;	 Catch:{ all -> 0x0069 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0069 }
            r3.<init>();	 Catch:{ all -> 0x0069 }
            r4 = "Unknown error from camera device: ";
            r3.append(r4);	 Catch:{ all -> 0x0069 }
            r3.append(r6);	 Catch:{ all -> 0x0069 }
            r3 = r3.toString();	 Catch:{ all -> 0x0069 }
            android.util.Log.e(r1, r3);	 Catch:{ all -> 0x0069 }
            r5.scheduleNotifyError(r2);	 Catch:{ all -> 0x0069 }
            goto L_0x0062;
        L_0x003e:
            r5.scheduleNotifyError(r1);	 Catch:{ all -> 0x0069 }
            goto L_0x0062;
        L_0x0042:
            r5.onCaptureErrorLocked(r6, r7);	 Catch:{ all -> 0x0069 }
            goto L_0x0062;
        L_0x0046:
            r5.scheduleNotifyError(r2);	 Catch:{ all -> 0x0069 }
            goto L_0x0062;
        L_0x004a:
            r1 = android.os.Binder.clearCallingIdentity();	 Catch:{ all -> 0x0069 }
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0064 }
            r3 = r3.mDeviceExecutor;	 Catch:{ all -> 0x0064 }
            r4 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x0064 }
            r4 = r4.mCallOnDisconnected;	 Catch:{ all -> 0x0064 }
            r3.execute(r4);	 Catch:{ all -> 0x0064 }
            android.os.Binder.restoreCallingIdentity(r1);	 Catch:{ all -> 0x0069 }
        L_0x0062:
            monitor-exit(r0);	 Catch:{ all -> 0x0069 }
            return;
        L_0x0064:
            r3 = move-exception;
            android.os.Binder.restoreCallingIdentity(r1);	 Catch:{ all -> 0x0069 }
            throw r3;	 Catch:{ all -> 0x0069 }
        L_0x0069:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0069 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl$CameraDeviceCallbacks.onDeviceError(int, android.hardware.camera2.impl.CaptureResultExtras):void");
        }

        private void scheduleNotifyError(int code) {
            CameraDeviceImpl.this.mInError = true;
            long ident = Binder.clearCallingIdentity();
            try {
                CameraDeviceImpl.this.mDeviceExecutor.execute(PooledLambda.obtainRunnable(-$$Lambda$CameraDeviceImpl$CameraDeviceCallbacks$Sm85frAzwGZVMAK-NE_gwckYXVQ.INSTANCE, this, Integer.valueOf(code)).recycleOnUse());
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }

        private void notifyError(int code) {
            if (!CameraDeviceImpl.this.isClosed()) {
                CameraDeviceImpl.this.mDeviceCallback.onError(CameraDeviceImpl.this, code);
            }
        }

        /* JADX WARNING: Missing block: B:12:0x003c, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:14:0x003e, code skipped:
            return;
     */
        public void onRepeatingRequestError(long r6, int r8) {
            /*
            r5 = this;
            r0 = android.hardware.camera2.impl.CameraDeviceImpl.this;
            r0 = r0.mInterfaceLock;
            monitor-enter(r0);
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x003f }
            r1 = r1.mRemoteDevice;	 Catch:{ all -> 0x003f }
            if (r1 == 0) goto L_0x003d;
        L_0x000d:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x003f }
            r1 = r1.mRepeatingRequestId;	 Catch:{ all -> 0x003f }
            r2 = -1;
            if (r1 != r2) goto L_0x0017;
        L_0x0016:
            goto L_0x003d;
        L_0x0017:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x003f }
            r3 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x003f }
            r3 = r3.mRepeatingRequestId;	 Catch:{ all -> 0x003f }
            r4 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x003f }
            r4 = r4.mRepeatingRequestTypes;	 Catch:{ all -> 0x003f }
            r1.checkEarlyTriggerSequenceComplete(r3, r6, r4);	 Catch:{ all -> 0x003f }
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x003f }
            r1 = r1.mRepeatingRequestId;	 Catch:{ all -> 0x003f }
            if (r1 != r8) goto L_0x003b;
        L_0x0030:
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x003f }
            r1.mRepeatingRequestId = r2;	 Catch:{ all -> 0x003f }
            r1 = android.hardware.camera2.impl.CameraDeviceImpl.this;	 Catch:{ all -> 0x003f }
            r2 = 0;
            r1.mRepeatingRequestTypes = r2;	 Catch:{ all -> 0x003f }
        L_0x003b:
            monitor-exit(r0);	 Catch:{ all -> 0x003f }
            return;
        L_0x003d:
            monitor-exit(r0);	 Catch:{ all -> 0x003f }
            return;
        L_0x003f:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x003f }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl$CameraDeviceCallbacks.onRepeatingRequestError(long, int):void");
        }

        public void onDeviceIdle() {
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                if (!CameraDeviceImpl.this.mIdle) {
                    long ident = Binder.clearCallingIdentity();
                    try {
                        CameraDeviceImpl.this.mDeviceExecutor.execute(CameraDeviceImpl.this.mCallOnIdle);
                    } finally {
                        Binder.restoreCallingIdentity(ident);
                    }
                }
                CameraDeviceImpl.this.mIdle = true;
            }
        }

        public void onCaptureStarted(CaptureResultExtras resultExtras, long timestamp) {
            int requestId = resultExtras.getRequestId();
            long frameNumber = resultExtras.getFrameNumber();
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                if (CameraDeviceImpl.this.mRemoteDevice == null) {
                    return;
                }
                CaptureCallbackHolder holder = (CaptureCallbackHolder) CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
                if (holder == null) {
                } else if (CameraDeviceImpl.this.isClosed()) {
                } else {
                    long ident = Binder.clearCallingIdentity();
                    try {
                        Executor executor = holder.getExecutor();
                        final CaptureResultExtras captureResultExtras = resultExtras;
                        final CaptureCallbackHolder captureCallbackHolder = holder;
                        final long j = timestamp;
                        AnonymousClass1 anonymousClass1 = r1;
                        final long j2 = frameNumber;
                        AnonymousClass1 anonymousClass12 = new Runnable() {
                            public void run() {
                                if (!CameraDeviceImpl.this.isClosed()) {
                                    int subsequenceId = captureResultExtras.getSubsequenceId();
                                    CaptureRequest request = captureCallbackHolder.getRequest(subsequenceId);
                                    if (captureCallbackHolder.hasBatchedOutputs()) {
                                        Range<Integer> fpsRange = (Range) request.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
                                        for (int i = 0; i < captureCallbackHolder.getRequestCount(); i++) {
                                            captureCallbackHolder.getCallback().onCaptureStarted(CameraDeviceImpl.this, captureCallbackHolder.getRequest(i), j - ((((long) (subsequenceId - i)) * 1000000000) / ((long) ((Integer) fpsRange.getUpper()).intValue())), j2 - ((long) (subsequenceId - i)));
                                        }
                                        return;
                                    }
                                    captureCallbackHolder.getCallback().onCaptureStarted(CameraDeviceImpl.this, captureCallbackHolder.getRequest(captureResultExtras.getSubsequenceId()), j, j2);
                                }
                            }
                        };
                        executor.execute(anonymousClass1);
                    } finally {
                        Binder.restoreCallingIdentity(ident);
                    }
                }
            }
        }

        public void onResultReceived(CameraMetadataNative result, CaptureResultExtras resultExtras, PhysicalCaptureResultInfo[] physicalResults) throws RemoteException {
            Throwable th;
            int i;
            CameraMetadataNative cameraMetadataNative = result;
            int requestId = resultExtras.getRequestId();
            long frameNumber = resultExtras.getFrameNumber();
            CaptureRequest captureRequest = CameraDeviceImpl.this.mInterfaceLock;
            synchronized (captureRequest) {
                CaptureRequest captureRequest2;
                long j;
                try {
                    if (CameraDeviceImpl.this.mRemoteDevice == null) {
                        try {
                            return;
                        } catch (Throwable th2) {
                            th = th2;
                            captureRequest2 = captureRequest;
                            i = requestId;
                            j = frameNumber;
                            throw th;
                        }
                    }
                    cameraMetadataNative.set(CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE, (Size) CameraDeviceImpl.this.getCharacteristics().get(CameraCharacteristics.LENS_INFO_SHADING_MAP_SIZE));
                    CaptureCallbackHolder holder = (CaptureCallbackHolder) CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
                    CaptureRequest request = holder.getRequest(resultExtras.getSubsequenceId());
                    boolean isPartialResult = resultExtras.getPartialResultCount() < CameraDeviceImpl.this.mTotalPartialCount;
                    int requestType = request.getRequestType();
                    if (CameraDeviceImpl.this.isClosed()) {
                        CameraDeviceImpl.this.mFrameNumberTracker.updateTracker(frameNumber, null, isPartialResult, requestType);
                        return;
                    }
                    CameraMetadataNative resultCopy;
                    Runnable resultDispatch;
                    CaptureResult finalResult;
                    if (holder.hasBatchedOutputs()) {
                        resultCopy = new CameraMetadataNative(cameraMetadataNative);
                    } else {
                        resultCopy = null;
                    }
                    final CaptureCallbackHolder captureCallbackHolder;
                    final CameraMetadataNative cameraMetadataNative2;
                    CaptureRequest captureRequest3;
                    if (isPartialResult) {
                        final CaptureResult resultAsCapture = new CaptureResult(cameraMetadataNative, request, resultExtras);
                        captureCallbackHolder = holder;
                        cameraMetadataNative2 = resultCopy;
                        final CaptureResultExtras captureResultExtras = resultExtras;
                        final CaptureRequest captureRequest4 = request;
                        Runnable resultDispatch2 = new Runnable() {
                            public void run() {
                                if (!CameraDeviceImpl.this.isClosed()) {
                                    if (captureCallbackHolder.hasBatchedOutputs()) {
                                        for (int i = 0; i < captureCallbackHolder.getRequestCount(); i++) {
                                            captureCallbackHolder.getCallback().onCaptureProgressed(CameraDeviceImpl.this, captureCallbackHolder.getRequest(i), new CaptureResult(new CameraMetadataNative(cameraMetadataNative2), captureCallbackHolder.getRequest(i), captureResultExtras));
                                        }
                                        return;
                                    }
                                    captureCallbackHolder.getCallback().onCaptureProgressed(CameraDeviceImpl.this, captureRequest4, resultAsCapture);
                                }
                            }
                        };
                        CaptureResult finalResult2 = resultAsCapture;
                        resultDispatch = resultDispatch2;
                        captureRequest3 = request;
                        captureRequest2 = captureRequest;
                        i = requestId;
                        j = frameNumber;
                        finalResult = finalResult2;
                        frameNumber = holder;
                    } else {
                        final CaptureResult resultAsCapture2;
                        final CaptureResultExtras captureResultExtras2;
                        final List<CaptureResult> list;
                        final CaptureRequest captureRequest5;
                        List<CaptureResult> partialResults = CameraDeviceImpl.this.mFrameNumberTracker.popPartialResults(frameNumber);
                        final long sensorTimestamp = ((Long) cameraMetadataNative.get(CaptureResult.SENSOR_TIMESTAMP)).longValue();
                        captureRequest3 = request;
                        final Range<Integer> fpsRange = (Range) request.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE);
                        final int subsequenceId = resultExtras.getSubsequenceId();
                        j = frameNumber;
                        CaptureResult totalCaptureResult = totalCaptureResult;
                        captureRequest2 = captureRequest3;
                        try {
                            totalCaptureResult = new TotalCaptureResult(result, captureRequest2, resultExtras, partialResults, holder.getSessionId(), physicalResults);
                            frameNumber = holder;
                            resultAsCapture2 = totalCaptureResult;
                            AnonymousClass3 anonymousClass3 = anonymousClass3;
                            captureCallbackHolder = frameNumber;
                            cameraMetadataNative2 = resultCopy;
                            captureRequest2 = captureRequest;
                            captureResultExtras2 = resultExtras;
                            list = partialResults;
                            captureRequest5 = captureRequest3;
                        } catch (Throwable th3) {
                            th = th3;
                            captureRequest2 = captureRequest;
                            i = requestId;
                            throw th;
                        }
                        try {
                            resultDispatch = new Runnable() {
                                public void run() {
                                    if (!CameraDeviceImpl.this.isClosed()) {
                                        if (captureCallbackHolder.hasBatchedOutputs()) {
                                            for (int i = 0; i < captureCallbackHolder.getRequestCount(); i++) {
                                                cameraMetadataNative2.set(CaptureResult.SENSOR_TIMESTAMP, Long.valueOf(sensorTimestamp - ((((long) (subsequenceId - i)) * 1000000000) / ((long) ((Integer) fpsRange.getUpper()).intValue()))));
                                                captureCallbackHolder.getCallback().onCaptureCompleted(CameraDeviceImpl.this, captureCallbackHolder.getRequest(i), new TotalCaptureResult(new CameraMetadataNative(cameraMetadataNative2), captureCallbackHolder.getRequest(i), captureResultExtras2, list, captureCallbackHolder.getSessionId(), new PhysicalCaptureResultInfo[0]));
                                            }
                                            return;
                                        }
                                        captureCallbackHolder.getCallback().onCaptureCompleted(CameraDeviceImpl.this, captureRequest5, resultAsCapture2);
                                    }
                                }
                            };
                            finalResult = resultAsCapture2;
                        } catch (Throwable th4) {
                            th = th4;
                            throw th;
                        }
                    }
                    long ident = Binder.clearCallingIdentity();
                    frameNumber.getExecutor().execute(resultDispatch);
                    Binder.restoreCallingIdentity(ident);
                    CameraDeviceImpl.this.mFrameNumberTracker.updateTracker(j, finalResult, isPartialResult, requestType);
                    if (!isPartialResult) {
                        CameraDeviceImpl.this.checkAndFireSequenceComplete();
                    }
                } catch (Throwable th5) {
                    th = th5;
                    captureRequest2 = captureRequest;
                    i = requestId;
                    j = frameNumber;
                    throw th;
                }
            }
        }

        public void onPrepared(int streamId) {
            OutputConfiguration output;
            StateCallbackKK sessionCallback;
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                output = (OutputConfiguration) CameraDeviceImpl.this.mConfiguredOutputs.get(streamId);
                sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
            }
            if (sessionCallback != null) {
                if (output == null) {
                    Log.w(CameraDeviceImpl.this.TAG, "onPrepared invoked for unknown output Surface");
                    return;
                }
                for (Surface surface : output.getSurfaces()) {
                    sessionCallback.onSurfacePrepared(surface);
                }
            }
        }

        public void onRequestQueueEmpty() {
            StateCallbackKK sessionCallback;
            synchronized (CameraDeviceImpl.this.mInterfaceLock) {
                sessionCallback = CameraDeviceImpl.this.mSessionStateCallback;
            }
            if (sessionCallback != null) {
                sessionCallback.onRequestQueueEmpty();
            }
        }

        private void onCaptureErrorLocked(int errorCode, CaptureResultExtras resultExtras) {
            int i = errorCode;
            int requestId = resultExtras.getRequestId();
            int subsequenceId = resultExtras.getSubsequenceId();
            long frameNumber = resultExtras.getFrameNumber();
            String errorPhysicalCameraId = resultExtras.getErrorPhysicalCameraId();
            CaptureCallbackHolder holder = (CaptureCallbackHolder) CameraDeviceImpl.this.mCaptureCallbackMap.get(requestId);
            CaptureRequest request = holder.getRequest(subsequenceId);
            final CaptureRequest captureRequest;
            long j;
            CaptureCallbackHolder captureCallbackHolder;
            if (i == 5) {
                for (Surface surface : ((OutputConfiguration) CameraDeviceImpl.this.mConfiguredOutputs.get(resultExtras.getErrorStreamId())).getSurfaces()) {
                    if (request.containsTarget(surface)) {
                        final CaptureCallbackHolder captureCallbackHolder2 = holder;
                        captureRequest = request;
                        final Surface surface2 = surface;
                        final long j2 = frameNumber;
                        AnonymousClass4 failureDispatch = new Runnable() {
                            public void run() {
                                if (!CameraDeviceImpl.this.isClosed()) {
                                    captureCallbackHolder2.getCallback().onCaptureBufferLost(CameraDeviceImpl.this, captureRequest, surface2, j2);
                                }
                            }
                        };
                        long ident = Binder.clearCallingIdentity();
                        try {
                            holder.getExecutor().execute(failureDispatch);
                            AnonymousClass4 anonymousClass4 = failureDispatch;
                        } finally {
                            Binder.restoreCallingIdentity(ident);
                        }
                    }
                }
                j = frameNumber;
                captureCallbackHolder = holder;
                captureRequest = request;
                int i2 = subsequenceId;
                int i3 = requestId;
                return;
            }
            boolean z = false;
            captureRequest = request;
            boolean mayHaveBuffers = i == 4;
            if (CameraDeviceImpl.this.mCurrentSession != null && CameraDeviceImpl.this.mCurrentSession.isAborting()) {
                z = true;
            }
            captureCallbackHolder = holder;
            j = frameNumber;
            final CaptureFailure captureFailure = new CaptureFailure(captureRequest, z, mayHaveBuffers, requestId, j, errorPhysicalCameraId);
            Runnable failureDispatch2 = new Runnable() {
                public void run() {
                    if (!CameraDeviceImpl.this.isClosed()) {
                        captureCallbackHolder.getCallback().onCaptureFailed(CameraDeviceImpl.this, captureRequest, captureFailure);
                    }
                }
            };
            CameraDeviceImpl.this.mFrameNumberTracker.updateTracker(j, true, captureRequest.getRequestType());
            CameraDeviceImpl.this.checkAndFireSequenceComplete();
            long ident2 = Binder.clearCallingIdentity();
            try {
                captureCallbackHolder.getExecutor().execute(failureDispatch2);
                Runnable runnable = failureDispatch2;
            } finally {
                Binder.restoreCallingIdentity(ident2);
            }
        }
    }

    private static class CameraHandlerExecutor implements Executor {
        private final Handler mHandler;

        public CameraHandlerExecutor(Handler handler) {
            this.mHandler = (Handler) Preconditions.checkNotNull(handler);
        }

        public void execute(Runnable command) {
            this.mHandler.post(command);
        }
    }

    static class CaptureCallbackHolder {
        private final CaptureCallback mCallback;
        private final Executor mExecutor;
        private final boolean mHasBatchedOutputs;
        private final boolean mRepeating;
        private final List<CaptureRequest> mRequestList;
        private final int mSessionId;

        CaptureCallbackHolder(CaptureCallback callback, List<CaptureRequest> requestList, Executor executor, boolean repeating, int sessionId) {
            if (callback == null || executor == null) {
                throw new UnsupportedOperationException("Must have a valid handler and a valid callback");
            }
            this.mRepeating = repeating;
            this.mExecutor = executor;
            this.mRequestList = new ArrayList(requestList);
            this.mCallback = callback;
            this.mSessionId = sessionId;
            boolean hasBatchedOutputs = true;
            int i = 0;
            while (i < requestList.size()) {
                CaptureRequest request = (CaptureRequest) requestList.get(i);
                if (request.isPartOfCRequestList()) {
                    if (i == 0 && request.getTargets().size() != 2) {
                        hasBatchedOutputs = false;
                        break;
                    }
                    i++;
                } else {
                    hasBatchedOutputs = false;
                    break;
                }
            }
            this.mHasBatchedOutputs = hasBatchedOutputs;
        }

        public boolean isRepeating() {
            return this.mRepeating;
        }

        public CaptureCallback getCallback() {
            return this.mCallback;
        }

        public CaptureRequest getRequest(int subsequenceId) {
            if (subsequenceId >= this.mRequestList.size()) {
                throw new IllegalArgumentException(String.format("Requested subsequenceId %d is larger than request list size %d.", new Object[]{Integer.valueOf(subsequenceId), Integer.valueOf(this.mRequestList.size())}));
            } else if (subsequenceId >= 0) {
                return (CaptureRequest) this.mRequestList.get(subsequenceId);
            } else {
                throw new IllegalArgumentException(String.format("Requested subsequenceId %d is negative", new Object[]{Integer.valueOf(subsequenceId)}));
            }
        }

        public CaptureRequest getRequest() {
            return getRequest(0);
        }

        public Executor getExecutor() {
            return this.mExecutor;
        }

        public int getSessionId() {
            return this.mSessionId;
        }

        public int getRequestCount() {
            return this.mRequestList.size();
        }

        public boolean hasBatchedOutputs() {
            return this.mHasBatchedOutputs;
        }
    }

    public class FrameNumberTracker {
        private long[] mCompletedFrameNumber = new long[3];
        private final TreeMap<Long, Integer> mFutureErrorMap = new TreeMap();
        private final HashMap<Long, List<CaptureResult>> mPartialResults = new HashMap();
        private final LinkedList<Long>[] mSkippedFrameNumbers = new LinkedList[3];
        private final LinkedList<Long>[] mSkippedOtherFrameNumbers = new LinkedList[3];

        public FrameNumberTracker() {
            for (int i = 0; i < 3; i++) {
                this.mCompletedFrameNumber[i] = -1;
                this.mSkippedOtherFrameNumbers[i] = new LinkedList();
                this.mSkippedFrameNumbers[i] = new LinkedList();
            }
        }

        private void update() {
            Iterator iter = this.mFutureErrorMap.entrySet().iterator();
            while (iter.hasNext()) {
                Entry pair = (Entry) iter.next();
                Long errorFrameNumber = (Long) pair.getKey();
                int requestType = ((Integer) pair.getValue()).intValue();
                Boolean removeError = Boolean.valueOf(null);
                long longValue = errorFrameNumber.longValue();
                long[] jArr = this.mCompletedFrameNumber;
                if (longValue != jArr[requestType] + 1) {
                    if (this.mSkippedFrameNumbers[requestType].isEmpty()) {
                        for (int i = 1; i < 3; i++) {
                            int otherType = (requestType + i) % 3;
                            if (!this.mSkippedOtherFrameNumbers[otherType].isEmpty() && errorFrameNumber == this.mSkippedOtherFrameNumbers[otherType].element()) {
                                this.mCompletedFrameNumber[requestType] = errorFrameNumber.longValue();
                                this.mSkippedOtherFrameNumbers[otherType].remove();
                                removeError = Boolean.valueOf(true);
                                break;
                            }
                        }
                    } else if (errorFrameNumber == this.mSkippedFrameNumbers[requestType].element()) {
                        this.mCompletedFrameNumber[requestType] = errorFrameNumber.longValue();
                        this.mSkippedFrameNumbers[requestType].remove();
                        removeError = Boolean.valueOf(true);
                    }
                } else {
                    jArr[requestType] = errorFrameNumber.longValue();
                    removeError = Boolean.valueOf(true);
                }
                if (removeError.booleanValue()) {
                    iter.remove();
                }
            }
        }

        public void updateTracker(long frameNumber, boolean isError, int requestType) {
            if (isError) {
                this.mFutureErrorMap.put(Long.valueOf(frameNumber), Integer.valueOf(requestType));
            } else {
                try {
                    updateCompletedFrameNumber(frameNumber, requestType);
                } catch (IllegalArgumentException e) {
                    Log.e(CameraDeviceImpl.this.TAG, e.getMessage());
                }
            }
            update();
        }

        public void updateTracker(long frameNumber, CaptureResult result, boolean partial, int requestType) {
            if (!partial) {
                updateTracker(frameNumber, false, requestType);
            } else if (result != null) {
                List<CaptureResult> partials = (List) this.mPartialResults.get(Long.valueOf(frameNumber));
                if (partials == null) {
                    partials = new ArrayList();
                    this.mPartialResults.put(Long.valueOf(frameNumber), partials);
                }
                partials.add(result);
            }
        }

        public List<CaptureResult> popPartialResults(long frameNumber) {
            return (List) this.mPartialResults.remove(Long.valueOf(frameNumber));
        }

        public long getCompletedFrameNumber() {
            return this.mCompletedFrameNumber[0];
        }

        public long getCompletedReprocessFrameNumber() {
            return this.mCompletedFrameNumber[1];
        }

        public long getCompletedZslStillFrameNumber() {
            return this.mCompletedFrameNumber[2];
        }

        private void updateCompletedFrameNumber(long frameNumber, int requestType) throws IllegalArgumentException {
            long j = frameNumber;
            long[] jArr = this.mCompletedFrameNumber;
            String str = " is a repeat";
            String str2 = "frame number ";
            if (j > jArr[requestType]) {
                int otherType1 = (requestType + 1) % 3;
                int otherType2 = (requestType + 2) % 3;
                long maxOtherFrameNumberSeen = Math.max(jArr[otherType1], jArr[otherType2]);
                if (j >= maxOtherFrameNumberSeen) {
                    long i = Math.max(maxOtherFrameNumberSeen, this.mCompletedFrameNumber[requestType]);
                    while (true) {
                        i++;
                        if (i >= j) {
                            break;
                        }
                        this.mSkippedOtherFrameNumbers[requestType].add(Long.valueOf(i));
                    }
                } else if (this.mSkippedFrameNumbers[requestType].isEmpty()) {
                    int index1 = this.mSkippedOtherFrameNumbers[otherType1].indexOf(Long.valueOf(frameNumber));
                    int index2 = this.mSkippedOtherFrameNumbers[otherType2].indexOf(Long.valueOf(frameNumber));
                    boolean z = true;
                    boolean inSkippedOther1 = index1 != -1;
                    if (index2 == -1) {
                        z = false;
                    }
                    if ((inSkippedOther1 ^ z) != 0) {
                        LinkedList<Long> srcList;
                        LinkedList<Long> dstList;
                        int index;
                        if (inSkippedOther1) {
                            srcList = this.mSkippedOtherFrameNumbers[otherType1];
                            dstList = this.mSkippedFrameNumbers[otherType2];
                            index = index1;
                        } else {
                            srcList = this.mSkippedOtherFrameNumbers[otherType2];
                            dstList = this.mSkippedFrameNumbers[otherType1];
                            index = index2;
                        }
                        for (int i2 = 0; i2 < index; i2++) {
                            dstList.add((Long) srcList.removeFirst());
                        }
                        srcList.remove();
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(str2);
                        stringBuilder.append(j);
                        stringBuilder.append(" is a repeat or invalid");
                        throw new IllegalArgumentException(stringBuilder.toString());
                    }
                } else if (j < ((Long) this.mSkippedFrameNumbers[requestType].element()).longValue()) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str2);
                    stringBuilder2.append(j);
                    stringBuilder2.append(str);
                    throw new IllegalArgumentException(stringBuilder2.toString());
                } else if (j <= ((Long) this.mSkippedFrameNumbers[requestType].element()).longValue()) {
                    this.mSkippedFrameNumbers[requestType].remove();
                } else {
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append(str2);
                    stringBuilder3.append(j);
                    stringBuilder3.append(" comes out of order. Expecting ");
                    stringBuilder3.append(this.mSkippedFrameNumbers[requestType].element());
                    throw new IllegalArgumentException(stringBuilder3.toString());
                }
                this.mCompletedFrameNumber[requestType] = j;
                return;
            }
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(str2);
            stringBuilder4.append(j);
            stringBuilder4.append(str);
            throw new IllegalArgumentException(stringBuilder4.toString());
        }
    }

    static class RequestLastFrameNumbersHolder {
        private final long mLastRegularFrameNumber;
        private final long mLastReprocessFrameNumber;
        private final long mLastZslStillFrameNumber;
        private final int mRequestId;

        public RequestLastFrameNumbersHolder(List<CaptureRequest> requestList, SubmitInfo requestInfo) {
            long lastRegularFrameNumber = -1;
            long lastReprocessFrameNumber = -1;
            long lastZslStillFrameNumber = -1;
            long frameNumber = requestInfo.getLastFrameNumber();
            int i = 1;
            List<CaptureRequest> list;
            if (requestInfo.getLastFrameNumber() >= ((long) (requestList.size() - 1))) {
                int i2 = requestList.size() - 1;
                while (i2 >= 0) {
                    int requestType = ((CaptureRequest) requestList.get(i2)).getRequestType();
                    if (requestType == i && lastReprocessFrameNumber == -1) {
                        lastReprocessFrameNumber = frameNumber;
                    } else if (requestType == 2 && lastZslStillFrameNumber == -1) {
                        lastZslStillFrameNumber = frameNumber;
                    } else if (requestType == 0 && lastRegularFrameNumber == -1) {
                        lastRegularFrameNumber = frameNumber;
                    }
                    if (lastReprocessFrameNumber != -1 && lastZslStillFrameNumber != -1 && lastRegularFrameNumber != -1) {
                        break;
                    }
                    frameNumber--;
                    i2--;
                    i = 1;
                }
                list = requestList;
                this.mLastRegularFrameNumber = lastRegularFrameNumber;
                this.mLastReprocessFrameNumber = lastReprocessFrameNumber;
                this.mLastZslStillFrameNumber = lastZslStillFrameNumber;
                this.mRequestId = requestInfo.getRequestId();
                return;
            }
            list = requestList;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("lastFrameNumber: ");
            stringBuilder.append(requestInfo.getLastFrameNumber());
            stringBuilder.append(" should be at least ");
            stringBuilder.append(requestList.size() - 1);
            stringBuilder.append(" for the number of  requests in the list: ");
            stringBuilder.append(requestList.size());
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        RequestLastFrameNumbersHolder(int requestId, long lastFrameNumber, int[] repeatingRequestTypes) {
            long lastRegularFrameNumber = -1;
            long lastZslStillFrameNumber = -1;
            if (repeatingRequestTypes == null) {
                throw new IllegalArgumentException("repeatingRequest list must not be null");
            } else if (lastFrameNumber >= ((long) (repeatingRequestTypes.length - 1))) {
                long frameNumber = lastFrameNumber;
                int i = repeatingRequestTypes.length;
                while (true) {
                    i--;
                    if (i >= 0) {
                        if (repeatingRequestTypes[i] == 2 && lastZslStillFrameNumber == -1) {
                            lastZslStillFrameNumber = frameNumber;
                        } else if (repeatingRequestTypes[i] == 0 && lastRegularFrameNumber == -1) {
                            lastRegularFrameNumber = frameNumber;
                        }
                        if (lastZslStillFrameNumber != -1 && lastRegularFrameNumber != -1) {
                            break;
                        }
                        frameNumber--;
                    } else {
                        break;
                    }
                }
                this.mLastRegularFrameNumber = lastRegularFrameNumber;
                this.mLastZslStillFrameNumber = lastZslStillFrameNumber;
                this.mLastReprocessFrameNumber = -1;
                this.mRequestId = requestId;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("lastFrameNumber: ");
                stringBuilder.append(lastFrameNumber);
                stringBuilder.append(" should be at least ");
                stringBuilder.append(repeatingRequestTypes.length - 1);
                stringBuilder.append(" for the number of requests in the list: ");
                stringBuilder.append(repeatingRequestTypes.length);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        public long getLastRegularFrameNumber() {
            return this.mLastRegularFrameNumber;
        }

        public long getLastReprocessFrameNumber() {
            return this.mLastReprocessFrameNumber;
        }

        public long getLastZslStillFrameNumber() {
            return this.mLastZslStillFrameNumber;
        }

        public long getLastFrameNumber() {
            return Math.max(this.mLastZslStillFrameNumber, Math.max(this.mLastRegularFrameNumber, this.mLastReprocessFrameNumber));
        }

        public int getRequestId() {
            return this.mRequestId;
        }
    }

    public CameraDeviceImpl(String cameraId, StateCallback callback, Executor executor, CameraCharacteristics characteristics, int appTargetSdkVersion) {
        if (cameraId == null || callback == null || executor == null || characteristics == null) {
            throw new IllegalArgumentException("Null argument given");
        }
        this.mCameraId = cameraId;
        this.mDeviceCallback = callback;
        this.mDeviceExecutor = executor;
        this.mCharacteristics = characteristics;
        this.mAppTargetSdkVersion = appTargetSdkVersion;
        String tag = String.format("CameraDevice-JV-%s", new Object[]{this.mCameraId});
        if (tag.length() > 23) {
            tag = tag.substring(0, 23);
        }
        this.TAG = tag;
        Integer partialCount = (Integer) this.mCharacteristics.get(CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT);
        if (partialCount == null) {
            this.mTotalPartialCount = 1;
        } else {
            this.mTotalPartialCount = partialCount.intValue();
        }
        this.mIsPrivilegedApp = checkPrivilegedAppList();
    }

    public CameraDeviceCallbacks getCallbacks() {
        return this.mCallbacks;
    }

    public void setRemoteDevice(ICameraDeviceUser remoteDevice) throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            if (this.mInError) {
                return;
            }
            this.mRemoteDevice = new ICameraDeviceUserWrapper(remoteDevice);
            IBinder remoteDeviceBinder = remoteDevice.asBinder();
            if (remoteDeviceBinder != null) {
                try {
                    remoteDeviceBinder.linkToDeath(this, 0);
                } catch (RemoteException e) {
                    this.mDeviceExecutor.execute(this.mCallOnDisconnected);
                    throw new CameraAccessException(2, "The camera device has encountered a serious error");
                }
            }
            this.mDeviceExecutor.execute(this.mCallOnOpened);
            this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
        }
    }

    public void setRemoteFailure(ServiceSpecificException failure) {
        int failureCode = 4;
        boolean failureIsError = true;
        int i = failure.errorCode;
        if (i == 4) {
            failureIsError = false;
        } else if (i == 10) {
            failureCode = 4;
        } else if (i == 6) {
            failureCode = 3;
        } else if (i == 7) {
            failureCode = 1;
        } else if (i != 8) {
            String str = this.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected failure in opening camera device: ");
            stringBuilder.append(failure.errorCode);
            stringBuilder.append(failure.getMessage());
            Log.e(str, stringBuilder.toString());
        } else {
            failureCode = 2;
        }
        i = failureCode;
        final boolean isError = failureIsError;
        synchronized (this.mInterfaceLock) {
            this.mInError = true;
            this.mDeviceExecutor.execute(new Runnable() {
                public void run() {
                    if (isError) {
                        CameraDeviceImpl.this.mDeviceCallback.onError(CameraDeviceImpl.this, i);
                    } else {
                        CameraDeviceImpl.this.mDeviceCallback.onDisconnected(CameraDeviceImpl.this);
                    }
                }
            });
        }
    }

    public void setVendorStreamConfigMode(int fpsrange) {
        this.customOpMode = fpsrange;
    }

    public String getId() {
        return this.mCameraId;
    }

    public void configureOutputs(List<Surface> outputs) throws CameraAccessException {
        ArrayList<OutputConfiguration> outputConfigs = new ArrayList(outputs.size());
        for (Surface s : outputs) {
            outputConfigs.add(new OutputConfiguration(s));
        }
        configureStreamsChecked(null, outputConfigs, 0, null);
    }

    public boolean configureStreamsChecked(InputConfiguration inputConfig, List<OutputConfiguration> outputs, int operatingMode, CaptureRequest sessionParams) throws CameraAccessException {
        if (outputs == null) {
            outputs = new ArrayList();
        }
        if (outputs.size() != 0 || inputConfig == null) {
            checkInputConfiguration(inputConfig);
            synchronized (this.mInterfaceLock) {
                checkIfCameraClosedOrInError();
                HashSet<OutputConfiguration> addSet = new HashSet(outputs);
                List<Integer> deleteList = new ArrayList();
                for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                    int streamId = this.mConfiguredOutputs.keyAt(i);
                    OutputConfiguration outConfig = (OutputConfiguration) this.mConfiguredOutputs.valueAt(i);
                    if (outputs.contains(outConfig)) {
                        if (!outConfig.isDeferredConfiguration()) {
                            addSet.remove(outConfig);
                        }
                    }
                    deleteList.add(Integer.valueOf(streamId));
                }
                this.mDeviceExecutor.execute(this.mCallOnBusy);
                stopRepeating();
                try {
                    waitUntilIdle();
                    this.mRemoteDevice.beginConfigure();
                    InputConfiguration currentInputConfig = (InputConfiguration) this.mConfiguredInput.getValue();
                    if (inputConfig != currentInputConfig && (inputConfig == null || !inputConfig.equals(currentInputConfig))) {
                        if (currentInputConfig != null) {
                            this.mRemoteDevice.deleteStream(((Integer) this.mConfiguredInput.getKey()).intValue());
                            this.mConfiguredInput = new SimpleEntry(Integer.valueOf(-1), null);
                        }
                        if (inputConfig != null) {
                            this.mConfiguredInput = new SimpleEntry(Integer.valueOf(this.mRemoteDevice.createInputStream(inputConfig.getWidth(), inputConfig.getHeight(), inputConfig.getFormat())), inputConfig);
                        }
                    }
                    for (Integer streamId2 : deleteList) {
                        this.mRemoteDevice.deleteStream(streamId2.intValue());
                        this.mConfiguredOutputs.delete(streamId2.intValue());
                    }
                    for (OutputConfiguration outConfig2 : outputs) {
                        if (addSet.contains(outConfig2)) {
                            this.mConfiguredOutputs.put(this.mRemoteDevice.createStream(outConfig2), outConfig2);
                        }
                    }
                    operatingMode |= this.customOpMode << 16;
                    if (sessionParams != null) {
                        this.mRemoteDevice.endConfigure(operatingMode, sessionParams.getNativeCopy());
                    } else {
                        this.mRemoteDevice.endConfigure(operatingMode, null);
                    }
                    if (1 != null) {
                        if (outputs.size() > 0) {
                            this.mDeviceExecutor.execute(this.mCallOnIdle);
                        }
                    }
                    this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
                } catch (IllegalArgumentException e) {
                    String str = this.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Stream configuration failed due to: ");
                    stringBuilder.append(e.getMessage());
                    Log.w(str, stringBuilder.toString());
                    if (null != null) {
                        if (outputs.size() > 0) {
                            this.mDeviceExecutor.execute(this.mCallOnIdle);
                            return false;
                        }
                    }
                    this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
                    return false;
                } catch (CameraAccessException e2) {
                    if (e2.getReason() == 4) {
                        throw new IllegalStateException("The camera is currently busy. You must wait until the previous operation completes.", e2);
                    }
                    throw e2;
                } catch (Throwable th) {
                    if (null == null || outputs.size() <= 0) {
                        this.mDeviceExecutor.execute(this.mCallOnUnconfigured);
                    } else {
                        this.mDeviceExecutor.execute(this.mCallOnIdle);
                    }
                }
            }
            return true;
        }
        throw new IllegalArgumentException("cannot configure an input stream without any output streams");
    }

    public void createCaptureSession(List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        List<OutputConfiguration> outConfigurations = new ArrayList(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(null, outConfigurations, callback, checkAndWrapHandler(handler), 0, null);
    }

    public void createCaptureSessionByOutputConfigurations(List<OutputConfiguration> outputConfigurations, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        createCaptureSessionInternal(null, new ArrayList(outputConfigurations), callback, checkAndWrapHandler(handler), 0, null);
    }

    public void createReprocessableCaptureSession(InputConfiguration inputConfig, List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (inputConfig != null) {
            List<OutputConfiguration> outConfigurations = new ArrayList(outputs.size());
            for (Surface surface : outputs) {
                outConfigurations.add(new OutputConfiguration(surface));
            }
            createCaptureSessionInternal(inputConfig, outConfigurations, callback, checkAndWrapHandler(handler), 0, null);
            return;
        }
        throw new IllegalArgumentException("inputConfig cannot be null when creating a reprocessable capture session");
    }

    public void createReprocessableCaptureSessionByConfigurations(InputConfiguration inputConfig, List<OutputConfiguration> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (inputConfig == null) {
            throw new IllegalArgumentException("inputConfig cannot be null when creating a reprocessable capture session");
        } else if (outputs != null) {
            List<OutputConfiguration> currentOutputs = new ArrayList();
            for (OutputConfiguration output : outputs) {
                currentOutputs.add(new OutputConfiguration(output));
            }
            createCaptureSessionInternal(inputConfig, currentOutputs, callback, checkAndWrapHandler(handler), 0, null);
        } else {
            throw new IllegalArgumentException("Output configurations cannot be null when creating a reprocessable capture session");
        }
    }

    public void createConstrainedHighSpeedCaptureSession(List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        if (outputs == null || outputs.size() == 0 || outputs.size() > 2) {
            throw new IllegalArgumentException("Output surface list must not be null and the size must be no more than 2");
        }
        List<OutputConfiguration> outConfigurations = new ArrayList(outputs.size());
        for (Surface surface : outputs) {
            outConfigurations.add(new OutputConfiguration(surface));
        }
        createCaptureSessionInternal(null, outConfigurations, callback, checkAndWrapHandler(handler), 1, null);
    }

    public void createCustomCaptureSession(InputConfiguration inputConfig, List<OutputConfiguration> outputs, int operatingMode, CameraCaptureSession.StateCallback callback, Handler handler) throws CameraAccessException {
        List<OutputConfiguration> currentOutputs = new ArrayList();
        for (OutputConfiguration output : outputs) {
            currentOutputs.add(new OutputConfiguration(output));
        }
        createCaptureSessionInternal(inputConfig, currentOutputs, callback, checkAndWrapHandler(handler), operatingMode, null);
    }

    public void createCaptureSession(SessionConfiguration config) throws CameraAccessException {
        if (config != null) {
            List<OutputConfiguration> outputConfigs = config.getOutputConfigurations();
            if (outputConfigs == null) {
                throw new IllegalArgumentException("Invalid output configurations");
            } else if (config.getExecutor() != null) {
                createCaptureSessionInternal(config.getInputConfiguration(), outputConfigs, config.getStateCallback(), config.getExecutor(), config.getSessionType(), config.getSessionParameters());
                return;
            } else {
                throw new IllegalArgumentException("Invalid executor");
            }
        }
        throw new IllegalArgumentException("Invalid session configuration");
    }

    private void createCaptureSessionInternal(InputConfiguration inputConfig, List<OutputConfiguration> outputConfigurations, CameraCaptureSession.StateCallback callback, Executor executor, int operatingMode, CaptureRequest sessionParams) throws CameraAccessException {
        Throwable th;
        InputConfiguration inputConfiguration = inputConfig;
        int i = operatingMode;
        synchronized (this.mInterfaceLock) {
            try {
                boolean isConstrainedHighSpeed;
                boolean configureSuccess;
                CameraAccessException pendingException;
                Surface input;
                CameraCaptureSessionCore newSession;
                checkIfCameraClosedOrInError();
                boolean isConstrainedHighSpeed2 = i == 1;
                if (isConstrainedHighSpeed2 || i != 32888) {
                    isConstrainedHighSpeed = isConstrainedHighSpeed2;
                } else {
                    String str = this.TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("custom high speed operating mode ");
                    stringBuilder.append(Integer.toHexString(operatingMode));
                    Log.w(str, stringBuilder.toString());
                    isConstrainedHighSpeed = true;
                }
                if (isConstrainedHighSpeed) {
                    if (inputConfiguration != null) {
                        throw new IllegalArgumentException("Constrained high speed session doesn't support input configuration yet.");
                    }
                }
                if (this.mCurrentSession != null) {
                    this.mCurrentSession.replaceSessionClose();
                }
                Surface input2 = null;
                try {
                    isConstrainedHighSpeed2 = configureStreamsChecked(inputConfiguration, outputConfigurations, i, sessionParams);
                    if (isConstrainedHighSpeed2 && inputConfiguration != null) {
                        input2 = this.mRemoteDevice.getInputSurface();
                    }
                    configureSuccess = isConstrainedHighSpeed2;
                    pendingException = null;
                    input = input2;
                } catch (CameraAccessException e) {
                    configureSuccess = false;
                    pendingException = e;
                    input = null;
                }
                int i2;
                if (isConstrainedHighSpeed) {
                    ArrayList<Surface> surfaces = new ArrayList(outputConfigurations.size());
                    for (OutputConfiguration outConfig : outputConfigurations) {
                        surfaces.add(outConfig.getSurface());
                    }
                    StreamConfigurationMap config = (StreamConfigurationMap) getCharacteristics().get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    SurfaceUtils.checkConstrainedHighSpeedSurfaces(surfaces, null, config);
                    i2 = this.mNextSessionId;
                    this.mNextSessionId = i2 + 1;
                    newSession = new CameraConstrainedHighSpeedCaptureSessionImpl(i2, callback, executor, this, this.mDeviceExecutor, configureSuccess, this.mCharacteristics);
                } else {
                    i2 = this.mNextSessionId;
                    this.mNextSessionId = i2 + 1;
                    newSession = new CameraCaptureSessionImpl(i2, input, callback, executor, this, this.mDeviceExecutor, configureSuccess);
                }
                this.mCurrentSession = newSession;
                if (pendingException == null) {
                    this.mSessionStateCallback = this.mCurrentSession.getDeviceStateCallback();
                    return;
                }
                throw pendingException;
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        }
    }

    public boolean isSessionConfigurationSupported(SessionConfiguration sessionConfig) throws CameraAccessException, UnsupportedOperationException, IllegalArgumentException {
        boolean isSessionConfigurationSupported;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            isSessionConfigurationSupported = this.mRemoteDevice.isSessionConfigurationSupported(sessionConfig);
        }
        return isSessionConfigurationSupported;
    }

    public void setSessionListener(StateCallbackKK sessionCallback) {
        synchronized (this.mInterfaceLock) {
            this.mSessionStateCallback = sessionCallback;
        }
    }

    private void overrideEnableZsl(CameraMetadataNative request, boolean newValue) {
        if (((Boolean) request.get(CaptureRequest.CONTROL_ENABLE_ZSL)) != null) {
            request.set(CaptureRequest.CONTROL_ENABLE_ZSL, Boolean.valueOf(newValue));
        }
    }

    public Builder createCaptureRequest(int templateType, Set<String> physicalCameraIdSet) throws CameraAccessException {
        Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            for (String physicalId : physicalCameraIdSet) {
                if (physicalId == getId()) {
                    throw new IllegalStateException("Physical id matches the logical id!");
                }
            }
            CameraMetadataNative templatedRequest = this.mRemoteDevice.createDefaultRequest(templateType);
            if (this.mAppTargetSdkVersion < 26 || templateType != 2) {
                overrideEnableZsl(templatedRequest, false);
            }
            builder = new Builder(templatedRequest, false, -1, getId(), physicalCameraIdSet);
        }
        return builder;
    }

    public Builder createCaptureRequest(int templateType) throws CameraAccessException {
        Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            CameraMetadataNative templatedRequest = this.mRemoteDevice.createDefaultRequest(templateType);
            if (this.mAppTargetSdkVersion < 26 || templateType != 2) {
                overrideEnableZsl(templatedRequest, false);
            }
            builder = new Builder(templatedRequest, false, -1, getId(), null);
        }
        return builder;
    }

    public Builder createReprocessCaptureRequest(TotalCaptureResult inputResult) throws CameraAccessException {
        Builder builder;
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            builder = new Builder(new CameraMetadataNative(inputResult.getNativeCopy()), true, inputResult.getSessionId(), getId(), null);
        }
        return builder;
    }

    public void prepare(Surface surface) throws CameraAccessException {
        if (surface != null) {
            synchronized (this.mInterfaceLock) {
                int streamId = -1;
                for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                    if (((OutputConfiguration) this.mConfiguredOutputs.valueAt(i)).getSurfaces().contains(surface)) {
                        streamId = this.mConfiguredOutputs.keyAt(i);
                        break;
                    }
                }
                if (streamId != -1) {
                    this.mRemoteDevice.prepare(streamId);
                } else {
                    throw new IllegalArgumentException("Surface is not part of this session");
                }
            }
            return;
        }
        throw new IllegalArgumentException("Surface is null");
    }

    public void prepare(int maxCount, Surface surface) throws CameraAccessException {
        if (surface == null) {
            throw new IllegalArgumentException("Surface is null");
        } else if (maxCount > 0) {
            synchronized (this.mInterfaceLock) {
                int streamId = -1;
                for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                    if (surface == ((OutputConfiguration) this.mConfiguredOutputs.valueAt(i)).getSurface()) {
                        streamId = this.mConfiguredOutputs.keyAt(i);
                        break;
                    }
                }
                if (streamId != -1) {
                    this.mRemoteDevice.prepare2(maxCount, streamId);
                } else {
                    throw new IllegalArgumentException("Surface is not part of this session");
                }
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid maxCount given: ");
            stringBuilder.append(maxCount);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    public void updateOutputConfiguration(OutputConfiguration config) throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            int streamId = -1;
            for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                if (config.getSurface() == ((OutputConfiguration) this.mConfiguredOutputs.valueAt(i)).getSurface()) {
                    streamId = this.mConfiguredOutputs.keyAt(i);
                    break;
                }
            }
            if (streamId != -1) {
                this.mRemoteDevice.updateOutputConfiguration(streamId, config);
                this.mConfiguredOutputs.put(streamId, config);
            } else {
                throw new IllegalArgumentException("Invalid output configuration");
            }
        }
    }

    public void tearDown(Surface surface) throws CameraAccessException {
        if (surface != null) {
            synchronized (this.mInterfaceLock) {
                int streamId = -1;
                for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                    if (surface == ((OutputConfiguration) this.mConfiguredOutputs.valueAt(i)).getSurface()) {
                        streamId = this.mConfiguredOutputs.keyAt(i);
                        break;
                    }
                }
                if (streamId != -1) {
                    this.mRemoteDevice.tearDown(streamId);
                } else {
                    throw new IllegalArgumentException("Surface is not part of this session");
                }
            }
            return;
        }
        throw new IllegalArgumentException("Surface is null");
    }

    public void finalizeOutputConfigs(List<OutputConfiguration> outputConfigs) throws CameraAccessException {
        if (outputConfigs == null || outputConfigs.size() == 0) {
            throw new IllegalArgumentException("deferred config is null or empty");
        }
        synchronized (this.mInterfaceLock) {
            for (OutputConfiguration config : outputConfigs) {
                int streamId = -1;
                for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                    if (config.equals(this.mConfiguredOutputs.valueAt(i))) {
                        streamId = this.mConfiguredOutputs.keyAt(i);
                        break;
                    }
                }
                if (streamId == -1) {
                    throw new IllegalArgumentException("Deferred config is not part of this session");
                } else if (config.getSurfaces().size() != 0) {
                    this.mRemoteDevice.finalizeOutputConfigurations(streamId, config);
                    this.mConfiguredOutputs.put(streamId, config);
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("The final config for stream ");
                    stringBuilder.append(streamId);
                    stringBuilder.append(" must have at least 1 surface");
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
        }
    }

    public int capture(CaptureRequest request, CaptureCallback callback, Executor executor) throws CameraAccessException {
        List<CaptureRequest> requestList = new ArrayList();
        requestList.add(request);
        return submitCaptureRequest(requestList, callback, executor, false);
    }

    public int captureBurst(List<CaptureRequest> requests, CaptureCallback callback, Executor executor) throws CameraAccessException {
        if (requests != null && !requests.isEmpty()) {
            return submitCaptureRequest(requests, callback, executor, false);
        }
        throw new IllegalArgumentException("At least one request must be given");
    }

    private void checkEarlyTriggerSequenceComplete(final int requestId, long lastFrameNumber, int[] repeatingRequestTypes) {
        if (lastFrameNumber == -1) {
            int index = this.mCaptureCallbackMap.indexOfKey(requestId);
            final CaptureCallbackHolder holder = index >= 0 ? (CaptureCallbackHolder) this.mCaptureCallbackMap.valueAt(index) : null;
            if (holder != null) {
                this.mCaptureCallbackMap.removeAt(index);
            }
            if (holder != null) {
                Runnable resultDispatch = new Runnable() {
                    public void run() {
                        if (!CameraDeviceImpl.this.isClosed()) {
                            holder.getCallback().onCaptureSequenceAborted(CameraDeviceImpl.this, requestId);
                        }
                    }
                };
                long ident = Binder.clearCallingIdentity();
                try {
                    holder.getExecutor().execute(resultDispatch);
                    return;
                } finally {
                    Binder.restoreCallingIdentity(ident);
                }
            } else {
                Log.w(this.TAG, String.format("did not register callback to request %d", new Object[]{Integer.valueOf(requestId)}));
                return;
            }
        }
        this.mRequestLastFrameNumbersList.add(new RequestLastFrameNumbersHolder(requestId, lastFrameNumber, repeatingRequestTypes));
        checkAndFireSequenceComplete();
    }

    private int[] getRequestTypes(CaptureRequest[] requestArray) {
        int[] requestTypes = new int[requestArray.length];
        for (int i = 0; i < requestArray.length; i++) {
            requestTypes[i] = requestArray[i].getRequestType();
        }
        return requestTypes;
    }

    private int submitCaptureRequest(List<CaptureRequest> requestList, CaptureCallback callback, Executor executor, boolean repeating) throws CameraAccessException {
        SubmitInfo requestInfo;
        List<CaptureRequest> list = requestList;
        CaptureCallback captureCallback = callback;
        boolean z = repeating;
        Executor executor2 = checkExecutor(executor, captureCallback);
        for (CaptureRequest request : requestList) {
            if (request.getTargets().isEmpty()) {
                throw new IllegalArgumentException("Each request must have at least one Surface target");
            }
            for (Surface surface : request.getTargets()) {
                if (surface != null) {
                    for (int i = 0; i < this.mConfiguredOutputs.size(); i++) {
                        OutputConfiguration configuration = (OutputConfiguration) this.mConfiguredOutputs.valueAt(i);
                        if (configuration.isForPhysicalCamera() && configuration.getSurfaces().contains(surface) && request.isReprocess()) {
                            throw new IllegalArgumentException("Reprocess request on physical stream is not allowed");
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Null Surface targets are not allowed");
                }
            }
        }
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (z) {
                stopRepeating();
            }
            CaptureRequest[] requestArray = (CaptureRequest[]) list.toArray(new CaptureRequest[requestList.size()]);
            for (CaptureRequest request2 : requestArray) {
                request2.convertSurfaceToStreamId(this.mConfiguredOutputs);
            }
            requestInfo = this.mRemoteDevice.submitRequestList(requestArray, z);
            for (CaptureRequest request22 : requestArray) {
                request22.recoverStreamIdToSurface();
            }
            if (captureCallback != null) {
                SparseArray sparseArray = this.mCaptureCallbackMap;
                CaptureCallbackHolder captureCallbackHolder = r2;
                int requestId = requestInfo.getRequestId();
                CaptureCallbackHolder captureCallbackHolder2 = new CaptureCallbackHolder(callback, requestList, executor2, repeating, this.mNextSessionId - 1);
                sparseArray.put(requestId, captureCallbackHolder);
            }
            if (z) {
                if (this.mRepeatingRequestId != -1) {
                    checkEarlyTriggerSequenceComplete(this.mRepeatingRequestId, requestInfo.getLastFrameNumber(), this.mRepeatingRequestTypes);
                }
                this.mRepeatingRequestId = requestInfo.getRequestId();
                this.mRepeatingRequestTypes = getRequestTypes(requestArray);
            } else {
                this.mRequestLastFrameNumbersList.add(new RequestLastFrameNumbersHolder(list, requestInfo));
            }
            if (this.mIdle) {
                this.mDeviceExecutor.execute(this.mCallOnActive);
            }
            this.mIdle = false;
        }
        return requestInfo.getRequestId();
    }

    public int setRepeatingRequest(CaptureRequest request, CaptureCallback callback, Executor executor) throws CameraAccessException {
        List<CaptureRequest> requestList = new ArrayList();
        requestList.add(request);
        return submitCaptureRequest(requestList, callback, executor, true);
    }

    public int setRepeatingBurst(List<CaptureRequest> requests, CaptureCallback callback, Executor executor) throws CameraAccessException {
        if (requests != null && !requests.isEmpty()) {
            return submitCaptureRequest(requests, callback, executor, true);
        }
        throw new IllegalArgumentException("At least one request must be given");
    }

    public void stopRepeating() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (this.mRepeatingRequestId != -1) {
                int requestId = this.mRepeatingRequestId;
                this.mRepeatingRequestId = -1;
                int[] requestTypes = this.mRepeatingRequestTypes;
                this.mRepeatingRequestTypes = null;
                try {
                    checkEarlyTriggerSequenceComplete(requestId, this.mRemoteDevice.cancelRequest(requestId), requestTypes);
                } catch (IllegalArgumentException e) {
                    return;
                }
            }
        }
    }

    private void waitUntilIdle() throws CameraAccessException {
        synchronized (this.mInterfaceLock) {
            checkIfCameraClosedOrInError();
            if (this.mRepeatingRequestId == -1) {
                this.mRemoteDevice.waitUntilIdle();
            } else {
                throw new IllegalStateException("Active repeating request ongoing");
            }
        }
    }

    /* JADX WARNING: Missing block: B:12:0x0032, code skipped:
            return;
     */
    public void flush() throws android.hardware.camera2.CameraAccessException {
        /*
        r6 = this;
        r0 = r6.mInterfaceLock;
        monitor-enter(r0);
        r6.checkIfCameraClosedOrInError();	 Catch:{ all -> 0x0033 }
        r1 = r6.mDeviceExecutor;	 Catch:{ all -> 0x0033 }
        r2 = r6.mCallOnBusy;	 Catch:{ all -> 0x0033 }
        r1.execute(r2);	 Catch:{ all -> 0x0033 }
        r1 = r6.mIdle;	 Catch:{ all -> 0x0033 }
        if (r1 == 0) goto L_0x001a;
    L_0x0011:
        r1 = r6.mDeviceExecutor;	 Catch:{ all -> 0x0033 }
        r2 = r6.mCallOnIdle;	 Catch:{ all -> 0x0033 }
        r1.execute(r2);	 Catch:{ all -> 0x0033 }
        monitor-exit(r0);	 Catch:{ all -> 0x0033 }
        return;
    L_0x001a:
        r1 = r6.mRemoteDevice;	 Catch:{ all -> 0x0033 }
        r1 = r1.flush();	 Catch:{ all -> 0x0033 }
        r3 = r6.mRepeatingRequestId;	 Catch:{ all -> 0x0033 }
        r4 = -1;
        if (r3 == r4) goto L_0x0031;
    L_0x0025:
        r3 = r6.mRepeatingRequestId;	 Catch:{ all -> 0x0033 }
        r5 = r6.mRepeatingRequestTypes;	 Catch:{ all -> 0x0033 }
        r6.checkEarlyTriggerSequenceComplete(r3, r1, r5);	 Catch:{ all -> 0x0033 }
        r6.mRepeatingRequestId = r4;	 Catch:{ all -> 0x0033 }
        r3 = 0;
        r6.mRepeatingRequestTypes = r3;	 Catch:{ all -> 0x0033 }
    L_0x0031:
        monitor-exit(r0);	 Catch:{ all -> 0x0033 }
        return;
    L_0x0033:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0033 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.flush():void");
    }

    public void close() {
        synchronized (this.mInterfaceLock) {
            if (this.mClosing.getAndSet(true)) {
                return;
            }
            if (this.mRemoteDevice != null) {
                this.mRemoteDevice.disconnect();
                this.mRemoteDevice.unlinkToDeath(this, 0);
            }
            if (this.mRemoteDevice != null || this.mInError) {
                this.mDeviceExecutor.execute(this.mCallOnClosed);
            }
            this.mRemoteDevice = null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            close();
        } finally {
            super.finalize();
        }
    }

    private boolean checkPrivilegedAppList() {
        String packageName = ActivityThread.currentOpPackageName();
        String packageList = SystemProperties.get("persist.vendor.camera.privapp.list");
        if (packageList.length() > 0) {
            StringSplitter<String> splitter = new SimpleStringSplitter(',');
            splitter.setString(packageList);
            for (String str : splitter) {
                if (packageName.equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isPrivilegedApp() {
        return this.mIsPrivilegedApp;
    }

    private void checkInputConfiguration(InputConfiguration inputConfig) {
        if (inputConfig != null) {
            StreamConfigurationMap configMap = (StreamConfigurationMap) this.mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            if (isPrivilegedApp()) {
                Log.w(this.TAG, "ignore input format/size check for white listed app");
                return;
            }
            int i = 0;
            boolean validFormat = false;
            for (int format : configMap.getInputFormats()) {
                if (format == inputConfig.getFormat()) {
                    validFormat = true;
                }
            }
            String str = " is not valid";
            if (validFormat || CameraDeviceImplInjector.isInputConfigurationFormatValid(this.mCharacteristics, inputConfig)) {
                boolean validSize = false;
                Size[] inputSizes = configMap.getInputSizes(inputConfig.getFormat());
                int length = inputSizes.length;
                while (i < length) {
                    Size s = inputSizes[i];
                    if (inputConfig.getWidth() == s.getWidth() && inputConfig.getHeight() == s.getHeight()) {
                        validSize = true;
                    }
                    i++;
                }
                if (!(validSize || CameraDeviceImplInjector.isInputConfigurationSizeValid(this.mCharacteristics, inputConfig))) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("input size ");
                    stringBuilder.append(inputConfig.getWidth());
                    stringBuilder.append("x");
                    stringBuilder.append(inputConfig.getHeight());
                    stringBuilder.append(str);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("input format ");
            stringBuilder2.append(inputConfig.getFormat());
            stringBuilder2.append(str);
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
    }

    /* JADX WARNING: Missing block: B:35:0x0080, code skipped:
            if (r14 == null) goto L_0x0084;
     */
    /* JADX WARNING: Missing block: B:36:0x0082, code skipped:
            if (r11 == false) goto L_0x0087;
     */
    /* JADX WARNING: Missing block: B:37:0x0084, code skipped:
            r9.remove();
     */
    /* JADX WARNING: Missing block: B:38:0x0087, code skipped:
            if (r11 == false) goto L_0x00a3;
     */
    /* JADX WARNING: Missing block: B:39:0x0089, code skipped:
            r2 = new android.hardware.camera2.impl.CameraDeviceImpl.AnonymousClass10(r1);
            r15 = android.os.Binder.clearCallingIdentity();
     */
    /* JADX WARNING: Missing block: B:41:?, code skipped:
            r14.getExecutor().execute(r2);
     */
    /* JADX WARNING: Missing block: B:43:0x009f, code skipped:
            android.os.Binder.restoreCallingIdentity(r15);
     */
    private void checkAndFireSequenceComplete() {
        /*
        r23 = this;
        r1 = r23;
        r0 = r1.mFrameNumberTracker;
        r2 = r0.getCompletedFrameNumber();
        r0 = r1.mFrameNumberTracker;
        r4 = r0.getCompletedReprocessFrameNumber();
        r0 = r1.mFrameNumberTracker;
        r6 = r0.getCompletedZslStillFrameNumber();
        r8 = 0;
        r0 = r1.mRequestLastFrameNumbersList;
        r9 = r0.iterator();
    L_0x001b:
        r0 = r9.hasNext();
        if (r0 == 0) goto L_0x00ae;
    L_0x0021:
        r0 = r9.next();
        r10 = r0;
        r10 = (android.hardware.camera2.impl.CameraDeviceImpl.RequestLastFrameNumbersHolder) r10;
        r11 = 0;
        r12 = r10.getRequestId();
        r13 = r1.mInterfaceLock;
        monitor-enter(r13);
        r0 = r1.mRemoteDevice;	 Catch:{ all -> 0x00a7 }
        if (r0 != 0) goto L_0x0042;
    L_0x0034:
        r0 = r1.TAG;	 Catch:{ all -> 0x003d }
        r14 = "Camera closed while checking sequences";
        android.util.Log.w(r0, r14);	 Catch:{ all -> 0x003d }
        monitor-exit(r13);	 Catch:{ all -> 0x003d }
        return;
    L_0x003d:
        r0 = move-exception;
        r21 = r2;
        goto L_0x00aa;
    L_0x0042:
        r0 = r1.mCaptureCallbackMap;	 Catch:{ all -> 0x00a7 }
        r0 = r0.indexOfKey(r12);	 Catch:{ all -> 0x00a7 }
        if (r0 < 0) goto L_0x0053;
    L_0x004a:
        r14 = r1.mCaptureCallbackMap;	 Catch:{ all -> 0x003d }
        r14 = r14.valueAt(r0);	 Catch:{ all -> 0x003d }
        r14 = (android.hardware.camera2.impl.CameraDeviceImpl.CaptureCallbackHolder) r14;	 Catch:{ all -> 0x003d }
        goto L_0x0054;
    L_0x0053:
        r14 = 0;
    L_0x0054:
        if (r14 == 0) goto L_0x007d;
        r15 = r10.getLastRegularFrameNumber();	 Catch:{ all -> 0x00a7 }
        r17 = r10.getLastReprocessFrameNumber();	 Catch:{ all -> 0x00a7 }
        r19 = r10.getLastZslStillFrameNumber();	 Catch:{ all -> 0x00a7 }
        r21 = (r15 > r2 ? 1 : (r15 == r2 ? 0 : -1));
        if (r21 > 0) goto L_0x007a;
    L_0x0069:
        r21 = (r17 > r4 ? 1 : (r17 == r4 ? 0 : -1));
        if (r21 > 0) goto L_0x007a;
    L_0x006d:
        r21 = (r19 > r6 ? 1 : (r19 == r6 ? 0 : -1));
        if (r21 > 0) goto L_0x007a;
    L_0x0071:
        r11 = 1;
        r21 = r2;
        r2 = r1.mCaptureCallbackMap;	 Catch:{ all -> 0x00ac }
        r2.removeAt(r0);	 Catch:{ all -> 0x00ac }
        goto L_0x007f;
    L_0x007a:
        r21 = r2;
        goto L_0x007f;
    L_0x007d:
        r21 = r2;
    L_0x007f:
        monitor-exit(r13);	 Catch:{ all -> 0x00ac }
        if (r14 == 0) goto L_0x0084;
    L_0x0082:
        if (r11 == 0) goto L_0x0087;
    L_0x0084:
        r9.remove();
    L_0x0087:
        if (r11 == 0) goto L_0x00a3;
    L_0x0089:
        r0 = new android.hardware.camera2.impl.CameraDeviceImpl$10;
        r0.<init>(r12, r14, r10);
        r2 = r0;
        r15 = android.os.Binder.clearCallingIdentity();
        r0 = r14.getExecutor();	 Catch:{ all -> 0x009e }
        r0.execute(r2);	 Catch:{ all -> 0x009e }
        android.os.Binder.restoreCallingIdentity(r15);
        goto L_0x00a3;
    L_0x009e:
        r0 = move-exception;
        android.os.Binder.restoreCallingIdentity(r15);
        throw r0;
    L_0x00a3:
        r2 = r21;
        goto L_0x001b;
    L_0x00a7:
        r0 = move-exception;
        r21 = r2;
    L_0x00aa:
        monitor-exit(r13);	 Catch:{ all -> 0x00ac }
        throw r0;
    L_0x00ac:
        r0 = move-exception;
        goto L_0x00aa;
    L_0x00ae:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.impl.CameraDeviceImpl.checkAndFireSequenceComplete():void");
    }

    static Executor checkExecutor(Executor executor) {
        return executor == null ? checkAndWrapHandler(null) : executor;
    }

    public static <T> Executor checkExecutor(Executor executor, T callback) {
        return callback != null ? checkExecutor(executor) : executor;
    }

    public static Executor checkAndWrapHandler(Handler handler) {
        return new CameraHandlerExecutor(checkHandler(handler));
    }

    static Handler checkHandler(Handler handler) {
        if (handler != null) {
            return handler;
        }
        Looper looper = Looper.myLooper();
        if (looper != null) {
            return new Handler(looper);
        }
        throw new IllegalArgumentException("No handler given, and current thread has no looper!");
    }

    static <T> Handler checkHandler(Handler handler, T callback) {
        if (callback != null) {
            return checkHandler(handler);
        }
        return handler;
    }

    private void checkIfCameraClosedOrInError() throws CameraAccessException {
        if (this.mRemoteDevice == null) {
            throw new IllegalStateException("CameraDevice was already closed");
        } else if (this.mInError) {
            throw new CameraAccessException(3, "The camera device has encountered a serious error");
        }
    }

    private boolean isClosed() {
        return this.mClosing.get();
    }

    private CameraCharacteristics getCharacteristics() {
        return this.mCharacteristics;
    }

    public void binderDied() {
        String str = this.TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CameraDevice ");
        stringBuilder.append(this.mCameraId);
        stringBuilder.append(" died unexpectedly");
        Log.w(str, stringBuilder.toString());
        if (this.mRemoteDevice != null) {
            this.mInError = true;
            Runnable r = new Runnable() {
                public void run() {
                    if (!CameraDeviceImpl.this.isClosed()) {
                        CameraDeviceImpl.this.mDeviceCallback.onError(CameraDeviceImpl.this, 5);
                    }
                }
            };
            long ident = Binder.clearCallingIdentity();
            try {
                this.mDeviceExecutor.execute(r);
            } finally {
                Binder.restoreCallingIdentity(ident);
            }
        }
    }
}
