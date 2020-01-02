package android.media.session;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.media.AudioAttributes;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.MediaMetadata.Builder;
import android.media.Rating;
import android.media.VolumeProvider;
import android.media.session.ISessionCallback.Stub;
import android.media.session.MediaSessionManager.RemoteUserInfo;
import android.net.Uri;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

public final class MediaSession {
    public static final int FLAG_EXCLUSIVE_GLOBAL_PRIORITY = 65536;
    @Deprecated
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    @Deprecated
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
    public static final int INVALID_PID = -1;
    public static final int INVALID_UID = -1;
    static final String TAG = "MediaSession";
    private boolean mActive;
    private final ISession mBinder;
    @UnsupportedAppUsage
    private CallbackMessageHandler mCallback;
    private final CallbackStub mCbStub;
    private final MediaController mController;
    private final Object mLock;
    private final int mMaxBitmapSize;
    private PlaybackState mPlaybackState;
    private final Token mSessionToken;
    private VolumeProvider mVolumeProvider;

    public static abstract class Callback {
        private CallbackMessageHandler mHandler;
        private boolean mMediaPlayPauseKeyPending;
        private MediaSession mSession;

        public void onCommand(String command, Bundle args, ResultReceiver cb) {
        }

        public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
            if (!(this.mSession == null || this.mHandler == null)) {
                if (Intent.ACTION_MEDIA_BUTTON.equals(mediaButtonIntent.getAction())) {
                    KeyEvent ke = (KeyEvent) mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                    if (ke != null && ke.getAction() == 0) {
                        PlaybackState state = this.mSession.mPlaybackState;
                        long validActions = state == null ? 0 : state.getActions();
                        int keyCode = ke.getKeyCode();
                        if (keyCode == 79 || keyCode == 85) {
                            if (ke.getRepeatCount() > 0) {
                                handleMediaPlayPauseKeySingleTapIfPending();
                            } else if (this.mMediaPlayPauseKeyPending) {
                                this.mHandler.removeMessages(24);
                                this.mMediaPlayPauseKeyPending = false;
                                if ((validActions & 32) != 0) {
                                    onSkipToNext();
                                }
                            } else {
                                this.mMediaPlayPauseKeyPending = true;
                                MediaSession mediaSession = this.mSession;
                                mediaSession.dispatchMediaButtonDelayed(mediaSession.getCurrentControllerInfo(), mediaButtonIntent, (long) ViewConfiguration.getDoubleTapTimeout());
                            }
                            return true;
                        }
                        handleMediaPlayPauseKeySingleTapIfPending();
                        keyCode = ke.getKeyCode();
                        if (keyCode != 126) {
                            if (keyCode != 127) {
                                switch (keyCode) {
                                    case 86:
                                        if ((1 & validActions) != 0) {
                                            onStop();
                                            return true;
                                        }
                                        break;
                                    case 87:
                                        if ((validActions & 32) != 0) {
                                            onSkipToNext();
                                            return true;
                                        }
                                        break;
                                    case 88:
                                        if ((16 & validActions) != 0) {
                                            onSkipToPrevious();
                                            return true;
                                        }
                                        break;
                                    case 89:
                                        if ((8 & validActions) != 0) {
                                            onRewind();
                                            return true;
                                        }
                                        break;
                                    case 90:
                                        if ((64 & validActions) != 0) {
                                            onFastForward();
                                            return true;
                                        }
                                        break;
                                }
                            } else if ((2 & validActions) != 0) {
                                onPause();
                                return true;
                            }
                        } else if ((4 & validActions) != 0) {
                            onPlay();
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        private void handleMediaPlayPauseKeySingleTapIfPending() {
            if (this.mMediaPlayPauseKeyPending) {
                boolean canPause = false;
                this.mMediaPlayPauseKeyPending = false;
                this.mHandler.removeMessages(24);
                PlaybackState state = this.mSession.mPlaybackState;
                long validActions = state == null ? 0 : state.getActions();
                boolean isPlaying = state != null && state.getState() == 3;
                boolean canPlay = (516 & validActions) != 0;
                if ((514 & validActions) != 0) {
                    canPause = true;
                }
                if (isPlaying && canPause) {
                    onPause();
                } else if (!isPlaying && canPlay) {
                    onPlay();
                }
            }
        }

        public void onPrepare() {
        }

        public void onPrepareFromMediaId(String mediaId, Bundle extras) {
        }

        public void onPrepareFromSearch(String query, Bundle extras) {
        }

        public void onPrepareFromUri(Uri uri, Bundle extras) {
        }

        public void onPlay() {
        }

        public void onPlayFromSearch(String query, Bundle extras) {
        }

        public void onPlayFromMediaId(String mediaId, Bundle extras) {
        }

        public void onPlayFromUri(Uri uri, Bundle extras) {
        }

        public void onSkipToQueueItem(long id) {
        }

        public void onPause() {
        }

        public void onSkipToNext() {
        }

        public void onSkipToPrevious() {
        }

        public void onFastForward() {
        }

        public void onRewind() {
        }

        public void onStop() {
        }

        public void onSeekTo(long pos) {
        }

        public void onSetRating(Rating rating) {
        }

        public void onSetPlaybackSpeed(float speed) {
        }

        public void onCustomAction(String action, Bundle extras) {
        }
    }

    private class CallbackMessageHandler extends Handler {
        private static final int MSG_ADJUST_VOLUME = 22;
        private static final int MSG_COMMAND = 1;
        private static final int MSG_CUSTOM_ACTION = 21;
        private static final int MSG_FAST_FORWARD = 16;
        private static final int MSG_MEDIA_BUTTON = 2;
        private static final int MSG_NEXT = 14;
        private static final int MSG_PAUSE = 12;
        private static final int MSG_PLAY = 7;
        private static final int MSG_PLAY_MEDIA_ID = 8;
        private static final int MSG_PLAY_PAUSE_KEY_DOUBLE_TAP_TIMEOUT = 24;
        private static final int MSG_PLAY_SEARCH = 9;
        private static final int MSG_PLAY_URI = 10;
        private static final int MSG_PREPARE = 3;
        private static final int MSG_PREPARE_MEDIA_ID = 4;
        private static final int MSG_PREPARE_SEARCH = 5;
        private static final int MSG_PREPARE_URI = 6;
        private static final int MSG_PREVIOUS = 15;
        private static final int MSG_RATE = 19;
        private static final int MSG_REWIND = 17;
        private static final int MSG_SEEK_TO = 18;
        private static final int MSG_SET_PLAYBACK_SPEED = 20;
        private static final int MSG_SET_VOLUME = 23;
        private static final int MSG_SKIP_TO_ITEM = 11;
        private static final int MSG_STOP = 13;
        private Callback mCallback;
        private RemoteUserInfo mCurrentControllerInfo;

        CallbackMessageHandler(Looper looper, Callback callback) {
            super(looper);
            this.mCallback = callback;
            this.mCallback.mHandler = this;
        }

        /* Access modifiers changed, original: 0000 */
        public void post(RemoteUserInfo caller, int what, Object obj, Bundle data, long delayMs) {
            Message msg = obtainMessage(what, Pair.create(caller, obj));
            msg.setAsynchronous(true);
            msg.setData(data);
            if (delayMs > 0) {
                sendMessageDelayed(msg, delayMs);
            } else {
                sendMessage(msg);
            }
        }

        public void handleMessage(Message msg) {
            this.mCurrentControllerInfo = (RemoteUserInfo) ((Pair) msg.obj).first;
            Command obj = ((Pair) msg.obj).second;
            VolumeProvider vp;
            switch (msg.what) {
                case 1:
                    Command cmd = obj;
                    this.mCallback.onCommand(cmd.command, cmd.extras, cmd.stub);
                    break;
                case 2:
                    this.mCallback.onMediaButtonEvent((Intent) obj);
                    break;
                case 3:
                    this.mCallback.onPrepare();
                    break;
                case 4:
                    this.mCallback.onPrepareFromMediaId((String) obj, msg.getData());
                    break;
                case 5:
                    this.mCallback.onPrepareFromSearch((String) obj, msg.getData());
                    break;
                case 6:
                    this.mCallback.onPrepareFromUri((Uri) obj, msg.getData());
                    break;
                case 7:
                    this.mCallback.onPlay();
                    break;
                case 8:
                    this.mCallback.onPlayFromMediaId((String) obj, msg.getData());
                    break;
                case 9:
                    this.mCallback.onPlayFromSearch((String) obj, msg.getData());
                    break;
                case 10:
                    this.mCallback.onPlayFromUri((Uri) obj, msg.getData());
                    break;
                case 11:
                    this.mCallback.onSkipToQueueItem(((Long) obj).longValue());
                    break;
                case 12:
                    this.mCallback.onPause();
                    break;
                case 13:
                    this.mCallback.onStop();
                    break;
                case 14:
                    this.mCallback.onSkipToNext();
                    break;
                case 15:
                    this.mCallback.onSkipToPrevious();
                    break;
                case 16:
                    this.mCallback.onFastForward();
                    break;
                case 17:
                    this.mCallback.onRewind();
                    break;
                case 18:
                    this.mCallback.onSeekTo(((Long) obj).longValue());
                    break;
                case 19:
                    this.mCallback.onSetRating((Rating) obj);
                    break;
                case 20:
                    this.mCallback.onSetPlaybackSpeed(((Float) obj).floatValue());
                    break;
                case 21:
                    this.mCallback.onCustomAction((String) obj, msg.getData());
                    break;
                case 22:
                    synchronized (MediaSession.this.mLock) {
                        vp = MediaSession.this.mVolumeProvider;
                    }
                    if (vp != null) {
                        vp.onAdjustVolume(((Integer) obj).intValue());
                        break;
                    }
                    break;
                case 23:
                    synchronized (MediaSession.this.mLock) {
                        vp = MediaSession.this.mVolumeProvider;
                    }
                    if (vp != null) {
                        vp.onSetVolumeTo(((Integer) obj).intValue());
                        break;
                    }
                    break;
                case 24:
                    this.mCallback.handleMediaPlayPauseKeySingleTapIfPending();
                    break;
            }
            this.mCurrentControllerInfo = null;
        }
    }

    public static class CallbackStub extends Stub {
        private WeakReference<MediaSession> mMediaSession;

        public CallbackStub(MediaSession session) {
            this.mMediaSession = new WeakReference(session);
        }

        private static RemoteUserInfo createRemoteUserInfo(String packageName, int pid, int uid) {
            return new RemoteUserInfo(packageName, pid, uid);
        }

        public void onCommand(String packageName, int pid, int uid, ISessionControllerCallback caller, String command, Bundle args, ResultReceiver cb) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchCommand(createRemoteUserInfo(packageName, pid, uid), command, args, cb);
            }
        }

        public void onMediaButton(String packageName, int pid, int uid, Intent mediaButtonIntent, int sequenceNumber, ResultReceiver cb) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                try {
                    session.dispatchMediaButton(createRemoteUserInfo(packageName, pid, uid), mediaButtonIntent);
                } catch (Throwable th) {
                    if (cb != null) {
                        cb.send(sequenceNumber, null);
                    }
                }
            }
            if (cb != null) {
                cb.send(sequenceNumber, null);
            }
        }

        public void onMediaButtonFromController(String packageName, int pid, int uid, ISessionControllerCallback caller, Intent mediaButtonIntent) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchMediaButton(createRemoteUserInfo(packageName, pid, uid), mediaButtonIntent);
            }
        }

        public void onPrepare(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchPrepare(createRemoteUserInfo(packageName, pid, uid));
            }
        }

        public void onPrepareFromMediaId(String packageName, int pid, int uid, ISessionControllerCallback caller, String mediaId, Bundle extras) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchPrepareFromMediaId(createRemoteUserInfo(packageName, pid, uid), mediaId, extras);
            }
        }

        public void onPrepareFromSearch(String packageName, int pid, int uid, ISessionControllerCallback caller, String query, Bundle extras) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchPrepareFromSearch(createRemoteUserInfo(packageName, pid, uid), query, extras);
            }
        }

        public void onPrepareFromUri(String packageName, int pid, int uid, ISessionControllerCallback caller, Uri uri, Bundle extras) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchPrepareFromUri(createRemoteUserInfo(packageName, pid, uid), uri, extras);
            }
        }

        public void onPlay(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchPlay(createRemoteUserInfo(packageName, pid, uid));
            }
        }

        public void onPlayFromMediaId(String packageName, int pid, int uid, ISessionControllerCallback caller, String mediaId, Bundle extras) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchPlayFromMediaId(createRemoteUserInfo(packageName, pid, uid), mediaId, extras);
            }
        }

        public void onPlayFromSearch(String packageName, int pid, int uid, ISessionControllerCallback caller, String query, Bundle extras) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchPlayFromSearch(createRemoteUserInfo(packageName, pid, uid), query, extras);
            }
        }

        public void onPlayFromUri(String packageName, int pid, int uid, ISessionControllerCallback caller, Uri uri, Bundle extras) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchPlayFromUri(createRemoteUserInfo(packageName, pid, uid), uri, extras);
            }
        }

        public void onSkipToTrack(String packageName, int pid, int uid, ISessionControllerCallback caller, long id) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchSkipToItem(createRemoteUserInfo(packageName, pid, uid), id);
            }
        }

        public void onPause(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchPause(createRemoteUserInfo(packageName, pid, uid));
            }
        }

        public void onStop(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchStop(createRemoteUserInfo(packageName, pid, uid));
            }
        }

        public void onNext(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchNext(createRemoteUserInfo(packageName, pid, uid));
            }
        }

        public void onPrevious(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchPrevious(createRemoteUserInfo(packageName, pid, uid));
            }
        }

        public void onFastForward(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchFastForward(createRemoteUserInfo(packageName, pid, uid));
            }
        }

        public void onRewind(String packageName, int pid, int uid, ISessionControllerCallback caller) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchRewind(createRemoteUserInfo(packageName, pid, uid));
            }
        }

        public void onSeekTo(String packageName, int pid, int uid, ISessionControllerCallback caller, long pos) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchSeekTo(createRemoteUserInfo(packageName, pid, uid), pos);
            }
        }

        public void onRate(String packageName, int pid, int uid, ISessionControllerCallback caller, Rating rating) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchRate(createRemoteUserInfo(packageName, pid, uid), rating);
            }
        }

        public void onSetPlaybackSpeed(String packageName, int pid, int uid, ISessionControllerCallback caller, float speed) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchSetPlaybackSpeed(createRemoteUserInfo(packageName, pid, uid), speed);
            }
        }

        public void onCustomAction(String packageName, int pid, int uid, ISessionControllerCallback caller, String action, Bundle args) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchCustomAction(createRemoteUserInfo(packageName, pid, uid), action, args);
            }
        }

        public void onAdjustVolume(String packageName, int pid, int uid, ISessionControllerCallback caller, int direction) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchAdjustVolume(createRemoteUserInfo(packageName, pid, uid), direction);
            }
        }

        public void onSetVolumeTo(String packageName, int pid, int uid, ISessionControllerCallback caller, int value) {
            MediaSession session = (MediaSession) this.mMediaSession.get();
            if (session != null) {
                session.dispatchSetVolumeTo(createRemoteUserInfo(packageName, pid, uid), value);
            }
        }
    }

    private static final class Command {
        public final String command;
        public final Bundle extras;
        public final ResultReceiver stub;

        Command(String command, Bundle extras, ResultReceiver stub) {
            this.command = command;
            this.extras = extras;
            this.stub = stub;
        }
    }

    public static final class QueueItem implements Parcelable {
        public static final Creator<QueueItem> CREATOR = new Creator<QueueItem>() {
            public QueueItem createFromParcel(Parcel p) {
                return new QueueItem(p, null);
            }

            public QueueItem[] newArray(int size) {
                return new QueueItem[size];
            }
        };
        public static final int UNKNOWN_ID = -1;
        private final MediaDescription mDescription;
        @UnsupportedAppUsage
        private final long mId;

        public QueueItem(MediaDescription description, long id) {
            if (description == null) {
                throw new IllegalArgumentException("Description cannot be null.");
            } else if (id != -1) {
                this.mDescription = description;
                this.mId = id;
            } else {
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
        }

        private QueueItem(Parcel in) {
            this.mDescription = (MediaDescription) MediaDescription.CREATOR.createFromParcel(in);
            this.mId = in.readLong();
        }

        public MediaDescription getDescription() {
            return this.mDescription;
        }

        public long getQueueId() {
            return this.mId;
        }

        public void writeToParcel(Parcel dest, int flags) {
            this.mDescription.writeToParcel(dest, flags);
            dest.writeLong(this.mId);
        }

        public int describeContents() {
            return 0;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MediaSession.QueueItem {Description=");
            stringBuilder.append(this.mDescription);
            stringBuilder.append(", Id=");
            stringBuilder.append(this.mId);
            stringBuilder.append(" }");
            return stringBuilder.toString();
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof QueueItem)) {
                return false;
            }
            QueueItem item = (QueueItem) o;
            if (this.mId == item.mId && Objects.equals(this.mDescription, item.mDescription)) {
                return true;
            }
            return false;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SessionFlags {
    }

    public static final class Token implements Parcelable {
        public static final Creator<Token> CREATOR = new Creator<Token>() {
            public Token createFromParcel(Parcel in) {
                return new Token(in);
            }

            public Token[] newArray(int size) {
                return new Token[size];
            }
        };
        private final ISessionController mBinder;
        private final int mUid;

        public Token(ISessionController binder) {
            this.mUid = Process.myUid();
            this.mBinder = binder;
        }

        Token(Parcel in) {
            this.mUid = in.readInt();
            this.mBinder = ISessionController.Stub.asInterface(in.readStrongBinder());
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mUid);
            dest.writeStrongBinder(this.mBinder.asBinder());
        }

        public int hashCode() {
            int result = this.mUid * 31;
            ISessionController iSessionController = this.mBinder;
            return result + (iSessionController == null ? 0 : iSessionController.asBinder().hashCode());
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Token other = (Token) obj;
            if (this.mUid != other.mUid) {
                return false;
            }
            ISessionController iSessionController = this.mBinder;
            if (iSessionController != null && other.mBinder != null) {
                return Objects.equals(iSessionController.asBinder(), other.mBinder.asBinder());
            }
            if (this.mBinder != other.mBinder) {
                z = false;
            }
            return z;
        }

        public int getUid() {
            return this.mUid;
        }

        public ISessionController getBinder() {
            return this.mBinder;
        }
    }

    public MediaSession(Context context, String tag) {
        this(context, tag, null);
    }

    public MediaSession(Context context, String tag, Bundle sessionInfo) {
        this.mLock = new Object();
        this.mActive = false;
        if (context == null) {
            throw new IllegalArgumentException("context cannot be null.");
        } else if (TextUtils.isEmpty(tag)) {
            throw new IllegalArgumentException("tag cannot be null or empty");
        } else if (hasCustomParcelable(sessionInfo)) {
            throw new IllegalArgumentException("sessionInfo shouldn't contain any custom parcelables");
        } else {
            this.mMaxBitmapSize = context.getResources().getDimensionPixelSize(R.dimen.config_mediaMetadataBitmapMaxSize);
            this.mCbStub = new CallbackStub(this);
            try {
                this.mBinder = ((MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE)).createSession(this.mCbStub, tag, sessionInfo);
                this.mSessionToken = new Token(this.mBinder.getController());
                this.mController = new MediaController(context, this.mSessionToken);
            } catch (RemoteException e) {
                throw new RuntimeException("Remote error creating session.", e);
            }
        }
    }

    public void setCallback(Callback callback) {
        setCallback(callback, null);
    }

    public void setCallback(Callback callback, Handler handler) {
        synchronized (this.mLock) {
            if (this.mCallback != null) {
                this.mCallback.mCallback.mSession = null;
                this.mCallback.removeCallbacksAndMessages(null);
            }
            if (callback == null) {
                this.mCallback = null;
                return;
            }
            if (handler == null) {
                handler = new Handler();
            }
            callback.mSession = this;
            this.mCallback = new CallbackMessageHandler(handler.getLooper(), callback);
        }
    }

    public void setSessionActivity(PendingIntent pi) {
        try {
            this.mBinder.setLaunchPendingIntent(pi);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Failure in setLaunchPendingIntent.", e);
        }
    }

    public void setMediaButtonReceiver(PendingIntent mbr) {
        try {
            this.mBinder.setMediaButtonReceiver(mbr);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Failure in setMediaButtonReceiver.", e);
        }
    }

    public void setFlags(int flags) {
        try {
            this.mBinder.setFlags(flags);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Failure in setFlags.", e);
        }
    }

    public void setPlaybackToLocal(AudioAttributes attributes) {
        if (attributes != null) {
            try {
                this.mBinder.setPlaybackToLocal(attributes);
                return;
            } catch (RemoteException e) {
                Log.wtf(TAG, "Failure in setPlaybackToLocal.", e);
                return;
            }
        }
        throw new IllegalArgumentException("Attributes cannot be null for local playback.");
    }

    public void setPlaybackToRemote(VolumeProvider volumeProvider) {
        if (volumeProvider != null) {
            synchronized (this.mLock) {
                this.mVolumeProvider = volumeProvider;
            }
            volumeProvider.setCallback(new android.media.VolumeProvider.Callback() {
                public void onVolumeChanged(VolumeProvider volumeProvider) {
                    MediaSession.this.notifyRemoteVolumeChanged(volumeProvider);
                }
            });
            try {
                this.mBinder.setPlaybackToRemote(volumeProvider.getVolumeControl(), volumeProvider.getMaxVolume());
                this.mBinder.setCurrentVolume(volumeProvider.getCurrentVolume());
                return;
            } catch (RemoteException e) {
                Log.wtf(TAG, "Failure in setPlaybackToRemote.", e);
                return;
            }
        }
        throw new IllegalArgumentException("volumeProvider may not be null!");
    }

    public void setActive(boolean active) {
        if (this.mActive != active) {
            try {
                this.mBinder.setActive(active);
                this.mActive = active;
            } catch (RemoteException e) {
                Log.wtf(TAG, "Failure in setActive.", e);
            }
        }
    }

    public boolean isActive() {
        return this.mActive;
    }

    public void sendSessionEvent(String event, Bundle extras) {
        if (TextUtils.isEmpty(event)) {
            throw new IllegalArgumentException("event cannot be null or empty");
        }
        try {
            this.mBinder.sendEvent(event, extras);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error sending event", e);
        }
    }

    public void release() {
        try {
            this.mBinder.destroySession();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error releasing session: ", e);
        }
    }

    public Token getSessionToken() {
        return this.mSessionToken;
    }

    public MediaController getController() {
        return this.mController;
    }

    public void setPlaybackState(PlaybackState state) {
        this.mPlaybackState = state;
        try {
            this.mBinder.setPlaybackState(state);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Dead object in setPlaybackState.", e);
        }
    }

    public void setMetadata(MediaMetadata metadata) {
        String str;
        long duration = -1;
        int fields = 0;
        MediaDescription description = null;
        if (metadata != null) {
            metadata = new Builder(metadata, this.mMaxBitmapSize).build();
            str = MediaMetadata.METADATA_KEY_DURATION;
            if (metadata.containsKey(str)) {
                duration = metadata.getLong(str);
            }
            fields = metadata.size();
            description = metadata.getDescription();
        }
        str = new StringBuilder();
        str.append("size=");
        str.append(fields);
        str.append(", description=");
        str.append(description);
        try {
            this.mBinder.setMetadata(metadata, duration, str.toString());
        } catch (RemoteException e) {
            Log.wtf(TAG, "Dead object in setPlaybackState.", e);
        }
    }

    public void setQueue(List<QueueItem> queue) {
        try {
            this.mBinder.setQueue(queue == null ? null : new ParceledListSlice(queue));
        } catch (RemoteException e) {
            Log.wtf("Dead object in setQueue.", e);
        }
    }

    public void setQueueTitle(CharSequence title) {
        try {
            this.mBinder.setQueueTitle(title);
        } catch (RemoteException e) {
            Log.wtf("Dead object in setQueueTitle.", e);
        }
    }

    public void setRatingType(int type) {
        try {
            this.mBinder.setRatingType(type);
        } catch (RemoteException e) {
            Log.e(TAG, "Error in setRatingType.", e);
        }
    }

    public void setExtras(Bundle extras) {
        try {
            this.mBinder.setExtras(extras);
        } catch (RemoteException e) {
            Log.wtf("Dead object in setExtras.", e);
        }
    }

    public final RemoteUserInfo getCurrentControllerInfo() {
        CallbackMessageHandler callbackMessageHandler = this.mCallback;
        if (callbackMessageHandler != null && callbackMessageHandler.mCurrentControllerInfo != null) {
            return this.mCallback.mCurrentControllerInfo;
        }
        throw new IllegalStateException("This should be called inside of MediaSession.Callback methods");
    }

    /* JADX WARNING: Missing block: B:9:?, code skipped:
            r3.mBinder.setCurrentVolume(r4.getCurrentVolume());
     */
    /* JADX WARNING: Missing block: B:10:0x0015, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:11:0x0016, code skipped:
            android.util.Log.e(TAG, "Error in notifyVolumeChanged", r0);
     */
    public void notifyRemoteVolumeChanged(android.media.VolumeProvider r4) {
        /*
        r3 = this;
        r0 = r3.mLock;
        monitor-enter(r0);
        if (r4 == 0) goto L_0x001e;
    L_0x0005:
        r1 = r3.mVolumeProvider;	 Catch:{ all -> 0x0027 }
        if (r4 == r1) goto L_0x000a;
    L_0x0009:
        goto L_0x001e;
    L_0x000a:
        monitor-exit(r0);	 Catch:{ all -> 0x0027 }
        r0 = r3.mBinder;	 Catch:{ RemoteException -> 0x0015 }
        r1 = r4.getCurrentVolume();	 Catch:{ RemoteException -> 0x0015 }
        r0.setCurrentVolume(r1);	 Catch:{ RemoteException -> 0x0015 }
        goto L_0x001d;
    L_0x0015:
        r0 = move-exception;
        r1 = "MediaSession";
        r2 = "Error in notifyVolumeChanged";
        android.util.Log.e(r1, r2, r0);
    L_0x001d:
        return;
    L_0x001e:
        r1 = "MediaSession";
        r2 = "Received update from stale volume provider";
        android.util.Log.w(r1, r2);	 Catch:{ all -> 0x0027 }
        monitor-exit(r0);	 Catch:{ all -> 0x0027 }
        return;
    L_0x0027:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0027 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.session.MediaSession.notifyRemoteVolumeChanged(android.media.VolumeProvider):void");
    }

    @UnsupportedAppUsage
    public String getCallingPackage() {
        CallbackMessageHandler callbackMessageHandler = this.mCallback;
        if (callbackMessageHandler == null || callbackMessageHandler.mCurrentControllerInfo == null) {
            return null;
        }
        return this.mCallback.mCurrentControllerInfo.getPackageName();
    }

    public static boolean isActiveState(int state) {
        switch (state) {
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
                return true;
            default:
                return false;
        }
    }

    static boolean hasCustomParcelable(Bundle bundle) {
        if (bundle == null) {
            return false;
        }
        Parcel parcel = null;
        boolean readBundle;
        try {
            parcel = Parcel.obtain();
            parcel.writeBundle(bundle);
            parcel.setDataPosition(0);
            readBundle = parcel.readBundle(null);
            readBundle.size();
            parcel.recycle();
            return false;
        } catch (BadParcelableException e) {
            readBundle = TAG;
            Log.d(readBundle, "Custom parcelable in bundle.", e);
            readBundle = true;
            return readBundle;
        } finally {
            if (parcel != null) {
                parcel.recycle();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchPrepare(RemoteUserInfo caller) {
        postToCallback(caller, 3, null, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchPrepareFromMediaId(RemoteUserInfo caller, String mediaId, Bundle extras) {
        postToCallback(caller, 4, mediaId, extras);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchPrepareFromSearch(RemoteUserInfo caller, String query, Bundle extras) {
        postToCallback(caller, 5, query, extras);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchPrepareFromUri(RemoteUserInfo caller, Uri uri, Bundle extras) {
        postToCallback(caller, 6, uri, extras);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchPlay(RemoteUserInfo caller) {
        postToCallback(caller, 7, null, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchPlayFromMediaId(RemoteUserInfo caller, String mediaId, Bundle extras) {
        postToCallback(caller, 8, mediaId, extras);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchPlayFromSearch(RemoteUserInfo caller, String query, Bundle extras) {
        postToCallback(caller, 9, query, extras);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchPlayFromUri(RemoteUserInfo caller, Uri uri, Bundle extras) {
        postToCallback(caller, 10, uri, extras);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchSkipToItem(RemoteUserInfo caller, long id) {
        postToCallback(caller, 11, Long.valueOf(id), null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchPause(RemoteUserInfo caller) {
        postToCallback(caller, 12, null, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchStop(RemoteUserInfo caller) {
        postToCallback(caller, 13, null, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchNext(RemoteUserInfo caller) {
        postToCallback(caller, 14, null, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchPrevious(RemoteUserInfo caller) {
        postToCallback(caller, 15, null, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchFastForward(RemoteUserInfo caller) {
        postToCallback(caller, 16, null, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchRewind(RemoteUserInfo caller) {
        postToCallback(caller, 17, null, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchSeekTo(RemoteUserInfo caller, long pos) {
        postToCallback(caller, 18, Long.valueOf(pos), null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchRate(RemoteUserInfo caller, Rating rating) {
        postToCallback(caller, 19, rating, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchSetPlaybackSpeed(RemoteUserInfo caller, float speed) {
        postToCallback(caller, 20, Float.valueOf(speed), null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchCustomAction(RemoteUserInfo caller, String action, Bundle args) {
        postToCallback(caller, 21, action, args);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchMediaButton(RemoteUserInfo caller, Intent mediaButtonIntent) {
        postToCallback(caller, 2, mediaButtonIntent, null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchMediaButtonDelayed(RemoteUserInfo info, Intent mediaButtonIntent, long delay) {
        postToCallbackDelayed(info, 24, mediaButtonIntent, null, delay);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchAdjustVolume(RemoteUserInfo caller, int direction) {
        postToCallback(caller, 22, Integer.valueOf(direction), null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchSetVolumeTo(RemoteUserInfo caller, int volume) {
        postToCallback(caller, 23, Integer.valueOf(volume), null);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchCommand(RemoteUserInfo caller, String command, Bundle args, ResultReceiver resultCb) {
        postToCallback(caller, 1, new Command(command, args, resultCb), null);
    }

    /* Access modifiers changed, original: 0000 */
    public void postToCallback(RemoteUserInfo caller, int what, Object obj, Bundle data) {
        postToCallbackDelayed(caller, what, obj, data, 0);
    }

    /* Access modifiers changed, original: 0000 */
    public void postToCallbackDelayed(RemoteUserInfo caller, int what, Object obj, Bundle data, long delay) {
        synchronized (this.mLock) {
            if (this.mCallback != null) {
                this.mCallback.post(caller, what, obj, data, delay);
            }
        }
    }
}
