package com.android.internal.os;

import android.content.Context;
import android.os.BatteryStats;
import android.util.ArrayMap;
import com.android.internal.os.BatterySipper.DrainType;
import com.android.internal.os.UidAppBatteryStatsImpl.UidPackage;
import java.util.ArrayList;
import java.util.List;

public class BatteryStatsHelperInjector {
    static final boolean DEBUG = false;
    private static final String TAG = "BatteryStatsHelperInjector";

    private BatteryStatsHelperInjector() {
    }

    public static List<BatterySipper> processUidAppUsage(Context context, UidAppBatteryStatsImpl uabsi, BatteryStats bs, int statsType, long ut, long rt) {
        Context context2 = context;
        ArrayList usageList = new ArrayList();
        UidAppWakelockPowerCalculator wakeLockPowerCalculator = new UidAppWakelockPowerCalculator(context2);
        UidAppCpuPowerCalculator cpuPowerCalculator = new UidAppCpuPowerCalculator(context2);
        UidAppMobileRadioPowerCalculator mobileRadioPowerCalculator = new UidAppMobileRadioPowerCalculator(context2, bs);
        UidAppWifiPowerCalculator wifiPowerCalculator = new UidAppWifiPowerCalculator(context2);
        UidAppSensorPowerCalculator uidAppSensorPowerCalculator = new UidAppSensorPowerCalculator(context, bs, rt, statsType);
        ArrayMap<String, ? extends UidPackage> packageStats = uabsi.getUidPackageStats();
        int NU = packageStats.size();
        int iu = 0;
        while (iu < NU) {
            UidPackage sp = (UidPackage) packageStats.valueAt(iu);
            BatterySipper app = new BatterySipper(DrainType.APP, null, 0.0d);
            app.packageWithHighestDrain = sp.getPackageName();
            wakeLockPowerCalculator.calculateUidApp(app, sp, rt, statsType);
            cpuPowerCalculator.calculateUidApp(app, sp, statsType);
            mobileRadioPowerCalculator.calculateUidApp(app, sp, rt, statsType);
            wifiPowerCalculator.calculateUidApp(app, sp, rt, statsType);
            uidAppSensorPowerCalculator.calculateUidApp(app, sp, rt, statsType);
            if (Math.abs(app.sumPower()) >= 1.0E-6d) {
                usageList.add(app);
            }
            iu++;
            BatteryStats batteryStats = bs;
        }
        int i = statsType;
        return usageList;
    }
}
