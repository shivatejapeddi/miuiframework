package com.miui.enterprise.sdk;

import android.os.RemoteException;
import com.miui.enterprise.EnterpriseManager;
import com.miui.enterprise.IAPNManager;
import com.miui.enterprise.IAPNManager.Stub;
import java.util.Collections;
import java.util.List;
import miui.util.Log;

public class APNManager {
    public static final int MODE_DEFAULT = 0;
    public static final int MODE_RESTRICTED = 1;
    private static final String TAG = "APNManager";
    private static volatile APNManager sInstance;
    private IAPNManager mService = Stub.asInterface(EnterpriseManager.getEnterpriseService(EnterpriseManager.APN_MANAGER));

    private APNManager() {
    }

    public static synchronized APNManager getInstance() {
        APNManager aPNManager;
        synchronized (APNManager.class) {
            if (sInstance == null) {
                sInstance = new APNManager();
            }
            aPNManager = sInstance;
        }
        return aPNManager;
    }

    public List<APNConfig> getAPNList(String numeric) {
        try {
            return this.mService.getAPNListForNumeric(numeric);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return Collections.emptyList();
        }
    }

    public List<APNConfig> getAPNList() {
        try {
            return this.mService.getAPNList();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return Collections.emptyList();
        }
    }

    public APNConfig getAPN(String name) {
        try {
            return this.mService.getAPN(name);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return null;
        }
    }

    public APNConfig getAPN(String numeric, String name) {
        try {
            return this.mService.getAPNForNumeric(numeric, name);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return null;
        }
    }

    public void addAPN(String numeric, APNConfig apnConfig) {
        try {
            this.mService.addAPNForNumeric(numeric, apnConfig);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void addAPN(APNConfig apnConfig) {
        try {
            this.mService.addAPN(apnConfig);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public void deleteAPN(String numeric, String name) {
        try {
            this.mService.deleteAPNForNumeric(numeric, name);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public boolean deleteAPN(String name) {
        try {
            return this.mService.deleteAPN(name);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return false;
        }
    }

    public void editAPN(String numeric, String name, APNConfig config) {
        try {
            this.mService.editAPNForNumeric(numeric, name, config);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public boolean editAPN(String name, APNConfig config) {
        try {
            return this.mService.editAPN(name, config);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return false;
        }
    }

    public void activeAPN(String numeric, String name) {
        try {
            this.mService.activeAPNForNumeric(numeric, name);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public boolean activeAPN(String name) {
        try {
            return this.mService.activeAPN(name);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return false;
        }
    }

    public boolean resetAPN() {
        try {
            return this.mService.resetAPN();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return false;
        }
    }

    public void setAPNActiveMode(int mode) {
        try {
            this.mService.setAPNActiveMode(mode);
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
        }
    }

    public int getAPNActiveMode() {
        try {
            return this.mService.getAPNActiveMode();
        } catch (RemoteException e) {
            Log.e(TAG, "Remote service dead", e);
            return 0;
        }
    }
}
