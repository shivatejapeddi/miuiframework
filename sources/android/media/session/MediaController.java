package android.media.session;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.media.AudioAttributes;
import android.media.MediaMetadata;
import android.media.Rating;
import android.media.session.ISessionControllerCallback.Stub;
import android.media.session.MediaSession.QueueItem;
import android.media.session.MediaSession.Token;
import android.media.session.PlaybackState.CustomAction;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class MediaController {
    private static final int MSG_DESTROYED = 8;
    private static final int MSG_EVENT = 1;
    private static final int MSG_UPDATE_EXTRAS = 7;
    private static final int MSG_UPDATE_METADATA = 3;
    private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
    private static final int MSG_UPDATE_QUEUE = 5;
    private static final int MSG_UPDATE_QUEUE_TITLE = 6;
    private static final int MSG_UPDATE_VOLUME = 4;
    private static final String TAG = "MediaController";
    private final ArrayList<MessageHandler> mCallbacks = new ArrayList();
    private boolean mCbRegistered = false;
    private final CallbackStub mCbStub = new CallbackStub(this);
    private final Context mContext;
    private final Object mLock = new Object();
    private String mPackageName;
    private final ISessionController mSessionBinder;
    private Bundle mSessionInfo;
    private String mTag;
    private final Token mToken;
    private final TransportControls mTransportControls;

    public static abstract class Callback {
        public void onSessionDestroyed() {
        }

        public void onSessionEvent(String event, Bundle extras) {
        }

        public void onPlaybackStateChanged(PlaybackState state) {
        }

        public void onMetadataChanged(MediaMetadata metadata) {
        }

        public void onQueueChanged(List<QueueItem> list) {
        }

        public void onQueueTitleChanged(CharSequence title) {
        }

        public void onExtrasChanged(Bundle extras) {
        }

        public void onAudioInfoChanged(PlaybackInfo info) {
        }
    }

    private static final class CallbackStub extends Stub {
        private final WeakReference<MediaController> mController;

        CallbackStub(MediaController controller) {
            this.mController = new WeakReference(controller);
        }

        public void onSessionDestroyed() {
            MediaController controller = (MediaController) this.mController.get();
            if (controller != null) {
                controller.postMessage(8, null, null);
            }
        }

        public void onEvent(String event, Bundle extras) {
            MediaController controller = (MediaController) this.mController.get();
            if (controller != null) {
                controller.postMessage(1, event, extras);
            }
        }

        public void onPlaybackStateChanged(PlaybackState state) {
            MediaController controller = (MediaController) this.mController.get();
            if (controller != null) {
                controller.postMessage(2, state, null);
            }
        }

        public void onMetadataChanged(MediaMetadata metadata) {
            MediaController controller = (MediaController) this.mController.get();
            if (controller != null) {
                controller.postMessage(3, metadata, null);
            }
        }

        public void onQueueChanged(ParceledListSlice queue) {
            MediaController controller = (MediaController) this.mController.get();
            if (controller != null) {
                controller.postMessage(5, queue, null);
            }
        }

        public void onQueueTitleChanged(CharSequence title) {
            MediaController controller = (MediaController) this.mController.get();
            if (controller != null) {
                controller.postMessage(6, title, null);
            }
        }

        public void onExtrasChanged(Bundle extras) {
            MediaController controller = (MediaController) this.mController.get();
            if (controller != null) {
                controller.postMessage(7, extras, null);
            }
        }

        public void onVolumeInfoChanged(PlaybackInfo info) {
            MediaController controller = (MediaController) this.mController.get();
            if (controller != null) {
                controller.postMessage(4, info, null);
            }
        }
    }

    private static final class MessageHandler extends Handler {
        private final Callback mCallback;
        private boolean mRegistered = false;

        MessageHandler(Looper looper, Callback cb) {
            super(looper);
            this.mCallback = cb;
        }

        public void handleMessage(Message msg) {
            if (this.mRegistered) {
                switch (msg.what) {
                    case 1:
                        this.mCallback.onSessionEvent((String) msg.obj, msg.getData());
                        break;
                    case 2:
                        this.mCallback.onPlaybackStateChanged((PlaybackState) msg.obj);
                        break;
                    case 3:
                        this.mCallback.onMetadataChanged((MediaMetadata) msg.obj);
                        break;
                    case 4:
                        this.mCallback.onAudioInfoChanged((PlaybackInfo) msg.obj);
                        break;
                    case 5:
                        List list;
                        Callback callback = this.mCallback;
                        if (msg.obj == null) {
                            list = null;
                        } else {
                            list = ((ParceledListSlice) msg.obj).getList();
                        }
                        callback.onQueueChanged(list);
                        break;
                    case 6:
                        this.mCallback.onQueueTitleChanged((CharSequence) msg.obj);
                        break;
                    case 7:
                        this.mCallback.onExtrasChanged((Bundle) msg.obj);
                        break;
                    case 8:
                        this.mCallback.onSessionDestroyed();
                        break;
                }
            }
        }

        public void post(int what, Object obj, Bundle data) {
            Message msg = obtainMessage(what, obj);
            msg.setAsynchronous(true);
            msg.setData(data);
            msg.sendToTarget();
        }
    }

    public static final class PlaybackInfo implements Parcelable {
        public static final Creator<PlaybackInfo> CREATOR = new Creator<PlaybackInfo>() {
            public PlaybackInfo createFromParcel(Parcel in) {
                return new PlaybackInfo(in);
            }

            public PlaybackInfo[] newArray(int size) {
                return new PlaybackInfo[size];
            }
        };
        public static final int PLAYBACK_TYPE_LOCAL = 1;
        public static final int PLAYBACK_TYPE_REMOTE = 2;
        private final AudioAttributes mAudioAttrs;
        private final int mCurrentVolume;
        private final int mMaxVolume;
        private final int mVolumeControl;
        private final int mVolumeType;

        public PlaybackInfo(int type, int control, int max, int current, AudioAttributes attrs) {
            this.mVolumeType = type;
            this.mVolumeControl = control;
            this.mMaxVolume = max;
            this.mCurrentVolume = current;
            this.mAudioAttrs = attrs;
        }

        PlaybackInfo(Parcel in) {
            this.mVolumeType = in.readInt();
            this.mVolumeControl = in.readInt();
            this.mMaxVolume = in.readInt();
            this.mCurrentVolume = in.readInt();
            this.mAudioAttrs = (AudioAttributes) in.readParcelable(null);
        }

        public int getPlaybackType() {
            return this.mVolumeType;
        }

        public int getVolumeControl() {
            return this.mVolumeControl;
        }

        public int getMaxVolume() {
            return this.mMaxVolume;
        }

        public int getCurrentVolume() {
            return this.mCurrentVolume;
        }

        public AudioAttributes getAudioAttributes() {
            return this.mAudioAttrs;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("volumeType=");
            stringBuilder.append(this.mVolumeType);
            stringBuilder.append(", volumeControl=");
            stringBuilder.append(this.mVolumeControl);
            stringBuilder.append(", maxVolume=");
            stringBuilder.append(this.mMaxVolume);
            stringBuilder.append(", currentVolume=");
            stringBuilder.append(this.mCurrentVolume);
            stringBuilder.append(", audioAttrs=");
            stringBuilder.append(this.mAudioAttrs);
            return stringBuilder.toString();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mVolumeType);
            dest.writeInt(this.mVolumeControl);
            dest.writeInt(this.mMaxVolume);
            dest.writeInt(this.mCurrentVolume);
            dest.writeParcelable(this.mAudioAttrs, flags);
        }
    }

    public final class TransportControls {
        private static final String TAG = "TransportController";

        private TransportControls() {
        }

        public void prepare() {
            try {
                MediaController.this.mSessionBinder.prepare(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling prepare.", e);
            }
        }

        public void prepareFromMediaId(String mediaId, Bundle extras) {
            if (TextUtils.isEmpty(mediaId)) {
                throw new IllegalArgumentException("You must specify a non-empty String for prepareFromMediaId.");
            }
            try {
                MediaController.this.mSessionBinder.prepareFromMediaId(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, mediaId, extras);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error calling prepare(");
                stringBuilder.append(mediaId);
                stringBuilder.append(").");
                Log.wtf(TAG, stringBuilder.toString(), e);
            }
        }

        public void prepareFromSearch(String query, Bundle extras) {
            if (query == null) {
                query = "";
            }
            try {
                MediaController.this.mSessionBinder.prepareFromSearch(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, query, extras);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error calling prepare(");
                stringBuilder.append(query);
                stringBuilder.append(").");
                Log.wtf(TAG, stringBuilder.toString(), e);
            }
        }

        public void prepareFromUri(Uri uri, Bundle extras) {
            if (uri == null || Uri.EMPTY.equals(uri)) {
                throw new IllegalArgumentException("You must specify a non-empty Uri for prepareFromUri.");
            }
            try {
                MediaController.this.mSessionBinder.prepareFromUri(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, uri, extras);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error calling prepare(");
                stringBuilder.append(uri);
                stringBuilder.append(").");
                Log.wtf(TAG, stringBuilder.toString(), e);
            }
        }

        public void play() {
            try {
                MediaController.this.mSessionBinder.play(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling play.", e);
            }
        }

        public void playFromMediaId(String mediaId, Bundle extras) {
            if (TextUtils.isEmpty(mediaId)) {
                throw new IllegalArgumentException("You must specify a non-empty String for playFromMediaId.");
            }
            try {
                MediaController.this.mSessionBinder.playFromMediaId(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, mediaId, extras);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error calling play(");
                stringBuilder.append(mediaId);
                stringBuilder.append(").");
                Log.wtf(TAG, stringBuilder.toString(), e);
            }
        }

        public void playFromSearch(String query, Bundle extras) {
            if (query == null) {
                query = "";
            }
            try {
                MediaController.this.mSessionBinder.playFromSearch(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, query, extras);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error calling play(");
                stringBuilder.append(query);
                stringBuilder.append(").");
                Log.wtf(TAG, stringBuilder.toString(), e);
            }
        }

        public void playFromUri(Uri uri, Bundle extras) {
            if (uri == null || Uri.EMPTY.equals(uri)) {
                throw new IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
            }
            try {
                MediaController.this.mSessionBinder.playFromUri(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, uri, extras);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error calling play(");
                stringBuilder.append(uri);
                stringBuilder.append(").");
                Log.wtf(TAG, stringBuilder.toString(), e);
            }
        }

        public void skipToQueueItem(long id) {
            try {
                MediaController.this.mSessionBinder.skipToQueueItem(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, id);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error calling skipToItem(");
                stringBuilder.append(id);
                stringBuilder.append(").");
                Log.wtf(TAG, stringBuilder.toString(), e);
            }
        }

        public void pause() {
            try {
                MediaController.this.mSessionBinder.pause(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling pause.", e);
            }
        }

        public void stop() {
            try {
                MediaController.this.mSessionBinder.stop(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling stop.", e);
            }
        }

        public void seekTo(long pos) {
            try {
                MediaController.this.mSessionBinder.seekTo(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, pos);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling seekTo.", e);
            }
        }

        public void fastForward() {
            try {
                MediaController.this.mSessionBinder.fastForward(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling fastForward.", e);
            }
        }

        public void skipToNext() {
            try {
                MediaController.this.mSessionBinder.next(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling next.", e);
            }
        }

        public void rewind() {
            try {
                MediaController.this.mSessionBinder.rewind(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling rewind.", e);
            }
        }

        public void skipToPrevious() {
            try {
                MediaController.this.mSessionBinder.previous(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling previous.", e);
            }
        }

        public void setRating(Rating rating) {
            try {
                MediaController.this.mSessionBinder.rate(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, rating);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling rate.", e);
            }
        }

        public void setPlaybackSpeed(float speed) {
            if (speed != 0.0f) {
                try {
                    MediaController.this.mSessionBinder.setPlaybackSpeed(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, speed);
                    return;
                } catch (RemoteException e) {
                    Log.wtf(TAG, "Error calling setPlaybackSpeed.", e);
                    return;
                }
            }
            throw new IllegalArgumentException("speed must not be zero");
        }

        public void sendCustomAction(CustomAction customAction, Bundle args) {
            if (customAction != null) {
                sendCustomAction(customAction.getAction(), args);
                return;
            }
            throw new IllegalArgumentException("CustomAction cannot be null.");
        }

        public void sendCustomAction(String action, Bundle args) {
            if (TextUtils.isEmpty(action)) {
                throw new IllegalArgumentException("CustomAction cannot be null.");
            }
            try {
                MediaController.this.mSessionBinder.sendCustomAction(MediaController.this.mContext.getPackageName(), MediaController.this.mCbStub, action, args);
            } catch (RemoteException e) {
                Log.d(TAG, "Dead object in sendCustomAction.", e);
            }
        }
    }

    public MediaController(Context context, Token token) {
        if (context == null) {
            throw new IllegalArgumentException("context shouldn't be null");
        } else if (token == null) {
            throw new IllegalArgumentException("token shouldn't be null");
        } else if (token.getBinder() != null) {
            this.mSessionBinder = token.getBinder();
            this.mTransportControls = new TransportControls();
            this.mToken = token;
            this.mContext = context;
        } else {
            throw new IllegalArgumentException("token.getBinder() shouldn't be null");
        }
    }

    public TransportControls getTransportControls() {
        return this.mTransportControls;
    }

    public boolean dispatchMediaButtonEvent(KeyEvent keyEvent) {
        if (keyEvent == null) {
            throw new IllegalArgumentException("KeyEvent may not be null");
        } else if (!KeyEvent.isMediaSessionKey(keyEvent.getKeyCode())) {
            return false;
        } else {
            try {
                return this.mSessionBinder.sendMediaButton(this.mContext.getPackageName(), this.mCbStub, keyEvent);
            } catch (RemoteException e) {
                return false;
            }
        }
    }

    public PlaybackState getPlaybackState() {
        try {
            return this.mSessionBinder.getPlaybackState();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getPlaybackState.", e);
            return null;
        }
    }

    public MediaMetadata getMetadata() {
        try {
            return this.mSessionBinder.getMetadata();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getMetadata.", e);
            return null;
        }
    }

    public List<QueueItem> getQueue() {
        List<QueueItem> list = null;
        try {
            ParceledListSlice list2 = this.mSessionBinder.getQueue();
            if (list2 != null) {
                list = list2.getList();
            }
            return list;
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getQueue.", e);
            return null;
        }
    }

    public CharSequence getQueueTitle() {
        try {
            return this.mSessionBinder.getQueueTitle();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getQueueTitle", e);
            return null;
        }
    }

    public Bundle getExtras() {
        try {
            return this.mSessionBinder.getExtras();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getExtras", e);
            return null;
        }
    }

    public int getRatingType() {
        try {
            return this.mSessionBinder.getRatingType();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getRatingType.", e);
            return 0;
        }
    }

    public long getFlags() {
        try {
            return this.mSessionBinder.getFlags();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getFlags.", e);
            return 0;
        }
    }

    public PlaybackInfo getPlaybackInfo() {
        try {
            return this.mSessionBinder.getVolumeAttributes();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getAudioInfo.", e);
            return null;
        }
    }

    public PendingIntent getSessionActivity() {
        try {
            return this.mSessionBinder.getLaunchPendingIntent();
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling getPendingIntent.", e);
            return null;
        }
    }

    public Token getSessionToken() {
        return this.mToken;
    }

    public void setVolumeTo(int value, int flags) {
        try {
            this.mSessionBinder.setVolumeTo(this.mContext.getPackageName(), this.mContext.getOpPackageName(), this.mCbStub, value, flags);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling setVolumeTo.", e);
        }
    }

    public void adjustVolume(int direction, int flags) {
        try {
            this.mSessionBinder.adjustVolume(this.mContext.getPackageName(), this.mContext.getOpPackageName(), this.mCbStub, direction, flags);
        } catch (RemoteException e) {
            Log.wtf(TAG, "Error calling adjustVolumeBy.", e);
        }
    }

    public void registerCallback(Callback callback) {
        registerCallback(callback, null);
    }

    public void registerCallback(Callback callback, Handler handler) {
        if (callback != null) {
            Handler handler2;
            if (handler == null) {
                handler2 = new Handler();
            } else {
                handler2 = handler;
            }
            synchronized (this.mLock) {
                addCallbackLocked(callback, handler2);
            }
            return;
        }
        throw new IllegalArgumentException("callback must not be null");
    }

    public void unregisterCallback(Callback callback) {
        if (callback != null) {
            synchronized (this.mLock) {
                removeCallbackLocked(callback);
            }
            return;
        }
        throw new IllegalArgumentException("callback must not be null");
    }

    public void sendCommand(String command, Bundle args, ResultReceiver cb) {
        if (TextUtils.isEmpty(command)) {
            throw new IllegalArgumentException("command cannot be null or empty");
        }
        try {
            this.mSessionBinder.sendCommand(this.mContext.getPackageName(), this.mCbStub, command, args, cb);
        } catch (RemoteException e) {
            Log.d(TAG, "Dead object in sendCommand.", e);
        }
    }

    public String getPackageName() {
        if (this.mPackageName == null) {
            try {
                this.mPackageName = this.mSessionBinder.getPackageName();
            } catch (RemoteException e) {
                Log.d(TAG, "Dead object in getPackageName.", e);
            }
        }
        return this.mPackageName;
    }

    public Bundle getSessionInfo() {
        String str = TAG;
        Bundle bundle = this.mSessionInfo;
        if (bundle != null) {
            return new Bundle(bundle);
        }
        try {
            this.mSessionInfo = this.mSessionBinder.getSessionInfo();
        } catch (RemoteException e) {
            Log.d(str, "Dead object in getSessionInfo.", e);
        }
        bundle = this.mSessionInfo;
        if (bundle == null) {
            Log.w(str, "sessionInfo shouldn't be null.");
            this.mSessionInfo = Bundle.EMPTY;
        } else if (MediaSession.hasCustomParcelable(bundle)) {
            Log.w(str, "sessionInfo contains custom parcelable. Ignoring.");
            this.mSessionInfo = Bundle.EMPTY;
        }
        return new Bundle(this.mSessionInfo);
    }

    public String getTag() {
        if (this.mTag == null) {
            try {
                this.mTag = this.mSessionBinder.getTag();
            } catch (RemoteException e) {
                Log.d(TAG, "Dead object in getTag.", e);
            }
        }
        return this.mTag;
    }

    /* Access modifiers changed, original: 0000 */
    public ISessionController getSessionBinder() {
        return this.mSessionBinder;
    }

    @UnsupportedAppUsage
    public boolean controlsSameSession(MediaController other) {
        boolean z = false;
        if (other == null) {
            return false;
        }
        if (this.mSessionBinder.asBinder() == other.getSessionBinder().asBinder()) {
            z = true;
        }
        return z;
    }

    private void addCallbackLocked(Callback cb, Handler handler) {
        MessageHandler handlerForCallbackLocked = getHandlerForCallbackLocked(cb);
        String str = TAG;
        if (handlerForCallbackLocked != null) {
            Log.w(str, "Callback is already added, ignoring");
            return;
        }
        handlerForCallbackLocked = new MessageHandler(handler.getLooper(), cb);
        this.mCallbacks.add(handlerForCallbackLocked);
        handlerForCallbackLocked.mRegistered = true;
        if (!this.mCbRegistered) {
            try {
                this.mSessionBinder.registerCallback(this.mContext.getPackageName(), this.mCbStub);
                this.mCbRegistered = true;
            } catch (RemoteException e) {
                Log.e(str, "Dead object in registerCallback", e);
            }
        }
    }

    private boolean removeCallbackLocked(Callback cb) {
        boolean success = false;
        int i = this.mCallbacks.size();
        while (true) {
            i--;
            if (i < 0) {
                break;
            }
            MessageHandler handler = (MessageHandler) this.mCallbacks.get(i);
            if (cb == handler.mCallback) {
                this.mCallbacks.remove(i);
                success = true;
                handler.mRegistered = false;
            }
        }
        if (this.mCbRegistered && this.mCallbacks.size() == 0) {
            try {
                this.mSessionBinder.unregisterCallback(this.mCbStub);
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in removeCallbackLocked");
            }
            this.mCbRegistered = false;
        }
        return success;
    }

    private MessageHandler getHandlerForCallbackLocked(Callback cb) {
        if (cb != null) {
            for (int i = this.mCallbacks.size() - 1; i >= 0; i--) {
                MessageHandler handler = (MessageHandler) this.mCallbacks.get(i);
                if (cb == handler.mCallback) {
                    return handler;
                }
            }
            return null;
        }
        throw new IllegalArgumentException("Callback cannot be null");
    }

    private void postMessage(int what, Object obj, Bundle data) {
        synchronized (this.mLock) {
            for (int i = this.mCallbacks.size() - 1; i >= 0; i--) {
                ((MessageHandler) this.mCallbacks.get(i)).post(what, obj, data);
            }
        }
    }
}
