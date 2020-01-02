package miui.log;

import android.net.wifi.WifiEnterpriseConfig;
import android.text.TextUtils;
import java.util.HashMap;

public class LogSwitch implements Cloneable {
    public final boolean isOn;
    public final String tagGroupName;
    public final String tagName;
    public final String uniqueName;

    public LogSwitch(String tagName, String tagGroupName, boolean isOn) {
        this.tagName = tagName;
        this.tagGroupName = tagGroupName;
        this.isOn = isOn;
        StringBuilder stringBuilder;
        if (TextUtils.isEmpty(this.tagName)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("group_");
            stringBuilder.append(this.tagGroupName);
            this.uniqueName = stringBuilder.toString();
            return;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("tag_");
        stringBuilder.append(this.tagName);
        this.uniqueName = stringBuilder.toString();
    }

    public String toString() {
        String statusStr = this.isOn ? "on" : "off";
        boolean isEmpty = TextUtils.isEmpty(this.tagName);
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        StringBuilder stringBuilder;
        if (isEmpty) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("group ");
            stringBuilder.append(this.tagGroupName);
            stringBuilder.append(str);
            stringBuilder.append(statusStr);
            return stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("tag ");
        stringBuilder.append(this.tagName);
        stringBuilder.append(str);
        stringBuilder.append(statusStr);
        return stringBuilder.toString();
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static LogSwitch parseLogSwitch(String logSwitchStr) {
        logSwitchStr = logSwitchStr.trim();
        if (logSwitchStr.length() < 4) {
            return null;
        }
        String[] switchParts = logSwitchStr.split("\\s+");
        if (switchParts.length != 3) {
            return null;
        }
        String str = "on";
        String str2 = "";
        if (switchParts[0].equalsIgnoreCase("Tag")) {
            return new LogSwitch(switchParts[1].trim(), str2, switchParts[2].trim().equalsIgnoreCase(str));
        }
        if (switchParts[0].equalsIgnoreCase("Group")) {
            return new LogSwitch(str2, switchParts[1].trim(), switchParts[2].trim().equalsIgnoreCase(str));
        }
        return null;
    }

    public static HashMap<String, LogSwitch> parseLogSwitches(String logSwitchesStr) {
        String[] logSwitchStrs = logSwitchesStr.split(",");
        HashMap<String, LogSwitch> logSwitches = new HashMap();
        for (String logSwitchStr : logSwitchStrs) {
            LogSwitch logSwitch = parseLogSwitch(logSwitchStr);
            if (logSwitch != null) {
                logSwitches.put(logSwitch.uniqueName, logSwitch);
            }
        }
        return logSwitches;
    }
}
