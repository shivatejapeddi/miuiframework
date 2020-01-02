package android.service.textclassifier;

import android.service.textclassifier.TextClassifierService.Callback;
import android.view.textclassifier.ConversationActions.Request;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TextClassifierService$OMrgO9sL3mlBJfpfxbmg7ieGoWk implements Runnable {
    private final /* synthetic */ TextClassifierService f$0;
    private final /* synthetic */ Callback f$1;
    private final /* synthetic */ Request f$2;

    public /* synthetic */ -$$Lambda$TextClassifierService$OMrgO9sL3mlBJfpfxbmg7ieGoWk(TextClassifierService textClassifierService, Callback callback, Request request) {
        this.f$0 = textClassifierService;
        this.f$1 = callback;
        this.f$2 = request;
    }

    public final void run() {
        this.f$0.lambda$onSuggestConversationActions$1$TextClassifierService(this.f$1, this.f$2);
    }
}
