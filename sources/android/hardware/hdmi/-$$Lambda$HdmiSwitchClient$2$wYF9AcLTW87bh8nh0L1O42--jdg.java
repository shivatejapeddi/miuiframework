package android.hardware.hdmi;

import android.hardware.hdmi.HdmiSwitchClient.OnSelectListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$HdmiSwitchClient$2$wYF9AcLTW87bh8nh0L1O42--jdg implements Runnable {
    private final /* synthetic */ OnSelectListener f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$HdmiSwitchClient$2$wYF9AcLTW87bh8nh0L1O42--jdg(OnSelectListener onSelectListener, int i) {
        this.f$0 = onSelectListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onSelect(this.f$1);
    }
}
