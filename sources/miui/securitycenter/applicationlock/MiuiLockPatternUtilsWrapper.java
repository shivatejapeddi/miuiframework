package miui.securitycenter.applicationlock;

import android.content.Context;
import android.security.MiuiLockPatternUtils;
import miui.security.SecurityManager;

public class MiuiLockPatternUtilsWrapper {
    private MiuiLockPatternUtils mLockPatternUtils;
    private SecurityManager mSecurityManager;

    public MiuiLockPatternUtilsWrapper(Context context) {
        this.mLockPatternUtils = new MiuiLockPatternUtils(context);
        this.mSecurityManager = (SecurityManager) context.getSystemService(Context.SECURITY_SERVICE);
    }

    public boolean isTactileFeedbackEnabled() {
        return this.mLockPatternUtils.isTactileFeedbackEnabled();
    }

    public boolean savedMiuiLockPatternExists() {
        return this.mSecurityManager.haveAccessControlPassword();
    }

    public void clearLockoutAttemptDeadline() {
        this.mLockPatternUtils.clearLockoutAttemptDeadline();
    }

    public boolean checkMiuiLockPattern(String patternString) {
        return false;
    }

    public void saveMiuiLockPattern(String patternString) {
    }
}
