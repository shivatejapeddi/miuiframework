package com.android.internal.os;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BatteryStatsInjector {
    private BatteryStatsInjector() {
    }

    public static void dumpSysApp(PrintWriter pw, BatterySipper bs, List<BatterySipper> sipperList, String prefix) {
        if (bs.uidObj != null && bs.uidObj.getUid() == 1000) {
            Collections.sort(sipperList, new Comparator<BatterySipper>() {
                public int compare(BatterySipper o1, BatterySipper o2) {
                    return Double.compare(o2.sumPower(), o1.sumPower());
                }
            });
            for (int si = 0; si < sipperList.size(); si++) {
                BatterySipper sabs = (BatterySipper) sipperList.get(si);
                if (sabs.sumPower() != 0.0d) {
                    pw.print(prefix);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("        App ");
                    stringBuilder.append(sabs.packageWithHighestDrain);
                    pw.print(stringBuilder.toString());
                    pw.print(": ");
                    printmAh(pw, sabs.sumPower());
                    pw.print(" (");
                    if (sabs.cpuPowerMah != 0.0d) {
                        pw.print(" cpu=");
                        printmAh(pw, sabs.cpuPowerMah);
                    }
                    if (sabs.wakeLockPowerMah != 0.0d) {
                        pw.print(" wake=");
                        printmAh(pw, sabs.wakeLockPowerMah);
                    }
                    if (sabs.mobileRadioPowerMah != 0.0d) {
                        pw.print(" radio=");
                        printmAh(pw, sabs.mobileRadioPowerMah);
                    }
                    if (sabs.wifiPowerMah != 0.0d) {
                        pw.print(" wifi=");
                        printmAh(pw, sabs.wifiPowerMah);
                    }
                    if (sabs.bluetoothPowerMah != 0.0d) {
                        pw.print(" bt=");
                        printmAh(pw, sabs.bluetoothPowerMah);
                    }
                    if (sabs.gpsPowerMah != 0.0d) {
                        pw.print(" gps=");
                        printmAh(pw, sabs.gpsPowerMah);
                    }
                    if (sabs.sensorPowerMah != 0.0d) {
                        pw.print(" sensor=");
                        printmAh(pw, sabs.sensorPowerMah);
                    }
                    if (sabs.cameraPowerMah != 0.0d) {
                        pw.print(" camera=");
                        printmAh(pw, sabs.cameraPowerMah);
                    }
                    pw.print(" )");
                    pw.println();
                }
            }
        }
    }

    private static void printmAh(PrintWriter printer, double power) {
        printer.print(BatteryStatsHelper.makemAh(power));
    }
}
