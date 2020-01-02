package android.media;

import android.annotation.UnsupportedAppUsage;
import android.media.VolumeShaper.Configuration;
import android.media.VolumeShaper.Operation;
import android.media.VolumeShaper.State;
import android.net.TrafficStats;
import android.opengl.GLES30;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.NioUtils;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Executor;

public class AudioTrack extends PlayerBase implements AudioRouting, VolumeAutomation {
    private static final int AUDIO_OUTPUT_FLAG_DEEP_BUFFER = 8;
    private static final int AUDIO_OUTPUT_FLAG_FAST = 4;
    public static final int ERROR = -1;
    public static final int ERROR_BAD_VALUE = -2;
    public static final int ERROR_DEAD_OBJECT = -6;
    public static final int ERROR_INVALID_OPERATION = -3;
    private static final int ERROR_NATIVESETUP_AUDIOSYSTEM = -16;
    private static final int ERROR_NATIVESETUP_INVALIDCHANNELMASK = -17;
    private static final int ERROR_NATIVESETUP_INVALIDFORMAT = -18;
    private static final int ERROR_NATIVESETUP_INVALIDSTREAMTYPE = -19;
    private static final int ERROR_NATIVESETUP_NATIVEINITFAILED = -20;
    public static final int ERROR_WOULD_BLOCK = -7;
    private static final float GAIN_MAX = 1.0f;
    private static final float GAIN_MIN = 0.0f;
    private static final float HEADER_V2_SIZE_BYTES = 20.0f;
    public static final int MODE_STATIC = 0;
    public static final int MODE_STREAM = 1;
    private static final int NATIVE_EVENT_CAN_WRITE_MORE_DATA = 9;
    private static final int NATIVE_EVENT_MARKER = 3;
    private static final int NATIVE_EVENT_NEW_IAUDIOTRACK = 6;
    private static final int NATIVE_EVENT_NEW_POS = 4;
    private static final int NATIVE_EVENT_STREAM_END = 7;
    public static final int PERFORMANCE_MODE_LOW_LATENCY = 1;
    public static final int PERFORMANCE_MODE_NONE = 0;
    public static final int PERFORMANCE_MODE_POWER_SAVING = 2;
    public static final int PLAYSTATE_PAUSED = 2;
    private static final int PLAYSTATE_PAUSED_STOPPING = 5;
    public static final int PLAYSTATE_PLAYING = 3;
    public static final int PLAYSTATE_STOPPED = 1;
    private static final int PLAYSTATE_STOPPING = 4;
    public static final int STATE_INITIALIZED = 1;
    public static final int STATE_NO_STATIC_DATA = 2;
    public static final int STATE_UNINITIALIZED = 0;
    public static final int SUCCESS = 0;
    private static final int SUPPORTED_OUT_CHANNELS = 7420;
    private static final String TAG = "android.media.AudioTrack";
    public static final int WRITE_BLOCKING = 0;
    public static final int WRITE_NON_BLOCKING = 1;
    private int mAudioFormat;
    private int mAvSyncBytesRemaining;
    private ByteBuffer mAvSyncHeader;
    private int mChannelConfiguration;
    private int mChannelCount;
    private int mChannelIndexMask;
    private int mChannelMask;
    private AudioAttributes mConfiguredAudioAttributes;
    private int mDataLoadMode;
    private NativePositionEventHandlerDelegate mEventHandlerDelegate;
    private final Looper mInitializationLooper;
    @UnsupportedAppUsage
    private long mJniData;
    private int mNativeBufferSizeInBytes;
    private int mNativeBufferSizeInFrames;
    @UnsupportedAppUsage
    protected long mNativeTrackInJavaObj;
    private int mOffloadDelayFrames;
    private boolean mOffloadEosPending;
    private int mOffloadPaddingFrames;
    private boolean mOffloaded;
    private int mOffset;
    private int mPlayState;
    private final Object mPlayStateLock;
    private AudioDeviceInfo mPreferredDevice;
    @GuardedBy({"mRoutingChangeListeners"})
    private ArrayMap<android.media.AudioRouting.OnRoutingChangedListener, NativeRoutingEventHandlerDelegate> mRoutingChangeListeners;
    private int mSampleRate;
    private int mSessionId;
    private int mState;
    @GuardedBy({"mStreamEventCbLock"})
    private LinkedList<StreamEventCbInfo> mStreamEventCbInfoList;
    private final Object mStreamEventCbLock;
    private volatile StreamEventHandler mStreamEventHandler;
    private HandlerThread mStreamEventHandlerThread;
    @UnsupportedAppUsage
    private int mStreamType;

    public static class Builder {
        private AudioAttributes mAttributes;
        private int mBufferSizeInBytes;
        private AudioFormat mFormat;
        private int mMode = 1;
        private boolean mOffload = false;
        private int mPerformanceMode = 0;
        private int mSessionId = 0;

        public Builder setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
            if (attributes != null) {
                this.mAttributes = attributes;
                return this;
            }
            throw new IllegalArgumentException("Illegal null AudioAttributes argument");
        }

        public Builder setAudioFormat(AudioFormat format) throws IllegalArgumentException {
            if (format != null) {
                this.mFormat = format;
                return this;
            }
            throw new IllegalArgumentException("Illegal null AudioFormat argument");
        }

        public Builder setBufferSizeInBytes(int bufferSizeInBytes) throws IllegalArgumentException {
            if (bufferSizeInBytes > 0) {
                this.mBufferSizeInBytes = bufferSizeInBytes;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid buffer size ");
            stringBuilder.append(bufferSizeInBytes);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder setTransferMode(int mode) throws IllegalArgumentException {
            if (mode == 0 || mode == 1) {
                this.mMode = mode;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid transfer mode ");
            stringBuilder.append(mode);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder setSessionId(int sessionId) throws IllegalArgumentException {
            if (sessionId == 0 || sessionId >= 1) {
                this.mSessionId = sessionId;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid audio session ID ");
            stringBuilder.append(sessionId);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder setPerformanceMode(int performanceMode) {
            if (performanceMode == 0 || performanceMode == 1 || performanceMode == 2) {
                this.mPerformanceMode = performanceMode;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid performance mode ");
            stringBuilder.append(performanceMode);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder setOffloadedPlayback(boolean offload) {
            this.mOffload = offload;
            return this;
        }

        /* JADX WARNING: Removed duplicated region for block: B:14:0x0068  */
        /* JADX WARNING: Removed duplicated region for block: B:17:0x0081  */
        /* JADX WARNING: Removed duplicated region for block: B:34:0x00d7 A:{Catch:{ IllegalArgumentException -> 0x00df }} */
        /* JADX WARNING: Removed duplicated region for block: B:33:0x00d6 A:{RETURN, Catch:{ IllegalArgumentException -> 0x00df }} */
        /* JADX WARNING: Missing block: B:7:0x001b, code skipped:
            if (r0 != 2) goto L_0x0064;
     */
        /* JADX WARNING: Missing block: B:10:0x0046, code skipped:
            if (android.media.AudioTrack.access$000(r9.mAttributes, r9.mFormat, r9.mBufferSizeInBytes, r9.mMode) == false) goto L_0x0064;
     */
        public android.media.AudioTrack build() throws java.lang.UnsupportedOperationException {
            /*
            r9 = this;
            r0 = r9.mAttributes;
            r1 = 1;
            if (r0 != 0) goto L_0x0014;
        L_0x0005:
            r0 = new android.media.AudioAttributes$Builder;
            r0.<init>();
            r0 = r0.setUsage(r1);
            r0 = r0.build();
            r9.mAttributes = r0;
        L_0x0014:
            r0 = r9.mPerformanceMode;
            if (r0 == 0) goto L_0x003a;
        L_0x0018:
            if (r0 == r1) goto L_0x001e;
        L_0x001a:
            r2 = 2;
            if (r0 == r2) goto L_0x0049;
        L_0x001d:
            goto L_0x0064;
        L_0x001e:
            r0 = new android.media.AudioAttributes$Builder;
            r2 = r9.mAttributes;
            r0.<init>(r2);
            r2 = r9.mAttributes;
            r2 = r2.getAllFlags();
            r2 = r2 | 256;
            r2 = r2 & -513;
            r0 = r0.replaceFlags(r2);
            r0 = r0.build();
            r9.mAttributes = r0;
            goto L_0x0064;
        L_0x003a:
            r0 = r9.mAttributes;
            r2 = r9.mFormat;
            r3 = r9.mBufferSizeInBytes;
            r4 = r9.mMode;
            r0 = android.media.AudioTrack.shouldEnablePowerSaving(r0, r2, r3, r4);
            if (r0 != 0) goto L_0x0049;
        L_0x0048:
            goto L_0x0064;
        L_0x0049:
            r0 = new android.media.AudioAttributes$Builder;
            r2 = r9.mAttributes;
            r0.<init>(r2);
            r2 = r9.mAttributes;
            r2 = r2.getAllFlags();
            r2 = r2 | 512;
            r2 = r2 & -257;
            r0 = r0.replaceFlags(r2);
            r0 = r0.build();
            r9.mAttributes = r0;
        L_0x0064:
            r0 = r9.mFormat;
            if (r0 != 0) goto L_0x007d;
        L_0x0068:
            r0 = new android.media.AudioFormat$Builder;
            r0.<init>();
            r2 = 12;
            r0 = r0.setChannelMask(r2);
            r0 = r0.setEncoding(r1);
            r0 = r0.build();
            r9.mFormat = r0;
        L_0x007d:
            r0 = r9.mOffload;
            if (r0 == 0) goto L_0x00a0;
        L_0x0081:
            r0 = r9.mPerformanceMode;
            if (r0 == r1) goto L_0x0098;
        L_0x0085:
            r0 = r9.mFormat;
            r2 = r9.mAttributes;
            r0 = android.media.AudioSystem.isOffloadSupported(r0, r2);
            if (r0 == 0) goto L_0x0090;
        L_0x008f:
            goto L_0x00a0;
        L_0x0090:
            r0 = new java.lang.UnsupportedOperationException;
            r1 = "Cannot create AudioTrack, offload format / attributes not supported";
            r0.<init>(r1);
            throw r0;
        L_0x0098:
            r0 = new java.lang.UnsupportedOperationException;
            r1 = "Offload and low latency modes are incompatible";
            r0.<init>(r1);
            throw r0;
        L_0x00a0:
            r0 = r9.mMode;	 Catch:{ IllegalArgumentException -> 0x00df }
            if (r0 != r1) goto L_0x00bd;
        L_0x00a4:
            r0 = r9.mBufferSizeInBytes;	 Catch:{ IllegalArgumentException -> 0x00df }
            if (r0 != 0) goto L_0x00bd;
        L_0x00a8:
            r0 = r9.mFormat;	 Catch:{ IllegalArgumentException -> 0x00df }
            r0 = r0.getChannelCount();	 Catch:{ IllegalArgumentException -> 0x00df }
            r1 = r9.mFormat;	 Catch:{ IllegalArgumentException -> 0x00df }
            r1 = r9.mFormat;	 Catch:{ IllegalArgumentException -> 0x00df }
            r1 = r1.getEncoding();	 Catch:{ IllegalArgumentException -> 0x00df }
            r1 = android.media.AudioFormat.getBytesPerSample(r1);	 Catch:{ IllegalArgumentException -> 0x00df }
            r0 = r0 * r1;
            r9.mBufferSizeInBytes = r0;	 Catch:{ IllegalArgumentException -> 0x00df }
        L_0x00bd:
            r0 = new android.media.AudioTrack;	 Catch:{ IllegalArgumentException -> 0x00df }
            r2 = r9.mAttributes;	 Catch:{ IllegalArgumentException -> 0x00df }
            r3 = r9.mFormat;	 Catch:{ IllegalArgumentException -> 0x00df }
            r4 = r9.mBufferSizeInBytes;	 Catch:{ IllegalArgumentException -> 0x00df }
            r5 = r9.mMode;	 Catch:{ IllegalArgumentException -> 0x00df }
            r6 = r9.mSessionId;	 Catch:{ IllegalArgumentException -> 0x00df }
            r7 = r9.mOffload;	 Catch:{ IllegalArgumentException -> 0x00df }
            r8 = 0;
            r1 = r0;
            r1.<init>(r2, r3, r4, r5, r6, r7, r8);	 Catch:{ IllegalArgumentException -> 0x00df }
            r1 = r0.getState();	 Catch:{ IllegalArgumentException -> 0x00df }
            if (r1 == 0) goto L_0x00d7;
        L_0x00d6:
            return r0;
        L_0x00d7:
            r1 = new java.lang.UnsupportedOperationException;	 Catch:{ IllegalArgumentException -> 0x00df }
            r2 = "Cannot create AudioTrack";
            r1.<init>(r2);	 Catch:{ IllegalArgumentException -> 0x00df }
            throw r1;	 Catch:{ IllegalArgumentException -> 0x00df }
        L_0x00df:
            r0 = move-exception;
            r1 = new java.lang.UnsupportedOperationException;
            r2 = r0.getMessage();
            r1.<init>(r2);
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.AudioTrack$Builder.build():android.media.AudioTrack");
        }
    }

    public static final class MetricsConstants {
        public static final String ATTRIBUTES = "android.media.audiotrack.attributes";
        @Deprecated
        public static final String CHANNELMASK = "android.media.audiorecord.channelmask";
        public static final String CHANNEL_MASK = "android.media.audiotrack.channelMask";
        public static final String CONTENTTYPE = "android.media.audiotrack.type";
        public static final String ENCODING = "android.media.audiotrack.encoding";
        public static final String FRAME_COUNT = "android.media.audiotrack.frameCount";
        private static final String MM_PREFIX = "android.media.audiotrack.";
        public static final String PORT_ID = "android.media.audiotrack.portId";
        @Deprecated
        public static final String SAMPLERATE = "android.media.audiorecord.samplerate";
        public static final String SAMPLE_RATE = "android.media.audiotrack.sampleRate";
        public static final String STREAMTYPE = "android.media.audiotrack.streamtype";
        public static final String USAGE = "android.media.audiotrack.usage";

        private MetricsConstants() {
        }
    }

    private class NativePositionEventHandlerDelegate {
        private final Handler mHandler;

        NativePositionEventHandlerDelegate(AudioTrack track, OnPlaybackPositionUpdateListener listener, Handler handler) {
            Looper looper;
            if (handler != null) {
                looper = handler.getLooper();
            } else {
                looper = AudioTrack.this.mInitializationLooper;
            }
            if (looper != null) {
                final AudioTrack audioTrack = AudioTrack.this;
                final AudioTrack audioTrack2 = track;
                final OnPlaybackPositionUpdateListener onPlaybackPositionUpdateListener = listener;
                this.mHandler = new Handler(looper) {
                    public void handleMessage(Message msg) {
                        if (audioTrack2 != null) {
                            int i = msg.what;
                            OnPlaybackPositionUpdateListener onPlaybackPositionUpdateListener;
                            if (i == 3) {
                                onPlaybackPositionUpdateListener = onPlaybackPositionUpdateListener;
                                if (onPlaybackPositionUpdateListener != null) {
                                    onPlaybackPositionUpdateListener.onMarkerReached(audioTrack2);
                                }
                            } else if (i != 4) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Unknown native event type: ");
                                stringBuilder.append(msg.what);
                                AudioTrack.loge(stringBuilder.toString());
                            } else {
                                onPlaybackPositionUpdateListener = onPlaybackPositionUpdateListener;
                                if (onPlaybackPositionUpdateListener != null) {
                                    onPlaybackPositionUpdateListener.onPeriodicNotification(audioTrack2);
                                }
                            }
                        }
                    }
                };
                return;
            }
            this.mHandler = null;
        }

        /* Access modifiers changed, original: 0000 */
        public Handler getHandler() {
            return this.mHandler;
        }
    }

    public interface OnPlaybackPositionUpdateListener {
        void onMarkerReached(AudioTrack audioTrack);

        void onPeriodicNotification(AudioTrack audioTrack);
    }

    @Deprecated
    public interface OnRoutingChangedListener extends android.media.AudioRouting.OnRoutingChangedListener {
        void onRoutingChanged(AudioTrack audioTrack);

        void onRoutingChanged(AudioRouting router) {
            if (router instanceof AudioTrack) {
                onRoutingChanged((AudioTrack) router);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface PerformanceMode {
    }

    public static abstract class StreamEventCallback {
        public void onTearDown(AudioTrack track) {
        }

        public void onPresentationEnded(AudioTrack track) {
        }

        public void onDataRequest(AudioTrack track, int sizeInFrames) {
        }
    }

    private static class StreamEventCbInfo {
        final StreamEventCallback mStreamEventCb;
        final Executor mStreamEventExec;

        StreamEventCbInfo(Executor e, StreamEventCallback cb) {
            this.mStreamEventExec = e;
            this.mStreamEventCb = cb;
        }
    }

    private class StreamEventHandler extends Handler {
        StreamEventHandler(Looper looper) {
            super(looper);
        }

        /* JADX WARNING: Missing block: B:27:0x006f, code skipped:
            r3 = android.os.Binder.clearCallingIdentity();
     */
        /* JADX WARNING: Missing block: B:29:?, code skipped:
            r0 = r1.iterator();
     */
        /* JADX WARNING: Missing block: B:31:0x007b, code skipped:
            if (r0.hasNext() == false) goto L_0x00b1;
     */
        /* JADX WARNING: Missing block: B:32:0x007d, code skipped:
            r5 = (android.media.AudioTrack.StreamEventCbInfo) r0.next();
            r6 = r9.what;
     */
        /* JADX WARNING: Missing block: B:33:0x0086, code skipped:
            if (r6 == 6) goto L_0x00a5;
     */
        /* JADX WARNING: Missing block: B:34:0x0088, code skipped:
            if (r6 == 7) goto L_0x009a;
     */
        /* JADX WARNING: Missing block: B:36:0x008c, code skipped:
            if (r6 == 9) goto L_0x008f;
     */
        /* JADX WARNING: Missing block: B:38:0x008f, code skipped:
            r5.mStreamEventExec.execute(new android.media.-$$Lambda$AudioTrack$StreamEventHandler$IUDediua4qA5AgKwU3zNCXA7jQo(r8, r5, r9));
     */
        /* JADX WARNING: Missing block: B:39:0x009a, code skipped:
            r5.mStreamEventExec.execute(new android.media.-$$Lambda$AudioTrack$StreamEventHandler$-3NLz6Sbq0z_YUytzGW6tVjPCao(r8, r5));
     */
        /* JADX WARNING: Missing block: B:40:0x00a5, code skipped:
            r5.mStreamEventExec.execute(new android.media.-$$Lambda$AudioTrack$StreamEventHandler$uWnWUbk1g3MhAY3NoSFc6o37wsk(r8, r5));
     */
        /* JADX WARNING: Missing block: B:44:0x00b7, code skipped:
            android.os.Binder.restoreCallingIdentity(r3);
     */
        public void handleMessage(android.os.Message r9) {
            /*
            r8 = this;
            r0 = android.media.AudioTrack.this;
            r0 = r0.mStreamEventCbLock;
            monitor-enter(r0);
            r1 = r9.what;	 Catch:{ all -> 0x00bb }
            r2 = 7;
            if (r1 != r2) goto L_0x0055;
        L_0x000c:
            r1 = android.media.AudioTrack.this;	 Catch:{ all -> 0x00bb }
            r1 = r1.mPlayStateLock;	 Catch:{ all -> 0x00bb }
            monitor-enter(r1);	 Catch:{ all -> 0x00bb }
            r3 = android.media.AudioTrack.this;	 Catch:{ all -> 0x0052 }
            r3 = r3.mPlayState;	 Catch:{ all -> 0x0052 }
            r4 = 4;
            if (r3 != r4) goto L_0x0050;
        L_0x001c:
            r3 = android.media.AudioTrack.this;	 Catch:{ all -> 0x0052 }
            r3 = r3.mOffloadEosPending;	 Catch:{ all -> 0x0052 }
            r4 = 0;
            if (r3 == 0) goto L_0x0031;
        L_0x0025:
            r3 = android.media.AudioTrack.this;	 Catch:{ all -> 0x0052 }
            r3.native_start();	 Catch:{ all -> 0x0052 }
            r3 = android.media.AudioTrack.this;	 Catch:{ all -> 0x0052 }
            r5 = 3;
            r3.mPlayState = r5;	 Catch:{ all -> 0x0052 }
            goto L_0x0042;
        L_0x0031:
            r3 = android.media.AudioTrack.this;	 Catch:{ all -> 0x0052 }
            r5 = 0;
            r3.mAvSyncHeader = r5;	 Catch:{ all -> 0x0052 }
            r3 = android.media.AudioTrack.this;	 Catch:{ all -> 0x0052 }
            r3.mAvSyncBytesRemaining = r4;	 Catch:{ all -> 0x0052 }
            r3 = android.media.AudioTrack.this;	 Catch:{ all -> 0x0052 }
            r5 = 1;
            r3.mPlayState = r5;	 Catch:{ all -> 0x0052 }
        L_0x0042:
            r3 = android.media.AudioTrack.this;	 Catch:{ all -> 0x0052 }
            r3.mOffloadEosPending = r4;	 Catch:{ all -> 0x0052 }
            r3 = android.media.AudioTrack.this;	 Catch:{ all -> 0x0052 }
            r3 = r3.mPlayStateLock;	 Catch:{ all -> 0x0052 }
            r3.notify();	 Catch:{ all -> 0x0052 }
        L_0x0050:
            monitor-exit(r1);	 Catch:{ all -> 0x0052 }
            goto L_0x0055;
        L_0x0052:
            r2 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0052 }
            throw r2;	 Catch:{ all -> 0x00bb }
        L_0x0055:
            r1 = android.media.AudioTrack.this;	 Catch:{ all -> 0x00bb }
            r1 = r1.mStreamEventCbInfoList;	 Catch:{ all -> 0x00bb }
            r1 = r1.size();	 Catch:{ all -> 0x00bb }
            if (r1 != 0) goto L_0x0063;
        L_0x0061:
            monitor-exit(r0);	 Catch:{ all -> 0x00bb }
            return;
        L_0x0063:
            r1 = new java.util.LinkedList;	 Catch:{ all -> 0x00bb }
            r3 = android.media.AudioTrack.this;	 Catch:{ all -> 0x00bb }
            r3 = r3.mStreamEventCbInfoList;	 Catch:{ all -> 0x00bb }
            r1.<init>(r3);	 Catch:{ all -> 0x00bb }
            monitor-exit(r0);	 Catch:{ all -> 0x00bb }
            r3 = android.os.Binder.clearCallingIdentity();
            r0 = r1.iterator();	 Catch:{ all -> 0x00b6 }
        L_0x0077:
            r5 = r0.hasNext();	 Catch:{ all -> 0x00b6 }
            if (r5 == 0) goto L_0x00b1;
        L_0x007d:
            r5 = r0.next();	 Catch:{ all -> 0x00b6 }
            r5 = (android.media.AudioTrack.StreamEventCbInfo) r5;	 Catch:{ all -> 0x00b6 }
            r6 = r9.what;	 Catch:{ all -> 0x00b6 }
            r7 = 6;
            if (r6 == r7) goto L_0x00a5;
        L_0x0088:
            if (r6 == r2) goto L_0x009a;
        L_0x008a:
            r7 = 9;
            if (r6 == r7) goto L_0x008f;
        L_0x008e:
            goto L_0x00b0;
        L_0x008f:
            r6 = r5.mStreamEventExec;	 Catch:{ all -> 0x00b6 }
            r7 = new android.media.-$$Lambda$AudioTrack$StreamEventHandler$IUDediua4qA5AgKwU3zNCXA7jQo;	 Catch:{ all -> 0x00b6 }
            r7.<init>(r8, r5, r9);	 Catch:{ all -> 0x00b6 }
            r6.execute(r7);	 Catch:{ all -> 0x00b6 }
            goto L_0x00b0;
        L_0x009a:
            r6 = r5.mStreamEventExec;	 Catch:{ all -> 0x00b6 }
            r7 = new android.media.-$$Lambda$AudioTrack$StreamEventHandler$-3NLz6Sbq0z_YUytzGW6tVjPCao;	 Catch:{ all -> 0x00b6 }
            r7.<init>(r8, r5);	 Catch:{ all -> 0x00b6 }
            r6.execute(r7);	 Catch:{ all -> 0x00b6 }
            goto L_0x00b0;
        L_0x00a5:
            r6 = r5.mStreamEventExec;	 Catch:{ all -> 0x00b6 }
            r7 = new android.media.-$$Lambda$AudioTrack$StreamEventHandler$uWnWUbk1g3MhAY3NoSFc6o37wsk;	 Catch:{ all -> 0x00b6 }
            r7.<init>(r8, r5);	 Catch:{ all -> 0x00b6 }
            r6.execute(r7);	 Catch:{ all -> 0x00b6 }
        L_0x00b0:
            goto L_0x0077;
        L_0x00b1:
            android.os.Binder.restoreCallingIdentity(r3);
            return;
        L_0x00b6:
            r0 = move-exception;
            android.os.Binder.restoreCallingIdentity(r3);
            throw r0;
        L_0x00bb:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x00bb }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.AudioTrack$StreamEventHandler.handleMessage(android.os.Message):void");
        }

        public /* synthetic */ void lambda$handleMessage$0$AudioTrack$StreamEventHandler(StreamEventCbInfo cbi, Message msg) {
            cbi.mStreamEventCb.onDataRequest(AudioTrack.this, msg.arg1);
        }

        public /* synthetic */ void lambda$handleMessage$1$AudioTrack$StreamEventHandler(StreamEventCbInfo cbi) {
            cbi.mStreamEventCb.onTearDown(AudioTrack.this);
        }

        public /* synthetic */ void lambda$handleMessage$2$AudioTrack$StreamEventHandler(StreamEventCbInfo cbi) {
            cbi.mStreamEventCb.onPresentationEnded(AudioTrack.this);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TransferMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WriteMode {
    }

    private native int native_applyVolumeShaper(Configuration configuration, Operation operation);

    private final native int native_attachAuxEffect(int i);

    private final native void native_disableDeviceCallback();

    private final native void native_enableDeviceCallback();

    private final native void native_finalize();

    private final native void native_flush();

    private native PersistableBundle native_getMetrics();

    private native int native_getPortId();

    private final native int native_getRoutedDeviceId();

    private native State native_getVolumeShaperState(int i);

    private final native int native_get_buffer_capacity_frames();

    private final native int native_get_buffer_size_frames();

    private final native int native_get_flags();

    private final native int native_get_latency();

    private final native int native_get_marker_pos();

    private static final native int native_get_min_buff_size(int i, int i2, int i3);

    private static final native int native_get_output_sample_rate(int i);

    private final native PlaybackParams native_get_playback_params();

    private final native int native_get_playback_rate();

    private final native int native_get_pos_update_period();

    private final native int native_get_position();

    private final native int native_get_timestamp(long[] jArr);

    private final native int native_get_underrun_count();

    private static native boolean native_is_direct_output_supported(int i, int i2, int i3, int i4, int i5, int i6, int i7);

    private final native void native_pause();

    private final native int native_reload_static();

    private final native int native_setAuxEffectSendLevel(float f);

    private final native boolean native_setOutputDevice(int i);

    private final native int native_setPresentation(int i, int i2);

    private final native void native_setVolume(float f, float f2);

    private final native int native_set_buffer_size_frames(int i);

    private native void native_set_delay_padding(int i, int i2);

    private final native int native_set_loop(int i, int i2, int i3);

    private final native int native_set_marker_pos(int i);

    private final native void native_set_playback_params(PlaybackParams playbackParams);

    private final native int native_set_playback_rate(int i);

    private final native int native_set_pos_update_period(int i);

    private final native int native_set_position(int i);

    private final native int native_setup(Object obj, Object obj2, int[] iArr, int i, int i2, int i3, int i4, int i5, int[] iArr2, long j, boolean z);

    private final native void native_start();

    private final native void native_stop();

    private final native int native_write_byte(byte[] bArr, int i, int i2, int i3, boolean z);

    private final native int native_write_float(float[] fArr, int i, int i2, int i3, boolean z);

    private final native int native_write_native_bytes(ByteBuffer byteBuffer, int i, int i2, int i3, boolean z);

    private final native int native_write_short(short[] sArr, int i, int i2, int i3, boolean z);

    public native String getParameters(String str);

    @UnsupportedAppUsage
    public final native void native_release();

    public native int setParameters(String str);

    public AudioTrack(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode) throws IllegalArgumentException {
        this(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSizeInBytes, mode, 0);
    }

    public AudioTrack(int streamType, int sampleRateInHz, int channelConfig, int audioFormat, int bufferSizeInBytes, int mode, int sessionId) throws IllegalArgumentException {
        this(new android.media.AudioAttributes.Builder().setLegacyStreamType(streamType).build(), new android.media.AudioFormat.Builder().setChannelMask(channelConfig).setEncoding(audioFormat).setSampleRate(sampleRateInHz).build(), bufferSizeInBytes, mode, sessionId);
        PlayerBase.deprecateStreamTypeForPlayback(streamType, "AudioTrack", "AudioTrack()");
    }

    public AudioTrack(AudioAttributes attributes, AudioFormat format, int bufferSizeInBytes, int mode, int sessionId) throws IllegalArgumentException {
        this(attributes, format, bufferSizeInBytes, mode, sessionId, false);
    }

    private AudioTrack(AudioAttributes attributes, AudioFormat format, int bufferSizeInBytes, int mode, int sessionId, boolean offload) throws IllegalArgumentException {
        AudioAttributes audioAttributes = attributes;
        AudioFormat audioFormat = format;
        int i = bufferSizeInBytes;
        int i2 = sessionId;
        super(audioAttributes, 1);
        this.mState = 0;
        this.mPlayState = 1;
        this.mOffloadEosPending = false;
        this.mPlayStateLock = new Object();
        this.mNativeBufferSizeInBytes = 0;
        this.mNativeBufferSizeInFrames = 0;
        this.mChannelCount = 1;
        this.mChannelMask = 4;
        this.mStreamType = 3;
        this.mDataLoadMode = 1;
        this.mChannelConfiguration = 4;
        this.mChannelIndexMask = 0;
        this.mSessionId = 0;
        this.mAvSyncHeader = null;
        this.mAvSyncBytesRemaining = 0;
        this.mOffset = 0;
        this.mOffloaded = false;
        this.mOffloadDelayFrames = 0;
        this.mOffloadPaddingFrames = 0;
        this.mPreferredDevice = null;
        this.mRoutingChangeListeners = new ArrayMap();
        this.mStreamEventCbLock = new Object();
        this.mStreamEventCbInfoList = new LinkedList();
        this.mConfiguredAudioAttributes = audioAttributes;
        int i3;
        if (audioFormat != null) {
            Looper looper;
            int rate;
            int channelIndexMask;
            int channelMask;
            int encoding;
            if (shouldEnablePowerSaving(this.mAttributes, audioFormat, i, mode)) {
                this.mAttributes = new android.media.AudioAttributes.Builder(this.mAttributes).replaceFlags((this.mAttributes.getAllFlags() | 512) & TrafficStats.TAG_NETWORK_STACK_RANGE_END).build();
            }
            Looper myLooper = Looper.myLooper();
            Looper looper2 = myLooper;
            if (myLooper == null) {
                looper = Looper.getMainLooper();
            } else {
                looper = looper2;
            }
            int rate2 = format.getSampleRate();
            if (rate2 == 0) {
                rate = 0;
            } else {
                rate = rate2;
            }
            if ((format.getPropertySetMask() & 8) != 0) {
                channelIndexMask = format.getChannelIndexMask();
            } else {
                channelIndexMask = 0;
            }
            if ((4 & format.getPropertySetMask()) != 0) {
                channelMask = format.getChannelMask();
            } else if (channelIndexMask == 0) {
                channelMask = 12;
            } else {
                channelMask = 0;
            }
            if ((format.getPropertySetMask() & 1) != 0) {
                encoding = format.getEncoding();
            } else {
                encoding = 1;
            }
            audioParamCheck(rate, channelMask, channelIndexMask, encoding, mode);
            this.mOffloaded = offload;
            this.mStreamType = -1;
            audioBuffSizeCheck(i);
            this.mInitializationLooper = looper;
            StringBuilder stringBuilder;
            if (i2 >= 0) {
                int[] sampleRate = new int[]{this.mSampleRate};
                int[] session = new int[]{i2};
                WeakReference weakReference = new WeakReference(this);
                AudioAttributes audioAttributes2 = this.mAttributes;
                int i4 = this.mChannelMask;
                int i5 = this.mChannelIndexMask;
                int i6 = this.mAudioFormat;
                int i7 = i5;
                int[] session2 = session;
                int i8 = i6;
                i3 = i2;
                i6 = native_setup(weakReference, audioAttributes2, sampleRate, i4, i7, i8, this.mNativeBufferSizeInBytes, this.mDataLoadMode, session2, 0, offload);
                if (i6 != 0) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Error code ");
                    stringBuilder.append(i6);
                    stringBuilder.append(" when initializing AudioTrack.");
                    loge(stringBuilder.toString());
                    return;
                }
                this.mSampleRate = sampleRate[0];
                this.mSessionId = session2[0];
                if ((this.mAttributes.getFlags() & 16) != 0) {
                    if (AudioFormat.isEncodingLinearFrames(this.mAudioFormat)) {
                        rate2 = this.mChannelCount * AudioFormat.getBytesPerSample(this.mAudioFormat);
                    } else {
                        rate2 = 1;
                    }
                    this.mOffset = ((int) Math.ceil((double) (HEADER_V2_SIZE_BYTES / ((float) rate2)))) * rate2;
                }
                if (this.mDataLoadMode == 0) {
                    this.mState = 2;
                } else {
                    this.mState = 1;
                }
                baseRegisterPlayer();
                return;
            }
            i3 = i2;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid audio session ID: ");
            stringBuilder.append(i3);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        i3 = i2;
        throw new IllegalArgumentException("Illegal null AudioFormat");
    }

    AudioTrack(long nativeTrackInJavaObj) {
        super(new android.media.AudioAttributes.Builder().build(), 1);
        this.mState = 0;
        this.mPlayState = 1;
        this.mOffloadEosPending = false;
        this.mPlayStateLock = new Object();
        this.mNativeBufferSizeInBytes = 0;
        this.mNativeBufferSizeInFrames = 0;
        this.mChannelCount = 1;
        this.mChannelMask = 4;
        this.mStreamType = 3;
        this.mDataLoadMode = 1;
        this.mChannelConfiguration = 4;
        this.mChannelIndexMask = 0;
        this.mSessionId = 0;
        this.mAvSyncHeader = null;
        this.mAvSyncBytesRemaining = 0;
        this.mOffset = 0;
        this.mOffloaded = false;
        this.mOffloadDelayFrames = 0;
        this.mOffloadPaddingFrames = 0;
        this.mPreferredDevice = null;
        this.mRoutingChangeListeners = new ArrayMap();
        this.mStreamEventCbLock = new Object();
        this.mStreamEventCbInfoList = new LinkedList();
        this.mNativeTrackInJavaObj = 0;
        this.mJniData = 0;
        Looper myLooper = Looper.myLooper();
        Looper looper = myLooper;
        if (myLooper == null) {
            looper = Looper.getMainLooper();
        }
        this.mInitializationLooper = looper;
        if (nativeTrackInJavaObj != 0) {
            baseRegisterPlayer();
            deferred_connect(nativeTrackInJavaObj);
            return;
        }
        this.mState = 0;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void deferred_connect(long nativeTrackInJavaObj) {
        if (this.mState != 1) {
            int[] session = new int[]{0};
            int initResult = native_setup(new WeakReference(this), null, new int[]{0}, 0, 0, 0, 0, 0, session, nativeTrackInJavaObj, false);
            if (initResult != 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error code ");
                stringBuilder.append(initResult);
                stringBuilder.append(" when initializing AudioTrack.");
                loge(stringBuilder.toString());
                return;
            }
            this.mSessionId = session[0];
            this.mState = 1;
        }
    }

    public void setOffloadDelayPadding(int delayInFrames, int paddingInFrames) {
        if (paddingInFrames < 0) {
            throw new IllegalArgumentException("Illegal negative padding");
        } else if (delayInFrames < 0) {
            throw new IllegalArgumentException("Illegal negative delay");
        } else if (!this.mOffloaded) {
            throw new IllegalStateException("Illegal use of delay/padding on non-offloaded track");
        } else if (this.mState != 0) {
            this.mOffloadDelayFrames = delayInFrames;
            this.mOffloadPaddingFrames = paddingInFrames;
            native_set_delay_padding(delayInFrames, paddingInFrames);
        } else {
            throw new IllegalStateException("Uninitialized track");
        }
    }

    public int getOffloadDelay() {
        if (!this.mOffloaded) {
            throw new IllegalStateException("Illegal query of delay on non-offloaded track");
        } else if (this.mState != 0) {
            return this.mOffloadDelayFrames;
        } else {
            throw new IllegalStateException("Illegal query of delay on uninitialized track");
        }
    }

    public int getOffloadPadding() {
        if (!this.mOffloaded) {
            throw new IllegalStateException("Illegal query of padding on non-offloaded track");
        } else if (this.mState != 0) {
            return this.mOffloadPaddingFrames;
        } else {
            throw new IllegalStateException("Illegal query of padding on uninitialized track");
        }
    }

    public void setOffloadEndOfStream() {
        if (!this.mOffloaded) {
            throw new IllegalStateException("EOS not supported on non-offloaded track");
        } else if (this.mState == 0) {
            throw new IllegalStateException("Uninitialized track");
        } else if (this.mPlayState == 3) {
            synchronized (this.mStreamEventCbLock) {
                if (this.mStreamEventCbInfoList.size() != 0) {
                } else {
                    throw new IllegalStateException("EOS not supported without StreamEventCallback");
                }
            }
            synchronized (this.mPlayStateLock) {
                native_stop();
                this.mOffloadEosPending = true;
                this.mPlayState = 4;
            }
        } else {
            throw new IllegalStateException("EOS not supported if not playing");
        }
    }

    public boolean isOffloadedPlayback() {
        return this.mOffloaded;
    }

    public static boolean isDirectPlaybackSupported(AudioFormat format, AudioAttributes attributes) {
        if (format == null) {
            throw new IllegalArgumentException("Illegal null AudioFormat argument");
        } else if (attributes != null) {
            return native_is_direct_output_supported(format.getEncoding(), format.getSampleRate(), format.getChannelMask(), format.getChannelIndexMask(), attributes.getContentType(), attributes.getUsage(), attributes.getFlags());
        } else {
            throw new IllegalArgumentException("Illegal null AudioAttributes argument");
        }
    }

    /* JADX WARNING: Missing block: B:12:0x0024, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:29:0x0073, code skipped:
            return false;
     */
    private static boolean shouldEnablePowerSaving(android.media.AudioAttributes r9, android.media.AudioFormat r10, int r11, int r12) {
        /*
        r0 = 1;
        r1 = 0;
        if (r9 == 0) goto L_0x0025;
    L_0x0004:
        r2 = r9.getAllFlags();
        if (r2 != 0) goto L_0x0024;
    L_0x000a:
        r2 = r9.getUsage();
        if (r2 != r0) goto L_0x0024;
    L_0x0010:
        r2 = r9.getContentType();
        if (r2 == 0) goto L_0x0025;
    L_0x0016:
        r2 = r9.getContentType();
        r3 = 2;
        if (r2 == r3) goto L_0x0025;
    L_0x001d:
        r2 = r9.getContentType();
        r3 = 3;
        if (r2 == r3) goto L_0x0025;
    L_0x0024:
        return r1;
    L_0x0025:
        if (r10 == 0) goto L_0x0073;
    L_0x0027:
        r2 = r10.getSampleRate();
        if (r2 == 0) goto L_0x0073;
    L_0x002d:
        r2 = r10.getEncoding();
        r2 = android.media.AudioFormat.isEncodingLinearPcm(r2);
        if (r2 == 0) goto L_0x0073;
    L_0x0037:
        r2 = r10.getEncoding();
        r2 = android.media.AudioFormat.isValidEncoding(r2);
        if (r2 == 0) goto L_0x0073;
    L_0x0041:
        r2 = r10.getChannelCount();
        if (r2 >= r0) goto L_0x0048;
    L_0x0047:
        goto L_0x0073;
    L_0x0048:
        if (r12 == r0) goto L_0x004b;
    L_0x004a:
        return r1;
    L_0x004b:
        if (r11 == 0) goto L_0x0072;
    L_0x004d:
        r2 = 100;
        r4 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r5 = 100;
        r7 = r10.getChannelCount();
        r7 = (long) r7;
        r7 = r7 * r5;
        r5 = r10.getEncoding();
        r5 = android.media.AudioFormat.getBytesPerSample(r5);
        r5 = (long) r5;
        r7 = r7 * r5;
        r5 = r10.getSampleRate();
        r5 = (long) r5;
        r7 = r7 * r5;
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r7 = r7 / r5;
        r5 = (long) r11;
        r5 = (r5 > r7 ? 1 : (r5 == r7 ? 0 : -1));
        if (r5 >= 0) goto L_0x0072;
    L_0x0071:
        return r1;
    L_0x0072:
        return r0;
    L_0x0073:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioTrack.shouldEnablePowerSaving(android.media.AudioAttributes, android.media.AudioFormat, int, int):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0095  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0095  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0095  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x009c  */
    private void audioParamCheck(int r6, int r7, int r8, int r9, int r10) {
        /*
        r5 = this;
        r0 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
        if (r6 < r0) goto L_0x0009;
    L_0x0004:
        r0 = 192000; // 0x2ee00 float:2.6905E-40 double:9.48606E-319;
        if (r6 <= r0) goto L_0x000b;
    L_0x0009:
        if (r6 != 0) goto L_0x00c0;
    L_0x000b:
        r5.mSampleRate = r6;
        r0 = 13;
        r1 = 12;
        if (r9 != r0) goto L_0x001e;
    L_0x0013:
        if (r7 != r1) goto L_0x0016;
    L_0x0015:
        goto L_0x001e;
    L_0x0016:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "ENCODING_IEC61937 must be configured as CHANNEL_OUT_STEREO";
        r0.<init>(r1);
        throw r0;
    L_0x001e:
        r5.mChannelConfiguration = r7;
        r0 = 4;
        r2 = 1;
        if (r7 == r2) goto L_0x0052;
    L_0x0024:
        r3 = 2;
        if (r7 == r3) goto L_0x0052;
    L_0x0027:
        r4 = 3;
        if (r7 == r4) goto L_0x004d;
    L_0x002a:
        if (r7 == r0) goto L_0x0052;
    L_0x002c:
        if (r7 == r1) goto L_0x004d;
    L_0x002e:
        if (r7 != 0) goto L_0x0036;
    L_0x0030:
        if (r8 == 0) goto L_0x0036;
    L_0x0032:
        r0 = 0;
        r5.mChannelCount = r0;
        goto L_0x0057;
    L_0x0036:
        r0 = isMultichannelConfigSupported(r7);
        if (r0 == 0) goto L_0x0045;
    L_0x003c:
        r5.mChannelMask = r7;
        r0 = android.media.AudioFormat.channelCountFromOutChannelMask(r7);
        r5.mChannelCount = r0;
        goto L_0x0057;
    L_0x0045:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Unsupported channel configuration.";
        r0.<init>(r1);
        throw r0;
    L_0x004d:
        r5.mChannelCount = r3;
        r5.mChannelMask = r1;
        goto L_0x0057;
    L_0x0052:
        r5.mChannelCount = r2;
        r5.mChannelMask = r0;
    L_0x0057:
        r5.mChannelIndexMask = r8;
        r0 = r5.mChannelIndexMask;
        if (r0 == 0) goto L_0x0093;
    L_0x005d:
        r0 = android.media.AudioSystem.OUT_CHANNEL_COUNT_MAX;
        r0 = r2 << r0;
        r0 = r0 - r2;
        r1 = ~r0;
        r1 = r1 & r8;
        if (r1 != 0) goto L_0x007c;
    L_0x0066:
        r1 = java.lang.Integer.bitCount(r8);
        r3 = r5.mChannelCount;
        if (r3 != 0) goto L_0x0071;
    L_0x006e:
        r5.mChannelCount = r1;
        goto L_0x0093;
    L_0x0071:
        if (r3 != r1) goto L_0x0074;
    L_0x0073:
        goto L_0x0093;
    L_0x0074:
        r2 = new java.lang.IllegalArgumentException;
        r3 = "Channel count must match";
        r2.<init>(r3);
        throw r2;
    L_0x007c:
        r1 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Unsupported channel index configuration ";
        r2.append(r3);
        r2.append(r8);
        r2 = r2.toString();
        r1.<init>(r2);
        throw r1;
    L_0x0093:
        if (r9 != r2) goto L_0x0096;
    L_0x0095:
        r9 = 2;
    L_0x0096:
        r0 = android.media.AudioFormat.isPublicEncoding(r9);
        if (r0 == 0) goto L_0x00b8;
    L_0x009c:
        r5.mAudioFormat = r9;
        if (r10 == r2) goto L_0x00a2;
    L_0x00a0:
        if (r10 != 0) goto L_0x00ad;
    L_0x00a2:
        if (r10 == r2) goto L_0x00b5;
    L_0x00a4:
        r0 = r5.mAudioFormat;
        r0 = android.media.AudioFormat.isEncodingLinearPcm(r0);
        if (r0 == 0) goto L_0x00ad;
    L_0x00ac:
        goto L_0x00b5;
    L_0x00ad:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Invalid mode.";
        r0.<init>(r1);
        throw r0;
    L_0x00b5:
        r5.mDataLoadMode = r10;
        return;
    L_0x00b8:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Unsupported audio encoding.";
        r0.<init>(r1);
        throw r0;
    L_0x00c0:
        r0 = new java.lang.IllegalArgumentException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r6);
        r2 = "Hz is not a supported sample rate.";
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioTrack.audioParamCheck(int, int, int, int, int):void");
    }

    private static boolean isMultichannelConfigSupported(int channelConfig) {
        if ((channelConfig & SUPPORTED_OUT_CHANNELS) != channelConfig) {
            loge("Channel configuration features unsupported channels");
            return false;
        }
        int channelCount = AudioFormat.channelCountFromOutChannelMask(channelConfig);
        if (channelCount > AudioSystem.OUT_CHANNEL_COUNT_MAX) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Channel configuration contains too many channels ");
            stringBuilder.append(channelCount);
            stringBuilder.append(">");
            stringBuilder.append(AudioSystem.OUT_CHANNEL_COUNT_MAX);
            loge(stringBuilder.toString());
            return false;
        } else if ((channelConfig & 12) != 12) {
            loge("Front channels must be present in multichannel configurations");
            return false;
        } else if ((channelConfig & 192) != 0 && (channelConfig & 192) != 192) {
            loge("Rear channels can't be used independently");
            return false;
        } else if ((channelConfig & GLES30.GL_COLOR) == 0 || (channelConfig & GLES30.GL_COLOR) == GLES30.GL_COLOR) {
            return true;
        } else {
            loge("Side channels can't be used independently");
            return false;
        }
    }

    private void audioBuffSizeCheck(int audioBufferSize) {
        int frameSizeInBytes;
        if (AudioFormat.isEncodingLinearFrames(this.mAudioFormat)) {
            frameSizeInBytes = this.mChannelCount * AudioFormat.getBytesPerSample(this.mAudioFormat);
        } else {
            frameSizeInBytes = 1;
        }
        if (audioBufferSize % frameSizeInBytes != 0 || audioBufferSize < 1) {
            throw new IllegalArgumentException("Invalid audio buffer size.");
        }
        this.mNativeBufferSizeInBytes = audioBufferSize;
        this.mNativeBufferSizeInFrames = audioBufferSize / frameSizeInBytes;
    }

    public void release() {
        synchronized (this.mStreamEventCbLock) {
            endStreamEventHandling();
        }
        try {
            stop();
        } catch (IllegalStateException e) {
        }
        baseRelease();
        native_release();
        synchronized (this.mPlayStateLock) {
            this.mState = 0;
            this.mPlayState = 1;
            this.mPlayStateLock.notify();
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        baseRelease();
        native_finalize();
    }

    public static float getMinVolume() {
        return 0.0f;
    }

    public static float getMaxVolume() {
        return 1.0f;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public int getPlaybackRate() {
        return native_get_playback_rate();
    }

    public PlaybackParams getPlaybackParams() {
        return native_get_playback_params();
    }

    public AudioAttributes getAudioAttributes() {
        if (this.mState != 0) {
            AudioAttributes audioAttributes = this.mConfiguredAudioAttributes;
            if (audioAttributes != null) {
                return audioAttributes;
            }
        }
        throw new IllegalStateException("track not initialized");
    }

    public int getAudioFormat() {
        return this.mAudioFormat;
    }

    public int getStreamType() {
        return this.mStreamType;
    }

    public int getChannelConfiguration() {
        return this.mChannelConfiguration;
    }

    public AudioFormat getFormat() {
        android.media.AudioFormat.Builder builder = new android.media.AudioFormat.Builder().setSampleRate(this.mSampleRate).setEncoding(this.mAudioFormat);
        int i = this.mChannelConfiguration;
        if (i != 0) {
            builder.setChannelMask(i);
        }
        i = this.mChannelIndexMask;
        if (i != 0) {
            builder.setChannelIndexMask(i);
        }
        return builder.build();
    }

    public int getChannelCount() {
        return this.mChannelCount;
    }

    public int getState() {
        return this.mState;
    }

    public int getPlayState() {
        synchronized (this.mPlayStateLock) {
            int i = this.mPlayState;
            if (i == 4) {
                return 3;
            } else if (i != 5) {
                i = this.mPlayState;
                return i;
            } else {
                return 2;
            }
        }
    }

    public int getBufferSizeInFrames() {
        return native_get_buffer_size_frames();
    }

    public int setBufferSizeInFrames(int bufferSizeInFrames) {
        if (this.mDataLoadMode == 0 || this.mState == 0) {
            return -3;
        }
        if (bufferSizeInFrames < 0) {
            return -2;
        }
        return native_set_buffer_size_frames(bufferSizeInFrames);
    }

    public int getBufferCapacityInFrames() {
        return native_get_buffer_capacity_frames();
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public int getNativeFrameCount() {
        return native_get_buffer_capacity_frames();
    }

    public int getNotificationMarkerPosition() {
        return native_get_marker_pos();
    }

    public int getPositionNotificationPeriod() {
        return native_get_pos_update_period();
    }

    public int getPlaybackHeadPosition() {
        return native_get_position();
    }

    @UnsupportedAppUsage(trackingBug = 130237544)
    public int getLatency() {
        return native_get_latency();
    }

    public int getUnderrunCount() {
        return native_get_underrun_count();
    }

    public int getPerformanceMode() {
        int flags = native_get_flags();
        if ((flags & 4) != 0) {
            return 1;
        }
        if ((flags & 8) != 0) {
            return 2;
        }
        return 0;
    }

    public static int getNativeOutputSampleRate(int streamType) {
        return native_get_output_sample_rate(streamType);
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x0032  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x002b  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0032  */
    public static int getMinBufferSize(int r4, int r5, int r6) {
        /*
        r0 = 0;
        r1 = 2;
        r2 = -2;
        if (r5 == r1) goto L_0x0023;
    L_0x0005:
        r1 = 3;
        if (r5 == r1) goto L_0x0021;
    L_0x0008:
        r1 = 4;
        if (r5 == r1) goto L_0x0023;
    L_0x000b:
        r1 = 12;
        if (r5 == r1) goto L_0x0021;
    L_0x000f:
        r1 = isMultichannelConfigSupported(r5);
        if (r1 != 0) goto L_0x001c;
    L_0x0015:
        r1 = "getMinBufferSize(): Invalid channel configuration.";
        loge(r1);
        return r2;
    L_0x001c:
        r0 = android.media.AudioFormat.channelCountFromOutChannelMask(r5);
        goto L_0x0025;
    L_0x0021:
        r0 = 2;
        goto L_0x0025;
    L_0x0023:
        r0 = 1;
    L_0x0025:
        r1 = android.media.AudioFormat.isPublicEncoding(r6);
        if (r1 != 0) goto L_0x0032;
    L_0x002b:
        r1 = "getMinBufferSize(): Invalid audio format.";
        loge(r1);
        return r2;
    L_0x0032:
        r1 = 4000; // 0xfa0 float:5.605E-42 double:1.9763E-320;
        if (r4 < r1) goto L_0x004b;
    L_0x0036:
        r1 = 192000; // 0x2ee00 float:2.6905E-40 double:9.48606E-319;
        if (r4 <= r1) goto L_0x003c;
    L_0x003b:
        goto L_0x004b;
    L_0x003c:
        r1 = native_get_min_buff_size(r4, r0, r6);
        if (r1 > 0) goto L_0x004a;
    L_0x0042:
        r2 = "getMinBufferSize(): error querying hardware";
        loge(r2);
        r2 = -1;
        return r2;
    L_0x004a:
        return r1;
    L_0x004b:
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r3 = "getMinBufferSize(): ";
        r1.append(r3);
        r1.append(r4);
        r3 = " Hz is not a supported sample rate.";
        r1.append(r3);
        r1 = r1.toString();
        loge(r1);
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioTrack.getMinBufferSize(int, int, int):int");
    }

    public int getAudioSessionId() {
        return this.mSessionId;
    }

    public boolean getTimestamp(AudioTimestamp timestamp) {
        if (timestamp != null) {
            long[] longArray = new long[2];
            if (native_get_timestamp(longArray) != 0) {
                return false;
            }
            timestamp.framePosition = longArray[0];
            timestamp.nanoTime = longArray[1];
            return true;
        }
        throw new IllegalArgumentException();
    }

    public int getTimestampWithStatus(AudioTimestamp timestamp) {
        if (timestamp != null) {
            long[] longArray = new long[2];
            int ret = native_get_timestamp(longArray);
            timestamp.framePosition = longArray[0];
            timestamp.nanoTime = longArray[1];
            return ret;
        }
        throw new IllegalArgumentException();
    }

    public PersistableBundle getMetrics() {
        return native_getMetrics();
    }

    public void setPlaybackPositionUpdateListener(OnPlaybackPositionUpdateListener listener) {
        setPlaybackPositionUpdateListener(listener, null);
    }

    public void setPlaybackPositionUpdateListener(OnPlaybackPositionUpdateListener listener, Handler handler) {
        if (listener != null) {
            this.mEventHandlerDelegate = new NativePositionEventHandlerDelegate(this, listener, handler);
        } else {
            this.mEventHandlerDelegate = null;
        }
    }

    private static float clampGainOrLevel(float gainOrLevel) {
        if (Float.isNaN(gainOrLevel)) {
            throw new IllegalArgumentException();
        } else if (gainOrLevel < 0.0f) {
            return 0.0f;
        } else {
            if (gainOrLevel > 1.0f) {
                return 1.0f;
            }
            return gainOrLevel;
        }
    }

    @Deprecated
    public int setStereoVolume(float leftGain, float rightGain) {
        if (this.mState == 0) {
            return -3;
        }
        baseSetVolume(leftGain, rightGain);
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void playerSetVolume(boolean muting, float leftVolume, float rightVolume) {
        float f = 0.0f;
        leftVolume = clampGainOrLevel(muting ? 0.0f : leftVolume);
        if (!muting) {
            f = rightVolume;
        }
        native_setVolume(leftVolume, clampGainOrLevel(f));
    }

    public int setVolume(float gain) {
        return setStereoVolume(gain, gain);
    }

    /* Access modifiers changed, original: 0000 */
    public int playerApplyVolumeShaper(Configuration configuration, Operation operation) {
        return native_applyVolumeShaper(configuration, operation);
    }

    /* Access modifiers changed, original: 0000 */
    public State playerGetVolumeShaperState(int id) {
        return native_getVolumeShaperState(id);
    }

    public VolumeShaper createVolumeShaper(Configuration configuration) {
        return new VolumeShaper(configuration, this);
    }

    public int setPlaybackRate(int sampleRateInHz) {
        if (this.mState != 1) {
            return -3;
        }
        if (sampleRateInHz <= 0) {
            return -2;
        }
        return native_set_playback_rate(sampleRateInHz);
    }

    public void setPlaybackParams(PlaybackParams params) {
        if (params != null) {
            native_set_playback_params(params);
            return;
        }
        throw new IllegalArgumentException("params is null");
    }

    public int setNotificationMarkerPosition(int markerInFrames) {
        if (this.mState == 0) {
            return -3;
        }
        return native_set_marker_pos(markerInFrames);
    }

    public int setPositionNotificationPeriod(int periodInFrames) {
        if (this.mState == 0) {
            return -3;
        }
        return native_set_pos_update_period(periodInFrames);
    }

    public int setPlaybackHeadPosition(int positionInFrames) {
        if (this.mDataLoadMode == 1 || this.mState == 0 || getPlayState() == 3) {
            return -3;
        }
        if (positionInFrames < 0 || positionInFrames > this.mNativeBufferSizeInFrames) {
            return -2;
        }
        return native_set_position(positionInFrames);
    }

    /* JADX WARNING: Missing block: B:11:0x001c, code skipped:
            if (r4 <= r0) goto L_0x001f;
     */
    public int setLoopPoints(int r3, int r4, int r5) {
        /*
        r2 = this;
        r0 = r2.mDataLoadMode;
        r1 = 1;
        if (r0 == r1) goto L_0x0026;
    L_0x0005:
        r0 = r2.mState;
        if (r0 == 0) goto L_0x0026;
    L_0x0009:
        r0 = r2.getPlayState();
        r1 = 3;
        if (r0 != r1) goto L_0x0011;
    L_0x0010:
        goto L_0x0026;
    L_0x0011:
        if (r5 != 0) goto L_0x0014;
    L_0x0013:
        goto L_0x001f;
    L_0x0014:
        if (r3 < 0) goto L_0x0024;
    L_0x0016:
        r0 = r2.mNativeBufferSizeInFrames;
        if (r3 >= r0) goto L_0x0024;
    L_0x001a:
        if (r3 >= r4) goto L_0x0024;
    L_0x001c:
        if (r4 <= r0) goto L_0x001f;
    L_0x001e:
        goto L_0x0024;
    L_0x001f:
        r0 = r2.native_set_loop(r3, r4, r5);
        return r0;
    L_0x0024:
        r0 = -2;
        return r0;
    L_0x0026:
        r0 = -3;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioTrack.setLoopPoints(int, int, int):int");
    }

    public int setPresentation(AudioPresentation presentation) {
        if (presentation != null) {
            return native_setPresentation(presentation.getPresentationId(), presentation.getProgramId());
        }
        throw new IllegalArgumentException("audio presentation is null");
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public void setState(int state) {
        this.mState = state;
    }

    public void play() throws IllegalStateException {
        if (this.mState == 1) {
            final int delay = getStartDelayMs();
            if (delay == 0) {
                startImpl();
                return;
            } else {
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep((long) delay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        AudioTrack.this.baseSetStartDelayMs(0);
                        try {
                            AudioTrack.this.startImpl();
                        } catch (IllegalStateException e2) {
                        }
                    }
                }.start();
                return;
            }
        }
        throw new IllegalStateException("play() called on uninitialized AudioTrack.");
    }

    private void startImpl() {
        synchronized (this.mPlayStateLock) {
            baseStart();
            native_start();
            if (this.mPlayState == 5) {
                this.mPlayState = 4;
            } else {
                this.mPlayState = 3;
                this.mOffloadEosPending = false;
            }
        }
    }

    public void stop() throws IllegalStateException {
        if (this.mState == 1) {
            synchronized (this.mPlayStateLock) {
                native_stop();
                baseStop();
                if (!this.mOffloaded || this.mPlayState == 5) {
                    this.mPlayState = 1;
                    this.mOffloadEosPending = false;
                    this.mAvSyncHeader = null;
                    this.mAvSyncBytesRemaining = 0;
                    this.mPlayStateLock.notify();
                } else {
                    this.mPlayState = 4;
                }
            }
            return;
        }
        throw new IllegalStateException("stop() called on uninitialized AudioTrack.");
    }

    public void pause() throws IllegalStateException {
        if (this.mState == 1) {
            synchronized (this.mPlayStateLock) {
                native_pause();
                basePause();
                if (this.mPlayState == 4) {
                    this.mPlayState = 5;
                } else {
                    this.mPlayState = 2;
                }
            }
            return;
        }
        throw new IllegalStateException("pause() called on uninitialized AudioTrack.");
    }

    public void flush() {
        if (this.mState == 1) {
            native_flush();
            this.mAvSyncHeader = null;
            this.mAvSyncBytesRemaining = 0;
        }
    }

    public int write(byte[] audioData, int offsetInBytes, int sizeInBytes) {
        return write(audioData, offsetInBytes, sizeInBytes, 0);
    }

    public int write(byte[] audioData, int offsetInBytes, int sizeInBytes, int writeMode) {
        if (this.mState == 0 || this.mAudioFormat == 4) {
            return -3;
        }
        if (writeMode != 0 && writeMode != 1) {
            Log.e(TAG, "AudioTrack.write() called with invalid blocking mode");
            return -2;
        } else if (audioData == null || offsetInBytes < 0 || sizeInBytes < 0 || offsetInBytes + sizeInBytes < 0 || offsetInBytes + sizeInBytes > audioData.length) {
            return -2;
        } else {
            if (!blockUntilOffloadDrain(writeMode)) {
                return 0;
            }
            int ret = native_write_byte(audioData, offsetInBytes, sizeInBytes, this.mAudioFormat, writeMode == 0);
            if (this.mDataLoadMode == 0 && this.mState == 2 && ret > 0) {
                this.mState = 1;
            }
            return ret;
        }
    }

    public int write(short[] audioData, int offsetInShorts, int sizeInShorts) {
        return write(audioData, offsetInShorts, sizeInShorts, 0);
    }

    public int write(short[] audioData, int offsetInShorts, int sizeInShorts, int writeMode) {
        if (this.mState == 0 || this.mAudioFormat == 4) {
            return -3;
        }
        if (writeMode != 0 && writeMode != 1) {
            Log.e(TAG, "AudioTrack.write() called with invalid blocking mode");
            return -2;
        } else if (audioData == null || offsetInShorts < 0 || sizeInShorts < 0 || offsetInShorts + sizeInShorts < 0 || offsetInShorts + sizeInShorts > audioData.length) {
            return -2;
        } else {
            if (!blockUntilOffloadDrain(writeMode)) {
                return 0;
            }
            int ret = native_write_short(audioData, offsetInShorts, sizeInShorts, this.mAudioFormat, writeMode == 0);
            if (this.mDataLoadMode == 0 && this.mState == 2 && ret > 0) {
                this.mState = 1;
            }
            return ret;
        }
    }

    public int write(float[] audioData, int offsetInFloats, int sizeInFloats, int writeMode) {
        int i = this.mState;
        String str = TAG;
        if (i == 0) {
            Log.e(str, "AudioTrack.write() called in invalid state STATE_UNINITIALIZED");
            return -3;
        } else if (this.mAudioFormat != 4) {
            Log.e(str, "AudioTrack.write(float[] ...) requires format ENCODING_PCM_FLOAT");
            return -3;
        } else if (writeMode != 0 && writeMode != 1) {
            Log.e(str, "AudioTrack.write() called with invalid blocking mode");
            return -2;
        } else if (audioData == null || offsetInFloats < 0 || sizeInFloats < 0 || offsetInFloats + sizeInFloats < 0 || offsetInFloats + sizeInFloats > audioData.length) {
            Log.e(str, "AudioTrack.write() called with invalid array, offset, or size");
            return -2;
        } else if (!blockUntilOffloadDrain(writeMode)) {
            return 0;
        } else {
            i = native_write_float(audioData, offsetInFloats, sizeInFloats, this.mAudioFormat, writeMode == 0);
            if (this.mDataLoadMode == 0 && this.mState == 2 && i > 0) {
                this.mState = 1;
            }
            return i;
        }
    }

    public int write(ByteBuffer audioData, int sizeInBytes, int writeMode) {
        int i = this.mState;
        String str = TAG;
        if (i == 0) {
            Log.e(str, "AudioTrack.write() called in invalid state STATE_UNINITIALIZED");
            return -3;
        } else if (writeMode != 0 && writeMode != 1) {
            Log.e(str, "AudioTrack.write() called with invalid blocking mode");
            return -2;
        } else if (audioData == null || sizeInBytes < 0 || sizeInBytes > audioData.remaining()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AudioTrack.write() called with invalid size (");
            stringBuilder.append(sizeInBytes);
            stringBuilder.append(") value");
            Log.e(str, stringBuilder.toString());
            return -2;
        } else if (!blockUntilOffloadDrain(writeMode)) {
            return 0;
        } else {
            if (audioData.isDirect()) {
                i = native_write_native_bytes(audioData, audioData.position(), sizeInBytes, this.mAudioFormat, writeMode == 0);
            } else {
                i = native_write_byte(NioUtils.unsafeArray(audioData), audioData.position() + NioUtils.unsafeArrayOffset(audioData), sizeInBytes, this.mAudioFormat, writeMode == 0);
            }
            if (this.mDataLoadMode == 0 && this.mState == 2 && i > 0) {
                this.mState = 1;
            }
            if (i > 0) {
                audioData.position(audioData.position() + i);
            }
            return i;
        }
    }

    public int write(ByteBuffer audioData, int sizeInBytes, int writeMode, long timestamp) {
        int i = this.mState;
        String str = TAG;
        if (i == 0) {
            Log.e(str, "AudioTrack.write() called in invalid state STATE_UNINITIALIZED");
            return -3;
        } else if (writeMode != 0 && writeMode != 1) {
            Log.e(str, "AudioTrack.write() called with invalid blocking mode");
            return -2;
        } else if (this.mDataLoadMode != 1) {
            Log.e(str, "AudioTrack.write() with timestamp called for non-streaming mode track");
            return -3;
        } else if ((this.mAttributes.getFlags() & 16) == 0) {
            Log.d(str, "AudioTrack.write() called on a regular AudioTrack. Ignoring pts...");
            return write(audioData, sizeInBytes, writeMode);
        } else if (audioData == null || sizeInBytes < 0 || sizeInBytes > audioData.remaining()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AudioTrack.write() called with invalid size (");
            stringBuilder.append(sizeInBytes);
            stringBuilder.append(") value");
            Log.e(str, stringBuilder.toString());
            return -2;
        } else if (!blockUntilOffloadDrain(writeMode)) {
            return 0;
        } else {
            if (this.mAvSyncHeader == null) {
                this.mAvSyncHeader = ByteBuffer.allocate(this.mOffset);
                this.mAvSyncHeader.order(ByteOrder.BIG_ENDIAN);
                this.mAvSyncHeader.putInt(1431633922);
            }
            if (this.mAvSyncBytesRemaining == 0) {
                this.mAvSyncHeader.putInt(4, sizeInBytes);
                this.mAvSyncHeader.putLong(8, timestamp);
                this.mAvSyncHeader.putInt(16, this.mOffset);
                this.mAvSyncHeader.position(0);
                this.mAvSyncBytesRemaining = sizeInBytes;
            }
            if (this.mAvSyncHeader.remaining() != 0) {
                ByteBuffer byteBuffer = this.mAvSyncHeader;
                i = write(byteBuffer, byteBuffer.remaining(), writeMode);
                if (i < 0) {
                    Log.e(str, "AudioTrack.write() could not write timestamp header!");
                    this.mAvSyncHeader = null;
                    this.mAvSyncBytesRemaining = 0;
                    return i;
                } else if (this.mAvSyncHeader.remaining() > 0) {
                    Log.v(str, "AudioTrack.write() partial timestamp header written.");
                    return 0;
                }
            }
            i = write(audioData, Math.min(this.mAvSyncBytesRemaining, sizeInBytes), writeMode);
            if (i < 0) {
                Log.e(str, "AudioTrack.write() could not write audio data!");
                this.mAvSyncHeader = null;
                this.mAvSyncBytesRemaining = 0;
                return i;
            }
            this.mAvSyncBytesRemaining -= i;
            return i;
        }
    }

    public int reloadStaticData() {
        if (this.mDataLoadMode == 1 || this.mState != 1) {
            return -3;
        }
        return native_reload_static();
    }

    private boolean blockUntilOffloadDrain(int writeMode) {
        synchronized (this.mPlayStateLock) {
            while (true) {
                if (this.mPlayState != 4) {
                    if (this.mPlayState != 5) {
                        return true;
                    }
                }
                if (writeMode == 1) {
                    return false;
                }
                try {
                    this.mPlayStateLock.wait();
                } catch (InterruptedException e) {
                }
            }
            while (true) {
            }
        }
    }

    public int attachAuxEffect(int effectId) {
        if (this.mState == 0) {
            return -3;
        }
        return native_attachAuxEffect(effectId);
    }

    public int setAuxEffectSendLevel(float level) {
        if (this.mState == 0) {
            return -3;
        }
        return baseSetAuxEffectSendLevel(level);
    }

    /* Access modifiers changed, original: 0000 */
    public int playerSetAuxEffectSendLevel(boolean muting, float level) {
        return native_setAuxEffectSendLevel(clampGainOrLevel(muting ? 0.0f : level)) == 0 ? 0 : -1;
    }

    public boolean setPreferredDevice(AudioDeviceInfo deviceInfo) {
        int preferredDeviceId = 0;
        if (deviceInfo != null && !deviceInfo.isSink()) {
            return false;
        }
        if (deviceInfo != null) {
            preferredDeviceId = deviceInfo.getId();
        }
        boolean status = native_setOutputDevice(preferredDeviceId);
        if (status) {
            synchronized (this) {
                this.mPreferredDevice = deviceInfo;
            }
        }
        return status;
    }

    public AudioDeviceInfo getPreferredDevice() {
        AudioDeviceInfo audioDeviceInfo;
        synchronized (this) {
            audioDeviceInfo = this.mPreferredDevice;
        }
        return audioDeviceInfo;
    }

    public AudioDeviceInfo getRoutedDevice() {
        int deviceId = native_getRoutedDeviceId();
        if (deviceId == 0) {
            return null;
        }
        AudioDeviceInfo[] devices = AudioManager.getDevicesStatic(2);
        for (int i = 0; i < devices.length; i++) {
            if (devices[i].getId() == deviceId) {
                return devices[i];
            }
        }
        return null;
    }

    @GuardedBy({"mRoutingChangeListeners"})
    private void testEnableNativeRoutingCallbacksLocked() {
        if (this.mRoutingChangeListeners.size() == 0) {
            native_enableDeviceCallback();
        }
    }

    @GuardedBy({"mRoutingChangeListeners"})
    private void testDisableNativeRoutingCallbacksLocked() {
        if (this.mRoutingChangeListeners.size() == 0) {
            native_disableDeviceCallback();
        }
    }

    public void addOnRoutingChangedListener(android.media.AudioRouting.OnRoutingChangedListener listener, Handler handler) {
        synchronized (this.mRoutingChangeListeners) {
            if (listener != null) {
                if (!this.mRoutingChangeListeners.containsKey(listener)) {
                    testEnableNativeRoutingCallbacksLocked();
                    this.mRoutingChangeListeners.put(listener, new NativeRoutingEventHandlerDelegate(this, listener, handler != null ? handler : new Handler(this.mInitializationLooper)));
                }
            }
        }
    }

    public void removeOnRoutingChangedListener(android.media.AudioRouting.OnRoutingChangedListener listener) {
        synchronized (this.mRoutingChangeListeners) {
            if (this.mRoutingChangeListeners.containsKey(listener)) {
                this.mRoutingChangeListeners.remove(listener);
            }
            testDisableNativeRoutingCallbacksLocked();
        }
    }

    @Deprecated
    public void addOnRoutingChangedListener(OnRoutingChangedListener listener, Handler handler) {
        addOnRoutingChangedListener((android.media.AudioRouting.OnRoutingChangedListener) listener, handler);
    }

    @Deprecated
    public void removeOnRoutingChangedListener(OnRoutingChangedListener listener) {
        removeOnRoutingChangedListener((android.media.AudioRouting.OnRoutingChangedListener) listener);
    }

    private void broadcastRoutingChange() {
        AudioManager.resetAudioPortGeneration();
        synchronized (this.mRoutingChangeListeners) {
            for (NativeRoutingEventHandlerDelegate delegate : this.mRoutingChangeListeners.values()) {
                delegate.notifyClient();
            }
        }
    }

    public void registerStreamEventCallback(Executor executor, StreamEventCallback eventCallback) {
        if (eventCallback == null) {
            throw new IllegalArgumentException("Illegal null StreamEventCallback");
        } else if (!this.mOffloaded) {
            throw new IllegalStateException("Cannot register StreamEventCallback on non-offloaded AudioTrack");
        } else if (executor != null) {
            synchronized (this.mStreamEventCbLock) {
                Iterator it = this.mStreamEventCbInfoList.iterator();
                while (it.hasNext()) {
                    if (((StreamEventCbInfo) it.next()).mStreamEventCb == eventCallback) {
                        throw new IllegalArgumentException("StreamEventCallback already registered");
                    }
                }
                beginStreamEventHandling();
                this.mStreamEventCbInfoList.add(new StreamEventCbInfo(executor, eventCallback));
            }
        } else {
            throw new IllegalArgumentException("Illegal null Executor for the StreamEventCallback");
        }
    }

    public void unregisterStreamEventCallback(StreamEventCallback eventCallback) {
        if (eventCallback == null) {
            throw new IllegalArgumentException("Illegal null StreamEventCallback");
        } else if (this.mOffloaded) {
            synchronized (this.mStreamEventCbLock) {
                Iterator it = this.mStreamEventCbInfoList.iterator();
                while (it.hasNext()) {
                    StreamEventCbInfo seci = (StreamEventCbInfo) it.next();
                    if (seci.mStreamEventCb == eventCallback) {
                        this.mStreamEventCbInfoList.remove(seci);
                        if (this.mStreamEventCbInfoList.size() == 0) {
                            endStreamEventHandling();
                        }
                    }
                }
                throw new IllegalArgumentException("StreamEventCallback was not registered");
            }
        } else {
            throw new IllegalStateException("No StreamEventCallback on non-offloaded AudioTrack");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void handleStreamEventFromNative(int what, int arg) {
        if (this.mStreamEventHandler != null) {
            if (what == 6) {
                this.mStreamEventHandler.sendMessage(this.mStreamEventHandler.obtainMessage(6));
            } else if (what == 7) {
                this.mStreamEventHandler.sendMessage(this.mStreamEventHandler.obtainMessage(7));
            } else if (what == 9) {
                this.mStreamEventHandler.removeMessages(9);
                this.mStreamEventHandler.sendMessage(this.mStreamEventHandler.obtainMessage(9, arg, 0));
            }
        }
    }

    @GuardedBy({"mStreamEventCbLock"})
    private void beginStreamEventHandling() {
        if (this.mStreamEventHandlerThread == null) {
            this.mStreamEventHandlerThread = new HandlerThread("android.media.AudioTrack.StreamEvent");
            this.mStreamEventHandlerThread.start();
            Looper looper = this.mStreamEventHandlerThread.getLooper();
            if (looper != null) {
                this.mStreamEventHandler = new StreamEventHandler(looper);
            }
        }
    }

    @GuardedBy({"mStreamEventCbLock"})
    private void endStreamEventHandling() {
        HandlerThread handlerThread = this.mStreamEventHandlerThread;
        if (handlerThread != null) {
            handlerThread.quit();
            this.mStreamEventHandlerThread = null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void playerStart() {
        play();
    }

    /* Access modifiers changed, original: 0000 */
    public void playerPause() {
        pause();
    }

    /* Access modifiers changed, original: 0000 */
    public void playerStop() {
        stop();
    }

    @UnsupportedAppUsage
    private static void postEventFromNative(Object audiotrack_ref, int what, int arg1, int arg2, Object obj) {
        AudioTrack track = (AudioTrack) ((WeakReference) audiotrack_ref).get();
        if (track != null) {
            if (what == 1000) {
                track.broadcastRoutingChange();
            } else if (what == 9 || what == 6 || what == 7) {
                track.handleStreamEventFromNative(what, arg1);
            } else {
                NativePositionEventHandlerDelegate delegate = track.mEventHandlerDelegate;
                if (delegate != null) {
                    Handler handler = delegate.getHandler();
                    if (handler != null) {
                        handler.sendMessage(handler.obtainMessage(what, arg1, arg2, obj));
                    }
                }
            }
        }
    }

    private static void logd(String msg) {
        Log.d(TAG, msg);
    }

    private static void loge(String msg) {
        Log.e(TAG, msg);
    }
}
