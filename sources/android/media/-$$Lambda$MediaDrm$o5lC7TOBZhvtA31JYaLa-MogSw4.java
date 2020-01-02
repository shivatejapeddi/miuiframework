package android.media;

import android.media.MediaDrm.OnSessionLostStateListener;
import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaDrm$o5lC7TOBZhvtA31JYaLa-MogSw4 implements Function {
    private final /* synthetic */ MediaDrm f$0;

    public /* synthetic */ -$$Lambda$MediaDrm$o5lC7TOBZhvtA31JYaLa-MogSw4(MediaDrm mediaDrm) {
        this.f$0 = mediaDrm;
    }

    public final Object apply(Object obj) {
        return this.f$0.createOnSessionLostStateListener((OnSessionLostStateListener) obj);
    }
}
