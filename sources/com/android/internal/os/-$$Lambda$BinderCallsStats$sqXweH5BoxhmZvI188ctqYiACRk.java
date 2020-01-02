package com.android.internal.os;

import com.android.internal.os.BinderCallsStats.ExportedCallStat;
import java.util.Comparator;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BinderCallsStats$sqXweH5BoxhmZvI188ctqYiACRk implements Comparator {
    public static final /* synthetic */ -$$Lambda$BinderCallsStats$sqXweH5BoxhmZvI188ctqYiACRk INSTANCE = new -$$Lambda$BinderCallsStats$sqXweH5BoxhmZvI188ctqYiACRk();

    private /* synthetic */ -$$Lambda$BinderCallsStats$sqXweH5BoxhmZvI188ctqYiACRk() {
    }

    public final int compare(Object obj, Object obj2) {
        return BinderCallsStats.compareByBinderClassAndCode((ExportedCallStat) obj, (ExportedCallStat) obj2);
    }
}
