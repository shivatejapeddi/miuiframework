package android.media;

import android.media.MediaDrm.OnEventListener;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaDrm$8rRollK1F3eENvuaBGoS8u_-heQ implements Consumer {
    private final /* synthetic */ MediaDrm f$0;
    private final /* synthetic */ OnEventListener f$1;

    public /* synthetic */ -$$Lambda$MediaDrm$8rRollK1F3eENvuaBGoS8u_-heQ(MediaDrm mediaDrm, OnEventListener onEventListener) {
        this.f$0 = mediaDrm;
        this.f$1 = onEventListener;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$createOnEventListener$0$MediaDrm(this.f$1, (ListenerArgs) obj);
    }
}
