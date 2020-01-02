package com.android.internal.os;

import com.android.internal.os.BinderCallsStats.UidEntry;
import java.util.function.ToDoubleFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BinderCallsStats$iPOmTqbqUiHzgsAugINuZgf9tls implements ToDoubleFunction {
    public static final /* synthetic */ -$$Lambda$BinderCallsStats$iPOmTqbqUiHzgsAugINuZgf9tls INSTANCE = new -$$Lambda$BinderCallsStats$iPOmTqbqUiHzgsAugINuZgf9tls();

    private /* synthetic */ -$$Lambda$BinderCallsStats$iPOmTqbqUiHzgsAugINuZgf9tls() {
    }

    public final double applyAsDouble(Object obj) {
        return ((double) ((UidEntry) obj).cpuTimeMicros);
    }
}
