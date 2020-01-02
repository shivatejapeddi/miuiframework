package android.view;

import android.graphics.HardwareRenderer.FrameDrawingCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ViewRootImpl$IReiNMSbDakZSGbIZuL_ifaFWn8 implements FrameDrawingCallback {
    private final /* synthetic */ FrameDrawingCallback f$0;

    public /* synthetic */ -$$Lambda$ViewRootImpl$IReiNMSbDakZSGbIZuL_ifaFWn8(FrameDrawingCallback frameDrawingCallback) {
        this.f$0 = frameDrawingCallback;
    }

    public final void onFrameDraw(long j) {
        ViewRootImpl.lambda$registerRtFrameCallback$0(this.f$0, j);
    }
}
