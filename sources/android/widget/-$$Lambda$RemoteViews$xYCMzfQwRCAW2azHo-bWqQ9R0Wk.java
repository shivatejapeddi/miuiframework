package android.widget;

import android.app.PendingIntent;
import android.view.View;
import android.widget.RemoteViews.OnClickHandler;
import android.widget.RemoteViews.RemoteResponse;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RemoteViews$xYCMzfQwRCAW2azHo-bWqQ9R0Wk implements OnClickHandler {
    public static final /* synthetic */ -$$Lambda$RemoteViews$xYCMzfQwRCAW2azHo-bWqQ9R0Wk INSTANCE = new -$$Lambda$RemoteViews$xYCMzfQwRCAW2azHo-bWqQ9R0Wk();

    private /* synthetic */ -$$Lambda$RemoteViews$xYCMzfQwRCAW2azHo-bWqQ9R0Wk() {
    }

    public final boolean onClickHandler(View view, PendingIntent pendingIntent, RemoteResponse remoteResponse) {
        return RemoteViews.startPendingIntent(view, pendingIntent, remoteResponse.getLaunchOptions(view));
    }
}
