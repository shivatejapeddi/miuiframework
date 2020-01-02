package android.widget;

import android.view.inspector.IntFlagMapping;
import java.util.function.IntFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$gFNlJIKfxqleu304aRWP5R5v1yY implements IntFunction {
    private final /* synthetic */ IntFlagMapping f$0;

    public /* synthetic */ -$$Lambda$gFNlJIKfxqleu304aRWP5R5v1yY(IntFlagMapping intFlagMapping) {
        this.f$0 = intFlagMapping;
    }

    public final Object apply(int i) {
        return this.f$0.get(i);
    }
}
