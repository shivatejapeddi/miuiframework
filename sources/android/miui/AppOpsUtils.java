package android.miui;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManagerCompat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Log;

public class AppOpsUtils {
    public static final int ACTION_ACCEPT = 3;
    public static final int ACTION_DEFAULT = 0;
    public static final int ACTION_PROMPT = 2;
    public static final int ACTION_REJECT = 1;
    private static final String AUTHORITY = "com.lbe.security.miui.permmgr";
    private static final Uri CONTENT_URI = Uri.parse("content://com.lbe.security.miui.permmgr");
    private static final String EXTRA_ACTION = "extra_action";
    private static final String EXTRA_FLAGS = "extra_flags";
    private static final String EXTRA_PACKAGE = "extra_package";
    private static final String EXTRA_PERMISSION = "extra_permission";
    public static final long PERM_ID_NOTIFICATION = 32768;
    public static final int SET_APPLICATION_PERMISSION = 6;
    private static final String TAG = "AppOpsUtils";

    public static void setApplicationAutoStart(Context context, String packageName, boolean autoStart) {
        try {
            ((AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE)).setMode(10008, PackageManagerCompat.getPackageUidAsUser(context.getPackageManager(), packageName, UserHandle.getUserId(Binder.getCallingUid())), packageName, autoStart ? 0 : 2);
        } catch (NameNotFoundException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't obtain the uid for package: ");
            stringBuilder.append(packageName);
            Log.e(TAG, stringBuilder.toString(), e);
        }
    }

    public static int getApplicationAutoStart(Context context, String packageName) {
        try {
            return ((AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE)).checkOpNoThrow(10008, PackageManagerCompat.getPackageUidAsUser(context.getPackageManager(), packageName, UserHandle.getUserId(Binder.getCallingUid())), packageName);
        } catch (NameNotFoundException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't obtain the uid for package: ");
            stringBuilder.append(packageName);
            Log.e(TAG, stringBuilder.toString(), e);
            return 1;
        }
    }

    public static int getApplicationAutoStart(Context context, String packageName, int uid) {
        return ((AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE)).checkOpNoThrow(10008, uid, packageName);
    }

    public static void setMode(final Context context, int code, final String packageName, final int mode) {
        if (code == 11) {
            new AsyncTask<Void, Void, Void>() {
                /* Access modifiers changed, original: protected|varargs */
                public Void doInBackground(Void... params) {
                    Bundle extras = new Bundle();
                    int action = 1;
                    int i = mode;
                    if (i == 0) {
                        action = 3;
                    } else if (i == 1 || i == 2) {
                        action = 1;
                    } else if (i == 5) {
                        action = 2;
                    }
                    extras.putLong(AppOpsUtils.EXTRA_PERMISSION, 32768);
                    extras.putInt(AppOpsUtils.EXTRA_ACTION, action);
                    extras.putStringArray(AppOpsUtils.EXTRA_PACKAGE, new String[]{packageName});
                    extras.putInt(AppOpsUtils.EXTRA_FLAGS, 0);
                    try {
                        context.getContentResolver().call(AppOpsUtils.CONTENT_URI, String.valueOf(6), null, extras);
                    } catch (Exception e) {
                        Log.e(AppOpsUtils.TAG, "SET_APPLICATION_PERMISSION : ", e);
                    }
                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("not support code :");
        stringBuilder.append(code);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static boolean isXOptMode() {
        return SystemProperties.getBoolean("persist.sys.miui_optimization", "1".equals(SystemProperties.get("ro.miui.cts")) ^ 1) ^ 1;
    }
}
