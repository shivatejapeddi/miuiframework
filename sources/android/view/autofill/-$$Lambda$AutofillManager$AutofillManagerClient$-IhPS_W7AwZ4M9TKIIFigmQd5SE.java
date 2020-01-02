package android.view.autofill;

import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AutofillManager$AutofillManagerClient$-IhPS_W7AwZ4M9TKIIFigmQd5SE implements Runnable {
    private final /* synthetic */ AutofillManager f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ List f$2;

    public /* synthetic */ -$$Lambda$AutofillManager$AutofillManagerClient$-IhPS_W7AwZ4M9TKIIFigmQd5SE(AutofillManager autofillManager, int i, List list) {
        this.f$0 = autofillManager;
        this.f$1 = i;
        this.f$2 = list;
    }

    public final void run() {
        this.f$0.setSessionFinished(this.f$1, this.f$2);
    }
}
