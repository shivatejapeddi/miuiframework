package android.widget;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.PixelCopy.OnPixelCopyFinishedListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$Magnifier$K0um0QSTAb4wXwua60CgJIIwGaI implements OnPixelCopyFinishedListener {
    private final /* synthetic */ Magnifier f$0;
    private final /* synthetic */ InternalPopupWindow f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ Point f$3;
    private final /* synthetic */ Bitmap f$4;

    public /* synthetic */ -$$Lambda$Magnifier$K0um0QSTAb4wXwua60CgJIIwGaI(Magnifier magnifier, InternalPopupWindow internalPopupWindow, boolean z, Point point, Bitmap bitmap) {
        this.f$0 = magnifier;
        this.f$1 = internalPopupWindow;
        this.f$2 = z;
        this.f$3 = point;
        this.f$4 = bitmap;
    }

    public final void onPixelCopyFinished(int i) {
        this.f$0.lambda$performPixelCopy$1$Magnifier(this.f$1, this.f$2, this.f$3, this.f$4, i);
    }
}
