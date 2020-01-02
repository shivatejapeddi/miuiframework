package com.android.internal.util;

import android.app.Notification;
import android.app.Notification.MessagingStyle;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import java.util.Objects;

public class NotificationMessagingUtil {
    private static final String DEFAULT_SMS_APP_SETTING = "sms_default_application";
    private final Context mContext;
    private ArrayMap<Integer, String> mDefaultSmsApp = new ArrayMap();
    private final ContentObserver mSmsContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) {
        public void onChange(boolean selfChange, Uri uri, int userId) {
            if (Secure.getUriFor("sms_default_application").equals(uri)) {
                NotificationMessagingUtil.this.cacheDefaultSmsApp(userId);
            }
        }
    };

    public NotificationMessagingUtil(Context context) {
        this.mContext = context;
        this.mContext.getContentResolver().registerContentObserver(Secure.getUriFor("sms_default_application"), false, this.mSmsContentObserver);
    }

    public boolean isImportantMessaging(StatusBarNotification sbn, int importance) {
        boolean z = false;
        if (importance < 2) {
            return false;
        }
        if (hasMessagingStyle(sbn) || (isCategoryMessage(sbn) && isDefaultMessagingApp(sbn))) {
            z = true;
        }
        return z;
    }

    public boolean isMessaging(StatusBarNotification sbn) {
        return hasMessagingStyle(sbn) || isDefaultMessagingApp(sbn) || isCategoryMessage(sbn);
    }

    private boolean isDefaultMessagingApp(StatusBarNotification sbn) {
        int userId = sbn.getUserId();
        if (userId == -10000 || userId == -1) {
            return false;
        }
        if (this.mDefaultSmsApp.get(Integer.valueOf(userId)) == null) {
            cacheDefaultSmsApp(userId);
        }
        return Objects.equals(this.mDefaultSmsApp.get(Integer.valueOf(userId)), sbn.getPackageName());
    }

    private void cacheDefaultSmsApp(int userId) {
        this.mDefaultSmsApp.put(Integer.valueOf(userId), Secure.getStringForUser(this.mContext.getContentResolver(), "sms_default_application", userId));
    }

    private boolean hasMessagingStyle(StatusBarNotification sbn) {
        return MessagingStyle.class.equals(sbn.getNotification().getNotificationStyle());
    }

    private boolean isCategoryMessage(StatusBarNotification sbn) {
        return Notification.CATEGORY_MESSAGE.equals(sbn.getNotification().category);
    }
}
