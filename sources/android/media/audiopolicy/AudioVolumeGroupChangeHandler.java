package android.media.audiopolicy;

import android.media.AudioManager.VolumeGroupCallback;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.android.internal.util.Preconditions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class AudioVolumeGroupChangeHandler {
    private static final int AUDIOVOLUMEGROUP_EVENT_NEW_LISTENER = 4;
    private static final int AUDIOVOLUMEGROUP_EVENT_VOLUME_CHANGED = 1000;
    private static final String TAG = "AudioVolumeGroupChangeHandler";
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    private long mJniCallback;
    private final ArrayList<VolumeGroupCallback> mListeners = new ArrayList();

    private native void native_finalize();

    private native void native_setup(Object obj);

    public void init() {
        synchronized (this) {
            if (this.mHandler != null) {
                return;
            }
            this.mHandlerThread = new HandlerThread(TAG);
            this.mHandlerThread.start();
            if (this.mHandlerThread.getLooper() == null) {
                this.mHandler = null;
                return;
            }
            this.mHandler = new Handler(this.mHandlerThread.getLooper()) {
                public void handleMessage(Message msg) {
                    ArrayList<VolumeGroupCallback> listeners;
                    synchronized (this) {
                        if (msg.what == 4) {
                            listeners = new ArrayList();
                            if (AudioVolumeGroupChangeHandler.this.mListeners.contains(msg.obj)) {
                                listeners.add((VolumeGroupCallback) msg.obj);
                            }
                        } else {
                            listeners = AudioVolumeGroupChangeHandler.this.mListeners;
                        }
                    }
                    if (!listeners.isEmpty() && msg.what == 1000) {
                        for (int i = 0; i < listeners.size(); i++) {
                            ((VolumeGroupCallback) listeners.get(i)).onAudioVolumeGroupChanged(msg.arg1, msg.arg2);
                        }
                    }
                }
            };
            native_setup(new WeakReference(this));
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        native_finalize();
        if (this.mHandlerThread.isAlive()) {
            this.mHandlerThread.quit();
        }
    }

    public void registerListener(VolumeGroupCallback cb) {
        Preconditions.checkNotNull(cb, "volume group callback shall not be null");
        synchronized (this) {
            this.mListeners.add(cb);
        }
        Message m = this.mHandler;
        if (m != null) {
            this.mHandler.sendMessage(m.obtainMessage(4, 0, 0, cb));
        }
    }

    public void unregisterListener(VolumeGroupCallback cb) {
        Preconditions.checkNotNull(cb, "volume group callback shall not be null");
        synchronized (this) {
            this.mListeners.remove(cb);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Handler handler() {
        return this.mHandler;
    }

    private static void postEventFromNative(Object moduleRef, int what, int arg1, int arg2, Object obj) {
        AudioVolumeGroupChangeHandler eventHandler = (AudioVolumeGroupChangeHandler) ((WeakReference) moduleRef).get();
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
