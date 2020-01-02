package android.media;

import android.media.MediaDrm.OnEventListener;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaDrm$IvEWhXQgSYABwC6_1bdnhTJ4V2I implements Function {
    private final /* synthetic */ MediaDrm f$0;

    public /* synthetic */ -$$Lambda$MediaDrm$IvEWhXQgSYABwC6_1bdnhTJ4V2I(MediaDrm mediaDrm) {
        this.f$0 = mediaDrm;
    }

    public final Object apply(Object obj) {
        return this.f$0.createOnEventListener((OnEventListener) obj);
    }
}
