package android.media;

import android.media.MediaDrm.OnKeyStatusChangeListener;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaDrm$V4Xmxq2t4qcaWIsuRLRluTj6MT0 implements Function {
    private final /* synthetic */ MediaDrm f$0;

    public /* synthetic */ -$$Lambda$MediaDrm$V4Xmxq2t4qcaWIsuRLRluTj6MT0(MediaDrm mediaDrm) {
        this.f$0 = mediaDrm;
    }

    public final Object apply(Object obj) {
        return this.f$0.createOnKeyStatusChangeListener((OnKeyStatusChangeListener) obj);
    }
}
