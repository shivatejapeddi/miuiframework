package android.hardware.biometrics;

import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BiometricPrompt$FhnggONVmg0fSM3ar79llL7ZRYM implements Runnable {
    private final /* synthetic */ BiometricPrompt f$0;
    private final /* synthetic */ AuthenticationCallback f$1;

    public /* synthetic */ -$$Lambda$BiometricPrompt$FhnggONVmg0fSM3ar79llL7ZRYM(BiometricPrompt biometricPrompt, AuthenticationCallback authenticationCallback) {
        this.f$0 = biometricPrompt;
        this.f$1 = authenticationCallback;
    }

    public final void run() {
        this.f$0.lambda$authenticateInternal$1$BiometricPrompt(this.f$1);
    }
}
