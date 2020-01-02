package android.media.session;

import android.media.session.MediaSessionManager.Session2TokensChangedWrapper.AnonymousClass1;
import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaSessionManager$Session2TokensChangedWrapper$1$4_TH2zkLY97pxK-e1EPxtPhZwdk implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ List f$1;

    public /* synthetic */ -$$Lambda$MediaSessionManager$Session2TokensChangedWrapper$1$4_TH2zkLY97pxK-e1EPxtPhZwdk(AnonymousClass1 anonymousClass1, List list) {
        this.f$0 = anonymousClass1;
        this.f$1 = list;
    }

    public final void run() {
        this.f$0.lambda$onSession2TokensChanged$0$MediaSessionManager$Session2TokensChangedWrapper$1(this.f$1);
    }
}
