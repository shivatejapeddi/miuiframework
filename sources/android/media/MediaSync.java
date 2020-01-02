package android.media;

import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public final class MediaSync {
    private static final int CB_RETURN_AUDIO_BUFFER = 1;
    private static final int EVENT_CALLBACK = 1;
    private static final int EVENT_SET_CALLBACK = 2;
    public static final int MEDIASYNC_ERROR_AUDIOTRACK_FAIL = 1;
    public static final int MEDIASYNC_ERROR_SURFACE_FAIL = 2;
    private static final String TAG = "MediaSync";
    private List<AudioBuffer> mAudioBuffers = new LinkedList();
    private Handler mAudioHandler = null;
    private final Object mAudioLock = new Object();
    private Looper mAudioLooper = null;
    private Thread mAudioThread = null;
    private AudioTrack mAudioTrack = null;
    private Callback mCallback = null;
    private Handler mCallbackHandler = null;
    private final Object mCallbackLock = new Object();
    private long mNativeContext;
    private OnErrorListener mOnErrorListener = null;
    private Handler mOnErrorListenerHandler = null;
    private final Object mOnErrorListenerLock = new Object();
    private float mPlaybackRate = 0.0f;

    private static class AudioBuffer {
        public int mBufferIndex;
        public ByteBuffer mByteBuffer;
        long mPresentationTimeUs;

        public AudioBuffer(ByteBuffer byteBuffer, int bufferId, long presentationTimeUs) {
            this.mByteBuffer = byteBuffer;
            this.mBufferIndex = bufferId;
            this.mPresentationTimeUs = presentationTimeUs;
        }
    }

    public static abstract class Callback {
        public abstract void onAudioBufferConsumed(MediaSync mediaSync, ByteBuffer byteBuffer, int i);
    }

    public interface OnErrorListener {
        void onError(MediaSync mediaSync, int i, int i2);
    }

    private final native void native_finalize();

    private final native void native_flush();

    private final native long native_getPlayTimeForPendingAudioFrames();

    private final native boolean native_getTimestamp(MediaTimestamp mediaTimestamp);

    private static final native void native_init();

    private final native void native_release();

    private final native void native_setAudioTrack(AudioTrack audioTrack);

    private native float native_setPlaybackParams(PlaybackParams playbackParams);

    private final native void native_setSurface(Surface surface);

    private native float native_setSyncParams(SyncParams syncParams);

    private final native void native_setup();

    private final native void native_updateQueuedAudioData(int i, long j);

    public final native Surface createInputSurface();

    public native PlaybackParams getPlaybackParams();

    public native SyncParams getSyncParams();

    public MediaSync() {
        native_setup();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        native_finalize();
    }

    public final void release() {
        returnAudioBuffers();
        if (this.mAudioThread != null) {
            Looper looper = this.mAudioLooper;
            if (looper != null) {
                looper.quit();
            }
        }
        setCallback(null, null);
        native_release();
    }

    public void setCallback(Callback cb, Handler handler) {
        synchronized (this.mCallbackLock) {
            if (handler != null) {
                this.mCallbackHandler = handler;
            } else {
                Looper myLooper = Looper.myLooper();
                Looper looper = myLooper;
                if (myLooper == null) {
                    looper = Looper.getMainLooper();
                }
                if (looper == null) {
                    this.mCallbackHandler = null;
                } else {
                    this.mCallbackHandler = new Handler(looper);
                }
            }
            this.mCallback = cb;
        }
    }

    public void setOnErrorListener(OnErrorListener listener, Handler handler) {
        synchronized (this.mOnErrorListenerLock) {
            if (handler != null) {
                this.mOnErrorListenerHandler = handler;
            } else {
                Looper myLooper = Looper.myLooper();
                Looper looper = myLooper;
                if (myLooper == null) {
                    looper = Looper.getMainLooper();
                }
                if (looper == null) {
                    this.mOnErrorListenerHandler = null;
                } else {
                    this.mOnErrorListenerHandler = new Handler(looper);
                }
            }
            this.mOnErrorListener = listener;
        }
    }

    public void setSurface(Surface surface) {
        native_setSurface(surface);
    }

    public void setAudioTrack(AudioTrack audioTrack) {
        native_setAudioTrack(audioTrack);
        this.mAudioTrack = audioTrack;
        if (audioTrack != null && this.mAudioThread == null) {
            createAudioThread();
        }
    }

    public void setPlaybackParams(PlaybackParams params) {
        synchronized (this.mAudioLock) {
            this.mPlaybackRate = native_setPlaybackParams(params);
        }
        if (((double) this.mPlaybackRate) != 0.0d && this.mAudioThread != null) {
            postRenderAudio(0);
        }
    }

    public void setSyncParams(SyncParams params) {
        synchronized (this.mAudioLock) {
            this.mPlaybackRate = native_setSyncParams(params);
        }
        if (((double) this.mPlaybackRate) != 0.0d && this.mAudioThread != null) {
            postRenderAudio(0);
        }
    }

    public void flush() {
        synchronized (this.mAudioLock) {
            this.mAudioBuffers.clear();
            this.mCallbackHandler.removeCallbacksAndMessages(null);
        }
        AudioTrack audioTrack = this.mAudioTrack;
        if (audioTrack != null) {
            audioTrack.pause();
            this.mAudioTrack.flush();
            this.mAudioTrack.stop();
        }
        native_flush();
    }

    public MediaTimestamp getTimestamp() {
        try {
            MediaTimestamp timestamp = new MediaTimestamp();
            if (native_getTimestamp(timestamp)) {
                return timestamp;
            }
            return null;
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public void queueAudio(ByteBuffer audioData, int bufferId, long presentationTimeUs) {
        if (this.mAudioTrack == null || this.mAudioThread == null) {
            throw new IllegalStateException("AudioTrack is NOT set or audio thread is not created");
        }
        synchronized (this.mAudioLock) {
            this.mAudioBuffers.add(new AudioBuffer(audioData, bufferId, presentationTimeUs));
        }
        if (((double) this.mPlaybackRate) != 0.0d) {
            postRenderAudio(0);
        }
    }

    private void postRenderAudio(long delayMillis) {
        this.mAudioHandler.postDelayed(new Runnable() {
            /* JADX WARNING: Missing block: B:31:0x009c, code skipped:
            return;
     */
            public void run() {
                /*
                r11 = this;
                r0 = android.media.MediaSync.this;
                r0 = r0.mAudioLock;
                monitor-enter(r0);
                r1 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r1 = r1.mPlaybackRate;	 Catch:{ all -> 0x00b4 }
                r1 = (double) r1;	 Catch:{ all -> 0x00b4 }
                r3 = 0;
                r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1));
                if (r1 != 0) goto L_0x0016;
            L_0x0014:
                monitor-exit(r0);	 Catch:{ all -> 0x00b4 }
                return;
            L_0x0016:
                r1 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r1 = r1.mAudioBuffers;	 Catch:{ all -> 0x00b4 }
                r1 = r1.isEmpty();	 Catch:{ all -> 0x00b4 }
                if (r1 == 0) goto L_0x0024;
            L_0x0022:
                monitor-exit(r0);	 Catch:{ all -> 0x00b4 }
                return;
            L_0x0024:
                r1 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r1 = r1.mAudioBuffers;	 Catch:{ all -> 0x00b4 }
                r2 = 0;
                r1 = r1.get(r2);	 Catch:{ all -> 0x00b4 }
                r1 = (android.media.MediaSync.AudioBuffer) r1;	 Catch:{ all -> 0x00b4 }
                r3 = r1.mByteBuffer;	 Catch:{ all -> 0x00b4 }
                r3 = r3.remaining();	 Catch:{ all -> 0x00b4 }
                if (r3 <= 0) goto L_0x0058;
            L_0x0039:
                r4 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r4 = r4.mAudioTrack;	 Catch:{ all -> 0x00b4 }
                r4 = r4.getPlayState();	 Catch:{ all -> 0x00b4 }
                r5 = 3;
                if (r4 == r5) goto L_0x0058;
            L_0x0046:
                r4 = android.media.MediaSync.this;	 Catch:{ IllegalStateException -> 0x0050 }
                r4 = r4.mAudioTrack;	 Catch:{ IllegalStateException -> 0x0050 }
                r4.play();	 Catch:{ IllegalStateException -> 0x0050 }
                goto L_0x0058;
            L_0x0050:
                r4 = move-exception;
                r5 = "MediaSync";
                r6 = "could not start audio track";
                android.util.Log.w(r5, r6);	 Catch:{ all -> 0x00b4 }
            L_0x0058:
                r4 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r4 = r4.mAudioTrack;	 Catch:{ all -> 0x00b4 }
                r5 = r1.mByteBuffer;	 Catch:{ all -> 0x00b4 }
                r6 = 1;
                r4 = r4.write(r5, r3, r6);	 Catch:{ all -> 0x00b4 }
                if (r4 <= 0) goto L_0x009d;
            L_0x0067:
                r5 = r1.mPresentationTimeUs;	 Catch:{ all -> 0x00b4 }
                r7 = -1;
                r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
                if (r5 == 0) goto L_0x0078;
            L_0x006f:
                r5 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r9 = r1.mPresentationTimeUs;	 Catch:{ all -> 0x00b4 }
                r5.native_updateQueuedAudioData(r3, r9);	 Catch:{ all -> 0x00b4 }
                r1.mPresentationTimeUs = r7;	 Catch:{ all -> 0x00b4 }
            L_0x0078:
                if (r4 != r3) goto L_0x009d;
            L_0x007a:
                r5 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r5.postReturnByteBuffer(r1);	 Catch:{ all -> 0x00b4 }
                r5 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r5 = r5.mAudioBuffers;	 Catch:{ all -> 0x00b4 }
                r5.remove(r2);	 Catch:{ all -> 0x00b4 }
                r2 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r2 = r2.mAudioBuffers;	 Catch:{ all -> 0x00b4 }
                r2 = r2.isEmpty();	 Catch:{ all -> 0x00b4 }
                if (r2 != 0) goto L_0x009b;
            L_0x0094:
                r2 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r5 = 0;
                r2.postRenderAudio(r5);	 Catch:{ all -> 0x00b4 }
            L_0x009b:
                monitor-exit(r0);	 Catch:{ all -> 0x00b4 }
                return;
            L_0x009d:
                r2 = java.util.concurrent.TimeUnit.MICROSECONDS;	 Catch:{ all -> 0x00b4 }
                r5 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r5 = r5.native_getPlayTimeForPendingAudioFrames();	 Catch:{ all -> 0x00b4 }
                r5 = r2.toMillis(r5);	 Catch:{ all -> 0x00b4 }
                r2 = android.media.MediaSync.this;	 Catch:{ all -> 0x00b4 }
                r7 = 2;
                r7 = r5 / r7;
                r2.postRenderAudio(r7);	 Catch:{ all -> 0x00b4 }
                monitor-exit(r0);	 Catch:{ all -> 0x00b4 }
                return;
            L_0x00b4:
                r1 = move-exception;
                monitor-exit(r0);	 Catch:{ all -> 0x00b4 }
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.media.MediaSync$AnonymousClass1.run():void");
            }
        }, delayMillis);
    }

    private final void postReturnByteBuffer(final AudioBuffer audioBuffer) {
        synchronized (this.mCallbackLock) {
            if (this.mCallbackHandler != null) {
                this.mCallbackHandler.post(new Runnable() {
                    /* JADX WARNING: Missing block: B:9:0x002b, code skipped:
            if (r1 == null) goto L_0x003a;
     */
                    /* JADX WARNING: Missing block: B:10:0x002d, code skipped:
            r1.onAudioBufferConsumed(r1, r5.mByteBuffer, r5.mBufferIndex);
     */
                    /* JADX WARNING: Missing block: B:11:0x003a, code skipped:
            return;
     */
                    public void run() {
                        /*
                        r4 = this;
                        r0 = android.media.MediaSync.this;
                        r0 = r0.mCallbackLock;
                        monitor-enter(r0);
                        r1 = android.media.MediaSync.this;	 Catch:{ all -> 0x003d }
                        r1 = r1.mCallback;	 Catch:{ all -> 0x003d }
                        r2 = android.media.MediaSync.this;	 Catch:{ all -> 0x003d }
                        r2 = r2.mCallbackHandler;	 Catch:{ all -> 0x003d }
                        if (r2 == 0) goto L_0x003b;
                    L_0x0015:
                        r2 = android.media.MediaSync.this;	 Catch:{ all -> 0x003d }
                        r2 = r2.mCallbackHandler;	 Catch:{ all -> 0x003d }
                        r2 = r2.getLooper();	 Catch:{ all -> 0x003d }
                        r2 = r2.getThread();	 Catch:{ all -> 0x003d }
                        r3 = java.lang.Thread.currentThread();	 Catch:{ all -> 0x003d }
                        if (r2 == r3) goto L_0x002a;
                    L_0x0029:
                        goto L_0x003b;
                    L_0x002a:
                        monitor-exit(r0);	 Catch:{ all -> 0x003d }
                        if (r1 == 0) goto L_0x003a;
                    L_0x002d:
                        r0 = r1;
                        r2 = r5;
                        r2 = r2.mByteBuffer;
                        r3 = r5;
                        r3 = r3.mBufferIndex;
                        r1.onAudioBufferConsumed(r0, r2, r3);
                    L_0x003a:
                        return;
                    L_0x003b:
                        monitor-exit(r0);	 Catch:{ all -> 0x003d }
                        return;
                    L_0x003d:
                        r1 = move-exception;
                        monitor-exit(r0);	 Catch:{ all -> 0x003d }
                        throw r1;
                        */
                        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaSync$AnonymousClass2.run():void");
                    }
                });
            }
        }
    }

    private final void returnAudioBuffers() {
        synchronized (this.mAudioLock) {
            for (AudioBuffer audioBuffer : this.mAudioBuffers) {
                postReturnByteBuffer(audioBuffer);
            }
            this.mAudioBuffers.clear();
        }
    }

    private void createAudioThread() {
        this.mAudioThread = new Thread() {
            public void run() {
                Looper.prepare();
                synchronized (MediaSync.this.mAudioLock) {
                    MediaSync.this.mAudioLooper = Looper.myLooper();
                    MediaSync.this.mAudioHandler = new Handler();
                    MediaSync.this.mAudioLock.notify();
                }
                Looper.loop();
            }
        };
        this.mAudioThread.start();
        synchronized (this.mAudioLock) {
            try {
                this.mAudioLock.wait();
            } catch (InterruptedException e) {
            }
        }
    }

    static {
        System.loadLibrary("media_jni");
        native_init();
    }
}
