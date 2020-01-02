package com.xiaomi.joyose;

import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import com.xiaomi.joyose.IJoyoseInterface.Stub;

public class MiPlatformAppBoosterManager {
    public static final String SERVICE_NAME = "xiaomi.joyose";
    private static IJoyoseInterface js = null;

    static class JoyoseManagerDeath implements DeathRecipient {
        private IJoyoseInterface mToken;

        JoyoseManagerDeath(IJoyoseInterface token) {
            this.mToken = token;
        }

        public void binderDied() {
            MiPlatformAppBoosterManager.js = null;
            IJoyoseInterface iJoyoseInterface = this.mToken;
            if (iJoyoseInterface != null) {
                iJoyoseInterface.asBinder().unlinkToDeath(this, 0);
            }
        }
    }

    private static IJoyoseInterface getService() {
        if (js == null) {
            js = Stub.asInterface(ServiceManager.getService("xiaomi.joyose"));
            try {
                if (js != null) {
                    js.asBinder().linkToDeath(new JoyoseManagerDeath(js), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                js = null;
            }
        }
        return js;
    }

    private static void checkService() {
        getService();
    }

    public static void MiPlatformBoosterForOneway(int cmd, String data) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("zhangxiaoliang MiPlatformBoosterForOneway cmd ");
        stringBuilder.append(cmd);
        stringBuilder.append(" data ");
        stringBuilder.append(data);
        Slog.d("zhang", stringBuilder.toString());
        checkService();
        IJoyoseInterface iJoyoseInterface = js;
        if (iJoyoseInterface != null) {
            try {
                iJoyoseInterface.MiPlatformBoosterForOneway(cmd, data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static String MiPlatformBoosterForSync(int cmd, String data) {
        checkService();
        IJoyoseInterface iJoyoseInterface = js;
        if (iJoyoseInterface == null) {
            return null;
        }
        try {
            return iJoyoseInterface.MiPlatformBoosterForSync(cmd, data);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
}
