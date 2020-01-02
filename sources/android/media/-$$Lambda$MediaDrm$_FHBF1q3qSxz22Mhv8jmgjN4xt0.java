package android.media;

import android.media.MediaDrm.OnKeyStatusChangeListener;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaDrm$_FHBF1q3qSxz22Mhv8jmgjN4xt0 implements Consumer {
    private final /* synthetic */ MediaDrm f$0;
    private final /* synthetic */ OnKeyStatusChangeListener f$1;

    public /* synthetic */ -$$Lambda$MediaDrm$_FHBF1q3qSxz22Mhv8jmgjN4xt0(MediaDrm mediaDrm, OnKeyStatusChangeListener onKeyStatusChangeListener) {
        this.f$0 = mediaDrm;
        this.f$1 = onKeyStatusChangeListener;
    }

    public final void accept(Object obj) {
        this.f$0.lambda$createOnKeyStatusChangeListener$1$MediaDrm(this.f$1, (ListenerArgs) obj);
    }
}
