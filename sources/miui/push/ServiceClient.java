package miui.push;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.Map;
import java.util.Map.Entry;

public class ServiceClient {
    private static ServiceClient sInstance;
    private Context mContext;

    public static ServiceClient getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ServiceClient(context);
        }
        return sInstance;
    }

    private ServiceClient(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public int openChannel(String userId, String chid, String token, String authMethod, String security, boolean kick, Map<String, String> clientExtras, Map<String, String> cloudExtras) {
        if (!serviceInstalled()) {
            return 1;
        }
        StringBuilder builder;
        int i;
        Intent intent = createServiceIntent();
        intent.setAction(PushConstants.ACTION_OPEN_CHANNEL);
        String str = userId;
        intent.putExtra(PushConstants.EXTRA_USER_ID, userId);
        intent.putExtra(PushConstants.EXTRA_CHANNEL_ID, chid);
        intent.putExtra(PushConstants.EXTRA_TOKEN, token);
        intent.putExtra(PushConstants.EXTRA_SECURITY, security);
        intent.putExtra(PushConstants.EXTRA_AUTH_METHOD, authMethod);
        intent.putExtra(PushConstants.EXTRA_KICK, kick);
        String str2 = ",";
        String str3 = ":";
        if (clientExtras != null) {
            builder = new StringBuilder();
            i = 1;
            for (Entry<String, String> entry : clientExtras.entrySet()) {
                builder.append((String) entry.getKey());
                builder.append(str3);
                builder.append((String) entry.getValue());
                if (i < clientExtras.size()) {
                    builder.append(str2);
                }
                i++;
            }
            if (!TextUtils.isEmpty(builder)) {
                intent.putExtra(PushConstants.EXTRA_CLIENT_ATTR, builder.toString());
            }
        }
        if (cloudExtras != null) {
            builder = new StringBuilder();
            i = 1;
            for (Entry<String, String> entry2 : cloudExtras.entrySet()) {
                builder.append((String) entry2.getKey());
                builder.append(str3);
                builder.append((String) entry2.getValue());
                if (i < cloudExtras.size()) {
                    builder.append(str2);
                }
                i++;
            }
            if (!TextUtils.isEmpty(builder)) {
                intent.putExtra(PushConstants.EXTRA_CLOUD_ATTR, builder.toString());
            }
        }
        this.mContext.startService(intent);
        return 0;
    }

    public boolean sendMessage(Message message) {
        if (!serviceInstalled() || !hasNetwork()) {
            return false;
        }
        Intent intent = createServiceIntent();
        Bundle messageBundle = message.toBundle();
        if (messageBundle == null) {
            return false;
        }
        intent.setAction(PushConstants.ACTION_SEND_MESSAGE);
        intent.putExtra(PushConstants.EXTRA_PACKET, messageBundle);
        this.mContext.startService(intent);
        return true;
    }

    public boolean batchSendMessage(Message[] messages) {
        if (!hasNetwork()) {
            return false;
        }
        Intent intent = createServiceIntent();
        Parcelable[] messageBundles = new Bundle[messages.length];
        for (int i = 0; i < messages.length; i++) {
            messageBundles[i] = messages[i].toBundle();
        }
        if (messageBundles.length <= 0) {
            return false;
        }
        intent.setAction(PushConstants.ACTION_BATCH_SEND_MESSAGE);
        intent.putExtra(PushConstants.EXTRA_PACKETS, messageBundles);
        this.mContext.startService(intent);
        return true;
    }

    public boolean sendIQ(IQ iq) {
        if (!serviceInstalled() || !hasNetwork()) {
            return false;
        }
        Intent intent = createServiceIntent();
        Bundle iqBundle = iq.toBundle();
        if (iqBundle == null) {
            return false;
        }
        intent.setAction(PushConstants.ACTION_SEND_IQ);
        intent.putExtra(PushConstants.EXTRA_PACKET, iqBundle);
        this.mContext.startService(intent);
        return true;
    }

    public boolean sendPresence(Presence presence) {
        if (!serviceInstalled() || !hasNetwork()) {
            return false;
        }
        Intent intent = createServiceIntent();
        Bundle presBundle = presence.toBundle();
        if (presBundle == null) {
            return false;
        }
        intent.setAction(PushConstants.ACTION_SEND_PRESENCE);
        intent.putExtra(PushConstants.EXTRA_PACKET, presBundle);
        this.mContext.startService(intent);
        return true;
    }

    public boolean closeChannel() {
        if (!serviceInstalled()) {
            return false;
        }
        Intent intent = createServiceIntent();
        intent.setAction(PushConstants.ACTION_CLOSE_CHANNEL);
        this.mContext.startService(intent);
        return true;
    }

    public boolean closeChannel(String chid) {
        if (!serviceInstalled()) {
            return false;
        }
        Intent intent = createServiceIntent();
        intent.setAction(PushConstants.ACTION_CLOSE_CHANNEL);
        intent.putExtra(PushConstants.EXTRA_CHANNEL_ID, chid);
        this.mContext.startService(intent);
        return true;
    }

    public boolean closeChannel(String chid, String userId) {
        if (!serviceInstalled()) {
            return false;
        }
        Intent intent = createServiceIntent();
        intent.setAction(PushConstants.ACTION_CLOSE_CHANNEL);
        intent.putExtra(PushConstants.EXTRA_CHANNEL_ID, chid);
        intent.putExtra(PushConstants.EXTRA_USER_ID, userId);
        this.mContext.startService(intent);
        return true;
    }

    public boolean forceReconnection() {
        if (!serviceInstalled() || !hasNetwork()) {
            return false;
        }
        Intent intent = createServiceIntent();
        intent.setAction(PushConstants.ACTION_FORCE_RECONNECT);
        this.mContext.startService(intent);
        return true;
    }

    public void resetConnection() {
        if (serviceInstalled()) {
            Intent intent = createServiceIntent();
            intent.setAction(PushConstants.ACTION_RESET_CONNECTION);
            this.mContext.startService(intent);
        }
    }

    private boolean hasNetwork() {
        int type = -1;
        ConnectivityManager cm = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null) {
                type = info.getType();
            }
        }
        return type >= 0;
    }

    private boolean serviceInstalled() {
        try {
            if (this.mContext.getPackageManager().getPackageInfo(PushConstants.PUSH_SERVICE_PACKAGE_NAME, 4) == null) {
                return false;
            }
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    private Intent createServiceIntent() {
        Intent intent = new Intent();
        String str = PushConstants.PUSH_SERVICE_PACKAGE_NAME;
        intent.setPackage(str);
        intent.setClassName(str, PushConstants.PUSH_SERVICE_CLASS_NAME);
        intent.putExtra(PushConstants.EXTRA_PACKAGE_NAME, this.mContext.getPackageName());
        return intent;
    }
}
