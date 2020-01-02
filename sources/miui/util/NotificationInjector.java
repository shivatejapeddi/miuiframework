package miui.util;

import android.service.notification.StatusBarNotification;

public class NotificationInjector {
    public static String getChannelId(StatusBarNotification sbn) {
        return sbn.getNotification().getChannelId();
    }
}
