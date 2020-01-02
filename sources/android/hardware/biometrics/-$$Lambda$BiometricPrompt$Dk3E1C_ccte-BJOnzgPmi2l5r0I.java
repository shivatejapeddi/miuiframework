package android.hardware.biometrics;

import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BiometricPrompt$Dk3E1C_ccte-BJOnzgPmi2l5r0I implements Runnable {
    private final /* synthetic */ BiometricPrompt f$0;
    private final /* synthetic */ AuthenticationCallback f$1;

    public /* synthetic */ -$$Lambda$BiometricPrompt$Dk3E1C_ccte-BJOnzgPmi2l5r0I(BiometricPrompt biometricPrompt, AuthenticationCallback authenticationCallback) {
        this.f$0 = biometricPrompt;
        this.f$1 = authenticationCallback;
    }

    public final void run() {
        this.f$0.lambda$authenticateInternal$0$BiometricPrompt(this.f$1);
    }
}
