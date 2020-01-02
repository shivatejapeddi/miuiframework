package com.android.internal.os;

import com.android.internal.os.BinderCallsStats.ExportedCallStat;
import java.util.Comparator;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BinderCallsStats$233x_Qux4c_AiqShYaWwvFplEXs implements Comparator {
    public static final /* synthetic */ -$$Lambda$BinderCallsStats$233x_Qux4c_AiqShYaWwvFplEXs INSTANCE = new -$$Lambda$BinderCallsStats$233x_Qux4c_AiqShYaWwvFplEXs();

    private /* synthetic */ -$$Lambda$BinderCallsStats$233x_Qux4c_AiqShYaWwvFplEXs() {
    }

    public final int compare(Object obj, Object obj2) {
        return BinderCallsStats.compareByCpuDesc((ExportedCallStat) obj, (ExportedCallStat) obj2);
    }
}
