package miui.log;

import android.content.Intent;
import android.os.SystemProperties;

public final class SystemLogSwitchesConfigManager {
    private static final String logSwitchesFileName = "switches.config";
    private static final String logSwitchesFolder = "/data/system/miuilog/switches";
    private static final LogSwitchesConfigManager logSwitchesManager = new LogSwitchesConfigManager(logSwitchesFolder, logSwitchesFileName);

    public static void enableLogSwitch(boolean createLogSwitchesFileIfNotExisted) {
        logSwitchesManager.startMonitoring(createLogSwitchesFileIfNotExisted, synchronizedReadInitialLogSwitches());
    }

    public static void updatePackageName(String packageName) {
        logSwitchesManager.updatePackageName(packageName);
    }

    public static void updateProgramName(String programName) {
        logSwitchesManager.updateProgramName(programName);
    }

    public static void updateProgramName() {
        logSwitchesManager.updateProgramName();
    }

    public static void updateLogSwitches(Intent intent) {
        logSwitchesManager.updateLogSwitches(intent);
    }

    private static boolean synchronizedReadInitialLogSwitches() {
        return SystemProperties.getBoolean("sys.miui.sync_read_log_switch", false);
    }
}
