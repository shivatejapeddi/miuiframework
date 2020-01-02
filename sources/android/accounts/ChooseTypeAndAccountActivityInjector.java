package android.accounts;

import android.content.Intent;

public class ChooseTypeAndAccountActivityInjector {
    public static void toMiuiChooseAccountTypeActivity(Intent intent) {
        intent.setClassName("com.miui.rom", "miui.accounts.MiuiChooseAccountTypeActivity");
    }
}
