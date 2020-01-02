package android.location;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public abstract class SettingInjectorService extends Service {
    public static final String ACTION_INJECTED_SETTING_CHANGED = "android.location.InjectedSettingChanged";
    public static final String ACTION_SERVICE_INTENT = "android.location.SettingInjectorService";
    public static final String ATTRIBUTES_NAME = "injected-location-setting";
    public static final String ENABLED_KEY = "enabled";
    public static final String MESSENGER_KEY = "messenger";
    public static final String META_DATA_NAME = "android.location.SettingInjectorService";
    public static final String SUMMARY_KEY = "summary";
    private static final String TAG = "SettingInjectorService";
    private final String mName;

    public abstract boolean onGetEnabled();

    public abstract String onGetSummary();

    public SettingInjectorService(String name) {
        this.mName = name;
    }

    public final IBinder onBind(Intent intent) {
        return null;
    }

    public final void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public final int onStartCommand(Intent intent, int flags, int startId) {
        onHandleIntent(intent);
        stopSelf(startId);
        return 2;
    }

    private void onHandleIntent(Intent intent) {
        String summary = null;
        boolean enabled = false;
        try {
            summary = onGetSummary();
            enabled = onGetEnabled();
        } finally {
            sendStatus(intent, summary, enabled);
        }
    }

    private void sendStatus(Intent intent, String summary, boolean enabled) {
        Messenger messenger = (Messenger) intent.getParcelableExtra("messenger");
        if (messenger != null) {
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("summary", summary);
            bundle.putBoolean("enabled", enabled);
            message.setData(bundle);
            String str = TAG;
            if (Log.isLoggable(str, 3)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(this.mName);
                stringBuilder.append(": received ");
                stringBuilder.append(intent);
                stringBuilder.append(", summary=");
                stringBuilder.append(summary);
                stringBuilder.append(", enabled=");
                stringBuilder.append(enabled);
                stringBuilder.append(", sending message: ");
                stringBuilder.append(message);
                Log.d(str, stringBuilder.toString());
            }
            try {
                messenger.send(message);
            } catch (RemoteException e) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(this.mName);
                stringBuilder2.append(": sending dynamic status failed");
                Log.e(str, stringBuilder2.toString(), e);
            }
        }
    }

    public static final void refreshSettings(Context context) {
        context.sendBroadcast(new Intent(ACTION_INJECTED_SETTING_CHANGED));
    }
}
