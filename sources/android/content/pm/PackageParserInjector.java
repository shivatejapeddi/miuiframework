package android.content.pm;

import android.text.TextUtils;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;
import miui.os.Build;
import miui.push.PushConstants;
import miui.security.SecurityManager;

public class PackageParserInjector {
    private static final String MIUI = "miui";
    private static final String TAG = "PackageParserInjector";
    private static String[] WCG_WHITE_LIST = new String[]{SecurityManager.SKIP_INTERCEPT_PACKAGE};
    private static final String XIAOMI = "xiaomi";
    private static List<String> sMiuiPersistentEnableList = new ArrayList();

    static {
        sMiuiPersistentEnableList.add("com.miui.rom");
        sMiuiPersistentEnableList.add("com.miui.daemon");
        sMiuiPersistentEnableList.add("com.miui.voicetrigger");
        sMiuiPersistentEnableList.add("com.miui.face");
        sMiuiPersistentEnableList.add("com.xiaomi.mircs");
        sMiuiPersistentEnableList.add(PushConstants.PUSH_SERVICE_PACKAGE_NAME);
        sMiuiPersistentEnableList.add("com.xiaomi.finddevice");
        if (!Build.IS_INTERNATIONAL_BUILD) {
            sMiuiPersistentEnableList.add("com.miui.contentcatcher");
        }
    }

    public static boolean isWCGWhiteList(String packageName) {
        for (String pkg : WCG_WHITE_LIST) {
            if (pkg.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    static void checkEnablePackagePersistent(ApplicationInfo ai) {
        String packageName = ai.packageName;
        if (!TextUtils.isEmpty(packageName)) {
            if ((packageName.toLowerCase().contains("miui") || packageName.toLowerCase().contains("xiaomi")) && !sMiuiPersistentEnableList.contains(packageName)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Permission Denied, Skip package: ");
                stringBuilder.append(packageName);
                stringBuilder.append(" to be persistent!");
                Slog.w(TAG, stringBuilder.toString());
                ai.flags &= -9;
            }
        }
    }
}
