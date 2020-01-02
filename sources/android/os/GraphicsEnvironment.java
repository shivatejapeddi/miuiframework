package android.os;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.provider.Settings.Global;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.widget.Toast;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphicsEnvironment {
    private static final String ACTION_ANGLE_FOR_ANDROID = "android.app.action.ANGLE_FOR_ANDROID";
    private static final String ACTION_ANGLE_FOR_ANDROID_TOAST_MESSAGE = "android.app.action.ANGLE_FOR_ANDROID_TOAST_MESSAGE";
    private static final String ANGLE_RULES_FILE = "a4a_rules.json";
    private static final String ANGLE_TEMP_RULES = "debug.angle.rules";
    private static final boolean DEBUG = false;
    private static final int GAME_DRIVER_GLOBAL_OPT_IN_DEFAULT = 0;
    private static final int GAME_DRIVER_GLOBAL_OPT_IN_GAME_DRIVER = 1;
    private static final int GAME_DRIVER_GLOBAL_OPT_IN_OFF = 3;
    private static final int GAME_DRIVER_GLOBAL_OPT_IN_PRERELEASE_DRIVER = 2;
    private static final String GAME_DRIVER_SPHAL_LIBRARIES_FILENAME = "sphal_libraries.txt";
    private static final String GAME_DRIVER_WHITELIST_ALL = "*";
    private static final String INTENT_KEY_A4A_TOAST_MESSAGE = "A4A Toast Message";
    private static final String METADATA_DRIVER_BUILD_TIME = "com.android.gamedriver.build_time";
    private static final String PROPERTY_GFX_DRIVER = "ro.gfx.driver.0";
    private static final String PROPERTY_GFX_DRIVER_BUILD_TIME = "ro.gfx.driver_build_time";
    private static final String PROPERTY_GFX_DRIVER_PRERELEASE = "ro.gfx.driver.1";
    private static final String SYSTEM_DRIVER_NAME = "system";
    private static final long SYSTEM_DRIVER_VERSION_CODE = 0;
    private static final String SYSTEM_DRIVER_VERSION_NAME = "";
    private static final String TAG = "GraphicsEnvironment";
    private static final int VULKAN_1_0 = 4194304;
    private static final int VULKAN_1_1 = 4198400;
    private static final Map<OpenGlDriverChoice, String> sDriverMap = buildMap();
    private static final GraphicsEnvironment sInstance = new GraphicsEnvironment();
    private ClassLoader mClassLoader;
    private String mDebugLayerPath;
    private String mLayerPath;

    enum OpenGlDriverChoice {
        DEFAULT,
        NATIVE,
        ANGLE
    }

    private static native int getCanLoadSystemLibraries();

    private static native boolean getShouldUseAngle(String str);

    public static native void hintActivityLaunch();

    private static native void setAngleInfo(String str, String str2, String str3, FileDescriptor fileDescriptor, long j, long j2);

    private static native void setDebugLayers(String str);

    private static native void setDebugLayersGLES(String str);

    private static native void setDriverPathAndSphalLibraries(String str, String str2);

    private static native void setGpuStats(String str, String str2, long j, long j2, String str3, int i);

    private static native void setLayerPaths(ClassLoader classLoader, String str);

    public static GraphicsEnvironment getInstance() {
        return sInstance;
    }

    public void setup(Context context, Bundle coreSettings) {
        PackageManager pm = context.getPackageManager();
        String packageName = context.getPackageName();
        Trace.traceBegin(2, "setupGpuLayers");
        setupGpuLayers(context, coreSettings, pm, packageName);
        Trace.traceEnd(2);
        Trace.traceBegin(2, "setupAngle");
        setupAngle(context, coreSettings, pm, packageName);
        Trace.traceEnd(2);
        Trace.traceBegin(2, "chooseDriver");
        if (!chooseDriver(context, coreSettings, pm, packageName)) {
            setGpuStats("system", "", 0, SystemProperties.getLong(PROPERTY_GFX_DRIVER_BUILD_TIME, 0), packageName, getVulkanVersion(pm));
        }
        Trace.traceEnd(2);
    }

    public static boolean shouldUseAngle(Context context, Bundle coreSettings, String packageName) {
        boolean isEmpty = packageName.isEmpty();
        String str = TAG;
        if (isEmpty) {
            Log.v(str, "No package name available yet, ANGLE should not be used");
            return false;
        }
        String devOptIn = getDriverForPkg(context, coreSettings, packageName);
        boolean whitelisted = checkAngleWhitelist(context, coreSettings, packageName);
        boolean requested = devOptIn.equals(sDriverMap.get(OpenGlDriverChoice.ANGLE));
        boolean useAngle = whitelisted || requested;
        if (!useAngle) {
            return false;
        }
        StringBuilder stringBuilder;
        if (whitelisted) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("ANGLE whitelist includes ");
            stringBuilder.append(packageName);
            Log.v(str, stringBuilder.toString());
        }
        if (requested) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("ANGLE developer option for ");
            stringBuilder.append(packageName);
            stringBuilder.append(": ");
            stringBuilder.append(devOptIn);
            Log.v(str, stringBuilder.toString());
        }
        return true;
    }

    private static int getVulkanVersion(PackageManager pm) {
        String str = PackageManager.FEATURE_VULKAN_HARDWARE_VERSION;
        if (pm.hasSystemFeature(str, VULKAN_1_1)) {
            return VULKAN_1_1;
        }
        if (pm.hasSystemFeature(str, 4194304)) {
            return 4194304;
        }
        return 0;
    }

    private static boolean isDebuggable(Context context) {
        return (context.getApplicationInfo().flags & 2) > 0;
    }

    public void setLayerPaths(ClassLoader classLoader, String layerPath, String debugLayerPath) {
        this.mClassLoader = classLoader;
        this.mLayerPath = layerPath;
        this.mDebugLayerPath = debugLayerPath;
    }

    private static String getDebugLayerAppPaths(PackageManager pm, String app) {
        try {
            ApplicationInfo appInfo = pm.getApplicationInfo(app, 131072);
            String abi = chooseAbi(appInfo);
            StringBuilder sb = new StringBuilder();
            sb.append(appInfo.nativeLibraryDir);
            sb.append(File.pathSeparator);
            sb.append(appInfo.sourceDir);
            sb.append("!/lib/");
            sb.append(abi);
            return sb.toString();
        } catch (NameNotFoundException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Debug layer app '");
            stringBuilder.append(app);
            stringBuilder.append("' not installed");
            Log.w(TAG, stringBuilder.toString());
            return null;
        }
    }

    private void setupGpuLayers(Context context, Bundle coreSettings, PackageManager pm, String packageName) {
        String layerPaths = "";
        if ((isDebuggable(context) || getCanLoadSystemLibraries() == 1) && coreSettings.getInt(Global.ENABLE_GPU_DEBUG_LAYERS, 0) != 0) {
            String gpuDebugApp = coreSettings.getString(Global.GPU_DEBUG_APP);
            if (!(gpuDebugApp == null || packageName == null || gpuDebugApp.isEmpty() || packageName.isEmpty() || !gpuDebugApp.equals(packageName))) {
                StringBuilder stringBuilder;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("GPU debug layers enabled for ");
                stringBuilder2.append(packageName);
                String stringBuilder3 = stringBuilder2.toString();
                String str = TAG;
                Log.i(str, stringBuilder3);
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(this.mDebugLayerPath);
                String str2 = ":";
                stringBuilder2.append(str2);
                layerPaths = stringBuilder2.toString();
                stringBuilder3 = coreSettings.getString(Global.GPU_DEBUG_LAYER_APP);
                if (!(stringBuilder3 == null || stringBuilder3.isEmpty())) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("GPU debug layer app: ");
                    stringBuilder.append(stringBuilder3);
                    Log.i(str, stringBuilder.toString());
                    String[] layerApps = stringBuilder3.split(str2);
                    for (String paths : layerApps) {
                        String paths2 = getDebugLayerAppPaths(pm, paths2);
                        if (paths2 != null) {
                            StringBuilder stringBuilder4 = new StringBuilder();
                            stringBuilder4.append(layerPaths);
                            stringBuilder4.append(paths2);
                            stringBuilder4.append(str2);
                            layerPaths = stringBuilder4.toString();
                        }
                    }
                }
                str2 = coreSettings.getString(Global.GPU_DEBUG_LAYERS);
                stringBuilder = new StringBuilder();
                stringBuilder.append("Vulkan debug layer list: ");
                stringBuilder.append(str2);
                Log.i(str, stringBuilder.toString());
                if (!(str2 == null || str2.isEmpty())) {
                    setDebugLayers(str2);
                }
                String layersGLES = coreSettings.getString(Global.GPU_DEBUG_LAYERS_GLES);
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append("GLES debug layer list: ");
                stringBuilder5.append(layersGLES);
                Log.i(str, stringBuilder5.toString());
                if (!(layersGLES == null || layersGLES.isEmpty())) {
                    setDebugLayersGLES(layersGLES);
                }
            }
        }
        StringBuilder stringBuilder6 = new StringBuilder();
        stringBuilder6.append(layerPaths);
        stringBuilder6.append(this.mLayerPath);
        setLayerPaths(this.mClassLoader, stringBuilder6.toString());
    }

    private static Map<OpenGlDriverChoice, String> buildMap() {
        Map<OpenGlDriverChoice, String> map = new HashMap();
        map.put(OpenGlDriverChoice.DEFAULT, "default");
        map.put(OpenGlDriverChoice.ANGLE, "angle");
        map.put(OpenGlDriverChoice.NATIVE, "native");
        return map;
    }

    private static List<String> getGlobalSettingsString(ContentResolver contentResolver, Bundle bundle, String globalSetting) {
        String settingsValue;
        if (bundle != null) {
            settingsValue = bundle.getString(globalSetting);
        } else {
            settingsValue = Global.getString(contentResolver, globalSetting);
        }
        if (settingsValue != null) {
            return new ArrayList(Arrays.asList(settingsValue.split(",")));
        }
        return new ArrayList();
    }

    private static int getGlobalSettingsPkgIndex(String pkgName, List<String> globalSettingsDriverPkgs) {
        for (int pkgIndex = 0; pkgIndex < globalSettingsDriverPkgs.size(); pkgIndex++) {
            if (((String) globalSettingsDriverPkgs.get(pkgIndex)).equals(pkgName)) {
                return pkgIndex;
            }
        }
        return -1;
    }

    private static String getDriverForPkg(Context context, Bundle bundle, String packageName) {
        String str = Global.GLOBAL_SETTINGS_ANGLE_GL_DRIVER_ALL_ANGLE;
        if (bundle != null) {
            str = bundle.getString(str);
        } else {
            str = Global.getString(context.getContentResolver(), str);
        }
        if (str != null && str.equals("1")) {
            return (String) sDriverMap.get(OpenGlDriverChoice.ANGLE);
        }
        ContentResolver contentResolver = context.getContentResolver();
        List<String> globalSettingsDriverPkgs = getGlobalSettingsString(contentResolver, bundle, Global.GLOBAL_SETTINGS_ANGLE_GL_DRIVER_SELECTION_PKGS);
        List<String> globalSettingsDriverValues = getGlobalSettingsString(contentResolver, bundle, Global.GLOBAL_SETTINGS_ANGLE_GL_DRIVER_SELECTION_VALUES);
        if (packageName == null || packageName.isEmpty()) {
            return (String) sDriverMap.get(OpenGlDriverChoice.DEFAULT);
        }
        if (globalSettingsDriverPkgs.size() != globalSettingsDriverValues.size()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Global.Settings values are invalid: globalSettingsDriverPkgs.size = ");
            stringBuilder.append(globalSettingsDriverPkgs.size());
            stringBuilder.append(", globalSettingsDriverValues.size = ");
            stringBuilder.append(globalSettingsDriverValues.size());
            Log.w(TAG, stringBuilder.toString());
            return (String) sDriverMap.get(OpenGlDriverChoice.DEFAULT);
        }
        int pkgIndex = getGlobalSettingsPkgIndex(packageName, globalSettingsDriverPkgs);
        if (pkgIndex < 0) {
            return (String) sDriverMap.get(OpenGlDriverChoice.DEFAULT);
        }
        return (String) globalSettingsDriverValues.get(pkgIndex);
    }

    private String getAnglePackageName(PackageManager pm) {
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(new Intent(ACTION_ANGLE_FOR_ANDROID), 1048576);
        if (resolveInfos.size() == 1) {
            return ((ResolveInfo) resolveInfos.get(0)).activityInfo.packageName;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid number of ANGLE packages. Required: 1, Found: ");
        stringBuilder.append(resolveInfos.size());
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.e(str, stringBuilder2);
        for (ResolveInfo resolveInfo : resolveInfos) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Found ANGLE package: ");
            stringBuilder3.append(resolveInfo.activityInfo.packageName);
            Log.e(str, stringBuilder3.toString());
        }
        return "";
    }

    private String getAngleDebugPackage(Context context, Bundle coreSettings) {
        boolean appIsDebuggable = isDebuggable(context);
        boolean z = true;
        if (getCanLoadSystemLibraries() != 1) {
            z = false;
        }
        boolean deviceIsDebuggable = z;
        if (appIsDebuggable || deviceIsDebuggable) {
            String str = Global.GLOBAL_SETTINGS_ANGLE_DEBUG_PACKAGE;
            if (coreSettings != null) {
                str = coreSettings.getString(str);
            } else {
                str = Global.getString(context.getContentResolver(), str);
            }
            if (!(str == null || str.isEmpty())) {
                return str;
            }
        }
        return "";
    }

    private static boolean setupAngleWithTempRulesFile(Context context, String packageName, String paths, String devOptIn) {
        StringBuilder stringBuilder;
        boolean appIsDebuggable = isDebuggable(context);
        boolean deviceIsDebuggable = getCanLoadSystemLibraries() == 1;
        String str = TAG;
        StringBuilder stringBuilder2;
        if (appIsDebuggable || deviceIsDebuggable) {
            String angleTempRules = SystemProperties.get(ANGLE_TEMP_RULES);
            if (angleTempRules == null || angleTempRules.isEmpty()) {
                Log.v(str, "System property 'debug.angle.rules' is not set or is empty");
                return false;
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Detected system property debug.angle.rules: ");
            stringBuilder2.append(angleTempRules);
            Log.i(str, stringBuilder2.toString());
            if (new File(angleTempRules).exists()) {
                stringBuilder2 = new StringBuilder();
                stringBuilder2.append(angleTempRules);
                stringBuilder2.append(" exists, loading file.");
                Log.i(str, stringBuilder2.toString());
                try {
                    FileInputStream stream = new FileInputStream(angleTempRules);
                    try {
                        FileDescriptor rulesFd = stream.getFD();
                        long rulesLength = stream.getChannel().size();
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Loaded temporary ANGLE rules from ");
                        stringBuilder2.append(angleTempRules);
                        Log.i(str, stringBuilder2.toString());
                        setAngleInfo(paths, packageName, devOptIn, rulesFd, 0, rulesLength);
                        stream.close();
                        return true;
                    } catch (IOException e) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Hit IOException thrown by FileInputStream: ");
                        stringBuilder.append(e);
                        Log.w(str, stringBuilder.toString());
                    }
                } catch (FileNotFoundException e2) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Temp ANGLE rules file not found: ");
                    stringBuilder.append(e2);
                    Log.w(str, stringBuilder.toString());
                } catch (SecurityException e3) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Temp ANGLE rules file not accessible: ");
                    stringBuilder.append(e3);
                    Log.w(str, stringBuilder.toString());
                }
            }
            return false;
        }
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Skipping loading temporary rules file: appIsDebuggable = ");
        stringBuilder2.append(appIsDebuggable);
        stringBuilder2.append(", adbRootEnabled = ");
        stringBuilder2.append(deviceIsDebuggable);
        Log.v(str, stringBuilder2.toString());
        return false;
    }

    private static boolean setupAngleRulesApk(String anglePkgName, ApplicationInfo angleInfo, PackageManager pm, String packageName, String paths, String devOptIn) {
        String str = anglePkgName;
        String str2 = "': ";
        String str3 = TAG;
        try {
            try {
                AssetFileDescriptor assetsFd = pm.getResourcesForApplication(angleInfo).getAssets().openFd(ANGLE_RULES_FILE);
                setAngleInfo(paths, packageName, devOptIn, assetsFd.getFileDescriptor(), assetsFd.getStartOffset(), assetsFd.getLength());
                assetsFd.close();
                return true;
            } catch (IOException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to get AssetFileDescriptor for a4a_rules.json from '");
                stringBuilder.append(anglePkgName);
                stringBuilder.append(str2);
                stringBuilder.append(e);
                Log.w(str3, stringBuilder.toString());
                return false;
            }
        } catch (NameNotFoundException e2) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Failed to get AssetManager for '");
            stringBuilder2.append(anglePkgName);
            stringBuilder2.append(str2);
            stringBuilder2.append(e2);
            Log.w(str3, stringBuilder2.toString());
        }
    }

    private static boolean checkAngleWhitelist(Context context, Bundle bundle, String packageName) {
        return getGlobalSettingsString(context.getContentResolver(), bundle, Global.GLOBAL_SETTINGS_ANGLE_WHITELIST).contains(packageName);
    }

    public boolean setupAngle(Context context, Bundle bundle, PackageManager pm, String packageName) {
        StringBuilder stringBuilder;
        Context context2 = context;
        Bundle bundle2 = bundle;
        PackageManager packageManager = pm;
        String str = packageName;
        if (!shouldUseAngle(context2, bundle2, str)) {
            return false;
        }
        StringBuilder stringBuilder2;
        ApplicationInfo angleInfo;
        String anglePkgName;
        ApplicationInfo angleInfo2 = null;
        String anglePkgName2 = getAngleDebugPackage(context, bundle);
        boolean isEmpty = anglePkgName2.isEmpty();
        String str2 = "' not installed";
        String str3 = TAG;
        if (!isEmpty) {
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("ANGLE debug package enabled: ");
            stringBuilder2.append(anglePkgName2);
            Log.i(str3, stringBuilder2.toString());
            try {
                angleInfo2 = packageManager.getApplicationInfo(anglePkgName2, 0);
            } catch (NameNotFoundException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("ANGLE debug package '");
                stringBuilder.append(anglePkgName2);
                stringBuilder.append(str2);
                Log.w(str3, stringBuilder.toString());
                return false;
            }
        }
        if (angleInfo2 == null) {
            anglePkgName2 = getAnglePackageName(packageManager);
            if (anglePkgName2.isEmpty()) {
                Log.e(str3, "Failed to find ANGLE package.");
                return false;
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("ANGLE package enabled: ");
            stringBuilder2.append(anglePkgName2);
            Log.i(str3, stringBuilder2.toString());
            try {
                angleInfo = packageManager.getApplicationInfo(anglePkgName2, 1048576);
                anglePkgName = anglePkgName2;
            } catch (NameNotFoundException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("ANGLE package '");
                stringBuilder.append(anglePkgName2);
                stringBuilder.append(str2);
                Log.w(str3, stringBuilder.toString());
                return false;
            }
        }
        angleInfo = angleInfo2;
        anglePkgName = anglePkgName2;
        String abi = chooseAbi(angleInfo);
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(angleInfo.nativeLibraryDir);
        stringBuilder3.append(File.pathSeparator);
        stringBuilder3.append(angleInfo.sourceDir);
        stringBuilder3.append("!/lib/");
        stringBuilder3.append(abi);
        String paths = stringBuilder3.toString();
        String devOptIn = getDriverForPkg(context2, bundle2, str);
        if (setupAngleWithTempRulesFile(context2, str, paths, devOptIn)) {
            return true;
        }
        if (setupAngleRulesApk(anglePkgName, angleInfo, pm, packageName, paths, devOptIn)) {
            return true;
        }
        return false;
    }

    private boolean shouldShowAngleInUseDialogBox(Context context) {
        boolean z = false;
        try {
            if (Global.getInt(context.getContentResolver(), Global.GLOBAL_SETTINGS_SHOW_ANGLE_IN_USE_DIALOG_BOX) == 1) {
                z = true;
            }
            return z;
        } catch (SettingNotFoundException | SecurityException e) {
            return false;
        }
    }

    private boolean setupAndUseAngle(Context context, String packageName) {
        boolean z = setupAngle(context, null, context.getPackageManager(), packageName);
        String str = "Package '";
        String str2 = TAG;
        if (z) {
            z = getShouldUseAngle(packageName);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(packageName);
            stringBuilder.append("' should use ANGLE = '");
            stringBuilder.append(z);
            stringBuilder.append("'");
            Log.v(str2, stringBuilder.toString());
            return z;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(packageName);
        stringBuilder2.append("' should not use ANGLE");
        Log.v(str2, stringBuilder2.toString());
        return false;
    }

    public void showAngleInUseDialogBox(Context context) {
        String packageName = context.getPackageName();
        if (shouldShowAngleInUseDialogBox(context) && setupAndUseAngle(context, packageName)) {
            Intent intent = new Intent(ACTION_ANGLE_FOR_ANDROID_TOAST_MESSAGE);
            intent.setPackage(getAnglePackageName(context.getPackageManager()));
            context.sendOrderedBroadcast(intent, null, new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    Toast.makeText(context, getResultExtras(true).getString(GraphicsEnvironment.INTENT_KEY_A4A_TOAST_MESSAGE), 1).show();
                }
            }, null, -1, null, null);
        }
    }

    private static String chooseDriverInternal(Context context, Bundle coreSettings) {
        String gameDriver = SystemProperties.get(PROPERTY_GFX_DRIVER);
        boolean hasGameDriver = (gameDriver == null || gameDriver.isEmpty()) ? false : true;
        String prereleaseDriver = SystemProperties.get(PROPERTY_GFX_DRIVER_PRERELEASE);
        boolean hasPrereleaseDriver = (prereleaseDriver == null || prereleaseDriver.isEmpty()) ? false : true;
        String str = null;
        if (!hasGameDriver && !hasPrereleaseDriver) {
            return null;
        }
        ApplicationInfo ai = context.getApplicationInfo();
        if (ai.isPrivilegedApp() || (ai.isSystemApp() && !ai.isUpdatedSystemApp())) {
            return null;
        }
        int i = coreSettings.getInt(Global.GAME_DRIVER_ALL_APPS, 0);
        if (i == 1) {
            if (hasGameDriver) {
                str = gameDriver;
            }
            return str;
        } else if (i == 2) {
            if (hasPrereleaseDriver) {
                str = prereleaseDriver;
            }
            return str;
        } else if (i == 3) {
            return null;
        } else {
            String appPackageName = ai.packageName;
            if (getGlobalSettingsString(null, coreSettings, Global.GAME_DRIVER_OPT_OUT_APPS).contains(appPackageName)) {
                return null;
            }
            if (getGlobalSettingsString(null, coreSettings, Global.GAME_DRIVER_PRERELEASE_OPT_IN_APPS).contains(appPackageName)) {
                if (hasPrereleaseDriver) {
                    str = prereleaseDriver;
                }
                return str;
            } else if (!hasGameDriver) {
                return null;
            } else {
                boolean isOptIn = getGlobalSettingsString(null, coreSettings, Global.GAME_DRIVER_OPT_IN_APPS).contains(appPackageName);
                List<String> whitelist = getGlobalSettingsString(null, coreSettings, Global.GAME_DRIVER_WHITELIST);
                if (!isOptIn && whitelist.indexOf("*") != 0 && !whitelist.contains(appPackageName)) {
                    return null;
                }
                if (isOptIn || !getGlobalSettingsString(null, coreSettings, Global.GAME_DRIVER_BLACKLIST).contains(appPackageName)) {
                    return gameDriver;
                }
                return null;
            }
        }
    }

    private static boolean chooseDriver(Context context, Bundle coreSettings, PackageManager pm, String packageName) {
        String driverPackageName = chooseDriverInternal(context, coreSettings);
        if (driverPackageName == null) {
            return false;
        }
        try {
            PackageInfo driverPackageInfo = pm.getPackageInfo(driverPackageName, 1048704);
            ApplicationInfo driverAppInfo = driverPackageInfo.applicationInfo;
            if (driverAppInfo.targetSdkVersion < 26) {
                return false;
            }
            String abi = chooseAbi(driverAppInfo);
            if (abi == null) {
                return false;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(driverAppInfo.nativeLibraryDir);
            sb.append(File.pathSeparator);
            sb.append(driverAppInfo.sourceDir);
            sb.append("!/lib/");
            sb.append(abi);
            String paths = sb.toString();
            String sphalLibraries = getSphalLibraries(context, driverPackageName);
            setDriverPathAndSphalLibraries(paths, sphalLibraries);
            if (driverAppInfo.metaData != null) {
                String driverBuildTime = driverAppInfo.metaData.getString(METADATA_DRIVER_BUILD_TIME);
                if (driverBuildTime == null || driverBuildTime.isEmpty()) {
                    String str = sphalLibraries;
                    throw new IllegalArgumentException("com.android.gamedriver.build_time is not set");
                }
                String str2 = driverPackageInfo.versionName;
                long j = driverAppInfo.longVersionCode;
                boolean z = true;
                long parseLong = Long.parseLong(driverBuildTime.substring(1));
                setGpuStats(driverPackageName, str2, j, parseLong, packageName, 0);
                return z;
            }
            throw new NullPointerException("apk's meta-data cannot be null");
        } catch (NameNotFoundException e) {
            Context context2 = context;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("driver package '");
            stringBuilder.append(driverPackageName);
            stringBuilder.append("' not installed");
            Log.w(TAG, stringBuilder.toString());
            return false;
        }
    }

    private static String chooseAbi(ApplicationInfo ai) {
        String isa = VMRuntime.getCurrentInstructionSet();
        if (ai.primaryCpuAbi != null && isa.equals(VMRuntime.getInstructionSet(ai.primaryCpuAbi))) {
            return ai.primaryCpuAbi;
        }
        if (ai.secondaryCpuAbi == null || !isa.equals(VMRuntime.getInstructionSet(ai.secondaryCpuAbi))) {
            return null;
        }
        return ai.secondaryCpuAbi;
    }

    private static String getSphalLibraries(Context context, String driverPackageName) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.createPackageContext(driverPackageName, 4).getAssets().open(GAME_DRIVER_SPHAL_LIBRARIES_FILENAME)));
            ArrayList<String> assetStrings = new ArrayList();
            while (true) {
                String readLine = reader.readLine();
                String assetString = readLine;
                if (readLine == null) {
                    return String.join(":", assetStrings);
                }
                assetStrings.add(assetString);
            }
        } catch (NameNotFoundException | IOException e) {
            return "";
        }
    }
}
