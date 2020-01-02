package android.media.tv;

import android.annotation.SystemApi;
import android.content.Intent;
import android.graphics.Rect;
import android.media.PlaybackParams;
import android.media.tv.ITvInputClient.Stub;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pools.Pool;
import android.util.Pools.SimplePool;
import android.util.SparseArray;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventSender;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class TvInputManager {
    public static final String ACTION_BLOCKED_RATINGS_CHANGED = "android.media.tv.action.BLOCKED_RATINGS_CHANGED";
    public static final String ACTION_PARENTAL_CONTROLS_ENABLED_CHANGED = "android.media.tv.action.PARENTAL_CONTROLS_ENABLED_CHANGED";
    public static final String ACTION_QUERY_CONTENT_RATING_SYSTEMS = "android.media.tv.action.QUERY_CONTENT_RATING_SYSTEMS";
    public static final String ACTION_SETUP_INPUTS = "android.media.tv.action.SETUP_INPUTS";
    public static final String ACTION_VIEW_RECORDING_SCHEDULES = "android.media.tv.action.VIEW_RECORDING_SCHEDULES";
    public static final int DVB_DEVICE_DEMUX = 0;
    public static final int DVB_DEVICE_DVR = 1;
    static final int DVB_DEVICE_END = 2;
    public static final int DVB_DEVICE_FRONTEND = 2;
    static final int DVB_DEVICE_START = 0;
    public static final int INPUT_STATE_CONNECTED = 0;
    public static final int INPUT_STATE_CONNECTED_STANDBY = 1;
    public static final int INPUT_STATE_DISCONNECTED = 2;
    public static final String META_DATA_CONTENT_RATING_SYSTEMS = "android.media.tv.metadata.CONTENT_RATING_SYSTEMS";
    static final int RECORDING_ERROR_END = 2;
    public static final int RECORDING_ERROR_INSUFFICIENT_SPACE = 1;
    public static final int RECORDING_ERROR_RESOURCE_BUSY = 2;
    static final int RECORDING_ERROR_START = 0;
    public static final int RECORDING_ERROR_UNKNOWN = 0;
    private static final String TAG = "TvInputManager";
    public static final long TIME_SHIFT_INVALID_TIME = Long.MIN_VALUE;
    public static final int TIME_SHIFT_STATUS_AVAILABLE = 3;
    public static final int TIME_SHIFT_STATUS_UNAVAILABLE = 2;
    public static final int TIME_SHIFT_STATUS_UNKNOWN = 0;
    public static final int TIME_SHIFT_STATUS_UNSUPPORTED = 1;
    public static final int VIDEO_UNAVAILABLE_REASON_AUDIO_ONLY = 4;
    public static final int VIDEO_UNAVAILABLE_REASON_BUFFERING = 3;
    static final int VIDEO_UNAVAILABLE_REASON_END = 5;
    public static final int VIDEO_UNAVAILABLE_REASON_NOT_CONNECTED = 5;
    static final int VIDEO_UNAVAILABLE_REASON_START = 0;
    public static final int VIDEO_UNAVAILABLE_REASON_TUNING = 1;
    public static final int VIDEO_UNAVAILABLE_REASON_UNKNOWN = 0;
    public static final int VIDEO_UNAVAILABLE_REASON_WEAK_SIGNAL = 2;
    private final List<TvInputCallbackRecord> mCallbackRecords = new LinkedList();
    private final ITvInputClient mClient;
    private final Object mLock = new Object();
    private int mNextSeq;
    private final ITvInputManager mService;
    private final SparseArray<SessionCallbackRecord> mSessionCallbackRecordMap = new SparseArray();
    private final Map<String, Integer> mStateMap = new ArrayMap();
    private final int mUserId;

    @SystemApi
    public static final class Hardware {
        private final ITvInputHardware mInterface;

        /* synthetic */ Hardware(ITvInputHardware x0, AnonymousClass1 x1) {
            this(x0);
        }

        private Hardware(ITvInputHardware hardwareInterface) {
            this.mInterface = hardwareInterface;
        }

        private ITvInputHardware getInterface() {
            return this.mInterface;
        }

        public boolean setSurface(Surface surface, TvStreamConfig config) {
            try {
                return this.mInterface.setSurface(surface, config);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void setStreamVolume(float volume) {
            try {
                this.mInterface.setStreamVolume(volume);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @SystemApi
        public boolean dispatchKeyEventToHdmi(KeyEvent event) {
            return false;
        }

        public void overrideAudioSink(int audioType, String audioAddress, int samplingRate, int channelMask, int format) {
            try {
                this.mInterface.overrideAudioSink(audioType, audioAddress, samplingRate, channelMask, format);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @SystemApi
    public static abstract class HardwareCallback {
        public abstract void onReleased();

        public abstract void onStreamConfigChanged(TvStreamConfig[] tvStreamConfigArr);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface InputState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RecordingError {
    }

    public static final class Session {
        static final int DISPATCH_HANDLED = 1;
        static final int DISPATCH_IN_PROGRESS = -1;
        static final int DISPATCH_NOT_HANDLED = 0;
        private static final long INPUT_SESSION_NOT_RESPONDING_TIMEOUT = 2500;
        private final List<TvTrackInfo> mAudioTracks;
        private InputChannel mChannel;
        private final InputEventHandler mHandler;
        private final Object mMetadataLock;
        private final Pool<PendingEvent> mPendingEventPool;
        private final SparseArray<PendingEvent> mPendingEvents;
        private String mSelectedAudioTrackId;
        private String mSelectedSubtitleTrackId;
        private String mSelectedVideoTrackId;
        private TvInputEventSender mSender;
        private final int mSeq;
        private final ITvInputManager mService;
        private final SparseArray<SessionCallbackRecord> mSessionCallbackRecordMap;
        private final List<TvTrackInfo> mSubtitleTracks;
        private IBinder mToken;
        private final int mUserId;
        private int mVideoHeight;
        private final List<TvTrackInfo> mVideoTracks;
        private int mVideoWidth;

        public interface FinishedInputEventCallback {
            void onFinishedInputEvent(Object obj, boolean z);
        }

        private final class InputEventHandler extends Handler {
            public static final int MSG_FLUSH_INPUT_EVENT = 3;
            public static final int MSG_SEND_INPUT_EVENT = 1;
            public static final int MSG_TIMEOUT_INPUT_EVENT = 2;

            InputEventHandler(Looper looper) {
                super(looper, null, true);
            }

            public void handleMessage(Message msg) {
                int i = msg.what;
                if (i == 1) {
                    Session.this.sendInputEventAndReportResultOnMainLooper((PendingEvent) msg.obj);
                } else if (i == 2) {
                    Session.this.finishedInputEvent(msg.arg1, false, true);
                } else if (i == 3) {
                    Session.this.finishedInputEvent(msg.arg1, false, false);
                }
            }
        }

        private final class PendingEvent implements Runnable {
            public FinishedInputEventCallback mCallback;
            public InputEvent mEvent;
            public Handler mEventHandler;
            public Object mEventToken;
            public boolean mHandled;

            private PendingEvent() {
            }

            /* synthetic */ PendingEvent(Session x0, AnonymousClass1 x1) {
                this();
            }

            public void recycle() {
                this.mEvent = null;
                this.mEventToken = null;
                this.mCallback = null;
                this.mEventHandler = null;
                this.mHandled = false;
            }

            public void run() {
                this.mCallback.onFinishedInputEvent(this.mEventToken, this.mHandled);
                synchronized (this.mEventHandler) {
                    Session.this.recyclePendingEventLocked(this);
                }
            }
        }

        private final class TvInputEventSender extends InputEventSender {
            public TvInputEventSender(InputChannel inputChannel, Looper looper) {
                super(inputChannel, looper);
            }

            public void onInputEventFinished(int seq, boolean handled) {
                Session.this.finishedInputEvent(seq, handled, false);
            }
        }

        /* synthetic */ Session(IBinder x0, InputChannel x1, ITvInputManager x2, int x3, int x4, SparseArray x5, AnonymousClass1 x6) {
            this(x0, x1, x2, x3, x4, x5);
        }

        private Session(IBinder token, InputChannel channel, ITvInputManager service, int userId, int seq, SparseArray<SessionCallbackRecord> sessionCallbackRecordMap) {
            this.mHandler = new InputEventHandler(Looper.getMainLooper());
            this.mPendingEventPool = new SimplePool(20);
            this.mPendingEvents = new SparseArray(20);
            this.mMetadataLock = new Object();
            this.mAudioTracks = new ArrayList();
            this.mVideoTracks = new ArrayList();
            this.mSubtitleTracks = new ArrayList();
            this.mToken = token;
            this.mChannel = channel;
            this.mService = service;
            this.mUserId = userId;
            this.mSeq = seq;
            this.mSessionCallbackRecordMap = sessionCallbackRecordMap;
        }

        public void release() {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.releaseSession(iBinder, this.mUserId);
                releaseInternal();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void setMain() {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.setMainSession(iBinder, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void setSurface(Surface surface) {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.setSurface(iBinder, surface, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void dispatchSurfaceChanged(int format, int width, int height) {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.dispatchSurfaceChanged(iBinder, format, width, height, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void setStreamVolume(float volume) {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
            } else if (volume < 0.0f || volume > 1.0f) {
                throw new IllegalArgumentException("volume should be between 0.0f and 1.0f");
            } else {
                try {
                    this.mService.setVolume(iBinder, volume, this.mUserId);
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }

        public void tune(Uri channelUri) {
            tune(channelUri, null);
        }

        public void tune(Uri channelUri, Bundle params) {
            Preconditions.checkNotNull(channelUri);
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            synchronized (this.mMetadataLock) {
                this.mAudioTracks.clear();
                this.mVideoTracks.clear();
                this.mSubtitleTracks.clear();
                this.mSelectedAudioTrackId = null;
                this.mSelectedVideoTrackId = null;
                this.mSelectedSubtitleTrackId = null;
                this.mVideoWidth = 0;
                this.mVideoHeight = 0;
            }
            try {
                this.mService.tune(this.mToken, channelUri, params, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void setCaptionEnabled(boolean enabled) {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.setCaptionEnabled(iBinder, enabled, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* JADX WARNING: Missing block: B:27:0x0072, code skipped:
            r0 = r4.mToken;
     */
        /* JADX WARNING: Missing block: B:28:0x0074, code skipped:
            if (r0 != null) goto L_0x007e;
     */
        /* JADX WARNING: Missing block: B:29:0x0076, code skipped:
            android.util.Log.w(android.media.tv.TvInputManager.TAG, "The session has been already released");
     */
        /* JADX WARNING: Missing block: B:30:0x007d, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:32:?, code skipped:
            r4.mService.selectTrack(r0, r5, r6, r4.mUserId);
     */
        /* JADX WARNING: Missing block: B:33:0x0086, code skipped:
            return;
     */
        /* JADX WARNING: Missing block: B:34:0x0087, code skipped:
            r0 = move-exception;
     */
        /* JADX WARNING: Missing block: B:36:0x008c, code skipped:
            throw r0.rethrowFromSystemServer();
     */
        public void selectTrack(int r5, java.lang.String r6) {
            /*
            r4 = this;
            r0 = r4.mMetadataLock;
            monitor-enter(r0);
            if (r5 != 0) goto L_0x0027;
        L_0x0005:
            if (r6 == 0) goto L_0x0071;
        L_0x0007:
            r1 = r4.mAudioTracks;	 Catch:{ all -> 0x008d }
            r1 = r4.containsTrack(r1, r6);	 Catch:{ all -> 0x008d }
            if (r1 != 0) goto L_0x0071;
        L_0x000f:
            r1 = "TvInputManager";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008d }
            r2.<init>();	 Catch:{ all -> 0x008d }
            r3 = "Invalid audio trackId: ";
            r2.append(r3);	 Catch:{ all -> 0x008d }
            r2.append(r6);	 Catch:{ all -> 0x008d }
            r2 = r2.toString();	 Catch:{ all -> 0x008d }
            android.util.Log.w(r1, r2);	 Catch:{ all -> 0x008d }
            monitor-exit(r0);	 Catch:{ all -> 0x008d }
            return;
        L_0x0027:
            r1 = 1;
            if (r5 != r1) goto L_0x004c;
        L_0x002a:
            if (r6 == 0) goto L_0x0071;
        L_0x002c:
            r1 = r4.mVideoTracks;	 Catch:{ all -> 0x008d }
            r1 = r4.containsTrack(r1, r6);	 Catch:{ all -> 0x008d }
            if (r1 != 0) goto L_0x0071;
        L_0x0034:
            r1 = "TvInputManager";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008d }
            r2.<init>();	 Catch:{ all -> 0x008d }
            r3 = "Invalid video trackId: ";
            r2.append(r3);	 Catch:{ all -> 0x008d }
            r2.append(r6);	 Catch:{ all -> 0x008d }
            r2 = r2.toString();	 Catch:{ all -> 0x008d }
            android.util.Log.w(r1, r2);	 Catch:{ all -> 0x008d }
            monitor-exit(r0);	 Catch:{ all -> 0x008d }
            return;
        L_0x004c:
            r1 = 2;
            if (r5 != r1) goto L_0x008f;
        L_0x004f:
            if (r6 == 0) goto L_0x0071;
        L_0x0051:
            r1 = r4.mSubtitleTracks;	 Catch:{ all -> 0x008d }
            r1 = r4.containsTrack(r1, r6);	 Catch:{ all -> 0x008d }
            if (r1 != 0) goto L_0x0071;
        L_0x0059:
            r1 = "TvInputManager";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008d }
            r2.<init>();	 Catch:{ all -> 0x008d }
            r3 = "Invalid subtitle trackId: ";
            r2.append(r3);	 Catch:{ all -> 0x008d }
            r2.append(r6);	 Catch:{ all -> 0x008d }
            r2 = r2.toString();	 Catch:{ all -> 0x008d }
            android.util.Log.w(r1, r2);	 Catch:{ all -> 0x008d }
            monitor-exit(r0);	 Catch:{ all -> 0x008d }
            return;
        L_0x0071:
            monitor-exit(r0);	 Catch:{ all -> 0x008d }
            r0 = r4.mToken;
            if (r0 != 0) goto L_0x007e;
        L_0x0076:
            r0 = "TvInputManager";
            r1 = "The session has been already released";
            android.util.Log.w(r0, r1);
            return;
        L_0x007e:
            r1 = r4.mService;	 Catch:{ RemoteException -> 0x0087 }
            r2 = r4.mUserId;	 Catch:{ RemoteException -> 0x0087 }
            r1.selectTrack(r0, r5, r6, r2);	 Catch:{ RemoteException -> 0x0087 }
            return;
        L_0x0087:
            r0 = move-exception;
            r1 = r0.rethrowFromSystemServer();
            throw r1;
        L_0x008d:
            r1 = move-exception;
            goto L_0x00a7;
        L_0x008f:
            r1 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x008d }
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008d }
            r2.<init>();	 Catch:{ all -> 0x008d }
            r3 = "invalid type: ";
            r2.append(r3);	 Catch:{ all -> 0x008d }
            r2.append(r5);	 Catch:{ all -> 0x008d }
            r2 = r2.toString();	 Catch:{ all -> 0x008d }
            r1.<init>(r2);	 Catch:{ all -> 0x008d }
            throw r1;	 Catch:{ all -> 0x008d }
        L_0x00a7:
            monitor-exit(r0);	 Catch:{ all -> 0x008d }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager$Session.selectTrack(int, java.lang.String):void");
        }

        private boolean containsTrack(List<TvTrackInfo> tracks, String trackId) {
            for (TvTrackInfo track : tracks) {
                if (track.getId().equals(trackId)) {
                    return true;
                }
            }
            return false;
        }

        public List<TvTrackInfo> getTracks(int type) {
            synchronized (this.mMetadataLock) {
                ArrayList arrayList;
                if (type == 0) {
                    try {
                        if (this.mAudioTracks == null) {
                            return null;
                        }
                        arrayList = new ArrayList(this.mAudioTracks);
                        return arrayList;
                    } catch (Throwable th) {
                        while (true) {
                        }
                    }
                } else if (type == 1) {
                    if (this.mVideoTracks == null) {
                        return null;
                    }
                    arrayList = new ArrayList(this.mVideoTracks);
                    return arrayList;
                } else if (type != 2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("invalid type: ");
                    stringBuilder.append(type);
                    throw new IllegalArgumentException(stringBuilder.toString());
                } else if (this.mSubtitleTracks == null) {
                    return null;
                } else {
                    arrayList = new ArrayList(this.mSubtitleTracks);
                    return arrayList;
                }
            }
        }

        public String getSelectedTrack(int type) {
            synchronized (this.mMetadataLock) {
                String str;
                if (type == 0) {
                    try {
                        str = this.mSelectedAudioTrackId;
                        return str;
                    } catch (Throwable th) {
                        while (true) {
                        }
                    }
                } else if (type == 1) {
                    str = this.mSelectedVideoTrackId;
                    return str;
                } else if (type == 2) {
                    str = this.mSelectedSubtitleTrackId;
                    return str;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("invalid type: ");
                    stringBuilder.append(type);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean updateTracks(List<TvTrackInfo> tracks) {
            boolean z;
            synchronized (this.mMetadataLock) {
                this.mAudioTracks.clear();
                this.mVideoTracks.clear();
                this.mSubtitleTracks.clear();
                Iterator it = tracks.iterator();
                while (true) {
                    z = true;
                    if (!it.hasNext()) {
                        break;
                    }
                    TvTrackInfo track = (TvTrackInfo) it.next();
                    if (track.getType() == 0) {
                        this.mAudioTracks.add(track);
                    } else if (track.getType() == 1) {
                        this.mVideoTracks.add(track);
                    } else if (track.getType() == 2) {
                        this.mSubtitleTracks.add(track);
                    }
                }
                if (this.mAudioTracks.isEmpty() && this.mVideoTracks.isEmpty()) {
                    if (this.mSubtitleTracks.isEmpty()) {
                        z = false;
                    }
                }
            }
            return z;
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Missing block: B:27:0x0033, code skipped:
            return false;
     */
        public boolean updateTrackSelection(int r4, java.lang.String r5) {
            /*
            r3 = this;
            r0 = r3.mMetadataLock;
            monitor-enter(r0);
            r1 = 1;
            if (r4 != 0) goto L_0x0014;
        L_0x0006:
            r2 = r3.mSelectedAudioTrackId;	 Catch:{ all -> 0x0012 }
            r2 = android.text.TextUtils.equals(r5, r2);	 Catch:{ all -> 0x0012 }
            if (r2 != 0) goto L_0x0014;
        L_0x000e:
            r3.mSelectedAudioTrackId = r5;	 Catch:{ all -> 0x0012 }
            monitor-exit(r0);	 Catch:{ all -> 0x0012 }
            return r1;
        L_0x0012:
            r1 = move-exception;
            goto L_0x0034;
        L_0x0014:
            if (r4 != r1) goto L_0x0022;
        L_0x0016:
            r2 = r3.mSelectedVideoTrackId;	 Catch:{ all -> 0x0012 }
            r2 = android.text.TextUtils.equals(r5, r2);	 Catch:{ all -> 0x0012 }
            if (r2 != 0) goto L_0x0022;
        L_0x001e:
            r3.mSelectedVideoTrackId = r5;	 Catch:{ all -> 0x0012 }
            monitor-exit(r0);	 Catch:{ all -> 0x0012 }
            return r1;
        L_0x0022:
            r2 = 2;
            if (r4 != r2) goto L_0x0031;
        L_0x0025:
            r2 = r3.mSelectedSubtitleTrackId;	 Catch:{ all -> 0x0012 }
            r2 = android.text.TextUtils.equals(r5, r2);	 Catch:{ all -> 0x0012 }
            if (r2 != 0) goto L_0x0031;
        L_0x002d:
            r3.mSelectedSubtitleTrackId = r5;	 Catch:{ all -> 0x0012 }
            monitor-exit(r0);	 Catch:{ all -> 0x0012 }
            return r1;
        L_0x0031:
            monitor-exit(r0);	 Catch:{ all -> 0x0012 }
            r0 = 0;
            return r0;
        L_0x0034:
            monitor-exit(r0);	 Catch:{ all -> 0x0012 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager$Session.updateTrackSelection(int, java.lang.String):boolean");
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Missing block: B:22:0x0046, code skipped:
            return null;
     */
        public android.media.tv.TvTrackInfo getVideoTrackToNotify() {
            /*
            r6 = this;
            r0 = r6.mMetadataLock;
            monitor-enter(r0);
            r1 = r6.mVideoTracks;	 Catch:{ all -> 0x0047 }
            r1 = r1.isEmpty();	 Catch:{ all -> 0x0047 }
            if (r1 != 0) goto L_0x0044;
        L_0x000b:
            r1 = r6.mSelectedVideoTrackId;	 Catch:{ all -> 0x0047 }
            if (r1 == 0) goto L_0x0044;
        L_0x000f:
            r1 = r6.mVideoTracks;	 Catch:{ all -> 0x0047 }
            r1 = r1.iterator();	 Catch:{ all -> 0x0047 }
        L_0x0015:
            r2 = r1.hasNext();	 Catch:{ all -> 0x0047 }
            if (r2 == 0) goto L_0x0044;
        L_0x001b:
            r2 = r1.next();	 Catch:{ all -> 0x0047 }
            r2 = (android.media.tv.TvTrackInfo) r2;	 Catch:{ all -> 0x0047 }
            r3 = r2.getId();	 Catch:{ all -> 0x0047 }
            r4 = r6.mSelectedVideoTrackId;	 Catch:{ all -> 0x0047 }
            r3 = r3.equals(r4);	 Catch:{ all -> 0x0047 }
            if (r3 == 0) goto L_0x0043;
        L_0x002d:
            r3 = r2.getVideoWidth();	 Catch:{ all -> 0x0047 }
            r4 = r2.getVideoHeight();	 Catch:{ all -> 0x0047 }
            r5 = r6.mVideoWidth;	 Catch:{ all -> 0x0047 }
            if (r5 != r3) goto L_0x003d;
        L_0x0039:
            r5 = r6.mVideoHeight;	 Catch:{ all -> 0x0047 }
            if (r5 == r4) goto L_0x0043;
        L_0x003d:
            r6.mVideoWidth = r3;	 Catch:{ all -> 0x0047 }
            r6.mVideoHeight = r4;	 Catch:{ all -> 0x0047 }
            monitor-exit(r0);	 Catch:{ all -> 0x0047 }
            return r2;
        L_0x0043:
            goto L_0x0015;
        L_0x0044:
            monitor-exit(r0);	 Catch:{ all -> 0x0047 }
            r0 = 0;
            return r0;
        L_0x0047:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0047 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager$Session.getVideoTrackToNotify():android.media.tv.TvTrackInfo");
        }

        /* Access modifiers changed, original: 0000 */
        public void timeShiftPlay(Uri recordedProgramUri) {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftPlay(iBinder, recordedProgramUri, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void timeShiftPause() {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftPause(iBinder, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void timeShiftResume() {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftResume(iBinder, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void timeShiftSeekTo(long timeMs) {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftSeekTo(iBinder, timeMs, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void timeShiftSetPlaybackParams(PlaybackParams params) {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftSetPlaybackParams(iBinder, params, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void timeShiftEnablePositionTracking(boolean enable) {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftEnablePositionTracking(iBinder, enable, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void startRecording(Uri programUri) {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.startRecording(iBinder, programUri, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void stopRecording() {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.stopRecording(iBinder, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public void sendAppPrivateCommand(String action, Bundle data) {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.sendAppPrivateCommand(iBinder, action, data, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void createOverlayView(View view, Rect frame) {
            Preconditions.checkNotNull(view);
            Preconditions.checkNotNull(frame);
            if (view.getWindowToken() != null) {
                IBinder iBinder = this.mToken;
                if (iBinder == null) {
                    Log.w(TvInputManager.TAG, "The session has been already released");
                    return;
                }
                try {
                    this.mService.createOverlayView(iBinder, view.getWindowToken(), frame, this.mUserId);
                    return;
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            throw new IllegalStateException("view must be attached to a window");
        }

        /* Access modifiers changed, original: 0000 */
        public void relayoutOverlayView(Rect frame) {
            Preconditions.checkNotNull(frame);
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.relayoutOverlayView(iBinder, frame, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void removeOverlayView() {
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.removeOverlayView(iBinder, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void unblockContent(TvContentRating unblockedRating) {
            Preconditions.checkNotNull(unblockedRating);
            IBinder iBinder = this.mToken;
            if (iBinder == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.unblockContent(iBinder, unblockedRating.flattenToString(), this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        public int dispatchInputEvent(InputEvent event, Object token, FinishedInputEventCallback callback, Handler handler) {
            Preconditions.checkNotNull(event);
            Preconditions.checkNotNull(callback);
            Preconditions.checkNotNull(handler);
            synchronized (this.mHandler) {
                if (this.mChannel == null) {
                    return 0;
                }
                PendingEvent p = obtainPendingEventLocked(event, token, callback, handler);
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    int sendInputEventOnMainLooperLocked = sendInputEventOnMainLooperLocked(p);
                    return sendInputEventOnMainLooperLocked;
                }
                Message msg = this.mHandler.obtainMessage(1, p);
                msg.setAsynchronous(true);
                this.mHandler.sendMessage(msg);
                return -1;
            }
        }

        private void sendInputEventAndReportResultOnMainLooper(PendingEvent p) {
            synchronized (this.mHandler) {
                if (sendInputEventOnMainLooperLocked(p) == -1) {
                    return;
                }
                invokeFinishedInputEventCallback(p, false);
            }
        }

        private int sendInputEventOnMainLooperLocked(PendingEvent p) {
            InputChannel inputChannel = this.mChannel;
            if (inputChannel != null) {
                if (this.mSender == null) {
                    this.mSender = new TvInputEventSender(inputChannel, this.mHandler.getLooper());
                }
                InputEvent event = p.mEvent;
                int seq = event.getSequenceNumber();
                if (this.mSender.sendInputEvent(seq, event)) {
                    this.mPendingEvents.put(seq, p);
                    Message msg = this.mHandler.obtainMessage(2, p);
                    msg.setAsynchronous(true);
                    this.mHandler.sendMessageDelayed(msg, INPUT_SESSION_NOT_RESPONDING_TIMEOUT);
                    return -1;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to send input event to session: ");
                stringBuilder.append(this.mToken);
                stringBuilder.append(" dropping:");
                stringBuilder.append(event);
                Log.w(TvInputManager.TAG, stringBuilder.toString());
            }
            return 0;
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Missing block: B:12:0x003c, code skipped:
            invokeFinishedInputEventCallback(r2, r8);
     */
        /* JADX WARNING: Missing block: B:13:0x003f, code skipped:
            return;
     */
        public void finishedInputEvent(int r7, boolean r8, boolean r9) {
            /*
            r6 = this;
            r0 = r6.mHandler;
            monitor-enter(r0);
            r1 = r6.mPendingEvents;	 Catch:{ all -> 0x0040 }
            r1 = r1.indexOfKey(r7);	 Catch:{ all -> 0x0040 }
            if (r1 >= 0) goto L_0x000d;
        L_0x000b:
            monitor-exit(r0);	 Catch:{ all -> 0x0040 }
            return;
        L_0x000d:
            r2 = r6.mPendingEvents;	 Catch:{ all -> 0x0040 }
            r2 = r2.valueAt(r1);	 Catch:{ all -> 0x0040 }
            r2 = (android.media.tv.TvInputManager.Session.PendingEvent) r2;	 Catch:{ all -> 0x0040 }
            r3 = r6.mPendingEvents;	 Catch:{ all -> 0x0040 }
            r3.removeAt(r1);	 Catch:{ all -> 0x0040 }
            if (r9 == 0) goto L_0x0035;
        L_0x001c:
            r3 = "TvInputManager";
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0040 }
            r4.<init>();	 Catch:{ all -> 0x0040 }
            r5 = "Timeout waiting for session to handle input event after 2500 ms: ";
            r4.append(r5);	 Catch:{ all -> 0x0040 }
            r5 = r6.mToken;	 Catch:{ all -> 0x0040 }
            r4.append(r5);	 Catch:{ all -> 0x0040 }
            r4 = r4.toString();	 Catch:{ all -> 0x0040 }
            android.util.Log.w(r3, r4);	 Catch:{ all -> 0x0040 }
            goto L_0x003b;
        L_0x0035:
            r3 = r6.mHandler;	 Catch:{ all -> 0x0040 }
            r4 = 2;
            r3.removeMessages(r4, r2);	 Catch:{ all -> 0x0040 }
        L_0x003b:
            monitor-exit(r0);	 Catch:{ all -> 0x0040 }
            r6.invokeFinishedInputEventCallback(r2, r8);
            return;
        L_0x0040:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0040 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager$Session.finishedInputEvent(int, boolean, boolean):void");
        }

        /* Access modifiers changed, original: 0000 */
        public void invokeFinishedInputEventCallback(PendingEvent p, boolean handled) {
            p.mHandled = handled;
            if (p.mEventHandler.getLooper().isCurrentThread()) {
                p.run();
                return;
            }
            Message msg = Message.obtain(p.mEventHandler, (Runnable) p);
            msg.setAsynchronous(true);
            msg.sendToTarget();
        }

        private void flushPendingEventsLocked() {
            this.mHandler.removeMessages(3);
            int count = this.mPendingEvents.size();
            for (int i = 0; i < count; i++) {
                Message msg = this.mHandler.obtainMessage(3, this.mPendingEvents.keyAt(i), 0);
                msg.setAsynchronous(true);
                msg.sendToTarget();
            }
        }

        private PendingEvent obtainPendingEventLocked(InputEvent event, Object token, FinishedInputEventCallback callback, Handler handler) {
            PendingEvent p = (PendingEvent) this.mPendingEventPool.acquire();
            if (p == null) {
                p = new PendingEvent(this, null);
            }
            p.mEvent = event;
            p.mEventToken = token;
            p.mCallback = callback;
            p.mEventHandler = handler;
            return p;
        }

        private void recyclePendingEventLocked(PendingEvent p) {
            p.recycle();
            this.mPendingEventPool.release(p);
        }

        /* Access modifiers changed, original: 0000 */
        public IBinder getToken() {
            return this.mToken;
        }

        private void releaseInternal() {
            this.mToken = null;
            synchronized (this.mHandler) {
                if (this.mChannel != null) {
                    if (this.mSender != null) {
                        flushPendingEventsLocked();
                        this.mSender.dispose();
                        this.mSender = null;
                    }
                    this.mChannel.dispose();
                    this.mChannel = null;
                }
            }
            synchronized (this.mSessionCallbackRecordMap) {
                this.mSessionCallbackRecordMap.delete(this.mSeq);
            }
        }
    }

    public static abstract class SessionCallback {
        public void onSessionCreated(Session session) {
        }

        public void onSessionReleased(Session session) {
        }

        public void onChannelRetuned(Session session, Uri channelUri) {
        }

        public void onTracksChanged(Session session, List<TvTrackInfo> list) {
        }

        public void onTrackSelected(Session session, int type, String trackId) {
        }

        public void onVideoSizeChanged(Session session, int width, int height) {
        }

        public void onVideoAvailable(Session session) {
        }

        public void onVideoUnavailable(Session session, int reason) {
        }

        public void onContentAllowed(Session session) {
        }

        public void onContentBlocked(Session session, TvContentRating rating) {
        }

        public void onLayoutSurface(Session session, int left, int top, int right, int bottom) {
        }

        public void onSessionEvent(Session session, String eventType, Bundle eventArgs) {
        }

        public void onTimeShiftStatusChanged(Session session, int status) {
        }

        public void onTimeShiftStartPositionChanged(Session session, long timeMs) {
        }

        public void onTimeShiftCurrentPositionChanged(Session session, long timeMs) {
        }

        /* Access modifiers changed, original: 0000 */
        public void onTuned(Session session, Uri channelUri) {
        }

        /* Access modifiers changed, original: 0000 */
        public void onRecordingStopped(Session session, Uri recordedProgramUri) {
        }

        /* Access modifiers changed, original: 0000 */
        public void onError(Session session, int error) {
        }
    }

    private static final class SessionCallbackRecord {
        private final Handler mHandler;
        private Session mSession;
        private final SessionCallback mSessionCallback;

        SessionCallbackRecord(SessionCallback sessionCallback, Handler handler) {
            this.mSessionCallback = sessionCallback;
            this.mHandler = handler;
        }

        /* Access modifiers changed, original: 0000 */
        public void postSessionCreated(final Session session) {
            this.mSession = session;
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onSessionCreated(session);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postSessionReleased() {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onSessionReleased(SessionCallbackRecord.this.mSession);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postChannelRetuned(final Uri channelUri) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onChannelRetuned(SessionCallbackRecord.this.mSession, channelUri);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postTracksChanged(final List<TvTrackInfo> tracks) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTracksChanged(SessionCallbackRecord.this.mSession, tracks);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postTrackSelected(final int type, final String trackId) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTrackSelected(SessionCallbackRecord.this.mSession, type, trackId);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postVideoSizeChanged(final int width, final int height) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onVideoSizeChanged(SessionCallbackRecord.this.mSession, width, height);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postVideoAvailable() {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onVideoAvailable(SessionCallbackRecord.this.mSession);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postVideoUnavailable(final int reason) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onVideoUnavailable(SessionCallbackRecord.this.mSession, reason);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postContentAllowed() {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onContentAllowed(SessionCallbackRecord.this.mSession);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postContentBlocked(final TvContentRating rating) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onContentBlocked(SessionCallbackRecord.this.mSession, rating);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postLayoutSurface(int left, int top, int right, int bottom) {
            final int i = left;
            final int i2 = top;
            final int i3 = right;
            final int i4 = bottom;
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onLayoutSurface(SessionCallbackRecord.this.mSession, i, i2, i3, i4);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postSessionEvent(final String eventType, final Bundle eventArgs) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onSessionEvent(SessionCallbackRecord.this.mSession, eventType, eventArgs);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postTimeShiftStatusChanged(final int status) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTimeShiftStatusChanged(SessionCallbackRecord.this.mSession, status);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postTimeShiftStartPositionChanged(final long timeMs) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTimeShiftStartPositionChanged(SessionCallbackRecord.this.mSession, timeMs);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postTimeShiftCurrentPositionChanged(final long timeMs) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTimeShiftCurrentPositionChanged(SessionCallbackRecord.this.mSession, timeMs);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postTuned(final Uri channelUri) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTuned(SessionCallbackRecord.this.mSession, channelUri);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postRecordingStopped(final Uri recordedProgramUri) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onRecordingStopped(SessionCallbackRecord.this.mSession, recordedProgramUri);
                }
            });
        }

        /* Access modifiers changed, original: 0000 */
        public void postError(final int error) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onError(SessionCallbackRecord.this.mSession, error);
                }
            });
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TimeShiftStatus {
    }

    public static abstract class TvInputCallback {
        public void onInputStateChanged(String inputId, int state) {
        }

        public void onInputAdded(String inputId) {
        }

        public void onInputRemoved(String inputId) {
        }

        public void onInputUpdated(String inputId) {
        }

        public void onTvInputInfoUpdated(TvInputInfo inputInfo) {
        }
    }

    private static final class TvInputCallbackRecord {
        private final TvInputCallback mCallback;
        private final Handler mHandler;

        public TvInputCallbackRecord(TvInputCallback callback, Handler handler) {
            this.mCallback = callback;
            this.mHandler = handler;
        }

        public TvInputCallback getCallback() {
            return this.mCallback;
        }

        public void postInputAdded(final String inputId) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    TvInputCallbackRecord.this.mCallback.onInputAdded(inputId);
                }
            });
        }

        public void postInputRemoved(final String inputId) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    TvInputCallbackRecord.this.mCallback.onInputRemoved(inputId);
                }
            });
        }

        public void postInputUpdated(final String inputId) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    TvInputCallbackRecord.this.mCallback.onInputUpdated(inputId);
                }
            });
        }

        public void postInputStateChanged(final String inputId, final int state) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    TvInputCallbackRecord.this.mCallback.onInputStateChanged(inputId, state);
                }
            });
        }

        public void postTvInputInfoUpdated(final TvInputInfo inputInfo) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    TvInputCallbackRecord.this.mCallback.onTvInputInfoUpdated(inputInfo);
                }
            });
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface VideoUnavailableReason {
    }

    public TvInputManager(ITvInputManager service, int userId) {
        this.mService = service;
        this.mUserId = userId;
        this.mClient = new Stub() {
            public void onSessionCreated(String inputId, IBinder token, InputChannel channel, int seq) {
                IBinder iBinder = token;
                int i = seq;
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(i);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for ");
                        stringBuilder.append(iBinder);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    Session session = null;
                    if (iBinder != null) {
                        session = new Session(token, channel, TvInputManager.this.mService, TvInputManager.this.mUserId, seq, TvInputManager.this.mSessionCallbackRecordMap, null);
                    } else {
                        TvInputManager.this.mSessionCallbackRecordMap.delete(i);
                    }
                    record.postSessionCreated(session);
                }
            }

            public void onSessionReleased(int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    TvInputManager.this.mSessionCallbackRecordMap.delete(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq:");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.mSession.releaseInternal();
                    record.postSessionReleased();
                }
            }

            public void onChannelRetuned(Uri channelUri, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postChannelRetuned(channelUri);
                }
            }

            /* JADX WARNING: Missing block: B:12:0x003e, code skipped:
            return;
     */
            public void onTracksChanged(java.util.List<android.media.tv.TvTrackInfo> r6, int r7) {
                /*
                r5 = this;
                r0 = android.media.tv.TvInputManager.this;
                r0 = r0.mSessionCallbackRecordMap;
                monitor-enter(r0);
                r1 = android.media.tv.TvInputManager.this;	 Catch:{ all -> 0x003f }
                r1 = r1.mSessionCallbackRecordMap;	 Catch:{ all -> 0x003f }
                r1 = r1.get(r7);	 Catch:{ all -> 0x003f }
                r1 = (android.media.tv.TvInputManager.SessionCallbackRecord) r1;	 Catch:{ all -> 0x003f }
                if (r1 != 0) goto L_0x002d;
            L_0x0015:
                r2 = "TvInputManager";
                r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x003f }
                r3.<init>();	 Catch:{ all -> 0x003f }
                r4 = "Callback not found for seq ";
                r3.append(r4);	 Catch:{ all -> 0x003f }
                r3.append(r7);	 Catch:{ all -> 0x003f }
                r3 = r3.toString();	 Catch:{ all -> 0x003f }
                android.util.Log.e(r2, r3);	 Catch:{ all -> 0x003f }
                monitor-exit(r0);	 Catch:{ all -> 0x003f }
                return;
            L_0x002d:
                r2 = r1.mSession;	 Catch:{ all -> 0x003f }
                r2 = r2.updateTracks(r6);	 Catch:{ all -> 0x003f }
                if (r2 == 0) goto L_0x003d;
            L_0x0037:
                r1.postTracksChanged(r6);	 Catch:{ all -> 0x003f }
                r5.postVideoSizeChangedIfNeededLocked(r1);	 Catch:{ all -> 0x003f }
            L_0x003d:
                monitor-exit(r0);	 Catch:{ all -> 0x003f }
                return;
            L_0x003f:
                r1 = move-exception;
                monitor-exit(r0);	 Catch:{ all -> 0x003f }
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager$AnonymousClass1.onTracksChanged(java.util.List, int):void");
            }

            /* JADX WARNING: Missing block: B:12:0x003e, code skipped:
            return;
     */
            public void onTrackSelected(int r6, java.lang.String r7, int r8) {
                /*
                r5 = this;
                r0 = android.media.tv.TvInputManager.this;
                r0 = r0.mSessionCallbackRecordMap;
                monitor-enter(r0);
                r1 = android.media.tv.TvInputManager.this;	 Catch:{ all -> 0x003f }
                r1 = r1.mSessionCallbackRecordMap;	 Catch:{ all -> 0x003f }
                r1 = r1.get(r8);	 Catch:{ all -> 0x003f }
                r1 = (android.media.tv.TvInputManager.SessionCallbackRecord) r1;	 Catch:{ all -> 0x003f }
                if (r1 != 0) goto L_0x002d;
            L_0x0015:
                r2 = "TvInputManager";
                r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x003f }
                r3.<init>();	 Catch:{ all -> 0x003f }
                r4 = "Callback not found for seq ";
                r3.append(r4);	 Catch:{ all -> 0x003f }
                r3.append(r8);	 Catch:{ all -> 0x003f }
                r3 = r3.toString();	 Catch:{ all -> 0x003f }
                android.util.Log.e(r2, r3);	 Catch:{ all -> 0x003f }
                monitor-exit(r0);	 Catch:{ all -> 0x003f }
                return;
            L_0x002d:
                r2 = r1.mSession;	 Catch:{ all -> 0x003f }
                r2 = r2.updateTrackSelection(r6, r7);	 Catch:{ all -> 0x003f }
                if (r2 == 0) goto L_0x003d;
            L_0x0037:
                r1.postTrackSelected(r6, r7);	 Catch:{ all -> 0x003f }
                r5.postVideoSizeChangedIfNeededLocked(r1);	 Catch:{ all -> 0x003f }
            L_0x003d:
                monitor-exit(r0);	 Catch:{ all -> 0x003f }
                return;
            L_0x003f:
                r1 = move-exception;
                monitor-exit(r0);	 Catch:{ all -> 0x003f }
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager$AnonymousClass1.onTrackSelected(int, java.lang.String, int):void");
            }

            private void postVideoSizeChangedIfNeededLocked(SessionCallbackRecord record) {
                TvTrackInfo track = record.mSession.getVideoTrackToNotify();
                if (track != null) {
                    record.postVideoSizeChanged(track.getVideoWidth(), track.getVideoHeight());
                }
            }

            public void onVideoAvailable(int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postVideoAvailable();
                }
            }

            public void onVideoUnavailable(int reason, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postVideoUnavailable(reason);
                }
            }

            public void onContentAllowed(int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postContentAllowed();
                }
            }

            public void onContentBlocked(String rating, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postContentBlocked(TvContentRating.unflattenFromString(rating));
                }
            }

            public void onLayoutSurface(int left, int top, int right, int bottom, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postLayoutSurface(left, top, right, bottom);
                }
            }

            public void onSessionEvent(String eventType, Bundle eventArgs, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postSessionEvent(eventType, eventArgs);
                }
            }

            public void onTimeShiftStatusChanged(int status, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postTimeShiftStatusChanged(status);
                }
            }

            public void onTimeShiftStartPositionChanged(long timeMs, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postTimeShiftStartPositionChanged(timeMs);
                }
            }

            public void onTimeShiftCurrentPositionChanged(long timeMs, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postTimeShiftCurrentPositionChanged(timeMs);
                }
            }

            public void onTuned(int seq, Uri channelUri) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postTuned(channelUri);
                }
            }

            public void onRecordingStopped(Uri recordedProgramUri, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postRecordingStopped(recordedProgramUri);
                }
            }

            public void onError(int error, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        String str = TvInputManager.TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Callback not found for seq ");
                        stringBuilder.append(seq);
                        Log.e(str, stringBuilder.toString());
                        return;
                    }
                    record.postError(error);
                }
            }
        };
        ITvInputManagerCallback managerCallback = new ITvInputManagerCallback.Stub() {
            public void onInputAdded(String inputId) {
                synchronized (TvInputManager.this.mLock) {
                    TvInputManager.this.mStateMap.put(inputId, Integer.valueOf(0));
                    for (TvInputCallbackRecord record : TvInputManager.this.mCallbackRecords) {
                        record.postInputAdded(inputId);
                    }
                }
            }

            public void onInputRemoved(String inputId) {
                synchronized (TvInputManager.this.mLock) {
                    TvInputManager.this.mStateMap.remove(inputId);
                    for (TvInputCallbackRecord record : TvInputManager.this.mCallbackRecords) {
                        record.postInputRemoved(inputId);
                    }
                }
            }

            public void onInputUpdated(String inputId) {
                synchronized (TvInputManager.this.mLock) {
                    for (TvInputCallbackRecord record : TvInputManager.this.mCallbackRecords) {
                        record.postInputUpdated(inputId);
                    }
                }
            }

            public void onInputStateChanged(String inputId, int state) {
                synchronized (TvInputManager.this.mLock) {
                    TvInputManager.this.mStateMap.put(inputId, Integer.valueOf(state));
                    for (TvInputCallbackRecord record : TvInputManager.this.mCallbackRecords) {
                        record.postInputStateChanged(inputId, state);
                    }
                }
            }

            public void onTvInputInfoUpdated(TvInputInfo inputInfo) {
                synchronized (TvInputManager.this.mLock) {
                    for (TvInputCallbackRecord record : TvInputManager.this.mCallbackRecords) {
                        record.postTvInputInfoUpdated(inputInfo);
                    }
                }
            }
        };
        try {
            if (this.mService != null) {
                this.mService.registerCallback(managerCallback, this.mUserId);
                List<TvInputInfo> infos = this.mService.getTvInputList(this.mUserId);
                synchronized (this.mLock) {
                    for (TvInputInfo info : infos) {
                        String inputId = info.getId();
                        this.mStateMap.put(inputId, Integer.valueOf(this.mService.getTvInputState(inputId, this.mUserId)));
                    }
                }
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<TvInputInfo> getTvInputList() {
        try {
            return this.mService.getTvInputList(this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public TvInputInfo getTvInputInfo(String inputId) {
        Preconditions.checkNotNull(inputId);
        try {
            return this.mService.getTvInputInfo(inputId, this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void updateTvInputInfo(TvInputInfo inputInfo) {
        Preconditions.checkNotNull(inputInfo);
        try {
            this.mService.updateTvInputInfo(inputInfo, this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public int getInputState(String inputId) {
        Preconditions.checkNotNull(inputId);
        synchronized (this.mLock) {
            Integer state = (Integer) this.mStateMap.get(inputId);
            if (state == null) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unrecognized input ID: ");
                stringBuilder.append(inputId);
                Log.w(str, stringBuilder.toString());
                return 2;
            }
            int intValue = state.intValue();
            return intValue;
        }
    }

    public void registerCallback(TvInputCallback callback, Handler handler) {
        Preconditions.checkNotNull(callback);
        Preconditions.checkNotNull(handler);
        synchronized (this.mLock) {
            this.mCallbackRecords.add(new TvInputCallbackRecord(callback, handler));
        }
    }

    public void unregisterCallback(TvInputCallback callback) {
        Preconditions.checkNotNull(callback);
        synchronized (this.mLock) {
            Iterator<TvInputCallbackRecord> it = this.mCallbackRecords.iterator();
            while (it.hasNext()) {
                if (((TvInputCallbackRecord) it.next()).getCallback() == callback) {
                    it.remove();
                    break;
                }
            }
        }
    }

    public boolean isParentalControlsEnabled() {
        try {
            return this.mService.isParentalControlsEnabled(this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void setParentalControlsEnabled(boolean enabled) {
        try {
            this.mService.setParentalControlsEnabled(enabled, this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isRatingBlocked(TvContentRating rating) {
        Preconditions.checkNotNull(rating);
        try {
            return this.mService.isRatingBlocked(rating.flattenToString(), this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<TvContentRating> getBlockedRatings() {
        try {
            List<TvContentRating> ratings = new ArrayList();
            for (String rating : this.mService.getBlockedRatings(this.mUserId)) {
                ratings.add(TvContentRating.unflattenFromString(rating));
            }
            return ratings;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void addBlockedRating(TvContentRating rating) {
        Preconditions.checkNotNull(rating);
        try {
            this.mService.addBlockedRating(rating.flattenToString(), this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void removeBlockedRating(TvContentRating rating) {
        Preconditions.checkNotNull(rating);
        try {
            this.mService.removeBlockedRating(rating.flattenToString(), this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<TvContentRatingSystemInfo> getTvContentRatingSystemList() {
        try {
            return this.mService.getTvContentRatingSystemList(this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void notifyPreviewProgramBrowsableDisabled(String packageName, long programId) {
        Intent intent = new Intent();
        intent.setAction(TvContract.ACTION_PREVIEW_PROGRAM_BROWSABLE_DISABLED);
        intent.putExtra(TvContract.EXTRA_PREVIEW_PROGRAM_ID, programId);
        intent.setPackage(packageName);
        try {
            this.mService.sendTvInputNotifyIntent(intent, this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void notifyWatchNextProgramBrowsableDisabled(String packageName, long programId) {
        Intent intent = new Intent();
        intent.setAction(TvContract.ACTION_WATCH_NEXT_PROGRAM_BROWSABLE_DISABLED);
        intent.putExtra(TvContract.EXTRA_WATCH_NEXT_PROGRAM_ID, programId);
        intent.setPackage(packageName);
        try {
            this.mService.sendTvInputNotifyIntent(intent, this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void notifyPreviewProgramAddedToWatchNext(String packageName, long previewProgramId, long watchNextProgramId) {
        Intent intent = new Intent();
        intent.setAction(TvContract.ACTION_PREVIEW_PROGRAM_ADDED_TO_WATCH_NEXT);
        intent.putExtra(TvContract.EXTRA_PREVIEW_PROGRAM_ID, previewProgramId);
        intent.putExtra(TvContract.EXTRA_WATCH_NEXT_PROGRAM_ID, watchNextProgramId);
        intent.setPackage(packageName);
        try {
            this.mService.sendTvInputNotifyIntent(intent, this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void createSession(String inputId, SessionCallback callback, Handler handler) {
        createSessionInternal(inputId, false, callback, handler);
    }

    public void createRecordingSession(String inputId, SessionCallback callback, Handler handler) {
        createSessionInternal(inputId, true, callback, handler);
    }

    private void createSessionInternal(String inputId, boolean isRecordingSession, SessionCallback callback, Handler handler) {
        Preconditions.checkNotNull(inputId);
        Preconditions.checkNotNull(callback);
        Preconditions.checkNotNull(handler);
        SessionCallbackRecord record = new SessionCallbackRecord(callback, handler);
        synchronized (this.mSessionCallbackRecordMap) {
            int seq = this.mNextSeq;
            this.mNextSeq = seq + 1;
            this.mSessionCallbackRecordMap.put(seq, record);
            try {
                this.mService.createSession(this.mClient, inputId, isRecordingSession, seq, this.mUserId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @SystemApi
    public List<TvStreamConfig> getAvailableTvStreamConfigList(String inputId) {
        try {
            return this.mService.getAvailableTvStreamConfigList(inputId, this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean captureFrame(String inputId, Surface surface, TvStreamConfig config) {
        try {
            return this.mService.captureFrame(inputId, surface, config, this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public boolean isSingleSessionActive() {
        try {
            return this.mService.isSingleSessionActive(this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public List<TvInputHardwareInfo> getHardwareList() {
        try {
            return this.mService.getHardwareList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public Hardware acquireTvInputHardware(int deviceId, HardwareCallback callback, TvInputInfo info) {
        return acquireTvInputHardware(deviceId, info, callback);
    }

    @SystemApi
    public Hardware acquireTvInputHardware(int deviceId, TvInputInfo info, final HardwareCallback callback) {
        try {
            return new Hardware(this.mService.acquireTvInputHardware(deviceId, new ITvInputHardwareCallback.Stub() {
                public void onReleased() {
                    callback.onReleased();
                }

                public void onStreamConfigChanged(TvStreamConfig[] configs) {
                    callback.onStreamConfigChanged(configs);
                }
            }, info, this.mUserId), null);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @SystemApi
    public void releaseTvInputHardware(int deviceId, Hardware hardware) {
        try {
            this.mService.releaseTvInputHardware(deviceId, hardware.getInterface(), this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<DvbDeviceInfo> getDvbDeviceList() {
        try {
            return this.mService.getDvbDeviceList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public ParcelFileDescriptor openDvbDevice(DvbDeviceInfo info, int device) {
        if (device < 0 || 2 < device) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid DVB device: ");
            stringBuilder.append(device);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        try {
            return this.mService.openDvbDevice(info, device);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void requestChannelBrowsable(Uri channelUri) {
        try {
            this.mService.requestChannelBrowsable(channelUri, this.mUserId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
