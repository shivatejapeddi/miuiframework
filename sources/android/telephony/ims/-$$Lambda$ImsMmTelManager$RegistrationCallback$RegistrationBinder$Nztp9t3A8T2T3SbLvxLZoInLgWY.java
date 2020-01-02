package android.telephony.ims;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$Nztp9t3A8T2T3SbLvxLZoInLgWY implements ThrowingRunnable {
    private final /* synthetic */ RegistrationBinder f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ ImsReasonInfo f$2;

    public /* synthetic */ -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$Nztp9t3A8T2T3SbLvxLZoInLgWY(RegistrationBinder registrationBinder, int i, ImsReasonInfo imsReasonInfo) {
        this.f$0 = registrationBinder;
        this.f$1 = i;
        this.f$2 = imsReasonInfo;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onTechnologyChangeFailed$7$ImsMmTelManager$RegistrationCallback$RegistrationBinder(this.f$1, this.f$2);
    }
}
