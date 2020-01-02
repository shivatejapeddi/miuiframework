package miui.app;

import android.content.Context;
import android.hardware.display.IDisplayManager.Stub;
import android.net.wifi.WifiManager;
import android.os.RemoteException;
import android.os.ServiceManager;

public class CompatibilityP {
    public static void setTemporaryScreenBrightness(int brightnessValue) {
        try {
            Stub.asInterface(ServiceManager.getService(Context.DISPLAY_SERVICE)).setTemporaryBrightness(brightnessValue);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void setTemporaryScreenAutoBrightness(float valf) {
        try {
            Stub.asInterface(ServiceManager.getService(Context.DISPLAY_SERVICE)).setTemporaryAutoBrightnessAdjustment(valf);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static boolean setWifiApEnabled(WifiManager wifiManager, boolean enable) {
        return false;
    }
}
