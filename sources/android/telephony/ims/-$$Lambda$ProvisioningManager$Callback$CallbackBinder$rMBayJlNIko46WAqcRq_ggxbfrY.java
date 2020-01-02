package android.telephony.ims;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ProvisioningManager$Callback$CallbackBinder$rMBayJlNIko46WAqcRq_ggxbfrY implements ThrowingRunnable {
    private final /* synthetic */ CallbackBinder f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$ProvisioningManager$Callback$CallbackBinder$rMBayJlNIko46WAqcRq_ggxbfrY(CallbackBinder callbackBinder, int i, int i2) {
        this.f$0 = callbackBinder;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onIntConfigChanged$1$ProvisioningManager$Callback$CallbackBinder(this.f$1, this.f$2);
    }
}
