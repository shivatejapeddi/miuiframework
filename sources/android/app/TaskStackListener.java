package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityManager.TaskDescription;
import android.app.ActivityManager.TaskSnapshot;
import android.app.ITaskStackListener.Stub;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;

public abstract class TaskStackListener extends Stub {
    @UnsupportedAppUsage
    public void onTaskStackChanged() throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onActivityPinned(String packageName, int userId, int taskId, int stackId) throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onActivityUnpinned() throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onPinnedActivityRestartAttempt(boolean clearedTask) throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onPinnedStackAnimationStarted() throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onPinnedStackAnimationEnded() throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onActivityForcedResizable(String packageName, int taskId, int reason) throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onActivityDismissingDockedStack() throws RemoteException {
    }

    public void onActivityLaunchOnSecondaryDisplayFailed(RunningTaskInfo taskInfo, int requestedDisplayId) throws RemoteException {
        onActivityLaunchOnSecondaryDisplayFailed();
    }

    @Deprecated
    @UnsupportedAppUsage
    public void onActivityLaunchOnSecondaryDisplayFailed() throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onActivityLaunchOnSecondaryDisplayRerouted(RunningTaskInfo taskInfo, int requestedDisplayId) throws RemoteException {
    }

    public void onTaskCreated(int taskId, ComponentName componentName) throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onTaskRemoved(int taskId) throws RemoteException {
    }

    public void onTaskMovedToFront(RunningTaskInfo taskInfo) throws RemoteException {
        onTaskMovedToFront(taskInfo.taskId);
    }

    @Deprecated
    @UnsupportedAppUsage
    public void onTaskMovedToFront(int taskId) throws RemoteException {
    }

    public void onTaskRemovalStarted(RunningTaskInfo taskInfo) throws RemoteException {
        onTaskRemovalStarted(taskInfo.taskId);
    }

    @Deprecated
    public void onTaskRemovalStarted(int taskId) throws RemoteException {
    }

    public void onTaskDescriptionChanged(RunningTaskInfo taskInfo) throws RemoteException {
        onTaskDescriptionChanged(taskInfo.taskId, taskInfo.taskDescription);
    }

    @Deprecated
    public void onTaskDescriptionChanged(int taskId, TaskDescription td) throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onActivityRequestedOrientationChanged(int taskId, int requestedOrientation) throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onTaskProfileLocked(int taskId, int userId) throws RemoteException {
    }

    @UnsupportedAppUsage
    public void onTaskSnapshotChanged(int taskId, TaskSnapshot snapshot) throws RemoteException {
        if (Binder.getCallingPid() != Process.myPid() && snapshot != null && snapshot.getSnapshot() != null) {
            snapshot.getSnapshot().destroy();
        }
    }

    @UnsupportedAppUsage
    public void onSizeCompatModeActivityChanged(int displayId, IBinder activityToken) throws RemoteException {
    }

    public void onBackPressedOnTaskRoot(RunningTaskInfo taskInfo) throws RemoteException {
    }

    public void onTaskDisplayChanged(int taskId, int newDisplayId) throws RemoteException {
    }
}
