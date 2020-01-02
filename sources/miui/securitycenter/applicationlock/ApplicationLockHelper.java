package miui.securitycenter.applicationlock;

import android.content.Context;
import android.security.MiuiLockPatternUtils;

public class ApplicationLockHelper {
    private Context mContext;
    private MiuiLockPatternUtils mLockPatternUtils;

    public ApplicationLockHelper(Context context) {
        this.mContext = context;
        this.mLockPatternUtils = new MiuiLockPatternUtils(context);
    }

    public boolean isVisiblePatternLock() {
        return this.mLockPatternUtils.isVisiblePatternEnabled(this.mContext.getUserId());
    }

    public long getLockoutAttempt() {
        return this.mLockPatternUtils.getLockoutAttemptDeadline(this.mContext.getUserId());
    }

    public long setLockoutAttempt() {
        return this.mLockPatternUtils.setLockoutAttemptDeadline(this.mContext.getUserId(), 30000);
    }

    public boolean checkLockPattern(String patternString) {
        return false;
    }

    public boolean saveLockPatternExists() {
        return this.mLockPatternUtils.savedMiuiLockPatternExists();
    }

    public void clearAppLock() {
        this.mLockPatternUtils.clearLock(null, this.mContext.getUserId());
    }
}
