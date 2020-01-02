package android.service.contentsuggestions;

import android.app.contentsuggestions.ContentSuggestionsManager.SelectionsCallback;
import android.app.contentsuggestions.ISelectionsCallback;
import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ContentSuggestionsService$Cq6WuwbJQLqgS0UnqLBYUMft1GM implements SelectionsCallback {
    private final /* synthetic */ ISelectionsCallback f$0;

    public /* synthetic */ -$$Lambda$ContentSuggestionsService$Cq6WuwbJQLqgS0UnqLBYUMft1GM(ISelectionsCallback iSelectionsCallback) {
        this.f$0 = iSelectionsCallback;
    }

    public final void onContentSelectionsAvailable(int i, List list) {
        ContentSuggestionsService.lambda$wrapSelectionsCallback$0(this.f$0, i, list);
    }
}
