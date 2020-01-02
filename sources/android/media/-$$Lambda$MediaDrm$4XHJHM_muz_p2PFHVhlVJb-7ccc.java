package android.media;

import android.media.MediaDrm.OnSessionLostStateListener;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaDrm$4XHJHM_muz_p2PFHVhlVJb-7ccc implements Consumer {
    private final /* synthetic */ MediaDrm f$0;
    private final /* synthetic */ OnSessionLostStateListener f$1;

    public /* synthetic */ -$$Lambda$MediaDrm$4XHJHM_muz_p2PFHVhlVJb-7ccc(MediaDrm mediaDrm, OnSessionLostStateListener onSessionLostStateListener) {
        this.f$0 = mediaDrm;
        this.f$1 = onSessionLostStateListener;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$createOnSessionLostStateListener$3$MediaDrm(this.f$1, (ListenerArgs) obj);
    }
}
