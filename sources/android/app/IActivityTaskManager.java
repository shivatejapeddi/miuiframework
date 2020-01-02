package android.app;

import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityManager.StackInfo;
import android.app.ActivityManager.TaskDescription;
import android.app.ActivityManager.TaskSnapshot;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.ComponentName;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.pm.ParceledListSlice;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;
import android.service.voice.IVoiceInteractionSession;
import android.text.TextUtils;
import android.view.IRecentsAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationDefinition;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.policy.IKeyguardDismissCallback;
import java.util.List;

public interface IActivityTaskManager extends IInterface {

    public static class Default implements IActivityTaskManager {
        public int startActivity(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options) throws RemoteException {
            return 0;
        }

        public int startActivities(IApplicationThread caller, String callingPackage, Intent[] intents, String[] resolvedTypes, IBinder resultTo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        public int startActivityAsUser(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        public boolean startNextMatchingActivity(IBinder callingActivity, Intent intent, Bundle options) throws RemoteException {
            return false;
        }

        public int startActivityIntentSender(IApplicationThread caller, IIntentSender target, IBinder whitelistToken, Intent fillInIntent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flagsMask, int flagsValues, Bundle options) throws RemoteException {
            return 0;
        }

        public WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return null;
        }

        public int startActivityWithConfig(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, Configuration newConfig, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        public int startVoiceActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, IVoiceInteractionSession session, IVoiceInteractor interactor, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        public int startAssistantActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, Bundle options, int userId) throws RemoteException {
            return 0;
        }

        public void startRecentsActivity(Intent intent, IAssistDataReceiver assistDataReceiver, IRecentsAnimationRunner recentsAnimationRunner) throws RemoteException {
        }

        public int startActivityFromRecents(int taskId, Bundle options) throws RemoteException {
            return 0;
        }

        public int startActivityAsCaller(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, IBinder permissionToken, boolean ignoreTargetSecurity, int userId) throws RemoteException {
            return 0;
        }

        public boolean isActivityStartAllowedOnDisplay(int displayId, Intent intent, String resolvedType, int userId) throws RemoteException {
            return false;
        }

        public void unhandledBack() throws RemoteException {
        }

        public boolean finishActivity(IBinder token, int code, Intent data, int finishTask) throws RemoteException {
            return false;
        }

        public boolean finishActivityAffinity(IBinder token) throws RemoteException {
            return false;
        }

        public void activityIdle(IBinder token, Configuration config, boolean stopProfiling) throws RemoteException {
        }

        public void activityResumed(IBinder token) throws RemoteException {
        }

        public void activityTopResumedStateLost() throws RemoteException {
        }

        public void activityPaused(IBinder token) throws RemoteException {
        }

        public void activityStopped(IBinder token, Bundle state, PersistableBundle persistentState, CharSequence description) throws RemoteException {
        }

        public void activityDestroyed(IBinder token) throws RemoteException {
        }

        public void activityRelaunched(IBinder token) throws RemoteException {
        }

        public void activitySlept(IBinder token) throws RemoteException {
        }

        public int getFrontActivityScreenCompatMode() throws RemoteException {
            return 0;
        }

        public void setFrontActivityScreenCompatMode(int mode) throws RemoteException {
        }

        public String getCallingPackage(IBinder token) throws RemoteException {
            return null;
        }

        public ComponentName getCallingActivity(IBinder token) throws RemoteException {
            return null;
        }

        public void setFocusedTask(int taskId) throws RemoteException {
        }

        public boolean removeTask(int taskId) throws RemoteException {
            return false;
        }

        public void removeAllVisibleRecentTasks() throws RemoteException {
        }

        public List<RunningTaskInfo> getTasks(int maxNum) throws RemoteException {
            return null;
        }

        public List<RunningTaskInfo> getFilteredTasks(int maxNum, int ignoreActivityType, int ignoreWindowingMode) throws RemoteException {
            return null;
        }

        public boolean shouldUpRecreateTask(IBinder token, String destAffinity) throws RemoteException {
            return false;
        }

        public boolean navigateUpTo(IBinder token, Intent target, int resultCode, Intent resultData) throws RemoteException {
            return false;
        }

        public void moveTaskToFront(IApplicationThread app, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
        }

        public int getTaskForActivity(IBinder token, boolean onlyRoot) throws RemoteException {
            return 0;
        }

        public void finishSubActivity(IBinder token, String resultWho, int requestCode) throws RemoteException {
        }

        public ParceledListSlice getRecentTasks(int maxNum, int flags, int userId) throws RemoteException {
            return null;
        }

        public boolean willActivityBeVisible(IBinder token) throws RemoteException {
            return false;
        }

        public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
        }

        public int getRequestedOrientation(IBinder token) throws RemoteException {
            return 0;
        }

        public boolean convertFromTranslucent(IBinder token) throws RemoteException {
            return false;
        }

        public boolean convertToTranslucent(IBinder token, Bundle options) throws RemoteException {
            return false;
        }

        public void notifyActivityDrawn(IBinder token) throws RemoteException {
        }

        public void reportActivityFullyDrawn(IBinder token, boolean restoredFromBundle) throws RemoteException {
        }

        public int getActivityDisplayId(IBinder activityToken) throws RemoteException {
            return 0;
        }

        public boolean isImmersive(IBinder token) throws RemoteException {
            return false;
        }

        public void setImmersive(IBinder token, boolean immersive) throws RemoteException {
        }

        public boolean isTopActivityImmersive() throws RemoteException {
            return false;
        }

        public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
            return false;
        }

        public TaskDescription getTaskDescription(int taskId) throws RemoteException {
            return null;
        }

        public void overridePendingTransition(IBinder token, String packageName, int enterAnim, int exitAnim) throws RemoteException {
        }

        public int getLaunchedFromUid(IBinder activityToken) throws RemoteException {
            return 0;
        }

        public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
            return null;
        }

        public void reportAssistContextExtras(IBinder token, Bundle extras, AssistStructure structure, AssistContent content, Uri referrer) throws RemoteException {
        }

        public void setFocusedStack(int stackId) throws RemoteException {
        }

        public StackInfo getFocusedStackInfo() throws RemoteException {
            return null;
        }

        public Rect getTaskBounds(int taskId) throws RemoteException {
            return null;
        }

        public void cancelRecentsAnimation(boolean restoreHomeStackPosition) throws RemoteException {
        }

        public void startLockTaskModeByToken(IBinder token) throws RemoteException {
        }

        public void stopLockTaskModeByToken(IBinder token) throws RemoteException {
        }

        public void updateLockTaskPackages(int userId, String[] packages) throws RemoteException {
        }

        public boolean isInLockTaskMode() throws RemoteException {
            return false;
        }

        public int getLockTaskModeState() throws RemoteException {
            return 0;
        }

        public void setTaskDescription(IBinder token, TaskDescription values) throws RemoteException {
        }

        public Bundle getActivityOptions(IBinder token) throws RemoteException {
            return null;
        }

        public List<IBinder> getAppTasks(String callingPackage) throws RemoteException {
            return null;
        }

        public void startSystemLockTaskMode(int taskId) throws RemoteException {
        }

        public void stopSystemLockTaskMode() throws RemoteException {
        }

        public void finishVoiceTask(IVoiceInteractionSession session) throws RemoteException {
        }

        public boolean isTopOfTask(IBinder token) throws RemoteException {
            return false;
        }

        public void notifyLaunchTaskBehindComplete(IBinder token) throws RemoteException {
        }

        public void notifyEnterAnimationComplete(IBinder token) throws RemoteException {
        }

        public int addAppTask(IBinder activityToken, Intent intent, TaskDescription description, Bitmap thumbnail) throws RemoteException {
            return 0;
        }

        public Point getAppTaskThumbnailSize() throws RemoteException {
            return null;
        }

        public boolean releaseActivityInstance(IBinder token) throws RemoteException {
            return false;
        }

        public IBinder requestStartActivityPermissionToken(IBinder delegatorToken) throws RemoteException {
            return null;
        }

        public void releaseSomeActivities(IApplicationThread app) throws RemoteException {
        }

        public Bitmap getTaskDescriptionIcon(String filename, int userId) throws RemoteException {
            return null;
        }

        public void startInPlaceAnimationOnFrontMostApplication(Bundle opts) throws RemoteException {
        }

        public void registerTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        public void unregisterTaskStackListener(ITaskStackListener listener) throws RemoteException {
        }

        public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
        }

        public void toggleFreeformWindowingMode(IBinder token) throws RemoteException {
        }

        public void resizeTask(int taskId, Rect bounds, int resizeMode) throws RemoteException {
        }

        public void moveStackToDisplay(int stackId, int displayId) throws RemoteException {
        }

        public void removeStack(int stackId) throws RemoteException {
        }

        public void setTaskWindowingMode(int taskId, int windowingMode, boolean toTop) throws RemoteException {
        }

        public void moveTaskToStack(int taskId, int stackId, boolean toTop) throws RemoteException {
        }

        public void resizeStack(int stackId, Rect bounds, boolean allowResizeInDockedMode, boolean preserveWindows, boolean animate, int animationDuration) throws RemoteException {
        }

        public boolean setTaskWindowingModeSplitScreenPrimary(int taskId, int createMode, boolean toTop, boolean animate, Rect initialBounds, boolean showRecents) throws RemoteException {
            return false;
        }

        public void offsetPinnedStackBounds(int stackId, Rect compareBounds, int xOffset, int yOffset, int animationDuration) throws RemoteException {
        }

        public void removeStacksInWindowingModes(int[] windowingModes) throws RemoteException {
        }

        public void removeStacksWithActivityTypes(int[] activityTypes) throws RemoteException {
        }

        public List<StackInfo> getAllStackInfos() throws RemoteException {
            return null;
        }

        public StackInfo getStackInfo(int windowingMode, int activityType) throws RemoteException {
            return null;
        }

        public void setLockScreenShown(boolean showingKeyguard, boolean showingAod) throws RemoteException {
        }

        public Bundle getAssistContextExtras(int requestType) throws RemoteException {
            return null;
        }

        public boolean launchAssistIntent(Intent intent, int requestType, String hint, int userHandle, Bundle args) throws RemoteException {
            return false;
        }

        public boolean requestAssistContextExtras(int requestType, IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, boolean focused, boolean newSessionId) throws RemoteException {
            return false;
        }

        public boolean requestAutofillData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
            return false;
        }

        public boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException {
            return false;
        }

        public boolean showAssistFromActivity(IBinder token, Bundle args) throws RemoteException {
            return false;
        }

        public boolean isRootVoiceInteraction(IBinder token) throws RemoteException {
            return false;
        }

        public void showLockTaskEscapeMessage(IBinder token) throws RemoteException {
        }

        public void keyguardGoingAway(int flags) throws RemoteException {
        }

        public ComponentName getActivityClassForToken(IBinder token) throws RemoteException {
            return null;
        }

        public String getPackageForToken(IBinder token) throws RemoteException {
            return null;
        }

        public void positionTaskInStack(int taskId, int stackId, int position) throws RemoteException {
        }

        public void reportSizeConfigurations(IBinder token, int[] horizontalSizeConfiguration, int[] verticalSizeConfigurations, int[] smallestWidthConfigurations) throws RemoteException {
        }

        public void dismissSplitScreenMode(boolean toTop) throws RemoteException {
        }

        public void dismissPip(boolean animate, int animationDuration) throws RemoteException {
        }

        public void suppressResizeConfigChanges(boolean suppress) throws RemoteException {
        }

        public void moveTasksToFullscreenStack(int fromStackId, boolean onTop) throws RemoteException {
        }

        public boolean moveTopActivityToPinnedStack(int stackId, Rect bounds) throws RemoteException {
            return false;
        }

        public boolean isInMultiWindowMode(IBinder token) throws RemoteException {
            return false;
        }

        public boolean isInPictureInPictureMode(IBinder token) throws RemoteException {
            return false;
        }

        public boolean enterPictureInPictureMode(IBinder token, PictureInPictureParams params) throws RemoteException {
            return false;
        }

        public void setPictureInPictureParams(IBinder token, PictureInPictureParams params) throws RemoteException {
        }

        public int getMaxNumPictureInPictureActions(IBinder token) throws RemoteException {
            return 0;
        }

        public IBinder getUriPermissionOwnerForActivity(IBinder activityToken) throws RemoteException {
            return null;
        }

        public void resizeDockedStack(Rect dockedBounds, Rect tempDockedTaskBounds, Rect tempDockedTaskInsetBounds, Rect tempOtherTaskBounds, Rect tempOtherTaskInsetBounds) throws RemoteException {
        }

        public void setSplitScreenResizing(boolean resizing) throws RemoteException {
        }

        public int setVrMode(IBinder token, boolean enabled, ComponentName packageName) throws RemoteException {
            return 0;
        }

        public void startLocalVoiceInteraction(IBinder token, Bundle options) throws RemoteException {
        }

        public void stopLocalVoiceInteraction(IBinder token) throws RemoteException {
        }

        public boolean supportsLocalVoiceInteraction() throws RemoteException {
            return false;
        }

        public void notifyPinnedStackAnimationStarted() throws RemoteException {
        }

        public void notifyPinnedStackAnimationEnded() throws RemoteException {
        }

        public ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
            return null;
        }

        public void resizePinnedStack(Rect pinnedBounds, Rect tempPinnedTaskBounds) throws RemoteException {
        }

        public boolean updateDisplayOverrideConfiguration(Configuration values, int displayId) throws RemoteException {
            return false;
        }

        public void dismissKeyguard(IBinder token, IKeyguardDismissCallback callback, CharSequence message) throws RemoteException {
        }

        public void cancelTaskWindowTransition(int taskId) throws RemoteException {
        }

        public TaskSnapshot getTaskSnapshot(int taskId, boolean reducedResolution) throws RemoteException {
            return null;
        }

        public void setDisablePreviewScreenshots(IBinder token, boolean disable) throws RemoteException {
        }

        public int getLastResumedActivityUserId() throws RemoteException {
            return 0;
        }

        public boolean updateConfiguration(Configuration values) throws RemoteException {
            return false;
        }

        public void updateLockTaskFeatures(int userId, int flags) throws RemoteException {
        }

        public void setShowWhenLocked(IBinder token, boolean showWhenLocked) throws RemoteException {
        }

        public void setInheritShowWhenLocked(IBinder token, boolean setInheritShownWhenLocked) throws RemoteException {
        }

        public void setTurnScreenOn(IBinder token, boolean turnScreenOn) throws RemoteException {
        }

        public void registerRemoteAnimations(IBinder token, RemoteAnimationDefinition definition) throws RemoteException {
        }

        public void registerRemoteAnimationForNextActivityStart(String packageName, RemoteAnimationAdapter adapter) throws RemoteException {
        }

        public void registerRemoteAnimationsForDisplay(int displayId, RemoteAnimationDefinition definition) throws RemoteException {
        }

        public void alwaysShowUnsupportedCompileSdkWarning(ComponentName activity) throws RemoteException {
        }

        public void setVrThread(int tid) throws RemoteException {
        }

        public void setPersistentVrThread(int tid) throws RemoteException {
        }

        public void stopAppSwitches() throws RemoteException {
        }

        public void resumeAppSwitches() throws RemoteException {
        }

        public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
        }

        public void setVoiceKeepAwake(IVoiceInteractionSession session, boolean keepAwake) throws RemoteException {
        }

        public int getPackageScreenCompatMode(String packageName) throws RemoteException {
            return 0;
        }

        public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
        }

        public boolean getPackageAskScreenCompat(String packageName) throws RemoteException {
            return false;
        }

        public void setPackageAskScreenCompat(String packageName, boolean ask) throws RemoteException {
        }

        public void clearLaunchParamsForPackages(List<String> list) throws RemoteException {
        }

        public void setDisplayToSingleTaskInstance(int displayId) throws RemoteException {
        }

        public void restartActivityProcessIfVisible(IBinder activityToken) throws RemoteException {
        }

        public void onBackPressedOnTaskRoot(IBinder activityToken, IRequestFinishCallback callback) throws RemoteException {
        }

        public int handleFreeformModeRequst(IBinder token, int cmd) throws RemoteException {
            return 0;
        }

        public Intent getTopVisibleActivity() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IActivityTaskManager {
        private static final String DESCRIPTOR = "android.app.IActivityTaskManager";
        static final int TRANSACTION_activityDestroyed = 22;
        static final int TRANSACTION_activityIdle = 17;
        static final int TRANSACTION_activityPaused = 20;
        static final int TRANSACTION_activityRelaunched = 23;
        static final int TRANSACTION_activityResumed = 18;
        static final int TRANSACTION_activitySlept = 24;
        static final int TRANSACTION_activityStopped = 21;
        static final int TRANSACTION_activityTopResumedStateLost = 19;
        static final int TRANSACTION_addAppTask = 75;
        static final int TRANSACTION_alwaysShowUnsupportedCompileSdkWarning = 147;
        static final int TRANSACTION_cancelRecentsAnimation = 60;
        static final int TRANSACTION_cancelTaskWindowTransition = 135;
        static final int TRANSACTION_clearLaunchParamsForPackages = 158;
        static final int TRANSACTION_convertFromTranslucent = 43;
        static final int TRANSACTION_convertToTranslucent = 44;
        static final int TRANSACTION_dismissKeyguard = 134;
        static final int TRANSACTION_dismissPip = 113;
        static final int TRANSACTION_dismissSplitScreenMode = 112;
        static final int TRANSACTION_enterPictureInPictureMode = 119;
        static final int TRANSACTION_finishActivity = 15;
        static final int TRANSACTION_finishActivityAffinity = 16;
        static final int TRANSACTION_finishSubActivity = 38;
        static final int TRANSACTION_finishVoiceTask = 71;
        static final int TRANSACTION_getActivityClassForToken = 108;
        static final int TRANSACTION_getActivityDisplayId = 47;
        static final int TRANSACTION_getActivityOptions = 67;
        static final int TRANSACTION_getAllStackInfos = 96;
        static final int TRANSACTION_getAppTaskThumbnailSize = 76;
        static final int TRANSACTION_getAppTasks = 68;
        static final int TRANSACTION_getAssistContextExtras = 99;
        static final int TRANSACTION_getCallingActivity = 28;
        static final int TRANSACTION_getCallingPackage = 27;
        static final int TRANSACTION_getDeviceConfigurationInfo = 131;
        static final int TRANSACTION_getFilteredTasks = 33;
        static final int TRANSACTION_getFocusedStackInfo = 58;
        static final int TRANSACTION_getFrontActivityScreenCompatMode = 25;
        static final int TRANSACTION_getLastResumedActivityUserId = 138;
        static final int TRANSACTION_getLaunchedFromPackage = 55;
        static final int TRANSACTION_getLaunchedFromUid = 54;
        static final int TRANSACTION_getLockTaskModeState = 65;
        static final int TRANSACTION_getMaxNumPictureInPictureActions = 121;
        static final int TRANSACTION_getPackageAskScreenCompat = 156;
        static final int TRANSACTION_getPackageForToken = 109;
        static final int TRANSACTION_getPackageScreenCompatMode = 154;
        static final int TRANSACTION_getRecentTasks = 39;
        static final int TRANSACTION_getRequestedOrientation = 42;
        static final int TRANSACTION_getStackInfo = 97;
        static final int TRANSACTION_getTaskBounds = 59;
        static final int TRANSACTION_getTaskDescription = 52;
        static final int TRANSACTION_getTaskDescriptionIcon = 80;
        static final int TRANSACTION_getTaskForActivity = 37;
        static final int TRANSACTION_getTaskSnapshot = 136;
        static final int TRANSACTION_getTasks = 32;
        static final int TRANSACTION_getTopVisibleActivity = 163;
        static final int TRANSACTION_getUriPermissionOwnerForActivity = 122;
        static final int TRANSACTION_handleFreeformModeRequst = 162;
        static final int TRANSACTION_isActivityStartAllowedOnDisplay = 13;
        static final int TRANSACTION_isAssistDataAllowedOnCurrentActivity = 103;
        static final int TRANSACTION_isImmersive = 48;
        static final int TRANSACTION_isInLockTaskMode = 64;
        static final int TRANSACTION_isInMultiWindowMode = 117;
        static final int TRANSACTION_isInPictureInPictureMode = 118;
        static final int TRANSACTION_isRootVoiceInteraction = 105;
        static final int TRANSACTION_isTopActivityImmersive = 50;
        static final int TRANSACTION_isTopOfTask = 72;
        static final int TRANSACTION_keyguardGoingAway = 107;
        static final int TRANSACTION_launchAssistIntent = 100;
        static final int TRANSACTION_moveActivityTaskToBack = 51;
        static final int TRANSACTION_moveStackToDisplay = 87;
        static final int TRANSACTION_moveTaskToFront = 36;
        static final int TRANSACTION_moveTaskToStack = 90;
        static final int TRANSACTION_moveTasksToFullscreenStack = 115;
        static final int TRANSACTION_moveTopActivityToPinnedStack = 116;
        static final int TRANSACTION_navigateUpTo = 35;
        static final int TRANSACTION_notifyActivityDrawn = 45;
        static final int TRANSACTION_notifyEnterAnimationComplete = 74;
        static final int TRANSACTION_notifyLaunchTaskBehindComplete = 73;
        static final int TRANSACTION_notifyPinnedStackAnimationEnded = 130;
        static final int TRANSACTION_notifyPinnedStackAnimationStarted = 129;
        static final int TRANSACTION_offsetPinnedStackBounds = 93;
        static final int TRANSACTION_onBackPressedOnTaskRoot = 161;
        static final int TRANSACTION_overridePendingTransition = 53;
        static final int TRANSACTION_positionTaskInStack = 110;
        static final int TRANSACTION_registerRemoteAnimationForNextActivityStart = 145;
        static final int TRANSACTION_registerRemoteAnimations = 144;
        static final int TRANSACTION_registerRemoteAnimationsForDisplay = 146;
        static final int TRANSACTION_registerTaskStackListener = 82;
        static final int TRANSACTION_releaseActivityInstance = 77;
        static final int TRANSACTION_releaseSomeActivities = 79;
        static final int TRANSACTION_removeAllVisibleRecentTasks = 31;
        static final int TRANSACTION_removeStack = 88;
        static final int TRANSACTION_removeStacksInWindowingModes = 94;
        static final int TRANSACTION_removeStacksWithActivityTypes = 95;
        static final int TRANSACTION_removeTask = 30;
        static final int TRANSACTION_reportActivityFullyDrawn = 46;
        static final int TRANSACTION_reportAssistContextExtras = 56;
        static final int TRANSACTION_reportSizeConfigurations = 111;
        static final int TRANSACTION_requestAssistContextExtras = 101;
        static final int TRANSACTION_requestAutofillData = 102;
        static final int TRANSACTION_requestStartActivityPermissionToken = 78;
        static final int TRANSACTION_resizeDockedStack = 123;
        static final int TRANSACTION_resizePinnedStack = 132;
        static final int TRANSACTION_resizeStack = 91;
        static final int TRANSACTION_resizeTask = 86;
        static final int TRANSACTION_restartActivityProcessIfVisible = 160;
        static final int TRANSACTION_resumeAppSwitches = 151;
        static final int TRANSACTION_setActivityController = 152;
        static final int TRANSACTION_setDisablePreviewScreenshots = 137;
        static final int TRANSACTION_setDisplayToSingleTaskInstance = 159;
        static final int TRANSACTION_setFocusedStack = 57;
        static final int TRANSACTION_setFocusedTask = 29;
        static final int TRANSACTION_setFrontActivityScreenCompatMode = 26;
        static final int TRANSACTION_setImmersive = 49;
        static final int TRANSACTION_setInheritShowWhenLocked = 142;
        static final int TRANSACTION_setLockScreenShown = 98;
        static final int TRANSACTION_setPackageAskScreenCompat = 157;
        static final int TRANSACTION_setPackageScreenCompatMode = 155;
        static final int TRANSACTION_setPersistentVrThread = 149;
        static final int TRANSACTION_setPictureInPictureParams = 120;
        static final int TRANSACTION_setRequestedOrientation = 41;
        static final int TRANSACTION_setShowWhenLocked = 141;
        static final int TRANSACTION_setSplitScreenResizing = 124;
        static final int TRANSACTION_setTaskDescription = 66;
        static final int TRANSACTION_setTaskResizeable = 84;
        static final int TRANSACTION_setTaskWindowingMode = 89;
        static final int TRANSACTION_setTaskWindowingModeSplitScreenPrimary = 92;
        static final int TRANSACTION_setTurnScreenOn = 143;
        static final int TRANSACTION_setVoiceKeepAwake = 153;
        static final int TRANSACTION_setVrMode = 125;
        static final int TRANSACTION_setVrThread = 148;
        static final int TRANSACTION_shouldUpRecreateTask = 34;
        static final int TRANSACTION_showAssistFromActivity = 104;
        static final int TRANSACTION_showLockTaskEscapeMessage = 106;
        static final int TRANSACTION_startActivities = 2;
        static final int TRANSACTION_startActivity = 1;
        static final int TRANSACTION_startActivityAndWait = 6;
        static final int TRANSACTION_startActivityAsCaller = 12;
        static final int TRANSACTION_startActivityAsUser = 3;
        static final int TRANSACTION_startActivityFromRecents = 11;
        static final int TRANSACTION_startActivityIntentSender = 5;
        static final int TRANSACTION_startActivityWithConfig = 7;
        static final int TRANSACTION_startAssistantActivity = 9;
        static final int TRANSACTION_startInPlaceAnimationOnFrontMostApplication = 81;
        static final int TRANSACTION_startLocalVoiceInteraction = 126;
        static final int TRANSACTION_startLockTaskModeByToken = 61;
        static final int TRANSACTION_startNextMatchingActivity = 4;
        static final int TRANSACTION_startRecentsActivity = 10;
        static final int TRANSACTION_startSystemLockTaskMode = 69;
        static final int TRANSACTION_startVoiceActivity = 8;
        static final int TRANSACTION_stopAppSwitches = 150;
        static final int TRANSACTION_stopLocalVoiceInteraction = 127;
        static final int TRANSACTION_stopLockTaskModeByToken = 62;
        static final int TRANSACTION_stopSystemLockTaskMode = 70;
        static final int TRANSACTION_supportsLocalVoiceInteraction = 128;
        static final int TRANSACTION_suppressResizeConfigChanges = 114;
        static final int TRANSACTION_toggleFreeformWindowingMode = 85;
        static final int TRANSACTION_unhandledBack = 14;
        static final int TRANSACTION_unregisterTaskStackListener = 83;
        static final int TRANSACTION_updateConfiguration = 139;
        static final int TRANSACTION_updateDisplayOverrideConfiguration = 133;
        static final int TRANSACTION_updateLockTaskFeatures = 140;
        static final int TRANSACTION_updateLockTaskPackages = 63;
        static final int TRANSACTION_willActivityBeVisible = 40;

        private static class Proxy implements IActivityTaskManager {
            public static IActivityTaskManager sDefaultImpl;
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
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public int startActivities(IApplicationThread caller, String callingPackage, Intent[] intents, String[] resolvedTypes, IBinder resultTo, Bundle options, int userId) throws RemoteException {
                Throwable th;
                Intent[] intentArr;
                String[] strArr;
                IBinder iBinder;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callingPackage);
                    } catch (Throwable th2) {
                        th = th2;
                        intentArr = intents;
                        strArr = resolvedTypes;
                        iBinder = resultTo;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeTypedArray(intents, 0);
                        try {
                            _data.writeStringArray(resolvedTypes);
                        } catch (Throwable th3) {
                            th = th3;
                            iBinder = resultTo;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeStrongBinder(resultTo);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(userId);
                            if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int startActivities = Stub.getDefaultImpl().startActivities(caller, callingPackage, intents, resolvedTypes, resultTo, options, userId);
                            _reply.recycle();
                            _data.recycle();
                            return startActivities;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        strArr = resolvedTypes;
                        iBinder = resultTo;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = callingPackage;
                    intentArr = intents;
                    strArr = resolvedTypes;
                    iBinder = resultTo;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
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
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public boolean startNextMatchingActivity(IBinder callingActivity, Intent intent, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callingActivity);
                    boolean _result = true;
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
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().startNextMatchingActivity(callingActivity, intent, options);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int startActivityIntentSender(IApplicationThread caller, IIntentSender target, IBinder whitelistToken, Intent fillInIntent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flagsMask, int flagsValues, Bundle options) throws RemoteException {
                IBinder asBinder;
                Throwable th;
                Parcel _reply;
                Intent intent = fillInIntent;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                _data.writeInterfaceToken(Stub.DESCRIPTOR);
                IBinder iBinder = null;
                if (caller != null) {
                    try {
                        asBinder = caller.asBinder();
                    } catch (Throwable th2) {
                        th = th2;
                        _reply = _reply2;
                    }
                } else {
                    asBinder = null;
                }
                try {
                    _data.writeStrongBinder(asBinder);
                    if (target != null) {
                        iBinder = target.asBinder();
                    }
                    _data.writeStrongBinder(iBinder);
                    _data.writeStrongBinder(whitelistToken);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeStrongBinder(resultTo);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    _data.writeInt(flagsMask);
                    _data.writeInt(flagsValues);
                    if (bundle != null) {
                        _data.writeInt(1);
                        bundle.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, _reply2, 0) || Stub.getDefaultImpl() == null) {
                        _reply = _reply2;
                        _reply.readException();
                        int _result = _reply.readInt();
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _reply = _reply2;
                    try {
                        int startActivityIntentSender = Stub.getDefaultImpl().startActivityIntentSender(caller, target, whitelistToken, fillInIntent, resolvedType, resultTo, resultWho, requestCode, flagsMask, flagsValues, options);
                        _reply.recycle();
                        _data.recycle();
                        return startActivityIntentSender;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    _reply = _reply2;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public WaitResult startActivityAndWait(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Throwable th;
                Parcel _reply;
                Intent intent2 = intent;
                ProfilerInfo profilerInfo2 = profilerInfo;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                Parcel _data2;
                try {
                    IBinder asBinder;
                    Parcel _reply3;
                    WaitResult startActivityAndWait;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (caller != null) {
                        try {
                            asBinder = caller.asBinder();
                        } catch (Throwable th2) {
                            th = th2;
                            _reply = _reply2;
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
                    if (!this.mRemote.transact(6, _data, _reply2, 0)) {
                        try {
                            if (Stub.getDefaultImpl() != null) {
                                _reply3 = _reply2;
                                _data2 = _data;
                                try {
                                    startActivityAndWait = Stub.getDefaultImpl().startActivityAndWait(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, userId);
                                    _reply3.recycle();
                                    _data2.recycle();
                                    return startActivityAndWait;
                                } catch (Throwable th3) {
                                    th = th3;
                                    _reply = _reply3;
                                    _reply.recycle();
                                    _data2.recycle();
                                    throw th;
                                }
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            _data2 = _data;
                            _reply = _reply2;
                            _reply.recycle();
                            _data2.recycle();
                            throw th;
                        }
                    }
                    _reply3 = _reply2;
                    _data2 = _data;
                    try {
                        _reply3.readException();
                        if (_reply3.readInt() != 0) {
                            _reply = _reply3;
                            try {
                                startActivityAndWait = (WaitResult) WaitResult.CREATOR.createFromParcel(_reply);
                            } catch (Throwable th5) {
                                th = th5;
                            }
                        } else {
                            _reply = _reply3;
                            startActivityAndWait = null;
                        }
                        _reply.recycle();
                        _data2.recycle();
                        return startActivityAndWait;
                    } catch (Throwable th6) {
                        th = th6;
                        _reply = _reply3;
                        _reply.recycle();
                        _data2.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    _reply = _reply2;
                    _data2 = _data;
                    _reply.recycle();
                    _data2.recycle();
                    throw th;
                }
            }

            public int startActivityWithConfig(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int startFlags, Configuration newConfig, Bundle options, int userId) throws RemoteException {
                Throwable th;
                Intent intent2 = intent;
                Configuration configuration = newConfig;
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
                    _data.writeInt(startFlags);
                    if (configuration != null) {
                        _data.writeInt(1);
                        configuration.writeToParcel(_data, 0);
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
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                        int startActivityWithConfig = Stub.getDefaultImpl().startActivityWithConfig(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, newConfig, options, userId);
                        _reply2.recycle();
                        _data2.recycle();
                        return startActivityWithConfig;
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

            public int startVoiceActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, IVoiceInteractionSession session, IVoiceInteractor interactor, int flags, ProfilerInfo profilerInfo, Bundle options, int userId) throws RemoteException {
                Throwable th;
                Parcel _reply;
                Parcel _data;
                Intent intent2 = intent;
                ProfilerInfo profilerInfo2 = profilerInfo;
                Bundle bundle = options;
                Parcel _data2 = Parcel.obtain();
                Parcel _reply2 = Parcel.obtain();
                _data2.writeInterfaceToken(Stub.DESCRIPTOR);
                _data2.writeString(callingPackage);
                _data2.writeInt(callingPid);
                _data2.writeInt(callingUid);
                if (intent2 != null) {
                    try {
                        _data2.writeInt(1);
                        intent2.writeToParcel(_data2, 0);
                    } catch (Throwable th2) {
                        th = th2;
                        _reply = _reply2;
                        _data = _data2;
                    }
                } else {
                    _data2.writeInt(0);
                }
                _data2.writeString(resolvedType);
                IBinder iBinder = null;
                try {
                    _data2.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (interactor != null) {
                        iBinder = interactor.asBinder();
                    }
                    _data2.writeStrongBinder(iBinder);
                    _data2.writeInt(flags);
                    if (profilerInfo2 != null) {
                        _data2.writeInt(1);
                        profilerInfo2.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    if (bundle != null) {
                        _data2.writeInt(1);
                        bundle.writeToParcel(_data2, 0);
                    } else {
                        _data2.writeInt(0);
                    }
                    _data2.writeInt(userId);
                    if (this.mRemote.transact(8, _data2, _reply2, 0) || Stub.getDefaultImpl() == null) {
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
                        int startVoiceActivity = Stub.getDefaultImpl().startVoiceActivity(callingPackage, callingPid, callingUid, intent, resolvedType, session, interactor, flags, profilerInfo, options, userId);
                        _reply.recycle();
                        _data.recycle();
                        return startVoiceActivity;
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

            public int startAssistantActivity(String callingPackage, int callingPid, int callingUid, Intent intent, String resolvedType, Bundle options, int userId) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                Intent intent2 = intent;
                Bundle bundle = options;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPackage);
                    } catch (Throwable th2) {
                        th = th2;
                        i = callingPid;
                        i2 = callingUid;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(callingPid);
                        try {
                            _data.writeInt(callingUid);
                            if (intent2 != null) {
                                _data.writeInt(1);
                                intent2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeString(resolvedType);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(userId);
                            if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                int _result = _reply.readInt();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            int startAssistantActivity = Stub.getDefaultImpl().startAssistantActivity(callingPackage, callingPid, callingUid, intent, resolvedType, options, userId);
                            _reply.recycle();
                            _data.recycle();
                            return startAssistantActivity;
                        } catch (Throwable th3) {
                            th = th3;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = callingUid;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    String str = callingPackage;
                    i = callingPid;
                    i2 = callingUid;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
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
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (!i.transact(11, _data, _reply, 0)) {
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

            public int startActivityAsCaller(IApplicationThread caller, String callingPackage, Intent intent, String resolvedType, IBinder resultTo, String resultWho, int requestCode, int flags, ProfilerInfo profilerInfo, Bundle options, IBinder permissionToken, boolean ignoreTargetSecurity, int userId) throws RemoteException {
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
                    int i = 1;
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
                    _data.writeStrongBinder(permissionToken);
                    if (!ignoreTargetSecurity) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply2 = _reply;
                        _data2 = _data;
                        _reply2.readException();
                        i = _reply2.readInt();
                        _reply2.recycle();
                        _data2.recycle();
                        return i;
                    }
                    _reply2 = _reply;
                    _data2 = _data;
                    try {
                        int startActivityAsCaller = Stub.getDefaultImpl().startActivityAsCaller(caller, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, flags, profilerInfo, options, permissionToken, ignoreTargetSecurity, userId);
                        _reply2.recycle();
                        _data2.recycle();
                        return startActivityAsCaller;
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

            public boolean isActivityStartAllowedOnDisplay(int displayId, Intent intent, String resolvedType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    boolean _result = true;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(resolvedType);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isActivityStartAllowedOnDisplay(displayId, intent, resolvedType, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unhandledBack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public boolean finishActivityAffinity(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().finishActivityAffinity(token);
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

            public void activityIdle(IBinder token, Configuration config, boolean stopProfiling) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    int i = 0;
                    if (config != null) {
                        _data.writeInt(1);
                        config.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (stopProfiling) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(17, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().activityIdle(token, config, stopProfiling);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void activityResumed(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().activityResumed(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void activityTopResumedStateLost() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().activityTopResumedStateLost();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void activityPaused(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().activityPaused(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void activityStopped(IBinder token, Bundle state, PersistableBundle persistentState, CharSequence description) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (persistentState != null) {
                        _data.writeInt(1);
                        persistentState.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (description != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(description, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().activityStopped(token, state, persistentState, description);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void activityDestroyed(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(22, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().activityDestroyed(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void activityRelaunched(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().activityRelaunched(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void activitySlept(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(24, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().activitySlept(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public int getFrontActivityScreenCompatMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getFrontActivityScreenCompatMode();
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

            public void setFrontActivityScreenCompatMode(int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFrontActivityScreenCompatMode(mode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getCallingPackage(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    String str = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getCallingPackage(token);
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

            public ComponentName getCallingActivity(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    ComponentName componentName = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getCallingActivity(token);
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFocusedTask(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setFocusedTask(taskId);
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
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
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

            public void removeAllVisibleRecentTasks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeAllVisibleRecentTasks();
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
                    List<RunningTaskInfo> list = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
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
                    List<RunningTaskInfo> list = 33;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
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

            public boolean shouldUpRecreateTask(IBinder token, String destAffinity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(destAffinity);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(34, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().shouldUpRecreateTask(token, destAffinity);
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

            public boolean navigateUpTo(IBinder token, Intent target, int resultCode, Intent resultData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _result = true;
                    if (target != null) {
                        _data.writeInt(1);
                        target.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(resultCode);
                    if (resultData != null) {
                        _data.writeInt(1);
                        resultData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().navigateUpTo(token, target, resultCode, resultData);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void moveTaskToFront(IApplicationThread app, String callingPackage, int task, int flags, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    _data.writeString(callingPackage);
                    _data.writeInt(task);
                    _data.writeInt(flags);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveTaskToFront(app, callingPackage, task, flags, options);
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
                    if (!i.transact(37, _data, _reply, 0)) {
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

            public void finishSubActivity(IBinder token, String resultWho, int requestCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(resultWho);
                    _data.writeInt(requestCode);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishSubActivity(token, resultWho, requestCode);
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
                    ParceledListSlice parceledListSlice = 39;
                    if (!this.mRemote.transact(39, _data, _reply, 0)) {
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

            public boolean willActivityBeVisible(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(40, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().willActivityBeVisible(token);
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

            public void setRequestedOrientation(IBinder token, int requestedOrientation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(requestedOrientation);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public int getRequestedOrientation(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    int i = 42;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getRequestedOrientation(token);
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

            public boolean convertFromTranslucent(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().convertFromTranslucent(token);
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

            public boolean convertToTranslucent(IBinder token, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _result = true;
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().convertToTranslucent(token, options);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyActivityDrawn(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyActivityDrawn(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportActivityFullyDrawn(IBinder token, boolean restoredFromBundle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(restoredFromBundle ? 1 : 0);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportActivityFullyDrawn(token, restoredFromBundle);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getActivityDisplayId(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    int i = 47;
                    if (!this.mRemote.transact(47, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getActivityDisplayId(activityToken);
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

            public boolean isImmersive(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(48, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isImmersive(token);
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

            public void setImmersive(IBinder token, boolean immersive) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(immersive ? 1 : 0);
                    if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setImmersive(token, immersive);
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
                    if (!this.mRemote.transact(50, _data, _reply, 0)) {
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

            public boolean moveActivityTaskToBack(IBinder token, boolean nonRoot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _result = true;
                    _data.writeInt(nonRoot ? 1 : 0);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public TaskDescription getTaskDescription(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    TaskDescription taskDescription = 52;
                    if (!this.mRemote.transact(52, _data, _reply, 0)) {
                        taskDescription = Stub.getDefaultImpl();
                        if (taskDescription != 0) {
                            taskDescription = Stub.getDefaultImpl().getTaskDescription(taskId);
                            return taskDescription;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        taskDescription = (TaskDescription) TaskDescription.CREATOR.createFromParcel(_reply);
                    } else {
                        taskDescription = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return taskDescription;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void overridePendingTransition(IBinder token, String packageName, int enterAnim, int exitAnim) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeString(packageName);
                    _data.writeInt(enterAnim);
                    _data.writeInt(exitAnim);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().overridePendingTransition(token, packageName, enterAnim, exitAnim);
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
                    int i = 54;
                    if (!this.mRemote.transact(54, _data, _reply, 0)) {
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

            public String getLaunchedFromPackage(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    String str = 55;
                    if (!this.mRemote.transact(55, _data, _reply, 0)) {
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

            public void reportAssistContextExtras(IBinder token, Bundle extras, AssistStructure structure, AssistContent content, Uri referrer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (structure != null) {
                        _data.writeInt(1);
                        structure.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (content != null) {
                        _data.writeInt(1);
                        content.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (referrer != null) {
                        _data.writeInt(1);
                        referrer.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportAssistContextExtras(token, extras, structure, content, referrer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setFocusedStack(int stackId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    StackInfo stackInfo = 58;
                    if (!this.mRemote.transact(58, _data, _reply, 0)) {
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

            public Rect getTaskBounds(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    Rect rect = 59;
                    if (!this.mRemote.transact(59, _data, _reply, 0)) {
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

            public void cancelRecentsAnimation(boolean restoreHomeStackPosition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(restoreHomeStackPosition ? 1 : 0);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void startLockTaskModeByToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startLockTaskModeByToken(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopLockTaskModeByToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(62, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopLockTaskModeByToken(token);
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
                    if (this.mRemote.transact(63, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public boolean isInLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(64, _data, _reply, 0)) {
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

            public int getLockTaskModeState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 65;
                    if (!this.mRemote.transact(65, _data, _reply, 0)) {
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

            public void setTaskDescription(IBinder token, TaskDescription values) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (values != null) {
                        _data.writeInt(1);
                        values.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(66, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTaskDescription(token, values);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getActivityOptions(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    Bundle bundle = 67;
                    if (!this.mRemote.transact(67, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getActivityOptions(token);
                            return bundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        bundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<IBinder> getAppTasks(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    List<IBinder> list = 68;
                    if (!this.mRemote.transact(68, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getAppTasks(callingPackage);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createBinderArrayList();
                    List<IBinder> _result = list;
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
                    if (this.mRemote.transact(69, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void stopSystemLockTaskMode() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(70, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopSystemLockTaskMode();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void finishVoiceTask(IVoiceInteractionSession session) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    if (this.mRemote.transact(71, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().finishVoiceTask(session);
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
                    if (!this.mRemote.transact(72, _data, _reply, 0)) {
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

            public void notifyLaunchTaskBehindComplete(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(73, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyLaunchTaskBehindComplete(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyEnterAnimationComplete(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(74, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyEnterAnimationComplete(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int addAppTask(IBinder activityToken, Intent intent, TaskDescription description, Bitmap thumbnail) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    int i = 0;
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (description != null) {
                        _data.writeInt(1);
                        description.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (thumbnail != null) {
                        _data.writeInt(1);
                        thumbnail.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().addAppTask(activityToken, intent, description, thumbnail);
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

            public Point getAppTaskThumbnailSize() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Point point = 76;
                    if (!this.mRemote.transact(76, _data, _reply, 0)) {
                        point = Stub.getDefaultImpl();
                        if (point != 0) {
                            point = Stub.getDefaultImpl().getAppTaskThumbnailSize();
                            return point;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        point = (Point) Point.CREATOR.createFromParcel(_reply);
                    } else {
                        point = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return point;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean releaseActivityInstance(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(77, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().releaseActivityInstance(token);
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

            public IBinder requestStartActivityPermissionToken(IBinder delegatorToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(delegatorToken);
                    IBinder iBinder = 78;
                    if (!this.mRemote.transact(78, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != 0) {
                            iBinder = Stub.getDefaultImpl().requestStartActivityPermissionToken(delegatorToken);
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

            public void releaseSomeActivities(IApplicationThread app) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(app != null ? app.asBinder() : null);
                    if (this.mRemote.transact(79, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().releaseSomeActivities(app);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bitmap getTaskDescriptionIcon(String filename, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(filename);
                    _data.writeInt(userId);
                    Bitmap bitmap = 80;
                    if (!this.mRemote.transact(80, _data, _reply, 0)) {
                        bitmap = Stub.getDefaultImpl();
                        if (bitmap != 0) {
                            bitmap = Stub.getDefaultImpl().getTaskDescriptionIcon(filename, userId);
                            return bitmap;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bitmap = (Bitmap) Bitmap.CREATOR.createFromParcel(_reply);
                    } else {
                        bitmap = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bitmap;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startInPlaceAnimationOnFrontMostApplication(Bundle opts) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (opts != null) {
                        _data.writeInt(1);
                        opts.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(81, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startInPlaceAnimationOnFrontMostApplication(opts);
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
                    if (this.mRemote.transact(82, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (this.mRemote.transact(83, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void setTaskResizeable(int taskId, int resizeableMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(resizeableMode);
                    if (this.mRemote.transact(84, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void toggleFreeformWindowingMode(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(85, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().toggleFreeformWindowingMode(token);
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
                    if (this.mRemote.transact(86, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void moveStackToDisplay(int stackId, int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(87, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveStackToDisplay(stackId, displayId);
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
                    if (this.mRemote.transact(88, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void setTaskWindowingMode(int taskId, int windowingMode, boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(windowingMode);
                    _data.writeInt(toTop ? 1 : 0);
                    if (this.mRemote.transact(89, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTaskWindowingMode(taskId, windowingMode, toTop);
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
                    if (this.mRemote.transact(90, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                                if (this.mRemote.transact(91, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public boolean setTaskWindowingModeSplitScreenPrimary(int taskId, int createMode, boolean toTop, boolean animate, Rect initialBounds, boolean showRecents) throws RemoteException {
                Throwable th;
                int i;
                Rect rect = initialBounds;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    boolean _result;
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(taskId);
                        try {
                            _data.writeInt(createMode);
                            _result = true;
                            _data.writeInt(toTop ? 1 : 0);
                            _data.writeInt(animate ? 1 : 0);
                            if (rect != null) {
                                _data.writeInt(1);
                                rect.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            _data.writeInt(showRecents ? 1 : 0);
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        i = createMode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(92, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() == 0) {
                                _result = false;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().setTaskWindowingModeSplitScreenPrimary(taskId, createMode, toTop, animate, initialBounds, showRecents);
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
                    int i2 = taskId;
                    i = createMode;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void offsetPinnedStackBounds(int stackId, Rect compareBounds, int xOffset, int yOffset, int animationDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(stackId);
                    if (compareBounds != null) {
                        _data.writeInt(1);
                        compareBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(xOffset);
                    _data.writeInt(yOffset);
                    _data.writeInt(animationDuration);
                    if (this.mRemote.transact(93, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().offsetPinnedStackBounds(stackId, compareBounds, xOffset, yOffset, animationDuration);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeStacksInWindowingModes(int[] windowingModes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(windowingModes);
                    if (this.mRemote.transact(94, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeStacksInWindowingModes(windowingModes);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeStacksWithActivityTypes(int[] activityTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(activityTypes);
                    if (this.mRemote.transact(95, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeStacksWithActivityTypes(activityTypes);
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
                    List<StackInfo> list = 96;
                    if (!this.mRemote.transact(96, _data, _reply, 0)) {
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

            public StackInfo getStackInfo(int windowingMode, int activityType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(windowingMode);
                    _data.writeInt(activityType);
                    StackInfo stackInfo = 97;
                    if (!this.mRemote.transact(97, _data, _reply, 0)) {
                        stackInfo = Stub.getDefaultImpl();
                        if (stackInfo != 0) {
                            stackInfo = Stub.getDefaultImpl().getStackInfo(windowingMode, activityType);
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

            public void setLockScreenShown(boolean showingKeyguard, boolean showingAod) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    _data.writeInt(showingKeyguard ? 1 : 0);
                    if (!showingAod) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(98, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setLockScreenShown(showingKeyguard, showingAod);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getAssistContextExtras(int requestType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(requestType);
                    Bundle bundle = 99;
                    if (!this.mRemote.transact(99, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getAssistContextExtras(requestType);
                            return bundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        bundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean launchAssistIntent(Intent intent, int requestType, String hint, int userHandle, Bundle args) throws RemoteException {
                Throwable th;
                int i;
                String str;
                Intent intent2 = intent;
                Bundle bundle = args;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (intent2 != null) {
                        _data.writeInt(1);
                        intent2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    try {
                        _data.writeInt(requestType);
                        try {
                            _data.writeString(hint);
                        } catch (Throwable th2) {
                            th = th2;
                            i = userHandle;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(userHandle);
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                if (this.mRemote.transact(100, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    if (_reply.readInt() == 0) {
                                        _result = false;
                                    }
                                    _reply.recycle();
                                    _data.recycle();
                                    return _result;
                                }
                                _result = Stub.getDefaultImpl().launchAssistIntent(intent, requestType, hint, userHandle, args);
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
                        str = hint;
                        i = userHandle;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i2 = requestType;
                    str = hint;
                    i = userHandle;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean requestAssistContextExtras(int requestType, IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, boolean focused, boolean newSessionId) throws RemoteException {
                Throwable th;
                IBinder iBinder;
                Bundle bundle = receiverExtras;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(requestType);
                        _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                        boolean _result = true;
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        try {
                            _data.writeStrongBinder(activityToken);
                            _data.writeInt(focused ? 1 : 0);
                            _data.writeInt(newSessionId ? 1 : 0);
                        } catch (Throwable th2) {
                            th = th2;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            if (this.mRemote.transact(101, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().requestAssistContextExtras(requestType, receiver, receiverExtras, activityToken, focused, newSessionId);
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
                        iBinder = activityToken;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i = requestType;
                    iBinder = activityToken;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean requestAutofillData(IAssistDataReceiver receiver, Bundle receiverExtras, IBinder activityToken, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
                    boolean _result = true;
                    if (receiverExtras != null) {
                        _data.writeInt(1);
                        receiverExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(activityToken);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(102, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().requestAutofillData(receiver, receiverExtras, activityToken, flags);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(103, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAssistDataAllowedOnCurrentActivity();
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

            public boolean showAssistFromActivity(IBinder token, Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _result = true;
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(104, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().showAssistFromActivity(token, args);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isRootVoiceInteraction(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(105, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isRootVoiceInteraction(token);
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

            public void showLockTaskEscapeMessage(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(106, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().showLockTaskEscapeMessage(token);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void keyguardGoingAway(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(107, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().keyguardGoingAway(flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getActivityClassForToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    ComponentName componentName = 108;
                    if (!this.mRemote.transact(108, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getActivityClassForToken(token);
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPackageForToken(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    String str = 109;
                    if (!this.mRemote.transact(109, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPackageForToken(token);
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

            public void positionTaskInStack(int taskId, int stackId, int position) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeInt(stackId);
                    _data.writeInt(position);
                    if (this.mRemote.transact(110, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void reportSizeConfigurations(IBinder token, int[] horizontalSizeConfiguration, int[] verticalSizeConfigurations, int[] smallestWidthConfigurations) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeIntArray(horizontalSizeConfiguration);
                    _data.writeIntArray(verticalSizeConfigurations);
                    _data.writeIntArray(smallestWidthConfigurations);
                    if (this.mRemote.transact(111, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportSizeConfigurations(token, horizontalSizeConfiguration, verticalSizeConfigurations, smallestWidthConfigurations);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dismissSplitScreenMode(boolean toTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(toTop ? 1 : 0);
                    if (this.mRemote.transact(112, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dismissSplitScreenMode(toTop);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dismissPip(boolean animate, int animationDuration) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(animate ? 1 : 0);
                    _data.writeInt(animationDuration);
                    if (this.mRemote.transact(113, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dismissPip(animate, animationDuration);
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
                    if (this.mRemote.transact(114, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void moveTasksToFullscreenStack(int fromStackId, boolean onTop) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fromStackId);
                    _data.writeInt(onTop ? 1 : 0);
                    if (this.mRemote.transact(115, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveTasksToFullscreenStack(fromStackId, onTop);
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
                    if (this.mRemote.transact(116, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public boolean isInMultiWindowMode(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(117, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInMultiWindowMode(token);
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

            public boolean isInPictureInPictureMode(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(118, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInPictureInPictureMode(token);
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

            public boolean enterPictureInPictureMode(IBinder token, PictureInPictureParams params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    boolean _result = true;
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(119, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().enterPictureInPictureMode(token, params);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPictureInPictureParams(IBinder token, PictureInPictureParams params) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (params != null) {
                        _data.writeInt(1);
                        params.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(120, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPictureInPictureParams(token, params);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMaxNumPictureInPictureActions(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    int i = 121;
                    if (!this.mRemote.transact(121, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getMaxNumPictureInPictureActions(token);
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

            public IBinder getUriPermissionOwnerForActivity(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    IBinder iBinder = 122;
                    if (!this.mRemote.transact(122, _data, _reply, 0)) {
                        iBinder = Stub.getDefaultImpl();
                        if (iBinder != 0) {
                            iBinder = Stub.getDefaultImpl().getUriPermissionOwnerForActivity(activityToken);
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
                    if (this.mRemote.transact(123, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void setSplitScreenResizing(boolean resizing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(resizing ? 1 : 0);
                    if (this.mRemote.transact(124, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSplitScreenResizing(resizing);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int setVrMode(IBinder token, boolean enabled, ComponentName packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    int i = 0;
                    _data.writeInt(enabled ? 1 : 0);
                    if (packageName != null) {
                        _data.writeInt(1);
                        packageName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(125, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().setVrMode(token, enabled, packageName);
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

            public void startLocalVoiceInteraction(IBinder token, Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(126, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startLocalVoiceInteraction(token, options);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void stopLocalVoiceInteraction(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(127, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().stopLocalVoiceInteraction(token);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean supportsLocalVoiceInteraction() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(128, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().supportsLocalVoiceInteraction();
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

            public void notifyPinnedStackAnimationStarted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(129, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyPinnedStackAnimationStarted();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyPinnedStackAnimationEnded() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(130, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyPinnedStackAnimationEnded();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ConfigurationInfo configurationInfo = 131;
                    if (!this.mRemote.transact(131, _data, _reply, 0)) {
                        configurationInfo = Stub.getDefaultImpl();
                        if (configurationInfo != 0) {
                            configurationInfo = Stub.getDefaultImpl().getDeviceConfigurationInfo();
                            return configurationInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        configurationInfo = (ConfigurationInfo) ConfigurationInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        configurationInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return configurationInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void resizePinnedStack(Rect pinnedBounds, Rect tempPinnedTaskBounds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (pinnedBounds != null) {
                        _data.writeInt(1);
                        pinnedBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (tempPinnedTaskBounds != null) {
                        _data.writeInt(1);
                        tempPinnedTaskBounds.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(132, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resizePinnedStack(pinnedBounds, tempPinnedTaskBounds);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean updateDisplayOverrideConfiguration(Configuration values, int displayId) throws RemoteException {
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
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(133, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().updateDisplayOverrideConfiguration(values, displayId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dismissKeyguard(IBinder token, IKeyguardDismissCallback callback, CharSequence message) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (message != null) {
                        _data.writeInt(1);
                        TextUtils.writeToParcel(message, _data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(134, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dismissKeyguard(token, callback, message);
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
                    if (this.mRemote.transact(135, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (!taskSnapshot.transact(136, _data, _reply, 0)) {
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

            public void setDisablePreviewScreenshots(IBinder token, boolean disable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(disable ? 1 : 0);
                    if (this.mRemote.transact(137, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDisablePreviewScreenshots(token, disable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getLastResumedActivityUserId() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 138;
                    if (!this.mRemote.transact(138, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getLastResumedActivityUserId();
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
                    if (this.mRemote.transact(139, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void updateLockTaskFeatures(int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(140, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().updateLockTaskFeatures(userId, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setShowWhenLocked(IBinder token, boolean showWhenLocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(showWhenLocked ? 1 : 0);
                    if (this.mRemote.transact(141, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setShowWhenLocked(token, showWhenLocked);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setInheritShowWhenLocked(IBinder token, boolean setInheritShownWhenLocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(setInheritShownWhenLocked ? 1 : 0);
                    if (this.mRemote.transact(142, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setInheritShowWhenLocked(token, setInheritShownWhenLocked);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setTurnScreenOn(IBinder token, boolean turnScreenOn) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(turnScreenOn ? 1 : 0);
                    if (this.mRemote.transact(143, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setTurnScreenOn(token, turnScreenOn);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerRemoteAnimations(IBinder token, RemoteAnimationDefinition definition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (definition != null) {
                        _data.writeInt(1);
                        definition.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(144, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerRemoteAnimations(token, definition);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerRemoteAnimationForNextActivityStart(String packageName, RemoteAnimationAdapter adapter) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (adapter != null) {
                        _data.writeInt(1);
                        adapter.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(145, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerRemoteAnimationForNextActivityStart(packageName, adapter);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerRemoteAnimationsForDisplay(int displayId, RemoteAnimationDefinition definition) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (definition != null) {
                        _data.writeInt(1);
                        definition.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(146, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerRemoteAnimationsForDisplay(displayId, definition);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void alwaysShowUnsupportedCompileSdkWarning(ComponentName activity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (activity != null) {
                        _data.writeInt(1);
                        activity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(147, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().alwaysShowUnsupportedCompileSdkWarning(activity);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVrThread(int tid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tid);
                    if (this.mRemote.transact(148, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVrThread(tid);
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
                    if (this.mRemote.transact(149, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void stopAppSwitches() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(150, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (this.mRemote.transact(151, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void setActivityController(IActivityController watcher, boolean imAMonkey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
                    _data.writeInt(imAMonkey ? 1 : 0);
                    if (this.mRemote.transact(152, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void setVoiceKeepAwake(IVoiceInteractionSession session, boolean keepAwake) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(session != null ? session.asBinder() : null);
                    _data.writeInt(keepAwake ? 1 : 0);
                    if (this.mRemote.transact(153, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVoiceKeepAwake(session, keepAwake);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPackageScreenCompatMode(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    int i = 154;
                    if (!this.mRemote.transact(154, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPackageScreenCompatMode(packageName);
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

            public void setPackageScreenCompatMode(String packageName, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(mode);
                    if (this.mRemote.transact(155, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public boolean getPackageAskScreenCompat(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(156, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getPackageAskScreenCompat(packageName);
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

            public void setPackageAskScreenCompat(String packageName, boolean ask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(ask ? 1 : 0);
                    if (this.mRemote.transact(157, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPackageAskScreenCompat(packageName, ask);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearLaunchParamsForPackages(List<String> packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packageNames);
                    if (this.mRemote.transact(158, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearLaunchParamsForPackages(packageNames);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDisplayToSingleTaskInstance(int displayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(displayId);
                    if (this.mRemote.transact(159, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDisplayToSingleTaskInstance(displayId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restartActivityProcessIfVisible(IBinder activityToken) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    if (this.mRemote.transact(160, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restartActivityProcessIfVisible(activityToken);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onBackPressedOnTaskRoot(IBinder activityToken, IRequestFinishCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activityToken);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(161, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onBackPressedOnTaskRoot(activityToken, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int handleFreeformModeRequst(IBinder token, int cmd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    _data.writeInt(cmd);
                    int i = 162;
                    if (!this.mRemote.transact(162, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().handleFreeformModeRequst(token, cmd);
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

            public Intent getTopVisibleActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Intent intent = 163;
                    if (!this.mRemote.transact(163, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().getTopVisibleActivity();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IActivityTaskManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IActivityTaskManager)) {
                return new Proxy(obj);
            }
            return (IActivityTaskManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "startActivity";
                case 2:
                    return "startActivities";
                case 3:
                    return "startActivityAsUser";
                case 4:
                    return "startNextMatchingActivity";
                case 5:
                    return "startActivityIntentSender";
                case 6:
                    return "startActivityAndWait";
                case 7:
                    return "startActivityWithConfig";
                case 8:
                    return "startVoiceActivity";
                case 9:
                    return "startAssistantActivity";
                case 10:
                    return "startRecentsActivity";
                case 11:
                    return "startActivityFromRecents";
                case 12:
                    return "startActivityAsCaller";
                case 13:
                    return "isActivityStartAllowedOnDisplay";
                case 14:
                    return "unhandledBack";
                case 15:
                    return "finishActivity";
                case 16:
                    return "finishActivityAffinity";
                case 17:
                    return "activityIdle";
                case 18:
                    return "activityResumed";
                case 19:
                    return "activityTopResumedStateLost";
                case 20:
                    return "activityPaused";
                case 21:
                    return "activityStopped";
                case 22:
                    return "activityDestroyed";
                case 23:
                    return "activityRelaunched";
                case 24:
                    return "activitySlept";
                case 25:
                    return "getFrontActivityScreenCompatMode";
                case 26:
                    return "setFrontActivityScreenCompatMode";
                case 27:
                    return "getCallingPackage";
                case 28:
                    return "getCallingActivity";
                case 29:
                    return "setFocusedTask";
                case 30:
                    return "removeTask";
                case 31:
                    return "removeAllVisibleRecentTasks";
                case 32:
                    return "getTasks";
                case 33:
                    return "getFilteredTasks";
                case 34:
                    return "shouldUpRecreateTask";
                case 35:
                    return "navigateUpTo";
                case 36:
                    return "moveTaskToFront";
                case 37:
                    return "getTaskForActivity";
                case 38:
                    return "finishSubActivity";
                case 39:
                    return "getRecentTasks";
                case 40:
                    return "willActivityBeVisible";
                case 41:
                    return "setRequestedOrientation";
                case 42:
                    return "getRequestedOrientation";
                case 43:
                    return "convertFromTranslucent";
                case 44:
                    return "convertToTranslucent";
                case 45:
                    return "notifyActivityDrawn";
                case 46:
                    return "reportActivityFullyDrawn";
                case 47:
                    return "getActivityDisplayId";
                case 48:
                    return "isImmersive";
                case 49:
                    return "setImmersive";
                case 50:
                    return "isTopActivityImmersive";
                case 51:
                    return "moveActivityTaskToBack";
                case 52:
                    return "getTaskDescription";
                case 53:
                    return "overridePendingTransition";
                case 54:
                    return "getLaunchedFromUid";
                case 55:
                    return "getLaunchedFromPackage";
                case 56:
                    return "reportAssistContextExtras";
                case 57:
                    return "setFocusedStack";
                case 58:
                    return "getFocusedStackInfo";
                case 59:
                    return "getTaskBounds";
                case 60:
                    return "cancelRecentsAnimation";
                case 61:
                    return "startLockTaskModeByToken";
                case 62:
                    return "stopLockTaskModeByToken";
                case 63:
                    return "updateLockTaskPackages";
                case 64:
                    return "isInLockTaskMode";
                case 65:
                    return "getLockTaskModeState";
                case 66:
                    return "setTaskDescription";
                case 67:
                    return "getActivityOptions";
                case 68:
                    return "getAppTasks";
                case 69:
                    return "startSystemLockTaskMode";
                case 70:
                    return "stopSystemLockTaskMode";
                case 71:
                    return "finishVoiceTask";
                case 72:
                    return "isTopOfTask";
                case 73:
                    return "notifyLaunchTaskBehindComplete";
                case 74:
                    return "notifyEnterAnimationComplete";
                case 75:
                    return "addAppTask";
                case 76:
                    return "getAppTaskThumbnailSize";
                case 77:
                    return "releaseActivityInstance";
                case 78:
                    return "requestStartActivityPermissionToken";
                case 79:
                    return "releaseSomeActivities";
                case 80:
                    return "getTaskDescriptionIcon";
                case 81:
                    return "startInPlaceAnimationOnFrontMostApplication";
                case 82:
                    return "registerTaskStackListener";
                case 83:
                    return "unregisterTaskStackListener";
                case 84:
                    return "setTaskResizeable";
                case 85:
                    return "toggleFreeformWindowingMode";
                case 86:
                    return "resizeTask";
                case 87:
                    return "moveStackToDisplay";
                case 88:
                    return "removeStack";
                case 89:
                    return "setTaskWindowingMode";
                case 90:
                    return "moveTaskToStack";
                case 91:
                    return "resizeStack";
                case 92:
                    return "setTaskWindowingModeSplitScreenPrimary";
                case 93:
                    return "offsetPinnedStackBounds";
                case 94:
                    return "removeStacksInWindowingModes";
                case 95:
                    return "removeStacksWithActivityTypes";
                case 96:
                    return "getAllStackInfos";
                case 97:
                    return "getStackInfo";
                case 98:
                    return "setLockScreenShown";
                case 99:
                    return "getAssistContextExtras";
                case 100:
                    return "launchAssistIntent";
                case 101:
                    return "requestAssistContextExtras";
                case 102:
                    return "requestAutofillData";
                case 103:
                    return "isAssistDataAllowedOnCurrentActivity";
                case 104:
                    return "showAssistFromActivity";
                case 105:
                    return "isRootVoiceInteraction";
                case 106:
                    return "showLockTaskEscapeMessage";
                case 107:
                    return "keyguardGoingAway";
                case 108:
                    return "getActivityClassForToken";
                case 109:
                    return "getPackageForToken";
                case 110:
                    return "positionTaskInStack";
                case 111:
                    return "reportSizeConfigurations";
                case 112:
                    return "dismissSplitScreenMode";
                case 113:
                    return "dismissPip";
                case 114:
                    return "suppressResizeConfigChanges";
                case 115:
                    return "moveTasksToFullscreenStack";
                case 116:
                    return "moveTopActivityToPinnedStack";
                case 117:
                    return "isInMultiWindowMode";
                case 118:
                    return "isInPictureInPictureMode";
                case 119:
                    return "enterPictureInPictureMode";
                case 120:
                    return "setPictureInPictureParams";
                case 121:
                    return "getMaxNumPictureInPictureActions";
                case 122:
                    return "getUriPermissionOwnerForActivity";
                case 123:
                    return "resizeDockedStack";
                case 124:
                    return "setSplitScreenResizing";
                case 125:
                    return "setVrMode";
                case 126:
                    return "startLocalVoiceInteraction";
                case 127:
                    return "stopLocalVoiceInteraction";
                case 128:
                    return "supportsLocalVoiceInteraction";
                case 129:
                    return "notifyPinnedStackAnimationStarted";
                case 130:
                    return "notifyPinnedStackAnimationEnded";
                case 131:
                    return "getDeviceConfigurationInfo";
                case 132:
                    return "resizePinnedStack";
                case 133:
                    return "updateDisplayOverrideConfiguration";
                case 134:
                    return "dismissKeyguard";
                case 135:
                    return "cancelTaskWindowTransition";
                case 136:
                    return "getTaskSnapshot";
                case 137:
                    return "setDisablePreviewScreenshots";
                case 138:
                    return "getLastResumedActivityUserId";
                case 139:
                    return "updateConfiguration";
                case 140:
                    return "updateLockTaskFeatures";
                case 141:
                    return "setShowWhenLocked";
                case 142:
                    return "setInheritShowWhenLocked";
                case 143:
                    return "setTurnScreenOn";
                case 144:
                    return "registerRemoteAnimations";
                case 145:
                    return "registerRemoteAnimationForNextActivityStart";
                case 146:
                    return "registerRemoteAnimationsForDisplay";
                case 147:
                    return "alwaysShowUnsupportedCompileSdkWarning";
                case 148:
                    return "setVrThread";
                case 149:
                    return "setPersistentVrThread";
                case 150:
                    return "stopAppSwitches";
                case 151:
                    return "resumeAppSwitches";
                case 152:
                    return "setActivityController";
                case 153:
                    return "setVoiceKeepAwake";
                case 154:
                    return "getPackageScreenCompatMode";
                case 155:
                    return "setPackageScreenCompatMode";
                case 156:
                    return "getPackageAskScreenCompat";
                case 157:
                    return "setPackageAskScreenCompat";
                case 158:
                    return "clearLaunchParamsForPackages";
                case 159:
                    return "setDisplayToSingleTaskInstance";
                case 160:
                    return "restartActivityProcessIfVisible";
                case 161:
                    return "onBackPressedOnTaskRoot";
                case 162:
                    return "handleFreeformModeRequst";
                case 163:
                    return "getTopVisibleActivity";
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
            Parcel parcel3;
            boolean z;
            if (i != 1598968902) {
                boolean _arg11 = false;
                Parcel parcel4;
                String _arg1;
                String _arg5;
                int _arg7;
                int _result;
                Bundle _arg52;
                IApplicationThread _arg0;
                String _arg12;
                Intent _arg2;
                String _arg3;
                IBinder _arg4;
                String _arg53;
                int _arg72;
                ProfilerInfo _arg8;
                Bundle _arg9;
                IBinder _arg02;
                Intent _arg13;
                boolean _result2;
                Intent _arg32;
                String _arg03;
                Intent _arg04;
                Parcel parcel5;
                Bundle _arg14;
                int _result3;
                boolean _result4;
                int _arg15;
                String _result5;
                ComponentName _result6;
                boolean _result7;
                int _arg22;
                int _arg33;
                Bundle _arg42;
                boolean _result8;
                TaskDescription _result9;
                Rect _result10;
                IBinder _result11;
                int _arg05;
                Rect _arg16;
                Rect _arg43;
                Rect _arg17;
                PictureInPictureParams _arg18;
                Configuration _arg06;
                RemoteAnimationDefinition _arg19;
                String _arg07;
                switch (i) {
                    case 1:
                        Intent _arg23;
                        ProfilerInfo _arg82;
                        Bundle _arg92;
                        parcel4 = parcel2;
                        parcel3 = parcel;
                        parcel3.enforceInterface(descriptor);
                        IApplicationThread _arg08 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (Intent) Intent.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg23 = null;
                        }
                        String _arg34 = data.readString();
                        IBinder _arg44 = data.readStrongBinder();
                        _arg5 = data.readString();
                        int _arg6 = data.readInt();
                        _arg7 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg82 = (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg82 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg92 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg92 = null;
                        }
                        _result = startActivity(_arg08, _arg1, _arg23, _arg34, _arg44, _arg5, _arg6, _arg7, _arg82, _arg92);
                        reply.writeNoException();
                        parcel4.writeInt(_result);
                        return true;
                    case 2:
                        parcel4 = parcel2;
                        parcel3 = parcel;
                        parcel3.enforceInterface(descriptor);
                        IApplicationThread _arg09 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        String _arg110 = data.readString();
                        Intent[] descriptor2 = (Intent[]) parcel3.createTypedArray(Intent.CREATOR);
                        String[] _arg35 = data.createStringArray();
                        IBinder _arg45 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg52 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg52 = null;
                        }
                        _result = startActivities(_arg09, _arg110, descriptor2, _arg35, _arg45, _arg52, data.readInt());
                        reply.writeNoException();
                        parcel4.writeInt(_result);
                        return true;
                    case 3:
                        parcel4 = parcel2;
                        parcel3 = parcel;
                        parcel3.enforceInterface(descriptor);
                        _arg0 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Intent) Intent.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readString();
                        _arg4 = data.readStrongBinder();
                        _arg53 = data.readString();
                        _arg7 = data.readInt();
                        _arg72 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg8 = (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg8 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg9 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg9 = null;
                        }
                        _result = startActivityAsUser(_arg0, _arg12, _arg2, _arg3, _arg4, _arg53, _arg7, _arg72, _arg8, _arg9, data.readInt());
                        reply.writeNoException();
                        parcel4.writeInt(_result);
                        return true;
                    case 4:
                        Bundle _arg24;
                        parcel4 = parcel2;
                        parcel3 = parcel;
                        parcel3.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg13 = (Intent) Intent.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg24 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg24 = null;
                        }
                        _result2 = startNextMatchingActivity(_arg02, _arg13, _arg24);
                        reply.writeNoException();
                        parcel4.writeInt(_result2);
                        return true;
                    case 5:
                        Bundle _arg10;
                        parcel4 = parcel2;
                        parcel3 = data;
                        parcel3.enforceInterface(descriptor);
                        _arg0 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        IIntentSender _arg111 = android.content.IIntentSender.Stub.asInterface(data.readStrongBinder());
                        IBinder _arg25 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg32 = (Intent) Intent.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg32 = null;
                        }
                        _arg5 = data.readString();
                        IBinder _arg54 = data.readStrongBinder();
                        String _arg62 = data.readString();
                        _arg72 = data.readInt();
                        int _arg83 = data.readInt();
                        int _arg93 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg10 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg10 = null;
                        }
                        _result = startActivityIntentSender(_arg0, _arg111, _arg25, _arg32, _arg5, _arg54, _arg62, _arg72, _arg83, _arg93, _arg10);
                        reply.writeNoException();
                        parcel4.writeInt(_result);
                        return true;
                    case 6:
                        parcel4 = parcel2;
                        parcel2 = parcel;
                        parcel2.enforceInterface(descriptor);
                        _arg0 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Intent) Intent.CREATOR.createFromParcel(parcel2);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readString();
                        _arg4 = data.readStrongBinder();
                        _arg53 = data.readString();
                        _arg7 = data.readInt();
                        _arg72 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg8 = (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(parcel2);
                        } else {
                            _arg8 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg9 = (Bundle) Bundle.CREATOR.createFromParcel(parcel2);
                        } else {
                            _arg9 = null;
                        }
                        WaitResult _result12 = startActivityAndWait(_arg0, _arg12, _arg2, _arg3, _arg4, _arg53, _arg7, _arg72, _arg8, _arg9, data.readInt());
                        reply.writeNoException();
                        if (_result12 != null) {
                            z = true;
                            parcel4.writeInt(1);
                            _result12.writeToParcel(parcel4, 1);
                        } else {
                            z = true;
                            parcel4.writeInt(0);
                        }
                        return z;
                    case 7:
                        Configuration _arg84;
                        parcel4 = parcel2;
                        parcel3 = parcel;
                        parcel3.enforceInterface(descriptor);
                        _arg0 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Intent) Intent.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readString();
                        _arg4 = data.readStrongBinder();
                        _arg53 = data.readString();
                        _arg7 = data.readInt();
                        _arg72 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg84 = (Configuration) Configuration.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg84 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg9 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg9 = null;
                        }
                        _result = startActivityWithConfig(_arg0, _arg12, _arg2, _arg3, _arg4, _arg53, _arg7, _arg72, _arg84, _arg9, data.readInt());
                        reply.writeNoException();
                        parcel4.writeInt(_result);
                        return true;
                    case 8:
                        parcel4 = parcel2;
                        parcel3 = parcel;
                        parcel3.enforceInterface(descriptor);
                        _arg1 = data.readString();
                        int _arg112 = data.readInt();
                        int _arg26 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg32 = (Intent) Intent.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg32 = null;
                        }
                        _arg5 = data.readString();
                        IVoiceInteractionSession _arg55 = android.service.voice.IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                        IVoiceInteractor _arg63 = com.android.internal.app.IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
                        _arg72 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg8 = (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg8 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg9 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg9 = null;
                        }
                        _result = startVoiceActivity(_arg1, _arg112, _arg26, _arg32, _arg5, _arg55, _arg63, _arg72, _arg8, _arg9, data.readInt());
                        reply.writeNoException();
                        parcel4.writeInt(_result);
                        return true;
                    case 9:
                        Intent _arg36;
                        parcel4 = parcel2;
                        parcel3 = parcel;
                        parcel3.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        int _arg113 = data.readInt();
                        descriptor = data.readInt();
                        if (data.readInt() != 0) {
                            _arg36 = (Intent) Intent.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg36 = null;
                        }
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg52 = (Bundle) Bundle.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg52 = null;
                        }
                        _result = startAssistantActivity(_arg03, _arg113, descriptor, _arg36, _arg1, _arg52, data.readInt());
                        reply.writeNoException();
                        parcel4.writeInt(_result);
                        return true;
                    case 10:
                        parcel4 = parcel2;
                        parcel3 = parcel;
                        parcel3.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Intent) Intent.CREATOR.createFromParcel(parcel3);
                        } else {
                            _arg04 = null;
                        }
                        startRecentsActivity(_arg04, android.app.IAssistDataReceiver.Stub.asInterface(data.readStrongBinder()), android.view.IRecentsAnimationRunner.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel4 = parcel2;
                        parcel5 = parcel;
                        parcel5.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg14 = (Bundle) Bundle.CREATOR.createFromParcel(parcel5);
                        } else {
                            _arg14 = null;
                        }
                        parcel3 = parcel5;
                        _result3 = startActivityFromRecents(_result, _arg14);
                        reply.writeNoException();
                        parcel4.writeInt(_result3);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _arg0 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _arg3 = data.readString();
                        _arg4 = data.readStrongBinder();
                        _arg53 = data.readString();
                        _arg7 = data.readInt();
                        _arg72 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg8 = (ProfilerInfo) ProfilerInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg8 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg9 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg9 = null;
                        }
                        IBinder _arg102 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        i = 1;
                        parcel4 = parcel2;
                        parcel5 = parcel;
                        _result = startActivityAsCaller(_arg0, _arg12, _arg2, _arg3, _arg4, _arg53, _arg7, _arg72, _arg8, _arg9, _arg102, _arg11, data.readInt());
                        reply.writeNoException();
                        parcel4.writeInt(_result);
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg13 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        _result4 = isActivityStartAllowedOnDisplay(_result, _arg13, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        unhandledBack();
                        reply.writeNoException();
                        return true;
                    case 15:
                        Intent _arg27;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        _arg15 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg27 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg27 = null;
                        }
                        _result4 = finishActivity(_arg02, _arg15, _arg27, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        z = finishActivityAffinity(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 17:
                        Configuration _arg114;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg114 = (Configuration) Configuration.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg114 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        activityIdle(_arg02, _arg114, _arg11);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        activityResumed(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        activityTopResumedStateLost();
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        activityPaused(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 21:
                        PersistableBundle _arg28;
                        CharSequence _arg37;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg14 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg28 = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg28 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg37 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg37 = null;
                        }
                        activityStopped(_arg02, _arg14, _arg28, _arg37);
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        activityDestroyed(data.readStrongBinder());
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        activityRelaunched(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        activitySlept(data.readStrongBinder());
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _result = getFrontActivityScreenCompatMode();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        setFrontActivityScreenCompatMode(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        _result5 = getCallingPackage(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _result6 = getCallingActivity(data.readStrongBinder());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        setFocusedTask(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        z = removeTask(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        removeAllVisibleRecentTasks();
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        List<RunningTaskInfo> _result13 = getTasks(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result13);
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        List<RunningTaskInfo> _result14 = getFilteredTasks(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result14);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        _result7 = shouldUpRecreateTask(data.readStrongBinder(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 35:
                        Intent _arg38;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg13 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        _result3 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg38 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg38 = null;
                        }
                        _result4 = navigateUpTo(_arg02, _arg13, _result3, _arg38);
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        IApplicationThread _arg010 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        String _arg115 = data.readString();
                        _arg22 = data.readInt();
                        _arg33 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg42 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        moveTaskToFront(_arg010, _arg115, _arg22, _arg33, _arg42);
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        _result3 = getTaskForActivity(_arg02, _arg11);
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        finishSubActivity(data.readStrongBinder(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result15 = getRecentTasks(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result15 != null) {
                            parcel2.writeInt(1);
                            _result15.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        z = willActivityBeVisible(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        setRequestedOrientation(data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        _arg15 = getRequestedOrientation(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_arg15);
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        z = convertFromTranslucent(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg14 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        _result7 = convertToTranslucent(_arg02, _arg14);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        notifyActivityDrawn(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        reportActivityFullyDrawn(_arg02, _arg11);
                        reply.writeNoException();
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        _arg15 = getActivityDisplayId(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_arg15);
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        z = isImmersive(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setImmersive(_arg02, _arg11);
                        reply.writeNoException();
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        _result8 = isTopActivityImmersive();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        _result7 = moveActivityTaskToBack(_arg02, _arg11);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        _result9 = getTaskDescription(data.readInt());
                        reply.writeNoException();
                        if (_result9 != null) {
                            parcel2.writeInt(1);
                            _result9.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        overridePendingTransition(data.readStrongBinder(), data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        _arg15 = getLaunchedFromUid(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_arg15);
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        _result5 = getLaunchedFromPackage(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 56:
                        Bundle _arg116;
                        AssistStructure _arg29;
                        AssistContent _arg39;
                        Uri _arg46;
                        parcel.enforceInterface(descriptor);
                        IBinder _arg011 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg116 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg116 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg29 = (AssistStructure) AssistStructure.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg29 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg39 = (AssistContent) AssistContent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg39 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg46 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg46 = null;
                        }
                        reportAssistContextExtras(_arg011, _arg116, _arg29, _arg39, _arg46);
                        reply.writeNoException();
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        setFocusedStack(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        StackInfo _result16 = getFocusedStackInfo();
                        reply.writeNoException();
                        if (_result16 != null) {
                            parcel2.writeInt(1);
                            _result16.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        _result10 = getTaskBounds(data.readInt());
                        reply.writeNoException();
                        if (_result10 != null) {
                            parcel2.writeInt(1);
                            _result10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        cancelRecentsAnimation(_arg11);
                        reply.writeNoException();
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        startLockTaskModeByToken(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        stopLockTaskModeByToken(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        updateLockTaskPackages(data.readInt(), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        _result8 = isInLockTaskMode();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        _result = getLockTaskModeState();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _result9 = (TaskDescription) TaskDescription.CREATOR.createFromParcel(parcel);
                        } else {
                            _result9 = null;
                        }
                        setTaskDescription(_arg02, _result9);
                        reply.writeNoException();
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        _arg14 = getActivityOptions(data.readStrongBinder());
                        reply.writeNoException();
                        if (_arg14 != null) {
                            parcel2.writeInt(1);
                            _arg14.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        List<IBinder> _result17 = getAppTasks(data.readString());
                        reply.writeNoException();
                        parcel2.writeBinderList(_result17);
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        startSystemLockTaskMode(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        stopSystemLockTaskMode();
                        reply.writeNoException();
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        finishVoiceTask(android.service.voice.IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 72:
                        parcel.enforceInterface(descriptor);
                        z = isTopOfTask(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 73:
                        parcel.enforceInterface(descriptor);
                        notifyLaunchTaskBehindComplete(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 74:
                        parcel.enforceInterface(descriptor);
                        notifyEnterAnimationComplete(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 75:
                        TaskDescription _arg210;
                        Bitmap _arg310;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg13 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg13 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg210 = (TaskDescription) TaskDescription.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg210 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg310 = (Bitmap) Bitmap.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg310 = null;
                        }
                        int _result18 = addAppTask(_arg02, _arg13, _arg210, _arg310);
                        reply.writeNoException();
                        parcel2.writeInt(_result18);
                        return true;
                    case 76:
                        parcel.enforceInterface(descriptor);
                        Point _result19 = getAppTaskThumbnailSize();
                        reply.writeNoException();
                        if (_result19 != null) {
                            parcel2.writeInt(1);
                            _result19.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 77:
                        parcel.enforceInterface(descriptor);
                        z = releaseActivityInstance(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 78:
                        parcel.enforceInterface(descriptor);
                        _result11 = requestStartActivityPermissionToken(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result11);
                        return true;
                    case 79:
                        parcel.enforceInterface(descriptor);
                        releaseSomeActivities(android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 80:
                        parcel.enforceInterface(descriptor);
                        Bitmap _result20 = getTaskDescriptionIcon(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result20 != null) {
                            parcel2.writeInt(1);
                            _result20.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 81:
                        Bundle _arg012;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg012 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg012 = null;
                        }
                        startInPlaceAnimationOnFrontMostApplication(_arg012);
                        reply.writeNoException();
                        return true;
                    case 82:
                        parcel.enforceInterface(descriptor);
                        registerTaskStackListener(android.app.ITaskStackListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 83:
                        parcel.enforceInterface(descriptor);
                        unregisterTaskStackListener(android.app.ITaskStackListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 84:
                        parcel.enforceInterface(descriptor);
                        setTaskResizeable(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 85:
                        parcel.enforceInterface(descriptor);
                        toggleFreeformWindowingMode(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 86:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _result10 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _result10 = null;
                        }
                        resizeTask(_result, _result10, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 87:
                        parcel.enforceInterface(descriptor);
                        moveStackToDisplay(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 88:
                        parcel.enforceInterface(descriptor);
                        removeStack(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 89:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        _arg15 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setTaskWindowingMode(_result, _arg15, _arg11);
                        reply.writeNoException();
                        return true;
                    case 90:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        _arg15 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        moveTaskToStack(_result, _arg15, _arg11);
                        reply.writeNoException();
                        return true;
                    case 91:
                        parcel.enforceInterface(descriptor);
                        _arg05 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg16 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        resizeStack(_arg05, _arg16, data.readInt() != 0, data.readInt() != 0, data.readInt() != 0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 92:
                        parcel.enforceInterface(descriptor);
                        _arg05 = data.readInt();
                        _arg22 = data.readInt();
                        _result2 = data.readInt() != 0;
                        _result4 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg43 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg43 = null;
                        }
                        _result8 = setTaskWindowingModeSplitScreenPrimary(_arg05, _arg22, _result2, _result4, _arg43, data.readInt() != 0);
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 93:
                        parcel.enforceInterface(descriptor);
                        int _arg013 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg17 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg17 = null;
                        }
                        offsetPinnedStackBounds(_arg013, _arg17, data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 94:
                        parcel.enforceInterface(descriptor);
                        removeStacksInWindowingModes(data.createIntArray());
                        reply.writeNoException();
                        return true;
                    case 95:
                        parcel.enforceInterface(descriptor);
                        removeStacksWithActivityTypes(data.createIntArray());
                        reply.writeNoException();
                        return true;
                    case 96:
                        parcel.enforceInterface(descriptor);
                        List<StackInfo> _result21 = getAllStackInfos();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result21);
                        return true;
                    case 97:
                        parcel.enforceInterface(descriptor);
                        StackInfo _result22 = getStackInfo(data.readInt(), data.readInt());
                        reply.writeNoException();
                        if (_result22 != null) {
                            parcel2.writeInt(1);
                            _result22.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 98:
                        parcel.enforceInterface(descriptor);
                        _result8 = data.readInt() != 0;
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setLockScreenShown(_result8, _arg11);
                        reply.writeNoException();
                        return true;
                    case 99:
                        parcel.enforceInterface(descriptor);
                        _arg14 = getAssistContextExtras(data.readInt());
                        reply.writeNoException();
                        if (_arg14 != null) {
                            parcel2.writeInt(1);
                            _arg14.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 100:
                        Intent _arg014;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg014 = (Intent) Intent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg014 = null;
                        }
                        _arg05 = data.readInt();
                        _arg03 = data.readString();
                        _arg33 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg42 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        _result8 = launchAssistIntent(_arg014, _arg05, _arg03, _arg33, _arg42);
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 101:
                        parcel.enforceInterface(descriptor);
                        _arg05 = data.readInt();
                        IAssistDataReceiver _arg117 = android.app.IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg42 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        _result8 = requestAssistContextExtras(_arg05, _arg117, _arg42, data.readStrongBinder(), data.readInt() != 0, data.readInt() != 0);
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 102:
                        parcel.enforceInterface(descriptor);
                        IAssistDataReceiver _arg015 = android.app.IAssistDataReceiver.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg14 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        _result4 = requestAutofillData(_arg015, _arg14, data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 103:
                        parcel.enforceInterface(descriptor);
                        _result8 = isAssistDataAllowedOnCurrentActivity();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 104:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg14 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        _result7 = showAssistFromActivity(_arg02, _arg14);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 105:
                        parcel.enforceInterface(descriptor);
                        z = isRootVoiceInteraction(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 106:
                        parcel.enforceInterface(descriptor);
                        showLockTaskEscapeMessage(data.readStrongBinder());
                        return true;
                    case 107:
                        parcel.enforceInterface(descriptor);
                        keyguardGoingAway(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 108:
                        parcel.enforceInterface(descriptor);
                        _result6 = getActivityClassForToken(data.readStrongBinder());
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 109:
                        parcel.enforceInterface(descriptor);
                        _result5 = getPackageForToken(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeString(_result5);
                        return true;
                    case 110:
                        parcel.enforceInterface(descriptor);
                        positionTaskInStack(data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 111:
                        parcel.enforceInterface(descriptor);
                        reportSizeConfigurations(data.readStrongBinder(), data.createIntArray(), data.createIntArray(), data.createIntArray());
                        reply.writeNoException();
                        return true;
                    case 112:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        dismissSplitScreenMode(_arg11);
                        reply.writeNoException();
                        return true;
                    case 113:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        dismissPip(_arg11, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 114:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        suppressResizeConfigChanges(_arg11);
                        reply.writeNoException();
                        return true;
                    case 115:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        moveTasksToFullscreenStack(_result, _arg11);
                        reply.writeNoException();
                        return true;
                    case 116:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _result10 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _result10 = null;
                        }
                        _result7 = moveTopActivityToPinnedStack(_result, _result10);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 117:
                        parcel.enforceInterface(descriptor);
                        z = isInMultiWindowMode(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 118:
                        parcel.enforceInterface(descriptor);
                        z = isInPictureInPictureMode(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 119:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg18 = (PictureInPictureParams) PictureInPictureParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        _result7 = enterPictureInPictureMode(_arg02, _arg18);
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 120:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg18 = (PictureInPictureParams) PictureInPictureParams.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg18 = null;
                        }
                        setPictureInPictureParams(_arg02, _arg18);
                        reply.writeNoException();
                        return true;
                    case 121:
                        parcel.enforceInterface(descriptor);
                        _arg15 = getMaxNumPictureInPictureActions(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeInt(_arg15);
                        return true;
                    case 122:
                        parcel.enforceInterface(descriptor);
                        _result11 = getUriPermissionOwnerForActivity(data.readStrongBinder());
                        reply.writeNoException();
                        parcel2.writeStrongBinder(_result11);
                        return true;
                    case 123:
                        Rect _arg016;
                        Rect _arg311;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg016 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg016 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg17 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg17 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg16 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg16 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg311 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg311 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg43 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg43 = null;
                        }
                        resizeDockedStack(_arg016, _arg17, _arg16, _arg311, _arg43);
                        reply.writeNoException();
                        return true;
                    case 124:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setSplitScreenResizing(_arg11);
                        reply.writeNoException();
                        return true;
                    case 125:
                        ComponentName _arg211;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        z = _arg11;
                        if (data.readInt() != 0) {
                            _arg211 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg211 = null;
                        }
                        int _result23 = setVrMode(_arg02, z, _arg211);
                        reply.writeNoException();
                        parcel2.writeInt(_result23);
                        return true;
                    case 126:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg14 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        startLocalVoiceInteraction(_arg02, _arg14);
                        reply.writeNoException();
                        return true;
                    case 127:
                        parcel.enforceInterface(descriptor);
                        stopLocalVoiceInteraction(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 128:
                        parcel.enforceInterface(descriptor);
                        _result8 = supportsLocalVoiceInteraction();
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 129:
                        parcel.enforceInterface(descriptor);
                        notifyPinnedStackAnimationStarted();
                        reply.writeNoException();
                        return true;
                    case 130:
                        parcel.enforceInterface(descriptor);
                        notifyPinnedStackAnimationEnded();
                        reply.writeNoException();
                        return true;
                    case 131:
                        parcel.enforceInterface(descriptor);
                        ConfigurationInfo _result24 = getDeviceConfigurationInfo();
                        reply.writeNoException();
                        if (_result24 != null) {
                            parcel2.writeInt(1);
                            _result24.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 132:
                        Rect _arg017;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg017 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg017 = null;
                        }
                        if (data.readInt() != 0) {
                            _result10 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _result10 = null;
                        }
                        resizePinnedStack(_arg017, _result10);
                        reply.writeNoException();
                        return true;
                    case 133:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (Configuration) Configuration.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        _result7 = updateDisplayOverrideConfiguration(_arg06, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result7);
                        return true;
                    case 134:
                        CharSequence _arg212;
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        IKeyguardDismissCallback _arg118 = com.android.internal.policy.IKeyguardDismissCallback.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg212 = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
                        } else {
                            _arg212 = null;
                        }
                        dismissKeyguard(_arg02, _arg118, _arg212);
                        reply.writeNoException();
                        return true;
                    case 135:
                        parcel.enforceInterface(descriptor);
                        cancelTaskWindowTransition(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 136:
                        parcel.enforceInterface(descriptor);
                        TaskSnapshot _result25 = getTaskSnapshot(data.readInt(), data.readInt() != 0);
                        reply.writeNoException();
                        if (_result25 != null) {
                            parcel2.writeInt(1);
                            _result25.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 137:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setDisablePreviewScreenshots(_arg02, _arg11);
                        reply.writeNoException();
                        return true;
                    case 138:
                        parcel.enforceInterface(descriptor);
                        _result = getLastResumedActivityUserId();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 139:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (Configuration) Configuration.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        z = updateConfiguration(_arg06);
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 140:
                        parcel.enforceInterface(descriptor);
                        updateLockTaskFeatures(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 141:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setShowWhenLocked(_arg02, _arg11);
                        reply.writeNoException();
                        return true;
                    case 142:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setInheritShowWhenLocked(_arg02, _arg11);
                        reply.writeNoException();
                        return true;
                    case 143:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setTurnScreenOn(_arg02, _arg11);
                        reply.writeNoException();
                        return true;
                    case 144:
                        parcel.enforceInterface(descriptor);
                        _arg02 = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg19 = (RemoteAnimationDefinition) RemoteAnimationDefinition.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg19 = null;
                        }
                        registerRemoteAnimations(_arg02, _arg19);
                        reply.writeNoException();
                        return true;
                    case 145:
                        RemoteAnimationAdapter _arg119;
                        parcel.enforceInterface(descriptor);
                        _arg07 = data.readString();
                        if (data.readInt() != 0) {
                            _arg119 = (RemoteAnimationAdapter) RemoteAnimationAdapter.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg119 = null;
                        }
                        registerRemoteAnimationForNextActivityStart(_arg07, _arg119);
                        reply.writeNoException();
                        return true;
                    case 146:
                        parcel.enforceInterface(descriptor);
                        _result = data.readInt();
                        if (data.readInt() != 0) {
                            _arg19 = (RemoteAnimationDefinition) RemoteAnimationDefinition.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg19 = null;
                        }
                        registerRemoteAnimationsForDisplay(_result, _arg19);
                        reply.writeNoException();
                        return true;
                    case 147:
                        ComponentName _arg018;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg018 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg018 = null;
                        }
                        alwaysShowUnsupportedCompileSdkWarning(_arg018);
                        reply.writeNoException();
                        return true;
                    case 148:
                        parcel.enforceInterface(descriptor);
                        setVrThread(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 149:
                        parcel.enforceInterface(descriptor);
                        setPersistentVrThread(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 150:
                        parcel.enforceInterface(descriptor);
                        stopAppSwitches();
                        reply.writeNoException();
                        return true;
                    case 151:
                        parcel.enforceInterface(descriptor);
                        resumeAppSwitches();
                        reply.writeNoException();
                        return true;
                    case 152:
                        parcel.enforceInterface(descriptor);
                        IActivityController _arg019 = android.app.IActivityController.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setActivityController(_arg019, _arg11);
                        reply.writeNoException();
                        return true;
                    case 153:
                        parcel.enforceInterface(descriptor);
                        IVoiceInteractionSession _arg020 = android.service.voice.IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setVoiceKeepAwake(_arg020, _arg11);
                        reply.writeNoException();
                        return true;
                    case 154:
                        parcel.enforceInterface(descriptor);
                        _arg15 = getPackageScreenCompatMode(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_arg15);
                        return true;
                    case 155:
                        parcel.enforceInterface(descriptor);
                        setPackageScreenCompatMode(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 156:
                        parcel.enforceInterface(descriptor);
                        z = getPackageAskScreenCompat(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(z);
                        return true;
                    case 157:
                        parcel.enforceInterface(descriptor);
                        _arg07 = data.readString();
                        if (data.readInt() != 0) {
                            _arg11 = true;
                        }
                        setPackageAskScreenCompat(_arg07, _arg11);
                        reply.writeNoException();
                        return true;
                    case 158:
                        parcel.enforceInterface(descriptor);
                        clearLaunchParamsForPackages(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 159:
                        parcel.enforceInterface(descriptor);
                        setDisplayToSingleTaskInstance(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 160:
                        parcel.enforceInterface(descriptor);
                        restartActivityProcessIfVisible(data.readStrongBinder());
                        reply.writeNoException();
                        return true;
                    case 161:
                        parcel.enforceInterface(descriptor);
                        onBackPressedOnTaskRoot(data.readStrongBinder(), android.app.IRequestFinishCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 162:
                        parcel.enforceInterface(descriptor);
                        _result3 = handleFreeformModeRequst(data.readStrongBinder(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 163:
                        parcel.enforceInterface(descriptor);
                        _arg04 = getTopVisibleActivity();
                        reply.writeNoException();
                        if (_arg04 != null) {
                            parcel2.writeInt(1);
                            _arg04.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            z = true;
            parcel3 = parcel;
            parcel2.writeString(descriptor);
            return z;
        }

        public static boolean setDefaultImpl(IActivityTaskManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IActivityTaskManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void activityDestroyed(IBinder iBinder) throws RemoteException;

    void activityIdle(IBinder iBinder, Configuration configuration, boolean z) throws RemoteException;

    void activityPaused(IBinder iBinder) throws RemoteException;

    void activityRelaunched(IBinder iBinder) throws RemoteException;

    void activityResumed(IBinder iBinder) throws RemoteException;

    void activitySlept(IBinder iBinder) throws RemoteException;

    void activityStopped(IBinder iBinder, Bundle bundle, PersistableBundle persistableBundle, CharSequence charSequence) throws RemoteException;

    void activityTopResumedStateLost() throws RemoteException;

    int addAppTask(IBinder iBinder, Intent intent, TaskDescription taskDescription, Bitmap bitmap) throws RemoteException;

    void alwaysShowUnsupportedCompileSdkWarning(ComponentName componentName) throws RemoteException;

    void cancelRecentsAnimation(boolean z) throws RemoteException;

    void cancelTaskWindowTransition(int i) throws RemoteException;

    void clearLaunchParamsForPackages(List<String> list) throws RemoteException;

    boolean convertFromTranslucent(IBinder iBinder) throws RemoteException;

    boolean convertToTranslucent(IBinder iBinder, Bundle bundle) throws RemoteException;

    void dismissKeyguard(IBinder iBinder, IKeyguardDismissCallback iKeyguardDismissCallback, CharSequence charSequence) throws RemoteException;

    void dismissPip(boolean z, int i) throws RemoteException;

    void dismissSplitScreenMode(boolean z) throws RemoteException;

    boolean enterPictureInPictureMode(IBinder iBinder, PictureInPictureParams pictureInPictureParams) throws RemoteException;

    boolean finishActivity(IBinder iBinder, int i, Intent intent, int i2) throws RemoteException;

    boolean finishActivityAffinity(IBinder iBinder) throws RemoteException;

    void finishSubActivity(IBinder iBinder, String str, int i) throws RemoteException;

    void finishVoiceTask(IVoiceInteractionSession iVoiceInteractionSession) throws RemoteException;

    ComponentName getActivityClassForToken(IBinder iBinder) throws RemoteException;

    int getActivityDisplayId(IBinder iBinder) throws RemoteException;

    Bundle getActivityOptions(IBinder iBinder) throws RemoteException;

    List<StackInfo> getAllStackInfos() throws RemoteException;

    Point getAppTaskThumbnailSize() throws RemoteException;

    List<IBinder> getAppTasks(String str) throws RemoteException;

    Bundle getAssistContextExtras(int i) throws RemoteException;

    ComponentName getCallingActivity(IBinder iBinder) throws RemoteException;

    String getCallingPackage(IBinder iBinder) throws RemoteException;

    ConfigurationInfo getDeviceConfigurationInfo() throws RemoteException;

    List<RunningTaskInfo> getFilteredTasks(int i, int i2, int i3) throws RemoteException;

    StackInfo getFocusedStackInfo() throws RemoteException;

    int getFrontActivityScreenCompatMode() throws RemoteException;

    int getLastResumedActivityUserId() throws RemoteException;

    String getLaunchedFromPackage(IBinder iBinder) throws RemoteException;

    int getLaunchedFromUid(IBinder iBinder) throws RemoteException;

    int getLockTaskModeState() throws RemoteException;

    int getMaxNumPictureInPictureActions(IBinder iBinder) throws RemoteException;

    boolean getPackageAskScreenCompat(String str) throws RemoteException;

    String getPackageForToken(IBinder iBinder) throws RemoteException;

    int getPackageScreenCompatMode(String str) throws RemoteException;

    ParceledListSlice getRecentTasks(int i, int i2, int i3) throws RemoteException;

    int getRequestedOrientation(IBinder iBinder) throws RemoteException;

    StackInfo getStackInfo(int i, int i2) throws RemoteException;

    Rect getTaskBounds(int i) throws RemoteException;

    TaskDescription getTaskDescription(int i) throws RemoteException;

    Bitmap getTaskDescriptionIcon(String str, int i) throws RemoteException;

    int getTaskForActivity(IBinder iBinder, boolean z) throws RemoteException;

    TaskSnapshot getTaskSnapshot(int i, boolean z) throws RemoteException;

    List<RunningTaskInfo> getTasks(int i) throws RemoteException;

    Intent getTopVisibleActivity() throws RemoteException;

    IBinder getUriPermissionOwnerForActivity(IBinder iBinder) throws RemoteException;

    int handleFreeformModeRequst(IBinder iBinder, int i) throws RemoteException;

    boolean isActivityStartAllowedOnDisplay(int i, Intent intent, String str, int i2) throws RemoteException;

    boolean isAssistDataAllowedOnCurrentActivity() throws RemoteException;

    boolean isImmersive(IBinder iBinder) throws RemoteException;

    boolean isInLockTaskMode() throws RemoteException;

    boolean isInMultiWindowMode(IBinder iBinder) throws RemoteException;

    boolean isInPictureInPictureMode(IBinder iBinder) throws RemoteException;

    boolean isRootVoiceInteraction(IBinder iBinder) throws RemoteException;

    boolean isTopActivityImmersive() throws RemoteException;

    boolean isTopOfTask(IBinder iBinder) throws RemoteException;

    void keyguardGoingAway(int i) throws RemoteException;

    boolean launchAssistIntent(Intent intent, int i, String str, int i2, Bundle bundle) throws RemoteException;

    boolean moveActivityTaskToBack(IBinder iBinder, boolean z) throws RemoteException;

    void moveStackToDisplay(int i, int i2) throws RemoteException;

    void moveTaskToFront(IApplicationThread iApplicationThread, String str, int i, int i2, Bundle bundle) throws RemoteException;

    void moveTaskToStack(int i, int i2, boolean z) throws RemoteException;

    void moveTasksToFullscreenStack(int i, boolean z) throws RemoteException;

    boolean moveTopActivityToPinnedStack(int i, Rect rect) throws RemoteException;

    boolean navigateUpTo(IBinder iBinder, Intent intent, int i, Intent intent2) throws RemoteException;

    void notifyActivityDrawn(IBinder iBinder) throws RemoteException;

    void notifyEnterAnimationComplete(IBinder iBinder) throws RemoteException;

    void notifyLaunchTaskBehindComplete(IBinder iBinder) throws RemoteException;

    void notifyPinnedStackAnimationEnded() throws RemoteException;

    void notifyPinnedStackAnimationStarted() throws RemoteException;

    void offsetPinnedStackBounds(int i, Rect rect, int i2, int i3, int i4) throws RemoteException;

    void onBackPressedOnTaskRoot(IBinder iBinder, IRequestFinishCallback iRequestFinishCallback) throws RemoteException;

    void overridePendingTransition(IBinder iBinder, String str, int i, int i2) throws RemoteException;

    void positionTaskInStack(int i, int i2, int i3) throws RemoteException;

    void registerRemoteAnimationForNextActivityStart(String str, RemoteAnimationAdapter remoteAnimationAdapter) throws RemoteException;

    void registerRemoteAnimations(IBinder iBinder, RemoteAnimationDefinition remoteAnimationDefinition) throws RemoteException;

    void registerRemoteAnimationsForDisplay(int i, RemoteAnimationDefinition remoteAnimationDefinition) throws RemoteException;

    void registerTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    boolean releaseActivityInstance(IBinder iBinder) throws RemoteException;

    void releaseSomeActivities(IApplicationThread iApplicationThread) throws RemoteException;

    void removeAllVisibleRecentTasks() throws RemoteException;

    void removeStack(int i) throws RemoteException;

    void removeStacksInWindowingModes(int[] iArr) throws RemoteException;

    void removeStacksWithActivityTypes(int[] iArr) throws RemoteException;

    boolean removeTask(int i) throws RemoteException;

    void reportActivityFullyDrawn(IBinder iBinder, boolean z) throws RemoteException;

    void reportAssistContextExtras(IBinder iBinder, Bundle bundle, AssistStructure assistStructure, AssistContent assistContent, Uri uri) throws RemoteException;

    void reportSizeConfigurations(IBinder iBinder, int[] iArr, int[] iArr2, int[] iArr3) throws RemoteException;

    boolean requestAssistContextExtras(int i, IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, boolean z, boolean z2) throws RemoteException;

    boolean requestAutofillData(IAssistDataReceiver iAssistDataReceiver, Bundle bundle, IBinder iBinder, int i) throws RemoteException;

    IBinder requestStartActivityPermissionToken(IBinder iBinder) throws RemoteException;

    void resizeDockedStack(Rect rect, Rect rect2, Rect rect3, Rect rect4, Rect rect5) throws RemoteException;

    void resizePinnedStack(Rect rect, Rect rect2) throws RemoteException;

    void resizeStack(int i, Rect rect, boolean z, boolean z2, boolean z3, int i2) throws RemoteException;

    void resizeTask(int i, Rect rect, int i2) throws RemoteException;

    void restartActivityProcessIfVisible(IBinder iBinder) throws RemoteException;

    void resumeAppSwitches() throws RemoteException;

    void setActivityController(IActivityController iActivityController, boolean z) throws RemoteException;

    void setDisablePreviewScreenshots(IBinder iBinder, boolean z) throws RemoteException;

    void setDisplayToSingleTaskInstance(int i) throws RemoteException;

    void setFocusedStack(int i) throws RemoteException;

    void setFocusedTask(int i) throws RemoteException;

    void setFrontActivityScreenCompatMode(int i) throws RemoteException;

    void setImmersive(IBinder iBinder, boolean z) throws RemoteException;

    void setInheritShowWhenLocked(IBinder iBinder, boolean z) throws RemoteException;

    void setLockScreenShown(boolean z, boolean z2) throws RemoteException;

    void setPackageAskScreenCompat(String str, boolean z) throws RemoteException;

    void setPackageScreenCompatMode(String str, int i) throws RemoteException;

    void setPersistentVrThread(int i) throws RemoteException;

    void setPictureInPictureParams(IBinder iBinder, PictureInPictureParams pictureInPictureParams) throws RemoteException;

    void setRequestedOrientation(IBinder iBinder, int i) throws RemoteException;

    void setShowWhenLocked(IBinder iBinder, boolean z) throws RemoteException;

    void setSplitScreenResizing(boolean z) throws RemoteException;

    void setTaskDescription(IBinder iBinder, TaskDescription taskDescription) throws RemoteException;

    void setTaskResizeable(int i, int i2) throws RemoteException;

    void setTaskWindowingMode(int i, int i2, boolean z) throws RemoteException;

    boolean setTaskWindowingModeSplitScreenPrimary(int i, int i2, boolean z, boolean z2, Rect rect, boolean z3) throws RemoteException;

    void setTurnScreenOn(IBinder iBinder, boolean z) throws RemoteException;

    void setVoiceKeepAwake(IVoiceInteractionSession iVoiceInteractionSession, boolean z) throws RemoteException;

    int setVrMode(IBinder iBinder, boolean z, ComponentName componentName) throws RemoteException;

    void setVrThread(int i) throws RemoteException;

    boolean shouldUpRecreateTask(IBinder iBinder, String str) throws RemoteException;

    boolean showAssistFromActivity(IBinder iBinder, Bundle bundle) throws RemoteException;

    void showLockTaskEscapeMessage(IBinder iBinder) throws RemoteException;

    int startActivities(IApplicationThread iApplicationThread, String str, Intent[] intentArr, String[] strArr, IBinder iBinder, Bundle bundle, int i) throws RemoteException;

    int startActivity(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle) throws RemoteException;

    WaitResult startActivityAndWait(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    int startActivityAsCaller(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, IBinder iBinder2, boolean z, int i3) throws RemoteException;

    int startActivityAsUser(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, ProfilerInfo profilerInfo, Bundle bundle, int i3) throws RemoteException;

    int startActivityFromRecents(int i, Bundle bundle) throws RemoteException;

    int startActivityIntentSender(IApplicationThread iApplicationThread, IIntentSender iIntentSender, IBinder iBinder, Intent intent, String str, IBinder iBinder2, String str2, int i, int i2, int i3, Bundle bundle) throws RemoteException;

    int startActivityWithConfig(IApplicationThread iApplicationThread, String str, Intent intent, String str2, IBinder iBinder, String str3, int i, int i2, Configuration configuration, Bundle bundle, int i3) throws RemoteException;

    int startAssistantActivity(String str, int i, int i2, Intent intent, String str2, Bundle bundle, int i3) throws RemoteException;

    void startInPlaceAnimationOnFrontMostApplication(Bundle bundle) throws RemoteException;

    void startLocalVoiceInteraction(IBinder iBinder, Bundle bundle) throws RemoteException;

    void startLockTaskModeByToken(IBinder iBinder) throws RemoteException;

    boolean startNextMatchingActivity(IBinder iBinder, Intent intent, Bundle bundle) throws RemoteException;

    void startRecentsActivity(Intent intent, IAssistDataReceiver iAssistDataReceiver, IRecentsAnimationRunner iRecentsAnimationRunner) throws RemoteException;

    void startSystemLockTaskMode(int i) throws RemoteException;

    int startVoiceActivity(String str, int i, int i2, Intent intent, String str2, IVoiceInteractionSession iVoiceInteractionSession, IVoiceInteractor iVoiceInteractor, int i3, ProfilerInfo profilerInfo, Bundle bundle, int i4) throws RemoteException;

    void stopAppSwitches() throws RemoteException;

    void stopLocalVoiceInteraction(IBinder iBinder) throws RemoteException;

    void stopLockTaskModeByToken(IBinder iBinder) throws RemoteException;

    void stopSystemLockTaskMode() throws RemoteException;

    boolean supportsLocalVoiceInteraction() throws RemoteException;

    void suppressResizeConfigChanges(boolean z) throws RemoteException;

    void toggleFreeformWindowingMode(IBinder iBinder) throws RemoteException;

    void unhandledBack() throws RemoteException;

    void unregisterTaskStackListener(ITaskStackListener iTaskStackListener) throws RemoteException;

    boolean updateConfiguration(Configuration configuration) throws RemoteException;

    boolean updateDisplayOverrideConfiguration(Configuration configuration, int i) throws RemoteException;

    void updateLockTaskFeatures(int i, int i2) throws RemoteException;

    void updateLockTaskPackages(int i, String[] strArr) throws RemoteException;

    boolean willActivityBeVisible(IBinder iBinder) throws RemoteException;
}
