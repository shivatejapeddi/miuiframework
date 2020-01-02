package android.telephony;

import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$6$AFjFk42NCFYCMG8wA5-6SCfk7No implements Runnable {
    private final /* synthetic */ Consumer f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$TelephonyManager$6$AFjFk42NCFYCMG8wA5-6SCfk7No(Consumer consumer, int i) {
        this.f$0 = consumer;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.accept(Integer.valueOf(this.f$1));
    }
}
