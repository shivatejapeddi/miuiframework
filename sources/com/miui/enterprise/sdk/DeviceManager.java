package com.miui.enterprise.sdk;

import android.graphics.Bitmap;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.miui.enterprise.EnterpriseManager;
import com.miui.enterprise.IDeviceManager;
import com.miui.enterprise.IDeviceManager.Stub;
import java.util.List;

public class DeviceManager {
    public static final int RESTRICTION_MODE_BLACK_LIST = 2;
    public static final int RESTRICTION_MODE_DEFAULT = 0;
    public static final int RESTRICTION_MODE_WHITE_LIST = 1;
    private static final String TAG = "DeviceManager";
    private static final String VERSION = "MIUI-ENT-4.0";
    private static volatile DeviceManager sInstance;
    private IDeviceManager mService = Stub.asInterface(EnterpriseManager.getEnterpriseService(EnterpriseManager.DEVICE_MANAGER));

    private DeviceManager() {
    }

    public static synchronized DeviceManager getInstance() {
        DeviceManager deviceManager;
        synchronized (DeviceManager.class) {
            if (sInstance == null) {
                sInstance = new DeviceManager();
            }
            deviceManager = sInstance;
        }
        return deviceManager;
    }

    public String getAPIVersion() {
        return VERSION;
    }

    public boolean isDeviceRoot() {
        try {
            return this.mService.isDeviceRoot();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return false;
        }
    }

    public void deviceShutDown() {
        try {
            this.mService.deviceShutDown();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void deviceReboot() {
        try {
            this.mService.deviceReboot();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void formatSdCard() {
        try {
            this.mService.formatSdCard();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void setUrlWhiteList(List<String> urls) {
        setUrlWhiteList(urls, UserHandle.myUserId());
    }

    public void setUrlWhiteList(List<String> urls, int userId) {
        try {
            this.mService.setUrlWhiteList(urls, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void setUrlBlackList(List<String> urls) {
        setUrlBlackList(urls, UserHandle.myUserId());
    }

    public void setUrlBlackList(List<String> urls, int userId) {
        try {
            this.mService.setUrlBlackList(urls, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
        }
    }

    public List<String> getUrlWhiteList() {
        return getUrlWhiteList(UserHandle.myUserId());
    }

    public List<String> getUrlWhiteList(int userId) {
        try {
            return this.mService.getUrlWhiteList(userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
            return null;
        }
    }

    public List<String> getUrlBlackList() {
        return getUrlBlackList(UserHandle.myUserId());
    }

    public List<String> getUrlBlackList(int userId) {
        try {
            return this.mService.getUrlBlackList(userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
            return null;
        }
    }

    public void setBrowserRestriction(int mode) {
        setBrowserRestriction(mode, UserHandle.myUserId());
    }

    public void setBrowserRestriction(int mode, int userId) {
        try {
            this.mService.setBrowserRestriction(mode, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
        }
    }

    public Bitmap captureScreen() {
        try {
            return this.mService.captureScreen();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
            return null;
        }
    }

    public void recoveryFactory(boolean formatSdcard) {
        try {
            this.mService.recoveryFactory(formatSdcard);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
        }
    }

    public boolean setBootAnimation(String path) {
        try {
            return this.mService.setBootAnimation(path);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
            return true;
        }
    }

    public void setDefaultHome(String pkgName) {
        try {
            this.mService.setDefaultHome(pkgName);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
        }
    }

    public String getDefaultHome() {
        try {
            return this.mService.getDefaultHome();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
            return "";
        }
    }

    public void setWallPaper(String path) {
        try {
            this.mService.setWallPaper(path);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
        }
    }

    public void setLockWallPaper(String path) {
        try {
            this.mService.setLockWallPaper(path);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service has died", e);
        }
    }
}
