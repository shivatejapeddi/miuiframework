package android.app;

import android.app.ActivityThread.ActivityClientRecord;
import android.content.res.Configuration;
import android.view.ViewRootImpl.ActivityConfigCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ActivityThread$ActivityClientRecord$HOrG1qglSjSUHSjKBn2rXtX0gGg implements ActivityConfigCallback {
    private final /* synthetic */ ActivityClientRecord f$0;

    public /* synthetic */ -$$Lambda$ActivityThread$ActivityClientRecord$HOrG1qglSjSUHSjKBn2rXtX0gGg(ActivityClientRecord activityClientRecord) {
        this.f$0 = activityClientRecord;
    }

    public final void onConfigurationChanged(Configuration configuration, int i) {
        this.f$0.lambda$init$0$ActivityThread$ActivityClientRecord(configuration, i);
    }
}
