package android.media.soundtrigger;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.hardware.soundtrigger.IRecognitionStatusCallback.Stub;
import android.hardware.soundtrigger.SoundTrigger.GenericRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.KeyphraseRecognitionEvent;
import android.hardware.soundtrigger.SoundTrigger.RecognitionConfig;
import android.media.AudioFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Slog;
import com.android.internal.app.ISoundTriggerService;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.UUID;

@SystemApi
public final class SoundTriggerDetector {
    private static final boolean DBG = false;
    private static final int MSG_AVAILABILITY_CHANGED = 1;
    private static final int MSG_DETECTION_ERROR = 3;
    private static final int MSG_DETECTION_PAUSE = 4;
    private static final int MSG_DETECTION_RESUME = 5;
    private static final int MSG_SOUND_TRIGGER_DETECTED = 2;
    public static final int RECOGNITION_FLAG_ALLOW_MULTIPLE_TRIGGERS = 2;
    public static final int RECOGNITION_FLAG_CAPTURE_TRIGGER_AUDIO = 1;
    public static final int RECOGNITION_FLAG_NONE = 0;
    private static final String TAG = "SoundTriggerDetector";
    private final Callback mCallback;
    private final Handler mHandler;
    private final Object mLock = new Object();
    private final RecognitionCallback mRecognitionCallback;
    private final UUID mSoundModelId;
    private final ISoundTriggerService mSoundTriggerService;

    public static abstract class Callback {
        public abstract void onAvailabilityChanged(int i);

        public abstract void onDetected(EventPayload eventPayload);

        public abstract void onError();

        public abstract void onRecognitionPaused();

        public abstract void onRecognitionResumed();
    }

    public static class EventPayload {
        private final AudioFormat mAudioFormat;
        private final boolean mCaptureAvailable;
        private final int mCaptureSession;
        private final byte[] mData;
        private final boolean mTriggerAvailable;

        private EventPayload(boolean triggerAvailable, boolean captureAvailable, AudioFormat audioFormat, int captureSession, byte[] data) {
            this.mTriggerAvailable = triggerAvailable;
            this.mCaptureAvailable = captureAvailable;
            this.mCaptureSession = captureSession;
            this.mAudioFormat = audioFormat;
            this.mData = data;
        }

        public AudioFormat getCaptureAudioFormat() {
            return this.mAudioFormat;
        }

        public byte[] getTriggerAudio() {
            if (this.mTriggerAvailable) {
                return this.mData;
            }
            return null;
        }

        @UnsupportedAppUsage
        public byte[] getData() {
            if (this.mTriggerAvailable) {
                return null;
            }
            return this.mData;
        }

        @UnsupportedAppUsage
        public Integer getCaptureSession() {
            if (this.mCaptureAvailable) {
                return Integer.valueOf(this.mCaptureSession);
            }
            return null;
        }
    }

    private class MyHandler extends Handler {
        MyHandler() {
        }

        MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            if (SoundTriggerDetector.this.mCallback == null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Received message: ");
                stringBuilder.append(msg.what);
                stringBuilder.append(" for NULL callback.");
                Slog.w(SoundTriggerDetector.TAG, stringBuilder.toString());
                return;
            }
            int i = msg.what;
            if (i == 2) {
                SoundTriggerDetector.this.mCallback.onDetected((EventPayload) msg.obj);
            } else if (i == 3) {
                SoundTriggerDetector.this.mCallback.onError();
            } else if (i == 4) {
                SoundTriggerDetector.this.mCallback.onRecognitionPaused();
            } else if (i != 5) {
                super.handleMessage(msg);
            } else {
                SoundTriggerDetector.this.mCallback.onRecognitionResumed();
            }
        }
    }

    private class RecognitionCallback extends Stub {
        private RecognitionCallback() {
        }

        public void onGenericSoundTriggerDetected(GenericRecognitionEvent event) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onGenericSoundTriggerDetected()");
            stringBuilder.append(event);
            Slog.d(SoundTriggerDetector.TAG, stringBuilder.toString());
            Message.obtain(SoundTriggerDetector.this.mHandler, 2, new EventPayload(event.triggerInData, event.captureAvailable, event.captureFormat, event.captureSession, event.data)).sendToTarget();
        }

        public void onKeyphraseDetected(KeyphraseRecognitionEvent event) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ignoring onKeyphraseDetected() called for ");
            stringBuilder.append(event);
            Slog.e(SoundTriggerDetector.TAG, stringBuilder.toString());
        }

        public void onError(int status) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onError()");
            stringBuilder.append(status);
            Slog.d(SoundTriggerDetector.TAG, stringBuilder.toString());
            SoundTriggerDetector.this.mHandler.sendEmptyMessage(3);
        }

        public void onRecognitionPaused() {
            Slog.d(SoundTriggerDetector.TAG, "onRecognitionPaused()");
            SoundTriggerDetector.this.mHandler.sendEmptyMessage(4);
        }

        public void onRecognitionResumed() {
            Slog.d(SoundTriggerDetector.TAG, "onRecognitionResumed()");
            SoundTriggerDetector.this.mHandler.sendEmptyMessage(5);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RecognitionFlags {
    }

    SoundTriggerDetector(ISoundTriggerService soundTriggerService, UUID soundModelId, Callback callback, Handler handler) {
        this.mSoundTriggerService = soundTriggerService;
        this.mSoundModelId = soundModelId;
        this.mCallback = callback;
        if (handler == null) {
            this.mHandler = new MyHandler();
        } else {
            this.mHandler = new MyHandler(handler.getLooper());
        }
        this.mRecognitionCallback = new RecognitionCallback();
    }

    public boolean startRecognition(int recognitionFlags) {
        boolean z = false;
        try {
            if (this.mSoundTriggerService.startRecognition(new ParcelUuid(this.mSoundModelId), this.mRecognitionCallback, new RecognitionConfig((recognitionFlags & 1) != 0, (recognitionFlags & 2) != 0, null, null)) == 0) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean stopRecognition() {
        boolean z = false;
        try {
            if (this.mSoundTriggerService.stopRecognition(new ParcelUuid(this.mSoundModelId), this.mRecognitionCallback) == 0) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            return false;
        }
    }

    public void dump(String prefix, PrintWriter pw) {
        synchronized (this.mLock) {
        }
    }
}
