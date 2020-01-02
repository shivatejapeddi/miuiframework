package android.telephony;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.BatteryStats.HistoryItem;
import android.os.ParcelUuid;
import com.android.internal.util.IndentingPrintWriter;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class AnomalyReporter {
    private static final String TAG = "AnomalyReporter";
    private static Context sContext = null;
    private static String sDebugPackageName = null;
    private static Map<UUID, Integer> sEvents = new ConcurrentHashMap();

    private AnomalyReporter() {
    }

    public static void reportAnomaly(UUID eventId, String description) {
        if (sContext == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AnomalyReporter not yet initialized, dropping event=");
            stringBuilder.append(eventId);
            Rlog.w(TAG, stringBuilder.toString());
            return;
        }
        Integer count = Integer.valueOf(sEvents.containsKey(eventId) ? ((Integer) sEvents.get(eventId)).intValue() + 1 : 1);
        sEvents.put(eventId, count);
        if (count.intValue() <= 1 && sDebugPackageName != null) {
            Intent dbgIntent = new Intent(TelephonyManager.ACTION_ANOMALY_REPORTED);
            dbgIntent.putExtra(TelephonyManager.EXTRA_ANOMALY_ID, new ParcelUuid(eventId));
            if (description != null) {
                dbgIntent.putExtra(TelephonyManager.EXTRA_ANOMALY_DESCRIPTION, description);
            }
            dbgIntent.setPackage(sDebugPackageName);
            sContext.sendBroadcast(dbgIntent, permission.READ_PRIVILEGED_PHONE_STATE);
        }
    }

    public static void initialize(Context context) {
        if (context != null) {
            context.enforceCallingOrSelfPermission(permission.MODIFY_PHONE_STATE, "This app does not have privileges to send debug events");
            sContext = context;
            PackageManager pm = sContext.getPackageManager();
            if (pm != null) {
                List<ResolveInfo> packages = pm.queryBroadcastReceivers(new Intent(TelephonyManager.ACTION_ANOMALY_REPORTED), HistoryItem.MOST_INTERESTING_STATES);
                if (packages != null && !packages.isEmpty()) {
                    int size = packages.size();
                    String str = TAG;
                    if (size > 1) {
                        Rlog.e(str, "Multiple Anomaly Receivers installed.");
                    }
                    for (ResolveInfo r : packages) {
                        if (r.activityInfo != null) {
                            if (pm.checkPermission(permission.READ_PRIVILEGED_PHONE_STATE, r.activityInfo.packageName) == 0) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Found a valid package ");
                                stringBuilder.append(r.activityInfo.packageName);
                                Rlog.d(str, stringBuilder.toString());
                                sDebugPackageName = r.activityInfo.packageName;
                                break;
                            }
                        }
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Found package without proper permissions or no activity");
                        stringBuilder2.append(r.activityInfo.packageName);
                        Rlog.w(str, stringBuilder2.toString());
                    }
                    return;
                }
                return;
            }
            return;
        }
        throw new IllegalArgumentException("AnomalyReporter needs a non-null context.");
    }

    public static void dump(FileDescriptor fd, PrintWriter printWriter, String[] args) {
        if (sContext != null) {
            IndentingPrintWriter pw = new IndentingPrintWriter(printWriter, "  ");
            sContext.enforceCallingOrSelfPermission(permission.DUMP, "Requires DUMP");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Initialized=");
            stringBuilder.append(sContext != null ? "Yes" : "No");
            pw.println(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("Debug Package=");
            stringBuilder.append(sDebugPackageName);
            pw.println(stringBuilder.toString());
            pw.println("Anomaly Counts:");
            pw.increaseIndent();
            for (UUID event : sEvents.keySet()) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(event);
                stringBuilder2.append(": ");
                stringBuilder2.append(sEvents.get(event));
                pw.println(stringBuilder2.toString());
            }
            pw.decreaseIndent();
            pw.flush();
        }
    }
}
