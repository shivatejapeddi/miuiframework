package android.view;

import android.graphics.HardwareRenderer.FrameCompleteCallback;
import android.os.Handler;
import java.util.ArrayList;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ViewRootImpl$YBiqAhbCbXVPSKdbE3K4rH2gpxI implements FrameCompleteCallback {
    private final /* synthetic */ ViewRootImpl f$0;
    private final /* synthetic */ Handler f$1;
    private final /* synthetic */ ArrayList f$2;

    public /* synthetic */ -$$Lambda$ViewRootImpl$YBiqAhbCbXVPSKdbE3K4rH2gpxI(ViewRootImpl viewRootImpl, Handler handler, ArrayList arrayList) {
        this.f$0 = viewRootImpl;
        this.f$1 = handler;
        this.f$2 = arrayList;
    }

    public final void onFrameComplete(long j) {
        this.f$0.lambda$performDraw$2$ViewRootImpl(this.f$1, this.f$2, j);
    }
}
