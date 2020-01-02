package android.app;

import android.app.IProcessObserver.Stub;

public abstract class IMiuiProcessObserver extends Stub {
    public abstract void onForegroundActivitiesChanged(int i, int i2, boolean z);

    public abstract void onImportanceChanged(int i, int i2, int i3);

    public abstract void onProcessDied(int i, int i2);

    public abstract void onProcessStateChanged(int i, int i2, int i3);
}
