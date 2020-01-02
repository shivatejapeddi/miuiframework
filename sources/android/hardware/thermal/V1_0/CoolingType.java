package android.hardware.thermal.V1_0;

import java.util.ArrayList;

public final class CoolingType {
    public static final int FAN_RPM = 0;

    public static final String toString(int o) {
        if (o == 0) {
            return "FAN_RPM";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("0x");
        stringBuilder.append(Integer.toHexString(o));
        return stringBuilder.toString();
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList();
        list.add("FAN_RPM");
        if (o != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("0x");
            stringBuilder.append(Integer.toHexString((~null) & o));
            list.add(stringBuilder.toString());
        }
        return String.join(" | ", list);
    }
}
