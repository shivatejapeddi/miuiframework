package android.media.session;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.media.IRemoteVolumeController;
import android.media.Session2Token;
import android.media.session.ICallback.Stub;
import android.media.session.MediaSession.CallbackStub;
import android.media.session.MediaSession.Token;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import com.android.internal.annotations.GuardedBy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MediaSessionManager {
    public static final int RESULT_MEDIA_KEY_HANDLED = 1;
    public static final int RESULT_MEDIA_KEY_NOT_HANDLED = 0;
    private static final String TAG = "SessionManager";
    private CallbackImpl mCallback;
    private Context mContext;
    @GuardedBy({"mLock"})
    private final ArrayMap<OnActiveSessionsChangedListener, SessionsChangedWrapper> mListeners = new ArrayMap();
    private final Object mLock = new Object();
    private OnMediaKeyListenerImpl mOnMediaKeyListener;
    private OnVolumeKeyLongPressListenerImpl mOnVolumeKeyLongPressListener;
    private final ISessionManager mService;
    @GuardedBy({"mLock"})
    private final ArrayMap<OnSession2TokensChangedListener, Session2TokensChangedWrapper> mSession2TokensListeners = new ArrayMap();

    public interface OnActiveSessionsChangedListener {
        void onActiveSessionsChanged(List<MediaController> list);
    }

    public static abstract class Callback {
        public abstract void onAddressedPlayerChanged(ComponentName componentName);

        public abstract void onAddressedPlayerChanged(Token token);

        public abstract void onMediaKeyEventDispatched(KeyEvent keyEvent, ComponentName componentName);

        public abstract void onMediaKeyEventDispatched(KeyEvent keyEvent, Token token);
    }

    private static final class CallbackImpl extends Stub {
        private final Callback mCallback;
        private final Handler mHandler;

        public CallbackImpl(Callback callback, Handler handler) {
            this.mCallback = callback;
            this.mHandler = handler;
        }

        public void onMediaKeyEventDispatchedToMediaSession(final KeyEvent event, final Token sessionToken) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    CallbackImpl.this.mCallback.onMediaKeyEventDispatched(event, sessionToken);
                }
            });
        }

        public void onMediaKeyEventDispatchedToMediaButtonReceiver(final KeyEvent event, final ComponentName mediaButtonReceiver) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    CallbackImpl.this.mCallback.onMediaKeyEventDispatched(event, mediaButtonReceiver);
                }
            });
        }

        public void onAddressedPlayerChangedToMediaSession(final Token sessionToken) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    CallbackImpl.this.mCallback.onAddressedPlayerChanged(sessionToken);
                }
            });
        }

        public void onAddressedPlayerChangedToMediaButtonReceiver(final ComponentName mediaButtonReceiver) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    CallbackImpl.this.mCallback.onAddressedPlayerChanged(mediaButtonReceiver);
                }
            });
        }
    }

    @SystemApi
    public interface OnMediaKeyListener {
        boolean onMediaKey(KeyEvent keyEvent);
    }

    private static final class OnMediaKeyListenerImpl extends IOnMediaKeyListener.Stub {
        private Handler mHandler;
        private OnMediaKeyListener mListener;

        public OnMediaKeyListenerImpl(OnMediaKeyListener listener, Handler handler) {
            this.mListener = listener;
            this.mHandler = handler;
        }

        public void onMediaKey(final KeyEvent event, final ResultReceiver result) {
            if (this.mListener != null) {
                Handler handler = this.mHandler;
                if (handler != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            boolean handled = OnMediaKeyListenerImpl.this.mListener.onMediaKey(event);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("The media key listener is returned ");
                            stringBuilder.append(handled);
                            Log.d(MediaSessionManager.TAG, stringBuilder.toString());
                            ResultReceiver resultReceiver = result;
                            if (resultReceiver != null) {
                                resultReceiver.send(handled, null);
                            }
                        }
                    });
                    return;
                }
            }
            Log.w(MediaSessionManager.TAG, "Failed to call media key listener. Either mListener or mHandler is null");
        }
    }

    public interface OnSession2TokensChangedListener {
        void onSession2TokensChanged(List<Session2Token> list);
    }

    @SystemApi
    public interface OnVolumeKeyLongPressListener {
        void onVolumeKeyLongPress(KeyEvent keyEvent);
    }

    private static final class OnVolumeKeyLongPressListenerImpl extends IOnVolumeKeyLongPressListener.Stub {
        private Handler mHandler;
        private OnVolumeKeyLongPressListener mListener;

        public OnVolumeKeyLongPressListenerImpl(OnVolumeKeyLongPressListener listener, Handler handler) {
            this.mListener = listener;
            this.mHandler = handler;
        }

        public void onVolumeKeyLongPress(final KeyEvent event) {
            if (this.mListener != null) {
                Handler handler = this.mHandler;
                if (handler != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            OnVolumeKeyLongPressListenerImpl.this.mListener.onVolumeKeyLongPress(event);
                        }
                    });
                    return;
                }
            }
            Log.w(MediaSessionManager.TAG, "Failed to call volume key long-press listener. Either mListener or mHandler is null");
        }
    }

    public static final class RemoteUserInfo {
        private final String mPackageName;
        private final int mPid;
        private final int mUid;

        public RemoteUserInfo(String packageName, int pid, int uid) {
            this.mPackageName = packageName;
            this.mPid = pid;
            this.mUid = uid;
        }

        public String getPackageName() {
            return this.mPackageName;
        }

        public int getPid() {
            return this.mPid;
        }

        public int getUid() {
            return this.mUid;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof RemoteUserInfo)) {
                return false;
            }
            boolean z = true;
            if (this == obj) {
                return true;
            }
            RemoteUserInfo otherUserInfo = (RemoteUserInfo) obj;
            if (!(TextUtils.equals(this.mPackageName, otherUserInfo.mPackageName) && this.mPid == otherUserInfo.mPid && this.mUid == otherUserInfo.mUid)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{this.mPackageName, Integer.valueOf(this.mPid), Integer.valueOf(this.mUid)});
        }
    }

    private static final class Session2TokensChangedWrapper {
        private final Handler mHandler;
        private final OnSession2TokensChangedListener mListener;
        private final ISession2TokensListener.Stub mStub = new ISession2TokensListener.Stub() {
            public /* synthetic */ void lambda$onSession2TokensChanged$0$MediaSessionManager$Session2TokensChangedWrapper$1(List tokens) {
                Session2TokensChangedWrapper.this.mListener.onSession2TokensChanged(tokens);
            }

            public void onSession2TokensChanged(List<Session2Token> tokens) {
                Session2TokensChangedWrapper.this.mHandler.post(new -$$Lambda$MediaSessionManager$Session2TokensChangedWrapper$1$4_TH2zkLY97pxK-e1EPxtPhZwdk(this, tokens));
            }
        };

        Session2TokensChangedWrapper(OnSession2TokensChangedListener listener, Handler handler) {
            Handler handler2;
            this.mListener = listener;
            if (handler == null) {
                handler2 = new Handler();
            } else {
                handler2 = new Handler(handler.getLooper());
            }
            this.mHandler = handler2;
        }

        public ISession2TokensListener.Stub getStub() {
            return this.mStub;
        }
    }

    private static final class SessionsChangedWrapper {
        private Context mContext;
        private Handler mHandler;
        private OnActiveSessionsChangedListener mListener;
        private final IActiveSessionsListener.Stub mStub = new IActiveSessionsListener.Stub() {
            public void onActiveSessionsChanged(final List<Token> tokens) {
                Handler handler = SessionsChangedWrapper.this.mHandler;
                if (handler != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Context context = SessionsChangedWrapper.this.mContext;
                            if (context != null) {
                                ArrayList<MediaController> controllers = new ArrayList();
                                int size = tokens.size();
                                for (int i = 0; i < size; i++) {
                                    controllers.add(new MediaController(context, (Token) tokens.get(i)));
                                }
                                OnActiveSessionsChangedListener listener = SessionsChangedWrapper.this.mListener;
                                if (listener != null) {
                                    listener.onActiveSessionsChanged(controllers);
                                }
                            }
                        }
                    });
                }
            }
        };

        public SessionsChangedWrapper(Context context, OnActiveSessionsChangedListener listener, Handler handler) {
            this.mContext = context;
            this.mListener = listener;
            this.mHandler = handler;
        }

        private void release() {
            this.mListener = null;
            this.mContext = null;
            this.mHandler = null;
        }
    }

    public MediaSessionManager(Context context) {
        this.mContext = context;
        this.mService = ISessionManager.Stub.asInterface(ServiceManager.getService(Context.MEDIA_SESSION_SERVICE));
    }

    public ISession createSession(CallbackStub cbStub, String tag, Bundle sessionInfo) {
        try {
            return this.mService.createSession(this.mContext.getPackageName(), cbStub, tag, sessionInfo, UserHandle.myUserId());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void notifySession2Created(Session2Token token) {
        if (token == null) {
            throw new IllegalArgumentException("token shouldn't be null");
        } else if (token.getType() == 0) {
            try {
                this.mService.notifySession2Created(token);
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        } else {
            throw new IllegalArgumentException("token's type should be TYPE_SESSION");
        }
    }

    public List<MediaController> getActiveSessions(ComponentName notificationListener) {
        return getActiveSessionsForUser(notificationListener, UserHandle.myUserId());
    }

    @UnsupportedAppUsage
    public List<MediaController> getActiveSessionsForUser(ComponentName notificationListener, int userId) {
        ArrayList<MediaController> controllers = new ArrayList();
        try {
            List<Token> tokens = this.mService.getSessions(notificationListener, userId);
            int size = tokens.size();
            for (int i = 0; i < size; i++) {
                controllers.add(new MediaController(this.mContext, (Token) tokens.get(i)));
            }
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get active sessions: ", e);
        }
        return controllers;
    }

    public List<Session2Token> getSession2Tokens() {
        return getSession2Tokens(UserHandle.myUserId());
    }

    public List<Session2Token> getSession2Tokens(int userId) {
        try {
            ParceledListSlice slice = this.mService.getSession2Tokens(userId);
            return slice == null ? new ArrayList() : slice.getList();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to get session tokens", e);
            return new ArrayList();
        }
    }

    public void addOnActiveSessionsChangedListener(OnActiveSessionsChangedListener sessionListener, ComponentName notificationListener) {
        addOnActiveSessionsChangedListener(sessionListener, notificationListener, null);
    }

    public void addOnActiveSessionsChangedListener(OnActiveSessionsChangedListener sessionListener, ComponentName notificationListener, Handler handler) {
        addOnActiveSessionsChangedListener(sessionListener, notificationListener, UserHandle.myUserId(), handler);
    }

    public void addOnActiveSessionsChangedListener(OnActiveSessionsChangedListener sessionListener, ComponentName notificationListener, int userId, Handler handler) {
        if (sessionListener != null) {
            Handler handler2;
            if (handler == null) {
                handler2 = new Handler();
            } else {
                handler2 = handler;
            }
            synchronized (this.mLock) {
                if (this.mListeners.get(sessionListener) != null) {
                    Log.w(TAG, "Attempted to add session listener twice, ignoring.");
                    return;
                }
                SessionsChangedWrapper wrapper = new SessionsChangedWrapper(this.mContext, sessionListener, handler2);
                try {
                    this.mService.addSessionsListener(wrapper.mStub, notificationListener, userId);
                    this.mListeners.put(sessionListener, wrapper);
                } catch (RemoteException e) {
                    Log.e(TAG, "Error in addOnActiveSessionsChangedListener.", e);
                }
            }
        } else {
            throw new IllegalArgumentException("listener may not be null");
        }
    }

    public void removeOnActiveSessionsChangedListener(OnActiveSessionsChangedListener listener) {
        if (listener != null) {
            synchronized (this.mLock) {
                SessionsChangedWrapper wrapper = (SessionsChangedWrapper) this.mListeners.remove(listener);
                if (wrapper != null) {
                    try {
                        this.mService.removeSessionsListener(wrapper.mStub);
                        wrapper.release();
                    } catch (RemoteException e) {
                        try {
                            Log.e(TAG, "Error in removeOnActiveSessionsChangedListener.", e);
                        } finally {
                            wrapper.release();
                        }
                    }
                }
            }
            return;
        }
        throw new IllegalArgumentException("listener may not be null");
    }

    public void addOnSession2TokensChangedListener(OnSession2TokensChangedListener listener) {
        addOnSession2TokensChangedListener(UserHandle.myUserId(), listener, new Handler());
    }

    public void addOnSession2TokensChangedListener(OnSession2TokensChangedListener listener, Handler handler) {
        addOnSession2TokensChangedListener(UserHandle.myUserId(), listener, handler);
    }

    public void addOnSession2TokensChangedListener(int userId, OnSession2TokensChangedListener listener, Handler handler) {
        if (listener != null) {
            synchronized (this.mLock) {
                if (this.mSession2TokensListeners.get(listener) != null) {
                    Log.w(TAG, "Attempted to add session listener twice, ignoring.");
                    return;
                }
                Session2TokensChangedWrapper wrapper = new Session2TokensChangedWrapper(listener, handler);
                try {
                    this.mService.addSession2TokensListener(wrapper.getStub(), userId);
                    this.mSession2TokensListeners.put(listener, wrapper);
                } catch (RemoteException e) {
                    Log.e(TAG, "Error in addSessionTokensListener.", e);
                    e.rethrowFromSystemServer();
                }
            }
        } else {
            throw new IllegalArgumentException("listener shouldn't be null");
        }
    }

    public void removeOnSession2TokensChangedListener(OnSession2TokensChangedListener listener) {
        if (listener != null) {
            Session2TokensChangedWrapper wrapper;
            synchronized (this.mLock) {
                wrapper = (Session2TokensChangedWrapper) this.mSession2TokensListeners.remove(listener);
            }
            if (wrapper != null) {
                try {
                    this.mService.removeSession2TokensListener(wrapper.getStub());
                    return;
                } catch (RemoteException e) {
                    Log.e(TAG, "Error in removeSessionTokensListener.", e);
                    e.rethrowFromSystemServer();
                    return;
                }
            }
            return;
        }
        throw new IllegalArgumentException("listener may not be null");
    }

    public void registerRemoteVolumeController(IRemoteVolumeController rvc) {
        try {
            this.mService.registerRemoteVolumeController(rvc);
        } catch (RemoteException e) {
            Log.e(TAG, "Error in registerRemoteVolumeController.", e);
        }
    }

    public void unregisterRemoteVolumeController(IRemoteVolumeController rvc) {
        try {
            this.mService.unregisterRemoteVolumeController(rvc);
        } catch (RemoteException e) {
            Log.e(TAG, "Error in unregisterRemoteVolumeController.", e);
        }
    }

    public void dispatchMediaKeyEvent(KeyEvent keyEvent) {
        dispatchMediaKeyEvent(keyEvent, false);
    }

    public void dispatchMediaKeyEvent(KeyEvent keyEvent, boolean needWakeLock) {
        dispatchMediaKeyEventInternal(false, keyEvent, needWakeLock);
    }

    public void dispatchMediaKeyEventAsSystemService(KeyEvent keyEvent) {
        dispatchMediaKeyEventInternal(true, keyEvent, false);
    }

    private void dispatchMediaKeyEventInternal(boolean asSystemService, KeyEvent keyEvent, boolean needWakeLock) {
        try {
            this.mService.dispatchMediaKeyEvent(this.mContext.getPackageName(), asSystemService, keyEvent, needWakeLock);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to send key event.", e);
        }
    }

    public boolean dispatchMediaKeyEventAsSystemService(Token sessionToken, KeyEvent keyEvent) {
        if (sessionToken == null) {
            throw new IllegalArgumentException("sessionToken shouldn't be null");
        } else if (keyEvent == null) {
            throw new IllegalArgumentException("keyEvent shouldn't be null");
        } else if (!KeyEvent.isMediaSessionKey(keyEvent.getKeyCode())) {
            return false;
        } else {
            try {
                return this.mService.dispatchMediaKeyEventToSessionAsSystemService(this.mContext.getPackageName(), sessionToken, keyEvent);
            } catch (RemoteException e) {
                Log.e(TAG, "Failed to send key event.", e);
                return false;
            }
        }
    }

    public void dispatchVolumeKeyEvent(KeyEvent keyEvent, int stream, boolean musicOnly) {
        dispatchVolumeKeyEventInternal(false, keyEvent, stream, musicOnly);
    }

    public void dispatchVolumeKeyEventAsSystemService(KeyEvent keyEvent, int streamType) {
        dispatchVolumeKeyEventInternal(true, keyEvent, streamType, false);
    }

    private void dispatchVolumeKeyEventInternal(boolean asSystemService, KeyEvent keyEvent, int stream, boolean musicOnly) {
        try {
            this.mService.dispatchVolumeKeyEvent(this.mContext.getPackageName(), this.mContext.getOpPackageName(), asSystemService, keyEvent, stream, musicOnly);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to send volume key event.", e);
        }
    }

    public void dispatchVolumeKeyEventAsSystemService(Token sessionToken, KeyEvent keyEvent) {
        if (sessionToken == null) {
            throw new IllegalArgumentException("sessionToken shouldn't be null");
        } else if (keyEvent != null) {
            try {
                this.mService.dispatchVolumeKeyEventToSessionAsSystemService(this.mContext.getPackageName(), this.mContext.getOpPackageName(), sessionToken, keyEvent);
            } catch (RemoteException e) {
                Log.wtf(TAG, "Error calling dispatchVolumeKeyEventAsSystemService", e);
            }
        } else {
            throw new IllegalArgumentException("keyEvent shouldn't be null");
        }
    }

    public void dispatchAdjustVolume(int suggestedStream, int direction, int flags) {
        try {
            this.mService.dispatchAdjustVolume(this.mContext.getPackageName(), this.mContext.getOpPackageName(), suggestedStream, direction, flags);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to send adjust volume.", e);
        }
    }

    public boolean isTrustedForMediaControl(RemoteUserInfo userInfo) {
        if (userInfo == null) {
            throw new IllegalArgumentException("userInfo may not be null");
        } else if (userInfo.getPackageName() == null) {
            return false;
        } else {
            try {
                return this.mService.isTrusted(userInfo.getPackageName(), userInfo.getPid(), userInfo.getUid());
            } catch (RemoteException e) {
                Log.wtf(TAG, "Cannot communicate with the service.", e);
                return false;
            }
        }
    }

    public boolean isGlobalPriorityActive() {
        try {
            return this.mService.isGlobalPriorityActive();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to check if the global priority is active.", e);
            return false;
        }
    }

    @SystemApi
    public void setOnVolumeKeyLongPressListener(OnVolumeKeyLongPressListener listener, Handler handler) {
        synchronized (this.mLock) {
            if (listener == null) {
                try {
                    this.mOnVolumeKeyLongPressListener = null;
                    this.mService.setOnVolumeKeyLongPressListener(null);
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to set volume key long press listener", e);
                } catch (Throwable th) {
                }
            } else {
                if (handler == null) {
                    handler = new Handler();
                }
                this.mOnVolumeKeyLongPressListener = new OnVolumeKeyLongPressListenerImpl(listener, handler);
                this.mService.setOnVolumeKeyLongPressListener(this.mOnVolumeKeyLongPressListener);
            }
        }
    }

    @SystemApi
    public void setOnMediaKeyListener(OnMediaKeyListener listener, Handler handler) {
        synchronized (this.mLock) {
            if (listener == null) {
                try {
                    this.mOnMediaKeyListener = null;
                    this.mService.setOnMediaKeyListener(null);
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to set media key listener", e);
                } catch (Throwable th) {
                }
            } else {
                if (handler == null) {
                    handler = new Handler();
                }
                this.mOnMediaKeyListener = new OnMediaKeyListenerImpl(listener, handler);
                this.mService.setOnMediaKeyListener(this.mOnMediaKeyListener);
            }
        }
    }

    public void setCallback(Callback callback, Handler handler) {
        synchronized (this.mLock) {
            if (callback == null) {
                try {
                    this.mCallback = null;
                    this.mService.setCallback(null);
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to set media key callback", e);
                } catch (Throwable th) {
                }
            } else {
                if (handler == null) {
                    handler = new Handler();
                }
                this.mCallback = new CallbackImpl(callback, handler);
                this.mService.setCallback(this.mCallback);
            }
        }
    }
}
