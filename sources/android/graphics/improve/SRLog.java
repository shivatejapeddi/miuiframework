package android.graphics.improve;

import android.util.Log;
import java.util.Arrays;
import miui.os.Build;

public class SRLog {
    private static final boolean DEBUG = Build.IS_DEBUGGABLE;
    private static final String TAG = "SuperResolution";

    private SRLog() {
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(tag);
            stringBuilder.append(":");
            stringBuilder.append(msg);
            Log.d("SuperResolution", stringBuilder.toString());
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(tag);
            stringBuilder.append(":");
            stringBuilder.append(msg);
            Log.d("SuperResolution", stringBuilder.toString(), tr);
        }
    }

    public static void e(String tag, String msg) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tag);
        stringBuilder.append(":");
        stringBuilder.append(msg);
        Log.e("SuperResolution", stringBuilder.toString());
    }

    public static void e(String tag, String msg, Throwable tr) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tag);
        stringBuilder.append(":");
        stringBuilder.append(msg);
        Log.e("SuperResolution", stringBuilder.toString(), tr);
    }

    public static void v(String tag, String msg) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tag);
        stringBuilder.append(":");
        stringBuilder.append(msg);
        Log.v("SuperResolution", stringBuilder.toString());
    }

    public static void v(String tag, String msg, Throwable tr) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tag);
        stringBuilder.append(":");
        stringBuilder.append(msg);
        Log.v("SuperResolution", stringBuilder.toString(), tr);
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(tag);
            stringBuilder.append(":");
            stringBuilder.append(msg);
            Log.i("SuperResolution", stringBuilder.toString());
        }
    }

    public static void i(String tag, String msg, Throwable tr) {
        if (DEBUG) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(tag);
            stringBuilder.append(":");
            stringBuilder.append(msg);
            Log.i("SuperResolution", stringBuilder.toString(), tr);
        }
    }

    public static void wtf(String tag, String msg) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tag);
        stringBuilder.append(":");
        stringBuilder.append(msg);
        Log.wtf("SuperResolution", stringBuilder.toString());
    }

    public static void wtf(String tag, String msg, Throwable tr) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tag);
        stringBuilder.append(":");
        stringBuilder.append(msg);
        Log.wtf("SuperResolution", stringBuilder.toString(), tr);
    }

    public static String logify(String privacy) {
        if (privacy == null) {
            return null;
        }
        char[] log = new char[privacy.length()];
        Arrays.fill(log, '*');
        return new String(log);
    }
}
