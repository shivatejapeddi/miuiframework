package miui.os;

import android.os.Process;

public class MiuiProcessUtil {
    public static long getTotalMemory() {
        return Process.getTotalMemory();
    }

    public static long getFreeMemory() {
        return Process.getFreeMemory();
    }
}
