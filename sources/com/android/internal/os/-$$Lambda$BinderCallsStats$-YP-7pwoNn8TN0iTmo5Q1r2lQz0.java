package com.android.internal.os;

import android.util.Pair;
import java.util.Comparator;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BinderCallsStats$-YP-7pwoNn8TN0iTmo5Q1r2lQz0 implements Comparator {
    public static final /* synthetic */ -$$Lambda$BinderCallsStats$-YP-7pwoNn8TN0iTmo5Q1r2lQz0 INSTANCE = new -$$Lambda$BinderCallsStats$-YP-7pwoNn8TN0iTmo5Q1r2lQz0();

    private /* synthetic */ -$$Lambda$BinderCallsStats$-YP-7pwoNn8TN0iTmo5Q1r2lQz0() {
    }

    public final int compare(Object obj, Object obj2) {
        return Integer.compare(((Integer) ((Pair) obj2).second).intValue(), ((Integer) ((Pair) obj).second).intValue());
    }
}
