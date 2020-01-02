package android.hardware.face;

import android.hardware.face.FaceManager.AnonymousClass2;
import android.hardware.face.FaceManager.LockoutResetCallback;
import android.os.PowerManager.WakeLock;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$FaceManager$2$IVmrd2VOH7JdDdb7PFFlL5bjZ5w implements Runnable {
    private final /* synthetic */ LockoutResetCallback f$0;
    private final /* synthetic */ WakeLock f$1;

    public /* synthetic */ -$$Lambda$FaceManager$2$IVmrd2VOH7JdDdb7PFFlL5bjZ5w(LockoutResetCallback lockoutResetCallback, WakeLock wakeLock) {
        this.f$0 = lockoutResetCallback;
        this.f$1 = wakeLock;
    }

    public final void run() {
        AnonymousClass2.lambda$onLockoutReset$0(this.f$0, this.f$1);
    }
}
