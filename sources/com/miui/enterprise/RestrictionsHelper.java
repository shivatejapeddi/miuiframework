package com.miui.enterprise;

import android.content.Context;
import android.os.storage.DiskInfo;
import android.os.storage.VolumeInfo;
import android.util.Slog;
import com.miui.enterprise.sdk.RestrictionsManager;
import com.miui.enterprise.settings.EnterpriseSettings;
import java.util.ArrayList;
import java.util.List;

public class RestrictionsHelper {
    private static final String TAG = "Enterprise";
    private static final List<String> whiteList = new ArrayList();

    private RestrictionsHelper() {
    }

    static {
        whiteList.add(RestrictionsManager.DISALLOW_KEY_BACK);
        whiteList.add(RestrictionsManager.DISALLOW_KEY_MENU);
        whiteList.add(RestrictionsManager.DISALLOW_KEY_HOME);
        whiteList.add(RestrictionsManager.DISALLOW_SCREENCAPTURE);
    }

    public static boolean hasRestriction(Context context, String key) {
        boolean z = true;
        if (whiteList.contains(key)) {
            if (EnterpriseSettings.getInt(context, key, 0) != 1) {
                z = false;
            }
            return z;
        } else if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        } else {
            if (EnterpriseSettings.getInt(context, key, 0) != 1) {
                z = false;
            }
            return z;
        }
    }

    public static boolean hasRestriction(Context context, String key, int userId) {
        boolean z = true;
        if (whiteList.contains(key)) {
            if (EnterpriseSettings.getInt(context, key, 0, userId) != 1) {
                z = false;
            }
            return z;
        } else if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        } else {
            if (EnterpriseSettings.getInt(context, key, 0, userId) != 1) {
                z = false;
            }
            return z;
        }
    }

    private static int getControlState(Context context, String key) {
        return EnterpriseSettings.getInt(context, key, 1);
    }

    public static boolean isCameraRestricted(Context context) {
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        }
        boolean isRestricted = hasRestriction(context, RestrictionsManager.DISALLOW_CAMERA);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Camera is restricted: ");
        stringBuilder.append(isRestricted);
        Slog.d(TAG, stringBuilder.toString());
        return isRestricted;
    }

    public static boolean isUsbDeviceRestricted(Context context) {
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        }
        boolean isRestricted = hasRestriction(context, RestrictionsManager.DISALLOW_USB_DEVICE);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Usb device is restricted: ");
        stringBuilder.append(isRestricted);
        Slog.d(TAG, stringBuilder.toString());
        return isRestricted;
    }

    public static boolean handleBluetoothChange(Context context, boolean isOpen) {
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        }
        int bluetoothState = getControlState(context, "bluetooth_state");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bluetooth restrict state: ");
        stringBuilder.append(bluetoothState);
        Slog.d(TAG, stringBuilder.toString());
        if (isOpen && bluetoothState == 0) {
            return true;
        }
        if (isOpen || bluetoothState != 4) {
            return false;
        }
        return true;
    }

    public static boolean hasWifiRestriction(Context context) {
        return isRestrictionState(context, "wifi_state");
    }

    public static boolean handleAirplaneChange(Context context, boolean origin) {
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return origin;
        }
        int wifiState = getControlState(context, RestrictionsManager.AIRPLANE_STATE);
        String str = TAG;
        if (wifiState == 0) {
            Slog.d(str, "Airplane mod is disabled");
            return false;
        } else if (wifiState != 4) {
            return origin;
        } else {
            Slog.d(str, "Airplane mod is force opened");
            return true;
        }
    }

    public static boolean isMountDisallowed(Context context, VolumeInfo vol) {
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        }
        DiskInfo diskInfo = vol.disk;
        String str = TAG;
        if (diskInfo != null && (vol.disk.flags & 4) != 0 && hasRestriction(context, RestrictionsManager.DISALLOW_SDCARD)) {
            Slog.d(str, "Sdcard is restricted");
            return true;
        } else if (vol.disk == null || (vol.disk.flags & 8) == 0 || !hasRestriction(context, RestrictionsManager.DISALLOW_USB_DEVICE)) {
            return false;
        } else {
            Slog.d(str, "Usb device is restricted");
            return true;
        }
    }

    public static boolean hasNFCRestriction(Context context) {
        return isRestrictionState(context, RestrictionsManager.NFC_STATE);
    }

    public static boolean hasAirplaneRestriction(Context context) {
        return isRestrictionState(context, RestrictionsManager.AIRPLANE_STATE);
    }

    private static boolean isRestrictionState(Context context, String key) {
        boolean z = false;
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED) {
            return false;
        }
        int state = getControlState(context, key);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(key);
        stringBuilder.append(": ");
        stringBuilder.append(state);
        Slog.d(TAG, stringBuilder.toString());
        if (state == 0 || state == 4) {
            z = true;
        }
        return z;
    }

    public static boolean hasKeyCodeRestriction(Context context, int keycode, int userId) {
        String key;
        if (keycode == 3) {
            key = RestrictionsManager.DISALLOW_KEY_HOME;
        } else if (keycode == 4) {
            key = RestrictionsManager.DISALLOW_KEY_BACK;
        } else if (keycode != 82 && keycode != 187) {
            return false;
        } else {
            key = RestrictionsManager.DISALLOW_KEY_MENU;
        }
        return hasRestriction(context, key, userId);
    }
}
