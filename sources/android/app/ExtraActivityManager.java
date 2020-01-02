package android.app;

import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.RemoteException;
import com.google.android.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import miui.util.AudioOutputHelper;

public class ExtraActivityManager {
    public static final int START_INCOMPATIBLE = 5;
    private static String TAG = ExtraActivityManager.class.getName();

    public static class PackageNameList {
        public final List<String> mOrderList;
        public final int mPlayingCount;

        public PackageNameList(List<String> list, int playingCount) {
            this.mOrderList = list;
            this.mPlayingCount = playingCount;
        }
    }

    public static class PriorityComponent {
        public final ComponentName mComponent;
        public final int mPriority;

        public PriorityComponent(ComponentName component, int p) {
            this.mComponent = component;
            this.mPriority = p;
        }
    }

    private static String getPackageNameForTask(RecentTaskInfo info) {
        String pkgName = null;
        Intent baseIntent = info.baseIntent;
        if (baseIntent != null) {
            pkgName = baseIntent.getPackage();
            if (pkgName == null && baseIntent.getComponent() != null) {
                pkgName = baseIntent.getComponent().getPackageName();
            }
        }
        if (pkgName != null || info.origActivity == null) {
            return pkgName;
        }
        return info.origActivity.getPackageName();
    }

    public static List<String> getPackageNameListForRecentTasks(Context context) {
        List<RecentTaskInfo> tasks = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        long token = Binder.clearCallingIdentity();
        try {
            tasks = am.getRecentTasks(20, 3);
            if (tasks == null) {
                return null;
            }
            List<String> names = new ArrayList(tasks.size());
            for (RecentTaskInfo t : tasks) {
                String name = getPackageNameForTask(t);
                if (name != null) {
                    names.add(name);
                }
            }
            return names;
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    public static PackageNameList getPackageNameListOrderByReceivePriority(Context context) {
        List<String> packageNames = Lists.newArrayList();
        int playingCount = 0;
        List<RunningAppProcessInfo> actives = AudioOutputHelper.getActiveClientProcessList(((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses(), context, true);
        if (actives != null) {
            for (RunningAppProcessInfo info : actives) {
                if (info.pkgList != null) {
                    for (String pkg : info.pkgList) {
                        packageNames.add(pkg);
                    }
                }
            }
            playingCount = packageNames.size();
        }
        List<String> tasks = getPackageNameListForRecentTasks(context);
        if (tasks != null) {
            packageNames.addAll(tasks);
        }
        return new PackageNameList(packageNames, playingCount);
    }

    public static PriorityComponent getMediaButtonReceiver(Context context, List<String> orderPackageNames, int minPriority) {
        if (minPriority == 0) {
            return null;
        }
        ParceledListSlice<ResolveInfo> receivers = null;
        try {
            receivers = AppGlobals.getPackageManager().queryIntentReceivers(new Intent(Intent.ACTION_MEDIA_BUTTON), null, 0, 0);
        } catch (RemoteException e) {
        }
        if (receivers != null && receivers.getList().size() > 0) {
            int p = 0;
            for (String name : orderPackageNames) {
                for (ResolveInfo r : receivers.getList()) {
                    if (r.activityInfo != null && r.activityInfo.name != null && name.equals(r.activityInfo.packageName)) {
                        return new PriorityComponent(new ComponentName(r.activityInfo.packageName, r.activityInfo.name), p);
                    }
                }
                p++;
                if (p >= minPriority) {
                    break;
                }
            }
        }
        return null;
    }
}
