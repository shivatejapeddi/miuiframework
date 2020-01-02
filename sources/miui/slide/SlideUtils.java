package miui.slide;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManagerInternal;
import android.content.pm.ResolveInfo;
import android.os.Process;
import com.android.server.LocalServices;
import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SlideUtils {
    public static String getProcessName(Context context, int pid) {
        for (RunningAppProcessInfo info : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
            if (info.pid == pid) {
                return info.processName;
            }
        }
        return null;
    }

    public static Intent getLaunchIntentForPackageAsUser(String packageName, int userId) {
        Intent intentToResolve = new Intent(Intent.ACTION_MAIN);
        String str = Intent.CATEGORY_INFO;
        intentToResolve.addCategory(str);
        intentToResolve.setPackage(packageName);
        List<ResolveInfo> ris = queryIntentActivitiesAsUser(intentToResolve, 0, userId);
        if (ris == null || ris.size() <= 0) {
            intentToResolve.removeCategory(str);
            intentToResolve.addCategory(Intent.CATEGORY_LAUNCHER);
            intentToResolve.setPackage(packageName);
            ris = queryIntentActivitiesAsUser(intentToResolve, 0, userId);
        }
        if (ris == null || ris.size() <= 0) {
            return null;
        }
        Intent intent = new Intent(intentToResolve);
        intent.setFlags(268435456);
        intent.setClassName(((ResolveInfo) ris.get(0)).activityInfo.packageName, ((ResolveInfo) ris.get(0)).activityInfo.name);
        return intent;
    }

    public static List<ResolveInfo> queryIntentActivitiesAsUser(Intent intent, int flags, int userId) {
        List<ResolveInfo> infos = ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).queryIntentActivities(intent, flags, Process.myUid(), userId);
        if (infos == null) {
            return Collections.emptyList();
        }
        return infos;
    }

    public static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
            }
        }
    }
}
