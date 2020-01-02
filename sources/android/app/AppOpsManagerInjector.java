package android.app;

import android.provider.BrowserContract;
import android.util.ArraySet;
import miui.app.ToggleManager;
import miui.os.Build;
import miui.provider.ExtraGuard;
import miui.security.SecurityManager;

public class AppOpsManagerInjector {
    private static ArraySet<String> sAutoStartRestrictions = new ArraySet();

    static {
        sAutoStartRestrictions.add("com.android.fileexplorer");
        sAutoStartRestrictions.add("com.android.thememanager");
        sAutoStartRestrictions.add(BrowserContract.AUTHORITY);
        sAutoStartRestrictions.add("com.android.soundrecorder");
        sAutoStartRestrictions.add("com.android.calculator2");
        sAutoStartRestrictions.add("com.android.camera");
        sAutoStartRestrictions.add("com.android.quicksearchbox");
        sAutoStartRestrictions.add("com.android.providers.downloads.ui");
        sAutoStartRestrictions.add("com.android.email");
        sAutoStartRestrictions.add("com.android.midrive");
        sAutoStartRestrictions.add("com.miui.mipub");
        sAutoStartRestrictions.add("com.miui.video");
        sAutoStartRestrictions.add("com.miui.cleanmaster");
        sAutoStartRestrictions.add("com.miui.varcodescanner");
        sAutoStartRestrictions.add("com.miui.compass");
        sAutoStartRestrictions.add("com.miui.voiceassist");
        sAutoStartRestrictions.add("com.miui.yellowpage");
        sAutoStartRestrictions.add("com.miui.personalassistant");
        sAutoStartRestrictions.add("com.miui.backup");
        sAutoStartRestrictions.add("com.miui.notes");
        sAutoStartRestrictions.add("com.miui.translation.kingsoft");
        sAutoStartRestrictions.add("com.miui.home.launcher.assistant");
        sAutoStartRestrictions.add("com.miui.fm");
        sAutoStartRestrictions.add("com.mi.misupport");
        sAutoStartRestrictions.add("com.xiaomi.scanner");
        sAutoStartRestrictions.add("com.xiaomi.vip");
        sAutoStartRestrictions.add("com.xiaomi.pass");
        sAutoStartRestrictions.add("com.xiaomi.account");
        sAutoStartRestrictions.add("com.xiaomi.pricecheck");
        sAutoStartRestrictions.add("com.xiaomi.gamecenter");
        sAutoStartRestrictions.add("com.xiaomi.gamecenter.sdk.service");
        sAutoStartRestrictions.add(ToggleManager.PKG_NAME_MIDROP);
        sAutoStartRestrictions.add("com.xiaomi.miplay");
        sAutoStartRestrictions.add("com.cleanmaster.sdk");
        sAutoStartRestrictions.add("com.sohu.inputmethod.sogou.xiaomi");
        sAutoStartRestrictions.add("com.miui.securityadd");
        sAutoStartRestrictions.add(ExtraGuard.DEFAULT_PACKAGE_NAME);
        sAutoStartRestrictions.add(SecurityManager.SKIP_INTERCEPT_PACKAGE);
        sAutoStartRestrictions.add("com.miui.player");
        sAutoStartRestrictions.add("com.miui.virtualsim");
        if (Build.IS_TABLET) {
            sAutoStartRestrictions.clear();
        }
    }

    public static boolean isAutoStartRestriction(String pkg) {
        return sAutoStartRestrictions.contains(pkg);
    }

    public static int opToDefaultMode(int op) {
        if (op == 10008 || op == 10019) {
            return 1;
        }
        if (op != 10025) {
            return 0;
        }
        return 5;
    }
}
