package android.service.textclassifier;

import android.service.textclassifier.TextClassifierService.AnonymousClass1;
import android.view.textclassifier.SelectionEvent;
import android.view.textclassifier.TextClassificationSessionId;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TextClassifierService$1$-Nsl56ysLPoVPJ4Gu0VUwYCh4wE implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ TextClassificationSessionId f$1;
    private final /* synthetic */ SelectionEvent f$2;

    public /* synthetic */ -$$Lambda$TextClassifierService$1$-Nsl56ysLPoVPJ4Gu0VUwYCh4wE(AnonymousClass1 anonymousClass1, TextClassificationSessionId textClassificationSessionId, SelectionEvent selectionEvent) {
        this.f$0 = anonymousClass1;
        this.f$1 = textClassificationSessionId;
        this.f$2 = selectionEvent;
    }

    public final void run() {
        this.f$0.lambda$onSelectionEvent$3$TextClassifierService$1(this.f$1, this.f$2);
    }
}
