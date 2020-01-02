package miui.slide;

import android.view.View;
import miui.slide.SlideConfig.TouchEventConfig;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SlideConfig$OWrUjwHDwalS9A4Tr0PKIikWk3I implements Runnable {
    private final /* synthetic */ SlideConfig f$0;
    private final /* synthetic */ View f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ TouchEventConfig f$3;

    public /* synthetic */ -$$Lambda$SlideConfig$OWrUjwHDwalS9A4Tr0PKIikWk3I(SlideConfig slideConfig, View view, boolean z, TouchEventConfig touchEventConfig) {
        this.f$0 = slideConfig;
        this.f$1 = view;
        this.f$2 = z;
        this.f$3 = touchEventConfig;
    }

    public final void run() {
        this.f$0.lambda$tryGotoTarget$0$SlideConfig(this.f$1, this.f$2, this.f$3);
    }
}
