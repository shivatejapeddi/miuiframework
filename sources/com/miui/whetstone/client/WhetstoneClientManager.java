package com.miui.whetstone.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemProperties;
import android.provider.MiuiSettings.XSpace;
import android.util.Log;
import com.miui.whetstone.IWhetstoneClient;
import com.miui.whetstone.process.WtServiceControlEntry;
import com.miui.whetstone.server.WhetstoneActivityManagerService;
import com.miui.whetstone.strategy.WhetstoneSystemSetting;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import miui.util.MiuiFeatureUtils;

public abstract class WhetstoneClientManager {
    private static final boolean BOARD_PERFORMANCE_SUPPORT = Build.HARDWARE.equals("qcom");
    private static boolean CACHAED_STATISTICS_SUPPORT = MiuiFeatureUtils.isSystemFeatureSupported("feature_cached_statistics_suport", true);
    private static boolean CHECK_APP_MEMORY_SUPPORT = MiuiFeatureUtils.isSystemFeatureSupported("feature_check_app_memory_suport", true);
    private static final boolean DEBUG = false;
    private static final String POWER_PROFILE_CONFIG = "persist.sys.aries.power_profile";
    private static final String POWER_PROFILE_PERFORMANCE = "high";
    private static boolean RED_SUPPORT = MiuiFeatureUtils.isSystemFeatureSupported("feature_red_suport", true);
    private static final String TAG = "WhetstoneClientManager";
    private static Context mContext = null;
    private static final Object mLock = new Object();
    private static ConcurrentHashMap<Integer, List<String>> mProcessLocked = new ConcurrentHashMap();
    private static IWhetstoneClient mService;
    public static WhetstoneSystemSetting mSetting;
    private static HashMap<String, Integer> mThresholds = new HashMap();
    private static WhetstoneActivityManagerService mWhetstoneAM = null;
    private static List<String> protectApps = new ArrayList();
    private static long sAndroidCachePss;
    private static int sCallingPid;
    private static boolean sPerformanceEnable = false;
    private static HashSet<ComponentName> sPerformanceSet = new HashSet();
    private static String sPowerProfile;

    static {
        String str = "com.tencent.mm";
        protectApps.add(str);
        protectApps.add("com.jeejen.family.miui");
        protectApps.add("com.google.android.gms");
        sPerformanceSet.add(new ComponentName(str, "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI"));
        sPerformanceSet.add(new ComponentName(str, "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI"));
        sPerformanceSet.add(new ComponentName(str, XSpace.KEY_WEIXIN_OPEN));
    }

    public static void init(Context context, IWhetstoneClient client, WhetstoneActivityManagerService whetstoneAM) {
        mContext = context;
        mService = client;
        mSetting = new WhetstoneSystemSetting(mContext, whetstoneAM);
        mSetting.addObserver(WtServiceControlEntry.getInstance());
        mWhetstoneAM = whetstoneAM;
        sCallingPid = -1;
    }

    public static boolean isSystemProtectImportantApp(String processName) {
        for (String item : protectApps) {
            if (item.equals(processName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isProtectImportantApp(String processName) {
        return isProtectImportantAppWithUid(processName, 0);
    }

    public static boolean isProtectImportantAppWithUid(String processName, int userId) {
        for (String item : protectApps) {
            if (item.equals(processName)) {
                return true;
            }
        }
        List<String> lockedApps = (List) mProcessLocked.get(Integer.valueOf(userId));
        if (processName == null || lockedApps == null) {
            return false;
        }
        for (String item2 : lockedApps) {
            if (item2.equals(processName)) {
                return true;
            }
        }
        return false;
    }

    public static boolean checkIfPackageIsLocked(String packageName) {
        return checkIfPackageIsLocked(packageName, 0);
    }

    public static boolean checkIfPackageIsLocked(String packageName, int userId) {
        List<String> lockedApps = (List) mProcessLocked.get(Integer.valueOf(userId));
        if (lockedApps == null) {
            return false;
        }
        for (String name : lockedApps) {
            if (packageName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static void setCachedPidLam(int index, int pid, long lastPss) {
        if (CACHAED_STATISTICS_SUPPORT) {
            if (index == 0) {
                sAndroidCachePss = 0;
            }
            sAndroidCachePss += lastPss;
        }
    }

    public static long getEmptyProcTotalMemoryInfo() {
        if (CACHAED_STATISTICS_SUPPORT) {
            return sAndroidCachePss;
        }
        return 0;
    }

    public static boolean AppBGIdleFeatureIsEnable() {
        WhetstoneActivityManagerService whetstoneActivityManagerService = mWhetstoneAM;
        if (whetstoneActivityManagerService != null) {
            return whetstoneActivityManagerService.getPowerKeeperPolicy().getAppBGIdleFeatureEnable();
        }
        return false;
    }

    public static int getAppBGIdleLevel(int uid) {
        WhetstoneActivityManagerService whetstoneActivityManagerService = mWhetstoneAM;
        if (whetstoneActivityManagerService != null) {
            return whetstoneActivityManagerService.getPowerKeeperPolicy().getAppBGIdleLevel(uid);
        }
        return 0;
    }

    public static void setCallingProcessPid(int pid) {
        sCallingPid = pid;
    }

    public static boolean addComponment(ComponentName[] comps) {
        synchronized (mLock) {
            HashSet<ComponentName> pset = (HashSet) sPerformanceSet.clone();
            for (ComponentName comp : comps) {
                pset.add(comp);
            }
            sPerformanceSet = pset;
        }
        return true;
    }

    public static boolean setComponment(ComponentName[] comps) {
        synchronized (mLock) {
            HashSet<ComponentName> pset = new HashSet();
            for (ComponentName comp : comps) {
                pset.add(comp);
            }
            sPerformanceSet = pset;
        }
        return true;
    }

    public static boolean prepareAppTransitionLam(ComponentName realActivity, ComponentName realActivity2) {
        if (!RED_SUPPORT || realActivity2 == null) {
            return false;
        }
        try {
            if (sPerformanceSet.contains(realActivity2)) {
                sPerformanceEnable = true;
                doPerformanceLam();
                return true;
            }
            sPerformanceEnable = false;
            doResumeLam();
            return false;
        } catch (Exception e) {
            Log.e(TAG, "prepareAppTransitionLam", e);
        }
    }

    private static void doResumeLam() {
        String str = sPowerProfile;
        if (str != null && BOARD_PERFORMANCE_SUPPORT) {
            SystemProperties.set("persist.sys.aries.power_profile", str);
            sPowerProfile = null;
        }
    }

    private static void doPerformanceLam() {
        String str = "persist.sys.aries.power_profile";
        if (sPowerProfile == null && BOARD_PERFORMANCE_SUPPORT) {
            sPowerProfile = SystemProperties.get(str, "middle");
        }
        if (BOARD_PERFORMANCE_SUPPORT) {
            SystemProperties.set(str, "high");
        }
    }

    public static boolean isAlarmAllowedLocked(int pid, int uid, String tag, boolean generalRestrictApply) {
        WhetstoneActivityManagerService whetstoneActivityManagerService = mWhetstoneAM;
        if (whetstoneActivityManagerService == null || !generalRestrictApply) {
            return true;
        }
        return whetstoneActivityManagerService.isAlarmAllowedLocked(pid, uid, tag);
    }

    public static boolean isBroadcastAllowedLocked(int pid, int uid, String type) {
        return true;
    }

    public static boolean startServiceAllowed(ComponentName name, int intentFlags, boolean whileRestarting) {
        return true;
    }

    public static boolean startServiceAllowed(Context context, Intent service, String resolvedType, int userId) {
        return true;
    }

    public static void updateApplicationsMemoryThreshold(List<String> thresholds) {
        synchronized (mThresholds) {
            for (String threshold : thresholds) {
                String[] values = threshold.split("#");
                if (values.length == 2) {
                    mThresholds.put(values[0], Integer.valueOf(values[1]));
                }
            }
        }
    }

    public static void updateUserLockedAppList(List<String> thresholds) {
        updateUserLockedAppList(thresholds, 0);
    }

    public static void updateUserLockedAppList(List<String> thresholds, int userId) {
        mProcessLocked.put(Integer.valueOf(userId), thresholds);
    }

    public static void updatePackageLockedStatus(String packageName, boolean locked) {
        updatePackageLockedStatus(packageName, locked, 0);
    }

    /* JADX WARNING: Missing block: B:9:0x0028, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:19:0x004c, code skipped:
            return;
     */
    public static void updatePackageLockedStatus(java.lang.String r6, boolean r7, int r8) {
        /*
        r0 = mProcessLocked;
        monitor-enter(r0);
        r1 = mProcessLocked;	 Catch:{ all -> 0x004d }
        r2 = java.lang.Integer.valueOf(r8);	 Catch:{ all -> 0x004d }
        r1 = r1.get(r2);	 Catch:{ all -> 0x004d }
        r1 = (java.util.List) r1;	 Catch:{ all -> 0x004d }
        r2 = 0;
        r3 = 0;
        if (r1 != 0) goto L_0x0029;
    L_0x0013:
        r4 = new java.util.ArrayList;	 Catch:{ all -> 0x004d }
        r4.<init>();	 Catch:{ all -> 0x004d }
        r1 = r4;
        if (r7 == 0) goto L_0x0027;
    L_0x001b:
        r1.add(r6);	 Catch:{ all -> 0x004d }
        r4 = mProcessLocked;	 Catch:{ all -> 0x004d }
        r5 = java.lang.Integer.valueOf(r8);	 Catch:{ all -> 0x004d }
        r4.put(r5, r1);	 Catch:{ all -> 0x004d }
    L_0x0027:
        monitor-exit(r0);	 Catch:{ all -> 0x004d }
        return;
    L_0x0029:
        if (r7 == 0) goto L_0x002f;
    L_0x002b:
        r1.add(r6);	 Catch:{ all -> 0x004d }
        goto L_0x004b;
    L_0x002f:
        r4 = r1.iterator();	 Catch:{ all -> 0x004d }
        r2 = r4;
    L_0x0034:
        r4 = r2.hasNext();	 Catch:{ all -> 0x004d }
        if (r4 == 0) goto L_0x004b;
    L_0x003a:
        r4 = r2.next();	 Catch:{ all -> 0x004d }
        r4 = (java.lang.String) r4;	 Catch:{ all -> 0x004d }
        r3 = r4;
        r4 = r3.equals(r6);	 Catch:{ all -> 0x004d }
        if (r4 == 0) goto L_0x0034;
    L_0x0047:
        r2.remove();	 Catch:{ all -> 0x004d }
        goto L_0x0034;
    L_0x004b:
        monitor-exit(r0);	 Catch:{ all -> 0x004d }
        return;
    L_0x004d:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x004d }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.whetstone.client.WhetstoneClientManager.updatePackageLockedStatus(java.lang.String, boolean, int):void");
    }

    public static WhetstoneSystemSetting getWhetstoneSystemSetting() {
        return mSetting;
    }

    public static boolean isStartServiceAllowedLocked(Intent service, int callingUid, String callingPackage, int userId) {
        return true;
    }
}
