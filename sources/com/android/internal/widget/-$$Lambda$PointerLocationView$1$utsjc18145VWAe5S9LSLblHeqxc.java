package com.android.internal.widget;

import android.graphics.Region;
import com.android.internal.widget.PointerLocationView.AnonymousClass1;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PointerLocationView$1$utsjc18145VWAe5S9LSLblHeqxc implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ Region f$1;

    public /* synthetic */ -$$Lambda$PointerLocationView$1$utsjc18145VWAe5S9LSLblHeqxc(AnonymousClass1 anonymousClass1, Region region) {
        this.f$0 = anonymousClass1;
        this.f$1 = region;
    }

    public final void run() {
        this.f$0.lambda$onSystemGestureExclusionChanged$0$PointerLocationView$1(this.f$1);
    }
}
