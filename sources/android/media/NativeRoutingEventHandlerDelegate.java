package android.media;

import android.media.AudioRouting.OnRoutingChangedListener;
import android.os.Handler;

class NativeRoutingEventHandlerDelegate {
    private AudioRouting mAudioRouting;
    private Handler mHandler;
    private OnRoutingChangedListener mOnRoutingChangedListener;

    NativeRoutingEventHandlerDelegate(AudioRouting audioRouting, OnRoutingChangedListener listener, Handler handler) {
        this.mAudioRouting = audioRouting;
        this.mOnRoutingChangedListener = listener;
        this.mHandler = handler;
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyClient() {
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(new Runnable() {
                public void run() {
                    if (NativeRoutingEventHandlerDelegate.this.mOnRoutingChangedListener != null) {
                        NativeRoutingEventHandlerDelegate.this.mOnRoutingChangedListener.onRoutingChanged(NativeRoutingEventHandlerDelegate.this.mAudioRouting);
                    }
                }
            });
        }
    }
}
