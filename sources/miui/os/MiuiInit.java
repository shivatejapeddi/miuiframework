package miui.os;

import android.content.res.Resources;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build.VERSION;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.R;
import miui.os.IMiuiInit.Stub;
import miui.util.CustomizeUtil;

public class MiuiInit {
    public static final String ACTION_MIUI_INIT_COMPLETED = "miui.intent.action.MIUI_INIT_COMPLETED";
    public static final String REGION = "region";
    public static final String SERVICE_NAME = "MiuiInit";
    private static final String TAG = "MiuiInit";
    private static boolean sNeedAspectSettings;
    private static boolean sNeedAspectSettingsInited;
    private static IMiuiInit sService;

    private static synchronized IMiuiInit getService() {
        IMiuiInit iMiuiInit;
        synchronized (MiuiInit.class) {
            if (sService == null) {
                sService = Stub.asInterface(ServiceManager.getService("MiuiInit"));
            }
            iMiuiInit = sService;
        }
        return iMiuiInit;
    }

    public static boolean initCustEnvironment(String custVariant, IMiuiInitObserver obs) {
        try {
            return getService().initCustEnvironment(custVariant, obs);
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Occur RemoteException when init cust environment for [");
            stringBuilder.append(custVariant);
            stringBuilder.append("]");
            Log.e("MiuiInit", stringBuilder.toString());
            return false;
        }
    }

    public static boolean installPreinstallApp() {
        try {
            getService().installPreinstallApp();
            return true;
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when install preinstall app");
            return false;
        }
    }

    public static String[] getCustVariants() {
        try {
            return getService().getCustVariants();
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when fetch cust variants");
            return null;
        }
    }

    public static void doFactoryReset(boolean keepUserApps) {
        try {
            getService().doFactoryReset(keepUserApps);
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when removing preinstall app history file");
        }
    }

    public static boolean isPreinstalledPackage(String pkg) {
        try {
            IMiuiInit service = getService();
            if (service != null) {
                return service.isPreinstalledPackage(pkg);
            }
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when checking preinstalled package");
        }
        return false;
    }

    public static boolean isPreinstalledPAIPackage(String pkg) {
        try {
            IMiuiInit service = getService();
            if (service != null) {
                return service.isPreinstalledPAIPackage(pkg);
            }
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when checking preinstalled PAI package");
        }
        return false;
    }

    public static int getPreinstalledAppVersion(String pkg) {
        try {
            IMiuiInit service = getService();
            if (service != null) {
                return service.getPreinstalledAppVersion(pkg);
            }
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when get preinstalled package version");
        }
        return -1;
    }

    public static String getMiuiChannelPath(String pkg) {
        try {
            return getService().getMiuiChannelPath(pkg);
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when checking preinstalled channel");
            return "";
        }
    }

    public static void removeFromPreinstallList(String pkg) {
        try {
            getService().removeFromPreinstallList(pkg);
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when removing from preinstall list");
        }
    }

    public static void removeFromPreinstallPAIList(String pkg) {
        try {
            getService().removeFromPreinstallPAIList(pkg);
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when removing from preinstall PAI list");
        }
    }

    public static void writePreinstallPAIPackage(String pkg) {
        try {
            getService().writePreinstallPAIPackage(pkg);
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when write package name to preinstall PAI list");
        }
    }

    public static void copyPreinstallPAITrackingFile(String type, String fileName, String content) {
        try {
            getService().copyPreinstallPAITrackingFile(type, fileName, content);
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when copy package tracking file to pai dir");
        }
    }

    public static String getMiuiPreinstallAppPath(String pkg) {
        try {
            return getService().getMiuiPreinstallAppPath(pkg);
        } catch (RemoteException e) {
            Log.e("MiuiInit", "Occur RemoteException when checking preinstalled app path");
            return "";
        }
    }

    private static boolean needAspectSettings() {
        if (!sNeedAspectSettingsInited) {
            String navBarOverride = SystemProperties.get("qemu.hw.mainkeys");
            boolean z = false;
            if ("1".equals(navBarOverride)) {
                sNeedAspectSettings = false;
            } else if ("0".equals(navBarOverride)) {
                sNeedAspectSettings = true;
            } else {
                sNeedAspectSettings = Resources.getSystem().getBoolean(R.bool.config_showNavigationBar);
            }
            if (sNeedAspectSettings && !Build.IS_TABLET) {
                z = true;
            }
            sNeedAspectSettings = z;
            sNeedAspectSettingsInited = true;
        }
        return sNeedAspectSettings;
    }

    public static void setRestrictAspect(String pkg, boolean restrict) {
        if (pkg != null && needAspectSettings()) {
            try {
                getService().setRestrictAspect(pkg, restrict);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Occur RemoteException when setRestrictAspect:");
                stringBuilder.append(pkg);
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                stringBuilder.append(restrict);
                Log.e("MiuiInit", stringBuilder.toString());
            }
        }
    }

    public static boolean isRestrictAspect(String pkg) {
        boolean z = false;
        if (pkg == null || !needAspectSettings()) {
            if (VERSION.SDK_INT >= 26) {
                z = true;
            }
            return z;
        }
        try {
            z = getService().isRestrictAspect(pkg);
            return z;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Occur RemoteException when isRestrictAspect:");
            stringBuilder.append(pkg);
            Log.e("MiuiInit", stringBuilder.toString());
            return z;
        }
    }

    public static float getAspectRatio(String pkg) {
        float f = 3.0f;
        if (pkg == null || !needAspectSettings()) {
            return 3.0f;
        }
        try {
            f = getService().getAspectRatio(pkg);
            return f;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Occur RemoteException when getAspectRatio:");
            stringBuilder.append(pkg);
            Log.e("MiuiInit", stringBuilder.toString());
            return f;
        }
    }

    public static int getDefaultAspectType(String pkg) {
        int i = 0;
        if (pkg == null || !needAspectSettings()) {
            return 0;
        }
        try {
            i = getService().getDefaultAspectType(pkg);
            return i;
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Occur RemoteException when getDefaultAspectType:");
            stringBuilder.append(pkg);
            Log.e("MiuiInit", stringBuilder.toString());
            return i;
        }
    }

    public static int getNotchConfig(String pkg) {
        int i = 0;
        if (pkg == null || !CustomizeUtil.HAS_NOTCH) {
            return 0;
        }
        try {
            i = getService().getNotchConfig(pkg);
            return i;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Occur RemoteException when getNotchConfig:");
            stringBuilder.append(pkg);
            Log.e("MiuiInit", stringBuilder.toString());
            return i;
        }
    }

    public static void setNotchSpecialMode(String pkg, boolean special) {
        if (pkg != null && CustomizeUtil.HAS_NOTCH) {
            try {
                getService().setNotchSpecialMode(pkg, special);
            } catch (RemoteException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Occur RemoteException when setNotchSpecailMode:");
                stringBuilder.append(pkg);
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                stringBuilder.append(special);
                Log.e("MiuiInit", stringBuilder.toString());
            }
        }
    }

    public static void setCutoutMode(String pkg, int mode) {
        if (!TextUtils.isEmpty(pkg) && CustomizeUtil.HAS_NOTCH) {
            try {
                getService().setCutoutMode(pkg, mode);
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Occur Exception when setCutoutMode:");
                stringBuilder.append(pkg);
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                stringBuilder.append(mode);
                Log.e("MiuiInit", stringBuilder.toString());
            }
        }
    }

    public static int getCutoutMode(String pkg) {
        if (TextUtils.isEmpty(pkg) || !CustomizeUtil.HAS_NOTCH) {
            return 0;
        }
        try {
            return getService().getCutoutMode(pkg);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Occur Exception when getCutoutMode:");
            stringBuilder.append(pkg);
            Log.e("MiuiInit", stringBuilder.toString());
            return 0;
        }
    }
}
