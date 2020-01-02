package com.android.internal.os;

import com.android.internal.os.KernelSysAppCpuTimeReader.Callback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$UidAppBatteryStatsImpl$_6qSEH1he1nSVJ97cFOKql74mJ0 implements Callback {
    private final /* synthetic */ UidAppBatteryStatsImpl f$0;
    private final /* synthetic */ BatteryStatsImpl f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ -$$Lambda$UidAppBatteryStatsImpl$_6qSEH1he1nSVJ97cFOKql74mJ0(UidAppBatteryStatsImpl uidAppBatteryStatsImpl, BatteryStatsImpl batteryStatsImpl, boolean z) {
        this.f$0 = uidAppBatteryStatsImpl;
        this.f$1 = batteryStatsImpl;
        this.f$2 = z;
    }

    public final void onPackageCpuTime(String str, long j, long j2) {
        this.f$0.lambda$readKernelSysAppCpuTimesLocked$0$UidAppBatteryStatsImpl(this.f$1, this.f$2, str, j, j2);
    }
}
