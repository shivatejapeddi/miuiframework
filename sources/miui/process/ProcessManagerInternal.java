package miui.process;

import android.content.ComponentName;
import android.content.pm.ApplicationInfo;

public abstract class ProcessManagerInternal {
    public abstract void forceStopPackage(String str, int i, String str2);

    public abstract IMiuiApplicationThread getMiuiApplicationThread(int i);

    public abstract ApplicationInfo getMultiWindowForegroundAppInfoLocked();

    public abstract void notifyActivityChanged(ComponentName componentName);

    public abstract void notifyForegroundInfoChanged(Object obj, Object obj2, int i, ApplicationInfo applicationInfo);

    public abstract void onCleanUpApplicationRecord(Object obj);

    public abstract void recordKillProcessEventIfNeeded(String str, String str2, int i);

    public abstract void updateProcessForegroundLocked(int i);
}
