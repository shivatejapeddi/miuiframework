package miui.app;

import android.net.ConnectivityManager;
import android.net.ConnectivityManager.OnStartTetheringCallback;
import android.net.wifi.WifiManager;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import miui.util.ObjectReference;
import miui.util.ReflectionUtils;

public class ConnectivityManagerReflector {
    private static final String TAG = "WifiManagerReflector";
    public static final int TETHERING_WIFI = 0;

    public static boolean stopTethering(ConnectivityManager cm, int type) {
        String str = TAG;
        try {
            Method method = ConnectivityManager.class.getMethod("stopTethering", new Class[]{Integer.TYPE});
            method.setAccessible(true);
            method.invoke(cm, new Object[]{Integer.valueOf(type)});
            return true;
        } catch (NoSuchMethodException e) {
            Log.e(str, "NoSuchMethodException", e);
            return false;
        } catch (IllegalAccessException e2) {
            Log.e(str, "IllegalAccessException", e2);
            return false;
        } catch (InvocationTargetException e3) {
            Log.e(str, "InvocationTargetException", e3);
            return false;
        } catch (Exception e4) {
            Log.e(str, "Exception", e4);
            return false;
        }
    }

    public static boolean startTethering(ConnectivityManager cm, int type, boolean showProvisioningUi) {
        String str = TAG;
        try {
            Method method = ConnectivityManager.class.getMethod("startTethering", new Class[]{Integer.TYPE, Boolean.TYPE, Class.forName("android.net.ConnectivityManager$OnStartTetheringCallback")});
            method.setAccessible(true);
            OnStartTetheringCallback callback = new OnStartTetheringCallback() {
            };
            method.invoke(cm, new Object[]{Integer.valueOf(type), Boolean.valueOf(showProvisioningUi), callback});
            return true;
        } catch (ClassNotFoundException e) {
            Log.e(str, "ClassNotFoundException", e);
            return false;
        } catch (NoSuchMethodException e2) {
            Log.e(str, "NoSuchMethodException", e2);
            return false;
        } catch (IllegalAccessException e3) {
            Log.e(str, "IllegalAccessException", e3);
            return false;
        } catch (InvocationTargetException e4) {
            Log.e(str, "InvocationTargetException", e4);
            return false;
        } catch (Exception e5) {
            Log.e(str, "Exception", e5);
            return false;
        }
    }

    public static boolean getWifiStaSapConcurrency(WifiManager wifiManager) {
        ObjectReference<Boolean> reference = ReflectionUtils.tryCallMethod(wifiManager, "getWifiStaSapConcurrency", Boolean.class, new Object[0]);
        if (reference == null) {
            return false;
        }
        return ((Boolean) reference.get()).booleanValue();
    }
}
