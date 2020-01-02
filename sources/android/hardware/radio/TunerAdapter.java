package android.hardware.radio;

import android.graphics.Bitmap;
import android.hardware.radio.ProgramList.Filter;
import android.hardware.radio.RadioManager.BandConfig;
import android.hardware.radio.RadioManager.ProgramInfo;
import android.os.RemoteException;
import android.util.Log;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class TunerAdapter extends RadioTuner {
    private static final String TAG = "BroadcastRadio.TunerAdapter";
    private int mBand;
    private final TunerCallbackAdapter mCallback;
    private boolean mIsClosed = false;
    private Map<String, String> mLegacyListFilter;
    private ProgramList mLegacyListProxy;
    private final ITuner mTuner;

    TunerAdapter(ITuner tuner, TunerCallbackAdapter callback, int band) {
        this.mTuner = (ITuner) Objects.requireNonNull(tuner);
        this.mCallback = (TunerCallbackAdapter) Objects.requireNonNull(callback);
        this.mBand = band;
    }

    /* JADX WARNING: Missing block: B:14:?, code skipped:
            r3.mTuner.close();
     */
    /* JADX WARNING: Missing block: B:15:0x002b, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:16:0x002c, code skipped:
            android.util.Log.e(TAG, "Exception trying to close tuner", r0);
     */
    public void close() {
        /*
        r3 = this;
        r0 = r3.mTuner;
        monitor-enter(r0);
        r1 = r3.mIsClosed;	 Catch:{ all -> 0x0034 }
        if (r1 == 0) goto L_0x0010;
    L_0x0007:
        r1 = "BroadcastRadio.TunerAdapter";
        r2 = "Tuner is already closed";
        android.util.Log.v(r1, r2);	 Catch:{ all -> 0x0034 }
        monitor-exit(r0);	 Catch:{ all -> 0x0034 }
        return;
    L_0x0010:
        r1 = 1;
        r3.mIsClosed = r1;	 Catch:{ all -> 0x0034 }
        r1 = r3.mLegacyListProxy;	 Catch:{ all -> 0x0034 }
        if (r1 == 0) goto L_0x001f;
    L_0x0017:
        r1 = r3.mLegacyListProxy;	 Catch:{ all -> 0x0034 }
        r1.close();	 Catch:{ all -> 0x0034 }
        r1 = 0;
        r3.mLegacyListProxy = r1;	 Catch:{ all -> 0x0034 }
    L_0x001f:
        r1 = r3.mCallback;	 Catch:{ all -> 0x0034 }
        r1.close();	 Catch:{ all -> 0x0034 }
        monitor-exit(r0);	 Catch:{ all -> 0x0034 }
        r0 = r3.mTuner;	 Catch:{ RemoteException -> 0x002b }
        r0.close();	 Catch:{ RemoteException -> 0x002b }
        goto L_0x0033;
    L_0x002b:
        r0 = move-exception;
        r1 = "BroadcastRadio.TunerAdapter";
        r2 = "Exception trying to close tuner";
        android.util.Log.e(r1, r2, r0);
    L_0x0033:
        return;
    L_0x0034:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0034 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.radio.TunerAdapter.close():void");
    }

    public int setConfiguration(BandConfig config) {
        String str = TAG;
        if (config == null) {
            return -22;
        }
        try {
            this.mTuner.setConfiguration(config);
            this.mBand = config.getType();
            return 0;
        } catch (IllegalArgumentException e) {
            Log.e(str, "Can't set configuration", e);
            return -22;
        } catch (RemoteException e2) {
            Log.e(str, "service died", e2);
            return -32;
        }
    }

    public int getConfiguration(BandConfig[] config) {
        if (config == null || config.length != 1) {
            throw new IllegalArgumentException("The argument must be an array of length 1");
        }
        try {
            config[0] = this.mTuner.getConfiguration();
            return 0;
        } catch (RemoteException e) {
            Log.e(TAG, "service died", e);
            return -32;
        }
    }

    public int setMute(boolean mute) {
        String str = TAG;
        try {
            this.mTuner.setMuted(mute);
            return 0;
        } catch (IllegalStateException e) {
            Log.e(str, "Can't set muted", e);
            return Integer.MIN_VALUE;
        } catch (RemoteException e2) {
            Log.e(str, "service died", e2);
            return -32;
        }
    }

    public boolean getMute() {
        try {
            return this.mTuner.isMuted();
        } catch (RemoteException e) {
            Log.e(TAG, "service died", e);
            return true;
        }
    }

    public int step(int direction, boolean skipSubChannel) {
        String str = TAG;
        try {
            ITuner iTuner = this.mTuner;
            boolean z = true;
            if (direction != 1) {
                z = false;
            }
            iTuner.step(z, skipSubChannel);
            return 0;
        } catch (IllegalStateException e) {
            Log.e(str, "Can't step", e);
            return -38;
        } catch (RemoteException e2) {
            Log.e(str, "service died", e2);
            return -32;
        }
    }

    public int scan(int direction, boolean skipSubChannel) {
        String str = TAG;
        try {
            ITuner iTuner = this.mTuner;
            boolean z = true;
            if (direction != 1) {
                z = false;
            }
            iTuner.scan(z, skipSubChannel);
            return 0;
        } catch (IllegalStateException e) {
            Log.e(str, "Can't scan", e);
            return -38;
        } catch (RemoteException e2) {
            Log.e(str, "service died", e2);
            return -32;
        }
    }

    public int tune(int channel, int subChannel) {
        String str = "Can't tune";
        String str2 = TAG;
        try {
            this.mTuner.tune(ProgramSelector.createAmFmSelector(this.mBand, channel, subChannel));
            return 0;
        } catch (IllegalStateException e) {
            Log.e(str2, str, e);
            return -38;
        } catch (IllegalArgumentException e2) {
            Log.e(str2, str, e2);
            return -22;
        } catch (RemoteException e3) {
            Log.e(str2, "service died", e3);
            return -32;
        }
    }

    public void tune(ProgramSelector selector) {
        try {
            this.mTuner.tune(selector);
        } catch (RemoteException e) {
            throw new RuntimeException("service died", e);
        }
    }

    public int cancel() {
        String str = TAG;
        try {
            this.mTuner.cancel();
            return 0;
        } catch (IllegalStateException e) {
            Log.e(str, "Can't cancel", e);
            return -38;
        } catch (RemoteException e2) {
            Log.e(str, "service died", e2);
            return -32;
        }
    }

    public void cancelAnnouncement() {
        try {
            this.mTuner.cancelAnnouncement();
        } catch (RemoteException e) {
            throw new RuntimeException("service died", e);
        }
    }

    public int getProgramInformation(ProgramInfo[] info) {
        String str = TAG;
        if (info == null || info.length != 1) {
            Log.e(str, "The argument must be an array of length 1");
            return -22;
        }
        ProgramInfo current = this.mCallback.getCurrentProgramInformation();
        if (current == null) {
            Log.w(str, "Didn't get program info yet");
            return -38;
        }
        info[0] = current;
        return 0;
    }

    public Bitmap getMetadataImage(int id) {
        try {
            return this.mTuner.getImage(id);
        } catch (RemoteException e) {
            throw new RuntimeException("service died", e);
        }
    }

    public boolean startBackgroundScan() {
        try {
            return this.mTuner.startBackgroundScan();
        } catch (RemoteException e) {
            throw new RuntimeException("service died", e);
        }
    }

    public List<ProgramInfo> getProgramList(Map<String, String> vendorFilter) {
        List<ProgramInfo> list;
        synchronized (this.mTuner) {
            if (this.mLegacyListProxy == null || !Objects.equals(this.mLegacyListFilter, vendorFilter)) {
                Log.i(TAG, "Program list filter has changed, requesting new list");
                this.mLegacyListProxy = new ProgramList();
                this.mLegacyListFilter = vendorFilter;
                this.mCallback.clearLastCompleteList();
                this.mCallback.setProgramListObserver(this.mLegacyListProxy, -$$Lambda$TunerAdapter$xm27iP_3PUgByOaDoK2KJcP5fnA.INSTANCE);
                try {
                    this.mTuner.startProgramListUpdates(new Filter((Map) vendorFilter));
                } catch (RemoteException ex) {
                    throw new RuntimeException("service died", ex);
                }
            }
            list = this.mCallback.getLastCompleteList();
            if (list != null) {
            } else {
                throw new IllegalStateException("Program list is not ready yet");
            }
        }
        return list;
    }

    static /* synthetic */ void lambda$getProgramList$0() {
    }

    public ProgramList getDynamicProgramList(Filter filter) {
        ProgramList list;
        synchronized (this.mTuner) {
            if (this.mLegacyListProxy != null) {
                this.mLegacyListProxy.close();
                this.mLegacyListProxy = null;
            }
            this.mLegacyListFilter = null;
            list = new ProgramList();
            this.mCallback.setProgramListObserver(list, new -$$Lambda$TunerAdapter$ytmKJEaNVVp6n7nE6SVU6pZ9g7c(this));
            try {
                this.mTuner.startProgramListUpdates(filter);
            } catch (UnsupportedOperationException e) {
                Log.i(TAG, "Program list is not supported with this hardware");
                return null;
            } catch (RemoteException ex) {
                this.mCallback.setProgramListObserver(null, -$$Lambda$TunerAdapter$St9hluCzvLWs9wyE7kDX24NpwJQ.INSTANCE);
                throw new RuntimeException("service died", ex);
            }
        }
        return list;
    }

    public /* synthetic */ void lambda$getDynamicProgramList$1$TunerAdapter() {
        try {
            this.mTuner.stopProgramListUpdates();
        } catch (RemoteException ex) {
            Log.e(TAG, "Couldn't stop program list updates", ex);
        }
    }

    static /* synthetic */ void lambda$getDynamicProgramList$2() {
    }

    public boolean isAnalogForced() {
        try {
            return isConfigFlagSet(2);
        } catch (UnsupportedOperationException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public void setAnalogForced(boolean isForced) {
        try {
            setConfigFlag(2, isForced);
        } catch (UnsupportedOperationException ex) {
            throw new IllegalStateException(ex);
        }
    }

    public boolean isConfigFlagSupported(int flag) {
        try {
            return this.mTuner.isConfigFlagSupported(flag);
        } catch (RemoteException e) {
            throw new RuntimeException("service died", e);
        }
    }

    public boolean isConfigFlagSet(int flag) {
        try {
            return this.mTuner.isConfigFlagSet(flag);
        } catch (RemoteException e) {
            throw new RuntimeException("service died", e);
        }
    }

    public void setConfigFlag(int flag, boolean value) {
        try {
            this.mTuner.setConfigFlag(flag, value);
        } catch (RemoteException e) {
            throw new RuntimeException("service died", e);
        }
    }

    public Map<String, String> setParameters(Map<String, String> parameters) {
        try {
            return this.mTuner.setParameters((Map) Objects.requireNonNull(parameters));
        } catch (RemoteException e) {
            throw new RuntimeException("service died", e);
        }
    }

    public Map<String, String> getParameters(List<String> keys) {
        try {
            return this.mTuner.getParameters((List) Objects.requireNonNull(keys));
        } catch (RemoteException e) {
            throw new RuntimeException("service died", e);
        }
    }

    public boolean isAntennaConnected() {
        return this.mCallback.isAntennaConnected();
    }

    public boolean hasControl() {
        try {
            return this.mTuner.isClosed() ^ 1;
        } catch (RemoteException e) {
            return false;
        }
    }
}
