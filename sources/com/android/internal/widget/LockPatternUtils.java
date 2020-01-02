package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;
import android.app.admin.DevicePolicyManager;
import android.app.admin.PasswordMetrics;
import android.app.trust.IStrongAuthTracker.Stub;
import android.app.trust.TrustManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.IStorageManager;
import android.os.storage.StorageManager;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.LockPatternView.Cell;
import com.android.server.LocalServices;
import com.google.android.collect.Lists;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import libcore.util.HexEncoding;
import miui.securityspace.XSpaceUserHandle;

public class LockPatternUtils {
    @Deprecated
    public static final String BIOMETRIC_WEAK_EVER_CHOSEN_KEY = "lockscreen.biometricweakeverchosen";
    public static final int CREDENTIAL_TYPE_NONE = -1;
    public static final int CREDENTIAL_TYPE_PASSWORD = 2;
    public static final int CREDENTIAL_TYPE_PATTERN = 1;
    public static final String DISABLE_LOCKSCREEN_KEY = "lockscreen.disabled";
    private static final String ENABLED_TRUST_AGENTS = "lockscreen.enabledtrustagents";
    public static final int FAILED_ATTEMPTS_BEFORE_WIPE_GRACE = 5;
    public static final long FAILED_ATTEMPT_COUNTDOWN_INTERVAL_MS = 1000;
    private static final boolean FRP_CREDENTIAL_ENABLED = true;
    private static final String HISTORY_DELIMITER = ",";
    private static final String IS_TRUST_USUALLY_MANAGED = "lockscreen.istrustusuallymanaged";
    public static final String LEGACY_LOCK_PATTERN_ENABLED = "legacy_lock_pattern_enabled";
    @Deprecated
    public static final String LOCKOUT_PERMANENT_KEY = "lockscreen.lockedoutpermanently";
    @Deprecated
    public static final String LOCKSCREEN_BIOMETRIC_WEAK_FALLBACK = "lockscreen.biometric_weak_fallback";
    public static final String LOCKSCREEN_OPTIONS = "lockscreen.options";
    public static final String LOCKSCREEN_POWER_BUTTON_INSTANTLY_LOCKS = "lockscreen.power_button_instantly_locks";
    @Deprecated
    public static final String LOCKSCREEN_WIDGETS_ENABLED = "lockscreen.widgets_enabled";
    public static final String LOCK_PASSWORD_SALT_KEY = "lockscreen.password_salt";
    private static final String LOCK_SCREEN_DEVICE_OWNER_INFO = "lockscreen.device_owner_info";
    private static final String LOCK_SCREEN_OWNER_INFO = "lock_screen_owner_info";
    private static final String LOCK_SCREEN_OWNER_INFO_ENABLED = "lock_screen_owner_info_enabled";
    public static final int MIN_LOCK_PASSWORD_SIZE = 4;
    public static final int MIN_LOCK_PATTERN_SIZE = 4;
    public static final int MIN_PATTERN_REGISTER_FAIL = 4;
    public static final String PASSWORD_HISTORY_KEY = "lockscreen.passwordhistory";
    public static final String PASSWORD_LENGTH_KEY = "lockscreen.password_length";
    @Deprecated
    public static final String PASSWORD_TYPE_ALTERNATE_KEY = "lockscreen.password_type_alternate";
    public static final String PASSWORD_TYPE_KEY = "lockscreen.password_type";
    public static final String PATTERN_EVER_CHOSEN_KEY = "lockscreen.patterneverchosen";
    public static final String PROFILE_KEY_NAME_DECRYPT = "profile_key_name_decrypt_";
    public static final String PROFILE_KEY_NAME_ENCRYPT = "profile_key_name_encrypt_";
    public static final String SYNTHETIC_PASSWORD_ENABLED_KEY = "enable-sp";
    public static final String SYNTHETIC_PASSWORD_HANDLE_KEY = "sp-handle";
    public static final String SYNTHETIC_PASSWORD_KEY_PREFIX = "synthetic_password_";
    private static final String TAG = "LockPatternUtils";
    public static final int USER_FRP = -9999;
    @UnsupportedAppUsage
    private final ContentResolver mContentResolver;
    @UnsupportedAppUsage
    private final Context mContext;
    private DevicePolicyManager mDevicePolicyManager;
    private final Handler mHandler;
    private Boolean mHasSecureLockScreen;
    private ILockSettings mLockSettingsService;
    private final SparseLongArray mLockoutDeadlines = new SparseLongArray();
    private UserManager mUserManager;

    public interface CheckCredentialProgressCallback {
        void onEarlyMatched();
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CredentialType {
    }

    public interface EscrowTokenStateChangeCallback {
        void onEscrowTokenActivated(long j, int i);
    }

    public static final class RequestThrottledException extends Exception {
        private int mTimeoutMs;

        @UnsupportedAppUsage
        public RequestThrottledException(int timeoutMs) {
            this.mTimeoutMs = timeoutMs;
        }

        @UnsupportedAppUsage
        public int getTimeoutMs() {
            return this.mTimeoutMs;
        }
    }

    public static class StrongAuthTracker {
        private static final int ALLOWING_BIOMETRIC = 4;
        public static final int SOME_AUTH_REQUIRED_AFTER_USER_REQUEST = 4;
        public static final int STRONG_AUTH_NOT_REQUIRED = 0;
        public static final int STRONG_AUTH_REQUIRED_AFTER_BOOT = 1;
        public static final int STRONG_AUTH_REQUIRED_AFTER_DPM_LOCK_NOW = 2;
        public static final int STRONG_AUTH_REQUIRED_AFTER_LOCKOUT = 8;
        public static final int STRONG_AUTH_REQUIRED_AFTER_TIMEOUT = 16;
        public static final int STRONG_AUTH_REQUIRED_AFTER_USER_LOCKDOWN = 32;
        private final int mDefaultStrongAuthFlags;
        private final H mHandler;
        private final SparseIntArray mStrongAuthRequiredForUser;
        protected final Stub mStub;

        private class H extends Handler {
            static final int MSG_ON_STRONG_AUTH_REQUIRED_CHANGED = 1;

            public H(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    StrongAuthTracker.this.handleStrongAuthRequiredChanged(msg.arg1, msg.arg2);
                }
            }
        }

        @Retention(RetentionPolicy.SOURCE)
        public @interface StrongAuthFlags {
        }

        public StrongAuthTracker(Context context) {
            this(context, Looper.myLooper());
        }

        public StrongAuthTracker(Context context, Looper looper) {
            this.mStrongAuthRequiredForUser = new SparseIntArray();
            this.mStub = new Stub() {
                public void onStrongAuthRequiredChanged(int strongAuthFlags, int userId) {
                    StrongAuthTracker.this.mHandler.obtainMessage(1, strongAuthFlags, userId).sendToTarget();
                }
            };
            this.mHandler = new H(looper);
            this.mDefaultStrongAuthFlags = getDefaultFlags(context);
        }

        public static int getDefaultFlags(Context context) {
            return context.getResources().getBoolean(R.bool.config_strongAuthRequiredOnBoot);
        }

        public int getStrongAuthForUser(int userId) {
            return this.mStrongAuthRequiredForUser.get(userId, this.mDefaultStrongAuthFlags);
        }

        public boolean isTrustAllowedForUser(int userId) {
            return getStrongAuthForUser(userId) == 0;
        }

        public boolean isBiometricAllowedForUser(int userId) {
            return (getStrongAuthForUser(userId) & -5) == 0;
        }

        public void onStrongAuthRequiredChanged(int userId) {
        }

        /* Access modifiers changed, original: protected */
        public void handleStrongAuthRequiredChanged(int strongAuthFlags, int userId) {
            if (strongAuthFlags != getStrongAuthForUser(userId)) {
                if (strongAuthFlags == this.mDefaultStrongAuthFlags) {
                    this.mStrongAuthRequiredForUser.delete(userId);
                } else {
                    this.mStrongAuthRequiredForUser.put(userId, strongAuthFlags);
                }
                onStrongAuthRequiredChanged(userId);
            }
        }
    }

    public boolean isTrustUsuallyManaged(int userId) {
        if (this.mLockSettingsService instanceof ILockSettings.Stub) {
            boolean z = false;
            try {
                z = getLockSettings().getBoolean(IS_TRUST_USUALLY_MANAGED, false, userId);
                return z;
            } catch (RemoteException e) {
                return z;
            }
        }
        throw new IllegalStateException("May only be called by TrustManagerService. Use TrustManager.isTrustUsuallyManaged()");
    }

    public void setTrustUsuallyManaged(boolean managed, int userId) {
        try {
            getLockSettings().setBoolean(IS_TRUST_USUALLY_MANAGED, managed, userId);
        } catch (RemoteException e) {
        }
    }

    public void userPresent(int userId) {
        try {
            getLockSettings().userPresent(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public DevicePolicyManager getDevicePolicyManager() {
        if (this.mDevicePolicyManager == null) {
            this.mDevicePolicyManager = (DevicePolicyManager) this.mContext.getSystemService(Context.DEVICE_POLICY_SERVICE);
            if (this.mDevicePolicyManager == null) {
                Log.e(TAG, "Can't get DevicePolicyManagerService: is it running?", new IllegalStateException("Stack trace:"));
            }
        }
        return this.mDevicePolicyManager;
    }

    private UserManager getUserManager() {
        if (this.mUserManager == null) {
            this.mUserManager = UserManager.get(this.mContext);
        }
        return this.mUserManager;
    }

    private TrustManager getTrustManager() {
        TrustManager trust = (TrustManager) this.mContext.getSystemService(Context.TRUST_SERVICE);
        if (trust == null) {
            Log.e(TAG, "Can't get TrustManagerService: is it running?", new IllegalStateException("Stack trace:"));
        }
        return trust;
    }

    @UnsupportedAppUsage
    public LockPatternUtils(Context context) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        Looper looper = Looper.myLooper();
        this.mHandler = looper != null ? new Handler(looper) : null;
    }

    @VisibleForTesting
    @UnsupportedAppUsage
    public ILockSettings getLockSettings() {
        if (this.mLockSettingsService == null) {
            this.mLockSettingsService = ILockSettings.Stub.asInterface(ServiceManager.getService("lock_settings"));
        }
        return this.mLockSettingsService;
    }

    public int getRequestedMinimumPasswordLength(int userId) {
        return getDevicePolicyManager().getPasswordMinimumLength(null, userId);
    }

    public int getMaximumPasswordLength(int quality) {
        return getDevicePolicyManager().getPasswordMaximumLength(quality);
    }

    public int getRequestedPasswordQuality(int userId) {
        return getDevicePolicyManager().getPasswordQuality(null, userId);
    }

    private int getRequestedPasswordHistoryLength(int userId) {
        return getDevicePolicyManager().getPasswordHistoryLength(null, userId);
    }

    public int getRequestedPasswordMinimumLetters(int userId) {
        return getDevicePolicyManager().getPasswordMinimumLetters(null, userId);
    }

    public int getRequestedPasswordMinimumUpperCase(int userId) {
        return getDevicePolicyManager().getPasswordMinimumUpperCase(null, userId);
    }

    public int getRequestedPasswordMinimumLowerCase(int userId) {
        return getDevicePolicyManager().getPasswordMinimumLowerCase(null, userId);
    }

    public int getRequestedPasswordMinimumNumeric(int userId) {
        return getDevicePolicyManager().getPasswordMinimumNumeric(null, userId);
    }

    public int getRequestedPasswordMinimumSymbols(int userId) {
        return getDevicePolicyManager().getPasswordMinimumSymbols(null, userId);
    }

    public int getRequestedPasswordMinimumNonLetter(int userId) {
        return getDevicePolicyManager().getPasswordMinimumNonLetter(null, userId);
    }

    @UnsupportedAppUsage
    public void reportFailedPasswordAttempt(int userId) {
        if (userId != USER_FRP || !frpCredentialEnabled(this.mContext)) {
            getDevicePolicyManager().reportFailedPasswordAttempt(userId);
            getTrustManager().reportUnlockAttempt(false, userId);
        }
    }

    @UnsupportedAppUsage
    public void reportSuccessfulPasswordAttempt(int userId) {
        if (userId != USER_FRP || !frpCredentialEnabled(this.mContext)) {
            getDevicePolicyManager().reportSuccessfulPasswordAttempt(userId);
            getTrustManager().reportUnlockAttempt(true, userId);
        }
    }

    public void reportPasswordLockout(int timeoutMs, int userId) {
        if (userId != USER_FRP || !frpCredentialEnabled(this.mContext)) {
            getTrustManager().reportUnlockLockout(timeoutMs, userId);
        }
    }

    public int getCurrentFailedPasswordAttempts(int userId) {
        if (userId == USER_FRP && frpCredentialEnabled(this.mContext)) {
            return 0;
        }
        return getDevicePolicyManager().getCurrentFailedPasswordAttempts(userId);
    }

    public int getMaximumFailedPasswordsForWipe(int userId) {
        if (userId == USER_FRP && frpCredentialEnabled(this.mContext)) {
            return 0;
        }
        return getDevicePolicyManager().getMaximumFailedPasswordsForWipe(null, userId);
    }

    private byte[] verifyCredential(byte[] credential, int type, long challenge, int userId) throws RequestThrottledException {
        try {
            VerifyCredentialResponse response = getLockSettings().verifyCredential(credential, type, challenge, userId);
            if (response.getResponseCode() == 0) {
                return response.getPayload();
            }
            if (response.getResponseCode() != 1) {
                return null;
            }
            throw new RequestThrottledException(response.getTimeout());
        } catch (RemoteException e) {
            return null;
        }
    }

    private boolean checkCredential(byte[] credential, int type, int userId, CheckCredentialProgressCallback progressCallback) throws RequestThrottledException {
        try {
            VerifyCredentialResponse response = getLockSettings().checkCredential(credential, type, userId, wrapCallback(progressCallback));
            if (response.getResponseCode() == 0) {
                return true;
            }
            if (response.getResponseCode() != 1) {
                return false;
            }
            throw new RequestThrottledException(response.getTimeout());
        } catch (RemoteException e) {
            return false;
        }
    }

    public byte[] verifyPattern(List<Cell> pattern, long challenge, int userId) throws RequestThrottledException {
        throwIfCalledOnMainThread();
        return verifyCredential(patternToByteArray(pattern), 1, challenge, userId);
    }

    public boolean checkPattern(List<Cell> pattern, int userId) throws RequestThrottledException {
        return checkPattern(pattern, userId, null);
    }

    public boolean checkPattern(List<Cell> pattern, int userId, CheckCredentialProgressCallback progressCallback) throws RequestThrottledException {
        throwIfCalledOnMainThread();
        return checkCredential(patternToByteArray(pattern), 1, userId, progressCallback);
    }

    public byte[] verifyPassword(byte[] password, long challenge, int userId) throws RequestThrottledException {
        throwIfCalledOnMainThread();
        return verifyCredential(password, 2, challenge, userId);
    }

    public byte[] verifyTiedProfileChallenge(byte[] password, boolean isPattern, long challenge, int userId) throws RequestThrottledException {
        throwIfCalledOnMainThread();
        try {
            VerifyCredentialResponse response = getLockSettings().verifyTiedProfileChallenge(password, isPattern ? 1 : 2, challenge, userId);
            if (response.getResponseCode() == 0) {
                return response.getPayload();
            }
            if (response.getResponseCode() != 1) {
                return null;
            }
            throw new RequestThrottledException(response.getTimeout());
        } catch (RemoteException e) {
            return null;
        }
    }

    @UnsupportedAppUsage
    public boolean checkPassword(String password, int userId) throws RequestThrottledException {
        return checkPassword(password != null ? password.getBytes() : null, userId, null);
    }

    public boolean checkPassword(byte[] password, int userId) throws RequestThrottledException {
        return checkPassword(password, userId, null);
    }

    public boolean checkPassword(String password, int userId, CheckCredentialProgressCallback progressCallback) throws RequestThrottledException {
        byte[] passwordBytes = password != null ? password.getBytes() : null;
        throwIfCalledOnMainThread();
        return checkCredential(passwordBytes, 2, userId, progressCallback);
    }

    public boolean checkPassword(byte[] password, int userId, CheckCredentialProgressCallback progressCallback) throws RequestThrottledException {
        throwIfCalledOnMainThread();
        return checkCredential(password, 2, userId, progressCallback);
    }

    public boolean checkVoldPassword(int userId) {
        try {
            return getLockSettings().checkVoldPassword(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public byte[] getPasswordHistoryHashFactor(byte[] currentPassword, int userId) {
        try {
            return getLockSettings().getHashFactor(currentPassword, userId);
        } catch (RemoteException e) {
            Log.e(TAG, "failed to get hash factor", e);
            return null;
        }
    }

    public boolean checkPasswordHistory(byte[] passwordToCheck, byte[] hashFactor, int userId) {
        if (passwordToCheck == null || passwordToCheck.length == 0) {
            Log.e(TAG, "checkPasswordHistory: empty password");
            return false;
        }
        String passwordHistory = getString(PASSWORD_HISTORY_KEY, userId);
        if (TextUtils.isEmpty(passwordHistory)) {
            return false;
        }
        int passwordHistoryLength = getRequestedPasswordHistoryLength(userId);
        if (passwordHistoryLength == 0) {
            return false;
        }
        String legacyHash = legacyPasswordToHash(passwordToCheck, userId);
        String passwordHash = passwordToHistoryHash(passwordToCheck, hashFactor, userId);
        String[] history = passwordHistory.split(",");
        int i = 0;
        while (i < Math.min(passwordHistoryLength, history.length)) {
            if (history[i].equals(legacyHash) || history[i].equals(passwordHash)) {
                return true;
            }
            i++;
        }
        return false;
    }

    private boolean savedPatternExists(int userId) {
        try {
            return getLockSettings().havePattern(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    private boolean savedPasswordExists(int userId) {
        try {
            return getLockSettings().havePassword(userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isPatternEverChosen(int userId) {
        return getBoolean(PATTERN_EVER_CHOSEN_KEY, false, userId);
    }

    public void reportPatternWasChosen(int userId) {
        setBoolean(PATTERN_EVER_CHOSEN_KEY, true, userId);
    }

    @UnsupportedAppUsage
    public int getActivePasswordQuality(int userId) {
        int quality = getKeyguardStoredPasswordQuality(userId);
        if (isLockPasswordEnabled(quality, userId) || isLockPatternEnabled(quality, userId)) {
            return quality;
        }
        return 0;
    }

    public void resetKeyStore(int userId) {
        try {
            getLockSettings().resetKeyStore(userId);
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't reset keystore ");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
        }
    }

    public boolean clearLock(byte[] savedCredential, int userHandle) {
        return clearLock(savedCredential, userHandle, false);
    }

    public boolean clearLock(byte[] savedCredential, int userHandle, boolean allowUntrustedChange) {
        int currentQuality = getKeyguardStoredPasswordQuality(userHandle);
        setKeyguardStoredPasswordQuality(0, userHandle);
        try {
            getLockSettings().setLockCredential(null, -1, savedCredential, 0, userHandle, allowUntrustedChange);
            setLong(PASSWORD_LENGTH_KEY, 0, userHandle);
            if (userHandle == 0) {
                updateEncryptionPassword(1, null);
                setCredentialRequiredToDecrypt(false);
            }
            onAfterChangingPassword(userHandle);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to clear lock", e);
            setKeyguardStoredPasswordQuality(currentQuality, userHandle);
            return false;
        }
    }

    public void setLockScreenDisabled(boolean disable, int userId) {
        setBoolean("lockscreen.disabled", disable, userId);
    }

    @UnsupportedAppUsage
    public boolean isLockScreenDisabled(int userId) {
        boolean z = false;
        if (isSecure(userId)) {
            return false;
        }
        boolean disabledByDefault = this.mContext.getResources().getBoolean(R.bool.config_disableLockscreenByDefault);
        boolean isSystemUser = UserManager.isSplitSystemUser() && userId == 0;
        UserInfo userInfo = getUserManager().getUserInfo(userId);
        boolean isDemoUser = UserManager.isDeviceInDemoMode(this.mContext) && userInfo != null && userInfo.isDemo();
        if (getBoolean("lockscreen.disabled", false, userId) || ((disabledByDefault && !isSystemUser) || isDemoUser)) {
            z = true;
        }
        return z;
    }

    public boolean saveLockPattern(List<Cell> pattern, byte[] savedPattern, int userId) {
        return saveLockPattern(pattern, savedPattern, userId, false);
    }

    public boolean saveLockPattern(List<Cell> pattern, byte[] savedPattern, int userId, boolean allowUntrustedChange) {
        if (!hasSecureLockScreen()) {
            throw new UnsupportedOperationException("This operation requires the lock screen feature.");
        } else if (pattern == null || pattern.size() < 4) {
            throw new IllegalArgumentException("pattern must not be null and at least 4 dots long.");
        } else {
            byte[] bytePattern = patternToByteArray(pattern);
            int currentQuality = getKeyguardStoredPasswordQuality(userId);
            setKeyguardStoredPasswordQuality(65536, userId);
            try {
                getLockSettings().setLockCredential(bytePattern, 1, savedPattern, 65536, userId, allowUntrustedChange);
                if (userId == 0 && isDeviceEncryptionEnabled()) {
                    if (shouldEncryptWithCredentials(true)) {
                        updateEncryptionPassword(2, bytePattern);
                    } else {
                        clearEncryptionPassword();
                    }
                }
                reportPatternWasChosen(userId);
                onAfterChangingPassword(userId);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Couldn't save lock pattern", e);
                setKeyguardStoredPasswordQuality(currentQuality, userId);
                return false;
            }
        }
    }

    public void sanitizePassword() {
        try {
            getLockSettings().sanitizePassword();
        } catch (RemoteException re) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't sanitize password");
            stringBuilder.append(re);
            Log.e(TAG, stringBuilder.toString());
        }
    }

    private void updateCryptoUserInfo(int userId) {
        if (userId == 0) {
            String ownerInfo = isOwnerInfoEnabled(userId) ? getOwnerInfo(userId) : "";
            IBinder service = ServiceManager.getService("mount");
            String str = TAG;
            if (service == null) {
                Log.e(str, "Could not find the mount service to update the user info");
                return;
            }
            IStorageManager storageManager = IStorageManager.Stub.asInterface(service);
            try {
                Log.d(str, "Setting owner info");
                storageManager.setField(StorageManager.OWNER_INFO_KEY, ownerInfo);
            } catch (RemoteException e) {
                Log.e(str, "Error changing user info", e);
            }
        }
    }

    @UnsupportedAppUsage
    public void setOwnerInfo(String info, int userId) {
        setString("lock_screen_owner_info", info, userId);
        updateCryptoUserInfo(userId);
    }

    @UnsupportedAppUsage
    public void setOwnerInfoEnabled(boolean enabled, int userId) {
        setBoolean("lock_screen_owner_info_enabled", enabled, userId);
        updateCryptoUserInfo(userId);
    }

    @UnsupportedAppUsage
    public String getOwnerInfo(int userId) {
        return getString("lock_screen_owner_info", userId);
    }

    public boolean isOwnerInfoEnabled(int userId) {
        return getBoolean("lock_screen_owner_info_enabled", false, userId);
    }

    public void setDeviceOwnerInfo(String info) {
        if (info != null && info.isEmpty()) {
            info = null;
        }
        setString(LOCK_SCREEN_DEVICE_OWNER_INFO, info, 0);
    }

    public String getDeviceOwnerInfo() {
        return getString(LOCK_SCREEN_DEVICE_OWNER_INFO, 0);
    }

    public boolean isDeviceOwnerInfoEnabled() {
        return getDeviceOwnerInfo() != null;
    }

    private void updateEncryptionPassword(final int type, byte[] password) {
        if (!hasSecureLockScreen()) {
            throw new UnsupportedOperationException("This operation requires the lock screen feature.");
        } else if (isDeviceEncryptionEnabled()) {
            final IBinder service = ServiceManager.getService("mount");
            if (service == null) {
                Log.e(TAG, "Could not find the mount service to update the encryption password");
                return;
            }
            final String passwordString = password != null ? new String(password) : null;
            new AsyncTask<Void, Void, Void>() {
                /* Access modifiers changed, original: protected|varargs */
                public Void doInBackground(Void... dummy) {
                    try {
                        IStorageManager.Stub.asInterface(service).changeEncryptionPassword(type, passwordString);
                    } catch (RemoteException e) {
                        Log.e(LockPatternUtils.TAG, "Error changing encryption password", e);
                    }
                    return null;
                }
            }.execute((Object[]) new Void[0]);
        }
    }

    @Deprecated
    public boolean saveLockPassword(String password, String savedPassword, int requestedQuality, int userHandle) {
        byte[] savedPasswordBytes = null;
        byte[] passwordBytes = password != null ? password.getBytes() : null;
        if (savedPassword != null) {
            savedPasswordBytes = savedPassword.getBytes();
        }
        return saveLockPassword(passwordBytes, savedPasswordBytes, requestedQuality, userHandle);
    }

    public boolean saveLockPassword(byte[] password, byte[] savedPassword, int requestedQuality, int userHandle) {
        return saveLockPassword(password, savedPassword, requestedQuality, userHandle, false);
    }

    public boolean saveLockPassword(byte[] password, byte[] savedPassword, int requestedQuality, int userHandle, boolean allowUntrustedChange) {
        byte[] bArr = password;
        int i = requestedQuality;
        int i2 = userHandle;
        if (!hasSecureLockScreen()) {
            throw new UnsupportedOperationException("This operation requires the lock screen feature.");
        } else if (bArr == null || bArr.length < 4) {
            throw new IllegalArgumentException("password must not be null and at least of length 4");
        } else if (i >= 131072) {
            int currentQuality = getKeyguardStoredPasswordQuality(i2);
            int passwordQuality = PasswordMetrics.computeForPassword(password).quality;
            setKeyguardStoredPasswordQuality(computeKeyguardQuality(2, i, passwordQuality), i2);
            try {
                getLockSettings().setLockCredential(password, 2, savedPassword, requestedQuality, userHandle, allowUntrustedChange);
                int quality = computeKeyguardQuality(2, i, passwordQuality);
                if (quality == 131072 || quality == 196608) {
                    setLong(PASSWORD_LENGTH_KEY, (long) bArr.length, i2);
                }
                updateEncryptionPasswordIfNeeded(bArr, passwordQuality, i2);
                updatePasswordHistory(bArr, i2);
                onAfterChangingPassword(i2);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Unable to save lock password", e);
                setKeyguardStoredPasswordQuality(currentQuality, i2);
                return false;
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("quality must be at least NUMERIC, but was ");
            stringBuilder.append(i);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    private int computeKeyguardQuality(int credentialType, int requestedQuality, int passwordQuality) {
        return credentialType == 2 ? Math.max(passwordQuality, requestedQuality) : passwordQuality;
    }

    private void updateEncryptionPasswordIfNeeded(byte[] password, int quality, int userHandle) {
        if (userHandle == 0 && isDeviceEncryptionEnabled()) {
            boolean numericComplex = true;
            if (shouldEncryptWithCredentials(true)) {
                int type = 0;
                boolean numeric = quality == 131072;
                if (quality != 196608) {
                    numericComplex = false;
                }
                if (numeric || numericComplex) {
                    type = 3;
                }
                updateEncryptionPassword(type, password);
                return;
            }
            clearEncryptionPassword();
        }
    }

    private void updatePasswordHistory(byte[] password, int userHandle) {
        String str = TAG;
        if (password == null || password.length == 0) {
            Log.e(str, "checkPasswordHistory: empty password");
            return;
        }
        String str2 = PASSWORD_HISTORY_KEY;
        String passwordHistory = getString(str2, userHandle);
        if (passwordHistory == null) {
            passwordHistory = "";
        }
        int passwordHistoryLength = getRequestedPasswordHistoryLength(userHandle);
        if (passwordHistoryLength == 0) {
            str = "";
        } else {
            String hash = passwordToHistoryHash(password, getPasswordHistoryHashFactor(password, userHandle), userHandle);
            if (hash == null) {
                Log.e(str, "Compute new style password hash failed, fallback to legacy style");
                hash = legacyPasswordToHash(password, userHandle);
            }
            if (TextUtils.isEmpty(passwordHistory)) {
                str = hash;
            } else {
                str = ",";
                String[] history = passwordHistory.split(str);
                StringJoiner joiner = new StringJoiner(str);
                joiner.add(hash);
                int i = 0;
                while (i < passwordHistoryLength - 1 && i < history.length) {
                    joiner.add(history[i]);
                    i++;
                }
                str = joiner.toString();
            }
        }
        setString(str2, str, userHandle);
    }

    @UnsupportedAppUsage
    public static boolean isDeviceEncryptionEnabled() {
        return StorageManager.isEncrypted();
    }

    public static boolean isFileEncryptionEnabled() {
        return StorageManager.isFileEncryptedNativeOrEmulated();
    }

    public void clearEncryptionPassword() {
        updateEncryptionPassword(1, null);
    }

    @UnsupportedAppUsage
    public int getKeyguardStoredPasswordQuality(int userHandle) {
        return (int) getLong(PASSWORD_TYPE_KEY, 0, userHandle);
    }

    private void setKeyguardStoredPasswordQuality(int quality, int userHandle) {
        setLong(PASSWORD_TYPE_KEY, (long) quality, userHandle);
    }

    public void setSeparateProfileChallengeEnabled(int userHandle, boolean enabled, byte[] managedUserPassword) {
        if (isManagedProfile(userHandle) || XSpaceUserHandle.isXSpaceUserId(userHandle)) {
            try {
                getLockSettings().setSeparateProfileChallengeEnabled(userHandle, enabled, managedUserPassword);
                onAfterChangingPassword(userHandle);
            } catch (RemoteException e) {
                Log.e(TAG, "Couldn't update work profile challenge enabled");
            }
        }
    }

    public boolean isSeparateProfileChallengeEnabled(int userHandle) {
        if (XSpaceUserHandle.isXSpaceUserId(userHandle)) {
            return hasSeparateChallenge(userHandle);
        }
        boolean z = isManagedProfile(userHandle) && hasSeparateChallenge(userHandle);
        return z;
    }

    public boolean isManagedProfileWithUnifiedChallenge(int userHandle) {
        return isManagedProfile(userHandle) && !hasSeparateChallenge(userHandle);
    }

    public boolean isSeparateProfileChallengeAllowed(int userHandle) {
        return isManagedProfile(userHandle) && getDevicePolicyManager().isSeparateProfileChallengeAllowed(userHandle);
    }

    public boolean isSeparateProfileChallengeAllowedToUnify(int userHandle) {
        if (getDevicePolicyManager().isProfileActivePasswordSufficientForParent(userHandle)) {
            if (!getUserManager().hasUserRestriction(UserManager.DISALLOW_UNIFIED_PASSWORD, UserHandle.of(userHandle))) {
                return true;
            }
        }
        return false;
    }

    private boolean hasSeparateChallenge(int userHandle) {
        try {
            return getLockSettings().getSeparateProfileChallengeEnabled(userHandle);
        } catch (RemoteException e) {
            Log.e(TAG, "Couldn't get separate profile challenge enabled");
            return false;
        }
    }

    private boolean isManagedProfile(int userHandle) {
        UserInfo info = getUserManager().getUserInfo(userHandle);
        return info != null && info.isManagedProfile();
    }

    @Deprecated
    public static List<Cell> stringToPattern(String string) {
        if (string == null) {
            return null;
        }
        return byteArrayToPattern(string.getBytes());
    }

    public static List<Cell> byteArrayToPattern(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        List<Cell> result = Lists.newArrayList();
        for (byte b : bytes) {
            byte b2 = (byte) (b2 - 49);
            result.add(Cell.of(b2 / 3, b2 % 3));
        }
        return result;
    }

    @Deprecated
    @UnsupportedAppUsage
    public static String patternToString(List<Cell> pattern) {
        return new String(patternToByteArray(pattern));
    }

    public static byte[] patternToByteArray(List<Cell> pattern) {
        if (pattern == null) {
            return new byte[0];
        }
        int patternSize = pattern.size();
        byte[] res = new byte[patternSize];
        for (int i = 0; i < patternSize; i++) {
            Cell cell = (Cell) pattern.get(i);
            res[i] = (byte) (((cell.getRow() * 3) + cell.getColumn()) + 49);
        }
        return res;
    }

    public static byte[] patternByteArrayToBaseZero(byte[] bytes) {
        if (bytes == null) {
            return new byte[0];
        }
        int patternSize = bytes.length;
        byte[] res = new byte[patternSize];
        for (int i = 0; i < patternSize; i++) {
            res[i] = (byte) (bytes[i] - 49);
        }
        return res;
    }

    @UnsupportedAppUsage
    public static byte[] patternToHash(List<Cell> pattern) {
        if (pattern == null) {
            return null;
        }
        int patternSize = pattern.size();
        byte[] res = new byte[patternSize];
        for (int i = 0; i < patternSize; i++) {
            Cell cell = (Cell) pattern.get(i);
            res[i] = (byte) ((cell.getRow() * 3) + cell.getColumn());
        }
        try {
            return MessageDigest.getInstance(KeyProperties.DIGEST_SHA1).digest(res);
        } catch (NoSuchAlgorithmException e) {
            return res;
        }
    }

    private String getSalt(int userId) {
        String str = LOCK_PASSWORD_SALT_KEY;
        long salt = getLong(str, 0, userId);
        if (salt == 0) {
            try {
                salt = SecureRandom.getInstance("SHA1PRNG").nextLong();
                setLong(str, salt, userId);
                String str2 = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Initialized lock password salt for user: ");
                stringBuilder.append(userId);
                Log.v(str2, stringBuilder.toString());
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("Couldn't get SecureRandom number", e);
            }
        }
        return Long.toHexString(salt);
    }

    public String legacyPasswordToHash(byte[] password, int userId) {
        if (password == null || password.length == 0) {
            return null;
        }
        try {
            byte[] salt = getSalt(userId).getBytes();
            byte[] saltedPassword = Arrays.copyOf(password, password.length + salt.length);
            System.arraycopy(salt, 0, saltedPassword, password.length, salt.length);
            byte[] sha1 = MessageDigest.getInstance(KeyProperties.DIGEST_SHA1).digest(saltedPassword);
            byte[] md5 = MessageDigest.getInstance(KeyProperties.DIGEST_MD5).digest(saltedPassword);
            byte[] combined = new byte[(sha1.length + md5.length)];
            System.arraycopy(sha1, 0, combined, 0, sha1.length);
            System.arraycopy(md5, 0, combined, sha1.length, md5.length);
            char[] hexEncoded = HexEncoding.encode(combined);
            Arrays.fill(saltedPassword, (byte) 0);
            return new String(hexEncoded);
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Missing digest algorithm: ", e);
        }
    }

    private String passwordToHistoryHash(byte[] passwordToHash, byte[] hashFactor, int userId) {
        if (passwordToHash == null || passwordToHash.length == 0 || hashFactor == null) {
            return null;
        }
        try {
            MessageDigest sha256 = MessageDigest.getInstance(KeyProperties.DIGEST_SHA256);
            sha256.update(hashFactor);
            byte[] salt = getSalt(userId).getBytes();
            byte[] saltedPassword = Arrays.copyOf(passwordToHash, passwordToHash.length + salt.length);
            System.arraycopy(salt, 0, saltedPassword, passwordToHash.length, salt.length);
            sha256.update(saltedPassword);
            Arrays.fill(saltedPassword, (byte) 0);
            return new String(HexEncoding.encode(sha256.digest()));
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError("Missing digest algorithm: ", e);
        }
    }

    @UnsupportedAppUsage
    public boolean isSecure(int userId) {
        int mode = getKeyguardStoredPasswordQuality(userId);
        return isLockPatternEnabled(mode, userId) || isLockPasswordEnabled(mode, userId);
    }

    @UnsupportedAppUsage
    public boolean isLockPasswordEnabled(int userId) {
        return isLockPasswordEnabled(getKeyguardStoredPasswordQuality(userId), userId);
    }

    private boolean isLockPasswordEnabled(int mode, int userId) {
        boolean passwordEnabled = mode == 262144 || mode == 131072 || mode == 196608 || mode == 327680 || mode == 393216 || mode == 524288;
        if (passwordEnabled && savedPasswordExists(userId)) {
            return true;
        }
        return false;
    }

    @UnsupportedAppUsage
    public boolean isLockPatternEnabled(int userId) {
        return isLockPatternEnabled(getKeyguardStoredPasswordQuality(userId), userId);
    }

    @Deprecated
    public boolean isLegacyLockPatternEnabled(int userId) {
        return getBoolean(LEGACY_LOCK_PATTERN_ENABLED, true, userId);
    }

    @Deprecated
    public void setLegacyLockPatternEnabled(int userId) {
        setBoolean("lock_pattern_autolock", true, userId);
    }

    private boolean isLockPatternEnabled(int mode, int userId) {
        return mode == 65536 && savedPatternExists(userId);
    }

    @UnsupportedAppUsage
    public boolean isVisiblePatternEnabled(int userId) {
        return getBoolean("lock_pattern_visible_pattern", false, userId);
    }

    public void setVisiblePatternEnabled(boolean enabled, int userId) {
        setBoolean("lock_pattern_visible_pattern", enabled, userId);
        if (userId == 0) {
            IBinder service = ServiceManager.getService("mount");
            String str = TAG;
            if (service == null) {
                Log.e(str, "Could not find the mount service to update the user info");
                return;
            }
            try {
                IStorageManager.Stub.asInterface(service).setField(StorageManager.PATTERN_VISIBLE_KEY, enabled ? "1" : "0");
            } catch (RemoteException e) {
                Log.e(str, "Error changing pattern visible state", e);
            }
        }
    }

    public boolean isVisiblePatternEverChosen(int userId) {
        return getString("lock_pattern_visible_pattern", userId) != null;
    }

    public void setVisiblePasswordEnabled(boolean enabled, int userId) {
        if (userId == 0) {
            IBinder service = ServiceManager.getService("mount");
            String str = TAG;
            if (service == null) {
                Log.e(str, "Could not find the mount service to update the user info");
                return;
            }
            try {
                IStorageManager.Stub.asInterface(service).setField(StorageManager.PASSWORD_VISIBLE_KEY, enabled ? "1" : "0");
            } catch (RemoteException e) {
                Log.e(str, "Error changing password visible state", e);
            }
        }
    }

    @UnsupportedAppUsage
    public boolean isTactileFeedbackEnabled() {
        return System.getIntForUser(this.mContentResolver, System.HAPTIC_FEEDBACK_ENABLED, 1, -2) != 0;
    }

    @UnsupportedAppUsage
    public long setLockoutAttemptDeadline(int userId, int timeoutMs) {
        long deadline = SystemClock.elapsedRealtime() + ((long) timeoutMs);
        if (userId == USER_FRP) {
            return deadline;
        }
        this.mLockoutDeadlines.put(userId, deadline);
        return deadline;
    }

    public long getLockoutAttemptDeadline(int userId) {
        long deadline = this.mLockoutDeadlines.get(userId, 0);
        if (deadline >= SystemClock.elapsedRealtime() || deadline == 0) {
            return deadline;
        }
        this.mLockoutDeadlines.put(userId, 0);
        return 0;
    }

    private boolean getBoolean(String secureSettingKey, boolean defaultValue, int userId) {
        try {
            return getLockSettings().getBoolean(secureSettingKey, defaultValue, userId);
        } catch (RemoteException e) {
            return defaultValue;
        }
    }

    private void setBoolean(String secureSettingKey, boolean enabled, int userId) {
        try {
            getLockSettings().setBoolean(secureSettingKey, enabled, userId);
        } catch (RemoteException re) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't write boolean ");
            stringBuilder.append(secureSettingKey);
            stringBuilder.append(re);
            Log.e(TAG, stringBuilder.toString());
        }
    }

    private long getLong(String secureSettingKey, long defaultValue, int userHandle) {
        try {
            return getLockSettings().getLong(secureSettingKey, defaultValue, userHandle);
        } catch (RemoteException e) {
            return defaultValue;
        }
    }

    @UnsupportedAppUsage
    private void setLong(String secureSettingKey, long value, int userHandle) {
        try {
            getLockSettings().setLong(secureSettingKey, value, userHandle);
        } catch (RemoteException re) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't write long ");
            stringBuilder.append(secureSettingKey);
            stringBuilder.append(re);
            Log.e(TAG, stringBuilder.toString());
        }
    }

    @UnsupportedAppUsage
    private String getString(String secureSettingKey, int userHandle) {
        String str = null;
        try {
            str = getLockSettings().getString(secureSettingKey, null, userHandle);
            return str;
        } catch (RemoteException e) {
            return str;
        }
    }

    @UnsupportedAppUsage
    private void setString(String secureSettingKey, String value, int userHandle) {
        try {
            getLockSettings().setString(secureSettingKey, value, userHandle);
        } catch (RemoteException re) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't write string ");
            stringBuilder.append(secureSettingKey);
            stringBuilder.append(re);
            Log.e(TAG, stringBuilder.toString());
        }
    }

    public void setPowerButtonInstantlyLocks(boolean enabled, int userId) {
        setBoolean(LOCKSCREEN_POWER_BUTTON_INSTANTLY_LOCKS, enabled, userId);
    }

    @UnsupportedAppUsage
    public boolean getPowerButtonInstantlyLocks(int userId) {
        return getBoolean(LOCKSCREEN_POWER_BUTTON_INSTANTLY_LOCKS, true, userId);
    }

    public boolean isPowerButtonInstantlyLocksEverChosen(int userId) {
        return getString(LOCKSCREEN_POWER_BUTTON_INSTANTLY_LOCKS, userId) != null;
    }

    public void setEnabledTrustAgents(Collection<ComponentName> activeTrustAgents, int userId) {
        StringBuilder sb = new StringBuilder();
        for (ComponentName cn : activeTrustAgents) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(cn.flattenToShortString());
        }
        setString(ENABLED_TRUST_AGENTS, sb.toString(), userId);
        getTrustManager().reportEnabledTrustAgentsChanged(userId);
    }

    public List<ComponentName> getEnabledTrustAgents(int userId) {
        String serialized = getString(ENABLED_TRUST_AGENTS, userId);
        if (TextUtils.isEmpty(serialized)) {
            return null;
        }
        String[] split = serialized.split(",");
        ArrayList<ComponentName> activeTrustAgents = new ArrayList(split.length);
        for (String s : split) {
            if (!TextUtils.isEmpty(s)) {
                activeTrustAgents.add(ComponentName.unflattenFromString(s));
            }
        }
        return activeTrustAgents;
    }

    public void requireCredentialEntry(int userId) {
        requireStrongAuth(4, userId);
    }

    public void requireStrongAuth(int strongAuthReason, int userId) {
        try {
            getLockSettings().requireStrongAuth(strongAuthReason, userId);
        } catch (RemoteException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error while requesting strong auth: ");
            stringBuilder.append(e);
            Log.e(TAG, stringBuilder.toString());
        }
    }

    private void onAfterChangingPassword(int userHandle) {
        getTrustManager().reportEnabledTrustAgentsChanged(userHandle);
    }

    public boolean isCredentialRequiredToDecrypt(boolean defaultValue) {
        int value = Global.getInt(this.mContentResolver, Global.REQUIRE_PASSWORD_TO_DECRYPT, -1);
        if (value == -1) {
            return defaultValue;
        }
        return value != 0;
    }

    public void setCredentialRequiredToDecrypt(boolean required) {
        if (!getUserManager().isSystemUser() && !getUserManager().isPrimaryUser()) {
            throw new IllegalStateException("Only the system or primary user may call setCredentialRequiredForDecrypt()");
        } else if (isDeviceEncryptionEnabled()) {
            Global.putInt(this.mContext.getContentResolver(), Global.REQUIRE_PASSWORD_TO_DECRYPT, required);
        }
    }

    private boolean isDoNotAskCredentialsOnBootSet() {
        return getDevicePolicyManager().getDoNotAskCredentialsOnBoot();
    }

    private boolean shouldEncryptWithCredentials(boolean defaultValue) {
        return isCredentialRequiredToDecrypt(defaultValue) && !isDoNotAskCredentialsOnBootSet();
    }

    private void throwIfCalledOnMainThread() {
        if (Looper.getMainLooper().isCurrentThread()) {
            throw new IllegalStateException("should not be called from the main thread.");
        }
    }

    public void registerStrongAuthTracker(StrongAuthTracker strongAuthTracker) {
        try {
            getLockSettings().registerStrongAuthTracker(strongAuthTracker.mStub);
        } catch (RemoteException e) {
            throw new RuntimeException("Could not register StrongAuthTracker");
        }
    }

    public void unregisterStrongAuthTracker(StrongAuthTracker strongAuthTracker) {
        try {
            getLockSettings().unregisterStrongAuthTracker(strongAuthTracker.mStub);
        } catch (RemoteException e) {
            Log.e(TAG, "Could not unregister StrongAuthTracker", e);
        }
    }

    public int getStrongAuthForUser(int userId) {
        try {
            return getLockSettings().getStrongAuthForUser(userId);
        } catch (RemoteException e) {
            Log.e(TAG, "Could not get StrongAuth", e);
            return StrongAuthTracker.getDefaultFlags(this.mContext);
        }
    }

    public boolean isTrustAllowedForUser(int userId) {
        return getStrongAuthForUser(userId) == 0;
    }

    public boolean isBiometricAllowedForUser(int userId) {
        return (getStrongAuthForUser(userId) & -5) == 0;
    }

    public boolean isUserInLockdown(int userId) {
        return getStrongAuthForUser(userId) == 32;
    }

    private ICheckCredentialProgressCallback wrapCallback(final CheckCredentialProgressCallback callback) {
        if (callback == null) {
            return null;
        }
        if (this.mHandler != null) {
            return new ICheckCredentialProgressCallback.Stub() {
                public void onCredentialVerified() throws RemoteException {
                    Handler access$000 = LockPatternUtils.this.mHandler;
                    CheckCredentialProgressCallback checkCredentialProgressCallback = callback;
                    Objects.requireNonNull(checkCredentialProgressCallback);
                    access$000.post(new -$$Lambda$gPQuiuEDuOmrh2MixBcV6a5gu5s(checkCredentialProgressCallback));
                }
            };
        }
        throw new IllegalStateException("Must construct LockPatternUtils on a looper thread to use progress callbacks.");
    }

    private LockSettingsInternal getLockSettingsInternal() {
        LockSettingsInternal service = (LockSettingsInternal) LocalServices.getService(LockSettingsInternal.class);
        if (service != null) {
            return service;
        }
        throw new SecurityException("Only available to system server itself");
    }

    public long addEscrowToken(byte[] token, int userId, EscrowTokenStateChangeCallback callback) {
        return getLockSettingsInternal().addEscrowToken(token, userId, callback);
    }

    public boolean removeEscrowToken(long handle, int userId) {
        return getLockSettingsInternal().removeEscrowToken(handle, userId);
    }

    public boolean isEscrowTokenActive(long handle, int userId) {
        return getLockSettingsInternal().isEscrowTokenActive(handle, userId);
    }

    public boolean setLockCredentialWithToken(byte[] credential, int type, int requestedQuality, long tokenHandle, byte[] token, int userId) {
        byte[] bArr = credential;
        int i = type;
        int i2 = userId;
        if (hasSecureLockScreen()) {
            LockSettingsInternal localService = getLockSettingsInternal();
            if (i != -1) {
                if (bArr == null || bArr.length < 4) {
                    throw new IllegalArgumentException("password must not be null and at least of length 4");
                }
                int quality = PasswordMetrics.computeForCredential(i, bArr).quality;
                if (!localService.setLockCredentialWithToken(credential, type, tokenHandle, token, computeKeyguardQuality(i, quality, requestedQuality), userId)) {
                    return false;
                }
                setKeyguardStoredPasswordQuality(quality, i2);
                if (quality == 131072 || quality == 196608) {
                    setLong(PASSWORD_LENGTH_KEY, (long) bArr.length, i2);
                }
                updateEncryptionPasswordIfNeeded(bArr, quality, i2);
                updatePasswordHistory(bArr, i2);
                onAfterChangingPassword(i2);
            } else if (bArr != null && bArr.length != 0) {
                throw new IllegalArgumentException("password must be emtpy for NONE type");
            } else if (!localService.setLockCredentialWithToken(null, -1, tokenHandle, token, 0, userId)) {
                return false;
            } else {
                setKeyguardStoredPasswordQuality(0, i2);
                if (i2 == 0) {
                    updateEncryptionPassword(1, null);
                    setCredentialRequiredToDecrypt(false);
                }
            }
            onAfterChangingPassword(i2);
            return true;
        }
        throw new UnsupportedOperationException("This operation requires the lock screen feature.");
    }

    public boolean unlockUserWithToken(long tokenHandle, byte[] token, int userId) {
        return getLockSettingsInternal().unlockUserWithToken(tokenHandle, token, userId);
    }

    public void enableSyntheticPassword() {
        setLong(SYNTHETIC_PASSWORD_ENABLED_KEY, 1, 0);
    }

    public void disableSyntheticPassword() {
        setLong(SYNTHETIC_PASSWORD_ENABLED_KEY, 0, 0);
    }

    public boolean isSyntheticPasswordEnabled() {
        return getLong(SYNTHETIC_PASSWORD_ENABLED_KEY, 0, 0) != 0;
    }

    public boolean hasPendingEscrowToken(int userId) {
        try {
            return getLockSettings().hasPendingEscrowToken(userId);
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return false;
        }
    }

    public boolean hasSecureLockScreen() {
        if (this.mHasSecureLockScreen == null) {
            this.mHasSecureLockScreen = Boolean.valueOf(this.mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_SECURE_LOCK_SCREEN));
        }
        return this.mHasSecureLockScreen.booleanValue();
    }

    public static boolean userOwnsFrpCredential(Context context, UserInfo info) {
        return info != null && info.isPrimary() && info.isAdmin() && frpCredentialEnabled(context);
    }

    public static boolean frpCredentialEnabled(Context context) {
        return context.getResources().getBoolean(R.bool.config_enableCredentialFactoryResetProtection);
    }

    public static byte[] charSequenceToByteArray(CharSequence chars) {
        if (chars == null) {
            return null;
        }
        byte[] bytes = new byte[chars.length()];
        for (int i = 0; i < chars.length(); i++) {
            bytes[i] = (byte) chars.charAt(i);
        }
        return bytes;
    }

    public long setKeyguardLockoutAttemptDeadline(int failedAttempts) {
        return -1;
    }

    public long getKeyguardLockoutAttemptDeadline(int failedAttempts) {
        return -1;
    }
}
