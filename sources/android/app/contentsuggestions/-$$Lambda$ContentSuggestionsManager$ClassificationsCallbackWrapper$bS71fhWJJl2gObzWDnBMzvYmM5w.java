package android.app.contentsuggestions;

import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ContentSuggestionsManager$ClassificationsCallbackWrapper$bS71fhWJJl2gObzWDnBMzvYmM5w implements Runnable {
    private final /* synthetic */ ClassificationsCallbackWrapper f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ List f$2;

    public /* synthetic */ -$$Lambda$ContentSuggestionsManager$ClassificationsCallbackWrapper$bS71fhWJJl2gObzWDnBMzvYmM5w(ClassificationsCallbackWrapper classificationsCallbackWrapper, int i, List list) {
        this.f$0 = classificationsCallbackWrapper;
        this.f$1 = i;
        this.f$2 = list;
    }

    public final void run() {
        this.f$0.lambda$onContentClassificationsAvailable$0$ContentSuggestionsManager$ClassificationsCallbackWrapper(this.f$1, this.f$2);
    }
}
