package android.telephony.ims;

import android.net.Uri;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$jAP4lCkBQEdyrlgt5jaNPTlFXlY implements Runnable {
    private final /* synthetic */ RegistrationBinder f$0;
    private final /* synthetic */ Uri[] f$1;

    public /* synthetic */ -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$jAP4lCkBQEdyrlgt5jaNPTlFXlY(RegistrationBinder registrationBinder, Uri[] uriArr) {
        this.f$0 = registrationBinder;
        this.f$1 = uriArr;
    }

    public final void run() {
        this.f$0.lambda$onSubscriberAssociatedUriChanged$8$ImsMmTelManager$RegistrationCallback$RegistrationBinder(this.f$1);
    }
}
