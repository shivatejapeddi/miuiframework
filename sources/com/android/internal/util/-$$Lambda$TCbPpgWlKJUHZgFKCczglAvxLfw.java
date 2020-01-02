package com.android.internal.util;

import android.content.ComponentName.WithComponentName;
import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TCbPpgWlKJUHZgFKCczglAvxLfw implements Predicate {
    public static final /* synthetic */ -$$Lambda$TCbPpgWlKJUHZgFKCczglAvxLfw INSTANCE = new -$$Lambda$TCbPpgWlKJUHZgFKCczglAvxLfw();

    private /* synthetic */ -$$Lambda$TCbPpgWlKJUHZgFKCczglAvxLfw() {
    }

    public final boolean test(Object obj) {
        return DumpUtils.isPlatformNonCriticalPackage((WithComponentName) obj);
    }
}
