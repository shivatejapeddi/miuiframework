package miui.securitycenter.applicationlock;

import android.content.Context;
import android.security.ChooseLockSettingsHelper;

public class ChooserLockSettingsHelperWrapper {
    private ChooseLockSettingsHelper mChooseLockSettingsHelper;
    private MiuiLockPatternUtilsWrapper mMiuiLockPatternUtilsWrapper;

    public ChooserLockSettingsHelperWrapper(Context context) {
        this.mMiuiLockPatternUtilsWrapper = new MiuiLockPatternUtilsWrapper(context);
        this.mChooseLockSettingsHelper = new ChooseLockSettingsHelper(context);
    }

    public MiuiLockPatternUtilsWrapper utils() {
        return this.mMiuiLockPatternUtilsWrapper;
    }

    public boolean isACLockEnabled() {
        return this.mMiuiLockPatternUtilsWrapper.savedMiuiLockPatternExists();
    }

    public void setACLockEnabled(boolean enabled) {
        this.mChooseLockSettingsHelper.setACLockEnabled(enabled);
    }

    public void setPasswordForPrivacyModeEnabled(boolean enabled) {
        this.mChooseLockSettingsHelper.setPasswordForPrivacyModeEnabled(enabled);
    }

    public void setPrivacyModeEnabled(boolean enabled) {
        this.mChooseLockSettingsHelper.setPrivacyModeEnabled(enabled);
    }
}
