package android.media;

import android.annotation.UnsupportedAppUsage;
import android.media.AudioManager.OnAudioPortUpdateListener;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

class AudioPortEventHandler {
    private static final int AUDIOPORT_EVENT_NEW_LISTENER = 4;
    private static final int AUDIOPORT_EVENT_PATCH_LIST_UPDATED = 2;
    private static final int AUDIOPORT_EVENT_PORT_LIST_UPDATED = 1;
    private static final int AUDIOPORT_EVENT_SERVICE_DIED = 3;
    private static final long RESCHEDULE_MESSAGE_DELAY_MS = 100;
    private static final String TAG = "AudioPortEventHandler";
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    @UnsupportedAppUsage
    private long mJniCallback;
    private final ArrayList<OnAudioPortUpdateListener> mListeners = new ArrayList();

    private native void native_finalize();

    private native void native_setup(Object obj);

    AudioPortEventHandler() {
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:11:0x0037, code skipped:
            return;
     */
    public void init() {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = r2.mHandler;	 Catch:{ all -> 0x0038 }
        if (r0 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r2);	 Catch:{ all -> 0x0038 }
        return;
    L_0x0007:
        r0 = new android.os.HandlerThread;	 Catch:{ all -> 0x0038 }
        r1 = "AudioPortEventHandler";
        r0.<init>(r1);	 Catch:{ all -> 0x0038 }
        r2.mHandlerThread = r0;	 Catch:{ all -> 0x0038 }
        r0 = r2.mHandlerThread;	 Catch:{ all -> 0x0038 }
        r0.start();	 Catch:{ all -> 0x0038 }
        r0 = r2.mHandlerThread;	 Catch:{ all -> 0x0038 }
        r0 = r0.getLooper();	 Catch:{ all -> 0x0038 }
        if (r0 == 0) goto L_0x0033;
    L_0x001d:
        r0 = new android.media.AudioPortEventHandler$1;	 Catch:{ all -> 0x0038 }
        r1 = r2.mHandlerThread;	 Catch:{ all -> 0x0038 }
        r1 = r1.getLooper();	 Catch:{ all -> 0x0038 }
        r0.<init>(r1);	 Catch:{ all -> 0x0038 }
        r2.mHandler = r0;	 Catch:{ all -> 0x0038 }
        r0 = new java.lang.ref.WeakReference;	 Catch:{ all -> 0x0038 }
        r0.<init>(r2);	 Catch:{ all -> 0x0038 }
        r2.native_setup(r0);	 Catch:{ all -> 0x0038 }
        goto L_0x0036;
    L_0x0033:
        r0 = 0;
        r2.mHandler = r0;	 Catch:{ all -> 0x0038 }
    L_0x0036:
        monitor-exit(r2);	 Catch:{ all -> 0x0038 }
        return;
    L_0x0038:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0038 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioPortEventHandler.init():void");
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        native_finalize();
        if (this.mHandlerThread.isAlive()) {
            this.mHandlerThread.quit();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void registerListener(OnAudioPortUpdateListener l) {
        synchronized (this) {
            this.mListeners.add(l);
        }
        Message m = this.mHandler;
        if (m != null) {
            this.mHandler.sendMessage(m.obtainMessage(4, 0, 0, l));
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void unregisterListener(OnAudioPortUpdateListener l) {
        synchronized (this) {
            this.mListeners.remove(l);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Handler handler() {
        return this.mHandler;
    }

    @UnsupportedAppUsage
    private static void postEventFromNative(Object module_ref, int what, int arg1, int arg2, Object obj) {
        AudioPortEventHandler eventHandler = (AudioPortEventHandler) ((WeakReference) module_ref).get();
        if (eventHandler != null) {
            Handler handler = eventHandler.handler();
            if (handler != null) {
                Message m = handler.obtainMessage(what, arg1, arg2, obj);
                if (what != 4) {
                    handler.removeMessages(what);
                }
                handler.sendMessage(m);
            }
        }
    }
}
