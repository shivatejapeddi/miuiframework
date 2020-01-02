package android.telephony;

import com.android.internal.telephony.ISub;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SubscriptionManager$OS3WICha4HbZhTnWrKCxeu6dr6g implements CallISubMethodHelper {
    private final /* synthetic */ String f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$SubscriptionManager$OS3WICha4HbZhTnWrKCxeu6dr6g(String str, int i, int i2) {
        this.f$0 = str;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final int callMethod(ISub iSub) {
        return iSub.setDisplayNameUsingSrc(this.f$0, this.f$1, this.f$2);
    }
}
