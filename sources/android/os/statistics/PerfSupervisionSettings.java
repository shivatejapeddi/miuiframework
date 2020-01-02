package android.os.statistics;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Process;
import android.text.TextUtils;
import android.util.Slog;
import android.util.TimedRemoteCaller;
import java.io.File;
import miui.os.Build;
import miui.os.Environment;
import miui.os.SystemProperties;

public class PerfSupervisionSettings {
    private static final boolean DEFAULT_AUTO_ANALYSIS_ON_STATE;
    private static final int DEFAULT_DIVISION_RATIO;
    private static final int DEFAULT_GLOBAL_PERF_EVENT_QUEUE_LENGTH;
    private static final int DEFAULT_LEVEL;
    private static final int DEFAULT_SOFT_THRESHOLD_MS;
    public static final int MAX_EVENT_THRESHOLD_MS = 65536;
    public static final int MIN_HARD_THRESHOLD_MS = 500;
    public static final int MIN_PEER_WAIT_TIME_RATIO_POWER = 5;
    public static final int MIN_SOFT_THRESHOLD_MS = 10;
    public static final int PERF_SUPERVISION_OFF = 0;
    public static final int PERF_SUPERVISION_ON_HEAVY = 2;
    public static final int PERF_SUPERVISION_ON_NORMAL = 1;
    public static final int PERF_SUPERVISION_ON_TEST = 9;
    private static final String TAG = PerfSupervisionSettings.class.getSimpleName();
    public static final int sGlobalPerfEventQueueLength = DEFAULT_GLOBAL_PERF_EVENT_QUEUE_LENGTH;
    public static final boolean sIsAutoAnalysisOn;
    private static boolean sIsMiuiDaemon = false;
    private static boolean sIsSystemServer = false;
    public static final int sMinOverlappedDurationMillis = Math.max((sMinPerfEventDurationMillis * 4) / 5, 10);
    public static final int sMinPerfEventDurationMillis;
    public static final int sPerfSupervisionDivisionRatio;
    public static final int sPerfSupervisionHardThreshold;
    private static final int sPerfSupervisionLevel;
    public static final int sPerfSupervisionSoftThreshold;
    private static boolean sReadySupervision = false;

    static {
        boolean z = false;
        boolean hasMiSysInfoFreader = false;
        try {
            hasMiSysInfoFreader = new File("/dev/misysinfofreader").exists();
        } catch (Exception e) {
        }
        int i = (Build.IS_STABLE_VERSION || !hasMiSysInfoFreader) ? 0 : 1;
        DEFAULT_LEVEL = i;
        i = Environment.getCpuCount();
        long memorySizeInMB = (Process.getTotalMemory() / 1024) / 1024;
        if (i > 2 || memorySizeInMB > 1024) {
            if (i <= 4) {
                DEFAULT_SOFT_THRESHOLD_MS = 200;
                DEFAULT_DIVISION_RATIO = 2;
            } else {
                DEFAULT_SOFT_THRESHOLD_MS = 100;
                DEFAULT_DIVISION_RATIO = 1;
            }
            DEFAULT_GLOBAL_PERF_EVENT_QUEUE_LENGTH = Math.max(Math.min((int) ((TimedRemoteCaller.DEFAULT_CALL_TIMEOUT_MILLIS * memorySizeInMB) / 1800), 10000), 2000);
            DEFAULT_AUTO_ANALYSIS_ON_STATE = Build.IS_STABLE_VERSION ^ 1;
        } else {
            DEFAULT_SOFT_THRESHOLD_MS = 400;
            DEFAULT_DIVISION_RATIO = 4;
            DEFAULT_GLOBAL_PERF_EVENT_QUEUE_LENGTH = 2000;
            DEFAULT_AUTO_ANALYSIS_ON_STATE = false;
        }
        String str = "";
        String optionStr = SystemProperties.get("persist.sys.perf_mistats_opt", str);
        if (TextUtils.isEmpty(optionStr)) {
            sPerfSupervisionLevel = DEFAULT_LEVEL;
            sPerfSupervisionSoftThreshold = DEFAULT_SOFT_THRESHOLD_MS;
            sPerfSupervisionDivisionRatio = DEFAULT_DIVISION_RATIO;
            sMinPerfEventDurationMillis = sPerfSupervisionSoftThreshold / sPerfSupervisionDivisionRatio;
            sPerfSupervisionHardThreshold = Math.min(sMinPerfEventDurationMillis << 5, 1000);
        } else {
            String[] options = optionStr.replace(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, str).split(",");
            if (options.length < 1 || TextUtils.isEmpty(options[0])) {
                sPerfSupervisionLevel = DEFAULT_LEVEL;
            } else {
                sPerfSupervisionLevel = parseIntWithDefault(options[0], DEFAULT_LEVEL);
            }
            if (options.length < 2 || TextUtils.isEmpty(options[1])) {
                sPerfSupervisionSoftThreshold = DEFAULT_SOFT_THRESHOLD_MS;
            } else {
                sPerfSupervisionSoftThreshold = Math.max(parseIntWithDefault(options[1], DEFAULT_SOFT_THRESHOLD_MS), 10);
            }
            if (options.length < 3 || TextUtils.isEmpty(options[2])) {
                sPerfSupervisionDivisionRatio = Math.min(DEFAULT_DIVISION_RATIO, sPerfSupervisionSoftThreshold / 10);
            } else {
                sPerfSupervisionDivisionRatio = Math.min(Math.max(parseIntWithDefault(options[2], DEFAULT_DIVISION_RATIO), 1), sPerfSupervisionSoftThreshold / 10);
            }
            sMinPerfEventDurationMillis = sPerfSupervisionSoftThreshold / sPerfSupervisionDivisionRatio;
            int defaultHardThresholdMs = Math.max(Math.min(sMinPerfEventDurationMillis << 5, 1000), 500);
            if (options.length < 4 || TextUtils.isEmpty(options[3])) {
                sPerfSupervisionHardThreshold = defaultHardThresholdMs;
            } else {
                sPerfSupervisionHardThreshold = Math.max(parseIntWithDefault(options[3], defaultHardThresholdMs), 500);
            }
        }
        if (DEFAULT_LEVEL != 0) {
            z = DEFAULT_AUTO_ANALYSIS_ON_STATE;
        }
        sIsAutoAnalysisOn = z;
    }

    static int parseIntWithDefault(String s, int defVal) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return defVal;
        }
    }

    public static void init() {
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(sPerfSupervisionLevel));
        String str2 = ",";
        stringBuilder.append(str2);
        stringBuilder.append(String.valueOf(sPerfSupervisionSoftThreshold));
        stringBuilder.append(str2);
        stringBuilder.append(String.valueOf(sPerfSupervisionDivisionRatio));
        stringBuilder.append(str2);
        stringBuilder.append(String.valueOf(sPerfSupervisionHardThreshold));
        stringBuilder.append(str2);
        stringBuilder.append(String.valueOf(sGlobalPerfEventQueueLength));
        stringBuilder.append(str2);
        stringBuilder.append(String.valueOf(sIsAutoAnalysisOn));
        Slog.d(str, stringBuilder.toString());
    }

    public static int getSupervisionLevel() {
        return sReadySupervision ? sPerfSupervisionLevel : 0;
    }

    public static boolean isSupervisionOn() {
        return getSupervisionLevel() >= 1;
    }

    public static boolean isSystemServer() {
        return sIsSystemServer;
    }

    public static boolean isPerfEventReportable() {
        return !sIsMiuiDaemon && isSupervisionOn();
    }

    public static boolean isInHeavyMode() {
        return sReadySupervision && sPerfSupervisionLevel >= 2;
    }

    public static boolean isInTestMode() {
        return sReadySupervision && sPerfSupervisionLevel >= 9;
    }

    public static boolean isAboveHardThreshold(long timeMillis) {
        return timeMillis >= ((long) sPerfSupervisionHardThreshold);
    }

    public static void notifySupervisionReady(boolean isSystemServer, boolean isMiuiDaemon) {
        sIsSystemServer = isSystemServer;
        sIsMiuiDaemon = isMiuiDaemon;
        sReadySupervision = true;
    }
}
