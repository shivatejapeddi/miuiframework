package android.os;

import android.os.PowerManager.WakeLock;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PowerManager$WakeLock$VvFzmRZ4ZGlXx7u3lSAJ_T-YUjw implements Runnable {
    private final /* synthetic */ WakeLock f$0;
    private final /* synthetic */ Runnable f$1;

    public /* synthetic */ -$$Lambda$PowerManager$WakeLock$VvFzmRZ4ZGlXx7u3lSAJ_T-YUjw(WakeLock wakeLock, Runnable runnable) {
        this.f$0 = wakeLock;
        this.f$1 = runnable;
    }

    public final void run() {
        this.f$0.lambda$wrap$0$PowerManager$WakeLock(this.f$1);
    }
}
