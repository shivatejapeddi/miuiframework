package android.hardware.camera2.legacy;

import android.hardware.Camera;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.impl.CameraMetadataNative;
import com.android.internal.util.Preconditions;

public class LegacyFocusStateMapper {
    private static final boolean DEBUG = false;
    private static String TAG = "LegacyFocusStateMapper";
    private String mAfModePrevious = null;
    private int mAfRun = 0;
    private int mAfState = 0;
    private int mAfStatePrevious = 0;
    private final Camera mCamera;
    private final Object mLock = new Object();

    public LegacyFocusStateMapper(Camera camera) {
        this.mCamera = (Camera) Preconditions.checkNotNull(camera, "camera must not be null");
    }

    /* JADX WARNING: Missing block: B:69:0x00e4, code skipped:
            if (r2.equals("auto") != false) goto L_0x00f2;
     */
    public void processRequestTriggers(android.hardware.camera2.CaptureRequest r11, android.hardware.Camera.Parameters r12) {
        /*
        r10 = this;
        r0 = "captureRequest must not be null";
        com.android.internal.util.Preconditions.checkNotNull(r11, r0);
        r0 = android.hardware.camera2.CaptureRequest.CONTROL_AF_TRIGGER;
        r1 = 0;
        r2 = java.lang.Integer.valueOf(r1);
        r0 = android.hardware.camera2.utils.ParamsUtils.getOrDefault(r11, r0, r2);
        r0 = (java.lang.Integer) r0;
        r0 = r0.intValue();
        r2 = r12.getFocusMode();
        r3 = r10.mAfModePrevious;
        r3 = java.util.Objects.equals(r3, r2);
        r4 = 1;
        if (r3 != 0) goto L_0x0037;
    L_0x0023:
        r3 = r10.mLock;
        monitor-enter(r3);
        r5 = r10.mAfRun;	 Catch:{ all -> 0x0034 }
        r5 = r5 + r4;
        r10.mAfRun = r5;	 Catch:{ all -> 0x0034 }
        r10.mAfState = r1;	 Catch:{ all -> 0x0034 }
        monitor-exit(r3);	 Catch:{ all -> 0x0034 }
        r3 = r10.mCamera;
        r3.cancelAutoFocus();
        goto L_0x0037;
    L_0x0034:
        r1 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0034 }
        throw r1;
    L_0x0037:
        r10.mAfModePrevious = r2;
        r3 = r10.mLock;
        monitor-enter(r3);
        r5 = r10.mAfRun;	 Catch:{ all -> 0x011f }
        monitor-exit(r3);	 Catch:{ all -> 0x011f }
        r3 = new android.hardware.camera2.legacy.LegacyFocusStateMapper$1;
        r3.<init>(r5, r2);
        r6 = r2.hashCode();
        r7 = -1;
        r8 = 3;
        r9 = 2;
        switch(r6) {
            case -194628547: goto L_0x006d;
            case 3005871: goto L_0x0063;
            case 103652300: goto L_0x0059;
            case 910005312: goto L_0x004f;
            default: goto L_0x004e;
        };
    L_0x004e:
        goto L_0x0077;
    L_0x004f:
        r6 = "continuous-picture";
        r6 = r2.equals(r6);
        if (r6 == 0) goto L_0x004e;
    L_0x0057:
        r6 = r9;
        goto L_0x0078;
    L_0x0059:
        r6 = "macro";
        r6 = r2.equals(r6);
        if (r6 == 0) goto L_0x004e;
    L_0x0061:
        r6 = r4;
        goto L_0x0078;
    L_0x0063:
        r6 = "auto";
        r6 = r2.equals(r6);
        if (r6 == 0) goto L_0x004e;
    L_0x006b:
        r6 = r1;
        goto L_0x0078;
    L_0x006d:
        r6 = "continuous-video";
        r6 = r2.equals(r6);
        if (r6 == 0) goto L_0x004e;
    L_0x0075:
        r6 = r8;
        goto L_0x0078;
    L_0x0077:
        r6 = r7;
    L_0x0078:
        if (r6 == 0) goto L_0x0081;
    L_0x007a:
        if (r6 == r4) goto L_0x0081;
    L_0x007c:
        if (r6 == r9) goto L_0x0081;
    L_0x007e:
        if (r6 == r8) goto L_0x0081;
    L_0x0080:
        goto L_0x0086;
    L_0x0081:
        r6 = r10.mCamera;
        r6.setAutoFocusMoveCallback(r3);
    L_0x0086:
        if (r0 == 0) goto L_0x011d;
    L_0x0088:
        if (r0 == r4) goto L_0x00c2;
    L_0x008a:
        if (r0 == r9) goto L_0x00a5;
    L_0x008c:
        r1 = TAG;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "processRequestTriggers - ignoring unknown control.afTrigger = ";
        r3.append(r4);
        r3.append(r0);
        r3 = r3.toString();
        android.util.Log.w(r1, r3);
        goto L_0x011e;
    L_0x00a5:
        r3 = r10.mLock;
        monitor-enter(r3);
        r5 = r10.mLock;	 Catch:{ all -> 0x00bf }
        monitor-enter(r5);	 Catch:{ all -> 0x00bf }
        r6 = r10.mAfRun;	 Catch:{ all -> 0x00bc }
        r6 = r6 + r4;
        r10.mAfRun = r6;	 Catch:{ all -> 0x00bc }
        r4 = r6;
        r10.mAfState = r1;	 Catch:{ all -> 0x00bc }
        monitor-exit(r5);	 Catch:{ all -> 0x00bc }
        r1 = r10.mCamera;	 Catch:{ all -> 0x00bf }
        r1.cancelAutoFocus();	 Catch:{ all -> 0x00bf }
        monitor-exit(r3);	 Catch:{ all -> 0x00bf }
        goto L_0x011e;
    L_0x00bc:
        r1 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x00bc }
        throw r1;	 Catch:{ all -> 0x00bf }
    L_0x00bf:
        r1 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x00bf }
        throw r1;
    L_0x00c2:
        r3 = r2.hashCode();
        switch(r3) {
            case -194628547: goto L_0x00e7;
            case 3005871: goto L_0x00de;
            case 103652300: goto L_0x00d4;
            case 910005312: goto L_0x00ca;
            default: goto L_0x00c9;
        };
    L_0x00c9:
        goto L_0x00f1;
    L_0x00ca:
        r1 = "continuous-picture";
        r1 = r2.equals(r1);
        if (r1 == 0) goto L_0x00c9;
    L_0x00d2:
        r1 = r9;
        goto L_0x00f2;
    L_0x00d4:
        r1 = "macro";
        r1 = r2.equals(r1);
        if (r1 == 0) goto L_0x00c9;
    L_0x00dc:
        r1 = r4;
        goto L_0x00f2;
    L_0x00de:
        r3 = "auto";
        r3 = r2.equals(r3);
        if (r3 == 0) goto L_0x00c9;
    L_0x00e6:
        goto L_0x00f2;
    L_0x00e7:
        r1 = "continuous-video";
        r1 = r2.equals(r1);
        if (r1 == 0) goto L_0x00c9;
    L_0x00ef:
        r1 = r8;
        goto L_0x00f2;
    L_0x00f1:
        r1 = r7;
    L_0x00f2:
        if (r1 == 0) goto L_0x00fe;
    L_0x00f4:
        if (r1 == r4) goto L_0x00fe;
    L_0x00f6:
        if (r1 == r9) goto L_0x00fc;
    L_0x00f8:
        if (r1 == r8) goto L_0x00fc;
    L_0x00fa:
        r1 = 0;
        goto L_0x0100;
    L_0x00fc:
        r1 = 1;
        goto L_0x0100;
    L_0x00fe:
        r1 = 3;
    L_0x0100:
        r3 = r10.mLock;
        monitor-enter(r3);
        r5 = r10.mAfRun;	 Catch:{ all -> 0x011a }
        r5 = r5 + r4;
        r10.mAfRun = r5;	 Catch:{ all -> 0x011a }
        r4 = r5;
        r10.mAfState = r1;	 Catch:{ all -> 0x011a }
        monitor-exit(r3);	 Catch:{ all -> 0x011a }
        if (r1 != 0) goto L_0x010f;
    L_0x010e:
        goto L_0x011e;
    L_0x010f:
        r3 = r10.mCamera;
        r5 = new android.hardware.camera2.legacy.LegacyFocusStateMapper$2;
        r5.<init>(r4, r2);
        r3.autoFocus(r5);
        goto L_0x011e;
    L_0x011a:
        r4 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x011a }
        throw r4;
    L_0x011e:
        return;
    L_0x011f:
        r1 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x011f }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.LegacyFocusStateMapper.processRequestTriggers(android.hardware.camera2.CaptureRequest, android.hardware.Camera$Parameters):void");
    }

    public void mapResultTriggers(CameraMetadataNative result) {
        int newAfState;
        Preconditions.checkNotNull(result, "result must not be null");
        synchronized (this.mLock) {
            newAfState = this.mAfState;
        }
        result.set(CaptureResult.CONTROL_AF_STATE, Integer.valueOf(newAfState));
        this.mAfStatePrevious = newAfState;
    }

    private static String afStateToString(int afState) {
        switch (afState) {
            case 0:
                return "INACTIVE";
            case 1:
                return "PASSIVE_SCAN";
            case 2:
                return "PASSIVE_FOCUSED";
            case 3:
                return "ACTIVE_SCAN";
            case 4:
                return "FOCUSED_LOCKED";
            case 5:
                return "NOT_FOCUSED_LOCKED";
            case 6:
                return "PASSIVE_UNFOCUSED";
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UNKNOWN(");
                stringBuilder.append(afState);
                stringBuilder.append(")");
                return stringBuilder.toString();
        }
    }
}
