package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.ProcessErrorStateInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityManager.StackInfo;
import android.app.ActivityManager.TaskSnapshot;
import android.app.ApplicationErrorReport.ParcelableCrashInfo;
import android.content.ComponentName;
import android.content.IIntentReceiver;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.ParceledListSlice;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IProgressListener;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.StrictMode.ViolationInfo;
import android.os.WorkSource;
import android.text.TextUtils;
import android.view.IRecentsAnimationRunner;
import com.android.internal.os.IResultReceiver;
import com.miui.internal.transition.IMiuiAppTransitionAnimationHelper;
import java.util.List;

public interface IActivityManager extends IInterface {

    public static class Default implements IActivityManager {
        public ParcelFileDescriptor openContentUri(String uriString) throws RemoteException {
            return null;
        }

        public void registerUidObserver(IUidObserver observer, int which, int cutpoint, String callingPackage) throws RemoteException {
        }

        public void unregisterUidObserver(IUidObserver observer) throws RemoteException {
        }

        public boolean isUidActive(int uid, String callingPackage) throws RemoteException {
            return false;
        }

        public int getUidProcessState(int uid, String callingPackage) throws RemoteException {
            return 0;
        }

        public void handleApplicationCrash(IBinder app, ParcelableCrashInfo crashInfo) throws RemoteException {
        }

        public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
            return 0;
        }

        public void unhandledBack() throws RemoteException {
        }

        public boolean finishActivity(IBinder token, int code, Intent data, int finishTask) throws RemoteException {
            return false;
        }

        public Intent registerReceiver(IApplicationThread caller, String callerPackage, IIntentReceiver receiver, IntentFilter filter, String requiredPermission, int userId, int flags) throws RemoteException {
            return null;
        }

        public void unregisterReceiver(IIntentReceiver receiver) throws RemoteException {
        }

        public int broadcastIntent(IApplicationThread caller, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
            return 0;
        }

        public void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId) throws RemoteException {
        }

        public void finishReceiver(IBinder who, int resultCode, String resultData, Bundle map, boolean abortBroadcast, int flags) throws RemoteException {
        }

        public void attachApplication(IApplicationThread app, long startSeq) throws RemoteException {
        }

        public List<RunningTaskInfo> getTasks(int maxNum) throws RemoteException {
            return null;
        }

        public List<RunningTaskInfo> getFilteredTasks(int maxNum, int ignoreActivityType, int ignoreWindowingMode) throws RemoteException {
            return null;
        }

        public void moveTaskToFront(IApplicationThread caller, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
        }

        public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
            return 0;
        }

        public ContentProviderHolder getContentProvider(IApplicationThread caller, String callingPackage, String name, int userId, boolean stable) throws RemoteException {
            return null;
        }

        public void publishContentProviders(IApplicationThread caller, List<ContentProviderHolder> list) throws RemoteException {
        }

        public boolean refContentProvider(IBinder connection, int stableDelta, int unstableDelta) throws RemoteException {
            return false;
        }

        public PendingIntent getRunningServiceControlPanel(ComponentName service) throws RemoteException {
            return null;
        }

        public ComponentName startService(IApplicationThread caller, Intent service, String resolvedType, boolean requireForeground, String callingPackage, int userId) throws RemoteException {
            return null;
        }

        public int stopService(IApplicationThread caller, Intent service, String resolvedType, int userId) throws RemoteException {
            return 0;
        }

        public int bindService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String callingPackage, int userId) throws RemoteException {
            return 0;
        }

        public int bindIsolatedService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String instanceName, String callingPackage, int userId) throws RemoteException {
            return 0;
        }

        public void updateServiceGroup(IServiceConnection connection, int group, int importance) throws RemoteException {
        }

        public boolean unbindService(IServiceConnection connection) throws RemoteException {
            return false;
        }

        public void publishService(IBinder token, Intent intent, IBinder service) throws RemoteException {
        }

        public void setDebugApp(String packageName, boolean waitForDebugger, boolean persistent) throws RemoteException {
        }

        public void setAgentApp(String packageName, String agent) throws RemoteException {
        }

        public void setAlwaysFinish(boolean enabled) throws RemoteException {
        }

        public boolean startInstrumentation(ComponentName className, String profileFile, int flags, Bundle arguments, IInstrumentationWatcher watcher, IUiAutomationConnection connection, int userId, String abiOverride) throws RemoteException {
            return false;
        }

        public void addInstrumentationResults(IApplicationThread target, Bundle results) throws RemoteException {
        }

        public void finishInstrumentation(IApplicationThread target, int resultCode, Bundle results) throws RemoteException {
        }

        public Configuration getConfiguration() throws RemoteException {
            return null;
        }

        public boolean updateConfiguration(Configuration values) throws RemoteException {
            return false;
        }

        public boolean stopServiceToken(ComponentName className, IBinder token, int startId) throws RemoteException {
            return false;
        }

        public void setProcessLimit(int max) throws RemoteException {
        }

        public int getProcessLimit() throws RemoteException {
            return 0;
        }

        public int checkPermission(String permission, int pid, int uid) throws RemoteException {
            return 0;
        }

        public int checkUriPermission(Uri uri, int pid, int uid, int mode, int userId, IBinder callerToken) throws RemoteException {
            return 0;
        }

        public void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
        }

        public void revokeUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
        }

        public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
        }

        public void showWaitingForDebugger(IApplicationThread who, boolean waiting) throws RemoteException {
        }

        public void signalPersistentProcesses(int signal) throws RemoteException {
        }

        public ParceledListSlice getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
            return null;
        }

        public void serviceDoneExecuting(IBinder token, int type, int startId, int res) throws RemoteException {
        }

        public IIntentSender getIntentSender(int type, String packageName, IBinder token, String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags, Bundle options, int userId) throws RemoteException {
            return null;
        }

        public void cancelIntentSender(IIntentSender sender) throws RemoteException {
        }

        public String getPackageForIntentSender(IIntentSender sender) throws RemoteException {
            return null;
        }

        public void registerIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
        }

        public void unregisterIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
        }

        public void enterSafeMode() throws RemoteException {
        }

        public void noteWakeupAlarm(IIntentSender sender, WorkSource workSource, int sourceUid, String sourcePkg, String tag) throws RemoteException {
        }

        public void removeContentProvider(IBinder connection, boolean stable) throws RemoteException {
        }

        public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
        }

        public void unbindFinished(IBinder token, Intent service, boolean doRebind) throws RemoteException {
        }

        public void setProcessImportant(IBinder token, int pid, boolean isForeground, String reason) throws RemoteException {
        }

        public void setServiceForeground(ComponentName className, IBinder token, int id, Notification notification, int flags, int foregroundServiceType) throws RemoteException {
        }

        public int getForegroundServiceType(ComponentName className, IBinder token) throws RemoteException {
            return 0;
        }

        public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
            return false;
        }

        public void getMemoryInfo(MemoryInfo outInfo) throws RemoteException {
        }

        public List<ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
            return null;
        }

        public boolean clearApplicationUserData(String packageName, boolean keepState, IPackageDataObserver observer, int userId) throws RemoteException {
            return false;
        }

        public void forceStopPackage(String packageName, int userId) throws RemoteException {
        }

        public boolean killPids(int[] pids, String reason, boolean secure) throws RemoteException {
            return false;
        }

        public List<RunningServiceInfo> getServices(int maxNum, int flags) throws RemoteException {
            return null;
        }

        public List<RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
            return null;
        }

        public IBinder peekService(Intent service, String resolvedType, String callingPackage) throws RemoteException {
            return null;
        }

        public boolean profileControl(String process, int userId, boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
            return false;
        }

        public boolean shutdown(int timeout) throws RemoteException {
            return false;
        }

        public void stopAppSwitches() throws RemoteException {
        }

        public void resumeAppSwitches() throws RemoteException {
        }

        public boolean bindBackupAgent(String packageName, int backupRestoreMode, int targetUserId) throws RemoteException {
            return false;
        }

        public void backupAgentCreated(String packageName, IBinder agent, int userId) throws RemoteException {
        }

        public void unbindBackupAgent(ApplicationInfo appInfo) throws RemoteException {
        }

        public int getUidForIntentSender(IIntentSender sender) throws RemoteException {
            return 0;
        }

        public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, String name, String callerPackage) throws RemoteException {
            return 0;
        }

        public void addPackageDependency(String packageName) throws RemoteException {
        }

        public void killApplication(String pkg, int appId, int userId, String reason) throws RemoteException {
        }

        public void closeSystemDialogs(String reason) throws RemoteException {
        }

        public Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws RemoteException {
            return null;
        }

        public void killApplicationProcess(String processName, int uid) throws RemoteException {
        }

        public boolean handleApplicationWtf(IBinder app, String tag, boolean system, ParcelableCrashInfo crashInfo) throws RemoteException {
            return false;
        }

        public void killBackgroundProcesses(String packageName, int userId) throws RemoteException {
        }

        public boolean isUserAMonkey() throws RemoteException {
            return false;
        }

        public List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
            return null;
        }

        public void finishHeavyWeightApp() throws RemoteException {
        }

        public void handleApplicationStrictModeViolation(IBinder app, int penaltyMask, ViolationInfo crashInfo) throws RemoteException {
        }

        public boolean isTopActivityImmersive() throws RemoteException {
            return false;
        }

        public void crashApplication(int uid, int initialPid, String packageName, int userId, String message) throws RemoteException {
        }

        public String getProviderMimeType(Uri uri, int userId) throws RemoteException {
            return null;
        }

        public boolean dumpHeap(String process, int userId, boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) throws RemoteException {
            return false;
        }

        public boolean isUserRunning(int userid, int flags) throws RemoteException {
            return false;
        }

        public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
        }

        public boolean switchUser(int userid) throws RemoteException {
            return false;
        }

        public boolean removeTask(int taskId) throws RemoteException {
            return false;
        }

        public void registerProcessObserver(IProcessObserver observer) throws RemoteException {
        }

        public void unregisterProcessObserver(IProcessObserver observer) throws RemoteException {
        }

        public boolean isIntentSenderTargetedToPackage(IIntentSender sender) throws RemoteException {
            return false;
        }

        public void updatePersistentConfiguration(Configuration values) throws RemoteException {
        }

        public long[] getProcessPss(int[] pids) throws RemoteException {
            return null;
        }

        public void showBootMessage(CharSequence msg, boolean always) throws RemoteException {
        }

        public void killAllBackgroundProcesses() throws RemoteException {
        }

        public ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token, String tag) throws RemoteException {
            return null;
        }

        public void removeContentProviderExternal(String name, IBinder token) throws RemoteException {
        }

        public void removeContentProviderExternalAsUser(String name, IBinder token, int userId) throws RemoteException {
        }

        public void getMyMemoryState(RunningAppProcessInfo outInfo) throws RemoteException {
        }

        public boolean killProcessesBelowForeground(String reason) throws RemoteException {
            return false;
        }

        public UserInfo getCurrentUser() throws RemoteException {
            return null;
        }

        public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
            return 0;
        }

        public void unstableProviderDied(IBinder connection) throws RemoteException {
        }

        public boolean isIntentSenderAnActivity(IIntentSender sender) throws RemoteException {
            return false;
        }

        public boolean isIntentSenderAForegroundService(IIntentSender sender) throws RemoteException {
            return false;
        }

        public boolean isIntentSenderABroadcast(IIntentSender sender) throws RemoteException {
            return false;
        }

        public int startActivityAsUser(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        public int stopUser(int userid, boolean force, IStopUserCallback callback) throws RemoteException {
            return 0;
        }

        public void registerUserSwitchObserver(IUserSwitchObserver observer, String name) throws RemoteException {
        }

        public void unregisterUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
        }

        public int[] getRunningUserIds() throws RemoteException {
            return null;
        }

        public void requestSystemServerHeapDump() throws RemoteException {
        }

        public void requestBugReport(int bugreportType) throws RemoteException {
        }

        public void requestTelephonyBugReport(String shareTitle, String shareDescription) throws RemoteException {
        }

        public void requestWifiBugReport(String shareTitle, String shareDescription) throws RemoteException {
        }

        public Intent getIntentForIntentSender(IIntentSender sender) throws RemoteException {
            return null;
        }

        public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
            return null;
        }

        public void killUid(int appId, int userId, String reason) throws RemoteException {
        }

        public void setUserIsMonkey(boolean monkey) throws RemoteException {
        }

        public void hang(IBinder who, boolean allowRestart) throws RemoteException {
        }

        public List<StackInfo> getAllStackInfos() throws RemoteException {
            return null;
        }

        public void moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
        }

        public void resizeStack(int stackId, Rect bounds, boolean allowResizeInDockedMode, boolean preserveWindows, boolean animate, int animationDuration) throws RemoteException {
        }

        public void setFocusedStack(int stackId) throws RemoteException {
        }

        public StackInfo getFocusedStackInfo() throws RemoteException {
            return null;
        }

        public void restart() throws RemoteException {
        }

        public void performIdleMaintenance() throws RemoteException {
        }

        public void appNotRespondingViaProvider(IBinder connection) throws RemoteException {
        }

        public Rect getTaskBounds(int taskId) throws RemoteException {
            return null;
        }

        public boolean setProcessMemoryTrimLevel(String process, int uid, int level) throws RemoteException {
            return false;
        }

        public String getTagForIntentSender(IIntentSender sender, String prefix) throws RemoteException {
            return null;
        }

        public boolean startUserInBackground(int userid) throws RemoteException {
            return false;
        }

        public boolean isInLockTaskMode() throws RemoteException {
            return false;
        }

        public void startRecentsActivity(Intent intent, IAssistDataReceiver assistDataReceiver, IRecentsAnimationRunner recentsAnimationRunner) throws RemoteException {
        }

        public void cancelRecentsAnimation(boolean restoreHomeStackPosition) throws RemoteException {
        }

        public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
            return 0;
        }

        public void startSystemLockTaskMode(int taskId) throws RemoteException {
        }

        public boolean isTopOfTask(IBinder token) throws RemoteException {
            return false;
        }

        public void bootAnimationComplete() throws RemoteException {
        }

        public int checkPermissionWithToken(String permission, int pid, int uid, IBinder callerToken) throws RemoteException {
            return 0;
        }

        public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        public void notifyCleartextNetwork(int uid, byte[] firstPacket) throws RemoteException {
        }

        public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
        }

        public void resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
        }

        public int getLockTaskModeState() throws RemoteException {
            return 0;
        }

        public void setDumpHeapDebugLimit(String processName, int uid, long maxMemSize, String reportPackage) throws RemoteException {
        }

        public void dumpHeapFinished(String path) throws RemoteException {
        }

        public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
        }

        public void noteAlarmStart(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
        }

        public void noteAlarmFinish(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
        }

        public int getPackageProcessState(String packageName, String callingPackage) throws RemoteException {
            return 0;
        }

        public void updateDeviceOwner(String packageName) throws RemoteException {
        }

        public boolean startBinderTracking() throws RemoteException {
            return false;
        }

        public boolean stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
            return false;
        }

        public void positionTaskInStack(int taskId, int stackId, int position) throws RemoteException {
        }

        public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
        }

        public boolean moveTopActivityToPinnedStack(int stackId, Rect bounds) throws RemoteException {
            return false;
        }

        public boolean isAppStartModeDisabled(int uid, String packageName) throws RemoteException {
            return false;
        }

        public boolean unlockUser(int userid, byte[] token, byte[] secret, IProgressListener listener) throws RemoteException {
            return false;
        }

        public void killPackageDependents(String packageName, int userId) throws RemoteException {
        }

        public void resizeDockedStack(Rect dockedBounds, Rect tempDockedTaskBounds, Rect tempDockedTaskInsetBounds, Rect tempOtherTaskBounds, Rect tempOtherTaskInsetBounds) throws RemoteException {
        }

        public void removeStack(int stackId) throws RemoteException {
        }

        public void makePackageIdle(String packageName, int userId) throws RemoteException {
        }

        public int getMemoryTrimLevel() throws RemoteException {
            return 0;
        }

        public boolean isVrModePackageEnabled(ComponentName packageName) throws RemoteException {
            return false;
        }

        public void notifyLockedProfile(int userId) throws RemoteException {
        }

        public void startConfirmDeviceCredentialIntent(Intent intent, Bundle options) throws RemoteException {
        }

        public void sendIdleJobTrigger() throws RemoteException {
        }

        public int sendIntentSender(IIntentSender target, IBinder whitelistToken, int code, Intent intent, String resolvedType, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) throws RemoteException {
            return 0;
        }

        public boolean isBackgroundRestricted(String packageName) throws RemoteException {
            return false;
        }

        public void setRenderThread(int tid) throws RemoteException {
        }

        public void setHasTopUi(boolean hasTopUi) throws RemoteException {
        }

        public int restartUserInBackground(int userId) throws RemoteException {
            return 0;
        }

        public void cancelTaskWindowTransition(int taskId) throws RemoteException {
        }

        public TaskSnapshot getTaskSnapshot(int taskId, boolean reducedResolution) throws RemoteException {
            return null;
        }

        public void scheduleApplicationInfoChanged(List<String> list, int userId) throws RemoteException {
        }

        public void setPersistentVrThread(int tid) throws RemoteException {
        }

        public void waitForNetworkStateUpdate(long procStateSeq) throws RemoteException {
        }

        public void backgroundWhitelistUid(int uid) throws RemoteException {
        }

        public boolean startUserInBackgroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
            return false;
        }

        public void startDelegateShellPermissionIdentity(int uid, String[] permissions) throws RemoteException {
        }

        public void reportKillProcessEvent(int killerPid, int killedPid) throws RemoteException {
        }

        public void registerActivityObserver(IMiuiActivityObserver observer, Intent intent) throws RemoteException {
        }

        public void unregisterActivityObserver(IMiuiActivityObserver observer) throws RemoteException {
        }

        public void stopDelegateShellPermissionIdentity() throws RemoteException {
        }

        public void registerMiuiAppTransitionAnimationHelper(IMiuiAppTransitionAnimationHelper helper, int displayid) throws RemoteException {
        }

        public void unregisterMiuiAppTransitionAnimationHelper(int displayid) throws RemoteException {
        }

        public void setDummyTranslucent(IBinder token, boolean translucent) throws RemoteException {
        }

        public void setResizeWhiteList(List<String> list) throws RemoteException {
        }

        public void setResizeBlackList(List<String> list) throws RemoteException {
        }

        public ParcelFileDescriptor getLifeMonitor() throws RemoteException {
            return null;
        }

        public boolean startUserInForegroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IActivityManager {
        private static final String DESCRIPTOR = "android.app.IActivityManager";
        static final int TRANSACTION_addInstrumentationResults = 35;
        static final int TRANSACTION_addPackageDependency = 82;
        static final int TRANSACTION_appNotRespondingViaProvider = 140;
        static final int TRANSACTION_attachApplication = 15;
        static final int TRANSACTION_backgroundWhitelistUid = 192;
        static final int TRANSACTION_backupAgentCreated = 78;
        static final int TRANSACTION_bindBackupAgent = 77;
        static final int TRANSACTION_bindIsolatedService = 27;
        static final int TRANSACTION_bindService = 26;
        static final int TRANSACTION_bootAnimationComplete = 151;
        static final int TRANSACTION_broadcastIntent = 12;
        static final int TRANSACTION_cancelIntentSender = 52;
        static final int TRANSACTION_cancelRecentsAnimation = 147;
        static final int TRANSACTION_cancelTaskWindowTransition = 187;
        static final int TRANSACTION_checkPermission = 42;
        static final int TRANSACTION_checkPermissionWithToken = 152;
        static final int TRANSACTION_checkUriPermission = 43;
        static final int TRANSACTION_clearApplicationUserData = 67;
        static final int TRANSACTION_closeSystemDialogs = 84;
        static final int TRANSACTION_crashApplication = 94;
        static final int TRANSACTION_dumpHeap = 96;
        static final int TRANSACTION_dumpHeapFinished = 160;
        static final int TRANSACTION_enterSafeMode = 56;
        static final int TRANSACTION_finishActivity = 9;
        static final int TRANSACTION_finishHeavyWeightApp = 91;
        static final int TRANSACTION_finishInstrumentation = 36;
        static final int TRANSACTION_finishReceiver = 14;
        static final int TRANSACTION_forceStopPackage = 68;
        static final int TRANSACTION_getAllStackInfos = 133;
        static final int TRANSACTION_getConfiguration = 37;
        static final int TRANSACTION_getContentProvider = 20;
        static final int TRANSACTION_getContentProviderExternal = 108;
        static final int TRANSACTION_getCurrentUser = 113;
        static final int TRANSACTION_getFilteredTasks = 17;
        static final int TRANSACTION_getFocusedStackInfo = 137;
        static final int TRANSACTION_getForegroundServiceType = 63;
        static final int TRANSACTION_getIntentForIntentSender = 128;
        static final int TRANSACTION_getIntentSender = 51;
        static final int TRANSACTION_getLaunchedFromPackage = 129;
        static final int TRANSACTION_getLaunchedFromUid = 114;
        static final int TRANSACTION_getLifeMonitor = 204;
        static final int TRANSACTION_getLockTaskModeState = 158;
        static final int TRANSACTION_getMemoryInfo = 65;
        static final int TRANSACTION_getMemoryTrimLevel = 177;
        static final int TRANSACTION_getMyMemoryState = 111;
        static final int TRANSACTION_getPackageForIntentSender = 53;
        static final int TRANSACTION_getPackageProcessState = 164;
        static final int TRANSACTION_getProcessLimit = 41;
        static final int TRANSACTION_getProcessMemoryInfo = 85;
        static final int TRANSACTION_getProcessPss = 105;
        static final int TRANSACTION_getProcessesInErrorState = 66;
        static final int TRANSACTION_getProviderMimeType = 95;
        static final int TRANSACTION_getRecentTasks = 49;
        static final int TRANSACTION_getRunningAppProcesses = 71;
        static final int TRANSACTION_getRunningExternalApplications = 90;
        static final int TRANSACTION_getRunningServiceControlPanel = 23;
        static final int TRANSACTION_getRunningUserIds = 123;
        static final int TRANSACTION_getServices = 70;
        static final int TRANSACTION_getTagForIntentSender = 143;
        static final int TRANSACTION_getTaskBounds = 141;
        static final int TRANSACTION_getTaskForActivity = 19;
        static final int TRANSACTION_getTaskSnapshot = 188;
        static final int TRANSACTION_getTasks = 16;
        static final int TRANSACTION_getUidForIntentSender = 80;
        static final int TRANSACTION_getUidProcessState = 5;
        static final int TRANSACTION_grantUriPermission = 44;
        static final int TRANSACTION_handleApplicationCrash = 6;
        static final int TRANSACTION_handleApplicationStrictModeViolation = 92;
        static final int TRANSACTION_handleApplicationWtf = 87;
        static final int TRANSACTION_handleIncomingUser = 81;
        static final int TRANSACTION_hang = 132;
        static final int TRANSACTION_isAppStartModeDisabled = 171;
        static final int TRANSACTION_isBackgroundRestricted = 183;
        static final int TRANSACTION_isInLockTaskMode = 145;
        static final int TRANSACTION_isIntentSenderABroadcast = 118;
        static final int TRANSACTION_isIntentSenderAForegroundService = 117;
        static final int TRANSACTION_isIntentSenderAnActivity = 116;
        static final int TRANSACTION_isIntentSenderTargetedToPackage = 103;
        static final int TRANSACTION_isTopActivityImmersive = 93;
        static final int TRANSACTION_isTopOfTask = 150;
        static final int TRANSACTION_isUidActive = 4;
        static final int TRANSACTION_isUserAMonkey = 89;
        static final int TRANSACTION_isUserRunning = 97;
        static final int TRANSACTION_isVrModePackageEnabled = 178;
        static final int TRANSACTION_killAllBackgroundProcesses = 107;
        static final int TRANSACTION_killApplication = 83;
        static final int TRANSACTION_killApplicationProcess = 86;
        static final int TRANSACTION_killBackgroundProcesses = 88;
        static final int TRANSACTION_killPackageDependents = 173;
        static final int TRANSACTION_killPids = 69;
        static final int TRANSACTION_killProcessesBelowForeground = 112;
        static final int TRANSACTION_killUid = 130;
        static final int TRANSACTION_makePackageIdle = 176;
        static final int TRANSACTION_moveActivityTaskToBack = 64;
        static final int TRANSACTION_moveTaskToFront = 18;
        static final int TRANSACTION_moveTaskToStack = 134;
        static final int TRANSACTION_moveTopActivityToPinnedStack = 170;
        static final int TRANSACTION_noteAlarmFinish = 163;
        static final int TRANSACTION_noteAlarmStart = 162;
        static final int TRANSACTION_noteWakeupAlarm = 57;
        static final int TRANSACTION_notifyCleartextNetwork = 155;
        static final int TRANSACTION_notifyLockedProfile = 179;
        static final int TRANSACTION_openContentUri = 1;
        static final int TRANSACTION_peekService = 72;
        static final int TRANSACTION_performIdleMaintenance = 139;
        static final int TRANSACTION_positionTaskInStack = 168;
        static final int TRANSACTION_profileControl = 73;
        static final int TRANSACTION_publishContentProviders = 21;
        static final int TRANSACTION_publishService = 30;
        static final int TRANSACTION_refContentProvider = 22;
        static final int TRANSACTION_registerActivityObserver = 196;
        static final int TRANSACTION_registerIntentSenderCancelListener = 54;
        static final int TRANSACTION_registerMiuiAppTransitionAnimationHelper = 199;
        static final int TRANSACTION_registerProcessObserver = 101;
        static final int TRANSACTION_registerReceiver = 10;
        static final int TRANSACTION_registerTaskStackListener = 153;
        static final int TRANSACTION_registerUidObserver = 2;
        static final int TRANSACTION_registerUserSwitchObserver = 121;
        static final int TRANSACTION_removeContentProvider = 58;
        static final int TRANSACTION_removeContentProviderExternal = 109;
        static final int TRANSACTION_removeContentProviderExternalAsUser = 110;
        static final int TRANSACTION_removeStack = 175;
        static final int TRANSACTION_removeTask = 100;
        static final int TRANSACTION_reportKillProcessEvent = 195;
        static final int TRANSACTION_requestBugReport = 125;
        static final int TRANSACTION_requestSystemServerHeapDump = 124;
        static final int TRANSACTION_requestTelephonyBugReport = 126;
        static final int TRANSACTION_requestWifiBugReport = 127;
        static final int TRANSACTION_resizeDockedStack = 174;
        static final int TRANSACTION_resizeStack = 135;
        static final int TRANSACTION_resizeTask = 157;
        static final int TRANSACTION_restart = 138;
        static final int TRANSACTION_restartUserInBackground = 186;
        static final int TRANSACTION_resumeAppSwitches = 76;
        static final int TRANSACTION_revokeUriPermission = 45;
        static final int TRANSACTION_scheduleApplicationInfoChanged = 189;
        static final int TRANSACTION_sendIdleJobTrigger = 181;
        static final int TRANSACTION_sendIntentSender = 182;
        static final int TRANSACTION_serviceDoneExecuting = 50;
        static final int TRANSACTION_setActivityController = 46;
        static final int TRANSACTION_setAgentApp = 32;
        static final int TRANSACTION_setAlwaysFinish = 33;
        static final int TRANSACTION_setDebugApp = 31;
        static final int TRANSACTION_setDummyTranslucent = 201;
        static final int TRANSACTION_setDumpHeapDebugLimit = 159;
        static final int TRANSACTION_setFocusedStack = 136;
        static final int TRANSACTION_setHasTopUi = 185;
        static final int TRANSACTION_setPackageScreenCompatMode = 98;
        static final int TRANSACTION_setPersistentVrThread = 190;
        static final int TRANSACTION_setProcessImportant = 61;
        static final int TRANSACTION_setProcessLimit = 40;
        static final int TRANSACTION_setProcessMemoryTrimLevel = 142;
        static final int TRANSACTION_setRenderThread = 184;
        static final int TRANSACTION_setRequestedOrientation = 59;
        static final int TRANSACTION_setResizeBlackList = 203;
        static final int TRANSACTION_setResizeWhiteList = 202;
        static final int TRANSACTION_setServiceForeground = 62;
        static final int TRANSACTION_setTaskResizeable = 156;
        static final int TRANSACTION_setUserIsMonkey = 131;
        static final int TRANSACTION_showBootMessage = 106;
        static final int TRANSACTION_showWaitingForDebugger = 47;
        static final int TRANSACTION_shutdown = 74;
        static final int TRANSACTION_signalPersistentProcesses = 48;
        static final int TRANSACTION_startActivity = 7;
        static final int TRANSACTION_startActivityAsUser = 119;
        static final int TRANSACTION_startActivityFromRecents = 148;
        static final int TRANSACTION_startBinderTracking = 166;
        static final int TRANSACTION_startConfirmDeviceCredentialIntent = 180;
        static final int TRANSACTION_startDelegateShellPermissionIdentity = 194;
        static final int TRANSACTION_startInstrumentation = 34;
        static final int TRANSACTION_startRecentsActivity = 146;
        static final int TRANSACTION_startService = 24;
        static final int TRANSACTION_startSystemLockTaskMode = 149;
        static final int TRANSACTION_startUserInBackground = 144;
        static final int TRANSACTION_startUserInBackgroundWithListener = 193;
        static final int TRANSACTION_startUserInForegroundWithListener = 205;
        static final int TRANSACTION_stopAppSwitches = 75;
        static final int TRANSACTION_stopBinderTrackingAndDump = 167;
        static final int TRANSACTION_stopDelegateShellPermissionIdentity = 198;
        static final int TRANSACTION_stopService = 25;
        static final int TRANSACTION_stopServiceToken = 39;
        static final int TRANSACTION_stopUser = 120;
        static final int TRANSACTION_suppressResizeConfigChanges = 169;
        static final int TRANSACTION_switchUser = 99;
        static final int TRANSACTION_unbindBackupAgent = 79;
        static final int TRANSACTION_unbindFinished = 60;
        static final int TRANSACTION_unbindService = 29;
        static final int TRANSACTION_unbroadcastIntent = 13;
        static final int TRANSACTION_unhandledBack = 8;
        static final int TRANSACTION_unlockUser = 172;
        static final int TRANSACTION_unregisterActivityObserver = 197;
        static final int TRANSACTION_unregisterIntentSenderCancelListener = 55;
        static final int TRANSACTION_unregisterMiuiAppTransitionAnimationHelper = 200;
        static final int TRANSACTION_unregisterProcessObserver = 102;
        static final int TRANSACTION_unregisterReceiver = 11;
        static final int TRANSACTION_unregisterTaskStackListener = 154;
        static final int TRANSACTION_unregisterUidObserver = 3;
        static final int TRANSACTION_unregisterUserSwitchObserver = 122;
        static final int TRANSACTION_unstableProviderDied = 115;
        static final int TRANSACTION_updateConfiguration = 38;
        static final int TRANSACTION_updateDeviceOwner = 165;
        static final int TRANSACTION_updateLockTaskPackages = 161;
        static final int TRANSACTION_updatePersistentConfiguration = 104;
        static final int TRANSACTION_updateServiceGroup = 28;
        static final int TRANSACTION_waitForNetworkStateUpdate = 191;

        private static class Proxy implements IActivityManager {
            public static IActivityManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public ParcelFileDescriptor openContentUri(String uriString) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uriString);
                    ParcelFileDescriptor parcelFileDescriptor = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().openContentUri(uriString);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerUidObserver(IUidObserver observer, int which, int cutpoint, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(which);
                    _data.writeInt(cutpoint);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerUidObserver(observer, which, cutpoint, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterUidObserver(IUidObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterUidObserver(observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUidActive(int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUidActive(uid, callingPackage);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getUidProcessState(int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUidProcessState(uid, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void handleApplicationCrash(IBinder app, ParcelableCrashInfo crashInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app);
                    if (crashInfo != null) {
                        _data.writeInt(1);
                        crashInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().handleApplicationCrash(app, crashInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
                Throwable th;
                Intent intent2 = intent;
                ProfilerInfo profilerInfo2 = profilerInfo;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                Parcel _reply2;
                try {
                    IBinder asBinder;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th2) {
                            th = th2;
                            _reply2 = _reply;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data.writeStrongBinder(asBinder);
                    _data.writeString(callingPackage);
                    if (intent2 != null) {
                        _data.writeInt(1);
                        intent2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(resultTo);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    _data.writeInt(flags);
                    if (profilerInfo2 != null) {
                        _data.writeInt(1);
                        profilerInfo2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply2 = _reply;
                        _reply2.readException();
                        int _result = _reply2.readInt();
                        _reply2.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _reply2 = _reply;
                    try {
                        int startActivity = Stub.getDefaultImpl().startActivity(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options);
                        _reply2.recycle();
                        _data.recycle();
                        return startActivity;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply2.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply2 = _reply;
                    _reply2.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void unhandledBack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unhandledBack();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean finishActivity(IBinder token, int code, Intent data, int finishTask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(code);
                    boolean _result = true;
                    if (data != null) {
                        _data.writeInt(1);
                        data.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(finishTask);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().finishActivity(token, code, data, finishTask);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Intent registerReceiver(IApplicationThread caller, String callerPackage, IIntentReceiver receiver, IntentFilter filter, String requiredPermission, int userId, int flags) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                IntentFilter intentFilter = filter;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callerPackage);
                        if (receiver != null) {
                            iBinder = receiver.asBinder();
                        }
                        _data.writeStrongBinder(iBinder);
                        if (intentFilter != null) {
                            _data.writeInt(1);
                            intentFilter.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        str = requiredPermission;
                        i = userId;
                        i2 = flags;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(requiredPermission);
                        try {
                            _data.writeInt(userId);
                        } catch (Throwable th3) {
                            th = th3;
                            i2 = flags;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(flags);
                            Intent _result;
                            if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    _result = (Intent) Intent.CREATOR.createFromParcel(_reply);
                                } else {
                                    _result = null;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().registerReceiver(caller, callerPackage, receiver, filter, requiredPermission, userId, flags);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i = userId;
                        i2 = flags;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = callerPackage;
                    str = requiredPermission;
                    i = userId;
                    i2 = flags;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void unregisterReceiver(IIntentReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterReceiver(receiver);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int broadcastIntent(IApplicationThread caller, Intent intent, String resolvedType, IIntentReceiver resultTo, int resultCode, String resultData, Bundle map, String[] requiredPermissions, int appOp, Bundle options, boolean serialized, boolean sticky, int userId) throws RemoteException {
                IBinder asBinder;
                Throwable th;
                Parcel _reply;
                Parcel _data;
                Intent intent2 = intent;
                Bundle bundle = map;
                Bundle bundle2 = options;
                Parcel _data2 = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                _data2.writeInterfaceToken(Stub.DESCRIPTOR);
                IBinder iBinder = null;
                if (caller != null) {
                    try {
                        asBinder = caller.asBinder();
                    } catch (Throwable th2) {
                        th = th2;
                        _reply = _reply2;
                        _data = _data2;
                    }
                } else {
                    asBinder = null;
                }
                try {
                    _data2.writeStrongBinder(asBinder);
                    int i = 1;
                    if (intent2 != null) {
                        _data2.writeInt(1);
                        intent2.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeString(resolvedType);
                    if (resultTo != null) {
                        iBinder = resultTo.asBinder();
                    }
                    _data2.writeStrongBinder(iBinder);
                    _data2.writeInt(resultCode);
                    _data2.writeString(resultData);
                    if (bundle != null) {
                        _data2.writeInt(1);
                        bundle.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeStringArray(requiredPermissions);
                    _data2.writeInt(appOp);
                    if (bundle2 != null) {
                        _data2.writeInt(1);
                        bundle2.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeInt(serialized ? 1 : 0);
                    if (!sticky) {
                        i = 0;
                    }
                    _data2.writeInt(i);
                    _data2.writeInt(userId);
                    if (this.mRemote.transact(12, _data2, _reply2, 0) || Stub.getDefaultImpl() == null) {
                        _reply = _reply2;
                        _data = _data2;
                        _reply.readException();
                        int _result = _reply.readInt();
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _reply = _reply2;
                    _data = _data2;
                    try {
                        i = Stub.getDefaultImpl().broadcastIntent(caller, intent, resolvedType, resultTo, resultCode, resultData, map, requiredPermissions, appOp, options, serialized, sticky, userId);
                        _reply.recycle();
                        _data.recycle();
                        return i;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply = _reply2;
                    _data = _data2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void unbroadcastIntent(IApplicationThread caller, Intent intent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unbroadcastIntent(caller, intent, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishReceiver(IBinder who, int resultCode, String resultData, Bundle map, boolean abortBroadcast, int flags) throws RemoteException {
                Throwable th;
                int i;
                String str;
                int i2;
                Bundle bundle = map;
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeStrongBinder(who);
                    } catch (Throwable th2) {
                        th = th2;
                        i = resultCode;
                        str = resultData;
                        i2 = flags;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(resultCode);
                        try {
                            _data.writeString(resultData);
                            int i3 = 0;
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (abortBroadcast) {
                                i3 = 1;
                            }
                            _data.writeInt(i3);
                        } catch (Throwable th3) {
                            th = th3;
                            i2 = flags;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        str = resultData;
                        i2 = flags;
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(flags);
                        try {
                            if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().finishReceiver(who, resultCode, resultData, map, abortBroadcast, flags);
                            _data.recycle();
                        } catch (Throwable th5) {
                            th = th5;
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    IBinder iBinder = who;
                    i = resultCode;
                    str = resultData;
                    i2 = flags;
                    _data.recycle();
                    throw th;
                }
            }

            public void attachApplication(IApplicationThread app, long startSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    _data.writeLong(startSeq);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().attachApplication(app, startSeq);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<RunningTaskInfo> getTasks(int maxNum) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    List<RunningTaskInfo> list = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getTasks(maxNum);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(RunningTaskInfo.CREATOR);
                    List<RunningTaskInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<RunningTaskInfo> getFilteredTasks(int maxNum, int ignoreActivityType, int ignoreWindowingMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(ignoreActivityType);
                    _data.writeInt(ignoreWindowingMode);
                    List<RunningTaskInfo> list = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getFilteredTasks(maxNum, ignoreActivityType, ignoreWindowingMode);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(RunningTaskInfo.CREATOR);
                    List<RunningTaskInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void moveTaskToFront(IApplicationThread caller, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeInt(task);
                    _data.writeInt(flags);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveTaskToFront(caller, callingPackage, task, flags, options);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(onlyRoot ? 1 : 0);
                    int i = this.mRemote;
                    if (!i.transact(19, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getTaskForActivity(token, onlyRoot);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ContentProviderHolder getContentProvider(IApplicationThread caller, String callingPackage, String name, int userId, boolean stable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeInt(stable ? 1 : 0);
                    ContentProviderHolder contentProviderHolder = this.mRemote;
                    if (!contentProviderHolder.transact(20, _data, _reply, 0)) {
                        contentProviderHolder = Stub.getDefaultImpl();
                        if (contentProviderHolder != null) {
                            contentProviderHolder = Stub.getDefaultImpl().getContentProvider(caller, callingPackage, name, userId, stable);
                            return contentProviderHolder;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        contentProviderHolder = (ContentProviderHolder) ContentProviderHolder.CREATOR.createFromParcel(_reply);
                    } else {
                        contentProviderHolder = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return contentProviderHolder;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void publishContentProviders(IApplicationThread caller, List<ContentProviderHolder> providers) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeTypedList(providers);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().publishContentProviders(caller, providers);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean refContentProvider(IBinder connection, int stableDelta, int unstableDelta) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    _data.writeInt(stableDelta);
                    _data.writeInt(unstableDelta);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().refContentProvider(connection, stableDelta, unstableDelta);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public PendingIntent getRunningServiceControlPanel(ComponentName service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    PendingIntent pendingIntent = this.mRemote;
                    if (!pendingIntent.transact(23, _data, _reply, 0)) {
                        pendingIntent = Stub.getDefaultImpl();
                        if (pendingIntent != null) {
                            pendingIntent = Stub.getDefaultImpl().getRunningServiceControlPanel(service);
                            return pendingIntent;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        pendingIntent = (PendingIntent) PendingIntent.CREATOR.createFromParcel(_reply);
                    } else {
                        pendingIntent = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return pendingIntent;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName startService(IApplicationThread caller, Intent service, String resolvedType, boolean requireForeground, String callingPackage, int userId) throws RemoteException {
                Throwable th;
                int i;
                String str;
                Intent intent = service;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    int i2 = 1;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(resolvedType);
                        if (!requireForeground) {
                            i2 = 0;
                        }
                        _data.writeInt(i2);
                        try {
                            _data.writeString(callingPackage);
                        } catch (Throwable th2) {
                            th = th2;
                            i = userId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(userId);
                            try {
                                ComponentName _result;
                                if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    if (_reply.readInt() != 0) {
                                        _result = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                                    } else {
                                        _result = null;
                                    }
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                }
                                _result = Stub.getDefaultImpl().startService(caller, service, resolvedType, requireForeground, callingPackage, userId);
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            } catch (Throwable th3) {
                                th = th3;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        str = callingPackage;
                        i = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = resolvedType;
                    str = callingPackage;
                    i = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int stopService(IApplicationThread caller, Intent service, String resolvedType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(userId);
                    int i = this.mRemote;
                    if (!i.transact(25, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().stopService(caller, service, resolvedType, userId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int bindService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String callingPackage, int userId) throws RemoteException {
                Throwable th;
                String str;
                int i;
                Intent intent = service;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeStrongBinder(token);
                        if (intent != null) {
                            _data.writeInt(1);
                            intent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        str = resolvedType;
                        i = flags;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(resolvedType);
                        if (connection != null) {
                            iBinder = connection.asBinder();
                        }
                        _data.writeStrongBinder(iBinder);
                        try {
                            _data.writeInt(flags);
                            _data.writeString(callingPackage);
                            _data.writeInt(userId);
                            if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int bindService = Stub.getDefaultImpl().bindService(caller, token, service, resolvedType, connection, flags, callingPackage, userId);
                            _reply.recycle();
                            _data.recycle();
                            return bindService;
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i = flags;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    IBinder iBinder2 = token;
                    str = resolvedType;
                    i = flags;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int bindIsolatedService(IApplicationThread caller, IBinder token, Intent service, String resolvedType, IServiceConnection connection, int flags, String instanceName, String callingPackage, int userId) throws RemoteException {
                Throwable th;
                String str;
                Intent intent = service;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeStrongBinder(token);
                        if (intent != null) {
                            _data.writeInt(1);
                            intent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        str = resolvedType;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(resolvedType);
                        if (connection != null) {
                            iBinder = connection.asBinder();
                        }
                        _data.writeStrongBinder(iBinder);
                        _data.writeInt(flags);
                        _data.writeString(instanceName);
                        _data.writeString(callingPackage);
                        _data.writeInt(userId);
                        if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            int _result = _reply.readInt();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        int bindIsolatedService = Stub.getDefaultImpl().bindIsolatedService(caller, token, service, resolvedType, connection, flags, instanceName, callingPackage, userId);
                        _reply.recycle();
                        _data.recycle();
                        return bindIsolatedService;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    IBinder iBinder2 = token;
                    str = resolvedType;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void updateServiceGroup(IServiceConnection connection, int group, int importance) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    _data.writeInt(group);
                    _data.writeInt(importance);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateServiceGroup(connection, group, importance);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unbindService(IServiceConnection connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection != null ? connection.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().unbindService(connection);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void publishService(IBinder token, Intent intent, IBinder service) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(service);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().publishService(token, intent, service);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDebugApp(String packageName, boolean waitForDebugger, boolean persistent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 1;
                    _data.writeInt(waitForDebugger ? 1 : 0);
                    if (!persistent) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDebugApp(packageName, waitForDebugger, persistent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAgentApp(String packageName, String agent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(agent);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAgentApp(packageName, agent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAlwaysFinish(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAlwaysFinish(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startInstrumentation(ComponentName className, String profileFile, int flags, Bundle arguments, IInstrumentationWatcher watcher, IUiAutomationConnection connection, int userId, String abiOverride) throws RemoteException {
                Throwable th;
                int i;
                ComponentName componentName = className;
                Bundle bundle = arguments;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeString(profileFile);
                        try {
                            _data.writeInt(flags);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            IBinder iBinder = null;
                            _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                            if (connection != null) {
                                iBinder = connection.asBinder();
                            }
                            _data.writeStrongBinder(iBinder);
                            _data.writeInt(userId);
                            _data.writeString(abiOverride);
                            if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().startInstrumentation(className, profileFile, flags, arguments, watcher, connection, userId, abiOverride);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i = flags;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String str = profileFile;
                    i = flags;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void addInstrumentationResults(IApplicationThread target, Bundle results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    if (results != null) {
                        _data.writeInt(1);
                        results.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addInstrumentationResults(target, results);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishInstrumentation(IApplicationThread target, int resultCode, Bundle results) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    _data.writeInt(resultCode);
                    if (results != null) {
                        _data.writeInt(1);
                        results.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishInstrumentation(target, resultCode, results);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Configuration getConfiguration() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Configuration configuration = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        configuration = Stub.getDefaultImpl();
                        if (configuration != 0) {
                            configuration = Stub.getDefaultImpl().getConfiguration();
                            return configuration;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        configuration = (Configuration) Configuration.CREATOR.createFromParcel(_reply);
                    } else {
                        configuration = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return configuration;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean updateConfiguration(Configuration values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (values != null) {
                        _data.writeInt(1);
                        values.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().updateConfiguration(values);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stopServiceToken(ComponentName className, IBinder token, int startId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(token);
                    _data.writeInt(startId);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().stopServiceToken(className, token, startId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setProcessLimit(int max) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(max);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setProcessLimit(max);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getProcessLimit() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 41;
                    if (!this.mRemote.transact(41, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getProcessLimit();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkPermission(String permission, int pid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    int i = 42;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkPermission(permission, pid, uid);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkUriPermission(Uri uri, int pid, int uid, int mode, int userId, IBinder callerToken) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                IBinder iBinder;
                Uri uri2 = uri;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri2 != null) {
                        _data.writeInt(1);
                        uri2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(pid);
                    } catch (Throwable th2) {
                        th = th2;
                        i = uid;
                        i2 = mode;
                        i3 = userId;
                        iBinder = callerToken;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(uid);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = mode;
                        i3 = userId;
                        iBinder = callerToken;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(mode);
                        try {
                            _data.writeInt(userId);
                        } catch (Throwable th4) {
                            th = th4;
                            iBinder = callerToken;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeStrongBinder(callerToken);
                            if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int checkUriPermission = Stub.getDefaultImpl().checkUriPermission(uri, pid, uid, mode, userId, callerToken);
                            _reply.recycle();
                            _data.recycle();
                            return checkUriPermission;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i3 = userId;
                        iBinder = callerToken;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i4 = pid;
                    i = uid;
                    i2 = mode;
                    i3 = userId;
                    iBinder = callerToken;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void grantUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(targetPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantUriPermission(caller, targetPkg, uri, mode, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void revokeUriPermission(IApplicationThread caller, String targetPkg, Uri uri, int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    _data.writeString(targetPkg);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().revokeUriPermission(caller, targetPkg, uri, mode, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    _data.writeInt(imAMonkey ? 1 : 0);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setActivityController(watcher, imAMonkey);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showWaitingForDebugger(IApplicationThread who, boolean waiting) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who != null ? who.asBinder() : null);
                    _data.writeInt(waiting ? 1 : 0);
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showWaitingForDebugger(who, waiting);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void signalPersistentProcesses(int signal) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(signal);
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().signalPersistentProcesses(signal);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 49;
                    if (!this.mRemote.transact(49, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getRecentTasks(maxNum, flags, userId);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void serviceDoneExecuting(IBinder token, int type, int startId, int res) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(type);
                    _data.writeInt(startId);
                    _data.writeInt(res);
                    if (this.mRemote.transact(50, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().serviceDoneExecuting(token, type, startId, res);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public IIntentSender getIntentSender(int type, String packageName, IBinder token, String resultWho, int requestCode, Intent[] intents, String[] resolvedTypes, int flags, Bundle options, int userId) throws RemoteException {
                Throwable th;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(type);
                        _data.writeString(packageName);
                        _data.writeStrongBinder(token);
                        _data.writeString(resultWho);
                        _data.writeInt(requestCode);
                        _data.writeTypedArray(intents, 0);
                        _data.writeStringArray(resolvedTypes);
                        _data.writeInt(flags);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(userId);
                        if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            IIntentSender _result = android.content.IIntentSender.Stub.asInterface(_reply.readStrongBinder());
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        IIntentSender intentSender = Stub.getDefaultImpl().getIntentSender(type, packageName, token, resultWho, requestCode, intents, resolvedTypes, flags, options, userId);
                        _reply.recycle();
                        _data.recycle();
                        return intentSender;
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    int i = type;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void cancelIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (this.mRemote.transact(52, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelIntentSender(sender);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPackageForIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    String str = 53;
                    if (!this.mRemote.transact(53, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPackageForIntentSender(sender);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (receiver != null) {
                        iBinder = receiver.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerIntentSenderCancelListener(sender, receiver);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterIntentSenderCancelListener(IIntentSender sender, IResultReceiver receiver) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (receiver != null) {
                        iBinder = receiver.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterIntentSenderCancelListener(sender, receiver);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enterSafeMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enterSafeMode();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteWakeupAlarm(IIntentSender sender, WorkSource workSource, int sourceUid, String sourcePkg, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceUid);
                    _data.writeString(sourcePkg);
                    _data.writeString(tag);
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteWakeupAlarm(sender, workSource, sourceUid, sourcePkg, tag);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeContentProvider(IBinder connection, boolean stable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    _data.writeInt(stable ? 1 : 0);
                    if (this.mRemote.transact(58, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeContentProvider(connection, stable);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(requestedOrientation);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRequestedOrientation(token, requestedOrientation);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unbindFinished(IBinder token, Intent service, boolean doRebind) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    int i = 1;
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!doRebind) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unbindFinished(token, service, doRebind);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setProcessImportant(IBinder token, int pid, boolean isForeground, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(pid);
                    _data.writeInt(isForeground ? 1 : 0);
                    _data.writeString(reason);
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setProcessImportant(token, pid, isForeground, reason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setServiceForeground(ComponentName className, IBinder token, int id, Notification notification, int flags, int foregroundServiceType) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                int i3;
                ComponentName componentName = className;
                Notification notification2 = notification;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeStrongBinder(token);
                        try {
                            _data.writeInt(id);
                            if (notification2 != null) {
                                _data.writeInt(1);
                                notification2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeInt(flags);
                            } catch (Throwable th2) {
                                th = th2;
                                i = foregroundServiceType;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            i2 = flags;
                            i = foregroundServiceType;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(foregroundServiceType);
                            if (this.mRemote.transact(62, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().setServiceForeground(className, token, id, notification, flags, foregroundServiceType);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i3 = id;
                        i2 = flags;
                        i = foregroundServiceType;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    IBinder iBinder = token;
                    i3 = id;
                    i2 = flags;
                    i = foregroundServiceType;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int getForegroundServiceType(ComponentName className, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (className != null) {
                        _data.writeInt(1);
                        className.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(token);
                    int i = this.mRemote;
                    if (!i.transact(63, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getForegroundServiceType(className, token);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _result = true;
                    _data.writeInt(nonRoot ? 1 : 0);
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().moveActivityTaskToBack(token, nonRoot);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getMemoryInfo(MemoryInfo outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            outInfo.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getMemoryInfo(outInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<ProcessErrorStateInfo> list = 66;
                    if (!this.mRemote.transact(66, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getProcessesInErrorState();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ProcessErrorStateInfo.CREATOR);
                    List<ProcessErrorStateInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean clearApplicationUserData(String packageName, boolean keepState, IPackageDataObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(keepState ? 1 : 0);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(67, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().clearApplicationUserData(packageName, keepState, observer, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forceStopPackage(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(68, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forceStopPackage(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean killPids(int[] pids, String reason, boolean secure) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    _data.writeString(reason);
                    boolean _result = true;
                    _data.writeInt(secure ? 1 : 0);
                    if (this.mRemote.transact(69, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().killPids(pids, reason, secure);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<RunningServiceInfo> getServices(int maxNum, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(maxNum);
                    _data.writeInt(flags);
                    List<RunningServiceInfo> list = 70;
                    if (!this.mRemote.transact(70, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getServices(maxNum, flags);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(RunningServiceInfo.CREATOR);
                    List<RunningServiceInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<RunningAppProcessInfo> list = 71;
                    if (!this.mRemote.transact(71, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getRunningAppProcesses();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(RunningAppProcessInfo.CREATOR);
                    List<RunningAppProcessInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IBinder peekService(Intent service, String resolvedType, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (service != null) {
                        _data.writeInt(1);
                        service.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeString(callingPackage);
                    IBinder iBinder = this.mRemote;
                    if (!iBinder.transact(72, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != null) {
                            iBinder = Stub.getDefaultImpl().peekService(service, resolvedType, callingPackage);
                            return iBinder;
                        }
                    }
                    _reply.readException();
                    iBinder = _reply.readStrongBinder();
                    IBinder _result = iBinder;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean profileControl(String process, int userId, boolean start, ProfilerInfo profilerInfo, int profileType) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                ProfilerInfo profilerInfo2 = profilerInfo;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    boolean _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(process);
                        try {
                            _data.writeInt(userId);
                            _result = true;
                            _data.writeInt(start ? 1 : 0);
                            if (profilerInfo2 != null) {
                                _data.writeInt(1);
                                profilerInfo2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            i = profileType;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = userId;
                        i = profileType;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(profileType);
                        try {
                            if (this.mRemote.transact(73, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().profileControl(process, userId, start, profilerInfo, profileType);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = process;
                    i2 = userId;
                    i = profileType;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean shutdown(int timeout) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(timeout);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(74, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shutdown(timeout);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(75, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopAppSwitches();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resumeAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(76, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resumeAppSwitches();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean bindBackupAgent(String packageName, int backupRestoreMode, int targetUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(backupRestoreMode);
                    _data.writeInt(targetUserId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(77, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().bindBackupAgent(packageName, backupRestoreMode, targetUserId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void backupAgentCreated(String packageName, IBinder agent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeStrongBinder(agent);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(78, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().backupAgentCreated(packageName, agent, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unbindBackupAgent(ApplicationInfo appInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appInfo != null) {
                        _data.writeInt(1);
                        appInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(79, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unbindBackupAgent(appInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getUidForIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    int i = 80;
                    if (!this.mRemote.transact(80, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUidForIntentSender(sender);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, String name, String callerPackage) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                String str2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(callingPid);
                    } catch (Throwable th2) {
                        th = th2;
                        i = callingUid;
                        i2 = userId;
                        str = name;
                        str2 = callerPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(callingUid);
                    } catch (Throwable th3) {
                        th = th3;
                        i2 = userId;
                        str = name;
                        str2 = callerPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        int i3 = 1;
                        _data.writeInt(allowAll ? 1 : 0);
                        if (!requireFull) {
                            i3 = 0;
                        }
                        _data.writeInt(i3);
                        try {
                            _data.writeString(name);
                        } catch (Throwable th4) {
                            th = th4;
                            str2 = callerPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(callerPackage);
                            if (this.mRemote.transact(81, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                i3 = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return i3;
                            }
                            int handleIncomingUser = Stub.getDefaultImpl().handleIncomingUser(callingPid, callingUid, userId, allowAll, requireFull, name, callerPackage);
                            _reply.recycle();
                            _data.recycle();
                            return handleIncomingUser;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        str = name;
                        str2 = callerPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    int i4 = callingPid;
                    i = callingUid;
                    i2 = userId;
                    str = name;
                    str2 = callerPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void addPackageDependency(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(82, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPackageDependency(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void killApplication(String pkg, int appId, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    if (this.mRemote.transact(83, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().killApplication(pkg, appId, userId, reason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeSystemDialogs(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    if (this.mRemote.transact(84, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().closeSystemDialogs(reason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    Debug.MemoryInfo[] memoryInfoArr = 85;
                    if (!this.mRemote.transact(85, _data, _reply, 0)) {
                        memoryInfoArr = Stub.getDefaultImpl();
                        if (memoryInfoArr != 0) {
                            memoryInfoArr = Stub.getDefaultImpl().getProcessMemoryInfo(pids);
                            return memoryInfoArr;
                        }
                    }
                    _reply.readException();
                    Debug.MemoryInfo[] _result = (Debug.MemoryInfo[]) _reply.createTypedArray(Debug.MemoryInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void killApplicationProcess(String processName, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(86, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().killApplicationProcess(processName, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean handleApplicationWtf(IBinder app, String tag, boolean system, ParcelableCrashInfo crashInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app);
                    _data.writeString(tag);
                    boolean _result = true;
                    _data.writeInt(system ? 1 : 0);
                    if (crashInfo != null) {
                        _data.writeInt(1);
                        crashInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(87, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().handleApplicationWtf(app, tag, system, crashInfo);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void killBackgroundProcesses(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(88, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().killBackgroundProcesses(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUserAMonkey() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(89, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUserAMonkey();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ApplicationInfo> getRunningExternalApplications() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<ApplicationInfo> list = 90;
                    if (!this.mRemote.transact(90, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getRunningExternalApplications();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ApplicationInfo.CREATOR);
                    List<ApplicationInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishHeavyWeightApp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(91, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishHeavyWeightApp();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void handleApplicationStrictModeViolation(IBinder app, int penaltyMask, ViolationInfo crashInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app);
                    _data.writeInt(penaltyMask);
                    if (crashInfo != null) {
                        _data.writeInt(1);
                        crashInfo.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(92, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().handleApplicationStrictModeViolation(app, penaltyMask, crashInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTopActivityImmersive() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(93, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTopActivityImmersive();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void crashApplication(int uid, int initialPid, String packageName, int userId, String message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(initialPid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(message);
                    if (this.mRemote.transact(94, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().crashApplication(uid, initialPid, packageName, userId, message);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getProviderMimeType(Uri uri, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    String str = this.mRemote;
                    if (!str.transact(95, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != null) {
                            str = Stub.getDefaultImpl().getProviderMimeType(uri, userId);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean dumpHeap(String process, int userId, boolean managed, boolean mallocInfo, boolean runGc, String path, ParcelFileDescriptor fd, RemoteCallback finishCallback) throws RemoteException {
                Throwable th;
                int i;
                ParcelFileDescriptor parcelFileDescriptor = fd;
                RemoteCallback remoteCallback = finishCallback;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(process);
                    } catch (Throwable th2) {
                        th = th2;
                        i = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        boolean _result = true;
                        _data.writeInt(managed ? 1 : 0);
                        _data.writeInt(mallocInfo ? 1 : 0);
                        _data.writeInt(runGc ? 1 : 0);
                        _data.writeString(path);
                        if (parcelFileDescriptor != null) {
                            _data.writeInt(1);
                            parcelFileDescriptor.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (remoteCallback != null) {
                            _data.writeInt(1);
                            remoteCallback.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (this.mRemote.transact(96, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() == 0) {
                                _result = false;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().dumpHeap(process, userId, managed, mallocInfo, runGc, path, fd, finishCallback);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String str = process;
                    i = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean isUserRunning(int userid, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeInt(flags);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(97, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUserRunning(userid, flags);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(98, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPackageScreenCompatMode(packageName, mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean switchUser(int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(99, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().switchUser(userid);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(100, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().removeTask(taskId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerProcessObserver(IProcessObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(101, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerProcessObserver(observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterProcessObserver(IProcessObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(102, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterProcessObserver(observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isIntentSenderTargetedToPackage(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(103, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isIntentSenderTargetedToPackage(sender);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updatePersistentConfiguration(Configuration values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (values != null) {
                        _data.writeInt(1);
                        values.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(104, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updatePersistentConfiguration(values);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long[] getProcessPss(int[] pids) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(pids);
                    long[] jArr = 105;
                    if (!this.mRemote.transact(105, _data, _reply, 0)) {
                        jArr = Stub.getDefaultImpl();
                        if (jArr != 0) {
                            jArr = Stub.getDefaultImpl().getProcessPss(pids);
                            return jArr;
                        }
                    }
                    _reply.readException();
                    jArr = _reply.createLongArray();
                    long[] _result = jArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void showBootMessage(CharSequence msg, boolean always) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (msg != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(msg, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!always) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(106, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().showBootMessage(msg, always);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void killAllBackgroundProcesses() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(107, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().killAllBackgroundProcesses();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ContentProviderHolder getContentProviderExternal(String name, int userId, IBinder token, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeInt(userId);
                    _data.writeStrongBinder(token);
                    _data.writeString(tag);
                    ContentProviderHolder contentProviderHolder = 108;
                    if (!this.mRemote.transact(108, _data, _reply, 0)) {
                        contentProviderHolder = Stub.getDefaultImpl();
                        if (contentProviderHolder != 0) {
                            contentProviderHolder = Stub.getDefaultImpl().getContentProviderExternal(name, userId, token, tag);
                            return contentProviderHolder;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        contentProviderHolder = (ContentProviderHolder) ContentProviderHolder.CREATOR.createFromParcel(_reply);
                    } else {
                        contentProviderHolder = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return contentProviderHolder;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeContentProviderExternal(String name, IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(109, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeContentProviderExternal(name, token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeContentProviderExternalAsUser(String name, IBinder token, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(name);
                    _data.writeStrongBinder(token);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(110, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeContentProviderExternalAsUser(name, token, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void getMyMemoryState(RunningAppProcessInfo outInfo) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(111, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            outInfo.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getMyMemoryState(outInfo);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean killProcessesBelowForeground(String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(reason);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(112, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().killProcessesBelowForeground(reason);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UserInfo getCurrentUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    UserInfo userInfo = 113;
                    if (!this.mRemote.transact(113, _data, _reply, 0)) {
                        userInfo = Stub.getDefaultImpl();
                        if (userInfo != 0) {
                            userInfo = Stub.getDefaultImpl().getCurrentUser();
                            return userInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        userInfo = (UserInfo) UserInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        userInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return userInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    int i = 114;
                    if (!this.mRemote.transact(114, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLaunchedFromUid(activityToken);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unstableProviderDied(IBinder connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    if (this.mRemote.transact(115, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unstableProviderDied(connection);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isIntentSenderAnActivity(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(116, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isIntentSenderAnActivity(sender);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isIntentSenderAForegroundService(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(117, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isIntentSenderAForegroundService(sender);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isIntentSenderABroadcast(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(118, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isIntentSenderABroadcast(sender);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startActivityAsUser(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Throwable th;
                Intent intent2 = intent;
                ProfilerInfo profilerInfo2 = profilerInfo;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                Parcel _reply2;
                Parcel _data2;
                try {
                    IBinder asBinder;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th2) {
                            th = th2;
                            _reply2 = _reply;
                            _data2 = _data;
                        }
                    } else {
                        asBinder = null;
                    }
                    _data.writeStrongBinder(asBinder);
                    _data.writeString(callingPackage);
                    if (intent2 != null) {
                        _data.writeInt(1);
                        intent2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(resultTo);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    _data.writeInt(flags);
                    if (profilerInfo2 != null) {
                        _data.writeInt(1);
                        profilerInfo2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(119, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply2 = _reply;
                        _data2 = _data;
                        _reply2.readException();
                        int _result = _reply2.readInt();
                        _reply2.recycle();
                        _data2.recycle();
                        return _result;
                    }
                    _reply2 = _reply;
                    _data2 = _data;
                    try {
                        int startActivityAsUser = Stub.getDefaultImpl().startActivityAsUser(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, userId);
                        _reply2.recycle();
                        _data2.recycle();
                        return startActivityAsUser;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply2.recycle();
                        _data2.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply2 = _reply;
                    _data2 = _data;
                    _reply2.recycle();
                    _data2.recycle();
                    throw th;
                }
            }

            public int stopUser(int userid, boolean force, IStopUserCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    int i = this.mRemote;
                    if (!i.transact(120, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().stopUser(userid, force, callback);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerUserSwitchObserver(IUserSwitchObserver observer, String name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeString(name);
                    if (this.mRemote.transact(121, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerUserSwitchObserver(observer, name);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterUserSwitchObserver(IUserSwitchObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(122, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterUserSwitchObserver(observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int[] getRunningUserIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int[] iArr = 123;
                    if (!this.mRemote.transact(123, _data, _reply, 0)) {
                        iArr = Stub.getDefaultImpl();
                        if (iArr != 0) {
                            iArr = Stub.getDefaultImpl().getRunningUserIds();
                            return iArr;
                        }
                    }
                    _reply.readException();
                    iArr = _reply.createIntArray();
                    int[] _result = iArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestSystemServerHeapDump() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(124, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestSystemServerHeapDump();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestBugReport(int bugreportType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(bugreportType);
                    if (this.mRemote.transact(125, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestBugReport(bugreportType);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestTelephonyBugReport(String shareTitle, String shareDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(shareTitle);
                    _data.writeString(shareDescription);
                    if (this.mRemote.transact(126, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestTelephonyBugReport(shareTitle, shareDescription);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestWifiBugReport(String shareTitle, String shareDescription) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(shareTitle);
                    _data.writeString(shareDescription);
                    if (this.mRemote.transact(127, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestWifiBugReport(shareTitle, shareDescription);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Intent getIntentForIntentSender(IIntentSender sender) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    Intent intent = 128;
                    if (!this.mRemote.transact(128, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().getIntentForIntentSender(sender);
                            return intent;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        intent = (Intent) Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        intent = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return intent;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    String str = 129;
                    if (!this.mRemote.transact(129, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getLaunchedFromPackage(activityToken);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void killUid(int appId, int userId, String reason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeInt(userId);
                    _data.writeString(reason);
                    if (this.mRemote.transact(130, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().killUid(appId, userId, reason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUserIsMonkey(boolean monkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(monkey ? 1 : 0);
                    if (this.mRemote.transact(131, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUserIsMonkey(monkey);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void hang(IBinder who, boolean allowRestart) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(who);
                    _data.writeInt(allowRestart ? 1 : 0);
                    if (this.mRemote.transact(132, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().hang(who, allowRestart);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<StackInfo> getAllStackInfos() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<StackInfo> list = 133;
                    if (!this.mRemote.transact(133, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAllStackInfos();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(StackInfo.CREATOR);
                    List<StackInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    _data.writeInt(toTop ? 1 : 0);
                    if (this.mRemote.transact(134, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveTaskToStack(taskId, stackId, toTop);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resizeStack(int stackId, Rect bounds, boolean allowResizeInDockedMode, boolean preserveWindows, boolean animate, int animationDuration) throws RemoteException {
                Throwable th;
                int i;
                Rect rect = bounds;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                int i2;
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    i2 = stackId;
                    try {
                        _data.writeInt(stackId);
                        int i3 = 1;
                        if (rect != null) {
                            _data.writeInt(1);
                            rect.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(allowResizeInDockedMode ? 1 : 0);
                        _data.writeInt(preserveWindows ? 1 : 0);
                        if (!animate) {
                            i3 = 0;
                        }
                        _data.writeInt(i3);
                        try {
                            _data.writeInt(animationDuration);
                            try {
                                if (this.mRemote.transact(135, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    _reply.recycle();
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().resizeStack(stackId, bounds, allowResizeInDockedMode, preserveWindows, animate, animationDuration);
                                _reply.recycle();
                                _data.recycle();
                            } catch (Throwable th2) {
                                th = th2;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i = animationDuration;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    i2 = stackId;
                    i = animationDuration;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void setFocusedStack(int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    if (this.mRemote.transact(136, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFocusedStack(stackId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StackInfo getFocusedStackInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    StackInfo stackInfo = 137;
                    if (!this.mRemote.transact(137, _data, _reply, 0)) {
                        stackInfo = Stub.getDefaultImpl();
                        if (stackInfo != 0) {
                            stackInfo = Stub.getDefaultImpl().getFocusedStackInfo();
                            return stackInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        stackInfo = (StackInfo) StackInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        stackInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return stackInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(138, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restart();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void performIdleMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(139, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().performIdleMaintenance();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void appNotRespondingViaProvider(IBinder connection) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(connection);
                    if (this.mRemote.transact(140, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().appNotRespondingViaProvider(connection);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Rect getTaskBounds(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    Rect rect = 141;
                    if (!this.mRemote.transact(141, _data, _reply, 0)) {
                        rect = Stub.getDefaultImpl();
                        if (rect != 0) {
                            rect = Stub.getDefaultImpl().getTaskBounds(taskId);
                            return rect;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rect = (Rect) Rect.CREATOR.createFromParcel(_reply);
                    } else {
                        rect = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rect;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean setProcessMemoryTrimLevel(String process, int uid, int level) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(process);
                    _data.writeInt(uid);
                    _data.writeInt(level);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(142, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().setProcessMemoryTrimLevel(process, uid, level);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getTagForIntentSender(IIntentSender sender, String prefix) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    _data.writeString(prefix);
                    String str = 143;
                    if (!this.mRemote.transact(143, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getTagForIntentSender(sender, prefix);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startUserInBackground(int userid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(144, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().startUserInBackground(userid);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isInLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(145, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInLockTaskMode();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startRecentsActivity(Intent intent, IAssistDataReceiver assistDataReceiver, IRecentsAnimationRunner recentsAnimationRunner) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    IBinder iBinder = null;
                    _data.writeStrongBinder(assistDataReceiver != null ? assistDataReceiver.asBinder() : null);
                    if (recentsAnimationRunner != null) {
                        iBinder = recentsAnimationRunner.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    if (this.mRemote.transact(146, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startRecentsActivity(intent, assistDataReceiver, recentsAnimationRunner);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelRecentsAnimation(boolean restoreHomeStackPosition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(restoreHomeStackPosition ? 1 : 0);
                    if (this.mRemote.transact(147, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelRecentsAnimation(restoreHomeStackPosition);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(148, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().startActivityFromRecents(taskId, options);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startSystemLockTaskMode(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (this.mRemote.transact(149, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startSystemLockTaskMode(taskId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTopOfTask(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(150, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTopOfTask(token);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void bootAnimationComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(151, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().bootAnimationComplete();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int checkPermissionWithToken(String permission, int pid, int uid, IBinder callerToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(permission);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeStrongBinder(callerToken);
                    int i = 152;
                    if (!this.mRemote.transact(152, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().checkPermissionWithToken(permission, pid, uid, callerToken);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(153, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerTaskStackListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(154, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterTaskStackListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyCleartextNetwork(int uid, byte[] firstPacket) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeByteArray(firstPacket);
                    if (this.mRemote.transact(155, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyCleartextNetwork(uid, firstPacket);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(resizeableMode);
                    if (this.mRemote.transact(156, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTaskResizeable(taskId, resizeableMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (bounds != null) {
                        _data.writeInt(1);
                        bounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resizeMode);
                    if (this.mRemote.transact(157, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resizeTask(taskId, bounds, resizeMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLockTaskModeState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 158;
                    if (!this.mRemote.transact(158, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLockTaskModeState();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDumpHeapDebugLimit(String processName, int uid, long maxMemSize, String reportPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(processName);
                    _data.writeInt(uid);
                    _data.writeLong(maxMemSize);
                    _data.writeString(reportPackage);
                    if (this.mRemote.transact(159, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDumpHeapDebugLimit(processName, uid, maxMemSize, reportPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dumpHeapFinished(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    if (this.mRemote.transact(160, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dumpHeapFinished(path);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeStringArray(packages);
                    if (this.mRemote.transact(161, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateLockTaskPackages(userId, packages);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteAlarmStart(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceUid);
                    _data.writeString(tag);
                    if (this.mRemote.transact(162, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteAlarmStart(sender, workSource, sourceUid, tag);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void noteAlarmFinish(IIntentSender sender, WorkSource workSource, int sourceUid, String tag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(sender != null ? sender.asBinder() : null);
                    if (workSource != null) {
                        _data.writeInt(1);
                        workSource.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(sourceUid);
                    _data.writeString(tag);
                    if (this.mRemote.transact(163, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().noteAlarmFinish(sender, workSource, sourceUid, tag);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPackageProcessState(String packageName, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(callingPackage);
                    int i = 164;
                    if (!this.mRemote.transact(164, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPackageProcessState(packageName, callingPackage);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void updateDeviceOwner(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(165, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateDeviceOwner(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startBinderTracking() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(166, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().startBinderTracking();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean stopBinderTrackingAndDump(ParcelFileDescriptor fd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (fd != null) {
                        _data.writeInt(1);
                        fd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(167, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().stopBinderTrackingAndDump(fd);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void positionTaskInStack(int taskId, int stackId, int position) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    _data.writeInt(position);
                    if (this.mRemote.transact(168, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().positionTaskInStack(taskId, stackId, position);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(suppress ? 1 : 0);
                    if (this.mRemote.transact(169, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().suppressResizeConfigChanges(suppress);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean moveTopActivityToPinnedStack(int stackId, Rect bounds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    boolean _result = true;
                    if (bounds != null) {
                        _data.writeInt(1);
                        bounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(170, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().moveTopActivityToPinnedStack(stackId, bounds);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAppStartModeDisabled(int uid, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(171, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAppStartModeDisabled(uid, packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean unlockUser(int userid, byte[] token, byte[] secret, IProgressListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(172, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().unlockUser(userid, token, secret, listener);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void killPackageDependents(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(173, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().killPackageDependents(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resizeDockedStack(Rect dockedBounds, Rect tempDockedTaskBounds, Rect tempDockedTaskInsetBounds, Rect tempOtherTaskBounds, Rect tempOtherTaskInsetBounds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (dockedBounds != null) {
                        _data.writeInt(1);
                        dockedBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (tempDockedTaskBounds != null) {
                        _data.writeInt(1);
                        tempDockedTaskBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (tempDockedTaskInsetBounds != null) {
                        _data.writeInt(1);
                        tempDockedTaskInsetBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (tempOtherTaskBounds != null) {
                        _data.writeInt(1);
                        tempOtherTaskBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (tempOtherTaskInsetBounds != null) {
                        _data.writeInt(1);
                        tempOtherTaskInsetBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(174, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resizeDockedStack(dockedBounds, tempDockedTaskBounds, tempDockedTaskInsetBounds, tempOtherTaskBounds, tempOtherTaskInsetBounds);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeStack(int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    if (this.mRemote.transact(175, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeStack(stackId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void makePackageIdle(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(176, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().makePackageIdle(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMemoryTrimLevel() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 177;
                    if (!this.mRemote.transact(177, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getMemoryTrimLevel();
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isVrModePackageEnabled(ComponentName packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (packageName != null) {
                        _data.writeInt(1);
                        packageName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(178, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isVrModePackageEnabled(packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyLockedProfile(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(179, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyLockedProfile(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startConfirmDeviceCredentialIntent(Intent intent, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(180, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startConfirmDeviceCredentialIntent(intent, options);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sendIdleJobTrigger() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(181, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sendIdleJobTrigger();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int sendIntentSender(IIntentSender target, IBinder whitelistToken, int code, Intent intent, String resolvedType, IIntentReceiver finishedReceiver, String requiredPermission, Bundle options) throws RemoteException {
                Throwable th;
                int i;
                Intent intent2 = intent;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBinder iBinder = null;
                    _data.writeStrongBinder(target != null ? target.asBinder() : null);
                    try {
                        _data.writeStrongBinder(whitelistToken);
                    } catch (Throwable th2) {
                        th = th2;
                        i = code;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(code);
                        if (intent2 != null) {
                            _data.writeInt(1);
                            intent2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeString(resolvedType);
                        if (finishedReceiver != null) {
                            iBinder = finishedReceiver.asBinder();
                        }
                        _data.writeStrongBinder(iBinder);
                        _data.writeString(requiredPermission);
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (this.mRemote.transact(182, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            int _result = _reply.readInt();
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        int sendIntentSender = Stub.getDefaultImpl().sendIntentSender(target, whitelistToken, code, intent, resolvedType, finishedReceiver, requiredPermission, options);
                        _reply.recycle();
                        _data.recycle();
                        return sendIntentSender;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    IBinder iBinder2 = whitelistToken;
                    i = code;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean isBackgroundRestricted(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(183, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isBackgroundRestricted(packageName);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRenderThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    if (this.mRemote.transact(184, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRenderThread(tid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setHasTopUi(boolean hasTopUi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(hasTopUi ? 1 : 0);
                    if (this.mRemote.transact(185, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setHasTopUi(hasTopUi);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int restartUserInBackground(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 186;
                    if (!this.mRemote.transact(186, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().restartUserInBackground(userId);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelTaskWindowTransition(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (this.mRemote.transact(187, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelTaskWindowTransition(taskId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public TaskSnapshot getTaskSnapshot(int taskId, boolean reducedResolution) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(reducedResolution ? 1 : 0);
                    TaskSnapshot taskSnapshot = this.mRemote;
                    if (!taskSnapshot.transact(188, _data, _reply, 0)) {
                        taskSnapshot = Stub.getDefaultImpl();
                        if (taskSnapshot != null) {
                            taskSnapshot = Stub.getDefaultImpl().getTaskSnapshot(taskId, reducedResolution);
                            return taskSnapshot;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        taskSnapshot = (TaskSnapshot) TaskSnapshot.CREATOR.createFromParcel(_reply);
                    } else {
                        taskSnapshot = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return taskSnapshot;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void scheduleApplicationInfoChanged(List<String> packageNames, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(189, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().scheduleApplicationInfoChanged(packageNames, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPersistentVrThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    if (this.mRemote.transact(190, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPersistentVrThread(tid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void waitForNetworkStateUpdate(long procStateSeq) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(procStateSeq);
                    if (this.mRemote.transact(191, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().waitForNetworkStateUpdate(procStateSeq);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void backgroundWhitelistUid(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(192, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().backgroundWhitelistUid(uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startUserInBackgroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeStrongBinder(unlockProgressListener != null ? unlockProgressListener.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(193, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().startUserInBackgroundWithListener(userid, unlockProgressListener);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startDelegateShellPermissionIdentity(int uid, String[] permissions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeStringArray(permissions);
                    if (this.mRemote.transact(194, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startDelegateShellPermissionIdentity(uid, permissions);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportKillProcessEvent(int killerPid, int killedPid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(killerPid);
                    _data.writeInt(killedPid);
                    if (this.mRemote.transact(195, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().reportKillProcessEvent(killerPid, killedPid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void registerActivityObserver(IMiuiActivityObserver observer, Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(196, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            intent.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerActivityObserver(observer, intent);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterActivityObserver(IMiuiActivityObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(197, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterActivityObserver(observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopDelegateShellPermissionIdentity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(198, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopDelegateShellPermissionIdentity();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerMiuiAppTransitionAnimationHelper(IMiuiAppTransitionAnimationHelper helper, int displayid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(helper != null ? helper.asBinder() : null);
                    _data.writeInt(displayid);
                    if (this.mRemote.transact(199, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registerMiuiAppTransitionAnimationHelper(helper, displayid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unregisterMiuiAppTransitionAnimationHelper(int displayid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayid);
                    if (this.mRemote.transact(200, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unregisterMiuiAppTransitionAnimationHelper(displayid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setDummyTranslucent(IBinder token, boolean translucent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(translucent ? 1 : 0);
                    if (this.mRemote.transact(201, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setDummyTranslucent(token, translucent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setResizeWhiteList(List<String> whiteList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(whiteList);
                    if (this.mRemote.transact(202, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setResizeWhiteList(whiteList);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setResizeBlackList(List<String> blackList) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(blackList);
                    if (this.mRemote.transact(203, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setResizeBlackList(blackList);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor getLifeMonitor() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParcelFileDescriptor parcelFileDescriptor = 204;
                    if (!this.mRemote.transact(204, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().getLifeMonitor();
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startUserInForegroundWithListener(int userid, IProgressListener unlockProgressListener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userid);
                    _data.writeStrongBinder(unlockProgressListener != null ? unlockProgressListener.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(205, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().startUserInForegroundWithListener(userid, unlockProgressListener);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IActivityManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IActivityManager)) {
                return new Proxy(obj);
            }
            return (IActivityManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "openContentUri";
                case 2:
                    return "registerUidObserver";
                case 3:
                    return "unregisterUidObserver";
                case 4:
                    return "isUidActive";
                case 5:
                    return "getUidProcessState";
                case 6:
                    return "handleApplicationCrash";
                case 7:
                    return "startActivity";
                case 8:
                    return "unhandledBack";
                case 9:
                    return "finishActivity";
                case 10:
                    return "registerReceiver";
                case 11:
                    return "unregisterReceiver";
                case 12:
                    return "broadcastIntent";
                case 13:
                    return "unbroadcastIntent";
                case 14:
                    return "finishReceiver";
                case 15:
                    return "attachApplication";
                case 16:
                    return "getTasks";
                case 17:
                    return "getFilteredTasks";
                case 18:
                    return "moveTaskToFront";
                case 19:
                    return "getTaskForActivity";
                case 20:
                    return "getContentProvider";
                case 21:
                    return "publishContentProviders";
                case 22:
                    return "refContentProvider";
                case 23:
                    return "getRunningServiceControlPanel";
                case 24:
                    return "startService";
                case 25:
                    return "stopService";
                case 26:
                    return "bindService";
                case 27:
                    return "bindIsolatedService";
                case 28:
                    return "updateServiceGroup";
                case 29:
                    return "unbindService";
                case 30:
                    return "publishService";
                case 31:
                    return "setDebugApp";
                case 32:
                    return "setAgentApp";
                case 33:
                    return "setAlwaysFinish";
                case 34:
                    return "startInstrumentation";
                case 35:
                    return "addInstrumentationResults";
                case 36:
                    return "finishInstrumentation";
                case 37:
                    return "getConfiguration";
                case 38:
                    return "updateConfiguration";
                case 39:
                    return "stopServiceToken";
                case 40:
                    return "setProcessLimit";
                case 41:
                    return "getProcessLimit";
                case 42:
                    return "checkPermission";
                case 43:
                    return "checkUriPermission";
                case 44:
                    return "grantUriPermission";
                case 45:
                    return "revokeUriPermission";
                case 46:
                    return "setActivityController";
                case 47:
                    return "showWaitingForDebugger";
                case 48:
                    return "signalPersistentProcesses";
                case 49:
                    return "getRecentTasks";
                case 50:
                    return "serviceDoneExecuting";
                case 51:
                    return "getIntentSender";
                case 52:
                    return "cancelIntentSender";
                case 53:
                    return "getPackageForIntentSender";
                case 54:
                    return "registerIntentSenderCancelListener";
                case 55:
                    return "unregisterIntentSenderCancelListener";
                case 56:
                    return "enterSafeMode";
                case 57:
                    return "noteWakeupAlarm";
                case 58:
                    return "removeContentProvider";
                case 59:
                    return "setRequestedOrientation";
                case 60:
                    return "unbindFinished";
                case 61:
                    return "setProcessImportant";
                case 62:
                    return "setServiceForeground";
                case 63:
                    return "getForegroundServiceType";
                case 64:
                    return "moveActivityTaskToBack";
                case 65:
                    return "getMemoryInfo";
                case 66:
                    return "getProcessesInErrorState";
                case 67:
                    return "clearApplicationUserData";
                case 68:
                    return "forceStopPackage";
                case 69:
                    return "killPids";
                case 70:
                    return "getServices";
                case 71:
                    return "getRunningAppProcesses";
                case 72:
                    return "peekService";
                case 73:
                    return "profileControl";
                case 74:
                    return "shutdown";
                case 75:
                    return "stopAppSwitches";
                case 76:
                    return "resumeAppSwitches";
                case 77:
                    return "bindBackupAgent";
                case 78:
                    return "backupAgentCreated";
                case 79:
                    return "unbindBackupAgent";
                case 80:
                    return "getUidForIntentSender";
                case 81:
                    return "handleIncomingUser";
                case 82:
                    return "addPackageDependency";
                case 83:
                    return "killApplication";
                case 84:
                    return "closeSystemDialogs";
                case 85:
                    return "getProcessMemoryInfo";
                case 86:
                    return "killApplicationProcess";
                case 87:
                    return "handleApplicationWtf";
                case 88:
                    return "killBackgroundProcesses";
                case 89:
                    return "isUserAMonkey";
                case 90:
                    return "getRunningExternalApplications";
                case 91:
                    return "finishHeavyWeightApp";
                case 92:
                    return "handleApplicationStrictModeViolation";
                case 93:
                    return "isTopActivityImmersive";
                case 94:
                    return "crashApplication";
                case 95:
                    return "getProviderMimeType";
                case 96:
                    return "dumpHeap";
                case 97:
                    return "isUserRunning";
                case 98:
                    return "setPackageScreenCompatMode";
                case 99:
                    return "switchUser";
                case 100:
                    return "removeTask";
                case 101:
                    return "registerProcessObserver";
                case 102:
                    return "unregisterProcessObserver";
                case 103:
                    return "isIntentSenderTargetedToPackage";
                case 104:
                    return "updatePersistentConfiguration";
                case 105:
                    return "getProcessPss";
                case 106:
                    return "showBootMessage";
                case 107:
                    return "killAllBackgroundProcesses";
                case 108:
                    return "getContentProviderExternal";
                case 109:
                    return "removeContentProviderExternal";
                case 110:
                    return "removeContentProviderExternalAsUser";
                case 111:
                    return "getMyMemoryState";
                case 112:
                    return "killProcessesBelowForeground";
                case 113:
                    return "getCurrentUser";
                case 114:
                    return "getLaunchedFromUid";
                case 115:
                    return "unstableProviderDied";
                case 116:
                    return "isIntentSenderAnActivity";
                case 117:
                    return "isIntentSenderAForegroundService";
                case 118:
                    return "isIntentSenderABroadcast";
                case 119:
                    return "startActivityAsUser";
                case 120:
                    return "stopUser";
                case 121:
                    return "registerUserSwitchObserver";
                case 122:
                    return "unregisterUserSwitchObserver";
                case 123:
                    return "getRunningUserIds";
                case 124:
                    return "requestSystemServerHeapDump";
                case 125:
                    return "requestBugReport";
                case 126:
                    return "requestTelephonyBugReport";
                case 127:
                    return "requestWifiBugReport";
                case 128:
                    return "getIntentForIntentSender";
                case 129:
                    return "getLaunchedFromPackage";
                case 130:
                    return "killUid";
                case 131:
                    return "setUserIsMonkey";
                case 132:
                    return "hang";
                case 133:
                    return "getAllStackInfos";
                case 134:
                    return "moveTaskToStack";
                case 135:
                    return "resizeStack";
                case 136:
                    return "setFocusedStack";
                case 137:
                    return "getFocusedStackInfo";
                case 138:
                    return "restart";
                case 139:
                    return "performIdleMaintenance";
                case 140:
                    return "appNotRespondingViaProvider";
                case 141:
                    return "getTaskBounds";
                case 142:
                    return "setProcessMemoryTrimLevel";
                case 143:
                    return "getTagForIntentSender";
                case 144:
                    return "startUserInBackground";
                case 145:
                    return "isInLockTaskMode";
                case 146:
                    return "startRecentsActivity";
                case 147:
                    return "cancelRecentsAnimation";
                case 148:
                    return "startActivityFromRecents";
                case 149:
                    return "startSystemLockTaskMode";
                case 150:
                    return "isTopOfTask";
                case 151:
                    return "bootAnimationComplete";
                case 152:
                    return "checkPermissionWithToken";
                case 153:
                    return "registerTaskStackListener";
                case 154:
                    return "unregisterTaskStackListener";
                case 155:
                    return "notifyCleartextNetwork";
                case 156:
                    return "setTaskResizeable";
                case 157:
                    return "resizeTask";
                case 158:
                    return "getLockTaskModeState";
                case 159:
                    return "setDumpHeapDebugLimit";
                case 160:
                    return "dumpHeapFinished";
                case 161:
                    return "updateLockTaskPackages";
                case 162:
                    return "noteAlarmStart";
                case 163:
                    return "noteAlarmFinish";
                case 164:
                    return "getPackageProcessState";
                case 165:
                    return "updateDeviceOwner";
                case 166:
                    return "startBinderTracking";
                case 167:
                    return "stopBinderTrackingAndDump";
                case 168:
                    return "positionTaskInStack";
                case 169:
                    return "suppressResizeConfigChanges";
                case 170:
                    return "moveTopActivityToPinnedStack";
                case 171:
                    return "isAppStartModeDisabled";
                case 172:
                    return "unlockUser";
                case 173:
                    return "killPackageDependents";
                case 174:
                    return "resizeDockedStack";
                case 175:
                    return "removeStack";
                case 176:
                    return "makePackageIdle";
                case 177:
                    return "getMemoryTrimLevel";
                case 178:
                    return "isVrModePackageEnabled";
                case 179:
                    return "notifyLockedProfile";
                case 180:
                    return "startConfirmDeviceCredentialIntent";
                case 181:
                    return "sendIdleJobTrigger";
                case 182:
                    return "sendIntentSender";
                case 183:
                    return "isBackgroundRestricted";
                case 184:
                    return "setRenderThread";
                case 185:
                    return "setHasTopUi";
                case 186:
                    return "restartUserInBackground";
                case 187:
                    return "cancelTaskWindowTransition";
                case 188:
                    return "getTaskSnapshot";
                case 189:
                    return "scheduleApplicationInfoChanged";
                case 190:
                    return "setPersistentVrThread";
                case 191:
                    return "waitForNetworkStateUpdate";
                case 192:
                    return "backgroundWhitelistUid";
                case 193:
                    return "startUserInBackgroundWithListener";
                case 194:
                    return "startDelegateShellPermissionIdentity";
                case 195:
                    return "reportKillProcessEvent";
                case 196:
                    return "registerActivityObserver";
                case 197:
                    return "unregisterActivityObserver";
                case 198:
                    return "stopDelegateShellPermissionIdentity";
                case 199:
                    return "registerMiuiAppTransitionAnimationHelper";
                case 200:
                    return "unregisterMiuiAppTransitionAnimationHelper";
                case 201:
                    return "setDummyTranslucent";
                case 202:
                    return "setResizeWhiteList";
                case 203:
                    return "setResizeBlackList";
                case 204:
                    return "getLifeMonitor";
                case 205:
                    return "startUserInForegroundWithListener";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            boolean z;
            if (i != 1598968902) {
                boolean z2 = false;
                Parcel parcel3;
                String descriptor2;
                boolean z3;
                int _result;
                Parcel parcel4;
                IBinder _arg0;
                String _arg1;
                Intent _arg2;
                String _arg3;
                int _arg7;
                Bundle _arg9;
                int _result2;
                boolean z4;
                int _arg12;
                boolean _result3;
                IApplicationThread _arg02;
                String _arg13;
                Intent _result4;
                boolean z5;
                String descriptor3;
                boolean z6;
                IApplicationThread _arg03;
                int _arg4;
                String _arg5;
                Bundle _arg6;
                Bundle _arg92;
                IApplicationThread _arg04;
                Intent _arg14;
                int _arg15;
                IApplicationThread _arg05;
                String _arg16;
                ComponentName _arg06;
                int _result5;
                IBinder _arg17;
                int _arg22;
                boolean _result6;
                Bundle _arg18;
                Configuration _result7;
                int _result8;
                Uri _arg23;
                String _arg19;
                String _arg32;
                int _arg72;
                IIntentSender _result9;
                String _result10;
                int[] _arg07;
                int _arg110;
                String _result11;
                Intent _arg24;
                Rect _arg111;
                Rect _result12;
                WorkSource _arg112;
                ParcelFileDescriptor _arg08;
                switch (i) {
                    case 1:
                        parcel3 = parcel2;
                        descriptor2 = descriptor;
                        parcel.enforceInterface(descriptor2);
                        ParcelFileDescriptor _result13 = openContentUri(data.readString());
                        reply.writeNoException();
                        if (_result13 != null) {
                            z = true;
                            parcel3.writeInt(1);
                            _result13.writeToParcel(parcel3, 1);
                        } else {
                            z = true;
                            parcel3.writeInt(0);
                        }
                        return z;
                    case 2:
                        parcel3 = parcel2;
                        descriptor2 = descriptor;
                        parcel.enforceInterface(descriptor2);
                        registerUidObserver(android.app.IUidObserver.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 3:
                        z3 = true;
                        parcel3 = parcel2;
                        descriptor2 = descriptor;
                        parcel.enforceInterface(descriptor2);
                        unregisterUidObserver(android.app.IUidObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z3;
                    case 4:
                        parcel3 = parcel2;
                        descriptor2 = descriptor;
                        parcel.enforceInterface(descriptor2);
                        z = isUidActive(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel3.writeInt(z);
                        return true;
                    case 5:
                        parcel3 = parcel2;
                        descriptor2 = descriptor;
                        parcel.enforceInterface(descriptor2);
                        _result = getUidProcessState(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel3.writeInt(_result);
                        return true;
                    case 6:
                        ParcelableCrashInfo _arg113;
                        parcel3 = parcel2;
                        descriptor2 = descriptor;
                        parcel4 = parcel;
                        parcel4.enforceInterface(descriptor2);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg113 = (ParcelableCrashInfo) ParcelableCrashInfo.CREATOR.createFromParcel(parcel4);
                        } else {
                            _arg113 = null;
                        }
                        handleApplicationCrash(_arg0, _arg113);
                        reply.writeNoException();
                        return true;
                    case 7:
                        ProfilerInfo _arg8;
                        parcel3 = parcel2;
                        descriptor2 = descriptor;
                        parcel4 = parcel;
                        parcel4.enforceInterface(descriptor2);
                        IApplicationThread _arg09 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Intent) Intent.CREATOR.createFromParcel(parcel4);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readString();
                        IBinder _arg42 = data.readStrongBinder();
                        String _arg52 = data.readString();
                        int _arg62 = data.readInt();
                        _arg7 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg8 = (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(parcel4);
                        } else {
                            _arg8 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg9 = (Bundle) Bundle.CREATOR.createFromParcel(parcel4);
                        } else {
                            _arg9 = null;
                        }
                        _result2 = startActivity(_arg09, _arg1, _arg2, _arg3, _arg42, _arg52, _arg62, _arg7, _arg8, _arg9);
                        reply.writeNoException();
                        parcel3.writeInt(_result2);
                        return true;
                    case 8:
                        z4 = true;
                        parcel3 = parcel2;
                        descriptor2 = descriptor;
                        parcel.enforceInterface(descriptor2);
                        unhandledBack();
                        reply.writeNoException();
                        return z4;
                    case 9:
                        Intent _arg25;
                        parcel3 = parcel2;
                        descriptor2 = descriptor;
                        parcel4 = parcel;
                        parcel4.enforceInterface(descriptor2);
                        _arg0 = data.readStrongBinder();
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg25 = (Intent) Intent.CREATOR.createFromParcel(parcel4);
                        } else {
                            _arg25 = null;
                        }
                        _result3 = finishActivity(_arg0, _arg12, _arg25, data.readInt());
                        reply.writeNoException();
                        parcel3.writeInt(_result3);
                        return true;
                    case 10:
                        IntentFilter _arg33;
                        parcel3 = parcel2;
                        descriptor2 = descriptor;
                        parcel4 = parcel;
                        parcel4.enforceInterface(descriptor2);
                        _arg02 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg13 = data.readString();
                        IIntentReceiver _arg26 = android.content.IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg33 = (IntentFilter) IntentFilter.CREATOR.createFromParcel(parcel4);
                        } else {
                            _arg33 = null;
                        }
                        _result4 = registerReceiver(_arg02, _arg13, _arg26, _arg33, data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result4 != null) {
                            z5 = true;
                            parcel3.writeInt(1);
                            _result4.writeToParcel(parcel3, 1);
                        } else {
                            z5 = true;
                            parcel3.writeInt(0);
                        }
                        return z5;
                    case 11:
                        z5 = true;
                        parcel3 = parcel2;
                        descriptor3 = descriptor;
                        parcel.enforceInterface(descriptor3);
                        unregisterReceiver(android.content.IIntentReceiver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z5;
                    case 12:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg2 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readString();
                        IIntentReceiver _arg34 = android.content.IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                        _arg4 = data.readInt();
                        _arg5 = data.readString();
                        if (data.readInt() != 0) {
                            _arg6 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        String[] _arg73 = data.createStringArray();
                        int _arg82 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg92 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg92 = null;
                        }
                        _arg13 = descriptor;
                        descriptor = data.readInt() != 0 ? z6 : false;
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        parcel3 = parcel2;
                        parcel4 = parcel;
                        _result2 = broadcastIntent(_arg03, _arg2, _arg3, _arg34, _arg4, _arg5, _arg6, _arg73, _arg82, _arg92, descriptor, z2, data.readInt());
                        reply.writeNoException();
                        parcel3.writeInt(_result2);
                        return true;
                    case 13:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg04 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg14 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        unbroadcastIntent(_arg04, _arg14, data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 14:
                        Bundle _arg35;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg010 = data.readStrongBinder();
                        _arg15 = data.readInt();
                        _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg35 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg35 = null;
                        }
                        finishReceiver(_arg010, _arg15, _arg13, _arg35, data.readInt() != 0 ? z6 : false, data.readInt());
                        return z6;
                    case 15:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        attachApplication(android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder()), data.readLong());
                        reply.writeNoException();
                        return z6;
                    case 16:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        List<RunningTaskInfo> _result14 = getTasks(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result14);
                        return z6;
                    case 17:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        List<RunningTaskInfo> _result15 = getFilteredTasks(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result15);
                        return z6;
                    case 18:
                        Bundle _arg43;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg05 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg16 = data.readString();
                        int _arg27 = data.readInt();
                        _arg15 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg43 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg43 = null;
                        }
                        moveTaskToFront(_arg05, _arg16, _arg27, _arg15, _arg43);
                        reply.writeNoException();
                        return z6;
                    case 19:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        _result = getTaskForActivity(_arg0, z2);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z6;
                    case 20:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        ContentProviderHolder _result16 = getContentProvider(android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readString(), data.readInt(), data.readInt() != 0 ? z6 : false);
                        reply.writeNoException();
                        if (_result16 != null) {
                            parcel2.writeInt(z6);
                            _result16.writeToParcel(parcel2, z6);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return z6;
                    case 21:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        publishContentProviders(android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder()), parcel.createTypedArrayList(ContentProviderHolder.CREATOR));
                        reply.writeNoException();
                        return z6;
                    case 22:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z3 = refContentProvider(data.readStrongBinder(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z3);
                        return z6;
                    case 23:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        PendingIntent _result17 = getRunningServiceControlPanel(_arg06);
                        reply.writeNoException();
                        if (_result17 != null) {
                            parcel2.writeInt(i);
                            _result17.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 24:
                        Intent _arg114;
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        IApplicationThread _arg011 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg114 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg114 = null;
                        }
                        _arg06 = startService(_arg011, _arg114, data.readString(), data.readInt() != 0 ? i : false, data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_arg06 != null) {
                            parcel2.writeInt(i);
                            _arg06.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 25:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg04 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg14 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        _result5 = stopService(_arg04, _arg14, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return z6;
                    case 26:
                        Intent _arg28;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg02 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        IBinder _arg115 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg28 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg28 = null;
                        }
                        _result2 = bindService(_arg02, _arg115, _arg28, data.readString(), android.app.IServiceConnection.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z6;
                    case 27:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        IApplicationThread _arg012 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg17 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg2 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result2 = bindIsolatedService(_arg012, _arg17, _arg2, data.readString(), android.app.IServiceConnection.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z6;
                    case 28:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        updateServiceGroup(android.app.IServiceConnection.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 29:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z5 = unbindService(android.app.IServiceConnection.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return z6;
                    case 30:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg14 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        publishService(_arg0, _arg14, data.readStrongBinder());
                        reply.writeNoException();
                        return z6;
                    case 31:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        descriptor3 = data.readString();
                        z5 = data.readInt() != 0 ? z6 : false;
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        setDebugApp(descriptor3, z5, z2);
                        reply.writeNoException();
                        return z6;
                    case 32:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        setAgentApp(data.readString(), data.readString());
                        reply.writeNoException();
                        return z6;
                    case 33:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        setAlwaysFinish(z2);
                        reply.writeNoException();
                        return z6;
                    case 34:
                        ComponentName _arg013;
                        Bundle _arg36;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg013 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg013 = null;
                        }
                        _arg13 = data.readString();
                        _arg22 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg36 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg36 = null;
                        }
                        _result6 = startInstrumentation(_arg013, _arg13, _arg22, _arg36, android.app.IInstrumentationWatcher.Stub.asInterface(data.readStrongBinder()), android.app.IUiAutomationConnection.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z6;
                    case 35:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg04 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg18 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        addInstrumentationResults(_arg04, _arg18);
                        reply.writeNoException();
                        return z6;
                    case 36:
                        Bundle _arg29;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg04 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg29 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg29 = null;
                        }
                        finishInstrumentation(_arg04, _arg12, _arg29);
                        reply.writeNoException();
                        return z6;
                    case 37:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        _result7 = getConfiguration();
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(i);
                            _result7.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 38:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result7 = (Configuration) Configuration.CREATOR.createFromParcel(parcel);
                        } else {
                            _result7 = null;
                        }
                        z5 = updateConfiguration(_result7);
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return z6;
                    case 39:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        z3 = stopServiceToken(_arg06, data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z3);
                        return z6;
                    case 40:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        setProcessLimit(data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 41:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _result2 = getProcessLimit();
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z6;
                    case 42:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _result8 = checkPermission(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return z6;
                    case 43:
                        Uri _arg014;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg014 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg014 = null;
                        }
                        _result2 = checkUriPermission(_arg014, data.readInt(), data.readInt(), data.readInt(), data.readInt(), data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z6;
                    case 44:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg05 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg16 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        grantUriPermission(_arg05, _arg16, _arg23, data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 45:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg05 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg16 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        revokeUriPermission(_arg05, _arg16, _arg23, data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 46:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        IActivityController _arg015 = android.app.IActivityController.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        setActivityController(_arg015, z2);
                        reply.writeNoException();
                        return z6;
                    case 47:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg04 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        showWaitingForDebugger(_arg04, z2);
                        reply.writeNoException();
                        return z6;
                    case 48:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        signalPersistentProcesses(data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 49:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result18 = getRecentTasks(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result18 != null) {
                            parcel2.writeInt(i);
                            _result18.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 50:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        serviceDoneExecuting(data.readStrongBinder(), data.readInt(), data.readInt(), data.readInt());
                        return z6;
                    case 51:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg22 = data.readInt();
                        _arg19 = data.readString();
                        IBinder _arg210 = data.readStrongBinder();
                        _arg32 = data.readString();
                        _arg4 = data.readInt();
                        Intent[] _arg53 = (Intent[]) parcel.createTypedArray(Intent.CREATOR);
                        String[] _arg63 = data.createStringArray();
                        _arg72 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg9 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg9 = null;
                        }
                        _result9 = getIntentSender(_arg22, _arg19, _arg210, _arg32, _arg4, _arg53, _arg63, _arg72, _arg9, data.readInt());
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result9 != null ? _result9.asBinder() : null);
                        return z6;
                    case 52:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        cancelIntentSender(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z6;
                    case 53:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _result10 = getPackageForIntentSender(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeString(_result10);
                        return z6;
                    case 54:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        registerIntentSenderCancelListener(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()), com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z6;
                    case 55:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        unregisterIntentSenderCancelListener(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()), com.android.internal.os.IResultReceiver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z6;
                    case 56:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        enterSafeMode();
                        reply.writeNoException();
                        return z6;
                    case 57:
                        WorkSource _arg116;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        IIntentSender _arg016 = android.content.IIntentSender.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg116 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg116 = null;
                        }
                        noteWakeupAlarm(_arg016, _arg116, data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return z6;
                    case 58:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        removeContentProvider(_arg0, z2);
                        return z6;
                    case 59:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        setRequestedOrientation(data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 60:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg14 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        unbindFinished(_arg0, _arg14, z2);
                        reply.writeNoException();
                        return z6;
                    case 61:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        setProcessImportant(_arg0, _arg12, z2, data.readString());
                        reply.writeNoException();
                        return z6;
                    case 62:
                        ComponentName _arg017;
                        Notification _arg37;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg017 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg017 = null;
                        }
                        IBinder _arg117 = data.readStrongBinder();
                        _arg15 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg37 = (Notification) Notification.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg37 = null;
                        }
                        setServiceForeground(_arg017, _arg117, _arg15, _arg37, data.readInt(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 63:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        _result = getForegroundServiceType(_arg06, data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return z6;
                    case 64:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        z = moveActivityTaskToBack(_arg0, z2);
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return z6;
                    case 65:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        MemoryInfo _arg018 = new MemoryInfo();
                        getMemoryInfo(_arg018);
                        reply.writeNoException();
                        parcel2.writeInt(i);
                        _arg018.writeToParcel(parcel2, i);
                        return i;
                    case 66:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        List<ProcessErrorStateInfo> _result19 = getProcessesInErrorState();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result19);
                        return z6;
                    case 67:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        descriptor3 = data.readString();
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        _result3 = clearApplicationUserData(descriptor3, z2, android.content.pm.IPackageDataObserver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return z6;
                    case 68:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        forceStopPackage(data.readString(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 69:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg07 = data.createIntArray();
                        _result10 = data.readString();
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        z3 = killPids(_arg07, _result10, z2);
                        reply.writeNoException();
                        parcel2.writeInt(z3);
                        return z6;
                    case 70:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        List<RunningServiceInfo> _result20 = getServices(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result20);
                        return z6;
                    case 71:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        List<RunningAppProcessInfo> _result21 = getRunningAppProcesses();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result21);
                        return z6;
                    case 72:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result4 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _result4 = null;
                        }
                        IBinder _result22 = peekService(_result4, data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result22);
                        return z6;
                    case 73:
                        ProfilerInfo _arg38;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        String _arg019 = data.readString();
                        _arg110 = data.readInt();
                        z3 = data.readInt() != 0 ? z6 : false;
                        if (data.readInt() != 0) {
                            _arg38 = (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg38 = null;
                        }
                        _result6 = profileControl(_arg019, _arg110, z3, _arg38, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z6;
                    case 74:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z5 = shutdown(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return z6;
                    case 75:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        stopAppSwitches();
                        reply.writeNoException();
                        return z6;
                    case 76:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        resumeAppSwitches();
                        reply.writeNoException();
                        return z6;
                    case 77:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z3 = bindBackupAgent(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z3);
                        return z6;
                    case 78:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        backupAgentCreated(data.readString(), data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 79:
                        ApplicationInfo _arg020;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg020 = (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg020 = null;
                        }
                        unbindBackupAgent(_arg020);
                        reply.writeNoException();
                        return z6;
                    case 80:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getUidForIntentSender(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_arg12);
                        return z6;
                    case 81:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _result2 = handleIncomingUser(data.readInt(), data.readInt(), data.readInt(), data.readInt() != 0 ? z6 : false, data.readInt() != 0 ? z6 : false, data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z6;
                    case 82:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        addPackageDependency(data.readString());
                        reply.writeNoException();
                        return z6;
                    case 83:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        killApplication(data.readString(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return z6;
                    case 84:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        closeSystemDialogs(data.readString());
                        reply.writeNoException();
                        return z6;
                    case 85:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        Debug.MemoryInfo[] _result23 = getProcessMemoryInfo(data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result23, i);
                        return i;
                    case 86:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        killApplicationProcess(data.readString(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 87:
                        ParcelableCrashInfo _arg39;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        _result10 = data.readString();
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        z = z2;
                        if (data.readInt() != 0) {
                            _arg39 = (ParcelableCrashInfo) ParcelableCrashInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg39 = null;
                        }
                        _result3 = handleApplicationWtf(_arg0, _result10, z, _arg39);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return z6;
                    case 88:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        killBackgroundProcesses(data.readString(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 89:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = isUserAMonkey();
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z6;
                    case 90:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        List<ApplicationInfo> _result24 = getRunningExternalApplications();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result24);
                        return z6;
                    case 91:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        finishHeavyWeightApp();
                        reply.writeNoException();
                        return z6;
                    case 92:
                        ViolationInfo _arg211;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg211 = (ViolationInfo) ViolationInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg211 = null;
                        }
                        handleApplicationStrictModeViolation(_arg0, _arg12, _arg211);
                        reply.writeNoException();
                        return z6;
                    case 93:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _result6 = isTopActivityImmersive();
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z6;
                    case 94:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        crashApplication(data.readInt(), data.readInt(), data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return z6;
                    case 95:
                        Uri _arg021;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg021 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg021 = null;
                        }
                        _result11 = getProviderMimeType(_arg021, data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result11);
                        return z6;
                    case 96:
                        ParcelFileDescriptor _arg64;
                        RemoteCallback _arg74;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        String _arg022 = data.readString();
                        int _arg118 = data.readInt();
                        z3 = data.readInt() != 0 ? z6 : false;
                        _result3 = data.readInt() != 0 ? z6 : false;
                        z4 = data.readInt() != 0 ? z6 : false;
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg64 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg64 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg74 = (RemoteCallback) RemoteCallback.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg74 = null;
                        }
                        _result6 = dumpHeap(_arg022, _arg118, z3, _result3, z4, _arg1, _arg64, _arg74);
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return z6;
                    case 97:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z = isUserRunning(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return z6;
                    case 98:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        setPackageScreenCompatMode(data.readString(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 99:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z5 = switchUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return z6;
                    case 100:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z5 = removeTask(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return z6;
                    case 101:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        registerProcessObserver(android.app.IProcessObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z6;
                    case 102:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        unregisterProcessObserver(android.app.IProcessObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return z6;
                    case 103:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z5 = isIntentSenderTargetedToPackage(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return z6;
                    case 104:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result7 = (Configuration) Configuration.CREATOR.createFromParcel(parcel);
                        } else {
                            _result7 = null;
                        }
                        updatePersistentConfiguration(_result7);
                        reply.writeNoException();
                        return z6;
                    case 105:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        long[] _result25 = getProcessPss(data.createIntArray());
                        reply.writeNoException();
                        parcel2.writeLongArray(_result25);
                        return z6;
                    case 106:
                        CharSequence _arg023;
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg023 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg023 = null;
                        }
                        if (data.readInt() != 0) {
                            z2 = z6;
                        }
                        showBootMessage(_arg023, z2);
                        reply.writeNoException();
                        return z6;
                    case 107:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        killAllBackgroundProcesses();
                        reply.writeNoException();
                        return z6;
                    case 108:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        ContentProviderHolder _result26 = getContentProviderExternal(data.readString(), data.readInt(), data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        if (_result26 != null) {
                            parcel2.writeInt(i);
                            _result26.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 109:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        removeContentProviderExternal(data.readString(), data.readStrongBinder());
                        reply.writeNoException();
                        return z6;
                    case 110:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        removeContentProviderExternalAsUser(data.readString(), data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        return z6;
                    case 111:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        RunningAppProcessInfo _arg024 = new RunningAppProcessInfo();
                        getMyMemoryState(_arg024);
                        reply.writeNoException();
                        parcel2.writeInt(i);
                        _arg024.writeToParcel(parcel2, i);
                        return i;
                    case 112:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z5 = killProcessesBelowForeground(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return z6;
                    case 113:
                        i = 1;
                        parcel.enforceInterface(descriptor);
                        UserInfo _result27 = getCurrentUser();
                        reply.writeNoException();
                        if (_result27 != null) {
                            parcel2.writeInt(i);
                            _result27.writeToParcel(parcel2, i);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return i;
                    case 114:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        _arg12 = getLaunchedFromUid(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_arg12);
                        return z6;
                    case 115:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        unstableProviderDied(data.readStrongBinder());
                        reply.writeNoException();
                        return z6;
                    case 116:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z5 = isIntentSenderAnActivity(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return z6;
                    case 117:
                        z6 = true;
                        parcel.enforceInterface(descriptor);
                        z5 = isIntentSenderAForegroundService(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return z6;
                    case 118:
                        z6 = true;
                        String str = descriptor;
                        parcel.enforceInterface(descriptor);
                        z5 = isIntentSenderABroadcast(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return z6;
                    case 119:
                        ProfilerInfo _arg83;
                        parcel.enforceInterface(descriptor);
                        _arg03 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg19 = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        _arg32 = data.readString();
                        IBinder _arg44 = data.readStrongBinder();
                        _arg5 = data.readString();
                        _arg7 = data.readInt();
                        _arg72 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg83 = (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg83 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg92 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg92 = null;
                        }
                        z6 = true;
                        _result2 = startActivityAsUser(_arg03, _arg19, _arg24, _arg32, _arg44, _arg5, _arg7, _arg72, _arg83, _arg92, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return z6;
                    case 120:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        _result8 = stopUser(_result2, z2, android.app.IStopUserCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 121:
                        parcel.enforceInterface(descriptor);
                        registerUserSwitchObserver(android.app.IUserSwitchObserver.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        return true;
                    case 122:
                        parcel.enforceInterface(descriptor);
                        unregisterUserSwitchObserver(android.app.IUserSwitchObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 123:
                        parcel.enforceInterface(descriptor);
                        _arg07 = getRunningUserIds();
                        reply.writeNoException();
                        parcel2.writeIntArray(_arg07);
                        return true;
                    case 124:
                        parcel.enforceInterface(descriptor);
                        requestSystemServerHeapDump();
                        reply.writeNoException();
                        return true;
                    case 125:
                        parcel.enforceInterface(descriptor);
                        requestBugReport(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 126:
                        parcel.enforceInterface(descriptor);
                        requestTelephonyBugReport(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 127:
                        parcel.enforceInterface(descriptor);
                        requestWifiBugReport(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 128:
                        parcel.enforceInterface(descriptor);
                        _arg14 = getIntentForIntentSender(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_arg14 != null) {
                            parcel2.writeInt(1);
                            _arg14.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 129:
                        parcel.enforceInterface(descriptor);
                        _result10 = getLaunchedFromPackage(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeString(_result10);
                        return true;
                    case 130:
                        parcel.enforceInterface(descriptor);
                        killUid(data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 131:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        setUserIsMonkey(z2);
                        reply.writeNoException();
                        return true;
                    case 132:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        hang(_arg0, z2);
                        reply.writeNoException();
                        return true;
                    case 133:
                        parcel.enforceInterface(descriptor);
                        List<StackInfo> _result28 = getAllStackInfos();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result28);
                        return true;
                    case 134:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        _arg12 = data.readInt();
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        moveTaskToStack(_result2, _arg12, z2);
                        reply.writeNoException();
                        return true;
                    case 135:
                        parcel.enforceInterface(descriptor);
                        _arg110 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg111 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg111 = null;
                        }
                        resizeStack(_arg110, _arg111, data.readInt() != 0, data.readInt() != 0, data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 136:
                        parcel.enforceInterface(descriptor);
                        setFocusedStack(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 137:
                        parcel.enforceInterface(descriptor);
                        StackInfo _result29 = getFocusedStackInfo();
                        reply.writeNoException();
                        if (_result29 != null) {
                            parcel2.writeInt(1);
                            _result29.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 138:
                        parcel.enforceInterface(descriptor);
                        restart();
                        reply.writeNoException();
                        return true;
                    case 139:
                        parcel.enforceInterface(descriptor);
                        performIdleMaintenance();
                        reply.writeNoException();
                        return true;
                    case 140:
                        parcel.enforceInterface(descriptor);
                        appNotRespondingViaProvider(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 141:
                        parcel.enforceInterface(descriptor);
                        _result12 = getTaskBounds(data.readInt());
                        reply.writeNoException();
                        if (_result12 != null) {
                            parcel2.writeInt(1);
                            _result12.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 142:
                        parcel.enforceInterface(descriptor);
                        z3 = setProcessMemoryTrimLevel(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z3);
                        return true;
                    case 143:
                        parcel.enforceInterface(descriptor);
                        _result11 = getTagForIntentSender(android.content.IIntentSender.Stub.asInterface(data.readStrongBinder()), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result11);
                        return true;
                    case 144:
                        parcel.enforceInterface(descriptor);
                        z5 = startUserInBackground(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return true;
                    case 145:
                        parcel.enforceInterface(descriptor);
                        _result6 = isInLockTaskMode();
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 146:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result4 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _result4 = null;
                        }
                        startRecentsActivity(_result4, android.app.IAssistDataReceiver.Stub.asInterface(data.readStrongBinder()), android.view.IRecentsAnimationRunner.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 147:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        cancelRecentsAnimation(z2);
                        reply.writeNoException();
                        return true;
                    case 148:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg18 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        _result = startActivityFromRecents(_result2, _arg18);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 149:
                        parcel.enforceInterface(descriptor);
                        startSystemLockTaskMode(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 150:
                        parcel.enforceInterface(descriptor);
                        z5 = isTopOfTask(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return true;
                    case 151:
                        parcel.enforceInterface(descriptor);
                        bootAnimationComplete();
                        reply.writeNoException();
                        return true;
                    case 152:
                        parcel.enforceInterface(descriptor);
                        _result5 = checkPermissionWithToken(data.readString(), data.readInt(), data.readInt(), data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 153:
                        parcel.enforceInterface(descriptor);
                        registerTaskStackListener(android.app.ITaskStackListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 154:
                        parcel.enforceInterface(descriptor);
                        unregisterTaskStackListener(android.app.ITaskStackListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 155:
                        parcel.enforceInterface(descriptor);
                        notifyCleartextNetwork(data.readInt(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 156:
                        parcel.enforceInterface(descriptor);
                        setTaskResizeable(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 157:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        if (data.readInt() != 0) {
                            _result12 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _result12 = null;
                        }
                        resizeTask(_result2, _result12, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 158:
                        parcel.enforceInterface(descriptor);
                        _result2 = getLockTaskModeState();
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 159:
                        parcel.enforceInterface(descriptor);
                        setDumpHeapDebugLimit(data.readString(), data.readInt(), data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 160:
                        parcel.enforceInterface(descriptor);
                        dumpHeapFinished(data.readString());
                        reply.writeNoException();
                        return true;
                    case 161:
                        parcel.enforceInterface(descriptor);
                        updateLockTaskPackages(data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 162:
                        parcel.enforceInterface(descriptor);
                        _result9 = android.content.IIntentSender.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg112 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg112 = null;
                        }
                        noteAlarmStart(_result9, _arg112, data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 163:
                        parcel.enforceInterface(descriptor);
                        _result9 = android.content.IIntentSender.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg112 = (WorkSource) WorkSource.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg112 = null;
                        }
                        noteAlarmFinish(_result9, _arg112, data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 164:
                        parcel.enforceInterface(descriptor);
                        _result = getPackageProcessState(data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 165:
                        parcel.enforceInterface(descriptor);
                        updateDeviceOwner(data.readString());
                        reply.writeNoException();
                        return true;
                    case 166:
                        parcel.enforceInterface(descriptor);
                        _result6 = startBinderTracking();
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 167:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg08 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg08 = null;
                        }
                        z5 = stopBinderTrackingAndDump(_arg08);
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return true;
                    case 168:
                        parcel.enforceInterface(descriptor);
                        positionTaskInStack(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 169:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        suppressResizeConfigChanges(z2);
                        reply.writeNoException();
                        return true;
                    case 170:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readInt();
                        if (data.readInt() != 0) {
                            _result12 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _result12 = null;
                        }
                        z = moveTopActivityToPinnedStack(_result2, _result12);
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 171:
                        parcel.enforceInterface(descriptor);
                        z = isAppStartModeDisabled(data.readInt(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 172:
                        parcel.enforceInterface(descriptor);
                        _result3 = unlockUser(data.readInt(), data.createByteArray(), data.createByteArray(), android.os.IProgressListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 173:
                        parcel.enforceInterface(descriptor);
                        killPackageDependents(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 174:
                        Rect _arg025;
                        Rect _arg119;
                        Rect _arg212;
                        Rect _arg45;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg025 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg025 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg119 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg119 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg212 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg212 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg111 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg111 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg45 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg45 = null;
                        }
                        resizeDockedStack(_arg025, _arg119, _arg212, _arg111, _arg45);
                        reply.writeNoException();
                        return true;
                    case 175:
                        parcel.enforceInterface(descriptor);
                        removeStack(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 176:
                        parcel.enforceInterface(descriptor);
                        makePackageIdle(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 177:
                        parcel.enforceInterface(descriptor);
                        _result2 = getMemoryTrimLevel();
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 178:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        z5 = isVrModePackageEnabled(_arg06);
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return true;
                    case 179:
                        parcel.enforceInterface(descriptor);
                        notifyLockedProfile(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 180:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result4 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _result4 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg18 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        startConfirmDeviceCredentialIntent(_result4, _arg18);
                        reply.writeNoException();
                        return true;
                    case 181:
                        parcel.enforceInterface(descriptor);
                        sendIdleJobTrigger();
                        reply.writeNoException();
                        return true;
                    case 182:
                        parcel.enforceInterface(descriptor);
                        IIntentSender _arg026 = android.content.IIntentSender.Stub.asInterface(data.readStrongBinder());
                        _arg17 = data.readStrongBinder();
                        int _arg213 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg24 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        _arg32 = data.readString();
                        IIntentReceiver _arg54 = android.content.IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                        _arg5 = data.readString();
                        if (data.readInt() != 0) {
                            _arg6 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        _result2 = sendIntentSender(_arg026, _arg17, _arg213, _arg24, _arg32, _arg54, _arg5, _arg6);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 183:
                        parcel.enforceInterface(descriptor);
                        z5 = isBackgroundRestricted(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(z5);
                        return true;
                    case 184:
                        parcel.enforceInterface(descriptor);
                        setRenderThread(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 185:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        setHasTopUi(z2);
                        reply.writeNoException();
                        return true;
                    case 186:
                        parcel.enforceInterface(descriptor);
                        _arg12 = restartUserInBackground(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_arg12);
                        return true;
                    case 187:
                        parcel.enforceInterface(descriptor);
                        cancelTaskWindowTransition(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 188:
                        parcel.enforceInterface(descriptor);
                        TaskSnapshot _result30 = getTaskSnapshot(data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result30 != null) {
                            parcel2.writeInt(1);
                            _result30.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 189:
                        parcel.enforceInterface(descriptor);
                        scheduleApplicationInfoChanged(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 190:
                        parcel.enforceInterface(descriptor);
                        setPersistentVrThread(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 191:
                        parcel.enforceInterface(descriptor);
                        waitForNetworkStateUpdate(data.readLong());
                        reply.writeNoException();
                        return true;
                    case 192:
                        parcel.enforceInterface(descriptor);
                        backgroundWhitelistUid(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 193:
                        parcel.enforceInterface(descriptor);
                        z = startUserInBackgroundWithListener(data.readInt(), android.os.IProgressListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 194:
                        parcel.enforceInterface(descriptor);
                        startDelegateShellPermissionIdentity(data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 195:
                        parcel.enforceInterface(descriptor);
                        reportKillProcessEvent(data.readInt(), data.readInt());
                        return true;
                    case 196:
                        parcel.enforceInterface(descriptor);
                        IMiuiActivityObserver _arg027 = android.app.IMiuiActivityObserver.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg14 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        registerActivityObserver(_arg027, _arg14);
                        reply.writeNoException();
                        if (_arg14 != null) {
                            parcel2.writeInt(1);
                            _arg14.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 197:
                        parcel.enforceInterface(descriptor);
                        unregisterActivityObserver(android.app.IMiuiActivityObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 198:
                        parcel.enforceInterface(descriptor);
                        stopDelegateShellPermissionIdentity();
                        reply.writeNoException();
                        return true;
                    case 199:
                        parcel.enforceInterface(descriptor);
                        registerMiuiAppTransitionAnimationHelper(com.miui.internal.transition.IMiuiAppTransitionAnimationHelper.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        return true;
                    case 200:
                        parcel.enforceInterface(descriptor);
                        unregisterMiuiAppTransitionAnimationHelper(data.readInt());
                        return true;
                    case 201:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            z2 = true;
                        }
                        setDummyTranslucent(_arg0, z2);
                        return true;
                    case 202:
                        parcel.enforceInterface(descriptor);
                        setResizeWhiteList(data.createStringArrayList());
                        return true;
                    case 203:
                        parcel.enforceInterface(descriptor);
                        setResizeBlackList(data.createStringArrayList());
                        return true;
                    case 204:
                        parcel.enforceInterface(descriptor);
                        _arg08 = getLifeMonitor();
                        reply.writeNoException();
                        if (_arg08 != null) {
                            parcel2.writeInt(1);
                            _arg08.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 205:
                        parcel.enforceInterface(descriptor);
                        z = startUserInForegroundWithListener(data.readInt(), android.os.IProgressListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(IActivityManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IActivityManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addInstrumentationResults(IApplicationThread iApplicationThread, Bundle bundle) throws RemoteException;

    void addPackageDependency(String str) throws RemoteException;

    void appNotRespondingViaProvider(IBinder iBinder) throws RemoteException;

    void attachApplication(IApplicationThread iApplicationThread, long j) throws RemoteException;

    void backgroundWhitelistUid(int i) throws RemoteException;

    void backupAgentCreated(String str, IBinder iBinder, int i) throws RemoteException;

    boolean bindBackupAgent(String str, int i, int i2) throws RemoteException;

    int bindIsolatedService(IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String str, IServiceConnection iServiceConnection, int i, String str2, String str3, int i2) throws RemoteException;

    @UnsupportedAppUsage
    int bindService(IApplicationThread iApplicationThread, IBinder iBinder, Intent intent, String str, IServiceConnection iServiceConnection, int i, String str2, int i2) throws RemoteException;

    void bootAnimationComplete() throws RemoteException;

    @UnsupportedAppUsage
    int broadcastIntent(IApplicationThread iApplicationThread, Intent intent, String str, IIntentReceiver iIntentReceiver, int i, String str2, Bundle bundle, String[] strArr, int i2, Bundle bundle2, boolean z, boolean z2, int i3) throws RemoteException;

    void cancelIntentSender(IIntentSender iIntentSender) throws RemoteException;

    @UnsupportedAppUsage
    void cancelRecentsAnimation(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void cancelTaskWindowTransition(int i) throws RemoteException;

    @UnsupportedAppUsage
    int checkPermission(String str, int i, int i2) throws RemoteException;

    int checkPermissionWithToken(String str, int i, int i2, IBinder iBinder) throws RemoteException;

    int checkUriPermission(Uri uri, int i, int i2, int i3, int i4, IBinder iBinder) throws RemoteException;

    boolean clearApplicationUserData(String str, boolean z, IPackageDataObserver iPackageDataObserver, int i) throws RemoteException;

    @UnsupportedAppUsage
    void closeSystemDialogs(String str) throws RemoteException;

    void crashApplication(int i, int i2, String str, int i3, String str2) throws RemoteException;

    boolean dumpHeap(String str, int i, boolean z, boolean z2, boolean z3, String str2, ParcelFileDescriptor parcelFileDescriptor, RemoteCallback remoteCallback) throws RemoteException;

    void dumpHeapFinished(String str) throws RemoteException;

    void enterSafeMode() throws RemoteException;

    @UnsupportedAppUsage
    boolean finishActivity(IBinder iBinder, int i, Intent intent, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void finishHeavyWeightApp() throws RemoteException;

    void finishInstrumentation(IApplicationThread iApplicationThread, int i, Bundle bundle) throws RemoteException;

    void finishReceiver(IBinder iBinder, int i, String str, Bundle bundle, boolean z, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void forceStopPackage(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    List<StackInfo> getAllStackInfos() throws RemoteException;

    @UnsupportedAppUsage
    Configuration getConfiguration() throws RemoteException;

    ContentProviderHolder getContentProvider(IApplicationThread iApplicationThread, String str, String str2, int i, boolean z) throws RemoteException;

    ContentProviderHolder getContentProviderExternal(String str, int i, IBinder iBinder, String str2) throws RemoteException;

    @UnsupportedAppUsage
    UserInfo getCurrentUser() throws RemoteException;

    @UnsupportedAppUsage
    List<RunningTaskInfo> getFilteredTasks(int i, int i2, int i3) throws RemoteException;

    StackInfo getFocusedStackInfo() throws RemoteException;

    int getForegroundServiceType(ComponentName componentName, IBinder iBinder) throws RemoteException;

    @UnsupportedAppUsage
    Intent getIntentForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    @UnsupportedAppUsage
    IIntentSender getIntentSender(int i, String str, IBinder iBinder, String str2, int i2, Intent[] intentArr, String[] strArr, int i3, Bundle bundle, int i4) throws RemoteException;

    @UnsupportedAppUsage
    String getLaunchedFromPackage(IBinder iBinder) throws RemoteException;

    @UnsupportedAppUsage
    int getLaunchedFromUid(IBinder iBinder) throws RemoteException;

    ParcelFileDescriptor getLifeMonitor() throws RemoteException;

    @UnsupportedAppUsage
    int getLockTaskModeState() throws RemoteException;

    @UnsupportedAppUsage
    void getMemoryInfo(MemoryInfo memoryInfo) throws RemoteException;

    int getMemoryTrimLevel() throws RemoteException;

    void getMyMemoryState(RunningAppProcessInfo runningAppProcessInfo) throws RemoteException;

    String getPackageForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    @UnsupportedAppUsage
    int getPackageProcessState(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    int getProcessLimit() throws RemoteException;

    @UnsupportedAppUsage
    Debug.MemoryInfo[] getProcessMemoryInfo(int[] iArr) throws RemoteException;

    @UnsupportedAppUsage
    long[] getProcessPss(int[] iArr) throws RemoteException;

    List<ProcessErrorStateInfo> getProcessesInErrorState() throws RemoteException;

    @UnsupportedAppUsage
    String getProviderMimeType(Uri uri, int i) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice getRecentTasks(int i, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    List<RunningAppProcessInfo> getRunningAppProcesses() throws RemoteException;

    List<ApplicationInfo> getRunningExternalApplications() throws RemoteException;

    PendingIntent getRunningServiceControlPanel(ComponentName componentName) throws RemoteException;

    int[] getRunningUserIds() throws RemoteException;

    @UnsupportedAppUsage
    List<RunningServiceInfo> getServices(int i, int i2) throws RemoteException;

    String getTagForIntentSender(IIntentSender iIntentSender, String str) throws RemoteException;

    @UnsupportedAppUsage
    Rect getTaskBounds(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getTaskForActivity(IBinder iBinder, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    TaskSnapshot getTaskSnapshot(int i, boolean z) throws RemoteException;

    List<RunningTaskInfo> getTasks(int i) throws RemoteException;

    int getUidForIntentSender(IIntentSender iIntentSender) throws RemoteException;

    int getUidProcessState(int i, String str) throws RemoteException;

    void grantUriPermission(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) throws RemoteException;

    void handleApplicationCrash(IBinder iBinder, ParcelableCrashInfo parcelableCrashInfo) throws RemoteException;

    @UnsupportedAppUsage
    void handleApplicationStrictModeViolation(IBinder iBinder, int i, ViolationInfo violationInfo) throws RemoteException;

    boolean handleApplicationWtf(IBinder iBinder, String str, boolean z, ParcelableCrashInfo parcelableCrashInfo) throws RemoteException;

    int handleIncomingUser(int i, int i2, int i3, boolean z, boolean z2, String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void hang(IBinder iBinder, boolean z) throws RemoteException;

    boolean isAppStartModeDisabled(int i, String str) throws RemoteException;

    boolean isBackgroundRestricted(String str) throws RemoteException;

    @UnsupportedAppUsage
    boolean isInLockTaskMode() throws RemoteException;

    boolean isIntentSenderABroadcast(IIntentSender iIntentSender) throws RemoteException;

    boolean isIntentSenderAForegroundService(IIntentSender iIntentSender) throws RemoteException;

    @UnsupportedAppUsage
    boolean isIntentSenderAnActivity(IIntentSender iIntentSender) throws RemoteException;

    boolean isIntentSenderTargetedToPackage(IIntentSender iIntentSender) throws RemoteException;

    boolean isTopActivityImmersive() throws RemoteException;

    @UnsupportedAppUsage
    boolean isTopOfTask(IBinder iBinder) throws RemoteException;

    boolean isUidActive(int i, String str) throws RemoteException;

    boolean isUserAMonkey() throws RemoteException;

    @UnsupportedAppUsage
    boolean isUserRunning(int i, int i2) throws RemoteException;

    boolean isVrModePackageEnabled(ComponentName componentName) throws RemoteException;

    @UnsupportedAppUsage
    void killAllBackgroundProcesses() throws RemoteException;

    void killApplication(String str, int i, int i2, String str2) throws RemoteException;

    void killApplicationProcess(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    void killBackgroundProcesses(String str, int i) throws RemoteException;

    void killPackageDependents(String str, int i) throws RemoteException;

    boolean killPids(int[] iArr, String str, boolean z) throws RemoteException;

    boolean killProcessesBelowForeground(String str) throws RemoteException;

    void killUid(int i, int i2, String str) throws RemoteException;

    void makePackageIdle(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean moveActivityTaskToBack(IBinder iBinder, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void moveTaskToFront(IApplicationThread iApplicationThread, String str, int i, int i2, Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    void moveTaskToStack(int i, int i2, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    boolean moveTopActivityToPinnedStack(int i, Rect rect) throws RemoteException;

    void noteAlarmFinish(IIntentSender iIntentSender, WorkSource workSource, int i, String str) throws RemoteException;

    void noteAlarmStart(IIntentSender iIntentSender, WorkSource workSource, int i, String str) throws RemoteException;

    void noteWakeupAlarm(IIntentSender iIntentSender, WorkSource workSource, int i, String str, String str2) throws RemoteException;

    void notifyCleartextNetwork(int i, byte[] bArr) throws RemoteException;

    void notifyLockedProfile(int i) throws RemoteException;

    ParcelFileDescriptor openContentUri(String str) throws RemoteException;

    IBinder peekService(Intent intent, String str, String str2) throws RemoteException;

    void performIdleMaintenance() throws RemoteException;

    @UnsupportedAppUsage
    void positionTaskInStack(int i, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    boolean profileControl(String str, int i, boolean z, ProfilerInfo profilerInfo, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void publishContentProviders(IApplicationThread iApplicationThread, List<ContentProviderHolder> list) throws RemoteException;

    void publishService(IBinder iBinder, Intent intent, IBinder iBinder2) throws RemoteException;

    boolean refContentProvider(IBinder iBinder, int i, int i2) throws RemoteException;

    void registerActivityObserver(IMiuiActivityObserver iMiuiActivityObserver, Intent intent) throws RemoteException;

    void registerIntentSenderCancelListener(IIntentSender iIntentSender, IResultReceiver iResultReceiver) throws RemoteException;

    void registerMiuiAppTransitionAnimationHelper(IMiuiAppTransitionAnimationHelper iMiuiAppTransitionAnimationHelper, int i) throws RemoteException;

    @UnsupportedAppUsage
    void registerProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    @UnsupportedAppUsage
    Intent registerReceiver(IApplicationThread iApplicationThread, String str, IIntentReceiver iIntentReceiver, IntentFilter intentFilter, String str2, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void registerTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    void registerUidObserver(IUidObserver iUidObserver, int i, int i2, String str) throws RemoteException;

    @UnsupportedAppUsage
    void registerUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver, String str) throws RemoteException;

    void removeContentProvider(IBinder iBinder, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void removeContentProviderExternal(String str, IBinder iBinder) throws RemoteException;

    void removeContentProviderExternalAsUser(String str, IBinder iBinder, int i) throws RemoteException;

    @UnsupportedAppUsage
    void removeStack(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean removeTask(int i) throws RemoteException;

    void reportKillProcessEvent(int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void requestBugReport(int i) throws RemoteException;

    void requestSystemServerHeapDump() throws RemoteException;

    void requestTelephonyBugReport(String str, String str2) throws RemoteException;

    void requestWifiBugReport(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void resizeDockedStack(Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5) throws RemoteException;

    @UnsupportedAppUsage
    void resizeStack(int i, Rect rect, boolean z, boolean z2, boolean z3, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void resizeTask(int i, Rect rect, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void restart() throws RemoteException;

    int restartUserInBackground(int i) throws RemoteException;

    @UnsupportedAppUsage
    void resumeAppSwitches() throws RemoteException;

    void revokeUriPermission(IApplicationThread iApplicationThread, String str, Uri uri, int i, int i2) throws RemoteException;

    void scheduleApplicationInfoChanged(List<String> list, int i) throws RemoteException;

    @UnsupportedAppUsage
    void sendIdleJobTrigger() throws RemoteException;

    int sendIntentSender(IIntentSender iIntentSender, IBinder iBinder, int i, Intent intent, String str, IIntentReceiver iIntentReceiver, String str2, Bundle bundle) throws RemoteException;

    void serviceDoneExecuting(IBinder iBinder, int i, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    void setActivityController(IActivityController iActivityController, boolean z) throws RemoteException;

    void setAgentApp(String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void setAlwaysFinish(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setDebugApp(String str, boolean z, boolean z2) throws RemoteException;

    void setDummyTranslucent(IBinder iBinder, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setDumpHeapDebugLimit(String str, int i, long j, String str2) throws RemoteException;

    void setFocusedStack(int i) throws RemoteException;

    void setHasTopUi(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    void setPackageScreenCompatMode(String str, int i) throws RemoteException;

    void setPersistentVrThread(int i) throws RemoteException;

    @UnsupportedAppUsage
    void setProcessImportant(IBinder iBinder, int i, boolean z, String str) throws RemoteException;

    @UnsupportedAppUsage
    void setProcessLimit(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean setProcessMemoryTrimLevel(String str, int i, int i2) throws RemoteException;

    void setRenderThread(int i) throws RemoteException;

    @UnsupportedAppUsage
    void setRequestedOrientation(IBinder iBinder, int i) throws RemoteException;

    void setResizeBlackList(List<String> list) throws RemoteException;

    void setResizeWhiteList(List<String> list) throws RemoteException;

    void setServiceForeground(ComponentName componentName, IBinder iBinder, int i, Notification notification, int i2, int i3) throws RemoteException;

    @UnsupportedAppUsage
    void setTaskResizeable(int i, int i2) throws RemoteException;

    void setUserIsMonkey(boolean z) throws RemoteException;

    void showBootMessage(CharSequence charSequence, boolean z) throws RemoteException;

    void showWaitingForDebugger(IApplicationThread iApplicationThread, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    boolean shutdown(int i) throws RemoteException;

    void signalPersistentProcesses(int i) throws RemoteException;

    @UnsupportedAppUsage
    int startActivity(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    int startActivityAsUser(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    @UnsupportedAppUsage
    int startActivityFromRecents(int i, Bundle bundle) throws RemoteException;

    @UnsupportedAppUsage
    boolean startBinderTracking() throws RemoteException;

    void startConfirmDeviceCredentialIntent(Intent intent, Bundle bundle) throws RemoteException;

    void startDelegateShellPermissionIdentity(int i, String[] strArr) throws RemoteException;

    @UnsupportedAppUsage
    boolean startInstrumentation(ComponentName componentName, String str, int i, Bundle bundle, IInstrumentationWatcher iInstrumentationWatcher, IUiAutomationConnection iUiAutomationConnection, int i2, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void startRecentsActivity(Intent intent, IAssistDataReceiver iAssistDataReceiver, IRecentsAnimationRunner iRecentsAnimationRunner) throws RemoteException;

    ComponentName startService(IApplicationThread iApplicationThread, Intent intent, String str, boolean z, String str2, int i) throws RemoteException;

    @UnsupportedAppUsage
    void startSystemLockTaskMode(int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean startUserInBackground(int i) throws RemoteException;

    boolean startUserInBackgroundWithListener(int i, IProgressListener iProgressListener) throws RemoteException;

    boolean startUserInForegroundWithListener(int i, IProgressListener iProgressListener) throws RemoteException;

    @UnsupportedAppUsage
    void stopAppSwitches() throws RemoteException;

    @UnsupportedAppUsage
    boolean stopBinderTrackingAndDump(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;

    void stopDelegateShellPermissionIdentity() throws RemoteException;

    @UnsupportedAppUsage
    int stopService(IApplicationThread iApplicationThread, Intent intent, String str, int i) throws RemoteException;

    boolean stopServiceToken(ComponentName componentName, IBinder iBinder, int i) throws RemoteException;

    @UnsupportedAppUsage
    int stopUser(int i, boolean z, IStopUserCallback iStopUserCallback) throws RemoteException;

    @UnsupportedAppUsage
    void suppressResizeConfigChanges(boolean z) throws RemoteException;

    @UnsupportedAppUsage
    boolean switchUser(int i) throws RemoteException;

    void unbindBackupAgent(ApplicationInfo applicationInfo) throws RemoteException;

    void unbindFinished(IBinder iBinder, Intent intent, boolean z) throws RemoteException;

    @UnsupportedAppUsage
    boolean unbindService(IServiceConnection iServiceConnection) throws RemoteException;

    void unbroadcastIntent(IApplicationThread iApplicationThread, Intent intent, int i) throws RemoteException;

    @UnsupportedAppUsage
    void unhandledBack() throws RemoteException;

    @UnsupportedAppUsage
    boolean unlockUser(int i, byte[] bArr, byte[] bArr2, IProgressListener iProgressListener) throws RemoteException;

    void unregisterActivityObserver(IMiuiActivityObserver iMiuiActivityObserver) throws RemoteException;

    void unregisterIntentSenderCancelListener(IIntentSender iIntentSender, IResultReceiver iResultReceiver) throws RemoteException;

    void unregisterMiuiAppTransitionAnimationHelper(int i) throws RemoteException;

    @UnsupportedAppUsage
    void unregisterProcessObserver(IProcessObserver iProcessObserver) throws RemoteException;

    @UnsupportedAppUsage
    void unregisterReceiver(IIntentReceiver iIntentReceiver) throws RemoteException;

    void unregisterTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    void unregisterUidObserver(IUidObserver iUidObserver) throws RemoteException;

    void unregisterUserSwitchObserver(IUserSwitchObserver iUserSwitchObserver) throws RemoteException;

    @UnsupportedAppUsage
    void unstableProviderDied(IBinder iBinder) throws RemoteException;

    @UnsupportedAppUsage
    boolean updateConfiguration(Configuration configuration) throws RemoteException;

    void updateDeviceOwner(String str) throws RemoteException;

    void updateLockTaskPackages(int i, String[] strArr) throws RemoteException;

    @UnsupportedAppUsage
    void updatePersistentConfiguration(Configuration configuration) throws RemoteException;

    void updateServiceGroup(IServiceConnection iServiceConnection, int i, int i2) throws RemoteException;

    void waitForNetworkStateUpdate(long j) throws RemoteException;
}
