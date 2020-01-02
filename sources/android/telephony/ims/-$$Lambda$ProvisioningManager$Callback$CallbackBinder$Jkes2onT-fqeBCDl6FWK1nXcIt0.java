package android.telephony.ims;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ProvisioningManager$Callback$CallbackBinder$Jkes2onT-fqeBCDl6FWK1nXcIt0 implements ThrowingRunnable {
    private final /* synthetic */ CallbackBinder f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ -$$Lambda$ProvisioningManager$Callback$CallbackBinder$Jkes2onT-fqeBCDl6FWK1nXcIt0(CallbackBinder callbackBinder, int i, String str) {
        this.f$0 = callbackBinder;
        this.f$1 = i;
        this.f$2 = str;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onStringConfigChanged$3$ProvisioningManager$Callback$CallbackBinder(this.f$1, this.f$2);
    }
}
