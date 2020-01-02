package com.android.id;

import android.content.Context;
import java.lang.reflect.Method;
import miui.util.Log;

public class IdentifierManager {
    private static final String TAG = "IdentifierManager";
    private static Class<?> sClass;
    private static Method sGetAAID;
    private static Method sGetOAID;
    private static Method sGetUDID;
    private static Method sGetVAID;
    private static Object sIdProivderImpl;

    static {
        sGetUDID = null;
        sGetOAID = null;
        sGetVAID = null;
        sGetAAID = null;
        try {
            sClass = Class.forName("com.android.id.impl.IdProviderImpl");
            sIdProivderImpl = sClass.newInstance();
            sGetUDID = sClass.getMethod("getUDID", new Class[]{Context.class});
            sGetOAID = sClass.getMethod("getOAID", new Class[]{Context.class});
            sGetVAID = sClass.getMethod("getVAID", new Class[]{Context.class});
            sGetAAID = sClass.getMethod("getAAID", new Class[]{Context.class});
        } catch (Exception e) {
            Log.e(TAG, "reflect exception!", e);
        }
    }

    public static boolean isSupported() {
        return (sClass == null || sIdProivderImpl == null) ? false : true;
    }

    public static String getUDID(Context context) {
        return invokeMethod(context, sGetUDID);
    }

    public static String getOAID(Context context) {
        return invokeMethod(context, sGetOAID);
    }

    public static String getVAID(Context context) {
        return invokeMethod(context, sGetVAID);
    }

    public static String getAAID(Context context) {
        return invokeMethod(context, sGetAAID);
    }

    private static String invokeMethod(Context context, Method method) {
        Object result = sIdProivderImpl;
        if (!(result == null || method == null)) {
            try {
                result = method.invoke(result, new Object[]{context});
                if (result != null) {
                    return (String) result;
                }
            } catch (Exception e) {
                Log.e(TAG, "invoke exception!", e);
            }
        }
        return null;
    }
}
