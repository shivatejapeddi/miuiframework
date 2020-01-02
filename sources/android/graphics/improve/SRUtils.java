package android.graphics.improve;

import android.os.SystemProperties;
import java.util.ArrayList;
import miui.os.Build;

public class SRUtils {
    public static final String PKG_MM = "com.tencent.mm";
    public static final String PKG_NEWS = "com.ss.android.article.news";
    private static final String PROD_CAPRICORN = "capricorn";
    private static final String PROD_CHIRON = "chiron";
    private static final String PROD_CLOVER = "clover";
    private static final String PROD_DIPPER = "dipper";
    private static final String PROD_EQUULEUS = "equuleus";
    private static final String PROD_GEMINI = "gemini";
    private static final String PROD_JASON = "jason";
    private static final String PROD_LITHIUM = "lithium";
    private static final String PROD_NATRIUM = "natrium";
    private static final String PROD_POLARIS = "polaris";
    private static final String PROD_SAGIT = "sagit";
    private static final String PROD_SCORPIO = "scorpio";
    private static final String PROD_SIRIUS = "sirius";
    private static final String PROD_URSA = "ursa";
    private static final String PROD_WAYNE = "wayne";
    private static final String PROP_LAB_TOGGLE = "persist.sys.sr_lab";
    private static final String PROP_LOCAL_TOGGLE = "persist.sys.sr_local";
    private static final String PROP_SERVER_TOGGLE = "persist.sys.resolution";
    public static ArrayList<String> sAccessAppList = new ArrayList();
    public static ArrayList<String> sProductList = new ArrayList();
    public static ArrayList<String> sSelfProductList = new ArrayList();

    static {
        ArrayList arrayList = sProductList;
        String str = PROD_CHIRON;
        arrayList.add(str);
        sProductList.add(PROD_SAGIT);
        sProductList.add(PROD_POLARIS);
        arrayList = sProductList;
        String str2 = PROD_DIPPER;
        arrayList.add(str2);
        sProductList.add(PROD_SIRIUS);
        sProductList.add(PROD_URSA);
        sProductList.add(PROD_GEMINI);
        sProductList.add(PROD_SCORPIO);
        sProductList.add(PROD_LITHIUM);
        sProductList.add(PROD_WAYNE);
        sProductList.add(PROD_CLOVER);
        sProductList.add(PROD_CAPRICORN);
        sProductList.add(PROD_NATRIUM);
        sProductList.add(PROD_EQUULEUS);
        arrayList = sProductList;
        String str3 = PROD_JASON;
        arrayList.add(str3);
        sAccessAppList.add("com.tencent.mm");
        sAccessAppList.add("com.ss.android.article.news");
        sSelfProductList.add(str2);
        sSelfProductList.add(str);
        sSelfProductList.add(str3);
    }

    public static boolean isAccessSR() {
        if (isAppAccessSR("com.tencent.mm") || isAppAccessSR("com.ss.android.article.news")) {
            return true;
        }
        return false;
    }

    public static boolean isProductAccessSR() {
        if (sProductList.contains(Build.PRODUCT)) {
            return true;
        }
        return false;
    }

    public static boolean isSupportSelfArithmetic() {
        return sSelfProductList.contains(Build.PRODUCT);
    }

    public static boolean isCloundAccessSR(String pkg) {
        return 1 ^ isPropContainPkgs(PROP_SERVER_TOGGLE, pkg);
    }

    public static boolean isLocalAccessSR(String pkg) {
        return 1 ^ isPropContainPkgs(PROP_LOCAL_TOGGLE, pkg);
    }

    public static boolean isAppAccessSR(String pkg) {
        if (isCloundAccessSR(pkg) && isLocalAccessSR(pkg)) {
            if (isPropContainPkgs(PROP_LAB_TOGGLE, pkg)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInAppAccessList(String pkg) {
        return sAccessAppList.contains(pkg);
    }

    public static void userOpenSR(String pkg) {
        addPkgFromProp(PROP_LAB_TOGGLE, pkg);
    }

    public static void userCloseSR(String pkg) {
        removePkgFromProp(PROP_LAB_TOGGLE, pkg);
    }

    public static void localOpenSR(String pkg) {
        removePkgFromProp(PROP_LOCAL_TOGGLE, pkg);
    }

    public static void localCloseSR(String pkg) {
        addPkgFromProp(PROP_LOCAL_TOGGLE, pkg);
    }

    public static void addPkgFromProp(String key, String pkg) {
        String value = SystemProperties.get(key);
        if (!value.contains(getAppPropStr(pkg))) {
            value = value.concat(getAppPropStr(pkg));
        }
        SystemProperties.set(key, value);
    }

    public static void removePkgFromProp(String key, String pkg) {
        String value = SystemProperties.get(key);
        String propStr = getAppPropStr(pkg);
        if (value.contains(propStr)) {
            value = value.replace(propStr, "");
        }
        SystemProperties.set(key, value);
    }

    public static boolean isPropContainPkgs(String key, String... pkgs) {
        if (pkgs == null || pkgs.length <= 0) {
            return false;
        }
        String value = SystemProperties.get(key);
        for (String pkg : pkgs) {
            if (!value.contains(pkg)) {
                return false;
            }
        }
        return true;
    }

    public static String getAppPropStr(String pkg) {
        return pkg.concat(":");
    }
}
