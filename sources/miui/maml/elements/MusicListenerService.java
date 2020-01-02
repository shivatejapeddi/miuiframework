package miui.maml.elements;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RemoteController;
import android.media.RemoteController.MetadataEditor;
import android.media.RemoteController.OnClientUpdateListener;
import android.os.Binder;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Deprecated
public class MusicListenerService extends NotificationListenerService implements OnClientUpdateListener {
    public static final String ACTION = "android.service.notification.MusicListenerService";
    private static final int BITMAP_HEIGHT = 1024;
    private static final int BITMAP_WIDTH = 1024;
    private static final String LOG_TAG = "MusicListenerService";
    private IBinder mBinder = new RCBinder();
    private List<WeakReference<OnClientUpdateListener>> mClientUpdateListeners = new CopyOnWriteArrayList();
    private Context mContext;
    private RemoteController mRemoteController;
    private boolean mRemoteControllerEnabled;

    public class RCBinder extends Binder {
        public MusicListenerService getService() {
            return MusicListenerService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        boolean equals = intent.getAction().equals(ACTION);
        String str = LOG_TAG;
        if (equals) {
            Log.d(str, "onBind: success");
            return this.mBinder;
        }
        Log.d(str, "onBind: fail");
        return null;
    }

    public void onCreate() {
        this.mContext = getApplicationContext();
        this.mRemoteController = new RemoteController(this.mContext, this);
    }

    public void onDestroy() {
        disableRemoteController();
    }

    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onNotificationPosted: pkg = ");
        stringBuilder.append(sbn.getPackageName());
        Log.d(LOG_TAG, stringBuilder.toString());
    }

    private void enableRemoteController() {
        String str = "fail to register RemoteController!";
        String str2 = LOG_TAG;
        if (!this.mRemoteControllerEnabled) {
            AudioManager am = (AudioManager) this.mContext.getSystemService("audio");
            this.mRemoteController = new RemoteController(this.mContext, this);
            try {
                this.mRemoteControllerEnabled = am.registerRemoteController(this.mRemoteController);
            } catch (Exception e) {
                Log.w(str2, str, e);
            }
            if (this.mRemoteControllerEnabled) {
                this.mRemoteController.setArtworkConfiguration(1024, 1024);
                this.mRemoteController.setSynchronizationMode(1);
                return;
            }
            Log.w(str2, str);
        }
    }

    private void disableRemoteController() {
        if (this.mRemoteControllerEnabled) {
            ((AudioManager) this.mContext.getSystemService("audio")).unregisterRemoteController(this.mRemoteController);
            this.mRemoteControllerEnabled = false;
        }
    }

    public void registerClientUpdateListener(OnClientUpdateListener listener) {
        enableRemoteController();
        for (WeakReference<OnClientUpdateListener> updateListenerWeakReference : this.mClientUpdateListeners) {
            OnClientUpdateListener temp = (OnClientUpdateListener) updateListenerWeakReference.get();
            if (temp != null && temp.equals(listener)) {
                return;
            }
        }
        this.mClientUpdateListeners.add(new WeakReference(listener));
    }

    public void unregisterClientUpdateListener(OnClientUpdateListener listener) {
        for (WeakReference<OnClientUpdateListener> updateListenerWeakReference : this.mClientUpdateListeners) {
            OnClientUpdateListener temp = (OnClientUpdateListener) updateListenerWeakReference.get();
            if (temp == null || temp.equals(listener)) {
                this.mClientUpdateListeners.remove(updateListenerWeakReference);
            }
        }
        if (this.mClientUpdateListeners.isEmpty()) {
            disableRemoteController();
        }
    }

    public void onClientChange(boolean arg0) {
        for (WeakReference<OnClientUpdateListener> updateListenerWeakReference : this.mClientUpdateListeners) {
            OnClientUpdateListener listener = (OnClientUpdateListener) updateListenerWeakReference.get();
            if (listener != null) {
                listener.onClientChange(arg0);
            } else {
                this.mClientUpdateListeners.remove(updateListenerWeakReference);
            }
        }
        if (this.mClientUpdateListeners.isEmpty()) {
            disableRemoteController();
        }
    }

    public void onClientMetadataUpdate(MetadataEditor arg0) {
        for (WeakReference<OnClientUpdateListener> updateListenerWeakReference : this.mClientUpdateListeners) {
            OnClientUpdateListener listener = (OnClientUpdateListener) updateListenerWeakReference.get();
            if (listener != null) {
                listener.onClientMetadataUpdate(arg0);
            } else {
                this.mClientUpdateListeners.remove(updateListenerWeakReference);
            }
        }
        if (this.mClientUpdateListeners.isEmpty()) {
            disableRemoteController();
        }
    }

    public void onClientPlaybackStateUpdate(int arg0) {
        for (WeakReference<OnClientUpdateListener> updateListenerWeakReference : this.mClientUpdateListeners) {
            OnClientUpdateListener listener = (OnClientUpdateListener) updateListenerWeakReference.get();
            if (listener != null) {
                listener.onClientPlaybackStateUpdate(arg0);
            } else {
                this.mClientUpdateListeners.remove(updateListenerWeakReference);
            }
        }
        if (this.mClientUpdateListeners.isEmpty()) {
            disableRemoteController();
        }
    }

    public void onClientPlaybackStateUpdate(int arg0, long arg1, long arg2, float arg3) {
        for (WeakReference<OnClientUpdateListener> updateListenerWeakReference : this.mClientUpdateListeners) {
            OnClientUpdateListener listener = (OnClientUpdateListener) updateListenerWeakReference.get();
            if (listener != null) {
                listener.onClientPlaybackStateUpdate(arg0, arg1, arg2, arg3);
            } else {
                this.mClientUpdateListeners.remove(updateListenerWeakReference);
            }
        }
        if (this.mClientUpdateListeners.isEmpty()) {
            disableRemoteController();
        }
    }

    public void onClientTransportControlUpdate(int arg0) {
        for (WeakReference<OnClientUpdateListener> updateListenerWeakReference : this.mClientUpdateListeners) {
            OnClientUpdateListener listener = (OnClientUpdateListener) updateListenerWeakReference.get();
            if (listener != null) {
                listener.onClientTransportControlUpdate(arg0);
            } else {
                this.mClientUpdateListeners.remove(updateListenerWeakReference);
            }
        }
        if (this.mClientUpdateListeners.isEmpty()) {
            disableRemoteController();
        }
    }

    public void onClientFolderInfoBrowsedPlayer(String stringUri) {
    }

    public void onClientUpdateNowPlayingEntries(long[] playList) {
    }

    public void onClientNowPlayingContentChange() {
    }

    public void onClientPlayItemResponse(boolean success) {
    }

    public RemoteController getRemoteController() {
        return this.mRemoteController;
    }
}
