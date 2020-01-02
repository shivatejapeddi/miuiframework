package android.os;

import android.view.IWindowManager;
import java.util.ArrayList;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$StrictMode$AndroidBlockGuardPolicy$9nBulCQKaMajrWr41SB7f7YRT1I implements Runnable {
    private final /* synthetic */ AndroidBlockGuardPolicy f$0;
    private final /* synthetic */ IWindowManager f$1;
    private final /* synthetic */ ArrayList f$2;

    public /* synthetic */ -$$Lambda$StrictMode$AndroidBlockGuardPolicy$9nBulCQKaMajrWr41SB7f7YRT1I(AndroidBlockGuardPolicy androidBlockGuardPolicy, IWindowManager iWindowManager, ArrayList arrayList) {
        this.f$0 = androidBlockGuardPolicy;
        this.f$1 = iWindowManager;
        this.f$2 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$handleViolationWithTimingAttempt$0$StrictMode$AndroidBlockGuardPolicy(this.f$1, this.f$2);
    }
}
