package android.widget;

import android.graphics.HardwareRenderer.FrameDrawingCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$Magnifier$InternalPopupWindow$qfjMrDJVvOQUv9_kKVdpLzbaJ-A implements FrameDrawingCallback {
    private final /* synthetic */ InternalPopupWindow f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ boolean f$4;

    public /* synthetic */ -$$Lambda$Magnifier$InternalPopupWindow$qfjMrDJVvOQUv9_kKVdpLzbaJ-A(InternalPopupWindow internalPopupWindow, boolean z, int i, int i2, boolean z2) {
        this.f$0 = internalPopupWindow;
        this.f$1 = z;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = z2;
    }

    public final void onFrameDraw(long j) {
        this.f$0.lambda$doDraw$0$Magnifier$InternalPopupWindow(this.f$1, this.f$2, this.f$3, this.f$4, j);
    }
}
