package android.service.textclassifier;

import android.service.textclassifier.TextClassifierService.AnonymousClass1;
import android.view.textclassifier.TextClassificationSessionId;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TextClassifierService$1$fhIvecFpMXNthJWnvX-RvpNrPFA implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ TextClassificationSessionId f$1;

    public /* synthetic */ -$$Lambda$TextClassifierService$1$fhIvecFpMXNthJWnvX-RvpNrPFA(AnonymousClass1 anonymousClass1, TextClassificationSessionId textClassificationSessionId) {
        this.f$0 = anonymousClass1;
        this.f$1 = textClassificationSessionId;
    }

    public final void run() {
        this.f$0.lambda$onDestroyTextClassificationSession$8$TextClassifierService$1(this.f$1);
    }
}
