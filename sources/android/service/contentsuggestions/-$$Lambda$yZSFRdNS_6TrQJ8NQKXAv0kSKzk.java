package android.service.contentsuggestions;

import android.app.contentsuggestions.ContentSuggestionsManager.SelectionsCallback;
import android.app.contentsuggestions.SelectionsRequest;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$yZSFRdNS_6TrQJ8NQKXAv0kSKzk implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$yZSFRdNS_6TrQJ8NQKXAv0kSKzk INSTANCE = new -$$Lambda$yZSFRdNS_6TrQJ8NQKXAv0kSKzk();

    private /* synthetic */ -$$Lambda$yZSFRdNS_6TrQJ8NQKXAv0kSKzk() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((ContentSuggestionsService) obj).onSuggestContentSelections((SelectionsRequest) obj2, (SelectionsCallback) obj3);
    }
}
