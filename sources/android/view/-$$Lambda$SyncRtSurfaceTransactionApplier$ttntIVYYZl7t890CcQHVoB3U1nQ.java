package android.view;

import android.graphics.HardwareRenderer.FrameDrawingCallback;
import android.view.SyncRtSurfaceTransactionApplier.SurfaceParams;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SyncRtSurfaceTransactionApplier$ttntIVYYZl7t890CcQHVoB3U1nQ implements FrameDrawingCallback {
    private final /* synthetic */ SyncRtSurfaceTransactionApplier f$0;
    private final /* synthetic */ SurfaceParams[] f$1;

    public /* synthetic */ -$$Lambda$SyncRtSurfaceTransactionApplier$ttntIVYYZl7t890CcQHVoB3U1nQ(SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier, SurfaceParams[] surfaceParamsArr) {
        this.f$0 = syncRtSurfaceTransactionApplier;
        this.f$1 = surfaceParamsArr;
    }

    public final void onFrameDraw(long j) {
        this.f$0.lambda$scheduleApply$0$SyncRtSurfaceTransactionApplier(this.f$1, j);
    }
}
