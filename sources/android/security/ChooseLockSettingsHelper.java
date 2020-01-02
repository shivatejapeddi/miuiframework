package android.security;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.UserHandle;
import android.provider.MiuiSettings;
import android.provider.Settings.Secure;
import com.miui.internal.search.SettingsTree;

public final class ChooseLockSettingsHelper {
    public static final String BG_COLOR = "com.android.settings.bgColor";
    public static final int DISABLE_ACCESS_CONTROL = 1;
    public static final int DISABLE_AC_FOR_PRIVACY_MODE = 3;
    public static final int DISABLE_PRIVACY_MODE = 4;
    public static final int ENABLE_AC_FOR_PRIVACY_MODE = 2;
    public static final String EXTRA_CONFIRM_PURPOSE = "confirm_purpose";
    public static final String EXTRA_KEY_PASSWORD = "password";
    public static final String FOOTER_TEXT = "com.android.settings.ConfirmLockPattern.footer";
    public static final String FOOTER_TEXT_COLOR = "com.android.settings.footerTextColor";
    public static final String FOOTER_WRONG_TEXT = "com.android.settings.ConfirmLockPattern.footer_wrong";
    public static final String FORGET_PATTERN_COLOR = "com.android.settings.forgetPatternColor";
    public static final String HEADER_TEXT = "com.android.settings.ConfirmLockPattern.header";
    public static final String HEADER_WRONG_TEXT = "com.android.settings.ConfirmLockPattern.header_wrong";
    public static final String LOCK_BTN_WHITE = "com.android.settings.lockBtnWhite";
    public static final int LOCK_SETTINGS_TYPE_AC = 0;
    public static final int LOCK_SETTINGS_TYPE_GALLERY = 2;
    public static final int LOCK_SETTINGS_TYPE_SMS = 1;
    private static final int NO_REQUEST_FOR_ACTIVITY_RESULT = -1024;
    public static final String SHOW_FORGET_PASSWORD = "com.android.settings.forgetPassword";
    public static final String TITLE_COLOR = "com.android.settings.titleColor";
    public static final String USERID_TO_CONFIRM_PASSWORD = "com.android.settings.userIdToConfirm";
    private Activity mActivity;
    private Context mContext;
    private Fragment mFragment;
    private MiuiLockPatternUtils mLockPatternUtils;

    public ChooseLockSettingsHelper(Activity activity) {
        this((Context) activity);
        this.mActivity = activity;
    }

    public ChooseLockSettingsHelper(Context context) {
        this.mContext = context;
        this.mLockPatternUtils = new MiuiLockPatternUtils(this.mContext);
    }

    public ChooseLockSettingsHelper(Activity activity, Fragment fragment) {
        this(activity);
        this.mFragment = fragment;
    }

    public ChooseLockSettingsHelper(Activity activity, int type) {
        this.mActivity = activity;
        this.mContext = activity;
        this.mLockPatternUtils = new MiuiLockPatternUtils(this.mContext, type);
    }

    public MiuiLockPatternUtils utils() {
        return this.mLockPatternUtils;
    }

    public boolean launchConfirmationActivity(int request, CharSequence message, CharSequence details) {
        if (this.mActivity == null) {
            return false;
        }
        MiuiLockPatternUtils miuiLockPatternUtils = this.mLockPatternUtils;
        if (miuiLockPatternUtils == null) {
            return false;
        }
        boolean launched = false;
        int keyguardStoredPasswordQuality = miuiLockPatternUtils.getKeyguardStoredPasswordQuality(miuiLockPatternUtils.getCurrentOrCallingUserId());
        if (keyguardStoredPasswordQuality == 65536) {
            launched = confirmPattern(request, message, details);
        } else if (keyguardStoredPasswordQuality == 131072 || keyguardStoredPasswordQuality == 262144 || keyguardStoredPasswordQuality == 327680 || keyguardStoredPasswordQuality == 393216) {
            launched = confirmPassword(request);
        }
        return launched;
    }

    public boolean launchConfirmationActivity(CharSequence message, CharSequence details) {
        boolean launched = false;
        MiuiLockPatternUtils miuiLockPatternUtils = this.mLockPatternUtils;
        if (miuiLockPatternUtils == null) {
            return false;
        }
        int keyguardStoredPasswordQuality = miuiLockPatternUtils.getKeyguardStoredPasswordQuality(miuiLockPatternUtils.getCurrentOrCallingUserId());
        if (keyguardStoredPasswordQuality == 65536) {
            launched = confirmPattern(NO_REQUEST_FOR_ACTIVITY_RESULT, message, details);
        } else if (keyguardStoredPasswordQuality == 131072 || keyguardStoredPasswordQuality == 262144 || keyguardStoredPasswordQuality == 327680 || keyguardStoredPasswordQuality == 393216) {
            launched = confirmPassword(NO_REQUEST_FOR_ACTIVITY_RESULT);
        }
        return launched;
    }

    private boolean confirmPattern(int request, CharSequence message, CharSequence details) {
        MiuiLockPatternUtils miuiLockPatternUtils = this.mLockPatternUtils;
        if (!miuiLockPatternUtils.isLockPatternEnabled(miuiLockPatternUtils.getCurrentOrCallingUserId()) || !this.mLockPatternUtils.savedPatternExists()) {
            return false;
        }
        Intent intent = new Intent();
        intent.putExtra(HEADER_TEXT, message);
        intent.putExtra(FOOTER_TEXT, details);
        intent.setClassName(SettingsTree.SETTINGS_PACKAGE, "com.android.settings.ConfirmLockPattern");
        if (request == NO_REQUEST_FOR_ACTIVITY_RESULT) {
            intent.setFlags(268435456);
            this.mContext.startActivity(intent);
        } else {
            Fragment fragment = this.mFragment;
            if (fragment != null) {
                fragment.startActivityForResult(intent, request);
            } else {
                this.mActivity.startActivityForResult(intent, request);
            }
        }
        return true;
    }

    private boolean confirmPassword(int request) {
        MiuiLockPatternUtils miuiLockPatternUtils = this.mLockPatternUtils;
        if (!miuiLockPatternUtils.isLockPasswordEnabled(miuiLockPatternUtils.getCurrentOrCallingUserId())) {
            return false;
        }
        Intent intent = new Intent();
        intent.setClassName(SettingsTree.SETTINGS_PACKAGE, "com.android.settings.ConfirmLockPassword");
        if (request == NO_REQUEST_FOR_ACTIVITY_RESULT) {
            intent.setFlags(268435456);
            this.mContext.startActivity(intent);
        } else {
            Fragment fragment = this.mFragment;
            if (fragment != null) {
                fragment.startActivityForResult(intent, request);
            } else {
                this.mActivity.startActivityForResult(intent, request);
            }
        }
        return true;
    }

    public boolean isPrivacyModeEnabled() {
        return 1 == Secure.getInt(this.mContext.getContentResolver(), "privacy_mode_enabled", 0);
    }

    public void setPrivacyModeEnabled(boolean enabled) {
        Secure.putInt(this.mContext.getContentResolver(), "privacy_mode_enabled", enabled);
    }

    public boolean isACLockEnabled() {
        if (1 == Secure.getInt(this.mContext.getContentResolver(), MiuiSettings.Secure.ACCESS_CONTROL_LOCK_ENABLED, 0) && this.mLockPatternUtils.savedMiuiLockPatternExists()) {
            return true;
        }
        return false;
    }

    public boolean isPrivateSmsEnabled() {
        if (1 == Secure.getInt(this.mContext.getContentResolver(), MiuiSettings.Secure.PRIVATE_SMS_LOCK_ENABLED, 0) && this.mLockPatternUtils.savedMiuiLockPatternExists()) {
            return true;
        }
        return false;
    }

    public boolean isPrivateGalleryEnabled() {
        if (1 == Secure.getInt(this.mContext.getContentResolver(), MiuiSettings.Secure.PRIVATE_GALLERY_LOCK_ENABLED, 0) && this.mLockPatternUtils.savedMiuiLockPatternExists()) {
            return true;
        }
        return false;
    }

    public void setACLockEnabled(boolean enabled) {
        Secure.putInt(this.mContext.getContentResolver(), MiuiSettings.Secure.ACCESS_CONTROL_LOCK_ENABLED, enabled);
        if (!enabled) {
            this.mLockPatternUtils.saveMiuiLockPattern(null);
        }
    }

    public void setPrivateSmsEnabled(boolean enabled) {
        setPrivateSmsEnabledAsUser(enabled, UserHandle.myUserId());
    }

    public void setPrivateSmsEnabledAsUser(boolean enabled, int userId) {
        Secure.putIntForUser(this.mContext.getContentResolver(), MiuiSettings.Secure.PRIVATE_SMS_LOCK_ENABLED, enabled, userId);
    }

    public void setPrivateGalleryEnabled(boolean enabled) {
        setPrivateGalleryEnabledAsUser(enabled, UserHandle.myUserId());
    }

    public void setPrivateGalleryEnabledAsUser(boolean enabled, int userId) {
        Secure.putIntForUser(this.mContext.getContentResolver(), MiuiSettings.Secure.PRIVATE_GALLERY_LOCK_ENABLED, enabled, userId);
    }

    public boolean isPasswordForPrivacyModeEnabled() {
        return 1 == Secure.getInt(this.mContext.getContentResolver(), MiuiSettings.Secure.PASSWORD_FOR_PRIVACYMODE, 0);
    }

    public void setPasswordForPrivacyModeEnabled(boolean enabled) {
        Secure.putInt(this.mContext.getContentResolver(), MiuiSettings.Secure.PASSWORD_FOR_PRIVACYMODE, enabled);
    }

    public void setPrivacyPasswordEnable(boolean enabled) {
        setPrivacyPasswordEnabledAsUser(enabled, UserHandle.myUserId());
    }

    public void setPrivacyPasswordEnabledAsUser(boolean enabled, int userId) {
        Secure.putIntForUser(this.mContext.getContentResolver(), MiuiSettings.Secure.PRIVACY_PASSWORD_IS_OPEN, enabled, userId);
    }

    public boolean isPrivacyPasswordEnabled() {
        return 1 == Secure.getInt(this.mContext.getContentResolver(), MiuiSettings.Secure.PRIVACY_PASSWORD_IS_OPEN, 0);
    }
}
