package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$3$VM3y0XwyxZN6vR6ERQTngCQIICc implements Runnable {
    private final /* synthetic */ NumberVerificationCallback f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$TelephonyManager$3$VM3y0XwyxZN6vR6ERQTngCQIICc(NumberVerificationCallback numberVerificationCallback, int i) {
        this.f$0 = numberVerificationCallback;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onVerificationFailed(this.f$1);
    }
}
