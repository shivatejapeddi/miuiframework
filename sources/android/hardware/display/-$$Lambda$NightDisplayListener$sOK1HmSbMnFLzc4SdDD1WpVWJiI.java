package android.hardware.display;

import android.hardware.display.NightDisplayListener.Callback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$NightDisplayListener$sOK1HmSbMnFLzc4SdDD1WpVWJiI implements Runnable {
    private final /* synthetic */ NightDisplayListener f$0;
    private final /* synthetic */ Callback f$1;

    public /* synthetic */ -$$Lambda$NightDisplayListener$sOK1HmSbMnFLzc4SdDD1WpVWJiI(NightDisplayListener nightDisplayListener, Callback callback) {
        this.f$0 = nightDisplayListener;
        this.f$1 = callback;
    }

    public final void run() {
        this.f$0.lambda$setCallback$0$NightDisplayListener(this.f$1);
    }
}
