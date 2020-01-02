package android.telephony.ims;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$q0Uz23ATIYan5EBJYUigIVvwE3g implements Runnable {
    private final /* synthetic */ RegistrationBinder f$0;
    private final /* synthetic */ ImsReasonInfo f$1;

    public /* synthetic */ -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$q0Uz23ATIYan5EBJYUigIVvwE3g(RegistrationBinder registrationBinder, ImsReasonInfo imsReasonInfo) {
        this.f$0 = registrationBinder;
        this.f$1 = imsReasonInfo;
    }

    public final void run() {
        this.f$0.lambda$onDeregistered$4$ImsMmTelManager$RegistrationCallback$RegistrationBinder(this.f$1);
    }
}
