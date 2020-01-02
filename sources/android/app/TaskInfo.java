package android.app;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager.TaskDescription;
import android.app.ActivityManager.TaskSnapshot;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class TaskInfo {
    private static final String TAG = "TaskInfo";
    public ComponentName baseActivity;
    public Intent baseIntent;
    @UnsupportedAppUsage
    public final Configuration configuration = new Configuration();
    public int displayId;
    public boolean isRunning;
    @UnsupportedAppUsage
    public long lastActiveTime;
    public int numActivities;
    public ComponentName origActivity;
    public ComponentName realActivity;
    @UnsupportedAppUsage
    public int resizeMode;
    @UnsupportedAppUsage
    public int stackId;
    @UnsupportedAppUsage
    public boolean supportsSplitScreenMultiWindow;
    public TaskDescription taskDescription;
    public int taskId;
    public ComponentName topActivity;
    @UnsupportedAppUsage
    public int userId;

    TaskInfo() {
    }

    private TaskInfo(Parcel source) {
        readFromParcel(source);
    }

    public TaskSnapshot getTaskSnapshot(boolean reducedResolution) {
        try {
            return ActivityManager.getService().getTaskSnapshot(this.taskId, reducedResolution);
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to get task snapshot, taskId=");
            stringBuilder.append(this.taskId);
            Log.e(TAG, stringBuilder.toString(), e);
            return null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void readFromParcel(Parcel source) {
        Intent intent;
        this.userId = source.readInt();
        this.stackId = source.readInt();
        this.taskId = source.readInt();
        this.displayId = source.readInt();
        this.isRunning = source.readBoolean();
        TaskDescription taskDescription = null;
        if (source.readInt() != 0) {
            intent = (Intent) Intent.CREATOR.createFromParcel(source);
        } else {
            intent = null;
        }
        this.baseIntent = intent;
        this.baseActivity = ComponentName.readFromParcel(source);
        this.topActivity = ComponentName.readFromParcel(source);
        this.origActivity = ComponentName.readFromParcel(source);
        this.realActivity = ComponentName.readFromParcel(source);
        this.numActivities = source.readInt();
        this.lastActiveTime = source.readLong();
        if (source.readInt() != 0) {
            taskDescription = (TaskDescription) TaskDescription.CREATOR.createFromParcel(source);
        }
        this.taskDescription = taskDescription;
        this.supportsSplitScreenMultiWindow = source.readBoolean();
        this.resizeMode = source.readInt();
        this.configuration.readFromParcel(source);
    }

    /* Access modifiers changed, original: 0000 */
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeInt(this.stackId);
        dest.writeInt(this.taskId);
        dest.writeInt(this.displayId);
        dest.writeBoolean(this.isRunning);
        if (this.baseIntent != null) {
            dest.writeInt(1);
            this.baseIntent.writeToParcel(dest, 0);
        } else {
            dest.writeInt(0);
        }
        ComponentName.writeToParcel(this.baseActivity, dest);
        ComponentName.writeToParcel(this.topActivity, dest);
        ComponentName.writeToParcel(this.origActivity, dest);
        ComponentName.writeToParcel(this.realActivity, dest);
        dest.writeInt(this.numActivities);
        dest.writeLong(this.lastActiveTime);
        if (this.taskDescription != null) {
            dest.writeInt(1);
            this.taskDescription.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        dest.writeBoolean(this.supportsSplitScreenMultiWindow);
        dest.writeInt(this.resizeMode);
        this.configuration.writeToParcel(dest, flags);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TaskInfo{userId=");
        stringBuilder.append(this.userId);
        stringBuilder.append(" stackId=");
        stringBuilder.append(this.stackId);
        stringBuilder.append(" taskId=");
        stringBuilder.append(this.taskId);
        stringBuilder.append(" displayId=");
        stringBuilder.append(this.displayId);
        stringBuilder.append(" isRunning=");
        stringBuilder.append(this.isRunning);
        stringBuilder.append(" baseIntent=");
        stringBuilder.append(this.baseIntent);
        stringBuilder.append(" baseActivity=");
        stringBuilder.append(this.baseActivity);
        stringBuilder.append(" topActivity=");
        stringBuilder.append(this.topActivity);
        stringBuilder.append(" origActivity=");
        stringBuilder.append(this.origActivity);
        stringBuilder.append(" realActivity=");
        stringBuilder.append(this.realActivity);
        stringBuilder.append(" numActivities=");
        stringBuilder.append(this.numActivities);
        stringBuilder.append(" lastActiveTime=");
        stringBuilder.append(this.lastActiveTime);
        stringBuilder.append(" supportsSplitScreenMultiWindow=");
        stringBuilder.append(this.supportsSplitScreenMultiWindow);
        stringBuilder.append(" resizeMode=");
        stringBuilder.append(this.resizeMode);
        return stringBuilder.toString();
    }
}
