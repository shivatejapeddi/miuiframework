package com.android.internal.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ParceledListSlice;
import android.media.AudioAttributes.Builder;
import android.os.RemoteException;
import android.provider.Settings.System;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemNotificationChannels {
    public static String ACCOUNT = "ACCOUNT";
    public static String ALERTS = "ALERTS";
    public static String CAR_MODE = "CAR_MODE";
    public static String DEVELOPER = "DEVELOPER";
    public static String DEVICE_ADMIN = "DEVICE_ADMIN_ALERTS";
    @Deprecated
    public static String DEVICE_ADMIN_DEPRECATED = "DEVICE_ADMIN";
    public static String DO_NOT_DISTURB = "DO_NOT_DISTURB";
    public static String FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    public static String HEAVY_WEIGHT_APP = "HEAVY_WEIGHT_APP";
    public static String NETWORK_ALERTS = "NETWORK_ALERTS";
    public static String NETWORK_AVAILABLE = "NETWORK_AVAILABLE";
    public static String NETWORK_STATUS = "NETWORK_STATUS";
    public static String PHYSICAL_KEYBOARD = "PHYSICAL_KEYBOARD";
    public static String RETAIL_MODE = "RETAIL_MODE";
    public static String SECURITY = "SECURITY";
    public static String SYSTEM_CHANGES = "SYSTEM_CHANGES";
    public static String UPDATES = "UPDATES";
    public static String USB = "USB";
    public static String VIRTUAL_KEYBOARD = "VIRTUAL_KEYBOARD";
    public static String VIRTUAL_KEYBOARD_MIUI = "VIRTUAL_KEYBOARD_MIUI";
    public static String VPN = "VPN";

    public static void createAll(Context context) {
        Context context2 = context;
        NotificationManager nm = (NotificationManager) context2.getSystemService(NotificationManager.class);
        List<NotificationChannel> channelsList = new ArrayList();
        NotificationChannel keyboard = new NotificationChannel(VIRTUAL_KEYBOARD_MIUI, context2.getString(R.string.notification_channel_virtual_keyboard), 5);
        keyboard.setBlockableSystem(true);
        keyboard.setSound(null, null);
        channelsList.add(keyboard);
        NotificationChannel physicalKeyboardChannel = new NotificationChannel(PHYSICAL_KEYBOARD, context2.getString(R.string.notification_channel_physical_keyboard), 3);
        physicalKeyboardChannel.setSound(System.DEFAULT_NOTIFICATION_URI, Notification.AUDIO_ATTRIBUTES_DEFAULT);
        physicalKeyboardChannel.setBlockableSystem(true);
        channelsList.add(physicalKeyboardChannel);
        channelsList.add(new NotificationChannel(SECURITY, context2.getString(R.string.notification_channel_security), 2));
        NotificationChannel car = new NotificationChannel(CAR_MODE, context2.getString(R.string.notification_channel_car_mode), 2);
        car.setBlockableSystem(true);
        channelsList.add(car);
        channelsList.add(newAccountChannel(context));
        NotificationChannel developer = new NotificationChannel(DEVELOPER, context2.getString(R.string.notification_channel_developer), 2);
        developer.setBlockableSystem(true);
        channelsList.add(developer);
        channelsList.add(new NotificationChannel(UPDATES, context2.getString(R.string.notification_channel_updates), 2));
        channelsList.add(new NotificationChannel(NETWORK_STATUS, context2.getString(R.string.notification_channel_network_status), 2));
        NotificationChannel networkAlertsChannel = new NotificationChannel(NETWORK_ALERTS, context2.getString(R.string.notification_channel_network_alerts), 4);
        networkAlertsChannel.setBlockableSystem(true);
        channelsList.add(networkAlertsChannel);
        NotificationChannel networkAvailable = new NotificationChannel(NETWORK_AVAILABLE, context2.getString(R.string.notification_channel_network_available), 2);
        networkAvailable.setBlockableSystem(true);
        channelsList.add(networkAvailable);
        NotificationChannel vpn = new NotificationChannel(VPN, context2.getString(R.string.notification_channel_vpn), 2);
        channelsList.add(vpn);
        keyboard = new NotificationChannel(DEVICE_ADMIN, context2.getString(R.string.notification_channel_device_admin), 4);
        channelsList.add(keyboard);
        keyboard = new NotificationChannel(ALERTS, context2.getString(R.string.notification_channel_alerts), 3);
        channelsList.add(keyboard);
        keyboard = new NotificationChannel(RETAIL_MODE, context2.getString(R.string.notification_channel_retail_mode), 2);
        channelsList.add(keyboard);
        keyboard = new NotificationChannel(USB, context2.getString(R.string.notification_channel_usb), 3);
        keyboard.setSound(null, null);
        channelsList.add(keyboard);
        keyboard = new NotificationChannel(FOREGROUND_SERVICE, context2.getString(R.string.notification_channel_foreground_service), 2);
        keyboard.setBlockableSystem(true);
        channelsList.add(keyboard);
        keyboard = new NotificationChannel(HEAVY_WEIGHT_APP, context2.getString(R.string.notification_channel_heavy_weight_app), 3);
        keyboard.setShowBadge(false);
        keyboard.setSound(null, new Builder().setContentType(4).setUsage(10).build());
        channelsList.add(keyboard);
        channelsList.add(new NotificationChannel(SYSTEM_CHANGES, context2.getString(R.string.notification_channel_system_changes), 2));
        channelsList.add(new NotificationChannel(DO_NOT_DISTURB, context2.getString(R.string.notification_channel_do_not_disturb), 2));
        nm.createNotificationChannels(channelsList);
        nm.deleteNotificationChannel(VIRTUAL_KEYBOARD);
    }

    public static void removeDeprecated(Context context) {
        ((NotificationManager) context.getSystemService(NotificationManager.class)).deleteNotificationChannel(DEVICE_ADMIN_DEPRECATED);
    }

    public static void createAccountChannelForPackage(String pkg, int uid, Context context) {
        try {
            NotificationManager.getService().createNotificationChannelsForPackage(pkg, uid, new ParceledListSlice(Arrays.asList(new NotificationChannel[]{newAccountChannel(context)})));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private static NotificationChannel newAccountChannel(Context context) {
        return new NotificationChannel(ACCOUNT, context.getString(R.string.notification_channel_account), 2);
    }

    private SystemNotificationChannels() {
    }
}
