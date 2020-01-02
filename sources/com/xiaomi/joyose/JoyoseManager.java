package com.xiaomi.joyose;

import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.xiaomi.joyose.IJoyoseInterface.Stub;
import java.util.List;

public class JoyoseManager {
    public static final String SERVICE_NAME = "xiaomi.joyose";
    private static IJoyoseInterface js = null;

    static class JoyoseManagerDeath implements DeathRecipient {
        private IJoyoseInterface mToken;

        JoyoseManagerDeath(IJoyoseInterface token) {
            this.mToken = token;
        }

        public void binderDied() {
            JoyoseManager.js = null;
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

    public static void registerGameEngineListener(int mask, IGameEngineCallback callback) {
        checkService();
        IJoyoseInterface iJoyoseInterface = js;
        if (iJoyoseInterface != null) {
            try {
                iJoyoseInterface.registerGameEngineListener(mask, callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void unRegisterGameEngineListener(IGameEngineCallback callback) {
        checkService();
        IJoyoseInterface iJoyoseInterface = js;
        if (iJoyoseInterface != null) {
            try {
                iJoyoseInterface.unRegisterGameEngineListener(callback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void handleGameBoosterForOneway(int cmd, String data) {
        checkService();
        IJoyoseInterface iJoyoseInterface = js;
        if (iJoyoseInterface != null) {
            try {
                iJoyoseInterface.handleGameBoosterForOneway(cmd, data);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static String handleGameBoosterForSync(int cmd, String data) {
        checkService();
        IJoyoseInterface iJoyoseInterface = js;
        if (iJoyoseInterface == null) {
            return null;
        }
        try {
            return iJoyoseInterface.handleGameBoosterForSync(cmd, data);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void predictAppBucketLevel(String packageName) {
        checkService();
        IJoyoseInterface iJoyoseInterface = js;
        if (iJoyoseInterface != null) {
            try {
                iJoyoseInterface.predictAppBucketLevel(packageName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void predictAppsBucketLevel(List<String> packageList, int id) {
        checkService();
        IJoyoseInterface iJoyoseInterface = js;
        if (iJoyoseInterface != null) {
            try {
                iJoyoseInterface.predictAppsBucketLevel(packageList, id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<String> getGameMotorAppList() {
        checkService();
        IJoyoseInterface iJoyoseInterface = js;
        if (iJoyoseInterface != null) {
            try {
                return iJoyoseInterface.getGameMotorAppList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
