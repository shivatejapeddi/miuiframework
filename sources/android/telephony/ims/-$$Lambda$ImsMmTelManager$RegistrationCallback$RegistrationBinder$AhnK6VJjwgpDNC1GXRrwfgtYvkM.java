package android.telephony.ims;

import android.net.Uri;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$AhnK6VJjwgpDNC1GXRrwfgtYvkM implements ThrowingRunnable {
    private final /* synthetic */ RegistrationBinder f$0;
    private final /* synthetic */ Uri[] f$1;

    public /* synthetic */ -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$AhnK6VJjwgpDNC1GXRrwfgtYvkM(RegistrationBinder registrationBinder, Uri[] uriArr) {
        this.f$0 = registrationBinder;
        this.f$1 = uriArr;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onSubscriberAssociatedUriChanged$9$ImsMmTelManager$RegistrationCallback$RegistrationBinder(this.f$1);
    }
}
