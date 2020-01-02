package android.hardware.hdmi;

import android.hardware.hdmi.HdmiSwitchClient.OnSelectListener;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$HdmiSwitchClient$3$Cqxvec1NmkC6VlEdX5OEOabobXY implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ OnSelectListener f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$HdmiSwitchClient$3$Cqxvec1NmkC6VlEdX5OEOabobXY(Executor executor, OnSelectListener onSelectListener, int i) {
        this.f$0 = executor;
        this.f$1 = onSelectListener;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$HdmiSwitchClient$3$apecUZ8P9DH90drOKNmw2Y8Fspg(this.f$1, this.f$2));
    }
}
