package android.service.textclassifier;

import android.service.textclassifier.TextClassifierService.Callback;
import android.view.textclassifier.TextLanguage.Request;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TextClassifierService$9kfVuo6FJ1uQiU277-n9JgliEEc implements Runnable {
    private final /* synthetic */ TextClassifierService f$0;
    private final /* synthetic */ Callback f$1;
    private final /* synthetic */ Request f$2;

    public /* synthetic */ -$$Lambda$TextClassifierService$9kfVuo6FJ1uQiU277-n9JgliEEc(TextClassifierService textClassifierService, Callback callback, Request request) {
        this.f$0 = textClassifierService;
        this.f$1 = callback;
        this.f$2 = request;
    }

    public final void run() {
        this.f$0.lambda$onDetectLanguage$0$TextClassifierService(this.f$1, this.f$2);
    }
}
