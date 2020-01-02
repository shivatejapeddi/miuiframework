package com.android.internal.os;

import com.android.internal.os.KernelCpuUidTimeReader.Callback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$BatteryStatsImpl$_l2oiaRDRhjCXI_PwXPsAhrgegI implements Callback {
    private final /* synthetic */ BatteryStatsImpl f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ -$$Lambda$BatteryStatsImpl$_l2oiaRDRhjCXI_PwXPsAhrgegI(BatteryStatsImpl batteryStatsImpl, boolean z) {
        this.f$0 = batteryStatsImpl;
        this.f$1 = z;
    }

    public final void onUidCpuTime(int i, Object obj) {
        this.f$0.lambda$readKernelUidCpuActiveTimesLocked$2$BatteryStatsImpl(this.f$1, i, (Long) obj);
    }
}
