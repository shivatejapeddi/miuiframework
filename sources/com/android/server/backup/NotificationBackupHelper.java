package com.android.server.backup;

import android.app.INotificationManager.Stub;
import android.app.backup.BlobBackupHelper;
import android.os.ServiceManager;
import android.util.Log;
import android.util.Slog;

public class NotificationBackupHelper extends BlobBackupHelper {
    static final int BLOB_VERSION = 1;
    static final boolean DEBUG = Log.isLoggable(TAG, 3);
    static final String KEY_NOTIFICATIONS = "notifications";
    static final String TAG = "NotifBackupHelper";
    private final int mUserId;

    public NotificationBackupHelper(int userId) {
        super(1, "notifications");
        this.mUserId = userId;
    }

    /* Access modifiers changed, original: protected */
    public byte[] getBackupPayload(String key) {
        if (!"notifications".equals(key)) {
            return null;
        }
        try {
            return Stub.asInterface(ServiceManager.getService("notification")).getBackupPayload(this.mUserId);
        } catch (Exception e) {
            Slog.e(TAG, "Couldn't communicate with notification manager");
            return null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void applyRestoredPayload(String key, byte[] payload) {
        boolean z = DEBUG;
        String str = TAG;
        if (z) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Got restore of ");
            stringBuilder.append(key);
            Slog.v(str, stringBuilder.toString());
        }
        if ("notifications".equals(key)) {
            try {
                Stub.asInterface(ServiceManager.getService("notification")).applyRestore(payload, this.mUserId);
            } catch (Exception e) {
                Slog.e(str, "Couldn't communicate with notification manager");
            }
        }
    }
}
