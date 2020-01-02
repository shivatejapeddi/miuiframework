package android.service.contentsuggestions;

import android.app.contentsuggestions.ClassificationsRequest;
import android.app.contentsuggestions.ContentSuggestionsManager.ClassificationsCallback;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$5oRtA6f92le979Nv8-bd2We4x10 implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$5oRtA6f92le979Nv8-bd2We4x10 INSTANCE = new -$$Lambda$5oRtA6f92le979Nv8-bd2We4x10();

    private /* synthetic */ -$$Lambda$5oRtA6f92le979Nv8-bd2We4x10() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((ContentSuggestionsService) obj).onClassifyContentSelections((ClassificationsRequest) obj2, (ClassificationsCallback) obj3);
    }
}
