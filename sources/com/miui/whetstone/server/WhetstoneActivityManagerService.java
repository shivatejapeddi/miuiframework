package com.miui.whetstone.server;

import android.Manifest.permission;
import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.statistics.PerfEventConstants;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.TimedRemoteCaller;
import com.android.internal.os.BackgroundThread;
import com.miui.whetstone.IWhetstoneClient;
import com.miui.whetstone.PowerKeeperPolicy;
import com.miui.whetstone.client.WhetstoneClientManager;
import com.miui.whetstone.process.WtServiceControlEntry;
import com.miui.whetstone.server.IWhetstoneActivityManager.Stub;
import com.miui.whetstone.strategy.WhetstonePackageInfo;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import miui.mqsas.sdk.event.KillProcessEvent;
import miui.securityspace.XSpaceUtils;
import miui.util.ReflectionUtils;

public class WhetstoneActivityManagerService extends Stub {
    public static final String APP_SERVICE_NAME = "miui.whetstone";
    public static final boolean D = Log.isLoggable("whetstone.activity", 3);
    private static final int FROZEN_APP = 1;
    private static final int MSG_SYSTEM_UPDATE_CURRENT_PROCESS_PSS = 3;
    private static final int MSG_USER_CLEAR_DEAD_NATIVE_PROCESS = 2;
    private static final int MSG_USER_REMOVE_PROMOTE_LEVEL = 1;
    private static final int PER_USER_RANGE = 100000;
    public static final int PROMOTE_LEVEL_HIGH = 2;
    public static final int PROMOTE_LEVEL_MIDDLE = 1;
    public static final int PROMOTE_LEVEL_NORMAL = 0;
    public static final String SERVICE = "whetstone.activity";
    private static final String TAG = "whetstone.activity";
    private static WhetstoneActivityManagerService mSelf;
    private Class<?> MiuiNetworkManagementService;
    private Class<?> PowerManagerServiceInjector;
    private Method getPartialWakeLockHoldByUid;
    private IBinder mAM;
    private Context mContext;
    private BroadcastReceiver mDeviceIdleChangeReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String str = "whetstone.activity";
            try {
                if (WhetstoneActivityManagerService.this.mPowerManager == null) {
                    WhetstoneActivityManagerService.this.mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                }
                boolean deviceIdle = ((Boolean) PowerManager.class.getDeclaredMethod("isDeviceIdleMode", new Class[0]).invoke(WhetstoneActivityManagerService.this.mPowerManager, new Object[0])).booleanValue();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("DeviceIdleMode changed to ");
                stringBuilder.append(deviceIdle);
                Slog.v(str, stringBuilder.toString());
                WhetstoneActivityManagerService.this.PowerManagerServiceInjector.getDeclaredMethod("updateAllPartialWakeLockDisableState", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                Log.e(str, Log.getStackTraceString(e));
            }
        }
    };
    private Class<?> mExtraActivityManagerService;
    private Method mGetConnProviderNames;
    private PromoteLevelManagerHandler mHandler;
    private Object mNetService;
    private SparseArray mPidsSelfLocked;
    private PowerKeeperPolicy mPowerKeeperPolicy;
    private PowerManager mPowerManager;
    private Method mRemoveTask;
    private Method mRemoveTaskByIdLocked;
    private Method mScheduleDestroyActivities;
    private ClassLoader mSystemServiceClassLoader;
    private final SparseBooleanArray mUidFrozenState = new SparseBooleanArray();

    private final class PromoteLevelManagerHandler extends Handler {
        public PromoteLevelManagerHandler(Looper looper) {
            super(looper, null, true);
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i != 1 && i == 2) {
                WhetstoneActivityManagerService.this.handleClearDeadAppFromNative();
            }
        }
    }

    private boolean checkCallInterfacePermission() {
        if (Binder.getCallingUid() % 100000 > 10000) {
            return false;
        }
        return true;
    }

    public WhetstoneActivityManagerService(Context context) {
        String str = "WhetstoneActivityManagerService";
        String str2 = "whetstone.activity";
        this.mContext = context;
        this.mSystemServiceClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            this.mExtraActivityManagerService = Class.forName("com.android.server.am.ExtraActivityManagerService", false, this.mSystemServiceClassLoader);
            this.mAM = (IBinder) ReflectionUtils.findMethodExact(ReflectionUtils.findClass("android.os.ServiceManager", null), "getService", String.class).invoke(null, new Object[]{Context.ACTIVITY_SERVICE});
            Field field = ReflectionUtils.findField(ReflectionUtils.findClass("com.android.server.am.ActivityManagerService", this.mSystemServiceClassLoader), "mPidsSelfLocked");
            if (field != null) {
                if (VERSION.SDK_INT >= 29) {
                    Field fieldMap = ReflectionUtils.findField(ReflectionUtils.findClass("com.android.server.am.ActivityManagerService$PidMap", this.mSystemServiceClassLoader), "mPidMap");
                    if (fieldMap != null) {
                        this.mPidsSelfLocked = (SparseArray) fieldMap.get(field.get(this.mAM));
                    } else {
                        throw new RuntimeException("Error: mPidMap not found in AcivityManagerService$PidMap");
                    }
                }
                this.mPidsSelfLocked = (SparseArray) field.get(this.mAM);
                findRemoveTaskMethod();
                try {
                    this.mScheduleDestroyActivities = ReflectionUtils.findMethodExact(this.mExtraActivityManagerService, "scheduleDestroyActivities", Integer.TYPE, Boolean.TYPE, String.class);
                    if (this.mScheduleDestroyActivities != null) {
                        this.PowerManagerServiceInjector = Class.forName("com.android.server.power.PowerManagerServiceInjector", false, this.mSystemServiceClassLoader);
                        this.getPartialWakeLockHoldByUid = ReflectionUtils.findMethodExact(this.PowerManagerServiceInjector, "getPartialWakeLockHoldByUid", Integer.TYPE);
                        try {
                            this.MiuiNetworkManagementService = Class.forName("com.android.server.MiuiNetworkManagementService", false, this.mSystemServiceClassLoader);
                            Method method = ReflectionUtils.findMethodExact(this.MiuiNetworkManagementService, "getInstance", new Class[0]);
                            if (method != null) {
                                this.mNetService = method.invoke(this.MiuiNetworkManagementService, new Object[0]);
                            }
                        } catch (Exception e) {
                            Log.e(str2, Log.getStackTraceString(e));
                        }
                        this.mPowerKeeperPolicy = PowerKeeperPolicy.getInstance();
                        this.mPowerKeeperPolicy.setContext(this.mContext);
                        this.mHandler = new PromoteLevelManagerHandler(BackgroundThread.get().getLooper());
                        try {
                            context.registerReceiver(this.mDeviceIdleChangeReceiver, new IntentFilter((String) PowerManager.class.getDeclaredField("ACTION_DEVICE_IDLE_MODE_CHANGED").get(null)), null, this.mHandler);
                        } catch (Exception e2) {
                            Log.e(str2, Log.getStackTraceString(e2));
                        }
                        mSelf = this;
                        return;
                    }
                    throw new RuntimeException("mScheduleDestroyActivities not found in AcivityManagerService");
                } catch (Exception e22) {
                    Log.e(str2, Log.getStackTraceString(e22));
                }
            } else {
                throw new RuntimeException("Error: mPidsSelfLocked not found in AcivityManagerService");
            }
        } catch (Exception e3) {
            Log.e(str2, Log.getStackTraceString(e3));
            throw new RuntimeException("Error: can not found AcivityManagerService");
        } catch (IllegalArgumentException e4) {
            Log.e(str2, str, e4);
        } catch (NoSuchMethodException e5) {
            Log.e(str2, str, e5);
        } catch (IllegalAccessException e6) {
            Log.e(str2, str, e6);
        } catch (NoSuchFieldException e7) {
            Log.e(str2, str, e7);
        } catch (ClassNotFoundException e8) {
            Log.e(str2, str, e8);
        }
    }

    public PowerKeeperPolicy getPowerKeeperPolicy() {
        return this.mPowerKeeperPolicy;
    }

    private void handleClearDeadAppFromNative() {
        try {
            Method clearMethod = ReflectionUtils.findMethodExact(ReflectionUtils.findClass("com.android.server.am.ActivityManagerService", this.mSystemServiceClassLoader), "clearDeadAppFromNative", new Class[0]);
            if (clearMethod != null) {
                clearMethod.invoke(this.mAM, new Object[0]);
            }
        } catch (Exception e) {
            Log.e("whetstone.activity", "handleClearDeadAppFromNative", e);
        }
    }

    public void clearDeadAppFromNative() {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2), TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS);
    }

    public String getPackageNamebyPid(int pid) {
        String packageName = null;
        synchronized (this.mPidsSelfLocked) {
            Object processRecord = this.mPidsSelfLocked.get(pid);
            if (processRecord != null) {
                try {
                    ApplicationInfo info = (ApplicationInfo) ReflectionUtils.findField(Class.forName("com.android.server.am.ProcessRecord", false, this.mSystemServiceClassLoader), "info").get(processRecord);
                    if (info != null) {
                        packageName = info.packageName;
                    }
                } catch (IllegalArgumentException e) {
                    Log.e("whetstone.activity", "getPackageNamebyPid", e);
                } catch (IllegalAccessException e2) {
                    Log.e("whetstone.activity", "getPackageNamebyPid", e2);
                } catch (NoSuchFieldException e3) {
                    Log.e("whetstone.activity", "getPackageNamebyPid", e3);
                } catch (ClassNotFoundException e4) {
                    Log.e("whetstone.activity", "getPackageNamebyPid", e4);
                }
            }
        }
        return packageName;
    }

    private int getProcessPidByPackageNameLocked(String packageName, int userId) {
        int n = this.mPidsSelfLocked.size();
        try {
            Class<?> clazz = Class.forName("com.android.server.am.ProcessRecord", false, this.mSystemServiceClassLoader);
            int i = n - 1;
            while (true) {
                String str = "pid";
                if (i >= 0) {
                    Object processRecord = this.mPidsSelfLocked.valueAt(i);
                    if (processRecord != null) {
                        Integer uId = (Integer) ReflectionUtils.findField(clazz, XSpaceUtils.EXTRA_XSPACE_ACTUAL_USERID).get(processRecord);
                        if (packageName.equals((String) ReflectionUtils.findField(clazz, PerfEventConstants.FIELD_PROCESS_NAME).get(processRecord)) && uId != null && uId.intValue() == userId) {
                            return ((Integer) ReflectionUtils.findField(clazz, str).get(processRecord)).intValue();
                        }
                    }
                    i--;
                } else {
                    i = 0;
                    int value = -1;
                    for (int i2 = n - 1; i2 >= 0; i2--) {
                        Object processRecord2 = this.mPidsSelfLocked.valueAt(i2);
                        if (processRecord2 != null) {
                            ApplicationInfo info = (ApplicationInfo) ReflectionUtils.findField(clazz, "info").get(processRecord2);
                            if (!(info == null || info.className == null || !info.className.contains(packageName))) {
                                i++;
                                value = ((Integer) ReflectionUtils.findField(clazz, str).get(processRecord2)).intValue();
                            }
                        }
                    }
                    if (i == 1) {
                        return value;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("whetstone.activity", Log.getStackTraceString(e));
        }
        return -1;
    }

    public void updateApplicationByLockedState(String packageName, boolean locked) {
        updateApplicationByLockedStateWithUserId(packageName, locked, 0);
    }

    public void updateApplicationByLockedStateWithUserId(String packageName, boolean locked, int userId) {
        if (!checkCallInterfacePermission() || packageName == null) {
            return;
        }
        if (WhetstoneClientManager.isSystemProtectImportantApp(packageName)) {
            WhetstoneClientManager.updatePackageLockedStatus(packageName, locked, userId);
            return;
        }
        int callingPid = Binder.getCallingPid();
        synchronized (this.mPidsSelfLocked) {
            try {
                int pid = getProcessPidByPackageNameLocked(packageName, userId);
                if (pid == -1) {
                    return;
                }
                Object processRecord = this.mPidsSelfLocked.get(pid);
                if (processRecord != null) {
                    Class<?> clazz = Class.forName("com.android.server.am.ProcessRecord", false, this.mSystemServiceClassLoader);
                    Class<?> clazz2 = Class.forName("com.android.server.am.ProcessList", false, this.mSystemServiceClassLoader);
                    Integer value = (Integer) ReflectionUtils.findField(clazz, "maxAdj").get(processRecord);
                    Integer value2 = (Integer) ReflectionUtils.getStaticObjectField(clazz2, "HEAVY_WEIGHT_APP_ADJ", Integer.TYPE);
                    int resultAdj = value.intValue();
                    if (!locked) {
                        Integer value3 = (Integer) ReflectionUtils.getStaticObjectField(clazz2, "UNKNOWN_ADJ", Integer.TYPE);
                        ReflectionUtils.findField(clazz, "maxAdj").set(processRecord, value3);
                        resultAdj = value3.intValue();
                    } else if (value.intValue() > value2.intValue()) {
                        ReflectionUtils.findField(clazz, "maxAdj").set(processRecord, value2);
                        resultAdj = value2.intValue();
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("MaxAdj Changed: ");
                    stringBuilder.append(pid);
                    stringBuilder.append(" From: ");
                    stringBuilder.append(value);
                    stringBuilder.append(" to: ");
                    stringBuilder.append(resultAdj);
                    stringBuilder.append(" by: ");
                    stringBuilder.append(callingPid);
                    Slog.w("whetstone.activity", stringBuilder.toString());
                }
                WhetstoneClientManager.updatePackageLockedStatus(packageName, locked, userId);
            } catch (Exception e) {
                Log.e("whetstone.activity", Log.getStackTraceString(e));
            }
        }
    }

    public void setGmsBlockerEnable(int uid, boolean enable) throws RemoteException {
        try {
            Method method = ReflectionUtils.findMethodExact(this.MiuiNetworkManagementService, "setGmsBlockerEnable", Integer.TYPE, Boolean.TYPE);
            if (method != null) {
                method.invoke(this.mNetService, new Object[]{Integer.valueOf(uid), Boolean.valueOf(enable)});
            }
        } catch (Exception e) {
            Log.e("whetstone.activity", Log.getStackTraceString(e));
        }
    }

    public boolean initGmsChain(String name, int uid, String rule) throws RemoteException {
        try {
            Method method = ReflectionUtils.findMethodExact(this.MiuiNetworkManagementService, "initGmsChain", String.class, Integer.TYPE, String.class);
            if (method == null) {
                return false;
            }
            return ((Boolean) method.invoke(this.mNetService, new Object[]{name, Integer.valueOf(uid), rule})).booleanValue();
        } catch (Exception e) {
            Log.e("whetstone.activity", Log.getStackTraceString(e));
            return false;
        }
    }

    public boolean setGmsChainState(String name, boolean enable) throws RemoteException {
        try {
            Method method = ReflectionUtils.findMethodExact(this.MiuiNetworkManagementService, "setGmsChainState", String.class, Boolean.TYPE);
            if (method == null) {
                return false;
            }
            return ((Boolean) method.invoke(this.mNetService, new Object[]{name, Boolean.valueOf(enable)})).booleanValue();
        } catch (Exception e) {
            Log.e("whetstone.activity", Log.getStackTraceString(e));
            return false;
        }
    }

    public String[] getBackgroundAPPS() {
        return null;
    }

    public boolean scheduleTrimMemory(int pid, int level) {
        String str = "whetstone.activity";
        Class scheduleTrimMemory = this.mExtraActivityManagerService;
        if (scheduleTrimMemory != null) {
            try {
                Method scheduleTrimMemory2 = ReflectionUtils.findMethodExact(scheduleTrimMemory, "scheduleTrimMemory", Integer.TYPE, Integer.TYPE);
                if (scheduleTrimMemory2 != null) {
                    scheduleTrimMemory2.invoke(null, new Object[]{Integer.valueOf(pid), Integer.valueOf(level)});
                    return true;
                }
            } catch (IllegalArgumentException e) {
                Log.e(str, Log.getStackTraceString(e));
            } catch (NoSuchMethodException e2) {
                Log.e(str, Log.getStackTraceString(e2));
            } catch (IllegalAccessException e3) {
                Log.e(str, Log.getStackTraceString(e3));
            } catch (InvocationTargetException e4) {
                Log.e(str, Log.getStackTraceString(e4));
            }
        }
        return false;
    }

    public boolean getProcessReceiverState(int pid) {
        String str = "whetstone.activity";
        int ProcStat = -1;
        Class getProcStateByPid = this.mExtraActivityManagerService;
        if (getProcStateByPid != null) {
            try {
                Method getProcStateByPid2 = ReflectionUtils.findMethodExact(getProcStateByPid, "getProcStateByPid", Integer.TYPE);
                if (getProcStateByPid2 != null) {
                    ProcStat = ((Integer) getProcStateByPid2.invoke(null, new Object[]{Integer.valueOf(pid)})).intValue();
                }
            } catch (IllegalArgumentException e) {
                Log.e(str, Log.getStackTraceString(e));
            } catch (NoSuchMethodException e2) {
                Log.e(str, Log.getStackTraceString(e2));
            } catch (IllegalAccessException e3) {
                Log.e(str, Log.getStackTraceString(e3));
            } catch (InvocationTargetException e4) {
                Log.e(str, Log.getStackTraceString(e4));
            }
        }
        if (ProcStat == 12) {
            return true;
        }
        return false;
    }

    public boolean isProcessExecutingServices(int pid) {
        String str = "whetstone.activity";
        int executingServicesN = 0;
        Class getExecutingServicesSize = this.mExtraActivityManagerService;
        if (getExecutingServicesSize != null) {
            try {
                Method getExecutingServicesSize2 = ReflectionUtils.findMethodExact(getExecutingServicesSize, "getExecutingServicesSize", Integer.TYPE);
                if (getExecutingServicesSize2 != null) {
                    executingServicesN = ((Integer) getExecutingServicesSize2.invoke(null, new Object[]{Integer.valueOf(pid)})).intValue();
                }
            } catch (IllegalArgumentException e) {
                Log.e(str, Log.getStackTraceString(e));
            } catch (NoSuchMethodException e2) {
                Log.e(str, Log.getStackTraceString(e2));
            } catch (IllegalAccessException e3) {
                Log.e(str, Log.getStackTraceString(e3));
            } catch (InvocationTargetException e4) {
                Log.e(str, Log.getStackTraceString(e4));
            }
        }
        if (executingServicesN > 0) {
            return true;
        }
        return false;
    }

    public boolean scheduleStopService(String packageName, ComponentName name) {
        return false;
    }

    public boolean distoryActivity(int pid) {
        String str = "distoryActivity";
        String str2 = "whetstone.activity";
        Method method = this.mScheduleDestroyActivities;
        if (method == null) {
            return false;
        }
        try {
            method.invoke(null, new Object[]{Integer.valueOf(pid), Boolean.valueOf(false), KillProcessEvent.POLICY_WHETSTONE});
            return true;
        } catch (IllegalArgumentException e) {
            Log.e(str2, str, e);
            return false;
        } catch (IllegalAccessException e2) {
            Log.e(str2, str, e2);
            return false;
        } catch (InvocationTargetException e3) {
            Log.e(str2, str, e3);
            return false;
        }
    }

    public boolean putUidFrozenState(int uid, int state) {
        boolean z = false;
        if (!UserHandle.isApp(uid)) {
            return false;
        }
        synchronized (this.mUidFrozenState) {
            SparseBooleanArray sparseBooleanArray = this.mUidFrozenState;
            if (1 == state) {
                z = true;
            }
            sparseBooleanArray.put(uid, z);
        }
        return true;
    }

    public boolean getUidFrozenState(int uid) {
        if (!UserHandle.isApp(uid)) {
            return false;
        }
        boolean z;
        synchronized (this.mUidFrozenState) {
            z = this.mUidFrozenState.get(uid, false);
        }
        return z;
    }

    public boolean isAlarmAllowedLocked(int pid, int uid, String tag) {
        StringBuilder stringBuilder;
        String str = "whetstone.activity";
        if (D) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("isAlarmAllowedLocked() for pid = ");
            stringBuilder.append(pid);
            stringBuilder.append(" uid= ");
            stringBuilder.append(uid);
            stringBuilder.append(", tag =");
            stringBuilder.append(tag);
            Log.d(str, stringBuilder.toString());
        }
        if (getPowerKeeperPolicy().isAlarmAllowedLocked(pid, uid, tag)) {
            return true;
        }
        if (D) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Alarm restrict for pid = ");
            stringBuilder.append(pid);
            stringBuilder.append(", uid = ");
            stringBuilder.append(uid);
            stringBuilder.append(", tag = ");
            stringBuilder.append(tag);
            Log.d(str, stringBuilder.toString());
        }
        return false;
    }

    public boolean isBroadcastAllowedLocked(int pid, int uid, String type) {
        StringBuilder stringBuilder;
        String str = "whetstone.activity";
        if (D) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("isBroadcastAllowedLocked() for pid = ");
            stringBuilder.append(pid);
            stringBuilder.append(" uid= ");
            stringBuilder.append(uid);
            stringBuilder.append(", type =");
            stringBuilder.append(type);
            Log.d(str, stringBuilder.toString());
        }
        if (getPowerKeeperPolicy().isBroadcastAllowedLocked(pid, uid, type)) {
            return true;
        }
        if (D) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Broadcast restrict for pid = ");
            stringBuilder.append(pid);
            stringBuilder.append(", uid = ");
            stringBuilder.append(uid);
            stringBuilder.append(", type = ");
            stringBuilder.append(type);
            Log.d(str, stringBuilder.toString());
        }
        return false;
    }

    public void bindWhetstoneService(IBinder client) {
        WhetstoneClientManager.init(this.mContext, IWhetstoneClient.Stub.asInterface(client), this);
    }

    public int getSystemPid() throws RemoteException {
        return Process.myPid();
    }

    public boolean setPerformanceComponents(ComponentName[] name) {
        return WhetstoneClientManager.setComponment(name);
    }

    public long getAndroidCachedEmptyProcessMemory() {
        return WhetstoneClientManager.getEmptyProcTotalMemoryInfo();
    }

    public void addAppToServiceControlWhitelist(List<String> listPkg) {
        WtServiceControlEntry.addAppToServiceControlWhitelist((List) listPkg);
    }

    public void removeAppFromServiceControlWhitelist(String pkgName) {
        WtServiceControlEntry.removeAppFromServiceControlWhitelist(pkgName);
    }

    public void updateApplicationsMemoryThreshold(List<String> thresholds) {
        WhetstoneClientManager.updateApplicationsMemoryThreshold(thresholds);
    }

    public void updateUserLockedAppList(List<String> thresholds) {
        if (checkCallInterfacePermission()) {
            WhetstoneClientManager.updateUserLockedAppList(thresholds);
        }
    }

    public void updateUserLockedAppListWithUserId(List<String> thresholds, int userId) {
        WhetstoneClientManager.updateUserLockedAppList(thresholds, userId);
    }

    public boolean checkIfPackageIsLocked(String packageName) {
        return WhetstoneClientManager.checkIfPackageIsLocked(packageName);
    }

    public boolean checkIfPackageIsLockedWithUserId(String packageName, int userId) {
        return WhetstoneClientManager.checkIfPackageIsLocked(packageName, userId);
    }

    private void updateCurrentProcessPss(int pid, long pss) {
        Message msg = this.mHandler.obtainMessage(3);
        msg.arg1 = pid;
        msg.arg2 = (int) pss;
        this.mHandler.sendMessage(msg);
    }

    public void checkApplicationsMemoryThreshold(String packageName, int pid, long threshold) {
    }

    public void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        if (this.mContext.checkCallingOrSelfPermission(permission.DUMP) != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Permission Denial: can't dump whetstone.activity service from from pid=");
            stringBuilder.append(Binder.getCallingPid());
            stringBuilder.append(", uid=");
            stringBuilder.append(Binder.getCallingUid());
            writer.println(stringBuilder.toString());
            return;
        }
        boolean dumpAll = false;
        boolean powerKeeper = false;
        if (args != null) {
            for (String arg : args) {
                if (arg.equalsIgnoreCase(KillProcessEvent.POLICY_POWERKEEPER)) {
                    powerKeeper = true;
                } else if (arg.equalsIgnoreCase("-a") || arg.equalsIgnoreCase("all")) {
                    dumpAll = true;
                }
            }
        } else {
            dumpAll = true;
        }
        if (powerKeeper || dumpAll) {
            PowerKeeperPolicy p = getPowerKeeperPolicy();
            if (p != null) {
                p.dump(fd, writer, args);
            }
        }
    }

    public int getPartialWakeLockHoldByUid(int uid) throws RemoteException {
        String str = "getPartialWakeLockHoldByUid";
        int ret = 0;
        Method method = this.getPartialWakeLockHoldByUid;
        String str2 = "whetstone.activity";
        if (method == null) {
            Log.v(str2, "whetstone.activity getPartialWakeLockHoldByUid == null");
            return -1;
        }
        try {
            Integer wakeLockCnt = (Integer) method.invoke(this.PowerManagerServiceInjector, new Object[]{Integer.valueOf(uid)});
            if (wakeLockCnt != null) {
                ret = wakeLockCnt.intValue();
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("whetstone.activity ret = ");
            stringBuilder.append(ret);
            Log.v(str2, stringBuilder.toString());
        } catch (IllegalArgumentException e) {
            Log.e(str2, str, e);
        } catch (IllegalAccessException e2) {
            Log.e(str2, str, e2);
        } catch (InvocationTargetException e3) {
            Log.e(str2, str, e3);
        }
        return ret;
    }

    public void updateFrameworkCommonConfig(String json) {
        WhetstoneClientManager.mSetting.updateFrameworkCommonConfig(json);
    }

    public static WhetstoneActivityManagerService getSingletonService() {
        return mSelf;
    }

    public int checkPackageState(String packageName, String caller, int callerType, int userId, String componentName, String processName, Object... additionalArgs) {
        return 1;
    }

    private boolean removeTaskByIdL(int taskId, boolean killProcess, boolean removeFromRecents) {
        ActivityManager am = (ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE);
        Method method = this.mRemoveTask;
        String str = "whetstone.activity";
        if (method == null) {
            Slog.e(str, "could not find removeTaskById L");
            return false;
        }
        try {
            Object[] objArr = new Object[2];
            objArr[0] = Integer.valueOf(taskId);
            objArr[1] = Integer.valueOf(killProcess ? 1 : 0);
            return ((Boolean) method.invoke(am, objArr)).booleanValue();
        } catch (Exception e) {
            Log.e(str, Log.getStackTraceString(e));
            return false;
        }
    }

    private boolean removeTaskByIdLocked(int taskId, boolean killProcess, boolean removeFromRecents) {
        if (this.mRemoveTaskByIdLocked == null) {
            Slog.e("whetstone.activity", "could not find removeTaskById M");
            return false;
        }
        long orig = Binder.clearCallingIdentity();
        try {
            boolean res;
            synchronized (this.mAM) {
                if (VERSION.SDK_INT <= 23) {
                    res = ((Boolean) this.mRemoveTaskByIdLocked.invoke(this.mAM, new Object[]{Integer.valueOf(taskId), Boolean.valueOf(killProcess)})).booleanValue();
                } else if (VERSION.SDK_INT < 26) {
                    res = ((Boolean) this.mRemoveTaskByIdLocked.invoke(this.mAM, new Object[]{Integer.valueOf(taskId), Boolean.valueOf(killProcess), Boolean.valueOf(removeFromRecents)})).booleanValue();
                } else {
                    res = ((Boolean) this.mRemoveTaskByIdLocked.invoke(null, new Object[]{Integer.valueOf(taskId), Boolean.valueOf(killProcess), Boolean.valueOf(removeFromRecents)})).booleanValue();
                }
            }
            Binder.restoreCallingIdentity(orig);
            return res;
        } catch (Exception e) {
            try {
                Log.e("whetstone.activity", Log.getStackTraceString(e));
                return false;
            } finally {
                Binder.restoreCallingIdentity(orig);
            }
        }
    }

    private boolean removeTaskByIdInternal(int taskId, boolean killProcess, boolean removeFromRecents) {
        String str = permission.REMOVE_TASKS;
        String str2 = "whetstone.activity";
        if (VERSION.SDK_INT <= 21) {
            return removeTaskByIdL(taskId, killProcess, true);
        }
        try {
            if (((IActivityManager) this.mAM).checkPermission(str, Binder.getCallingPid(), UserHandle.getAppId(Binder.getCallingUid())) == 0) {
                return removeTaskByIdLocked(taskId, killProcess, true);
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Permission Denial: removeTaskById from pid=");
            stringBuilder.append(Binder.getCallingPid());
            stringBuilder.append(", uid=");
            stringBuilder.append(Binder.getCallingUid());
            stringBuilder.append(" requires ");
            stringBuilder.append(str);
            Slog.w(str2, stringBuilder.toString());
            return false;
        } catch (Exception e) {
            Log.e(str2, Log.getStackTraceString(e));
            return false;
        }
    }

    private void findRemoveTaskMethod() {
        String str = "removeTask";
        String str2 = "WhetstoneActivityManagerService";
        String str3 = "whetstone.activity";
        if (VERSION.SDK_INT <= 21) {
            try {
                this.mRemoveTask = ReflectionUtils.findMethodExact(ActivityManager.class, str, Integer.TYPE, Integer.TYPE);
                this.mRemoveTaskByIdLocked = null;
            } catch (NoSuchMethodException e) {
                Log.e(str3, str2, e);
            }
        } else {
            String str4 = "com.android.server.am.ActivityManagerService";
            String str5 = "removeTaskByIdLocked";
            if (VERSION.SDK_INT <= 23) {
                try {
                    this.mRemoveTask = ReflectionUtils.findMethodExact(ActivityManager.class, str, Integer.TYPE);
                    this.mRemoveTaskByIdLocked = ReflectionUtils.findMethodExact(ReflectionUtils.findClass(str4, this.mSystemServiceClassLoader), str5, Integer.TYPE, Boolean.TYPE);
                } catch (NoSuchMethodException e2) {
                    Log.e(str3, str2, e2);
                } catch (ClassNotFoundException e3) {
                    Log.e(str3, str2, e3);
                } catch (IllegalArgumentException e4) {
                    Log.e(str3, str2, e4);
                }
            } else if (VERSION.SDK_INT < 26) {
                try {
                    this.mRemoveTask = ReflectionUtils.findMethodExact(ActivityManager.class, str, Integer.TYPE);
                    this.mRemoveTaskByIdLocked = ReflectionUtils.findMethodExact(ReflectionUtils.findClass(str4, this.mSystemServiceClassLoader), str5, Integer.TYPE, Boolean.TYPE, Boolean.TYPE);
                } catch (NoSuchMethodException e22) {
                    Log.e(str3, str2, e22);
                } catch (ClassNotFoundException e32) {
                    Log.e(str3, str2, e32);
                } catch (IllegalArgumentException e42) {
                    Log.e(str3, str2, e42);
                }
            } else {
                try {
                    this.mRemoveTask = ReflectionUtils.findMethodExact(ActivityManager.class, str, Integer.TYPE);
                    this.mRemoveTaskByIdLocked = ReflectionUtils.findMethodExact(this.mExtraActivityManagerService, str5, Integer.TYPE, Boolean.TYPE, Boolean.TYPE);
                } catch (NoSuchMethodException e222) {
                    Log.e(str3, str2, e222);
                } catch (IllegalArgumentException e422) {
                    Log.e(str3, str2, e422);
                } catch (Exception e5) {
                    Log.e(str3, str2, e5);
                }
            }
        }
        Method method = this.mRemoveTask;
        if (method != null) {
            method.setAccessible(true);
        } else {
            Slog.e(str3, "could not find removeTask");
        }
        method = this.mRemoveTaskByIdLocked;
        if (method != null) {
            method.setAccessible(true);
        } else {
            Slog.e(str3, "could not find removeTaskByIdLocked");
        }
    }

    public boolean removeTaskById(int taskId, boolean killProcess) {
        return removeTaskByIdInternal(taskId, killProcess, true);
    }

    public boolean getConnProviderNames(String packageName, int userId, List<String> providers) {
        String str = "whetstone.activity";
        Class cls = this.mExtraActivityManagerService;
        if (cls != null) {
            List<String> serProviders = null;
            try {
                if (this.mGetConnProviderNames == null) {
                    this.mGetConnProviderNames = ReflectionUtils.findMethodExact(cls, "getConnProviderNamesLocked", String.class, Integer.TYPE);
                }
                if (this.mGetConnProviderNames != null) {
                    serProviders = (List) this.mGetConnProviderNames.invoke(null, new Object[]{packageName, Integer.valueOf(userId)});
                }
                if (serProviders != null && serProviders.size() > 0) {
                    boolean result = true;
                    if (providers != null) {
                        if (providers.size() > 0) {
                            for (String name : providers) {
                                if (!serProviders.contains(name)) {
                                    result = false;
                                    break;
                                }
                            }
                        }
                        providers.clear();
                        for (String name2 : serProviders) {
                            providers.add(name2);
                        }
                    }
                    return result;
                } else if (providers != null) {
                    providers.clear();
                }
            } catch (IllegalArgumentException e) {
                Log.e(str, Log.getStackTraceString(e));
            } catch (NoSuchMethodException e2) {
                Log.e(str, Log.getStackTraceString(e2));
            } catch (IllegalAccessException e3) {
                Log.e(str, Log.getStackTraceString(e3));
            } catch (InvocationTargetException e4) {
                Log.e(str, Log.getStackTraceString(e4));
            }
        }
        return false;
    }

    public void setWhetstonePackageInfo(List<WhetstonePackageInfo> list, boolean isAppend) {
    }
}
