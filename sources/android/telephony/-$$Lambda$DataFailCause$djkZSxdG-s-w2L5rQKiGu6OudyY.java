package android.telephony;

import java.util.function.IntPredicate;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DataFailCause$djkZSxdG-s-w2L5rQKiGu6OudyY implements IntPredicate {
    private final /* synthetic */ int f$0;

    public /* synthetic */ -$$Lambda$DataFailCause$djkZSxdG-s-w2L5rQKiGu6OudyY(int i) {
        this.f$0 = i;
    }

    public final boolean test(int i) {
        return DataFailCause.lambda$isRadioRestartFailure$0(this.f$0, i);
    }
}
