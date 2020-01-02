package android.os;

import android.util.Log;

class ProcessInjector {
    private static final String TAG = "ProcessInjector";
    private static Object mAm;

    ProcessInjector() {
    }

    static void reportKillProcessEvent(int killedPid) {
        try {
            if (mAm == null) {
                mAm = Class.forName("android.app.ActivityManagerNative").getDeclaredMethod("getDefault", new Class[0]).invoke(null, new Object[0]);
            }
            mAm.getClass().getDeclaredMethod("reportKillProcessEvent", new Class[]{Integer.TYPE, Integer.TYPE}).invoke(mAm, new Object[]{Integer.valueOf(Process.myPid()), Integer.valueOf(killedPid)});
        } catch (Exception e) {
            Log.e(TAG, "error while reportKillProcessEvent to system server!", e);
        }
    }
}
