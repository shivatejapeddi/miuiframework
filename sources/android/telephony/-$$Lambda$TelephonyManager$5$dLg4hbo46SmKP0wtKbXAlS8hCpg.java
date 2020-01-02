package android.telephony;

import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$5$dLg4hbo46SmKP0wtKbXAlS8hCpg implements Runnable {
    private final /* synthetic */ Consumer f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$TelephonyManager$5$dLg4hbo46SmKP0wtKbXAlS8hCpg(Consumer consumer, int i) {
        this.f$0 = consumer;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.accept(Integer.valueOf(this.f$1));
    }
}
