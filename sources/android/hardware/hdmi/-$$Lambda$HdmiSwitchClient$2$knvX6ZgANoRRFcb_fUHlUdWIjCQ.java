package android.hardware.hdmi;

import android.hardware.hdmi.HdmiSwitchClient.OnSelectListener;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$HdmiSwitchClient$2$knvX6ZgANoRRFcb_fUHlUdWIjCQ implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ OnSelectListener f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$HdmiSwitchClient$2$knvX6ZgANoRRFcb_fUHlUdWIjCQ(Executor executor, OnSelectListener onSelectListener, int i) {
        this.f$0 = executor;
        this.f$1 = onSelectListener;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$HdmiSwitchClient$2$wYF9AcLTW87bh8nh0L1O42--jdg(this.f$1, this.f$2));
    }
}
