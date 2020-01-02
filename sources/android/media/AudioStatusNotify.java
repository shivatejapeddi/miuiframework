package android.media;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.util.Log;
import com.android.internal.R;

public class AudioStatusNotify {
    public static final int NOTIFY_ID = 10001;
    public static final String TAG = AudioStatusNotify.class.getSimpleName();

    public static class AppInfo {
        public Bitmap icon;
        public String name;
        public int pid;
        public String pkg;
    }

    private AudioStatusNotify() {
    }

    public static void sendAudioStatusNotification(Context context, int pid, boolean isSpeakerOn) {
        AppInfo appInfo = getApplicationInfo(context, pid, true);
        if (appInfo == null) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("can not get app info for pid ");
            stringBuilder.append(pid);
            Log.e(str, stringBuilder.toString());
            return;
        }
        String summary;
        NotificationManager manager = (NotificationManager) context.getSystemService("notification");
        if (isSpeakerOn) {
            summary = context.getResources().getString(R.string.status_communication_speaker_summary);
        } else {
            summary = context.getResources().getString(R.string.status_communication_summary);
        }
        String id = "channel_1";
        manager.createNotificationChannel(new NotificationChannel(id, context.getResources().getString(R.string.notification_description), 2));
        Notification notification = new Builder(context, id).setCategory(Notification.CATEGORY_REMINDER).setLargeIcon(appInfo.icon).setSmallIcon(appInfo.icon != null ? Icon.createWithBitmap(appInfo.icon) : null).setContentTitle(appInfo.name).setContentText(summary).build();
        notification.flags |= 32;
        manager.notify(10001, notification);
    }

    public static AppInfo getApplicationInfo(Context context, int pid, boolean getIcon) {
        PackageManager pm = context.getPackageManager();
        for (RunningAppProcessInfo info : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
            if (info.pid == pid) {
                try {
                    ApplicationInfo applicationInfo = pm.getApplicationInfo(info.processName, 128);
                    AppInfo appInfo = new AppInfo();
                    appInfo.pid = pid;
                    appInfo.pkg = applicationInfo.packageName;
                    appInfo.name = pm.getApplicationLabel(applicationInfo).toString();
                    if (getIcon) {
                        appInfo.icon = ((BitmapDrawable) pm.getApplicationIcon(applicationInfo)).getBitmap();
                    }
                    return appInfo;
                } catch (NameNotFoundException e) {
                    Log.e(TAG, "getApplicationInfo NameNotFoundException failed", e);
                } catch (Exception e2) {
                    Log.e(TAG, "getApplicationInfo failed", e2);
                }
            }
        }
        return null;
    }
}
