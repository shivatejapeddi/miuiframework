package miui.log;

import android.text.TextUtils;
import java.util.HashMap;

public class AppLogSwitches implements Cloneable {
    public final HashMap<String, LogSwitch> logSwitches;
    public final String packageName;
    public final String programName;
    public final boolean targetAllApps;
    public final String uniqueName;

    public AppLogSwitches(boolean targetAllApps, String packageName, String programName, HashMap<String, LogSwitch> logSwitches) {
        this.targetAllApps = targetAllApps;
        this.packageName = packageName;
        this.programName = programName;
        StringBuilder stringBuilder;
        if (this.targetAllApps) {
            this.uniqueName = "all";
        } else if (TextUtils.isEmpty(this.packageName)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("program_");
            stringBuilder.append(this.programName);
            this.uniqueName = stringBuilder.toString();
        } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("package_");
            stringBuilder.append(this.packageName);
            this.uniqueName = stringBuilder.toString();
        }
        this.logSwitches = logSwitches;
    }

    public void merge(AppLogSwitches other) {
        if (other.uniqueName.equals(this.uniqueName)) {
            this.logSwitches.putAll(other.logSwitches);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.targetAllApps) {
            sb.append("all");
        } else if (TextUtils.isEmpty(this.packageName)) {
            sb.append("program ");
            sb.append(this.programName);
        } else {
            sb.append("package ");
            sb.append(this.packageName);
        }
        sb.append(" | ");
        boolean firstLogSwitch = true;
        for (LogSwitch logSwitch : this.logSwitches.values()) {
            if (!firstLogSwitch) {
                sb.append(", ");
            }
            sb.append(logSwitch.toString());
            firstLogSwitch = false;
        }
        return sb.toString();
    }

    public Object clone() {
        AppLogSwitches clonedObject = new AppLogSwitches(this.targetAllApps, this.packageName, this.programName, new HashMap());
        for (LogSwitch logSwitch : this.logSwitches.values()) {
            LogSwitch clonedSwitch = (LogSwitch) logSwitch.clone();
            clonedObject.logSwitches.put(logSwitch.uniqueName, logSwitch);
        }
        return clonedObject;
    }

    public static AppLogSwitches parseAppLogSwitches(String appLogSwitchesStr) {
        String[] appLogSwitchParts = appLogSwitchesStr.trim().split("\\|");
        if (appLogSwitchParts.length != 2) {
            return null;
        }
        HashMap<String, LogSwitch> logSwitches = LogSwitch.parseLogSwitches(appLogSwitchParts[1].trim());
        if (logSwitches == null || logSwitches.size() == 0) {
            return null;
        }
        String[] targetParts = appLogSwitchParts[0].trim().split("\\s+");
        String str = "";
        if (targetParts.length == 1) {
            if (targetParts[0].equalsIgnoreCase("all")) {
                return new AppLogSwitches(true, str, str, logSwitches);
            }
            return null;
        } else if (targetParts.length != 2) {
            return null;
        } else {
            if (targetParts[0].equalsIgnoreCase("package")) {
                return new AppLogSwitches(false, targetParts[1].trim(), str, logSwitches);
            }
            if (targetParts[0].equalsIgnoreCase("program")) {
                return new AppLogSwitches(false, str, targetParts[1].trim(), logSwitches);
            }
            return null;
        }
    }
}
