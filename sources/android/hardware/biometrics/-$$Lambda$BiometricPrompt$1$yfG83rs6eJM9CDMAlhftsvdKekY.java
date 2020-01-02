package android.hardware.biometrics;

import android.hardware.biometrics.BiometricPrompt.AnonymousClass1;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BiometricPrompt$1$yfG83rs6eJM9CDMAlhftsvdKekY implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ -$$Lambda$BiometricPrompt$1$yfG83rs6eJM9CDMAlhftsvdKekY(AnonymousClass1 anonymousClass1, int i, String str) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
        this.f$2 = str;
    }

    public final void run() {
        this.f$0.lambda$onAcquired$3$BiometricPrompt$1(this.f$1, this.f$2);
    }
}
