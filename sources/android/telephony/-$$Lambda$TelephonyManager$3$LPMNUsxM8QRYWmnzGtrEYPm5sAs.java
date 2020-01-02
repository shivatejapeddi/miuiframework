package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$3$LPMNUsxM8QRYWmnzGtrEYPm5sAs implements Runnable {
    private final /* synthetic */ NumberVerificationCallback f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ -$$Lambda$TelephonyManager$3$LPMNUsxM8QRYWmnzGtrEYPm5sAs(NumberVerificationCallback numberVerificationCallback, String str) {
        this.f$0 = numberVerificationCallback;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.onCallReceived(this.f$1);
    }
}
