package android.media;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.session.MediaController;
import android.media.session.MediaController.Callback;
import android.media.session.MediaSessionLegacyHelper;
import android.media.session.MediaSessionManager;
import android.media.session.MediaSessionManager.OnActiveSessionsChangedListener;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.util.Log;
import android.view.KeyEvent;
import java.util.List;

@Deprecated
public final class RemoteController {
    private static final boolean DEBUG = false;
    private static final int MAX_BITMAP_DIMENSION = 512;
    private static final int MSG_CLIENT_CHANGE = 0;
    private static final int MSG_NEW_MEDIA_METADATA = 2;
    private static final int MSG_NEW_PLAYBACK_STATE = 1;
    public static final int POSITION_SYNCHRONIZATION_CHECK = 1;
    public static final int POSITION_SYNCHRONIZATION_NONE = 0;
    private static final int SENDMSG_NOOP = 1;
    private static final int SENDMSG_QUEUE = 2;
    private static final int SENDMSG_REPLACE = 0;
    private static final String TAG = "RemoteController";
    private static final Object mInfoLock = new Object();
    private int mArtworkHeight;
    private int mArtworkWidth;
    private final Context mContext;
    @UnsupportedAppUsage
    private MediaController mCurrentSession;
    private boolean mEnabled;
    private final EventHandler mEventHandler;
    private boolean mIsRegistered;
    private PlaybackInfo mLastPlaybackInfo;
    private final int mMaxBitmapDimension;
    private MetadataEditor mMetadataEditor;
    private OnClientUpdateListener mOnClientUpdateListener;
    private Callback mSessionCb;
    private OnActiveSessionsChangedListener mSessionListener;
    private MediaSessionManager mSessionManager;

    private class EventHandler extends Handler {
        public EventHandler(RemoteController rc, Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            boolean z = true;
            if (i == 0) {
                RemoteController remoteController = RemoteController.this;
                if (msg.arg2 != 1) {
                    z = false;
                }
                remoteController.onClientChange(z);
            } else if (i == 1) {
                RemoteController.this.onNewPlaybackState((PlaybackState) msg.obj);
            } else if (i != 2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unknown event ");
                stringBuilder.append(msg.what);
                Log.e(RemoteController.TAG, stringBuilder.toString());
            } else {
                RemoteController.this.onNewMediaMetadata((MediaMetadata) msg.obj);
            }
        }
    }

    private class MediaControllerCallback extends Callback {
        private MediaControllerCallback() {
        }

        public void onPlaybackStateChanged(PlaybackState state) {
            RemoteController.this.onNewPlaybackState(state);
        }

        public void onMetadataChanged(MediaMetadata metadata) {
            RemoteController.this.onNewMediaMetadata(metadata);
        }
    }

    public class MetadataEditor extends MediaMetadataEditor {
        protected MetadataEditor() {
        }

        protected MetadataEditor(Bundle metadata, long editableKeys) {
            this.mEditorMetadata = metadata;
            this.mEditableKeys = editableKeys;
            this.mEditorArtwork = (Bitmap) metadata.getParcelable(String.valueOf(100));
            if (this.mEditorArtwork != null) {
                cleanupBitmapFromBundle(100);
            }
            this.mMetadataChanged = true;
            this.mArtworkChanged = true;
            this.mApplied = false;
        }

        private void cleanupBitmapFromBundle(int key) {
            if (METADATA_KEYS_TYPE.get(key, -1) == 2) {
                this.mEditorMetadata.remove(String.valueOf(key));
            }
        }

        public synchronized void apply() {
            Throwable th;
            if (this.mMetadataChanged) {
                synchronized (RemoteController.mInfoLock) {
                    if (RemoteController.this.mCurrentSession != null) {
                        try {
                            if (this.mEditorMetadata.containsKey(String.valueOf(268435457))) {
                                Rating rating = (Rating) getObject(268435457, null);
                                if (rating != null) {
                                    RemoteController.this.mCurrentSession.getTransportControls().setRating(rating);
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            throw th;
                        }
                    }
                    try {
                        this.mApplied = false;
                        return;
                    } catch (Throwable th3) {
                        th = th3;
                        throw th;
                    }
                }
            } else {
                return;
            }
        }
    }

    public interface OnClientUpdateListener {
        void onClientChange(boolean z);

        void onClientMetadataUpdate(MetadataEditor metadataEditor);

        void onClientPlaybackStateUpdate(int i);

        void onClientPlaybackStateUpdate(int i, long j, long j2, float f);

        void onClientTransportControlUpdate(int i);
    }

    private static class PlaybackInfo {
        long mCurrentPosMs;
        float mSpeed;
        int mState;
        long mStateChangeTimeMs;

        PlaybackInfo(int state, long stateChangeTimeMs, long currentPosMs, float speed) {
            this.mState = state;
            this.mStateChangeTimeMs = stateChangeTimeMs;
            this.mCurrentPosMs = currentPosMs;
            this.mSpeed = speed;
        }
    }

    private class TopTransportSessionListener implements OnActiveSessionsChangedListener {
        private TopTransportSessionListener() {
        }

        public void onActiveSessionsChanged(List<MediaController> controllers) {
            int size = controllers.size();
            for (int i = 0; i < size; i++) {
                MediaController controller = (MediaController) controllers.get(i);
                if ((2 & controller.getFlags()) != 0) {
                    RemoteController.this.updateController(controller);
                    return;
                }
            }
            RemoteController.this.updateController(null);
        }
    }

    public RemoteController(Context context, OnClientUpdateListener updateListener) throws IllegalArgumentException {
        this(context, updateListener, null);
    }

    public RemoteController(Context context, OnClientUpdateListener updateListener, Looper looper) throws IllegalArgumentException {
        this.mSessionCb = new MediaControllerCallback();
        this.mIsRegistered = false;
        this.mArtworkWidth = -1;
        this.mArtworkHeight = -1;
        this.mEnabled = true;
        if (context == null) {
            throw new IllegalArgumentException("Invalid null Context");
        } else if (updateListener != null) {
            Looper l;
            if (looper != null) {
                this.mEventHandler = new EventHandler(this, looper);
            } else {
                l = Looper.myLooper();
                if (l != null) {
                    this.mEventHandler = new EventHandler(this, l);
                } else {
                    throw new IllegalArgumentException("Calling thread not associated with a looper");
                }
            }
            this.mOnClientUpdateListener = updateListener;
            this.mContext = context;
            this.mSessionManager = (MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE);
            this.mSessionListener = new TopTransportSessionListener();
            if (ActivityManager.isLowRamDeviceStatic()) {
                this.mMaxBitmapDimension = 512;
                return;
            }
            l = context.getResources().getDisplayMetrics();
            this.mMaxBitmapDimension = Math.max(l.widthPixels, l.heightPixels);
        } else {
            throw new IllegalArgumentException("Invalid null OnClientUpdateListener");
        }
    }

    public String getRemoteControlClientPackageName() {
        String packageName;
        synchronized (mInfoLock) {
            if (this.mCurrentSession != null) {
                packageName = this.mCurrentSession.getPackageName();
            } else {
                packageName = null;
            }
        }
        return packageName;
    }

    /* JADX WARNING: Missing block: B:12:0x0018, code skipped:
            return -1;
     */
    public long getEstimatedMediaPosition() {
        /*
        r4 = this;
        r0 = mInfoLock;
        monitor-enter(r0);
        r1 = r4.mCurrentSession;	 Catch:{ all -> 0x0019 }
        if (r1 == 0) goto L_0x0015;
    L_0x0007:
        r1 = r4.mCurrentSession;	 Catch:{ all -> 0x0019 }
        r1 = r1.getPlaybackState();	 Catch:{ all -> 0x0019 }
        if (r1 == 0) goto L_0x0015;
    L_0x000f:
        r2 = r1.getPosition();	 Catch:{ all -> 0x0019 }
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        return r2;
    L_0x0015:
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        r0 = -1;
        return r0;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0019 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.RemoteController.getEstimatedMediaPosition():long");
    }

    public boolean sendMediaKeyEvent(KeyEvent keyEvent) throws IllegalArgumentException {
        if (KeyEvent.isMediaSessionKey(keyEvent.getKeyCode())) {
            synchronized (mInfoLock) {
                if (this.mCurrentSession != null) {
                    boolean dispatchMediaButtonEvent = this.mCurrentSession.dispatchMediaButtonEvent(keyEvent);
                    return dispatchMediaButtonEvent;
                }
                return false;
            }
        }
        throw new IllegalArgumentException("not a media key event");
    }

    public boolean seekTo(long timeMs) throws IllegalArgumentException {
        if (!this.mEnabled) {
            Log.e(TAG, "Cannot use seekTo() from a disabled RemoteController");
            return false;
        } else if (timeMs >= 0) {
            synchronized (mInfoLock) {
                if (this.mCurrentSession != null) {
                    this.mCurrentSession.getTransportControls().seekTo(timeMs);
                }
            }
            return true;
        } else {
            throw new IllegalArgumentException("illegal negative time value");
        }
    }

    @UnsupportedAppUsage
    public boolean setArtworkConfiguration(boolean wantBitmap, int width, int height) throws IllegalArgumentException {
        synchronized (mInfoLock) {
            if (!wantBitmap) {
                this.mArtworkWidth = -1;
                this.mArtworkHeight = -1;
            } else if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Invalid dimensions");
            } else {
                if (width > this.mMaxBitmapDimension) {
                    width = this.mMaxBitmapDimension;
                }
                if (height > this.mMaxBitmapDimension) {
                    height = this.mMaxBitmapDimension;
                }
                this.mArtworkWidth = width;
                this.mArtworkHeight = height;
            }
        }
        return true;
    }

    public boolean setArtworkConfiguration(int width, int height) throws IllegalArgumentException {
        return setArtworkConfiguration(true, width, height);
    }

    public boolean clearArtworkConfiguration() {
        return setArtworkConfiguration(false, -1, -1);
    }

    public boolean setSynchronizationMode(int sync) throws IllegalArgumentException {
        if (sync != 0 && sync != 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown synchronization mode ");
            stringBuilder.append(sync);
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (this.mIsRegistered) {
            return true;
        } else {
            Log.e(TAG, "Cannot set synchronization mode on an unregistered RemoteController");
            return false;
        }
    }

    public MetadataEditor editMetadata() {
        MetadataEditor editor = new MetadataEditor();
        editor.mEditorMetadata = new Bundle();
        editor.mEditorArtwork = null;
        editor.mMetadataChanged = true;
        editor.mArtworkChanged = true;
        editor.mEditableKeys = 0;
        return editor;
    }

    /* Access modifiers changed, original: 0000 */
    public void startListeningToSessions() {
        ComponentName listenerComponent = new ComponentName(this.mContext, this.mOnClientUpdateListener.getClass());
        Handler handler = null;
        if (Looper.myLooper() == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        this.mSessionManager.addOnActiveSessionsChangedListener(this.mSessionListener, listenerComponent, UserHandle.myUserId(), handler);
        this.mSessionListener.onActiveSessionsChanged(this.mSessionManager.getActiveSessions(listenerComponent));
    }

    /* Access modifiers changed, original: 0000 */
    public void stopListeningToSessions() {
        this.mSessionManager.removeOnActiveSessionsChangedListener(this.mSessionListener);
    }

    private static void sendMsg(Handler handler, int msg, int existingMsgPolicy, int arg1, int arg2, Object obj, int delayMs) {
        if (handler == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("null event handler, will not deliver message ");
            stringBuilder.append(msg);
            Log.e(TAG, stringBuilder.toString());
            return;
        }
        if (existingMsgPolicy == 0) {
            handler.removeMessages(msg);
        } else if (existingMsgPolicy == 1 && handler.hasMessages(msg)) {
            return;
        }
        handler.sendMessageDelayed(handler.obtainMessage(msg, arg1, arg2, obj), (long) delayMs);
    }

    private void onClientChange(boolean clearing) {
        OnClientUpdateListener l;
        synchronized (mInfoLock) {
            l = this.mOnClientUpdateListener;
            this.mMetadataEditor = null;
        }
        if (l != null) {
            l.onClientChange(clearing);
        }
    }

    private void updateController(MediaController controller) {
        MediaController mediaController = controller;
        synchronized (mInfoLock) {
            if (mediaController == null) {
                if (this.mCurrentSession != null) {
                    this.mCurrentSession.unregisterCallback(this.mSessionCb);
                    this.mCurrentSession = null;
                    sendMsg(this.mEventHandler, 0, 0, 0, 1, null, 0);
                }
            } else if (this.mCurrentSession == null || !controller.getSessionToken().equals(this.mCurrentSession.getSessionToken())) {
                if (this.mCurrentSession != null) {
                    this.mCurrentSession.unregisterCallback(this.mSessionCb);
                }
                sendMsg(this.mEventHandler, 0, 0, 0, 0, null, 0);
                this.mCurrentSession = mediaController;
                this.mCurrentSession.registerCallback(this.mSessionCb, this.mEventHandler);
                sendMsg(this.mEventHandler, 1, 0, 0, 0, controller.getPlaybackState(), 0);
                sendMsg(this.mEventHandler, 2, 0, 0, 0, controller.getMetadata(), 0);
            }
        }
    }

    private void onNewPlaybackState(PlaybackState state) {
        OnClientUpdateListener l;
        synchronized (mInfoLock) {
            l = this.mOnClientUpdateListener;
        }
        if (l != null) {
            int playstate;
            if (state == null) {
                playstate = 0;
            } else {
                playstate = RemoteControlClient.getRccStateFromState(state.getState());
            }
            if (state == null || state.getPosition() == -1) {
                l.onClientPlaybackStateUpdate(playstate);
            } else {
                l.onClientPlaybackStateUpdate(playstate, state.getLastPositionUpdateTime(), state.getPosition(), state.getPlaybackSpeed());
            }
            if (state != null) {
                l.onClientTransportControlUpdate(RemoteControlClient.getRccControlFlagsFromActions(state.getActions()));
            }
        }
    }

    private void onNewMediaMetadata(MediaMetadata metadata) {
        if (metadata != null) {
            OnClientUpdateListener l;
            MetadataEditor metadataEditor;
            synchronized (mInfoLock) {
                l = this.mOnClientUpdateListener;
                boolean canRate = (this.mCurrentSession == null || this.mCurrentSession.getRatingType() == 0) ? false : true;
                this.mMetadataEditor = new MetadataEditor(MediaSessionLegacyHelper.getOldMetadata(metadata, this.mArtworkWidth, this.mArtworkHeight), canRate ? 268435457 : 0);
                metadataEditor = this.mMetadataEditor;
            }
            if (l != null) {
                l.onClientMetadataUpdate(metadataEditor);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public OnClientUpdateListener getUpdateListener() {
        return this.mOnClientUpdateListener;
    }
}
