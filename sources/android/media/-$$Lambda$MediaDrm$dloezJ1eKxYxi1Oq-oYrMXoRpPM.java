package android.media;

import android.media.MediaDrm.OnExpirationUpdateListener;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaDrm$dloezJ1eKxYxi1Oq-oYrMXoRpPM implements Function {
    private final /* synthetic */ MediaDrm f$0;

    public /* synthetic */ -$$Lambda$MediaDrm$dloezJ1eKxYxi1Oq-oYrMXoRpPM(MediaDrm mediaDrm) {
        this.f$0 = mediaDrm;
    }

    public final Object apply(Object obj) {
        return this.f$0.createOnExpirationUpdateListener((OnExpirationUpdateListener) obj);
    }
}
