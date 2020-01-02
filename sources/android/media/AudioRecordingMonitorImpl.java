package android.media;

import android.media.AudioManager.AudioRecordingCallback;
import android.media.IRecordingConfigDispatcher.Stub;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.annotations.GuardedBy;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;

public class AudioRecordingMonitorImpl implements AudioRecordingMonitor {
    private static final int MSG_RECORDING_CONFIG_CHANGE = 1;
    private static final String TAG = "android.media.AudioRecordingMonitor";
    private static IAudioService sService;
    private final AudioRecordingMonitorClient mClient;
    @GuardedBy({"mRecordCallbackLock"})
    private LinkedList<AudioRecordingCallbackInfo> mRecordCallbackList = new LinkedList();
    private final Object mRecordCallbackLock = new Object();
    @GuardedBy({"mRecordCallbackLock"})
    private final IRecordingConfigDispatcher mRecordingCallback = new Stub() {
        public void dispatchRecordingConfigChange(List<AudioRecordingConfiguration> configs) {
            AudioRecordingConfiguration config = AudioRecordingMonitorImpl.this.getMyConfig(configs);
            if (config != null) {
                synchronized (AudioRecordingMonitorImpl.this.mRecordCallbackLock) {
                    if (AudioRecordingMonitorImpl.this.mRecordingCallbackHandler != null) {
                        AudioRecordingMonitorImpl.this.mRecordingCallbackHandler.sendMessage(AudioRecordingMonitorImpl.this.mRecordingCallbackHandler.obtainMessage(1, config));
                    }
                }
            }
        }
    };
    @GuardedBy({"mRecordCallbackLock"})
    private volatile Handler mRecordingCallbackHandler;
    @GuardedBy({"mRecordCallbackLock"})
    private HandlerThread mRecordingCallbackHandlerThread;

    private static class AudioRecordingCallbackInfo {
        final AudioRecordingCallback mCb;
        final Executor mExecutor;

        AudioRecordingCallbackInfo(Executor e, AudioRecordingCallback cb) {
            this.mExecutor = e;
            this.mCb = cb;
        }
    }

    AudioRecordingMonitorImpl(AudioRecordingMonitorClient client) {
        this.mClient = client;
    }

    public void registerAudioRecordingCallback(Executor executor, AudioRecordingCallback cb) {
        if (cb == null) {
            throw new IllegalArgumentException("Illegal null AudioRecordingCallback");
        } else if (executor != null) {
            synchronized (this.mRecordCallbackLock) {
                Iterator it = this.mRecordCallbackList.iterator();
                while (it.hasNext()) {
                    if (((AudioRecordingCallbackInfo) it.next()).mCb == cb) {
                        throw new IllegalArgumentException("AudioRecordingCallback already registered");
                    }
                }
                beginRecordingCallbackHandling();
                this.mRecordCallbackList.add(new AudioRecordingCallbackInfo(executor, cb));
            }
        } else {
            throw new IllegalArgumentException("Illegal null Executor");
        }
    }

    public void unregisterAudioRecordingCallback(AudioRecordingCallback cb) {
        if (cb != null) {
            synchronized (this.mRecordCallbackLock) {
                Iterator it = this.mRecordCallbackList.iterator();
                while (it.hasNext()) {
                    AudioRecordingCallbackInfo arci = (AudioRecordingCallbackInfo) it.next();
                    if (arci.mCb == cb) {
                        this.mRecordCallbackList.remove(arci);
                        if (this.mRecordCallbackList.size() == 0) {
                            endRecordingCallbackHandling();
                        }
                    }
                }
                throw new IllegalArgumentException("AudioRecordingCallback was not registered");
            }
            return;
        }
        throw new IllegalArgumentException("Illegal null AudioRecordingCallback argument");
    }

    public AudioRecordingConfiguration getActiveRecordingConfiguration() {
        try {
            return getMyConfig(getService().getActiveRecordingConfigurations());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @GuardedBy({"mRecordCallbackLock"})
    private void beginRecordingCallbackHandling() {
        if (this.mRecordingCallbackHandlerThread == null) {
            this.mRecordingCallbackHandlerThread = new HandlerThread("android.media.AudioRecordingMonitor.RecordingCallback");
            this.mRecordingCallbackHandlerThread.start();
            Looper looper = this.mRecordingCallbackHandlerThread.getLooper();
            if (looper != null) {
                this.mRecordingCallbackHandler = new Handler(looper) {
                    /* JADX WARNING: Missing block: B:15:0x0050, code skipped:
            r3 = android.os.Binder.clearCallingIdentity();
     */
                    /* JADX WARNING: Missing block: B:17:?, code skipped:
            r1 = r2.iterator();
     */
                    /* JADX WARNING: Missing block: B:19:0x005c, code skipped:
            if (r1.hasNext() == false) goto L_0x006f;
     */
                    /* JADX WARNING: Missing block: B:20:0x005e, code skipped:
            r5 = (android.media.AudioRecordingMonitorImpl.AudioRecordingCallbackInfo) r1.next();
            r5.mExecutor.execute(new android.media.-$$Lambda$AudioRecordingMonitorImpl$2$cn04v8rie0OYr-_fiLO_SMYka7I(r5, r0));
     */
                    /* JADX WARNING: Missing block: B:24:0x0076, code skipped:
            android.os.Binder.restoreCallingIdentity(r3);
     */
                    public void handleMessage(android.os.Message r9) {
                        /*
                        r8 = this;
                        r0 = r9.what;
                        r1 = 1;
                        if (r0 == r1) goto L_0x001e;
                    L_0x0005:
                        r0 = new java.lang.StringBuilder;
                        r0.<init>();
                        r1 = "Unknown event ";
                        r0.append(r1);
                        r1 = r9.what;
                        r0.append(r1);
                        r0 = r0.toString();
                        r1 = "android.media.AudioRecordingMonitor";
                        android.util.Log.e(r1, r0);
                        goto L_0x0074;
                    L_0x001e:
                        r0 = r9.obj;
                        if (r0 != 0) goto L_0x0023;
                    L_0x0022:
                        return;
                    L_0x0023:
                        r0 = new java.util.ArrayList;
                        r0.<init>();
                        r1 = r9.obj;
                        r1 = (android.media.AudioRecordingConfiguration) r1;
                        r0.add(r1);
                        r1 = android.media.AudioRecordingMonitorImpl.this;
                        r1 = r1.mRecordCallbackLock;
                        monitor-enter(r1);
                        r2 = android.media.AudioRecordingMonitorImpl.this;	 Catch:{ all -> 0x007a }
                        r2 = r2.mRecordCallbackList;	 Catch:{ all -> 0x007a }
                        r2 = r2.size();	 Catch:{ all -> 0x007a }
                        if (r2 != 0) goto L_0x0044;
                    L_0x0042:
                        monitor-exit(r1);	 Catch:{ all -> 0x007a }
                        return;
                    L_0x0044:
                        r2 = new java.util.LinkedList;	 Catch:{ all -> 0x007a }
                        r3 = android.media.AudioRecordingMonitorImpl.this;	 Catch:{ all -> 0x007a }
                        r3 = r3.mRecordCallbackList;	 Catch:{ all -> 0x007a }
                        r2.<init>(r3);	 Catch:{ all -> 0x007a }
                        monitor-exit(r1);	 Catch:{ all -> 0x007a }
                        r3 = android.os.Binder.clearCallingIdentity();
                        r1 = r2.iterator();	 Catch:{ all -> 0x0075 }
                    L_0x0058:
                        r5 = r1.hasNext();	 Catch:{ all -> 0x0075 }
                        if (r5 == 0) goto L_0x006f;
                    L_0x005e:
                        r5 = r1.next();	 Catch:{ all -> 0x0075 }
                        r5 = (android.media.AudioRecordingMonitorImpl.AudioRecordingCallbackInfo) r5;	 Catch:{ all -> 0x0075 }
                        r6 = r5.mExecutor;	 Catch:{ all -> 0x0075 }
                        r7 = new android.media.-$$Lambda$AudioRecordingMonitorImpl$2$cn04v8rie0OYr-_fiLO_SMYka7I;	 Catch:{ all -> 0x0075 }
                        r7.<init>(r5, r0);	 Catch:{ all -> 0x0075 }
                        r6.execute(r7);	 Catch:{ all -> 0x0075 }
                        goto L_0x0058;
                    L_0x006f:
                        android.os.Binder.restoreCallingIdentity(r3);
                    L_0x0074:
                        return;
                    L_0x0075:
                        r1 = move-exception;
                        android.os.Binder.restoreCallingIdentity(r3);
                        throw r1;
                    L_0x007a:
                        r2 = move-exception;
                        monitor-exit(r1);	 Catch:{ all -> 0x007a }
                        throw r2;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioRecordingMonitorImpl$AnonymousClass2.handleMessage(android.os.Message):void");
                    }
                };
                try {
                    getService().registerRecordingCallback(this.mRecordingCallback);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    @GuardedBy({"mRecordCallbackLock"})
    private void endRecordingCallbackHandling() {
        if (this.mRecordingCallbackHandlerThread != null) {
            try {
                getService().unregisterRecordingCallback(this.mRecordingCallback);
                this.mRecordingCallbackHandlerThread.quit();
                this.mRecordingCallbackHandlerThread = null;
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public AudioRecordingConfiguration getMyConfig(List<AudioRecordingConfiguration> configs) {
        int portId = this.mClient.getPortId();
        for (AudioRecordingConfiguration config : configs) {
            if (config.getClientPortId() == portId) {
                return config;
            }
        }
        return null;
    }

    private static IAudioService getService() {
        IAudioService iAudioService = sService;
        if (iAudioService != null) {
            return iAudioService;
        }
        sService = IAudioService.Stub.asInterface(ServiceManager.getService("audio"));
        return sService;
    }
}
