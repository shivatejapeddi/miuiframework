package android.hardware.biometrics;

import android.hardware.biometrics.BiometricPrompt.AnonymousClass1;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BiometricPrompt$1$aJtOJjyL74ZJt5iM1EsAPg6PHK4 implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ -$$Lambda$BiometricPrompt$1$aJtOJjyL74ZJt5iM1EsAPg6PHK4(AnonymousClass1 anonymousClass1, int i, String str) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
        this.f$2 = str;
    }

    public final void run() {
        this.f$0.lambda$onError$2$BiometricPrompt$1(this.f$1, this.f$2);
    }
}
