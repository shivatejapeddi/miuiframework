package miui.slide;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import android.provider.ContactsContract;
import android.provider.MiuiSettings.SettingsCloudData;
import android.provider.MiuiSettings.SettingsCloudData.CloudData;
import android.provider.MiuiSettings.XSpace;
import android.service.notification.Condition;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Slog;
import com.android.internal.content.PackageMonitor;
import com.android.internal.os.BackgroundThread;
import com.android.internal.util.FastPrintWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miui.log.LogSwitchesConfigManager;
import miui.process.IActivityChangeListener;
import miui.process.ProcessManager;
import miui.slide.SlideConfig.TouchEventConfig;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SlideCloudConfigHelper {
    private static final String CloudConfigFilePath = "/data/system/mirihi-config-cloud.json";
    private static final String ConfigFilePath = "/system/etc/mirihi-config.json";
    private static final String TAG = "SlideCloudConfigHelper";
    private static volatile SlideCloudConfigHelper sCloudConfigHelper = null;
    private IActivityChangeListener mActivityChangeListener;
    private HashMap<String, AppSlideConfig> mAppSlideConfigs = new HashMap();
    private PackageManager mPackageManager;
    PackageMonitor mPackageMonitor = new PackageMonitor() {
        public void onPackageAdded(String packageName, int uid) {
            if (SlideCloudConfigHelper.this.mSlide3rdPackageList.contains(packageName)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onPackageAdded ");
                stringBuilder.append(packageName);
                String stringBuilder2 = stringBuilder.toString();
                String str = SlideCloudConfigHelper.TAG;
                Slog.d(str, stringBuilder2);
                try {
                    PackageInfo packageInfo = SlideCloudConfigHelper.this.mPackageManager.getPackageInfo(packageName, 0);
                    if (packageInfo != null) {
                        SlideCloudConfigHelper.this.parseSlideConfigFile(false, packageName, packageInfo.versionCode);
                    }
                } catch (NameNotFoundException e) {
                    Slog.w(str, e);
                }
                SlideCloudConfigHelper.this.registerSlideActivityChangeListener();
            }
            super.onPackageAdded(packageName, uid);
        }

        public void onPackageRemoved(String packageName, int uid) {
            if (SlideCloudConfigHelper.this.mSlide3rdPackageList.contains(packageName)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onPackageRemoved ");
                stringBuilder.append(packageName);
                Slog.d(SlideCloudConfigHelper.TAG, stringBuilder.toString());
                SlideCloudConfigHelper.this.mAppSlideConfigs.remove(packageName);
            }
            super.onPackageRemoved(packageName, uid);
        }

        public void onPackageUpdateFinished(String packageName, int uid) {
            if (SlideCloudConfigHelper.this.mSlide3rdPackageList.contains(packageName)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("onPackageUpdateFinished ");
                stringBuilder.append(packageName);
                String stringBuilder2 = stringBuilder.toString();
                String str = SlideCloudConfigHelper.TAG;
                Slog.d(str, stringBuilder2);
                try {
                    PackageInfo packageInfo = SlideCloudConfigHelper.this.mPackageManager.getPackageInfo(packageName, 0);
                    if (packageInfo != null) {
                        SlideCloudConfigHelper.this.parseSlideConfigFile(false, packageName, packageInfo.versionCode);
                    }
                } catch (NameNotFoundException e) {
                    Slog.w(str, e);
                }
            }
            super.onPackageUpdateFinished(packageName, uid);
        }
    };
    private ArrayList<String> mSlide3rdPackageList = new ArrayList();
    private ArrayList<String> mSlideSystemPackageList = new ArrayList();

    public static SlideCloudConfigHelper getInstance() {
        if (sCloudConfigHelper == null) {
            synchronized (SlideCloudConfigHelper.class) {
                if (sCloudConfigHelper == null) {
                    sCloudConfigHelper = new SlideCloudConfigHelper();
                }
            }
        }
        return sCloudConfigHelper;
    }

    private SlideCloudConfigHelper() {
        this.mSlideSystemPackageList.add("com.android.camera");
        this.mSlideSystemPackageList.add(TelephonyManager.PHONE_PROCESS_NAME);
        this.mSlideSystemPackageList.add("com.miui.home");
        this.mSlideSystemPackageList.add("com.android.incallui");
        this.mSlideSystemPackageList.add(ContactsContract.AUTHORITY);
        this.mSlideSystemPackageList.add("com.example.xcm.test");
    }

    public void initConfig(Context context) {
        this.mPackageManager = context.getPackageManager();
        registerDataObserver(context);
        parseSlideConfigFile(true, null, 0);
        initPackageMonitor(context);
    }

    public void setActivityChangeListener(IActivityChangeListener listener) {
        this.mActivityChangeListener = listener;
        registerSlideActivityChangeListener();
    }

    private void registerDataObserver(final Context context) {
        context.getContentResolver().registerContentObserver(SettingsCloudData.getCloudDataNotifyUri(), true, new ContentObserver(BackgroundThread.getHandler()) {
            public void onChange(boolean selfChange) {
                Slog.w(SlideCloudConfigHelper.TAG, "SlideCloudConfigInfo onChange");
                SlideCloudConfigHelper.this.updateAppSlideCloudConfigList(context);
                SlideCloudConfigHelper.this.registerSlideActivityChangeListener();
            }
        });
    }

    private void updateAppSlideCloudConfigList(Context context) {
        String str = TAG;
        List<CloudData> cloudDatas = SettingsCloudData.getCloudDataList(context.getContentResolver(), "SlideCloudConfig");
        if (cloudDatas != null) {
            JSONArray cloudDataArray = new JSONArray();
            for (CloudData cloudData : cloudDatas) {
                JSONObject jsonObject = cloudData.json();
                if (generateAppSlideConfigFromJson(jsonObject) != null) {
                    cloudDataArray.put(jsonObject);
                }
            }
            if (cloudDataArray.length() != 0) {
                JSONObject jsonObject2 = new JSONObject();
                try {
                    jsonObject2.put(LogSwitchesConfigManager.EXTRA_KEY_PACKAGES, cloudDataArray);
                } catch (JSONException e) {
                    Slog.w(str, e);
                }
                String fileContent = jsonObject2.toString();
                ParcelFileDescriptor fd = null;
                try {
                    fd = ParcelFileDescriptor.open(new File(CloudConfigFilePath), 1006632960);
                    PrintWriter pw = new FastPrintWriter(new FileOutputStream(fd.getFileDescriptor()));
                    pw.print(fileContent);
                    pw.flush();
                } catch (Exception e2) {
                    Slog.w(str, e2);
                } catch (Throwable th) {
                    SlideUtils.closeQuietly(fd);
                }
                SlideUtils.closeQuietly(fd);
            } else {
                return;
            }
        }
        parseSlideConfigFile(true, null, 0);
    }

    private AppSlideConfig generateAppSlideConfigFromJson(JSONObject jsonObject) {
        Throwable e;
        Object obj;
        JSONObject jSONObject = jsonObject;
        String str = TAG;
        if (jSONObject == null) {
            return null;
        }
        try {
            String packageName = jSONObject.getString("pkg");
            JSONArray actionConfigs = jSONObject.getJSONArray("actionConfigs");
            String str2;
            if (actionConfigs == null || actionConfigs.length() <= 0) {
                str2 = packageName;
                return null;
            }
            AppSlideConfig appConfig = new AppSlideConfig(packageName);
            int i = 0;
            int index = 0;
            while (index < actionConfigs.length()) {
                JSONObject actionConfig = actionConfigs.getJSONObject(index);
                int keyCode = actionConfig.getInt("keyCode");
                String version = actionConfig.getString("version");
                String startingActivity = actionConfig.getString("startActivity");
                int flagAction = actionConfig.getInt("flagAction");
                int flagResult = actionConfig.getInt("flagResult");
                int flagCondition = actionConfig.getInt("flagCondition");
                boolean conditionTrueFalse = actionConfig.getBoolean(Condition.SCHEME);
                String viewID = actionConfig.getString("viewID");
                String viewClassName = actionConfig.getString("viewClassName");
                String targetActivity = actionConfig.getString("targetActivity");
                List<TouchEventConfig> touchEventConfigList = new ArrayList();
                String touchEventListString = actionConfig.getString("touchEvent");
                try {
                    int i2;
                    if (TextUtils.isEmpty(touchEventListString)) {
                        i2 = i;
                        str2 = packageName;
                    } else {
                        try {
                            String[] touchEventList = touchEventListString.split(";");
                            i = touchEventList.length;
                            int i3 = 0;
                            while (i3 < i) {
                                int i4 = i;
                                String[] touchEventList2 = touchEventList;
                                touchEventList = touchEventList[i3].trim();
                                String[] configSegList = touchEventList.split(",");
                                String[] strArr = touchEventList;
                                str2 = packageName;
                                if (configSegList.length == 5) {
                                    try {
                                        touchEventConfigList.add(new TouchEventConfig(Integer.parseInt(configSegList[null]), Integer.parseInt(configSegList[1]), Integer.parseInt(configSegList[2]), Integer.parseInt(configSegList[3]), Integer.parseInt(configSegList[4])));
                                    } catch (Exception e2) {
                                        e = e2;
                                        packageName = touchEventListString;
                                        obj = touchEventConfigList;
                                    }
                                }
                                i3++;
                                i = i4;
                                touchEventList = touchEventList2;
                                packageName = str2;
                            }
                            str2 = packageName;
                            i2 = 0;
                        } catch (Exception e3) {
                            e = e3;
                            str2 = packageName;
                            packageName = touchEventListString;
                            obj = touchEventConfigList;
                            Slog.w(str, e);
                            return null;
                        }
                    }
                    appConfig.addSlideConfig(new SlideConfig(keyCode, version.isEmpty() ? i2 : Integer.parseInt(version), startingActivity, flagAction, flagResult, flagCondition, conditionTrueFalse, viewID, viewClassName, touchEventConfigList, targetActivity));
                    index++;
                    jSONObject = jsonObject;
                    i = i2;
                    packageName = str2;
                } catch (Exception e4) {
                    e = e4;
                    str2 = packageName;
                    packageName = touchEventListString;
                    List<TouchEventConfig> list = touchEventConfigList;
                    Slog.w(str, e);
                    return null;
                }
            }
            return appConfig;
        } catch (JSONException e5) {
            Slog.w(str, e5);
        }
    }

    private StringBuilder parseFile(File configFile) {
        if (configFile == null || !configFile.exists()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(configFile));
            while (true) {
                String readLine = reader.readLine();
                String line = readLine;
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(line);
            }
            return stringBuilder;
        } catch (Exception e) {
            Slog.w(TAG, e);
            return null;
        } finally {
            SlideUtils.closeQuietly(reader);
        }
    }

    private void parseSlideConfigFile(boolean updateAll, String packageName, int versionCode) {
        StringBuilder stringBuilder;
        Throwable e;
        boolean z = updateAll;
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("parseSlideConfigFile updateAll = ");
        stringBuilder2.append(z);
        String stringBuilder3 = stringBuilder2.toString();
        String str = TAG;
        Slog.w(str, stringBuilder3);
        stringBuilder2 = parseFile(new File(CloudConfigFilePath));
        if (stringBuilder2 == null) {
            stringBuilder2 = parseFile(new File(ConfigFilePath));
            if (stringBuilder2 != null) {
                stringBuilder = stringBuilder2;
            } else {
                return;
            }
        }
        stringBuilder = stringBuilder2;
        String fileContent = stringBuilder.toString();
        if (!TextUtils.isEmpty(fileContent)) {
            if (z) {
                this.mAppSlideConfigs.clear();
                this.mSlide3rdPackageList.clear();
            }
            String str2;
            int i;
            try {
                JSONArray packageArray = new JSONObject(fileContent).getJSONArray(LogSwitchesConfigManager.EXTRA_KEY_PACKAGES);
                for (int i2 = 0; i2 < packageArray.length(); i2++) {
                    AppSlideConfig config = generateAppSlideConfigFromJson(packageArray.getJSONObject(i2));
                    if (config == null) {
                        str2 = packageName;
                        i = versionCode;
                    } else if (z) {
                        this.mSlide3rdPackageList.add(config.mPackageName);
                        try {
                            PackageInfo packageInfo = this.mPackageManager.getPackageInfo(config.mPackageName, 0);
                            if (packageInfo != null) {
                                config.matchVersionSlideConfig(packageInfo.versionCode);
                                this.mAppSlideConfigs.put(config.mPackageName, config);
                            }
                        } catch (NameNotFoundException e2) {
                        }
                        str2 = packageName;
                        i = versionCode;
                    } else {
                        try {
                            if (config.mPackageName.equals(packageName)) {
                                try {
                                    config.matchVersionSlideConfig(versionCode);
                                    this.mAppSlideConfigs.put(config.mPackageName, config);
                                } catch (Exception e3) {
                                    e = e3;
                                }
                            } else {
                                i = versionCode;
                            }
                        } catch (Exception e4) {
                            e = e4;
                            i = versionCode;
                            Slog.w(str, e);
                        }
                    }
                }
                str2 = packageName;
                i = versionCode;
            } catch (Exception e5) {
                e = e5;
                str2 = packageName;
                i = versionCode;
                Slog.w(str, e);
            }
        }
    }

    public AppSlideConfig getAppSlideConfigs(String packageName) {
        if (this.mAppSlideConfigs.containsKey(packageName)) {
            return (AppSlideConfig) this.mAppSlideConfigs.get(packageName);
        }
        return null;
    }

    public boolean isMiuiAdapteringApp(String packageName) {
        if (packageName.equals("com.tencent.mm") || packageName.equals(XSpace.QQ_PACKAGE_NAME)) {
            return false;
        }
        return this.mAppSlideConfigs.containsKey(packageName);
    }

    /* JADX WARNING: Missing block: B:8:0x001c, code skipped:
            return false;
     */
    public boolean is3rdAppProcessingActivity(java.lang.String r3, java.lang.String r4) {
        /*
        r2 = this;
        r0 = 0;
        if (r3 == 0) goto L_0x001c;
    L_0x0003:
        if (r4 != 0) goto L_0x0006;
    L_0x0005:
        goto L_0x001c;
    L_0x0006:
        r1 = r2.mAppSlideConfigs;
        r1 = r1.containsKey(r3);
        if (r1 == 0) goto L_0x001b;
    L_0x000e:
        r0 = r2.mAppSlideConfigs;
        r0 = r0.get(r3);
        r0 = (miui.slide.AppSlideConfig) r0;
        r1 = r0.matchStartingActivity(r4);
        return r1;
    L_0x001b:
        return r0;
    L_0x001c:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.slide.SlideCloudConfigHelper.is3rdAppProcessingActivity(java.lang.String, java.lang.String):boolean");
    }

    public void registerSlideActivityChangeListener() {
        if (this.mActivityChangeListener != null) {
            List<String> packageListForRegister = new ArrayList(this.mAppSlideConfigs.keySet());
            packageListForRegister.addAll(this.mSlideSystemPackageList);
            List<String> targetActivities = new ArrayList();
            targetActivities.add("com.android.keyguard.settings.MiuiFaceDataInput");
            targetActivities.add("com.android.keyguard.settings.MiuiFaceDataIntroduction");
            ProcessManager.unregisterActivityChanageListener(this.mActivityChangeListener);
            ProcessManager.registerActivityChangeListener(packageListForRegister, targetActivities, this.mActivityChangeListener);
        }
    }

    public void initPackageMonitor(Context context) {
        this.mPackageMonitor.register(context, BackgroundThread.getHandler().getLooper(), UserHandle.CURRENT, true);
    }
}
