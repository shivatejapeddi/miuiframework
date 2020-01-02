package android.telephony;

import com.android.internal.telephony.ISub;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SubscriptionManager$NazcIP1h3U0cfnY--L174e3u4tk implements CallISubMethodHelper {
    private final /* synthetic */ SubscriptionManager f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$SubscriptionManager$NazcIP1h3U0cfnY--L174e3u4tk(SubscriptionManager subscriptionManager, boolean z, int i) {
        this.f$0 = subscriptionManager;
        this.f$1 = z;
        this.f$2 = i;
    }

    public final int callMethod(ISub iSub) {
        return this.f$0.lambda$setOpportunistic$5$SubscriptionManager(this.f$1, this.f$2, iSub);
    }
}
