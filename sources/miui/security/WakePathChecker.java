package miui.security;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.content.pm.ResolveInfo;
import android.miui.AppOpsUtils;
import android.os.Binder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.MiuiSettings.AntiSpam;
import android.service.media.MediaBrowserService;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import com.android.internal.app.IAppOpsService;
import com.android.internal.app.IAppOpsService.Stub;
import com.android.internal.app.IWakePathCallback;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import miui.os.Build;
import miui.securityspace.XSpaceUserHandle;

public class WakePathChecker {
    private static final int CALL_LIST_LOG_MAP_MAX_SIZE = 200;
    private static final int GET_CONTENT_PROVIDER_RULE_INFOS_LIST_INDEX = 2;
    private static final int RULE_INFOS_LIST_COUNT = 4;
    private static final int SEND_BROADCAST_RULE_INFOS_LIST_INDEX = 1;
    private static final int START_ACTIVITY_RULE_INFOS_LIST_INDEX = 0;
    private static final int START_SERVICE_RULE_INFOS_LIST_INDEX = 3;
    private static final String TAG = WakePathChecker.class.getSimpleName();
    public static final int WAKEPATH_CONFIRM_DIALOG_WHITELIST_TYPE_CALLEE = 1;
    public static final int WAKEPATH_CONFIRM_DIALOG_WHITELIST_TYPE_CALLER = 2;
    private static WakePathChecker sInstance;
    private IAppOpsService mAppOpsService;
    private List<String> mBindServiceCheckActions = new ArrayList();
    Object mCallListLogLocker = new Object();
    Map<Integer, WakePathRuleInfo> mCallListLogMap;
    private IWakePathCallback mCallback;
    List<String> mLauncherPackageNames = new ArrayList();
    boolean mTrackCallListLogEnabled = (Build.IS_STABLE_VERSION ^ 1);
    private boolean mUpdatePkgsEnable = false;
    private List<String> mUpdatePkgsList = new ArrayList();
    private Map<Integer, WakePathRuleData> mUserWakePathRuleDataMap = new HashMap();
    private List<String> mWakePathCallerWhiteList = new ArrayList();
    private List<String> mWakePathConfirmDialogCallerWhitelist = new ArrayList();
    private List<String> mWakePathConfirmDialogWhitelist = new ArrayList();

    private class WakePathRuleData {
        Map<String, List<String>> mAllowedStartActivityRulesMap;
        List<List<WakePathRuleInfo>> mWakePathRuleInfosList = new ArrayList(4);
        List<String> mWakePathWhiteList;

        WakePathRuleData() {
            for (int i = 0; i < 4; i++) {
                this.mWakePathRuleInfosList.add(null);
            }
        }
    }

    private WakePathChecker() {
        if (this.mTrackCallListLogEnabled) {
            this.mCallListLogMap = new HashMap(200);
        }
        this.mWakePathConfirmDialogWhitelist.add("com.mfashiongallery.express");
        this.mWakePathConfirmDialogWhitelist.add("com.mi.dlabs.vr.thor");
        this.mWakePathCallerWhiteList.add("com.miui.home");
        this.mWakePathCallerWhiteList.add(AntiSpam.ANTISPAM_PKG);
        this.mBindServiceCheckActions.add("miui.action.CAMERA_EMPTY_SERVICE");
        this.mBindServiceCheckActions.add(MediaBrowserService.SERVICE_INTERFACE);
        this.mAppOpsService = Stub.asInterface(ServiceManager.getService(Context.APP_OPS_SERVICE));
    }

    public static synchronized WakePathChecker getInstance() {
        WakePathChecker wakePathChecker;
        synchronized (WakePathChecker.class) {
            if (sInstance == null) {
                sInstance = new WakePathChecker();
            }
            wakePathChecker = sInstance;
        }
        return wakePathChecker;
    }

    public void init(Context context) {
        updateLauncherPackageNames(context);
    }

    private int wakeTypeToRuleInfosListIndex(int wakeType) {
        if (wakeType == 8) {
            return 3;
        }
        if (wakeType == 1) {
            return 0;
        }
        if (wakeType == 4) {
            return 2;
        }
        if (wakeType == 2) {
            return 1;
        }
        return -1;
    }

    private WakePathRuleData getWakePathRuleDataByUser(int userId) {
        WakePathRuleData wakePathRuleData;
        if (XSpaceUserHandle.isXSpaceUserId(userId) || userId == -1) {
            userId = 0;
        }
        synchronized (this.mUserWakePathRuleDataMap) {
            wakePathRuleData = (WakePathRuleData) this.mUserWakePathRuleDataMap.get(Integer.valueOf(userId));
            if (wakePathRuleData == null) {
                wakePathRuleData = new WakePathRuleData();
                this.mUserWakePathRuleDataMap.put(Integer.valueOf(userId), wakePathRuleData);
            }
        }
        return wakePathRuleData;
    }

    public void pushWakePathRuleInfos(int wakeType, List<WakePathRuleInfo> infos, int userId) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MIUILOG-WAKEPATH pushWakePathRuleInfos: wakeType=");
        stringBuilder.append(wakeType);
        stringBuilder.append(" userId=");
        stringBuilder.append(userId);
        stringBuilder.append(" size=");
        stringBuilder.append(infos == null ? 0 : infos.size());
        Slog.i(str, stringBuilder.toString());
        WakePathRuleData wakePathRuleData = getWakePathRuleDataByUser(userId);
        synchronized (wakePathRuleData) {
            int i;
            if (wakeType == 17) {
                wakePathRuleData.mAllowedStartActivityRulesMap = new HashMap();
                if (infos != null) {
                    for (i = 0; i < infos.size(); i++) {
                        WakePathRuleInfo info = (WakePathRuleInfo) infos.get(i);
                        List<String> pkgNames = (List) wakePathRuleData.mAllowedStartActivityRulesMap.get(info.getCalleeExpress());
                        if (pkgNames == null) {
                            pkgNames = new ArrayList();
                            wakePathRuleData.mAllowedStartActivityRulesMap.put(info.getCalleeExpress(), pkgNames);
                        }
                        pkgNames.add(info.getCallerExpress());
                    }
                }
            } else {
                i = wakeTypeToRuleInfosListIndex(wakeType);
                if (i >= 0 && i < 4) {
                    wakePathRuleData.mWakePathRuleInfosList.set(i, infos);
                }
            }
        }
    }

    public void pushWakePathWhiteList(List<String> wakePathWhiteList, int userId) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MIUILOG-WAKEPATH pushWakePathWhiteList: userId=");
        stringBuilder.append(userId);
        stringBuilder.append(" size=");
        stringBuilder.append(wakePathWhiteList == null ? 0 : wakePathWhiteList.size());
        Slog.i(str, stringBuilder.toString());
        WakePathRuleData wakePathRuleData = getWakePathRuleDataByUser(userId);
        synchronized (wakePathRuleData) {
            wakePathRuleData.mWakePathWhiteList = wakePathWhiteList;
        }
    }

    public void pushWakePathConfirmDialogWhiteList(int type, List<String> whiteList) {
        if (whiteList != null && whiteList.size() != 0) {
            if (type == 1) {
                synchronized (this.mWakePathConfirmDialogWhitelist) {
                    this.mWakePathConfirmDialogWhitelist.clear();
                    this.mWakePathConfirmDialogWhitelist.addAll(whiteList);
                }
            } else if (type == 2) {
                synchronized (this.mWakePathConfirmDialogCallerWhitelist) {
                    this.mWakePathConfirmDialogCallerWhitelist.clear();
                    this.mWakePathConfirmDialogCallerWhitelist.addAll(whiteList);
                }
            }
        }
    }

    public void removeWakePathData(int userId) {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MIUILOG-WAKEPATH removeWakePathData: userId=");
        stringBuilder.append(userId);
        Slog.i(str, stringBuilder.toString());
        if (userId != 0 && !XSpaceUserHandle.isXSpaceUserId(userId)) {
            synchronized (this.mUserWakePathRuleDataMap) {
                WakePathRuleData wakePathRuleData = (WakePathRuleData) this.mUserWakePathRuleDataMap.get(Integer.valueOf(userId));
                if (wakePathRuleData != null) {
                    this.mUserWakePathRuleDataMap.remove(wakePathRuleData);
                }
            }
        }
    }

    public void setTrackWakePathCallListLogEnabled(boolean enabled) {
        if (!enabled) {
            this.mTrackCallListLogEnabled = enabled;
            if (!this.mTrackCallListLogEnabled) {
                synchronized (this.mCallListLogLocker) {
                    if (this.mCallListLogMap != null) {
                        this.mCallListLogMap.clear();
                        this.mCallListLogMap = null;
                    }
                }
            }
        }
    }

    public ParceledListSlice getWakePathCallListLog() {
        List<WakePathRuleInfo> ret = null;
        if (this.mTrackCallListLogEnabled) {
            synchronized (this.mCallListLogLocker) {
                if (this.mCallListLogMap != null) {
                    ret = new ArrayList(this.mCallListLogMap.values());
                    this.mCallListLogMap.clear();
                }
            }
        }
        if (ret == null) {
            return null;
        }
        return new ParceledListSlice(ret);
    }

    public boolean checkBroadcastWakePath(Intent intent, String caller, ApplicationInfo callerAppInfo, ResolveInfo info, int userId) {
        ResolveInfo resolveInfo = info;
        String str;
        if (intent == null) {
            str = caller;
        } else if (TextUtils.isEmpty(caller)) {
            str = caller;
        } else {
            String callee = "";
            String action = "";
            String className = "";
            int calleeUid = -1;
            action = intent.getAction();
            if (intent.getComponent() != null) {
                className = intent.getComponent().getClassName();
                callee = intent.getComponent().getPackageName();
            }
            if (!(resolveInfo == null || resolveInfo.activityInfo == null)) {
                if (resolveInfo.activityInfo.applicationInfo != null) {
                    callee = resolveInfo.activityInfo.applicationInfo.packageName;
                    calleeUid = resolveInfo.activityInfo.applicationInfo.uid;
                }
                className = resolveInfo.activityInfo.name;
            }
            return TextUtils.equals(callee, caller) ? true : 1 ^ matchWakePathRule(action, className, caller, callee, calleeUid, 2, userId);
        }
        return true;
    }

    public boolean matchWakePathRule(String action, String className, String caller, String callee, int calleeUid, int wakeType, int userId) {
        long callingIdentity;
        Throwable th;
        String str = action;
        String str2 = className;
        String str3 = caller;
        String str4 = callee;
        int i = calleeUid;
        int i2 = wakeType;
        int i3 = userId;
        String str5;
        if (AppRunningControlManager.matchRule(str4, i2)) {
            str5 = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AppRunningControl, Reject userId= ");
            stringBuilder.append(i3);
            stringBuilder.append(" caller= ");
            stringBuilder.append(str3);
            stringBuilder.append(" callee= ");
            stringBuilder.append(str4);
            stringBuilder.append(" classname=");
            stringBuilder.append(str2);
            stringBuilder.append(" action=");
            stringBuilder.append(str);
            stringBuilder.append(" wakeType=");
            stringBuilder.append(i2);
            Slog.w(str5, stringBuilder.toString());
            return true;
        }
        if (this.mTrackCallListLogEnabled) {
            trackCallListInfo(action, className, caller, callee, wakeType);
        }
        WakePathRuleData wakePathRuleData = getWakePathRuleDataByUser(i3);
        synchronized (wakePathRuleData) {
            WakePathRuleData wakePathRuleData2;
            try {
                if (this.mWakePathCallerWhiteList.contains(str3)) {
                    wakePathRuleData2 = wakePathRuleData;
                } else {
                    if (wakePathRuleData.mWakePathWhiteList != null) {
                        try {
                            if (wakePathRuleData.mWakePathWhiteList.size() > 0 && wakePathRuleData.mWakePathWhiteList.contains(str4)) {
                                wakePathRuleData2 = wakePathRuleData;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            wakePathRuleData2 = wakePathRuleData;
                            throw th;
                        }
                    }
                    if (i2 == 8 && i > 0 && str4 != null && this.mBindServiceCheckActions.contains(str) && !AppOpsUtils.isXOptMode() && this.mAppOpsService != null) {
                        callingIdentity = Binder.clearCallingIdentity();
                        try {
                            if (this.mAppOpsService.checkOperation(10008, i, str4) != 0) {
                                String str6 = TAG;
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("MIUILOG-AutoStart, BindService Reject userId= ");
                                stringBuilder2.append(i3);
                                stringBuilder2.append(" caller= ");
                                stringBuilder2.append(str3);
                                stringBuilder2.append(" callee= ");
                                stringBuilder2.append(str4);
                                stringBuilder2.append(" classname=");
                                stringBuilder2.append(str2);
                                stringBuilder2.append(" action=");
                                stringBuilder2.append(str);
                                stringBuilder2.append(" wakeType=");
                                stringBuilder2.append(i2);
                                Slog.w(str6, stringBuilder2.toString());
                                Binder.restoreCallingIdentity(callingIdentity);
                            } else {
                                Binder.restoreCallingIdentity(callingIdentity);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "checkOperation", e);
                            Binder.restoreCallingIdentity(callingIdentity);
                        }
                    }
                    int index = wakeTypeToRuleInfosListIndex(i2);
                    int i4;
                    if (index < 0) {
                        wakePathRuleData2 = wakePathRuleData;
                    } else if (index >= 4) {
                        i4 = index;
                        wakePathRuleData2 = wakePathRuleData;
                    } else {
                        List<WakePathRuleInfo> wakePathRuleInfos = (List) wakePathRuleData.mWakePathRuleInfosList.get(index);
                        if (wakePathRuleInfos == null) {
                            i4 = index;
                            wakePathRuleData2 = wakePathRuleData;
                        } else if (wakePathRuleInfos.size() == 0) {
                            i4 = index;
                            wakePathRuleData2 = wakePathRuleData;
                        } else {
                            int i5;
                            int size;
                            int size2 = wakePathRuleInfos.size();
                            int i6 = 0;
                            while (i6 < size2) {
                                i5 = i6;
                                size = size2;
                                i4 = index;
                                wakePathRuleData2 = wakePathRuleData;
                                try {
                                    if (((WakePathRuleInfo) wakePathRuleInfos.get(i6)).equals(action, className, caller, callee, wakeType)) {
                                        if (this.mCallback != null) {
                                            this.mCallback.onRejectCall(str3, str4, i2, i3);
                                        }
                                        str5 = TAG;
                                        StringBuilder stringBuilder3 = new StringBuilder();
                                        stringBuilder3.append("MIUILOG-WAKEPATH: call was rejected by wakepath. userId= ");
                                        stringBuilder3.append(i3);
                                        stringBuilder3.append(" caller= ");
                                        stringBuilder3.append(str3);
                                        stringBuilder3.append(" callee= ");
                                        stringBuilder3.append(str4);
                                        stringBuilder3.append(" classname=");
                                        stringBuilder3.append(str2);
                                        stringBuilder3.append(" action=");
                                        stringBuilder3.append(str);
                                        stringBuilder3.append(" wakeType=");
                                        stringBuilder3.append(i2);
                                        Slog.w(str5, stringBuilder3.toString());
                                        return true;
                                    }
                                    i6 = i5 + 1;
                                    size2 = size;
                                    index = i4;
                                    wakePathRuleData = wakePathRuleData2;
                                } catch (RemoteException e2) {
                                    e2.printStackTrace();
                                } catch (Throwable th3) {
                                    th = th3;
                                    throw th;
                                }
                            }
                            i5 = i6;
                            size = size2;
                            i4 = index;
                            wakePathRuleData2 = wakePathRuleData;
                            if (this.mCallback != null) {
                                try {
                                    this.mCallback.onAllowCall(str3, str4, i2, i3);
                                } catch (RemoteException e22) {
                                    e22.printStackTrace();
                                }
                            }
                            return false;
                        }
                        return false;
                    }
                    Slog.e(TAG, "MIUILOG-WAKEPATH invalid parameter");
                    return false;
                }
                if (this.mCallback != null) {
                    try {
                        this.mCallback.onAllowCall(str3, str4, i2, i3);
                    } catch (RemoteException e222) {
                        e222.printStackTrace();
                    }
                }
                return false;
            } catch (Throwable th4) {
                th = th4;
                wakePathRuleData2 = wakePathRuleData;
                throw th;
            }
        }
        return true;
    }

    /* JADX WARNING: Missing block: B:45:0x00c0, code skipped:
            return;
     */
    private void trackCallListInfo(java.lang.String r18, java.lang.String r19, java.lang.String r20, java.lang.String r21, int r22) {
        /*
        r17 = this;
        r1 = r17;
        r9 = r20;
        r10 = r21;
        r11 = r1.mCallListLogLocker;
        monitor-enter(r11);
        r0 = android.text.TextUtils.isEmpty(r20);	 Catch:{ all -> 0x00e7 }
        if (r0 != 0) goto L_0x00c1;
    L_0x000f:
        r0 = android.text.TextUtils.isEmpty(r21);	 Catch:{ all -> 0x00e7 }
        if (r0 == 0) goto L_0x001d;
    L_0x0015:
        r13 = r18;
        r14 = r19;
        r15 = r22;
        goto L_0x00c7;
    L_0x001d:
        r0 = r1.mCallListLogMap;	 Catch:{ all -> 0x00e7 }
        if (r0 == 0) goto L_0x00b9;
    L_0x0021:
        r0 = r1.mCallListLogMap;	 Catch:{ all -> 0x00e7 }
        r0 = r0.size();	 Catch:{ all -> 0x00e7 }
        r2 = 200; // 0xc8 float:2.8E-43 double:9.9E-322;
        if (r0 < r2) goto L_0x002d;
    L_0x002b:
        monitor-exit(r11);	 Catch:{ all -> 0x00e7 }
        return;
    L_0x002d:
        r0 = miui.security.WakePathRuleInfo.getHashCode(r18, r19, r20, r21);	 Catch:{ all -> 0x00e7 }
        r12 = r0;
        if (r12 != 0) goto L_0x007d;
    L_0x0034:
        r0 = TAG;	 Catch:{ all -> 0x00e7 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00e7 }
        r2.<init>();	 Catch:{ all -> 0x00e7 }
        r3 = "MIUILOG-WAKEPATH trackCallListInfo: hashCode == 0,(action =";
        r2.append(r3);	 Catch:{ all -> 0x00e7 }
        r13 = r18;
        r2.append(r13);	 Catch:{ all -> 0x007a }
        r3 = " className=";
        r2.append(r3);	 Catch:{ all -> 0x007a }
        r14 = r19;
        r2.append(r14);	 Catch:{ all -> 0x0077 }
        r3 = " caller=";
        r2.append(r3);	 Catch:{ all -> 0x0077 }
        r2.append(r9);	 Catch:{ all -> 0x0077 }
        r3 = " callee=";
        r2.append(r3);	 Catch:{ all -> 0x0077 }
        r2.append(r10);	 Catch:{ all -> 0x0077 }
        r3 = " wakeType=";
        r2.append(r3);	 Catch:{ all -> 0x0077 }
        r15 = r22;
        r2.append(r15);	 Catch:{ all -> 0x00f0 }
        r3 = ")";
        r2.append(r3);	 Catch:{ all -> 0x00f0 }
        r2 = r2.toString();	 Catch:{ all -> 0x00f0 }
        android.util.Slog.e(r0, r2);	 Catch:{ all -> 0x00f0 }
        monitor-exit(r11);	 Catch:{ all -> 0x00f0 }
        return;
    L_0x0077:
        r0 = move-exception;
        goto L_0x00ec;
    L_0x007a:
        r0 = move-exception;
        goto L_0x00ea;
    L_0x007d:
        r13 = r18;
        r14 = r19;
        r15 = r22;
        r0 = r1.mCallListLogMap;	 Catch:{ all -> 0x00f0 }
        r2 = java.lang.Integer.valueOf(r12);	 Catch:{ all -> 0x00f0 }
        r0 = r0.get(r2);	 Catch:{ all -> 0x00f0 }
        r0 = (miui.security.WakePathRuleInfo) r0;	 Catch:{ all -> 0x00f0 }
        r16 = r0;
        if (r16 != 0) goto L_0x00bf;
    L_0x0093:
        r0 = new miui.security.WakePathRuleInfo;	 Catch:{ Exception -> 0x00a7 }
        r8 = 0;
        r2 = r0;
        r3 = r18;
        r4 = r19;
        r5 = r20;
        r6 = r21;
        r7 = r22;
        r2.<init>(r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x00a7 }
        r16 = r0;
        goto L_0x00ad;
    L_0x00a7:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x00f0 }
        r0 = r16;
    L_0x00ad:
        if (r0 == 0) goto L_0x00bf;
    L_0x00af:
        r2 = r1.mCallListLogMap;	 Catch:{ all -> 0x00f0 }
        r3 = java.lang.Integer.valueOf(r12);	 Catch:{ all -> 0x00f0 }
        r2.put(r3, r0);	 Catch:{ all -> 0x00f0 }
        goto L_0x00bf;
    L_0x00b9:
        r13 = r18;
        r14 = r19;
        r15 = r22;
    L_0x00bf:
        monitor-exit(r11);	 Catch:{ all -> 0x00f0 }
        return;
    L_0x00c1:
        r13 = r18;
        r14 = r19;
        r15 = r22;
    L_0x00c7:
        r0 = TAG;	 Catch:{ all -> 0x00f0 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00f0 }
        r2.<init>();	 Catch:{ all -> 0x00f0 }
        r3 = "MIUILOG-WAKEPATH trackCallListInfo: invalid parameter caller=";
        r2.append(r3);	 Catch:{ all -> 0x00f0 }
        r2.append(r9);	 Catch:{ all -> 0x00f0 }
        r3 = " callee=";
        r2.append(r3);	 Catch:{ all -> 0x00f0 }
        r2.append(r10);	 Catch:{ all -> 0x00f0 }
        r2 = r2.toString();	 Catch:{ all -> 0x00f0 }
        android.util.Slog.w(r0, r2);	 Catch:{ all -> 0x00f0 }
        monitor-exit(r11);	 Catch:{ all -> 0x00f0 }
        return;
    L_0x00e7:
        r0 = move-exception;
        r13 = r18;
    L_0x00ea:
        r14 = r19;
    L_0x00ec:
        r15 = r22;
    L_0x00ee:
        monitor-exit(r11);	 Catch:{ all -> 0x00f0 }
        throw r0;
    L_0x00f0:
        r0 = move-exception;
        goto L_0x00ee;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.security.WakePathChecker.trackCallListInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, int):void");
    }

    public void registerWakePathCallback(IWakePathCallback callback) {
        this.mCallback = callback;
    }

    /* JADX WARNING: Missing block: B:12:0x001c, code skipped:
            r2 = r4.mLauncherPackageNames;
     */
    /* JADX WARNING: Missing block: B:13:0x001e, code skipped:
            monitor-enter(r2);
     */
    /* JADX WARNING: Missing block: B:16:0x0025, code skipped:
            if (r4.mLauncherPackageNames.contains(r5) == false) goto L_0x0029;
     */
    /* JADX WARNING: Missing block: B:17:0x0027, code skipped:
            monitor-exit(r2);
     */
    /* JADX WARNING: Missing block: B:18:0x0028, code skipped:
            return true;
     */
    /* JADX WARNING: Missing block: B:19:0x0029, code skipped:
            monitor-exit(r2);
     */
    /* JADX WARNING: Missing block: B:20:0x002a, code skipped:
            r0 = r4.mWakePathConfirmDialogCallerWhitelist;
     */
    /* JADX WARNING: Missing block: B:21:0x002c, code skipped:
            monitor-enter(r0);
     */
    /* JADX WARNING: Missing block: B:24:0x0033, code skipped:
            if (r4.mWakePathConfirmDialogCallerWhitelist.contains(r5) == false) goto L_0x0037;
     */
    /* JADX WARNING: Missing block: B:25:0x0035, code skipped:
            monitor-exit(r0);
     */
    /* JADX WARNING: Missing block: B:26:0x0036, code skipped:
            return true;
     */
    /* JADX WARNING: Missing block: B:27:0x0037, code skipped:
            monitor-exit(r0);
     */
    /* JADX WARNING: Missing block: B:28:0x0038, code skipped:
            r2 = getWakePathRuleDataByUser(r7);
     */
    /* JADX WARNING: Missing block: B:29:0x003c, code skipped:
            monitor-enter(r2);
     */
    /* JADX WARNING: Missing block: B:32:0x003f, code skipped:
            if (r2.mWakePathWhiteList == null) goto L_0x0053;
     */
    /* JADX WARNING: Missing block: B:34:0x0047, code skipped:
            if (r2.mWakePathWhiteList.size() <= 0) goto L_0x0053;
     */
    /* JADX WARNING: Missing block: B:36:0x004f, code skipped:
            if (r2.mWakePathWhiteList.contains(r6) == false) goto L_0x0053;
     */
    /* JADX WARNING: Missing block: B:37:0x0051, code skipped:
            monitor-exit(r2);
     */
    /* JADX WARNING: Missing block: B:38:0x0052, code skipped:
            return true;
     */
    /* JADX WARNING: Missing block: B:40:0x0055, code skipped:
            if (r2.mAllowedStartActivityRulesMap == null) goto L_0x0069;
     */
    /* JADX WARNING: Missing block: B:41:0x0057, code skipped:
            r0 = (java.util.List) r2.mAllowedStartActivityRulesMap.get(r6);
     */
    /* JADX WARNING: Missing block: B:42:0x005f, code skipped:
            if (r0 == null) goto L_0x0069;
     */
    /* JADX WARNING: Missing block: B:44:0x0065, code skipped:
            if (r0.contains(r5) == false) goto L_0x0069;
     */
    /* JADX WARNING: Missing block: B:45:0x0067, code skipped:
            monitor-exit(r2);
     */
    /* JADX WARNING: Missing block: B:46:0x0068, code skipped:
            return true;
     */
    /* JADX WARNING: Missing block: B:47:0x0069, code skipped:
            monitor-exit(r2);
     */
    /* JADX WARNING: Missing block: B:49:0x006b, code skipped:
            return false;
     */
    public boolean checkAllowStartActivity(java.lang.String r5, java.lang.String r6, int r7) {
        /*
        r4 = this;
        r0 = android.text.TextUtils.isEmpty(r5);
        r1 = 1;
        if (r0 != 0) goto L_0x0078;
    L_0x0007:
        r0 = android.text.TextUtils.isEmpty(r6);
        if (r0 == 0) goto L_0x000e;
    L_0x000d:
        goto L_0x0078;
    L_0x000e:
        r0 = r4.mWakePathConfirmDialogWhitelist;
        monitor-enter(r0);
        r2 = r4.mWakePathConfirmDialogWhitelist;	 Catch:{ all -> 0x0075 }
        r2 = r2.contains(r6);	 Catch:{ all -> 0x0075 }
        if (r2 == 0) goto L_0x001b;
    L_0x0019:
        monitor-exit(r0);	 Catch:{ all -> 0x0075 }
        return r1;
    L_0x001b:
        monitor-exit(r0);	 Catch:{ all -> 0x0075 }
        r2 = r4.mLauncherPackageNames;
        monitor-enter(r2);
        r0 = r4.mLauncherPackageNames;	 Catch:{ all -> 0x0072 }
        r0 = r0.contains(r5);	 Catch:{ all -> 0x0072 }
        if (r0 == 0) goto L_0x0029;
    L_0x0027:
        monitor-exit(r2);	 Catch:{ all -> 0x0072 }
        return r1;
    L_0x0029:
        monitor-exit(r2);	 Catch:{ all -> 0x0072 }
        r0 = r4.mWakePathConfirmDialogCallerWhitelist;
        monitor-enter(r0);
        r2 = r4.mWakePathConfirmDialogCallerWhitelist;	 Catch:{ all -> 0x006f }
        r2 = r2.contains(r5);	 Catch:{ all -> 0x006f }
        if (r2 == 0) goto L_0x0037;
    L_0x0035:
        monitor-exit(r0);	 Catch:{ all -> 0x006f }
        return r1;
    L_0x0037:
        monitor-exit(r0);	 Catch:{ all -> 0x006f }
        r2 = r4.getWakePathRuleDataByUser(r7);
        monitor-enter(r2);
        r0 = r2.mWakePathWhiteList;	 Catch:{ all -> 0x006c }
        if (r0 == 0) goto L_0x0053;
    L_0x0041:
        r0 = r2.mWakePathWhiteList;	 Catch:{ all -> 0x006c }
        r0 = r0.size();	 Catch:{ all -> 0x006c }
        if (r0 <= 0) goto L_0x0053;
    L_0x0049:
        r0 = r2.mWakePathWhiteList;	 Catch:{ all -> 0x006c }
        r0 = r0.contains(r6);	 Catch:{ all -> 0x006c }
        if (r0 == 0) goto L_0x0053;
    L_0x0051:
        monitor-exit(r2);	 Catch:{ all -> 0x006c }
        return r1;
    L_0x0053:
        r0 = r2.mAllowedStartActivityRulesMap;	 Catch:{ all -> 0x006c }
        if (r0 == 0) goto L_0x0069;
    L_0x0057:
        r0 = r2.mAllowedStartActivityRulesMap;	 Catch:{ all -> 0x006c }
        r0 = r0.get(r6);	 Catch:{ all -> 0x006c }
        r0 = (java.util.List) r0;	 Catch:{ all -> 0x006c }
        if (r0 == 0) goto L_0x0069;
    L_0x0061:
        r3 = r0.contains(r5);	 Catch:{ all -> 0x006c }
        if (r3 == 0) goto L_0x0069;
    L_0x0067:
        monitor-exit(r2);	 Catch:{ all -> 0x006c }
        return r1;
    L_0x0069:
        monitor-exit(r2);	 Catch:{ all -> 0x006c }
        r0 = 0;
        return r0;
    L_0x006c:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x006c }
        throw r0;
    L_0x006f:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x006f }
        throw r1;
    L_0x0072:
        r0 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0072 }
        throw r0;
    L_0x0075:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0075 }
        throw r1;
    L_0x0078:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.security.WakePathChecker.checkAllowStartActivity(java.lang.String, java.lang.String, int):boolean");
    }

    private void updateLauncherPackageNames(Context context) {
        List<String> packageNames = new ArrayList();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        try {
            for (ResolveInfo ri : context.getPackageManager().queryIntentActivities(intent, null)) {
                packageNames.add(ri.activityInfo.packageName);
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("updateLauncherPackageNames =");
                stringBuilder.append(ri.activityInfo.packageName);
                Slog.i(str, stringBuilder.toString());
            }
        } catch (Exception e) {
            Slog.e(TAG, "updateLauncherPackageNames", e);
        }
        synchronized (this.mLauncherPackageNames) {
            this.mLauncherPackageNames.clear();
            if (packageNames.size() > 0) {
                for (String name : packageNames) {
                    this.mLauncherPackageNames.add(name);
                }
            }
        }
    }

    public void onPackageAdded(final Context context) {
        new Thread() {
            public void run() {
                WakePathChecker.this.updateLauncherPackageNames(context);
            }
        }.start();
    }

    /* JADX WARNING: Missing block: B:19:?, code skipped:
            r4.mCallback.onUpdateCall(0, r5, r6.packageName);
     */
    /* JADX WARNING: Missing block: B:20:0x002c, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:21:0x002d, code skipped:
            r1 = TAG;
            r2 = new java.lang.StringBuilder();
            r2.append("updatePath error:");
            r2.append(r0.toString());
            android.util.Log.d(r1, r2.toString());
     */
    /* JADX WARNING: Missing block: B:27:0x004d, code skipped:
            return;
     */
    public void updatePath(android.content.Intent r5, android.content.pm.ComponentInfo r6, int r7, int r8) {
        /*
        r4 = this;
        r0 = 1;
        if (r7 != r0) goto L_0x004d;
    L_0x0003:
        if (r8 != 0) goto L_0x004d;
    L_0x0005:
        r0 = miui.os.Build.IS_INTERNATIONAL_BUILD;
        if (r0 == 0) goto L_0x000a;
    L_0x0009:
        goto L_0x004d;
    L_0x000a:
        r0 = r4.mCallback;
        if (r0 == 0) goto L_0x004c;
    L_0x000e:
        r0 = r4.mUpdatePkgsEnable;
        if (r0 != 0) goto L_0x0013;
    L_0x0012:
        return;
    L_0x0013:
        r0 = r4.mUpdatePkgsList;
        monitor-enter(r0);
        r1 = r4.mUpdatePkgsList;	 Catch:{ all -> 0x0049 }
        r2 = r6.packageName;	 Catch:{ all -> 0x0049 }
        r1 = r1.contains(r2);	 Catch:{ all -> 0x0049 }
        if (r1 != 0) goto L_0x0022;
    L_0x0020:
        monitor-exit(r0);	 Catch:{ all -> 0x0049 }
        return;
    L_0x0022:
        monitor-exit(r0);	 Catch:{ all -> 0x0049 }
        r0 = r4.mCallback;	 Catch:{ Exception -> 0x002c }
        r1 = 0;
        r2 = r6.packageName;	 Catch:{ Exception -> 0x002c }
        r0.onUpdateCall(r1, r5, r2);	 Catch:{ Exception -> 0x002c }
        goto L_0x004c;
    L_0x002c:
        r0 = move-exception;
        r1 = TAG;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "updatePath error:";
        r2.append(r3);
        r3 = r0.toString();
        r2.append(r3);
        r2 = r2.toString();
        android.util.Log.d(r1, r2);
        goto L_0x004c;
    L_0x0049:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0049 }
        throw r1;
    L_0x004c:
        return;
    L_0x004d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.security.WakePathChecker.updatePath(android.content.Intent, android.content.pm.ComponentInfo, int, int):void");
    }

    public void pushUpdatePkgsData(List<String> updatePkgsList, boolean enable) {
        this.mUpdatePkgsEnable = enable;
        if (updatePkgsList != null && updatePkgsList.size() != 0) {
            synchronized (this.mUpdatePkgsList) {
                this.mUpdatePkgsList.clear();
                this.mUpdatePkgsList.addAll(updatePkgsList);
            }
        }
    }

    public void dump(PrintWriter pw) {
        if (pw != null) {
            pw.println("========================================WAKEPATH DUMP BEGIN========================================");
            try {
                synchronized (this.mUserWakePathRuleDataMap) {
                    if (this.mUserWakePathRuleDataMap.size() > 0) {
                        for (Integer userId : this.mUserWakePathRuleDataMap.keySet()) {
                            WakePathRuleData ruleData = (WakePathRuleData) this.mUserWakePathRuleDataMap.get(userId);
                            pw.println("----------------------------------------");
                            if (ruleData != null) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("userId=");
                                stringBuilder.append(userId);
                                pw.println(stringBuilder.toString());
                                if (ruleData.mWakePathWhiteList != null) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append("whitelist=");
                                    stringBuilder.append(ruleData.mWakePathWhiteList.toString());
                                    pw.println(stringBuilder.toString());
                                } else {
                                    pw.println("whitelist is null.");
                                }
                                for (int i = 0; i < 4; i++) {
                                    StringBuilder stringBuilder2;
                                    if (ruleData.mWakePathRuleInfosList.get(i) == null) {
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("rule info index=");
                                        stringBuilder2.append(i);
                                        stringBuilder2.append(" size=0");
                                        pw.println(stringBuilder2.toString());
                                    } else {
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("rule info index=");
                                        stringBuilder2.append(i);
                                        stringBuilder2.append(" size=");
                                        stringBuilder2.append(((List) ruleData.mWakePathRuleInfosList.get(i)).size());
                                        pw.println(stringBuilder2.toString());
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                Slog.e(TAG, "dump", e);
            }
            pw.println("========================================WAKEPATH DUMP END========================================");
        }
    }
}
