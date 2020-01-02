package android.hardware.hdmi;

import android.hardware.hdmi.HdmiSwitchClient.OnSelectListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$HdmiSwitchClient$3$apecUZ8P9DH90drOKNmw2Y8Fspg implements Runnable {
    private final /* synthetic */ OnSelectListener f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$HdmiSwitchClient$3$apecUZ8P9DH90drOKNmw2Y8Fspg(OnSelectListener onSelectListener, int i) {
        this.f$0 = onSelectListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onSelect(this.f$1);
    }
}
