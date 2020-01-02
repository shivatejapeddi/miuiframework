package android.hardware.camera2.legacy;

import android.hardware.camera2.impl.CameraMetadataNative;
import android.os.Handler;
import android.util.Log;
import com.android.internal.telephony.IccCardConstants;

public class CameraDeviceState {
    private static final boolean DEBUG = false;
    public static final int NO_CAPTURE_ERROR = -1;
    private static final int STATE_CAPTURING = 4;
    private static final int STATE_CONFIGURING = 2;
    private static final int STATE_ERROR = 0;
    private static final int STATE_IDLE = 3;
    private static final int STATE_UNCONFIGURED = 1;
    private static final String TAG = "CameraDeviceState";
    private static final String[] sStateNames = new String[]{"ERROR", "UNCONFIGURED", "CONFIGURING", "IDLE", "CAPTURING"};
    private int mCurrentError = -1;
    private Handler mCurrentHandler = null;
    private CameraDeviceStateListener mCurrentListener = null;
    private RequestHolder mCurrentRequest = null;
    private int mCurrentState = 1;

    public interface CameraDeviceStateListener {
        void onBusy();

        void onCaptureResult(CameraMetadataNative cameraMetadataNative, RequestHolder requestHolder);

        void onCaptureStarted(RequestHolder requestHolder, long j);

        void onConfiguring();

        void onError(int i, Object obj, RequestHolder requestHolder);

        void onIdle();

        void onRepeatingRequestError(long j, int i);

        void onRequestQueueEmpty();
    }

    public synchronized void setError(int error) {
        this.mCurrentError = error;
        doStateTransition(0);
    }

    public synchronized boolean setConfiguring() {
        doStateTransition(2);
        return this.mCurrentError == -1;
    }

    public synchronized boolean setIdle() {
        doStateTransition(3);
        return this.mCurrentError == -1;
    }

    public synchronized boolean setCaptureStart(RequestHolder request, long timestamp, int captureError) {
        this.mCurrentRequest = request;
        doStateTransition(4, timestamp, captureError);
        return this.mCurrentError == -1;
    }

    /* JADX WARNING: Missing block: B:8:0x002d, code skipped:
            return r2;
     */
    /* JADX WARNING: Missing block: B:21:0x0054, code skipped:
            return r2;
     */
    public synchronized boolean setCaptureResult(final android.hardware.camera2.legacy.RequestHolder r7, final android.hardware.camera2.impl.CameraMetadataNative r8, final int r9, final java.lang.Object r10) {
        /*
        r6 = this;
        monitor-enter(r6);
        r0 = r6.mCurrentState;	 Catch:{ all -> 0x0055 }
        r1 = 4;
        r2 = 1;
        r3 = 0;
        r4 = -1;
        if (r0 == r1) goto L_0x002e;
    L_0x0009:
        r0 = "CameraDeviceState";
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0055 }
        r1.<init>();	 Catch:{ all -> 0x0055 }
        r5 = "Cannot receive result while in state: ";
        r1.append(r5);	 Catch:{ all -> 0x0055 }
        r5 = r6.mCurrentState;	 Catch:{ all -> 0x0055 }
        r1.append(r5);	 Catch:{ all -> 0x0055 }
        r1 = r1.toString();	 Catch:{ all -> 0x0055 }
        android.util.Log.e(r0, r1);	 Catch:{ all -> 0x0055 }
        r6.mCurrentError = r2;	 Catch:{ all -> 0x0055 }
        r6.doStateTransition(r3);	 Catch:{ all -> 0x0055 }
        r0 = r6.mCurrentError;	 Catch:{ all -> 0x0055 }
        if (r0 != r4) goto L_0x002b;
    L_0x002a:
        goto L_0x002c;
    L_0x002b:
        r2 = r3;
    L_0x002c:
        monitor-exit(r6);
        return r2;
    L_0x002e:
        r0 = r6.mCurrentHandler;	 Catch:{ all -> 0x0055 }
        if (r0 == 0) goto L_0x004d;
    L_0x0032:
        r0 = r6.mCurrentListener;	 Catch:{ all -> 0x0055 }
        if (r0 == 0) goto L_0x004d;
    L_0x0036:
        if (r9 == r4) goto L_0x0043;
    L_0x0038:
        r0 = r6.mCurrentHandler;	 Catch:{ all -> 0x0055 }
        r1 = new android.hardware.camera2.legacy.CameraDeviceState$1;	 Catch:{ all -> 0x0055 }
        r1.<init>(r9, r10, r7);	 Catch:{ all -> 0x0055 }
        r0.post(r1);	 Catch:{ all -> 0x0055 }
        goto L_0x004d;
    L_0x0043:
        r0 = r6.mCurrentHandler;	 Catch:{ all -> 0x0055 }
        r1 = new android.hardware.camera2.legacy.CameraDeviceState$2;	 Catch:{ all -> 0x0055 }
        r1.<init>(r8, r7);	 Catch:{ all -> 0x0055 }
        r0.post(r1);	 Catch:{ all -> 0x0055 }
    L_0x004d:
        r0 = r6.mCurrentError;	 Catch:{ all -> 0x0055 }
        if (r0 != r4) goto L_0x0052;
    L_0x0051:
        goto L_0x0053;
    L_0x0052:
        r2 = r3;
    L_0x0053:
        monitor-exit(r6);
        return r2;
    L_0x0055:
        r7 = move-exception;
        monitor-exit(r6);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.CameraDeviceState.setCaptureResult(android.hardware.camera2.legacy.RequestHolder, android.hardware.camera2.impl.CameraMetadataNative, int, java.lang.Object):boolean");
    }

    public synchronized boolean setCaptureResult(RequestHolder request, CameraMetadataNative result) {
        return setCaptureResult(request, result, -1, null);
    }

    public synchronized void setRepeatingRequestError(final long lastFrameNumber, final int repeatingRequestId) {
        this.mCurrentHandler.post(new Runnable() {
            public void run() {
                CameraDeviceState.this.mCurrentListener.onRepeatingRequestError(lastFrameNumber, repeatingRequestId);
            }
        });
    }

    public synchronized void setRequestQueueEmpty() {
        this.mCurrentHandler.post(new Runnable() {
            public void run() {
                CameraDeviceState.this.mCurrentListener.onRequestQueueEmpty();
            }
        });
    }

    public synchronized void setCameraDeviceCallbacks(Handler handler, CameraDeviceStateListener listener) {
        this.mCurrentHandler = handler;
        this.mCurrentListener = listener;
    }

    private void doStateTransition(int newState) {
        doStateTransition(newState, 0, -1);
    }

    private void doStateTransition(int newState, final long timestamp, final int error) {
        int i = this.mCurrentState;
        String str = TAG;
        if (newState != i) {
            String stateName = IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
            if (newState >= 0) {
                String[] strArr = sStateNames;
                if (newState < strArr.length) {
                    stateName = strArr[newState];
                }
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Legacy camera service transitioning to state ");
            stringBuilder.append(stateName);
            Log.i(str, stringBuilder.toString());
        }
        if (!(newState == 0 || newState == 3 || this.mCurrentState == newState)) {
            Handler handler = this.mCurrentHandler;
            if (!(handler == null || this.mCurrentListener == null)) {
                handler.post(new Runnable() {
                    public void run() {
                        CameraDeviceState.this.mCurrentListener.onBusy();
                    }
                });
            }
        }
        Handler handler2;
        StringBuilder stringBuilder2;
        if (newState == 0) {
            if (this.mCurrentState != 0) {
                handler2 = this.mCurrentHandler;
                if (!(handler2 == null || this.mCurrentListener == null)) {
                    handler2.post(new Runnable() {
                        public void run() {
                            CameraDeviceState.this.mCurrentListener.onError(CameraDeviceState.this.mCurrentError, null, CameraDeviceState.this.mCurrentRequest);
                        }
                    });
                }
            }
            this.mCurrentState = 0;
        } else if (newState == 2) {
            int i2 = this.mCurrentState;
            if (i2 == 1 || i2 == 3) {
                if (this.mCurrentState != 2) {
                    handler2 = this.mCurrentHandler;
                    if (!(handler2 == null || this.mCurrentListener == null)) {
                        handler2.post(new Runnable() {
                            public void run() {
                                CameraDeviceState.this.mCurrentListener.onConfiguring();
                            }
                        });
                    }
                }
                this.mCurrentState = 2;
                return;
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Cannot call configure while in state: ");
            stringBuilder2.append(this.mCurrentState);
            Log.e(str, stringBuilder2.toString());
            this.mCurrentError = 1;
            doStateTransition(0);
        } else if (newState == 3) {
            int i3 = this.mCurrentState;
            if (i3 != 3) {
                if (i3 == 2 || i3 == 4) {
                    if (this.mCurrentState != 3) {
                        Handler handler3 = this.mCurrentHandler;
                        if (!(handler3 == null || this.mCurrentListener == null)) {
                            handler3.post(new Runnable() {
                                public void run() {
                                    CameraDeviceState.this.mCurrentListener.onIdle();
                                }
                            });
                        }
                    }
                    this.mCurrentState = 3;
                    return;
                }
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Cannot call idle while in state: ");
                stringBuilder2.append(this.mCurrentState);
                Log.e(str, stringBuilder2.toString());
                this.mCurrentError = 1;
                doStateTransition(0);
            }
        } else if (newState == 4) {
            int i4 = this.mCurrentState;
            if (i4 == 3 || i4 == 4) {
                handler2 = this.mCurrentHandler;
                if (!(handler2 == null || this.mCurrentListener == null)) {
                    if (error != -1) {
                        handler2.post(new Runnable() {
                            public void run() {
                                CameraDeviceState.this.mCurrentListener.onError(error, null, CameraDeviceState.this.mCurrentRequest);
                            }
                        });
                    } else {
                        handler2.post(new Runnable() {
                            public void run() {
                                CameraDeviceState.this.mCurrentListener.onCaptureStarted(CameraDeviceState.this.mCurrentRequest, timestamp);
                            }
                        });
                    }
                }
                this.mCurrentState = 4;
                return;
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Cannot call capture while in state: ");
            stringBuilder2.append(this.mCurrentState);
            Log.e(str, stringBuilder2.toString());
            this.mCurrentError = 1;
            doStateTransition(0);
        } else {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Transition to unknown state: ");
            stringBuilder3.append(newState);
            throw new IllegalStateException(stringBuilder3.toString());
        }
    }
}
