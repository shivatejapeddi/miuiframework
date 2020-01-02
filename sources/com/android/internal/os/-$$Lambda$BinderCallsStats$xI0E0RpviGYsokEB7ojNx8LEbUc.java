package com.android.internal.os;

import com.android.internal.os.BinderCallsStats.UidEntry;
import java.util.function.ToDoubleFunction;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BinderCallsStats$xI0E0RpviGYsokEB7ojNx8LEbUc implements ToDoubleFunction {
    public static final /* synthetic */ -$$Lambda$BinderCallsStats$xI0E0RpviGYsokEB7ojNx8LEbUc INSTANCE = new -$$Lambda$BinderCallsStats$xI0E0RpviGYsokEB7ojNx8LEbUc();

    private /* synthetic */ -$$Lambda$BinderCallsStats$xI0E0RpviGYsokEB7ojNx8LEbUc() {
    }

    public final double applyAsDouble(Object obj) {
        return ((double) ((UidEntry) obj).cpuTimeMicros);
    }
}
