package com.miui.whetstone;

import android.content.ComponentName;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.miui.whetstone.IWhetstone.Stub;
import java.util.List;

public abstract class WhetstoneCloudControlManager {
    public static int REGISTER_ALREADY = 0;
    public static int REGISTER_FAIL = -1;
    public static int REGISTER_SUCCESS = 1;
    public static final String SERVICE_NAME = "miui.whetstone";
    public static int UNREGISTER_FAIL = -1;
    public static int UNREGISTER_SUCCESS = 1;
    private static IWhetstone ws = getService();

    static class WhetstoneCloudManagerDeath implements DeathRecipient {
        private IWhetstone mToken;

        WhetstoneCloudManagerDeath(IWhetstone token) {
            this.mToken = token;
        }

        public void binderDied() {
            WhetstoneCloudControlManager.ws = null;
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
                    ws.asBinder().linkToDeath(new WhetstoneCloudManagerDeath(ws), 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ws;
    }

    private static void checkService() {
        getService();
    }

    public static int registerMiuiWhetstoneCloudSync(ComponentName component, CloudControlInfo info) {
        checkService();
        int ret = REGISTER_FAIL;
        IWhetstone iWhetstone = ws;
        if (iWhetstone == null) {
            return ret;
        }
        try {
            return iWhetstone.registerMiuiWhetstoneCloudSync(component, info);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ret;
        }
    }

    public static int registerMiuiWhetstoneCloudSyncList(ComponentName component, List<CloudControlInfo> infos) {
        checkService();
        int ret = REGISTER_FAIL;
        IWhetstone iWhetstone = ws;
        if (iWhetstone == null) {
            return ret;
        }
        try {
            return iWhetstone.registerMiuiWhetstoneCloudSyncList(component, infos);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ret;
        }
    }

    public static int unregisterMiuiWhetstoneCloudSync(ComponentName component) {
        checkService();
        int ret = UNREGISTER_FAIL;
        IWhetstone iWhetstone = ws;
        if (iWhetstone == null) {
            return ret;
        }
        try {
            return iWhetstone.unregisterMiuiWhetstoneCloudSync(component);
        } catch (RemoteException e) {
            e.printStackTrace();
            return ret;
        }
    }
}
