package miui.util;

import android.os.Build;
import android.os.Process;
import android.util.Log;
import com.miui.daemon.performance.PerfShielderManager;
import com.miui.whetstone.WhetstoneActivityManager;
import java.util.HashMap;
import miui.os.FileUtils;
import miui.os.SystemProperties;

public class HardwareInfo {
    private static boolean DEBUG_MEMORY_PERFORMANCE = false;
    private static int DEBUG_MEMORY_PERFORMANCE_MASK = 1;
    private static final long GB = 1073741824;
    private static final long MB = 1048576;
    private static final String TAG = "HardwareInfo";
    private static HashMap<String, Long> sDevice2Memory = new HashMap();
    private static HashMap<String, Long> sDevice2MemoryAdjust = new HashMap();
    private static long sTotalMemory = 0;
    private static long sTotalPhysicalMemory;

    static {
        boolean z = true;
        if (Build.TYPE.equalsIgnoreCase("user") && (SystemProperties.getInt("persist.sys.mem_perf_debug", 0) & DEBUG_MEMORY_PERFORMANCE_MASK) == 0) {
            z = false;
        }
        DEBUG_MEMORY_PERFORMANCE = z;
        sDevice2Memory.put("hwu9200", Long.valueOf(1073741824));
        sDevice2Memory.put("hwu9500", Long.valueOf(1073741824));
        sDevice2Memory.put("maguro", Long.valueOf(1073741824));
        sDevice2Memory.put("ville", Long.valueOf(1073741824));
        sDevice2Memory.put("LT26i", Long.valueOf(1073741824));
        sDevice2Memory.put("ventana", Long.valueOf(1073741824));
        sDevice2Memory.put("stuttgart", Long.valueOf(1073741824));
        sDevice2Memory.put("t03g", Long.valueOf(2147483648L));
        sDevice2Memory.put("pisces", Long.valueOf(2147483648L));
        sDevice2Memory.put("HM2014501", Long.valueOf(1073741824));
        sDevice2Memory.put("leo", Long.valueOf(4294967296L));
        sDevice2Memory.put("HM2014011", Long.valueOf(1073741824));
        sDevice2Memory.put("HM2013022", Long.valueOf(1073741824));
        sDevice2Memory.put("HM2013023", Long.valueOf(1073741824));
        sDevice2MemoryAdjust.put("lcsh92_wet_xm_td", Long.valueOf(-536870912));
        sDevice2MemoryAdjust.put("lcsh92_wet_xm_kk", Long.valueOf(-536870912));
    }

    public static long getTotalMemory() {
        if (sTotalMemory == 0) {
            sTotalMemory = Process.getTotalMemory();
        }
        return sTotalMemory;
    }

    public static long getTotalPhysicalMemory() {
        if (sTotalPhysicalMemory == 0) {
            if (sDevice2Memory.containsKey(miui.os.Build.DEVICE)) {
                sTotalPhysicalMemory = ((Long) sDevice2Memory.get(miui.os.Build.DEVICE)).longValue();
            } else {
                sTotalPhysicalMemory = ((((((getTotalMemory() / 1024) + 102400) / 524288) + 1) * 512) * 1024) * 1024;
                if (sDevice2MemoryAdjust.containsKey(miui.os.Build.BOARD)) {
                    sTotalPhysicalMemory += ((Long) sDevice2MemoryAdjust.get(miui.os.Build.BOARD)).longValue();
                }
            }
        }
        return sTotalPhysicalMemory;
    }

    public static long getFreeMemory() {
        return PerfShielderManager.getFreeMemory().longValue();
    }

    public static long getLowMemoryLimitation() {
        try {
            String line = FileUtils.readFileAsString("/sys/module/lowmemorykiller/parameters/minfree");
            return (long) ((Integer.parseInt(line.trim().substring(line.lastIndexOf(44) + 1)) * 4) * 1024);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static long getAndroidCacheMemory() {
        long cacheMemory = WhetstoneActivityManager.getAndroidCachedEmptyProcessMemory().longValue();
        if (DEBUG_MEMORY_PERFORMANCE) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("get CacheMemory ");
            stringBuilder.append(cacheMemory);
            stringBuilder.append("KB");
            Log.i(TAG, stringBuilder.toString());
        }
        return cacheMemory > 0 ? cacheMemory : 0;
    }
}
