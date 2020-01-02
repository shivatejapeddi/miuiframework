package com.miui.whetstone;

import android.content.ComponentName;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Log;
import com.miui.whetstone.server.IWhetstoneActivityManager;
import com.miui.whetstone.server.IWhetstoneActivityManager.Stub;
import com.miui.whetstone.strategy.WhetstonePackageInfo;
import java.util.List;

public abstract class WhetstoneActivityManager {
    public static final String SERVICE_NAME = "whetstone.activity";
    private static IWhetstoneActivityManager ws = null;

    static class WhetstoneManagerDeath implements DeathRecipient {
        private IWhetstoneActivityManager mToken;

        WhetstoneManagerDeath(IWhetstoneActivityManager token) {
            this.mToken = token;
        }

        public void binderDied() {
            WhetstoneActivityManager.ws = null;
            IWhetstoneActivityManager iWhetstoneActivityManager = this.mToken;
            if (iWhetstoneActivityManager != null) {
                iWhetstoneActivityManager.asBinder().unlinkToDeath(this, 0);
            }
        }
    }

    private static IWhetstoneActivityManager getService() {
        if (ws == null) {
            ws = Stub.asInterface(ServiceManager.getService("whetstone.activity"));
            try {
                if (ws != null) {
                    ws.asBinder().linkToDeath(new WhetstoneManagerDeath(ws), 0);
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

    public boolean setPerformanceComponents(ComponentName[] names) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager == null) {
            return false;
        }
        try {
            return iWhetstoneActivityManager.setPerformanceComponents(names);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void addAppToServiceControlWhitelist(List<String> listPkg) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.addAppToServiceControlWhitelist(listPkg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void removeAppFromServiceControlWhitelist(String pkgName) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.removeAppFromServiceControlWhitelist(pkgName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static Long getAndroidCachedEmptyProcessMemory() {
        checkService();
        long totoalMemory = 0;
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                totoalMemory = iWhetstoneActivityManager.getAndroidCachedEmptyProcessMemory();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return Long.valueOf(totoalMemory);
    }

    public static void updateApplicationsMemoryThreshold(List<String> thresholds) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.updateApplicationsMemoryThreshold(thresholds);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateUserLockedAppList(List<String> lockedApps) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.updateUserLockedAppList(lockedApps);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean checkIfPackageIsLocked(String packageName) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                return iWhetstoneActivityManager.checkIfPackageIsLocked(packageName);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean checkIfPackageIsLocked(String packageName, int userId) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                return iWhetstoneActivityManager.checkIfPackageIsLockedWithUserId(packageName, userId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void updateUserLockedAppList(List<String> lockedApps, int userId) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.updateUserLockedAppListWithUserId(lockedApps, userId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void checkApplicationsMemoryThreshold(String packageName, int pid, long threshold) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.checkApplicationsMemoryThreshold(packageName, pid, threshold);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearDeadAppFromNative() {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.clearDeadAppFromNative();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static void bindWhetstoneService(IBinder binder) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.bindWhetstoneService(binder);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getPackageNamebyPid(int callingPid) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager == null) {
            return null;
        }
        try {
            return iWhetstoneActivityManager.getPackageNamebyPid(callingPid);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean scheduleTrimMemory(int pid, int level) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.scheduleTrimMemory(pid, level);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static boolean scheduleDestoryActivities(int pid) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager == null) {
            return false;
        }
        try {
            return iWhetstoneActivityManager.distoryActivity(pid);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getSystemPid() {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager == null) {
            return 0;
        }
        try {
            return iWhetstoneActivityManager.getSystemPid();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void setGmsBlockerEnable(int uid, boolean enable) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.setGmsBlockerEnable(uid, enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean initGmsChain(String name, int uid, String rule) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager == null) {
            return false;
        }
        try {
            return iWhetstoneActivityManager.initGmsChain(name, uid, rule);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean setGmsChainState(String name, boolean enable) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager == null) {
            return false;
        }
        try {
            return iWhetstoneActivityManager.setGmsChainState(name, enable);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateFrameworkCommonConfig(String json) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.updateFrameworkCommonConfig(json);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean removeTaskById(int taskId, boolean killProcess) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager == null) {
            return false;
        }
        try {
            return iWhetstoneActivityManager.removeTaskById(taskId, killProcess);
        } catch (RemoteException e) {
            Log.e("whetstone.activity", Log.getStackTraceString(e));
            return false;
        }
    }

    public static boolean getConnProviderNames(String packageName, int userId, List<String> providers) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                return iWhetstoneActivityManager.getConnProviderNames(packageName, userId, providers);
            } catch (RemoteException e) {
                Log.e("whetstone.activity", Log.getStackTraceString(e));
            }
        }
        return false;
    }

    public static void setWhetstonePackageInfo(List<WhetstonePackageInfo> info, boolean isAppend) {
        checkService();
        IWhetstoneActivityManager iWhetstoneActivityManager = ws;
        if (iWhetstoneActivityManager != null) {
            try {
                iWhetstoneActivityManager.setWhetstonePackageInfo(info, isAppend);
            } catch (RemoteException e) {
                Log.e("whetstone.activity", Log.getStackTraceString(e));
            }
        }
    }
}
