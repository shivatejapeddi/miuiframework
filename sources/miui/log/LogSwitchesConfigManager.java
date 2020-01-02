package miui.log;

import android.content.Intent;
import android.text.TextUtils;
import com.android.internal.util.ArrayUtils;
import java.util.HashMap;

public final class LogSwitchesConfigManager {
    public static final String ACTION_REVERT_MIUILOG_SWITCHES = "miui.intent.action.REVERT_MIUILOG_SWITCHES";
    public static final String ACTION_SWITCH_OFF_MIUILOGS = "miui.intent.action.SWITCH_OFF_MIUILOGS";
    public static final String ACTION_SWITCH_ON_MIUILOGS = "miui.intent.action.SWITCH_ON_MIUILOGS";
    public static final String EXTRA_KEY_PACKAGES = "packages";
    public static final String EXTRA_KEY_PROGRAMS = "programs";
    public static final String EXTRA_KEY_TAGGROUPS = "groups";
    public static final String EXTRA_KEY_TAGS = "tags";
    public static final String EXTRA_KEY_TARGETALL = "allapps";
    public static final String TAG = "LogSwitchesConfigManager";
    private final String logSwitchesFileName;
    private final String logSwitchesFilePath;
    private final String logSwitchesFolder;
    private final LogSwitchesConfigMonitor logSwitchesMonitor;
    private final LogSwitchesConfigWriter logSwitchesWriter;

    public LogSwitchesConfigManager(String logSwitchesFolder, String logSwitchesFileName) {
        this.logSwitchesFolder = logSwitchesFolder;
        this.logSwitchesFileName = logSwitchesFileName;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(logSwitchesFolder);
        stringBuilder.append("/");
        stringBuilder.append(logSwitchesFileName);
        this.logSwitchesFilePath = stringBuilder.toString();
        this.logSwitchesMonitor = new LogSwitchesConfigMonitor(logSwitchesFolder, logSwitchesFileName);
        this.logSwitchesWriter = new LogSwitchesConfigWriter(logSwitchesFolder, logSwitchesFileName);
    }

    public synchronized void startMonitoring(boolean createLogSwitchesFileIfNotExisted, boolean synchronizedReadInitialLogSwitches) {
        if (!this.logSwitchesMonitor.isWatching()) {
            this.logSwitchesMonitor.startMonitoring(createLogSwitchesFileIfNotExisted, synchronizedReadInitialLogSwitches);
        }
    }

    public synchronized void updatePackageName(String packageName) {
        this.logSwitchesMonitor.updatePackageName(packageName);
    }

    public synchronized void updateProgramName(String programName) {
        this.logSwitchesMonitor.updateProgramName(programName);
    }

    public synchronized void updateProgramName() {
        this.logSwitchesMonitor.updateProgramName();
    }

    public void updateLogSwitches(Intent intent) {
        if (intent != null) {
            if (ACTION_REVERT_MIUILOG_SWITCHES.equals(intent.getAction())) {
                revertLogSwitches(intent);
            } else {
                if (ACTION_SWITCH_ON_MIUILOGS.equals(intent.getAction())) {
                    switchStatusOfLogSwitches(intent, true);
                } else {
                    if (ACTION_SWITCH_OFF_MIUILOGS.equals(intent.getAction())) {
                        switchStatusOfLogSwitches(intent, false);
                    }
                }
            }
        }
    }

    private void revertLogSwitches(Intent intent) {
        boolean targetAll = intent.getBooleanExtra(EXTRA_KEY_TARGETALL, false);
        String packagesStr = intent.getStringExtra(EXTRA_KEY_PACKAGES);
        String programsStr = intent.getStringExtra(EXTRA_KEY_PROGRAMS);
        String str = ",";
        Object[] packages = packagesStr == null ? new String[0] : packagesStr.split(str);
        Object[] programs = programsStr == null ? new String[0] : programsStr.split(str);
        if (targetAll) {
            this.logSwitchesWriter.write(new HashMap());
        } else if (packages.length > 0 || programs.length > 0) {
            HashMap<String, AppLogSwitches> currentConfig = this.logSwitchesMonitor.retrieveCurrentLogSwitches();
            HashMap<String, AppLogSwitches> newConfig = new HashMap();
            for (AppLogSwitches appConfig : currentConfig.values()) {
                if (TextUtils.isEmpty(appConfig.packageName)) {
                    if (!(TextUtils.isEmpty(appConfig.programName) || ArrayUtils.contains(programs, appConfig.programName))) {
                        newConfig.put(appConfig.uniqueName, (AppLogSwitches) appConfig.clone());
                    }
                } else if (!ArrayUtils.contains(packages, appConfig.packageName)) {
                    newConfig.put(appConfig.uniqueName, (AppLogSwitches) appConfig.clone());
                }
            }
            this.logSwitchesWriter.write(newConfig);
        }
    }

    private void switchStatusOfLogSwitches(Intent intent, boolean isOn) {
        Intent intent2 = intent;
        boolean targetAll = intent2.getBooleanExtra(EXTRA_KEY_TARGETALL, false);
        String packagesStr = intent2.getStringExtra(EXTRA_KEY_PACKAGES);
        String programsStr = intent2.getStringExtra(EXTRA_KEY_PROGRAMS);
        String tagsStr = intent2.getStringExtra("tags");
        String groupsStr = intent2.getStringExtra(EXTRA_KEY_TAGGROUPS);
        String str = ",";
        String[] packages = packagesStr == null ? new String[0] : packagesStr.split(str);
        String[] programs = programsStr == null ? new String[0] : programsStr.split(str);
        String[] tags = tagsStr == null ? new String[0] : tagsStr.split(str);
        String[] groups = groupsStr == null ? new String[0] : groupsStr.split(str);
        if (tags.length != 0 || groups.length != 0) {
            HashMap<String, AppLogSwitches> newAppLogSwitches;
            HashMap currentAppLogSwitches = this.logSwitchesMonitor.retrieveCurrentLogSwitches();
            HashMap hashMap;
            String[] strArr;
            if (targetAll) {
                String str2 = "";
                newAppLogSwitches = merge(currentAppLogSwitches, new AppLogSwitches(true, str2, str2, buildLogSwitches(tags, groups, isOn)));
                hashMap = currentAppLogSwitches;
                String[] strArr2 = groups;
                strArr = tags;
                String newAppLogSwitches2 = groupsStr;
                String currentAppLogSwitches2 = tagsStr;
                String str3 = programsStr;
            } else {
                strArr = tags;
                String[] tags2 = tags;
                HashMap<String, AppLogSwitches> appLevelUpdates = new HashMap();
                hashMap = currentAppLogSwitches;
                appLevelUpdates.putAll(buildAppLogSwitches(true, packages, strArr, groups, isOn));
                HashMap newAppLogSwitches3 = appLevelUpdates;
                newAppLogSwitches3.putAll(buildAppLogSwitches(false, programs, tags2, groups, isOn));
                newAppLogSwitches = merge(hashMap, newAppLogSwitches3);
            }
            this.logSwitchesWriter.write(newAppLogSwitches);
        }
    }

    private HashMap<String, AppLogSwitches> merge(HashMap<String, AppLogSwitches> current, AppLogSwitches globalUpdates) {
        HashMap<String, AppLogSwitches> newConfig = new HashMap();
        for (AppLogSwitches currentAppConfig : current.values()) {
            AppLogSwitches newAppConfig = (AppLogSwitches) currentAppConfig.clone();
            update(newAppConfig, globalUpdates);
            if (newAppConfig.logSwitches.size() > 0) {
                newConfig.put(newAppConfig.uniqueName, newAppConfig);
            }
        }
        if (!newConfig.containsKey(globalUpdates.uniqueName)) {
            newConfig.put(globalUpdates.uniqueName, globalUpdates);
        }
        return newConfig;
    }

    private HashMap<String, AppLogSwitches> merge(HashMap<String, AppLogSwitches> current, HashMap<String, AppLogSwitches> appLevelUpdates) {
        HashMap<String, AppLogSwitches> newConfig = new HashMap();
        for (AppLogSwitches currentAppLogSwitches : current.values()) {
            AppLogSwitches appUpdates = (AppLogSwitches) appLevelUpdates.get(currentAppLogSwitches.uniqueName);
            AppLogSwitches newAppLogSwitches = (AppLogSwitches) currentAppLogSwitches.clone();
            if (appUpdates == null) {
                newConfig.put(newAppLogSwitches.uniqueName, newAppLogSwitches);
            } else {
                update(newAppLogSwitches, appUpdates);
                if (newAppLogSwitches.logSwitches.size() > 0) {
                    newConfig.put(newAppLogSwitches.uniqueName, newAppLogSwitches);
                }
            }
        }
        for (AppLogSwitches currentAppLogSwitches2 : appLevelUpdates.values()) {
            if (!current.containsKey(currentAppLogSwitches2.uniqueName)) {
                newConfig.put(currentAppLogSwitches2.uniqueName, (AppLogSwitches) currentAppLogSwitches2.clone());
            }
        }
        return newConfig;
    }

    private void update(AppLogSwitches current, AppLogSwitches updates) {
        for (LogSwitch updateSwitch : updates.logSwitches.values()) {
            LogSwitch currentSwitch = (LogSwitch) current.logSwitches.get(updateSwitch.uniqueName);
            if (currentSwitch == null) {
                current.logSwitches.put(updateSwitch.uniqueName, updateSwitch);
            } else if (updateSwitch.isOn != currentSwitch.isOn) {
                current.logSwitches.remove(updateSwitch.uniqueName);
            }
        }
    }

    private HashMap<String, AppLogSwitches> buildAppLogSwitches(boolean isPackage, String[] appNames, String[] tags, String[] groups, boolean isOn) {
        HashMap<String, AppLogSwitches> result = new HashMap();
        for (String appName : appNames) {
            String str = "";
            String str2 = isPackage ? appName : str;
            if (!isPackage) {
                str = appName;
            }
            AppLogSwitches appConfig = new AppLogSwitches(false, str2, str, buildLogSwitches(tags, groups, isOn));
            result.put(appConfig.uniqueName, appConfig);
        }
        return result;
    }

    private HashMap<String, LogSwitch> buildLogSwitches(String[] tags, String[] groups, boolean isOn) {
        String str;
        HashMap<String, LogSwitch> logSwitches = new HashMap();
        int length = tags.length;
        int i = 0;
        int i2 = 0;
        while (true) {
            str = "";
            if (i2 >= length) {
                break;
            }
            LogSwitch logSwitch = new LogSwitch(tags[i2], str, isOn);
            logSwitches.put(logSwitch.uniqueName, logSwitch);
            i2++;
        }
        length = groups.length;
        while (i < length) {
            LogSwitch logSwitch2 = new LogSwitch(str, groups[i], isOn);
            logSwitches.put(logSwitch2.uniqueName, logSwitch2);
            i++;
        }
        return logSwitches;
    }
}
