package android.telephony.ims;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ProvisioningManager$Callback$CallbackBinder$rsWuitP9riQDO6nFxj5wJBdYX40 implements Runnable {
    private final /* synthetic */ CallbackBinder f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ -$$Lambda$ProvisioningManager$Callback$CallbackBinder$rsWuitP9riQDO6nFxj5wJBdYX40(CallbackBinder callbackBinder, int i, String str) {
        this.f$0 = callbackBinder;
        this.f$1 = i;
        this.f$2 = str;
    }

    public final void run() {
        this.f$0.lambda$onStringConfigChanged$2$ProvisioningManager$Callback$CallbackBinder(this.f$1, this.f$2);
    }
}
