package android.telephony.ims;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$IeNlpXTAPM2z2VxFA81E0v9udZw implements Runnable {
    private final /* synthetic */ RegistrationBinder f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ ImsReasonInfo f$2;

    public /* synthetic */ -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$IeNlpXTAPM2z2VxFA81E0v9udZw(RegistrationBinder registrationBinder, int i, ImsReasonInfo imsReasonInfo) {
        this.f$0 = registrationBinder;
        this.f$1 = i;
        this.f$2 = imsReasonInfo;
    }

    public final void run() {
        this.f$0.lambda$onTechnologyChangeFailed$6$ImsMmTelManager$RegistrationCallback$RegistrationBinder(this.f$1, this.f$2);
    }
}
