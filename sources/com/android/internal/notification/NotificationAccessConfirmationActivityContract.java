package com.android.internal.notification;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Parcelable;
import com.miui.internal.search.SettingsTree;

public final class NotificationAccessConfirmationActivityContract {
    private static final ComponentName COMPONENT_NAME = new ComponentName(SettingsTree.SETTINGS_PACKAGE, "com.android.settings.notification.NotificationAccessConfirmationActivity");
    public static final String EXTRA_COMPONENT_NAME = "component_name";
    public static final String EXTRA_PACKAGE_TITLE = "package_title";
    public static final String EXTRA_USER_ID = "user_id";

    public static Intent launcherIntent(int userId, ComponentName component, String packageTitle) {
        return new Intent().setComponent(COMPONENT_NAME).putExtra("user_id", userId).putExtra(EXTRA_COMPONENT_NAME, (Parcelable) component).putExtra(EXTRA_PACKAGE_TITLE, packageTitle);
    }
}
