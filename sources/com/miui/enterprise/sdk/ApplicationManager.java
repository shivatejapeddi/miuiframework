package com.miui.enterprise.sdk;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver2;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.miui.enterprise.EnterpriseManager;
import com.miui.enterprise.IApplicationManager;
import com.miui.enterprise.IApplicationManager.Stub;
import java.util.Collections;
import java.util.List;

public class ApplicationManager {
    public static final String ENT_PERMISSION = "com.miui.enterprise.permission.ACCESS_ENTERPRISE_API";
    public static final int FLAG_ALLOW_AUTOSTART = 8;
    public static final int FLAG_DEFAULT = 0;
    public static final int FLAG_GRANT_ALL_RUNTIME_PERMISSION = 16;
    public static final int FLAG_KEEP_ALIVE = 1;
    public static final int FLAG_PREVENT_UNINSTALLATION = 4;
    public static final String FLOAT = "float";
    public static final String KEYGUARD = "_keyguard";
    public static final String LED = "_led";
    public static final String MESSAGE = "_message";
    public static final int RESTRICTION_MODE_BLACK_LIST = 2;
    public static final int RESTRICTION_MODE_DEFAULT = 0;
    public static final int RESTRICTION_MODE_WHITE_LIST = 1;
    public static final String SOUND = "_sound";
    private static final String TAG = "ApplicationManager";
    public static final String VIBRATE = "_vibrate";
    private static volatile ApplicationManager sInstance;
    private IApplicationManager mService = Stub.asInterface(EnterpriseManager.getEnterpriseService(EnterpriseManager.APPLICATION_MANAGER));

    private ApplicationManager() {
    }

    public static synchronized ApplicationManager getInstance() {
        ApplicationManager applicationManager;
        synchronized (ApplicationManager.class) {
            if (sInstance == null) {
                sInstance = new ApplicationManager();
            }
            applicationManager = sInstance;
        }
        return applicationManager;
    }

    public void installPackage(String path, int flag, IPackageInstallObserver2 observer) {
        installPackage(path, flag, observer, UserHandle.myUserId());
    }

    public void installPackage(String path, int flag, IPackageInstallObserver2 observer, int userId) {
        try {
            this.mService.installPackage(path, flag, observer, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void installPackageWithPendingIntent(String path, PendingIntent pendingIntent) {
        installPackageWithPendingIntent(path, pendingIntent, UserHandle.myUserId());
    }

    public void installPackageWithPendingIntent(String path, PendingIntent pendingIntent, int userId) {
        try {
            this.mService.installPackageWithPendingIntent(path, pendingIntent, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public void deletePackage(String packageName, int flag, IPackageDeleteObserver observer) {
        deletePackage(packageName, flag, observer, UserHandle.myUserId());
    }

    public void deletePackage(String packageName, int flag, IPackageDeleteObserver observer, int userId) {
        try {
            this.mService.deletePackage(packageName, flag, observer, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public void clearApplicationUserData(String packageName) {
        clearApplicationUserData(packageName, UserHandle.myUserId());
    }

    public void clearApplicationUserData(String packageName, int userId) {
        try {
            this.mService.clearApplicationUserData(packageName, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public void clearApplicationCache(String packageName) {
        clearApplicationCache(packageName, UserHandle.myUserId());
    }

    public void clearApplicationCache(String packageName, int userId) {
        try {
            this.mService.clearApplicationCache(packageName, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public void setApplicationSettings(String packageName, int flags) {
        setApplicationSettings(packageName, flags, UserHandle.myUserId());
    }

    public void setApplicationSettings(String packageName, int flags, int userId) {
        try {
            this.mService.setApplicationSettings(packageName, flags, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public int getApplicationSettings(String packageName) {
        return getApplicationSettings(packageName, UserHandle.myUserId());
    }

    public int getApplicationSettings(String packageName, int userId) {
        try {
            return this.mService.getApplicationSettings(packageName, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
            return 0;
        }
    }

    public void setApplicationBlackList(List<String> packages) {
        setApplicationBlackList(packages, UserHandle.myUserId());
    }

    public void setApplicationBlackList(List<String> packages, int userId) {
        try {
            this.mService.setApplicationBlackList(packages, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public List<String> getApplicationBlackList() {
        return getApplicationBlackList(UserHandle.myUserId());
    }

    public List<String> getApplicationBlackList(int userId) {
        try {
            return this.mService.getApplicationBlackList(userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
            return Collections.emptyList();
        }
    }

    public void setApplicationWhiteList(List<String> packages) {
        setApplicationWhiteList(packages, UserHandle.myUserId());
    }

    public void setApplicationWhiteList(List<String> packages, int userId) {
        try {
            this.mService.setApplicationWhiteList(packages, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public List<String> getApplicationWhiteList() {
        return getApplicationWhiteList(UserHandle.myUserId());
    }

    public List<String> getApplicationWhiteList(int userId) {
        try {
            return this.mService.getApplicationWhiteList(userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
            return Collections.emptyList();
        }
    }

    public void setApplicationRestriction(int mode) {
        setApplicationRestriction(mode, UserHandle.myUserId());
    }

    public void setApplicationRestriction(int mode, int userId) {
        try {
            this.mService.setApplicationRestriction(mode, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public int getApplicationRestriction() {
        return getApplicationRestriction(UserHandle.myUserId());
    }

    public int getApplicationRestriction(int userId) {
        try {
            return this.mService.getApplicationRestriction(userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
            return 0;
        }
    }

    public boolean setDeviceAdmin(ComponentName component) {
        return setDeviceAdmin(component, UserHandle.myUserId());
    }

    public boolean setDeviceAdmin(ComponentName component, int userId) {
        try {
            return this.mService.setDeviceAdmin(component, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
            return false;
        }
    }

    public boolean removeDeviceAdmin(ComponentName component) {
        return removeDeviceAdmin(component, UserHandle.myUserId());
    }

    public boolean removeDeviceAdmin(ComponentName component, int userId) {
        try {
            return this.mService.removeDeviceAdmin(component, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
            return false;
        }
    }

    public void killProcess(String packageName) {
        killProcess(packageName, UserHandle.myUserId());
    }

    public void killProcess(String packageName, int userId) {
        try {
            this.mService.killProcess(packageName, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public void enableAccessibilityService(ComponentName componentName, boolean enable) {
        try {
            this.mService.enableAccessibilityService(componentName, enable);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public void setDisallowedRunningAppList(List<String> packages) {
        setDisallowedRunningAppList(packages, UserHandle.myUserId());
    }

    public void setDisallowedRunningAppList(List<String> packages, int userId) {
        try {
            this.mService.setDisallowedRunningAppList(packages, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public List<String> getDisallowedRunningAppList() {
        return getDisallowedRunningAppList(UserHandle.myUserId());
    }

    public List<String> getDisallowedRunningAppList(int userId) {
        try {
            return this.mService.getDisallowedRunningAppList(userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
            return Collections.emptyList();
        }
    }

    public void addTrustedAppStore(List<String> packages) {
        addTrustedAppStore(packages, UserHandle.myUserId());
    }

    public void addTrustedAppStore(List<String> packages, int userId) {
        try {
            this.mService.addTrustedAppStore(packages, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public List<String> getTrustedAppStore() {
        return getTrustedAppStore(UserHandle.myUserId());
    }

    public List<String> getTrustedAppStore(int userId) {
        try {
            return this.mService.getTrustedAppStore(userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
            return Collections.emptyList();
        }
    }

    public void enableTrustedAppStore(boolean enabld) {
        enableTrustedAppStore(enabld, UserHandle.myUserId());
    }

    public void enableTrustedAppStore(boolean enabld, int userId) {
        try {
            this.mService.enableTrustedAppStore(enabld, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public boolean isTrustedAppStoreEnabled() {
        return isTrustedAppStoreEnabled(UserHandle.myUserId());
    }

    public boolean isTrustedAppStoreEnabled(int userId) {
        try {
            return this.mService.isTrustedAppStoreEnabled(userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
            return false;
        }
    }

    public void setApplicationEnabled(String packageName, boolean enable) {
        setApplicationEnabled(packageName, enable, UserHandle.myUserId());
    }

    public void setApplicationEnabled(String packageName, boolean enable, int userId) {
        try {
            this.mService.setApplicationEnabled(packageName, enable, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public void enableNotifications(String packageName, boolean enabled) {
        try {
            this.mService.enableNotifications(packageName, enabled);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public void setNotificaitonFilter(String packageName, String channelId, String type, boolean allow) {
        try {
            this.mService.setNotificaitonFilter(packageName, channelId, type, allow);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public void setXSpaceBlack(List<String> packageName) {
        try {
            this.mService.setXSpaceBlack(packageName);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }

    public List<String> getXSpaceBlack() {
        try {
            return this.mService.getXSpaceBlack();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
            return Collections.emptyList();
        }
    }

    public void grantRuntimePermission(String packageName, String permission, int userId) {
        try {
            this.mService.grantRuntimePermission(packageName, permission, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service died", e);
        }
    }
}
