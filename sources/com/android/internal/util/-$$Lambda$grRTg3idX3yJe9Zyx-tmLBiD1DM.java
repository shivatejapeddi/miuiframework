package com.android.internal.util;

import android.content.ComponentName.WithComponentName;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$grRTg3idX3yJe9Zyx-tmLBiD1DM implements Predicate {
    public static final /* synthetic */ -$$Lambda$grRTg3idX3yJe9Zyx-tmLBiD1DM INSTANCE = new -$$Lambda$grRTg3idX3yJe9Zyx-tmLBiD1DM();

    private /* synthetic */ -$$Lambda$grRTg3idX3yJe9Zyx-tmLBiD1DM() {
    }

    public final boolean test(Object obj) {
        return DumpUtils.isPlatformCriticalPackage((WithComponentName) obj);
    }
}
