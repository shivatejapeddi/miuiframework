package android.hardware.radio;

import android.hardware.radio.ITunerCallback.Stub;
import android.hardware.radio.ProgramList.Chunk;
import android.hardware.radio.RadioManager.BandConfig;
import android.hardware.radio.RadioManager.ProgramInfo;
import android.hardware.radio.RadioTuner.Callback;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class TunerCallbackAdapter extends Stub {
    private static final String TAG = "BroadcastRadio.TunerCallbackAdapter";
    private final Callback mCallback;
    ProgramInfo mCurrentProgramInfo;
    private boolean mDelayedCompleteCallback = false;
    private final Handler mHandler;
    boolean mIsAntennaConnected = true;
    List<ProgramInfo> mLastCompleteList;
    private final Object mLock = new Object();
    ProgramList mProgramList;

    TunerCallbackAdapter(Callback callback, Handler handler) {
        this.mCallback = callback;
        if (handler == null) {
            this.mHandler = new Handler(Looper.getMainLooper());
        } else {
            this.mHandler = handler;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void close() {
        synchronized (this.mLock) {
            if (this.mProgramList != null) {
                this.mProgramList.close();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setProgramListObserver(ProgramList programList, OnCloseListener closeListener) {
        Objects.requireNonNull(closeListener);
        synchronized (this.mLock) {
            if (this.mProgramList != null) {
                Log.w(TAG, "Previous program list observer wasn't properly closed, closing it...");
                this.mProgramList.close();
            }
            this.mProgramList = programList;
            if (programList == null) {
                return;
            }
            programList.setOnCloseListener(new -$$Lambda$TunerCallbackAdapter$Hl80-0ppQ17uTjZuGamwBQMrO6Y(this, programList, closeListener));
            programList.addOnCompleteListener(new -$$Lambda$TunerCallbackAdapter$V-mJUy8dIlOVjsZ1ckkgn490jFI(this, programList));
        }
    }

    public /* synthetic */ void lambda$setProgramListObserver$0$TunerCallbackAdapter(ProgramList programList, OnCloseListener closeListener) {
        synchronized (this.mLock) {
            if (this.mProgramList != programList) {
                return;
            }
            this.mProgramList = null;
            this.mLastCompleteList = null;
            closeListener.onClose();
        }
    }

    /* JADX WARNING: Missing block: B:11:0x001e, code skipped:
            return;
     */
    public /* synthetic */ void lambda$setProgramListObserver$1$TunerCallbackAdapter(android.hardware.radio.ProgramList r4) {
        /*
        r3 = this;
        r0 = r3.mLock;
        monitor-enter(r0);
        r1 = r3.mProgramList;	 Catch:{ all -> 0x001f }
        if (r1 == r4) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x001f }
        return;
    L_0x0009:
        r1 = r4.toList();	 Catch:{ all -> 0x001f }
        r3.mLastCompleteList = r1;	 Catch:{ all -> 0x001f }
        r1 = r3.mDelayedCompleteCallback;	 Catch:{ all -> 0x001f }
        if (r1 == 0) goto L_0x001d;
    L_0x0013:
        r1 = "BroadcastRadio.TunerCallbackAdapter";
        r2 = "Sending delayed onBackgroundScanComplete callback";
        android.util.Log.d(r1, r2);	 Catch:{ all -> 0x001f }
        r3.sendBackgroundScanCompleteLocked();	 Catch:{ all -> 0x001f }
    L_0x001d:
        monitor-exit(r0);	 Catch:{ all -> 0x001f }
        return;
    L_0x001f:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001f }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.radio.TunerCallbackAdapter.lambda$setProgramListObserver$1$TunerCallbackAdapter(android.hardware.radio.ProgramList):void");
    }

    /* Access modifiers changed, original: 0000 */
    public List<ProgramInfo> getLastCompleteList() {
        List list;
        synchronized (this.mLock) {
            list = this.mLastCompleteList;
        }
        return list;
    }

    /* Access modifiers changed, original: 0000 */
    public void clearLastCompleteList() {
        synchronized (this.mLock) {
            this.mLastCompleteList = null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ProgramInfo getCurrentProgramInformation() {
        ProgramInfo programInfo;
        synchronized (this.mLock) {
            programInfo = this.mCurrentProgramInfo;
        }
        return programInfo;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isAntennaConnected() {
        return this.mIsAntennaConnected;
    }

    public /* synthetic */ void lambda$onError$2$TunerCallbackAdapter(int status) {
        this.mCallback.onError(status);
    }

    public void onError(int status) {
        this.mHandler.post(new -$$Lambda$TunerCallbackAdapter$jl29exheqPoYrltfLs9fLsjsI1A(this, status));
    }

    public /* synthetic */ void lambda$onTuneFailed$3$TunerCallbackAdapter(int status, ProgramSelector selector) {
        this.mCallback.onTuneFailed(status, selector);
    }

    /* JADX WARNING: Missing block: B:11:0x001f, code skipped:
            if (r4 != -1) goto L_0x003f;
     */
    public void onTuneFailed(int r4, android.hardware.radio.ProgramSelector r5) {
        /*
        r3 = this;
        r0 = r3.mHandler;
        r1 = new android.hardware.radio.-$$Lambda$TunerCallbackAdapter$Hj_P___HTEx_8p7qvYVPXmhwu7w;
        r1.<init>(r3, r4, r5);
        r0.post(r1);
        r0 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r4 == r0) goto L_0x0024;
    L_0x000e:
        r0 = -38;
        if (r4 == r0) goto L_0x0024;
    L_0x0012:
        r0 = -32;
        if (r4 == r0) goto L_0x0022;
    L_0x0016:
        r0 = -22;
        if (r4 == r0) goto L_0x0024;
    L_0x001a:
        r0 = -19;
        if (r4 == r0) goto L_0x0024;
    L_0x001e:
        r0 = -1;
        if (r4 == r0) goto L_0x0022;
    L_0x0021:
        goto L_0x003f;
    L_0x0022:
        r0 = 1;
        goto L_0x0040;
    L_0x0024:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Got an error with no mapping to the legacy API (";
        r0.append(r1);
        r0.append(r4);
        r1 = "), doing a best-effort conversion to ERROR_SCAN_TIMEOUT";
        r0.append(r1);
        r0 = r0.toString();
        r1 = "BroadcastRadio.TunerCallbackAdapter";
        android.util.Log.i(r1, r0);
    L_0x003f:
        r0 = 3;
    L_0x0040:
        r1 = r3.mHandler;
        r2 = new android.hardware.radio.-$$Lambda$TunerCallbackAdapter$HcS5_voI1xju970_jCP6Iz0LgPE;
        r2.<init>(r3, r0);
        r1.post(r2);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.radio.TunerCallbackAdapter.onTuneFailed(int, android.hardware.radio.ProgramSelector):void");
    }

    public /* synthetic */ void lambda$onTuneFailed$4$TunerCallbackAdapter(int errorCode) {
        this.mCallback.onError(errorCode);
    }

    public /* synthetic */ void lambda$onConfigurationChanged$5$TunerCallbackAdapter(BandConfig config) {
        this.mCallback.onConfigurationChanged(config);
    }

    public void onConfigurationChanged(BandConfig config) {
        this.mHandler.post(new -$$Lambda$TunerCallbackAdapter$B4BuskgdSatf-Xt5wzgLniEltQk(this, config));
    }

    public void onCurrentProgramInfoChanged(ProgramInfo info) {
        if (info == null) {
            Log.e(TAG, "ProgramInfo must not be null");
            return;
        }
        synchronized (this.mLock) {
            this.mCurrentProgramInfo = info;
        }
        this.mHandler.post(new -$$Lambda$TunerCallbackAdapter$RSNrzX5-O3nayC2_jg0kAR6KkKY(this, info));
    }

    public /* synthetic */ void lambda$onCurrentProgramInfoChanged$6$TunerCallbackAdapter(ProgramInfo info) {
        this.mCallback.onProgramInfoChanged(info);
        RadioMetadata metadata = info.getMetadata();
        if (metadata != null) {
            this.mCallback.onMetadataChanged(metadata);
        }
    }

    public /* synthetic */ void lambda$onTrafficAnnouncement$7$TunerCallbackAdapter(boolean active) {
        this.mCallback.onTrafficAnnouncement(active);
    }

    public void onTrafficAnnouncement(boolean active) {
        this.mHandler.post(new -$$Lambda$TunerCallbackAdapter$tiaoLZrR2K56rYeqHvSRh5lRdBI(this, active));
    }

    public /* synthetic */ void lambda$onEmergencyAnnouncement$8$TunerCallbackAdapter(boolean active) {
        this.mCallback.onEmergencyAnnouncement(active);
    }

    public void onEmergencyAnnouncement(boolean active) {
        this.mHandler.post(new -$$Lambda$TunerCallbackAdapter$ZwPm3xxjeLvbP12KweyzqFJVnj4(this, active));
    }

    public void onAntennaState(boolean connected) {
        this.mIsAntennaConnected = connected;
        this.mHandler.post(new -$$Lambda$TunerCallbackAdapter$dR-VQmFrL_tBD2wpNvborTd8W08(this, connected));
    }

    public /* synthetic */ void lambda$onAntennaState$9$TunerCallbackAdapter(boolean connected) {
        this.mCallback.onAntennaState(connected);
    }

    public /* synthetic */ void lambda$onBackgroundScanAvailabilityChange$10$TunerCallbackAdapter(boolean isAvailable) {
        this.mCallback.onBackgroundScanAvailabilityChange(isAvailable);
    }

    public void onBackgroundScanAvailabilityChange(boolean isAvailable) {
        this.mHandler.post(new -$$Lambda$TunerCallbackAdapter$4zf9n0sz_rU8z6a9GJmRInWrYkQ(this, isAvailable));
    }

    private void sendBackgroundScanCompleteLocked() {
        this.mDelayedCompleteCallback = false;
        this.mHandler.post(new -$$Lambda$TunerCallbackAdapter$xIUT1Qu5TkA83V8ttYy1zv-JuFo(this));
    }

    public /* synthetic */ void lambda$sendBackgroundScanCompleteLocked$11$TunerCallbackAdapter() {
        this.mCallback.onBackgroundScanComplete();
    }

    public void onBackgroundScanComplete() {
        synchronized (this.mLock) {
            if (this.mLastCompleteList == null) {
                Log.i(TAG, "Got onBackgroundScanComplete callback, but the program list didn't get through yet. Delaying it...");
                this.mDelayedCompleteCallback = true;
                return;
            }
            sendBackgroundScanCompleteLocked();
        }
    }

    public /* synthetic */ void lambda$onProgramListChanged$12$TunerCallbackAdapter() {
        this.mCallback.onProgramListChanged();
    }

    public void onProgramListChanged() {
        this.mHandler.post(new -$$Lambda$TunerCallbackAdapter$UsmGhKordXy4lhCylRP0mm2NcYc(this));
    }

    public void onProgramListUpdated(Chunk chunk) {
        synchronized (this.mLock) {
            if (this.mProgramList == null) {
                return;
            }
            this.mProgramList.apply((Chunk) Objects.requireNonNull(chunk));
        }
    }

    public /* synthetic */ void lambda$onParametersUpdated$13$TunerCallbackAdapter(Map parameters) {
        this.mCallback.onParametersUpdated(parameters);
    }

    public void onParametersUpdated(Map parameters) {
        this.mHandler.post(new -$$Lambda$TunerCallbackAdapter$Yz-4KCDu1MOynGdkDf_oMxqhjeY(this, parameters));
    }
}
