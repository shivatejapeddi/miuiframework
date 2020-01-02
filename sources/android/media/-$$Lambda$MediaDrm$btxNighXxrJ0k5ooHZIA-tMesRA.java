package android.media;

import android.media.MediaDrm.OnExpirationUpdateListener;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaDrm$btxNighXxrJ0k5ooHZIA-tMesRA implements Consumer {
    private final /* synthetic */ MediaDrm f$0;
    private final /* synthetic */ OnExpirationUpdateListener f$1;

    public /* synthetic */ -$$Lambda$MediaDrm$btxNighXxrJ0k5ooHZIA-tMesRA(MediaDrm mediaDrm, OnExpirationUpdateListener onExpirationUpdateListener) {
        this.f$0 = mediaDrm;
        this.f$1 = onExpirationUpdateListener;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$createOnExpirationUpdateListener$2$MediaDrm(this.f$1, (ListenerArgs) obj);
    }
}
