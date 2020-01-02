package android.app.contentsuggestions;

import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ContentSuggestionsManager$SelectionsCallbackWrapper$1Py0lukljawDYbfwobeRIUDvpNM implements Runnable {
    private final /* synthetic */ SelectionsCallbackWrapper f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ List f$2;

    public /* synthetic */ -$$Lambda$ContentSuggestionsManager$SelectionsCallbackWrapper$1Py0lukljawDYbfwobeRIUDvpNM(SelectionsCallbackWrapper selectionsCallbackWrapper, int i, List list) {
        this.f$0 = selectionsCallbackWrapper;
        this.f$1 = i;
        this.f$2 = list;
    }

    public final void run() {
        this.f$0.lambda$onContentSelectionsAvailable$0$ContentSuggestionsManager$SelectionsCallbackWrapper(this.f$1, this.f$2);
    }
}
