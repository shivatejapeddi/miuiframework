package android.service.textclassifier;

import android.service.textclassifier.TextClassifierService.AnonymousClass1;
import android.view.textclassifier.TextClassificationContext;
import android.view.textclassifier.TextClassificationSessionId;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TextClassifierService$1$oecuM3n2XJWuEPg_O0hSZtoF0ls implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ TextClassificationContext f$1;
    private final /* synthetic */ TextClassificationSessionId f$2;

    public /* synthetic */ -$$Lambda$TextClassifierService$1$oecuM3n2XJWuEPg_O0hSZtoF0ls(AnonymousClass1 anonymousClass1, TextClassificationContext textClassificationContext, TextClassificationSessionId textClassificationSessionId) {
        this.f$0 = anonymousClass1;
        this.f$1 = textClassificationContext;
        this.f$2 = textClassificationSessionId;
    }

    public final void run() {
        this.f$0.lambda$onCreateTextClassificationSession$7$TextClassifierService$1(this.f$1, this.f$2);
    }
}
