package android.net.wifi;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Binder;
import android.util.Log;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

final class WifiStateMachineInjector {
    private static final String TAG = "WifiStateMachineInj";
    private static final HashSet<String> sBannedProcessName = new HashSet();

    WifiStateMachineInjector() {
    }

    static {
        sBannedProcessName.add("com.amap.android.location");
    }

    public static boolean cancelScan(Context context, AtomicBoolean p2pConnected) {
        if (p2pConnected != null && p2pConnected.get()) {
            List<RunningAppProcessInfo> infos = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
            int callingPid = Binder.getCallingPid();
            for (RunningAppProcessInfo info : infos) {
                if (info.pid == callingPid) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("P2P has connected, the process( name : ");
                    stringBuilder.append(info.processName);
                    stringBuilder.append(", pid : ");
                    stringBuilder.append(callingPid);
                    stringBuilder.append(" ) calls wifi scan.");
                    String stringBuilder2 = stringBuilder.toString();
                    String str = TAG;
                    Log.d(str, stringBuilder2);
                    if (info.importance != 100 || sBannedProcessName.contains(info.processName)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Wifi scan request comes from process ");
                        stringBuilder.append(info.pid);
                        stringBuilder.append(" has canceled!");
                        Log.d(str, stringBuilder.toString());
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
