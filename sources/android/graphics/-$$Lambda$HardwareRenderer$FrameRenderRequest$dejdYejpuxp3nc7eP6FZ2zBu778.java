package android.graphics;

import android.graphics.HardwareRenderer.FrameCompleteCallback;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$HardwareRenderer$FrameRenderRequest$dejdYejpuxp3nc7eP6FZ2zBu778 implements FrameCompleteCallback {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ Runnable f$1;

    public /* synthetic */ -$$Lambda$HardwareRenderer$FrameRenderRequest$dejdYejpuxp3nc7eP6FZ2zBu778(Executor executor, Runnable runnable) {
        this.f$0 = executor;
        this.f$1 = runnable;
    }

    public final void onFrameComplete(long j) {
        this.f$0.execute(this.f$1);
    }
}
