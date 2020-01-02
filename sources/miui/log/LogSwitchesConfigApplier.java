package miui.log;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public final class LogSwitchesConfigApplier {
    private final HashMap<String, LogSwitch> logSwitchesConfigOfCurrentApp = new HashMap();
    private String packageName;
    private String programName;

    public LogSwitchesConfigApplier() {
        String str = "";
        this.packageName = str;
        this.programName = str;
    }

    public void updatePackageName(String packageName) {
        this.packageName = packageName;
    }

    public void updateProgramName(String programName) {
        this.programName = programName;
    }

    public synchronized HashMap<String, AppLogSwitches> apply(String configFilePath) {
        HashMap logSwitchesConfig;
        logSwitchesConfig = LogSwitchesConfigParser.parseLogSwitchesConfig(configFilePath);
        if (logSwitchesConfig != null) {
            if (logSwitchesConfig.size() != 0) {
                apply(logSwitchesConfig);
            }
        }
        clearAllOnLogs();
        return logSwitchesConfig;
    }

    public synchronized void apply(HashMap<String, AppLogSwitches> logSwitchesConfig) {
        HashMap<String, LogSwitch> appLogSwitches = new HashMap();
        AppLogSwitches targetedAllAppsConfig = null;
        for (AppLogSwitches appConfig : logSwitchesConfig.values()) {
            if (appConfig.targetAllApps) {
                targetedAllAppsConfig = appConfig;
                break;
            }
        }
        if (targetedAllAppsConfig != null) {
            appLogSwitches.putAll(targetedAllAppsConfig.logSwitches);
        }
        for (AppLogSwitches appConfig2 : logSwitchesConfig.values()) {
            if (!appConfig2.targetAllApps) {
                if (checkTargetApp(appConfig2)) {
                    appLogSwitches.putAll(appConfig2.logSwitches);
                }
            }
        }
        if (appLogSwitches.size() == 0) {
            clearAllOnLogs();
        } else {
            applyLogSwitchesOfCurrentApp(appLogSwitches);
        }
    }

    private synchronized void clearAllOnLogs() {
        for (LogSwitch logSwitch : this.logSwitchesConfigOfCurrentApp.values()) {
            revertLogSwitch(logSwitch);
        }
        this.logSwitchesConfigOfCurrentApp.clear();
    }

    private boolean checkTargetApp(AppLogSwitches appConfig) {
        if (appConfig.targetAllApps) {
            return true;
        }
        if (!TextUtils.isEmpty(appConfig.packageName) && appConfig.packageName.equals(this.packageName)) {
            return true;
        }
        if (TextUtils.isEmpty(appConfig.programName) || !appConfig.programName.equals(this.programName)) {
            return false;
        }
        return true;
    }

    private void applyLogSwitchesOfCurrentApp(HashMap<String, LogSwitch> newLogSwitchesConfigOfCurrentApp) {
        LogSwitch existing;
        HashMap<String, LogSwitch> result = new HashMap();
        for (LogSwitch logSwitch : newLogSwitchesConfigOfCurrentApp.values()) {
            existing = (LogSwitch) this.logSwitchesConfigOfCurrentApp.get(logSwitch.uniqueName);
            if (existing == null) {
                applyLogSwitch(logSwitch);
                this.logSwitchesConfigOfCurrentApp.put(logSwitch.uniqueName, logSwitch);
            } else if (existing.isOn != logSwitch.isOn) {
                revertLogSwitch(existing);
                applyLogSwitch(logSwitch);
                this.logSwitchesConfigOfCurrentApp.remove(logSwitch.uniqueName);
                this.logSwitchesConfigOfCurrentApp.put(logSwitch.uniqueName, logSwitch);
            }
        }
        ArrayList<LogSwitch> toBeReverted = new ArrayList();
        for (LogSwitch existing2 : this.logSwitchesConfigOfCurrentApp.values()) {
            if (!newLogSwitchesConfigOfCurrentApp.containsKey(existing2.uniqueName)) {
                toBeReverted.add(existing2);
            }
        }
        Iterator it = toBeReverted.iterator();
        while (it.hasNext()) {
            existing2 = (LogSwitch) it.next();
            revertLogSwitch(existing2);
            this.logSwitchesConfigOfCurrentApp.remove(existing2.uniqueName);
        }
    }

    private void applyLogSwitch(LogSwitch logSwitch) {
        if (logSwitch.isOn) {
            switchOn(logSwitch);
        } else {
            switchOff(logSwitch);
        }
    }

    private void revertLogSwitch(LogSwitch logSwitch) {
        if (logSwitch.isOn) {
            switchOff(logSwitch);
        } else {
            switchOn(logSwitch);
        }
    }

    private void switchOn(LogSwitch logSwitch) {
        if (TextUtils.isEmpty(logSwitch.tagGroupName)) {
            if (!TextUtils.isEmpty(logSwitch.tagName)) {
                if (Tags.getMiuiTag(logSwitch.tagName) != null) {
                    Tags.switchOnMiuiTag(logSwitch.tagName);
                } else if (Tags.getAndroidTag(logSwitch.tagName) != null) {
                    Tags.switchOnAndroidTag(logSwitch.tagName);
                }
            }
        } else if (Tags.getTagGroup(logSwitch.tagGroupName) != null) {
            Tags.switchOnTagGroup(logSwitch.tagGroupName);
        }
    }

    private void switchOff(LogSwitch logSwitch) {
        if (TextUtils.isEmpty(logSwitch.tagGroupName)) {
            if (!TextUtils.isEmpty(logSwitch.tagName)) {
                if (Tags.getMiuiTag(logSwitch.tagName) != null) {
                    Tags.switchOffMiuiTag(logSwitch.tagName);
                } else if (Tags.getAndroidTag(logSwitch.tagName) != null) {
                    Tags.switchOffAndroidTag(logSwitch.tagName);
                }
            }
        } else if (Tags.getTagGroup(logSwitch.tagGroupName) != null) {
            Tags.switchOffTagGroup(logSwitch.tagGroupName);
        }
    }
}
