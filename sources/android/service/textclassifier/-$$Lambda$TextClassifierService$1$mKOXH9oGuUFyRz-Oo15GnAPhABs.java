package android.service.textclassifier;

import android.service.textclassifier.TextClassifierService.AnonymousClass1;
import android.view.textclassifier.TextClassificationSessionId;
import android.view.textclassifier.TextSelection.Request;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TextClassifierService$1$mKOXH9oGuUFyRz-Oo15GnAPhABs implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ TextClassificationSessionId f$1;
    private final /* synthetic */ Request f$2;
    private final /* synthetic */ ITextClassifierCallback f$3;

    public /* synthetic */ -$$Lambda$TextClassifierService$1$mKOXH9oGuUFyRz-Oo15GnAPhABs(AnonymousClass1 anonymousClass1, TextClassificationSessionId textClassificationSessionId, Request request, ITextClassifierCallback iTextClassifierCallback) {
        this.f$0 = anonymousClass1;
        this.f$1 = textClassificationSessionId;
        this.f$2 = request;
        this.f$3 = iTextClassifierCallback;
    }

    public final void run() {
        this.f$0.lambda$onSuggestSelection$0$TextClassifierService$1(this.f$1, this.f$2, this.f$3);
    }
}
