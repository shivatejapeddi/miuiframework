package com.android.internal.os;

public class BatteryStatsImplInjector {
    public static String getProcessName(String NameAndReason) {
        if (NameAndReason == null) {
            return NameAndReason;
        }
        int pos = NameAndReason.indexOf("#for");
        if (pos == -1) {
            return NameAndReason;
        }
        return NameAndReason.substring(0, pos).trim();
    }
}
