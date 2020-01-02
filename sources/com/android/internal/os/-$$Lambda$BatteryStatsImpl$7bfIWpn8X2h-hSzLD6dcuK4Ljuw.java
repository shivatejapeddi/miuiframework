package com.android.internal.os;

import android.util.SparseLongArray;
import com.android.internal.os.KernelCpuUidTimeReader.Callback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BatteryStatsImpl$7bfIWpn8X2h-hSzLD6dcuK4Ljuw implements Callback {
    private final /* synthetic */ BatteryStatsImpl f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ SparseLongArray f$3;

    public /* synthetic */ -$$Lambda$BatteryStatsImpl$7bfIWpn8X2h-hSzLD6dcuK4Ljuw(BatteryStatsImpl batteryStatsImpl, int i, boolean z, SparseLongArray sparseLongArray) {
        this.f$0 = batteryStatsImpl;
        this.f$1 = i;
        this.f$2 = z;
        this.f$3 = sparseLongArray;
    }

    public final void onUidCpuTime(int i, Object obj) {
        this.f$0.lambda$readKernelUidCpuTimesLocked$0$BatteryStatsImpl(this.f$1, this.f$2, this.f$3, i, (long[]) obj);
    }
}
