package android.view;

import android.graphics.HardwareRenderer.FrameCompleteCallback;
import android.os.Handler;
import java.util.ArrayList;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ViewRootImpl$zlBUjCwDtoAWMNaHI62DIq-eKFY implements FrameCompleteCallback {
    private final /* synthetic */ Handler f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ -$$Lambda$ViewRootImpl$zlBUjCwDtoAWMNaHI62DIq-eKFY(Handler handler, ArrayList arrayList) {
        this.f$0 = handler;
        this.f$1 = arrayList;
    }

    public final void onFrameComplete(long j) {
        this.f$0.postAtFrontOfQueue(new -$$Lambda$ViewRootImpl$-dgEKMWLAJVMlaVy41safRlNQBo(this.f$1));
    }
}
