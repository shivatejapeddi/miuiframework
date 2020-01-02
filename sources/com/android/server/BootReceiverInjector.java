package com.android.server;

import android.content.Context;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import miui.util.SystemAnalytics;
import miui.util.SystemAnalytics.Action;

public class BootReceiverInjector {
    public static void recordBootTime(Context context) {
        Action action = new Action();
        action.addParam("action", "boot");
        action.addParam("time", System.currentTimeMillis());
        SystemAnalytics.trackSystem(context, SystemAnalytics.CONFIGKEY_BOOT_SHUT, action);
    }

    public static void onBootCompleted() {
        MQSEventManagerDelegate.getInstance().onBootCompleted();
    }
}
