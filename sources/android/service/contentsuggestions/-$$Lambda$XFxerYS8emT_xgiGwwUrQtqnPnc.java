package android.service.contentsuggestions;

import android.os.Bundle;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$XFxerYS8emT_xgiGwwUrQtqnPnc implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$XFxerYS8emT_xgiGwwUrQtqnPnc INSTANCE = new -$$Lambda$XFxerYS8emT_xgiGwwUrQtqnPnc();

    private /* synthetic */ -$$Lambda$XFxerYS8emT_xgiGwwUrQtqnPnc() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((ContentSuggestionsService) obj).onNotifyInteraction((String) obj2, (Bundle) obj3);
    }
}
