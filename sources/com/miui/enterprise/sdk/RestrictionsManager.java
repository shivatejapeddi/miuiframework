package com.miui.enterprise.sdk;

import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.miui.enterprise.EnterpriseManager;
import com.miui.enterprise.IRestrictionsManager;
import com.miui.enterprise.IRestrictionsManager.Stub;

public class RestrictionsManager {
    public static final String AIRPLANE_STATE = "airplane_state";
    public static final String BLUETOOTH_STATE = "bluetooth_state";
    public static final int CLOSE = 3;
    public static final int DISABLE = 0;
    public static final String DISABLE_ACCELEROMETER = "disable_accelerometer";
    public static final String DISALLOW_AUTO_SYNC = "disallow_auto_sync";
    public static final String DISALLOW_BACKUP = "disallow_backup";
    public static final String DISALLOW_CAMERA = "disallow_camera";
    public static final String DISALLOW_CHANGE_LANGUAGE = "disallow_change_language";
    public static final String DISALLOW_FACTORYRESET = "disallow_factoryreset";
    public static final String DISALLOW_FINGERPRINT = "disallow_fingerprint";
    public static final String DISALLOW_IMEIREAD = "disallow_imeiread";
    public static final String DISALLOW_KEY_BACK = "disallow_key_back";
    public static final String DISALLOW_KEY_HOME = "disallow_key_home";
    public static final String DISALLOW_KEY_MENU = "disallow_key_menu";
    public static final String DISALLOW_MICROPHONE = "disallow_microphone";
    public static final String DISALLOW_MI_ACCOUNT = "disallow_mi_account";
    public static final String DISALLOW_MTP = "disallow_mtp";
    public static final String DISALLOW_OTG = "disallow_otg";
    public static final String DISALLOW_SAFE_MODE = "disallow_safe_mode";
    public static final String DISALLOW_SCREENCAPTURE = "disallow_screencapture";
    public static final String DISALLOW_SDCARD = "disallow_sdcard";
    public static final String DISALLOW_STATUS_BAR = "disallow_status_bar";
    public static final String DISALLOW_SYSTEM_UPDATE = "disallow_system_update";
    public static final String DISALLOW_TETHER = "disallow_tether";
    public static final String DISALLOW_TIMESET = "disallow_timeset";
    public static final String DISALLOW_USBDEBUG = "disallow_usbdebug";
    public static final String DISALLOW_USB_DEVICE = "disable_usb_device";
    public static final String DISALLOW_VPN = "disallow_vpn";
    public static final int ENABLE = 1;
    public static final int FORCE_OPEN = 4;
    public static final String GPS_STATE = "gps_state";
    public static final String NFC_STATE = "nfc_state";
    public static final int OPEN = 2;
    public static final String WIFI_STATE = "wifi_state";
    private static volatile RestrictionsManager sInstance;
    private String TAG = "RestrictionsManager";
    private IRestrictionsManager mService = Stub.asInterface(EnterpriseManager.getEnterpriseService(EnterpriseManager.RESTRICTIONS_MANAGER));

    private RestrictionsManager() {
    }

    public static synchronized RestrictionsManager getInstance() {
        RestrictionsManager restrictionsManager;
        synchronized (RestrictionsManager.class) {
            if (sInstance == null) {
                sInstance = new RestrictionsManager();
            }
            restrictionsManager = sInstance;
        }
        return restrictionsManager;
    }

    public void setControlStatus(String key, int value) {
        setControlStatus(key, value, UserHandle.myUserId());
    }

    public void setControlStatus(String key, int value, int userId) {
        try {
            this.mService.setControlStatus(key, value, userId);
        } catch (RemoteException e) {
            Log.e(this.TAG, "Remote service dead");
        }
    }

    public void setRestriction(String key, boolean value) {
        setRestriction(key, value, UserHandle.myUserId());
    }

    public void setRestriction(String key, boolean value, int userId) {
        try {
            this.mService.setRestriction(key, value, userId);
        } catch (RemoteException e) {
            Log.e(this.TAG, "Remote service dead");
        }
    }

    public int getControlStatus(String key) {
        return getControlStatus(key, UserHandle.myUserId());
    }

    public int getControlStatus(String key, int userId) {
        try {
            return this.mService.getControlStatus(key, userId);
        } catch (RemoteException e) {
            Log.e(this.TAG, "Remote service dead");
            return 1;
        }
    }

    public boolean hasRestriction(String key) {
        return hasRestriction(key, UserHandle.myUserId());
    }

    public boolean hasRestriction(String key, int userId) {
        try {
            return this.mService.hasRestriction(key, userId);
        } catch (RemoteException e) {
            Log.e(this.TAG, "Remote service dead");
            return false;
        }
    }
}
