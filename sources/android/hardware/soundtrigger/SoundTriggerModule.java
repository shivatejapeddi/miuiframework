package android.hardware.soundtrigger;

import android.annotation.UnsupportedAppUsage;
import android.hardware.soundtrigger.SoundTrigger.RecognitionConfig;
import android.hardware.soundtrigger.SoundTrigger.RecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.SoundModel;
import android.hardware.soundtrigger.SoundTrigger.SoundModelEvent;
import android.hardware.soundtrigger.SoundTrigger.StatusListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

public class SoundTriggerModule {
    private static final int EVENT_RECOGNITION = 1;
    private static final int EVENT_SERVICE_DIED = 2;
    private static final int EVENT_SERVICE_STATE_CHANGE = 4;
    private static final int EVENT_SOUNDMODEL = 3;
    private NativeEventHandlerDelegate mEventHandlerDelegate;
    @UnsupportedAppUsage
    private int mId;
    @UnsupportedAppUsage
    private long mNativeContext;

    private class NativeEventHandlerDelegate {
        private final Handler mHandler;

        NativeEventHandlerDelegate(final StatusListener listener, Handler handler) {
            Looper looper;
            if (handler != null) {
                looper = handler.getLooper();
            } else {
                looper = Looper.getMainLooper();
            }
            if (looper != null) {
                this.mHandler = new Handler(looper, SoundTriggerModule.this) {
                    public void handleMessage(Message msg) {
                        int i = msg.what;
                        StatusListener statusListener;
                        if (i == 1) {
                            statusListener = listener;
                            if (statusListener != null) {
                                statusListener.onRecognition((RecognitionEvent) msg.obj);
                            }
                        } else if (i == 2) {
                            statusListener = listener;
                            if (statusListener != null) {
                                statusListener.onServiceDied();
                            }
                        } else if (i == 3) {
                            statusListener = listener;
                            if (statusListener != null) {
                                statusListener.onSoundModelUpdate((SoundModelEvent) msg.obj);
                            }
                        } else if (i == 4) {
                            statusListener = listener;
                            if (statusListener != null) {
                                statusListener.onServiceStateChange(msg.arg1);
                            }
                        }
                    }
                };
            } else {
                this.mHandler = null;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public Handler handler() {
            return this.mHandler;
        }
    }

    private native void native_finalize();

    private native void native_setup(String str, Object obj);

    @UnsupportedAppUsage
    public native void detach();

    public native int getModelState(int i);

    @UnsupportedAppUsage
    public native int loadSoundModel(SoundModel soundModel, int[] iArr);

    @UnsupportedAppUsage
    public native int startRecognition(int i, RecognitionConfig recognitionConfig);

    @UnsupportedAppUsage
    public native int stopRecognition(int i);

    @UnsupportedAppUsage
    public native int unloadSoundModel(int i);

    SoundTriggerModule(int moduleId, StatusListener listener, Handler handler) {
        this.mId = moduleId;
        this.mEventHandlerDelegate = new NativeEventHandlerDelegate(listener, handler);
        native_setup(SoundTrigger.getCurrentOpPackageName(), new WeakReference(this));
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        native_finalize();
    }

    @UnsupportedAppUsage
    private static void postEventFromNative(Object module_ref, int what, int arg1, int arg2, Object obj) {
        SoundTriggerModule module = (SoundTriggerModule) ((WeakReference) module_ref).get();
        if (module != null) {
            NativeEventHandlerDelegate delegate = module.mEventHandlerDelegate;
            if (delegate != null) {
                Handler handler = delegate.handler();
                if (handler != null) {
                    handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
                }
            }
        }
    }
}
