package miui.util;

import android.app.AppGlobals;
import android.graphics.Point;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.view.Display;
import android.view.DisplayInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import miui.os.Build;
import miui.os.MiuiInit;

public class CustomizeUtil {
    public static final String ADJUST = "adjust";
    public static final String ANDROID_MAX_ASPECT = "android.max_aspect";
    private static final String CUST_VARIANT = "cust_variant";
    private static final File CUST_VARIANT_FILE = getMiuiCustVariantFile();
    private static final File DATA_NONCUSTOMIZED_APP_DIR = new File("/data/miui/app/noncustomized");
    public static final String ENABLE_CONFIG = "enable_config";
    public static final int EXTRA_PRIVATE_FLAG_SPECIAL_MODE = 128;
    public static final boolean HAS_NOTCH = "1".equals(SystemProperties.get("ro.miui.notch", "0"));
    public static final float MAX_ASPECT_RATIO = 3.0f;
    private static final File MIUI_APP_DIR = new File(getMiuiCustomizedDir(), "app");
    private static final File MIUI_CUSTOMIZED_APP_DIR = new File(getMiuiAppDir(), "customized");
    private static final File MIUI_CUSTOMIZED_CUST_DIR = new File("/cust/");
    private static final File MIUI_CUSTOMIZED_DATA_DIR = new File("/data/miui/");
    private static final File MIUI_CUST_DIR = new File(getMiuiCustomizedDir(), "cust");
    public static final String NEED_ADJUST = "need_adjust";
    public static final String NOTCH_CONFIG = "notch.config";
    public static final String PACKAGE = "pkg";
    private static final File PRODUCT_NONCUSTOMIZED_APP_DIR = new File("/product/data-app/");
    public static final float RESTRICT_ASPECT_RATIO = ("lithium".equals(Build.DEVICE) ? 1.7777778f : 1.833f);
    private static final File SYSTEM_NONCUSTOMIZED_APP_DIR = new File("/system/data-app/");
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_METADATA = 1;
    public static final int TYPE_OTHER = 5;
    public static final int TYPE_RESIZEABLE = 2;
    public static final int TYPE_RESTRICT = 4;
    public static final int TYPE_SUGGEST = 3;
    public static final String UPDATE_SPECIAL_MODE = "upate_specail_mode";
    private static final File VENDOR_NONCUSTOMIZED_APP_DIR = new File("/vendor/data-app/");
    private static String sCustVariant = "";
    private static ArrayList<String> sForceLayoutHideNavigationPkgs = new ArrayList();
    private static ArrayList<String> sNeedCompatNotchPkgs = new ArrayList();

    static {
        sForceLayoutHideNavigationPkgs.add("android");
        sForceLayoutHideNavigationPkgs.add("com.android.systemui");
        sForceLayoutHideNavigationPkgs.add("com.android.keyguard");
        sForceLayoutHideNavigationPkgs.add("com.miui.aod");
        sNeedCompatNotchPkgs.add("com.tencent.tmgp.sgame");
        sNeedCompatNotchPkgs.add("com.tencent.tmgp.sgamece");
        sNeedCompatNotchPkgs.add("com.tencent.tmgp.pubgmhd");
        sNeedCompatNotchPkgs.add("com.tencent.tmgp.pubgmhdce");
        sNeedCompatNotchPkgs.add("com.tencent.tmgp.speedmobile");
        sNeedCompatNotchPkgs.add("com.tencent.tmgp.speedmobileEx");
        sNeedCompatNotchPkgs.add("com.tencent.tmgp.cf");
        sNeedCompatNotchPkgs.add("com.tencent.tmgp.pubgm");
        sNeedCompatNotchPkgs.add("com.netease.hyxd.mi");
        sNeedCompatNotchPkgs.add("com.netease.hyxd");
        sNeedCompatNotchPkgs.add("com.netease.dwrg.mi");
        sNeedCompatNotchPkgs.add("com.netease.dwrg");
        sNeedCompatNotchPkgs.add("com.netease.mrzh.mi");
        sNeedCompatNotchPkgs.add("com.netease.mrzh");
        sNeedCompatNotchPkgs.add("com.netease.h48");
        sNeedCompatNotchPkgs.add("com.netease.h48.mi");
    }

    public static File getMiuiCustomizedDir() {
        if (Build.HAS_CUST_PARTITION) {
            return MIUI_CUSTOMIZED_CUST_DIR;
        }
        return MIUI_CUSTOMIZED_DATA_DIR;
    }

    public static File getMiuiNoCustomizedAppDir() {
        if (Build.HAS_CUST_PARTITION) {
            return SYSTEM_NONCUSTOMIZED_APP_DIR;
        }
        return DATA_NONCUSTOMIZED_APP_DIR;
    }

    public static File getMiuiVendorNoCustomizedAppDir() {
        if (Build.HAS_CUST_PARTITION) {
            return VENDOR_NONCUSTOMIZED_APP_DIR;
        }
        return DATA_NONCUSTOMIZED_APP_DIR;
    }

    public static File getMiuiProductNoCustomizedAppDir() {
        if (Build.HAS_CUST_PARTITION) {
            return PRODUCT_NONCUSTOMIZED_APP_DIR;
        }
        return DATA_NONCUSTOMIZED_APP_DIR;
    }

    public static File getMiuiAppDir() {
        return MIUI_APP_DIR;
    }

    public static File getMiuiCustomizedAppDir() {
        return MIUI_CUSTOMIZED_APP_DIR;
    }

    public static File getMiuiCustDir() {
        return MIUI_CUST_DIR;
    }

    public static File getMiuiCustVariantDir() {
        StringBuilder stringBuilder;
        Exception e;
        StringBuilder stringBuilder2;
        String str = "getMiuiCustVariantDir finally Exception e:";
        String str2 = "CustomizeUtil";
        if (Build.IS_GLOBAL_BUILD && !TextUtils.isEmpty(sCustVariant)) {
            return new File(getMiuiCustDir(), sCustVariant);
        }
        if (CUST_VARIANT_FILE.exists()) {
            FileReader fr = null;
            BufferedReader br = null;
            try {
                fr = new FileReader(CUST_VARIANT_FILE);
                br = new BufferedReader(fr);
                String cust_variant = br.readLine();
                if (cust_variant != null) {
                    File file = new File(getMiuiCustDir(), cust_variant.trim().replace("\n", ""));
                    try {
                        fr.close();
                        br.close();
                    } catch (Exception e2) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(e2.getMessage());
                        Log.e(str2, stringBuilder.toString());
                    }
                    return file;
                }
                try {
                    fr.close();
                    br.close();
                } catch (Exception e3) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(e3.getMessage());
                    Log.e(str2, stringBuilder.toString());
                }
                return null;
            } catch (FileNotFoundException e4) {
                e4.printStackTrace();
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (Exception e5) {
                        e = e5;
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str);
                        stringBuilder2.append(e.getMessage());
                        Log.e(str2, stringBuilder2.toString());
                        return null;
                    }
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e6) {
                e6.printStackTrace();
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (Exception e7) {
                        e = e7;
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str);
                        stringBuilder2.append(e.getMessage());
                        Log.e(str2, stringBuilder2.toString());
                        return null;
                    }
                }
                if (br != null) {
                    br.close();
                }
            } catch (Throwable th) {
                if (fr != null) {
                    try {
                        fr.close();
                    } catch (Exception e8) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(str);
                        stringBuilder2.append(e8.getMessage());
                        Log.e(str2, stringBuilder2.toString());
                    }
                }
                if (br != null) {
                    br.close();
                }
            }
        }
        return null;
    }

    public static void setMiuiCustVariatDir(String custVariant) {
        sCustVariant = custVariant;
    }

    public static File getMiuiCustVariantFile() {
        boolean z = Build.HAS_CUST_PARTITION;
        String str = CUST_VARIANT;
        if (!z || Build.IS_GLOBAL_BUILD) {
            return new File(MIUI_CUSTOMIZED_DATA_DIR, str);
        }
        return new File(MIUI_CUSTOMIZED_CUST_DIR, str);
    }

    public static boolean needChangeSize() {
        return false;
    }

    public static boolean forceLayoutHideNavigation(String pkg) {
        return sForceLayoutHideNavigationPkgs.contains(pkg);
    }

    public static void getRealSize(Display display, Point outPoint) {
        try {
            Display.class.getDeclaredMethod("getRealSize", new Class[]{Point.class, Boolean.TYPE}).invoke(display, new Object[]{outPoint, Boolean.valueOf(true)});
        } catch (Exception e) {
            Log.w("CustomizeUtil", "no getRealSize hack method");
            display.getRealSize(outPoint);
        }
    }

    public static boolean isRestrict(float maxAspect) {
        return maxAspect > 0.0f && maxAspect < 3.0f;
    }

    private static String getCallingUidPackage(int callingUid) {
        if (callingUid > 0) {
            try {
                String[] packages = AppGlobals.getPackageManager().getPackagesForUid(callingUid);
                if (packages != null && packages.length > 0) {
                    return packages[0];
                }
            } catch (Exception e) {
                Log.w("CustomizeUtil", "getCallingUidPackage failed.", e);
            }
        }
        return null;
    }

    public static DisplayInfo adjustDisplay(DisplayInfo info, int callingUid, String callingPkg) {
        DisplayInfo localDisplayInfo = info;
        if (!(callingUid == 1000 || callingUid == 0)) {
            try {
                if (MiuiInit.isRestrictAspect(callingPkg)) {
                    localDisplayInfo = new DisplayInfo(info);
                    if (localDisplayInfo.logicalWidth < localDisplayInfo.logicalHeight) {
                        localDisplayInfo.logicalHeight = Math.min(localDisplayInfo.logicalHeight, (int) ((((float) localDisplayInfo.logicalWidth) * RESTRICT_ASPECT_RATIO) + 1056964608));
                        localDisplayInfo.appHeight = Math.min(localDisplayInfo.appHeight, (int) ((((float) localDisplayInfo.appWidth) * RESTRICT_ASPECT_RATIO) + 0.5f));
                    } else {
                        localDisplayInfo.logicalWidth = Math.min(localDisplayInfo.logicalWidth, (int) ((((float) localDisplayInfo.logicalHeight) * RESTRICT_ASPECT_RATIO) + 1056964608));
                        localDisplayInfo.appWidth = Math.min(localDisplayInfo.appWidth, (int) ((((float) localDisplayInfo.appHeight) * RESTRICT_ASPECT_RATIO) + 0.5f));
                    }
                }
            } catch (Exception e) {
                Log.w("CustomizeUtil", "ajsustDisplay failed.", e);
            }
        }
        return localDisplayInfo;
    }

    public static boolean needCompatNotch(String pkg) {
        return HAS_NOTCH && sNeedCompatNotchPkgs.contains(pkg);
    }
}
