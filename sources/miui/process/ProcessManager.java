package miui.process;

import android.os.Build.VERSION;
import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class ProcessManager {
    public static final int AI_MAX_ADJ = (VERSION.SDK_INT > 23 ? 400 : 4);
    public static final int AI_MAX_PROCESS_STATE = 14;
    public static final boolean DEBUG = true;
    public static final int DEFAULT_MAX_ADJ = (VERSION.SDK_INT > 23 ? 1001 : 16);
    public static final int DEFAULT_PROCESS_STATE = 21;
    public static final int FLAG_START_PROCESS_AI = 1;
    public static final int FLAG_START_PROCESS_FAST_RESTART = 2;
    public static final int LOCKED_MAX_ADJ = (VERSION.SDK_INT > 23 ? 400 : 4);
    public static final int LOCKED_MAX_PROCESS_STATE = 14;
    public static final long MAX_ADJ_BOOST_TIMEOUT = 300000;
    public static final int MIUI_AI_MODE_STACK_ID = 7;
    public static final int PROTECT_MAX_ADJ;
    public static final int PROTECT_MAX_PROCESS_STATE = 14;
    public static final String SERVICE_NAME = "ProcessManager";
    private static final int SINGLE_COUNT = 1;
    public static final String TAG = "ProcessManager";

    static {
        int i = 400;
        if (VERSION.SDK_INT <= 23) {
            i = 4;
        }
        PROTECT_MAX_ADJ = i;
    }

    public static boolean kill(ProcessConfig config) {
        try {
            return ProcessManagerNative.getDefault().kill(config);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateApplicationLockedState(String packageName, int userId, boolean isLocked) {
        try {
            ProcessManagerNative.getDefault().updateApplicationLockedState(packageName, userId, isLocked);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static List<String> getLockedApplication(int userId) {
        try {
            return ProcessManagerNative.getDefault().getLockedApplication(userId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isLockedApplication(String packageName, int userId) {
        try {
            return ProcessManagerNative.getDefault().isLockedApplication(packageName, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void updateConfig(ProcessConfig config) {
        try {
            ProcessManagerNative.getDefault().updateConfig(config);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static boolean startProcess(PreloadProcessData data, int userId, int flag) {
        return startProcess(data, false, userId, flag);
    }

    public static boolean startProcess(String packageName, boolean ignoreMemory, int userId, int flag) {
        return startProcess(new PreloadProcessData(packageName, false, null), ignoreMemory, userId, flag);
    }

    public static boolean startProcess(PreloadProcessData data, boolean ignoreMemory, int userId, int flag) {
        boolean z = false;
        if (data == null || TextUtils.isEmpty(data.getPackageName())) {
            Log.e("ProcessManager", "preload data and packageName cannot be null!");
            return false;
        }
        List<PreloadProcessData> dataList = new ArrayList(1);
        dataList.add(data);
        if (startPreloadProcesses(dataList, 1, ignoreMemory, userId, flag) == 1) {
            z = true;
        }
        return z;
    }

    public static int startProcesses(List<String> packageNames, int startProcessCount, boolean ignoreMemory, int userId, int flag) {
        if (packageNames == null || packageNames.size() <= 0) {
            return 0;
        }
        List<PreloadProcessData> dataList = new ArrayList();
        for (String packageName : packageNames) {
            if (!TextUtils.isEmpty(packageName)) {
                dataList.add(new PreloadProcessData(packageName, false, null));
            }
        }
        return startPreloadProcesses(dataList, startProcessCount, ignoreMemory, userId, flag);
    }

    public static int startPreloadProcesses(List<PreloadProcessData> dataList, int startProcessCount, boolean ignoreMemory, int userId, int flag) {
        String str = "ProcessManager";
        if (dataList == null || dataList.size() == 0) {
            Log.e(str, "dataList cannot be null!");
            return 0;
        } else if (dataList.size() < startProcessCount || startProcessCount <= 0) {
            Log.e(str, "illegal start number!");
            return 0;
        } else {
            int result = 0;
            try {
                result = ProcessManagerNative.getDefault().startProcesses(dataList, startProcessCount, ignoreMemory, userId, flag);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    public static boolean protectCurrentProcess(boolean isProtected) {
        try {
            return ProcessManagerNative.getDefault().protectCurrentProcess(isProtected, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean protectCurrentProcess(boolean isProtected, int timeout) {
        try {
            return ProcessManagerNative.getDefault().protectCurrentProcess(isProtected, timeout);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void adjBoost(String processName, int targetAdj, long timeout, int userId) {
        try {
            ProcessManagerNative.getDefault().adjBoost(processName, targetAdj, timeout, userId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void updateCloudData(ProcessCloudData cloudData) {
        if (cloudData != null) {
            try {
                ProcessManagerNative.getDefault().updateCloudData(cloudData);
                return;
            } catch (RemoteException e) {
                e.printStackTrace();
                return;
            }
        }
        Log.e("ProcessManager", "cloudData is null!");
    }

    public static void registerForegroundInfoListener(IForegroundInfoListener listener) {
        try {
            ProcessManagerNative.getDefault().registerForegroundInfoListener(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void unregisterForegroundInfoListener(IForegroundInfoListener listener) {
        try {
            ProcessManagerNative.getDefault().unregisterForegroundInfoListener(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static ForegroundInfo getForegroundInfo() {
        try {
            return ProcessManagerNative.getDefault().getForegroundInfo();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void addMiuiApplicationThread(IMiuiApplicationThread applicationThread) {
        try {
            IProcessManager pm = ProcessManagerNative.getDefault();
            if (pm != null) {
                pm.addMiuiApplicationThread(applicationThread, Process.myPid());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static IMiuiApplicationThread getForegroundApplicationThread() {
        try {
            IProcessManager pm = ProcessManagerNative.getDefault();
            if (pm != null) {
                return pm.getForegroundApplicationThread();
            }
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void registerActivityChangeListener(List<String> targetPackages, List<String> targetActivities, IActivityChangeListener listener) {
        try {
            ProcessManagerNative.getDefault().registerActivityChangeListener(targetPackages, targetActivities, listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void unregisterActivityChanageListener(IActivityChangeListener listener) {
        try {
            ProcessManagerNative.getDefault().unregisterActivityChangeListener(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static RunningProcessInfo getRunningProcessInfoByPid(int pid) throws RemoteException {
        List<RunningProcessInfo> infoList = getRunningProcessInfo(pid, -1, null, null, -1);
        if (infoList == null || infoList.isEmpty()) {
            return null;
        }
        return (RunningProcessInfo) infoList.get(0);
    }

    public static List<RunningProcessInfo> getRunningProcessInfoByUid(int uid) throws RemoteException {
        return getRunningProcessInfo(-1, uid, null, null, -1);
    }

    public static List<RunningProcessInfo> getRunningProcessInfoByPackageName(String packageName) throws RemoteException {
        return getRunningProcessInfo(-1, -1, packageName, null, -1);
    }

    public static RunningProcessInfo getRunningProcessInfoByProcessName(String processName) throws RemoteException {
        List<RunningProcessInfo> infoList = getRunningProcessInfo(-1, -1, null, processName, -1);
        if (infoList == null || infoList.isEmpty()) {
            return null;
        }
        return (RunningProcessInfo) infoList.get(0);
    }

    public static List<RunningProcessInfo> getRunningProcessInfo(int pid, int uid, String packageName, String processName, int userId) throws RemoteException {
        return ProcessManagerNative.getDefault().getRunningProcessInfo(pid, uid, packageName, processName, userId);
    }

    public static void boostCameraIfNeed() {
        try {
            ProcessManagerNative.getDefault().boostCameraIfNeeded();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
