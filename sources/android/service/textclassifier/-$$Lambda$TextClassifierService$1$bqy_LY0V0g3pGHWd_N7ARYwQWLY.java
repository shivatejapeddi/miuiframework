package android.service.textclassifier;

import android.service.textclassifier.TextClassifierService.AnonymousClass1;
import android.view.textclassifier.TextClassificationSessionId;
import android.view.textclassifier.TextClassifierEvent;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TextClassifierService$1$bqy_LY0V0g3pGHWd_N7ARYwQWLY implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ TextClassificationSessionId f$1;
    private final /* synthetic */ TextClassifierEvent f$2;

    public /* synthetic */ -$$Lambda$TextClassifierService$1$bqy_LY0V0g3pGHWd_N7ARYwQWLY(AnonymousClass1 anonymousClass1, TextClassificationSessionId textClassificationSessionId, TextClassifierEvent textClassifierEvent) {
        this.f$0 = anonymousClass1;
        this.f$1 = textClassificationSessionId;
        this.f$2 = textClassifierEvent;
    }

    public final void run() {
        this.f$0.lambda$onTextClassifierEvent$4$TextClassifierService$1(this.f$1, this.f$2);
    }
}
