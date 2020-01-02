package com.miui.whetstone;

import android.app.PendingIntent;
import android.os.Build.VERSION;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import com.miui.whetstone.IWhetstone.Stub;
import java.io.File;

public abstract class WhetstoneManager {
    public static final String ANALYTIC_PROPERTY = "persist.sys.whetstone.analytic";
    public static final boolean DEBUG = SystemProperties.getBoolean("persist.sys.whetstone.debug", false);
    public static final String SERVICE_NAME = "miui.whetstone";
    private static IWhetstone ws = null;

    public static class PermissionFile {
        public static final int L_VERSION_START_NUMBER = 21;
        public static final String WHETSTONE_PERMISSION_ENTRY_NAME = "permissions.xml";
        public static final String WHETSTONE_PERMISSION_FILE_NAME = "WhetstonePermission.apk";
        public static final String WHETSTONE_VERSION_ENTRY_NAME = "version";

        public static final File getSystemWhetstonePermissionFile() {
            int i = VERSION.SDK_INT;
            String str = WHETSTONE_PERMISSION_FILE_NAME;
            if (i >= 21) {
                return new File("/system/etc/WhetstonePermission", str);
            }
            return new File("/system/etc", str);
        }

        public static final File getDataWhetstonePermissionFile() {
            return new File("/data/system/whetstone", WHETSTONE_PERMISSION_FILE_NAME);
        }
    }

    static class WhetstoneManagerDeath implements DeathRecipient {
        private IWhetstone mToken;

        WhetstoneManagerDeath(IWhetstone token) {
            this.mToken = token;
        }

        public void binderDied() {
            WhetstoneManager.ws = null;
            IWhetstone iWhetstone = this.mToken;
            if (iWhetstone != null) {
                iWhetstone.asBinder().unlinkToDeath(this, 0);
            }
        }
    }

    private static IWhetstone getService() {
        if (ws == null) {
            ws = Stub.asInterface(ServiceManager.getService("miui.whetstone"));
            try {
                if (ws != null) {
                    ws.asBinder().linkToDeath(new WhetstoneManagerDeath(ws), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ws = null;
            }
        }
        return ws;
    }

    private static void checkService() {
        getService();
    }

    public static void recordRTCWakeupInfo(int uid, PendingIntent operation, boolean status) {
        checkService();
        IWhetstone iWhetstone = ws;
        if (iWhetstone != null) {
            try {
                iWhetstone.recordRTCWakeupInfo(uid, operation, status);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void log(int tag, byte[] data) {
    }

    public static boolean checkIfSupportWhestone() {
        return false;
    }

    public static int deepClean(WhetstoneConfig config) {
        return 0;
    }
}
