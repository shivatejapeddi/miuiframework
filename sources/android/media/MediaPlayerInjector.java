package android.media;

import android.content.Context;
import android.provider.MiuiSettings.SilenceMode;
import android.provider.MiuiSettings.XSpace;
import android.util.EventLog;
import java.util.HashMap;

class MediaPlayerInjector {
    public static final int AUDIO_STATUS_CHANGE = 30200;
    private static HashMap<String, Integer> sAppList = new HashMap();

    MediaPlayerInjector() {
    }

    static void updateActiveProcessStatus(boolean stayAwake, boolean awake, int pid, int uid) {
        if (stayAwake != awake && uid > 10000) {
            int status = awake ? 1 : 2;
            StringBuilder stringBuilder = new StringBuilder(16);
            stringBuilder.append(pid);
            String str = "&";
            stringBuilder.append(str);
            stringBuilder.append(uid);
            stringBuilder.append(str);
            stringBuilder.append(status);
            EventLog.writeEvent(30200, stringBuilder.toString());
        }
    }

    static {
        sAppList.put("com.tencent.mm", Integer.valueOf(1));
        sAppList.put(XSpace.QQ_PACKAGE_NAME, Integer.valueOf(2));
    }

    public static boolean needMuteNotification(Context context) {
        if (!SilenceMode.isDNDEnabled(context) || sAppList.get(context.getPackageName()) == null) {
            return false;
        }
        return true;
    }
}
