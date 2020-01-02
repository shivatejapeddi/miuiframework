package android.content.pm;

import android.content.ComponentName;

public final class ActivityPresentationInfo {
    public final ComponentName componentName;
    public final int displayId;
    public final int taskId;

    public ActivityPresentationInfo(int taskId, int displayId, ComponentName componentName) {
        this.taskId = taskId;
        this.displayId = displayId;
        this.componentName = componentName;
    }
}
