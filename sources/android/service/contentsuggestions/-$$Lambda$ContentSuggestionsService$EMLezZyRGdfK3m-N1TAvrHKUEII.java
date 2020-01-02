package android.service.contentsuggestions;

import android.app.contentsuggestions.ContentSuggestionsManager.ClassificationsCallback;
import android.app.contentsuggestions.IClassificationsCallback;
import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ContentSuggestionsService$EMLezZyRGdfK3m-N1TAvrHKUEII implements ClassificationsCallback {
    private final /* synthetic */ IClassificationsCallback f$0;

    public /* synthetic */ -$$Lambda$ContentSuggestionsService$EMLezZyRGdfK3m-N1TAvrHKUEII(IClassificationsCallback iClassificationsCallback) {
        this.f$0 = iClassificationsCallback;
    }

    public final void onContentClassificationsAvailable(int i, List list) {
        ContentSuggestionsService.lambda$wrapClassificationCallback$1(this.f$0, i, list);
    }
}
