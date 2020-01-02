package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$4i1RRVjnCzfQvX2hIGG9K8g4DaY implements Runnable {
    private final /* synthetic */ NumberVerificationCallback f$0;

    public /* synthetic */ -$$Lambda$TelephonyManager$4i1RRVjnCzfQvX2hIGG9K8g4DaY(NumberVerificationCallback numberVerificationCallback) {
        this.f$0 = numberVerificationCallback;
    }

    public final void run() {
        this.f$0.onVerificationFailed(0);
    }
}
