package android.telephony;

import com.android.internal.telephony.ISub;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SubscriptionManager$3ws2BzXOcyDc-7TPZx2HIBCIjbs implements CallISubMethodHelper {
    private final /* synthetic */ String f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$SubscriptionManager$3ws2BzXOcyDc-7TPZx2HIBCIjbs(String str, int i) {
        this.f$0 = str;
        this.f$1 = i;
    }

    public final int callMethod(ISub iSub) {
        return iSub.setDisplayNumber(this.f$0, this.f$1);
    }
}
