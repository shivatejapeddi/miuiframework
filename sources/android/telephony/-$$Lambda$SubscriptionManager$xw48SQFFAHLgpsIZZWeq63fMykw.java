package android.telephony;

import com.android.internal.telephony.ISub;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SubscriptionManager$xw48SQFFAHLgpsIZZWeq63fMykw implements CallISubMethodHelper {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$SubscriptionManager$xw48SQFFAHLgpsIZZWeq63fMykw(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final int callMethod(ISub iSub) {
        return iSub.setDataRoaming(this.f$0, this.f$1);
    }
}
